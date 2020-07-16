
package com.hdntec.gestao.integracao.integracaoCRM;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hdntec.gestao.batch.integracaoCRM.IntegracaoSistemaCRM;
import com.hdntec.gestao.domain.integracao.ControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IControladorIntegracao;
import com.hdntec.gestao.domain.integracao.IntegracaoCargaCRM;
import com.hdntec.gestao.domain.integracao.IntegracaoItemControleCRM;
import com.hdntec.gestao.domain.integracao.IntegracaoNavioCRM;
import com.hdntec.gestao.domain.integracao.IntegracaoOrientEmbarqueCRM;
import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.plano.comparadores.ComparadorSituacoesPatio;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.navios.ControladorNavios;
import com.hdntec.gestao.integracao.navios.IControladorNavios;
import com.hdntec.gestao.integracao.plano.controladores.ControladorPlanoDeSituacoesDoPatio;
import com.hdntec.gestao.integracao.plano.controladores.IControladorPlanoDeSituacoesDoPatio;
import com.hdntec.gestao.integracao.produto.ControladorProduto;
import com.hdntec.gestao.integracao.produto.IControladorProduto;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;

/**
 * Interface com o sistema externo CRM. Essa interface é utilizada para buscar dados atualizados do CRM.
 * <p>
 * CRM é o sistema externo que armazena e envia para o sistema os dados de navios, programações de embarque e embarques realizados.
 * 
 * @author andre
 * 
 */
public class InterfaceCRM {

    /** Acesso ao subsistema Controlador do Plano de Empilhamento e Recuperacao */
    private IControladorPlanoDeSituacoesDoPatio controladorPlano;

    /** Acesso ao subsistema Controlador das Integracoes */
    private IControladorIntegracao controladorIntegracao;

    /** Acesso ao subsistema Navios */
    private IControladorNavios controladorNavios;

    /** Acesso ao subsistema Produtos */
    private IControladorProduto controladorProduto;

    /**Acesso ao subsistema de  */
    /**
     * Atualiza os dados do sistema externo CRM, a partir da ultimaAtualizacao e da situacao de patio com horario mais proximo à data da
     * ultimaAtualizacao
     */
    public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao,
                    PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao, Date dataSitucao)
                    throws IntegracaoNaoRealizadaException, ErroSistemicoException {
        try {
            initServices();
            // obtem os parametros do sistema CRM
            IntegracaoParametros paramIntegracao = controladorIntegracao.buscarParametroSistema(this.getIdSistemaCRM());

            // busca os novos navios para cadastrar na fila
            List<IntegracaoNavioCRM> listaNaviosParaFila = controladorIntegracao.buscarNovosNaviosParaFila(paramIntegracao
                            .getDataUltimaLeitura(), dataExecucaoAtualizacao);

            if (listaNaviosParaFila.size() == 0) {
                throw new IntegracaoNaoRealizadaException(PropertiesUtil.getMessage("mensagem.integracao.crm.mespatio"));
            }

            Collections.sort(planoEmpilhamentoRecuperacao.getListaSituacoesPatio(), new ComparadorSituacoesPatio());

            FilaDeNavios filaCadastrada = null;///situacaoPatio.getFilaDeNavios();
            // busca a lista de clientes cadastrados no sistema
            List<Cliente> listaClientesCadastrados = controladorNavios.buscaListaClientes();

            List<TipoProduto> listaTiposProdutoCadastrado = controladorProduto.buscarTiposProduto(new TipoProduto());
            List<MetaNavio> result = new ArrayList<MetaNavio>();
            // percorre a lista de novos navios e os adiciona na fila cadastrada
            for (IntegracaoNavioCRM integraNavio : listaNaviosParaFila) {
                result.add(insereNavioNaFila(filaCadastrada, integraNavio, listaClientesCadastrados,
                                listaTiposProdutoCadastrado, dataSitucao.getTime()));
            }

            // atualiza o status de processamento da integracao com CRM
            controladorIntegracao.atualizarNaviosIntegrados(listaNaviosParaFila);

            MetaNavioDAO dao = new MetaNavioDAO();
            dao.salvar(result);
            dao = null;

            // atualizando a data da ultima leitura
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataExecucaoAtualizacao);
            cal.add(Calendar.DAY_OF_MONTH, new Integer(PropertiesUtil
                            .buscarPropriedade("quantidade.dias.a.frente.busca.navios.fila"))
                            * -1);

            paramIntegracao.setDataUltimaLeitura(cal.getTime());
            controladorIntegracao.atualizarParametroSistema(paramIntegracao);
            controladorIntegracao = null;

        } catch (Exception e) {
            throw new ErroSistemicoException(e);
        }

        return planoEmpilhamentoRecuperacao;
    }

    /**Acesso ao subsistema de  */
    /**
     * Atualiza os dados do sistema externo CRM, a partir da ultimaAtualizacao e da situacao de patio com horario mais proximo à data da
     * ultimaAtualizacao 
     */
    public void atualizarNavio(MetaNavio metaNavio, long timeDefault) throws IntegracaoNaoRealizadaException,
                    ErroSistemicoException {
        try {
            initServices();

            IntegracaoSistemaCRM integracaoSistemaCRM = new IntegracaoSistemaCRM();
            try {
                integracaoSistemaCRM.atualizarDadosNavio(metaNavio.getSap_vbap_vbeln());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                throw new ErroSistemicoException(e);
            }
            // busca os novos navios para cadastrar na fila
            List<IntegracaoNavioCRM> listaNaviosParaFila = controladorIntegracao.buscarAtualizacaoNavio(metaNavio
                            .getSap_vbap_vbeln());

            if (listaNaviosParaFila.size() == 0) {
                throw new IntegracaoNaoRealizadaException(PropertiesUtil.getMessage("mensagem.integracao.crm.mespatio"));
            }

            // busca a lista de clientes cadastrados no sistema
            List<Cliente> listaClientesCadastrados = controladorNavios.buscaListaClientes();

            List<TipoProduto> listaTiposProdutoCadastrado = controladorProduto.buscarTiposProduto(new TipoProduto());
            List<MetaNavio> result = new ArrayList<MetaNavio>();
            // percorre a lista de novos navios e os adiciona na fila cadastrada
            for (IntegracaoNavioCRM integraNavio : listaNaviosParaFila) {
                result.add(atualizarDados(metaNavio, integraNavio, listaClientesCadastrados, listaTiposProdutoCadastrado,
                                timeDefault));
            }

            // atualiza o status de processamento da integracao com CRM
            controladorIntegracao.atualizarNaviosIntegrados(listaNaviosParaFila);

            MetaNavioDAO dao = new MetaNavioDAO();
            dao.salvar(result);
            dao = null;
            integracaoSistemaCRM = null;

        } catch (Exception e) {
            throw new ErroSistemicoException(e);
        }

    }

    /**
    * Metodo auxiliar que busca um cliente pelo codigo em uma lista de clientes
    * @param listaClientes
    * @param codigoCliente
    * @return
    */
    private Cliente buscarClienteCadastrado(List<Cliente> listaClientes, String codigoCliente) {
        Cliente clientePesquisado = null;
        for (Cliente cliente : listaClientes) {
            if (cliente.getCodigoCliente().equals(codigoCliente)) {
                clientePesquisado = cliente;
                break;
            }
        }
        return clientePesquisado;
    }

    /**
     * Metodo auxiliar que cria uma orientacao de embarque atraves de uma carga liga do
     * sistema CRM
     * @param cargaCRM
     * @return
     */
    private OrientacaoDeEmbarque criaOrientacaoEmbarqueCarga(IntegracaoCargaCRM cargaCRM, Carga cargaMesPatio)
                    throws ErroSistemicoException {

        OrientacaoDeEmbarque oe = null;

        if (cargaCRM.getOrientacaoEmbarque() != null) {
            oe = new OrientacaoDeEmbarque();
            oe.setIdUser(1L);
            oe.setDtInsert(cargaMesPatio.getDtInsert());
            oe.setDtInicio(cargaMesPatio.getDtInicio());
            oe.setPenalizacao(Boolean.FALSE);
            oe.setCarga(cargaMesPatio);
            oe.setListaItemDeControle(criaListaItensControleOrientacaoEmbarque(cargaCRM.getOrientacaoEmbarque(),cargaMesPatio.getProduto().getTipoProduto()));
            oe.setQuantidadeNecessaria(cargaCRM.getQuantidadeCarga());
            if (cargaMesPatio != null && cargaMesPatio.getProduto() != null) {
                oe.setTipoProduto(cargaMesPatio.getProduto().getTipoProduto());
            }
        }
        return oe;
    }

    /**
     * Metodo auxiliar que cria uma lista de itens de controle usando a orientacao de emabarque
     * liga do sistema CRM
     * @param integraOE
     * @return
     */
    private List<ItemDeControle> criaListaItensControleOrientacaoEmbarque(IntegracaoOrientEmbarqueCRM integraOE,TipoProduto tipoProduto)
                    throws ErroSistemicoException {

        List<ItemDeControle> listaItensControleOrientacaoEmbarque = new ArrayList<ItemDeControle>();

        // ... obtem a lista com os tipos de item de controle cadastrados
        List<TipoItemDeControle> listaTipoItemsControle = controladorProduto.buscarTiposItemControle();

        // ... mapa com os itens de controle existentes
        HashMap<String, TipoItemDeControle> mapaTipoItemControle = new HashMap<String, TipoItemDeControle>();
        for (TipoItemDeControle itemControleMesPatio : listaTipoItemsControle) {
            
                if (itemControleMesPatio.getAreaRespEDPelota() != null ) {
                    mapaTipoItemControle.put(itemControleMesPatio.getDescricaoTipoItemControle().trim()
                                + itemControleMesPatio.getCdTipoItemControlePelota()
                                + itemControleMesPatio.getTipoProcessoPelota() + itemControleMesPatio.getAreaRespEDPelota(),
                                itemControleMesPatio);
                }    
                if (itemControleMesPatio.getAreaRespEDPelletFeed() != null ) {
                        mapaTipoItemControle.put(itemControleMesPatio.getDescricaoTipoItemControle().trim()
                                    + itemControleMesPatio.getCdTipoItemControlePelletFeed()
                                    + itemControleMesPatio.getTipoProcessoPelletFeed() + itemControleMesPatio.getAreaRespEDPelletFeed(),
                                    itemControleMesPatio);
                 }     
        }

        for (IntegracaoItemControleCRM item : integraOE.getListaItensControleCRM()) {
            ItemDeControle ic = new ItemDeControleOrientacaoEmbarque();
            ic.setLimSupMetaOrientacaoEmb(item.getValorGarantidoMaximo());
            ic.setLimInfMetaOrientacaoEmb(item.getValorGarantidoMinimo());
            ic.setValor(null);
            ic.setIdUser(1l);
            ic.setDtInicio(new Date(System.currentTimeMillis()));

            String key = null;
            if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA)) {
                key = item.getDescricaoTipoItemControle().trim() + item.getCdTipoItemControlePelota() + item.getTipoProcessoPelota() + item.getAreaRespEDPelota();
            } else if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED)) {    
                key = item.getDescricaoTipoItemControle().trim() + item.getCdTipoItemControlePelletFeed() + item.getTipoProcessoPelletFeed() + item.getAreaRespEDPelletFeed();
            }   
                // procura no mapa de tipo de item de controle o item da orientacao
            TipoItemDeControle tipoItemCadastrado = mapaTipoItemControle.get(key);

            if (tipoItemCadastrado != null) {
                ic.setTipoItemControle(tipoItemCadastrado);
                listaItensControleOrientacaoEmbarque.add(ic);
            }
        }

        return listaItensControleOrientacaoEmbarque;
    }

    public Long getIdSistemaCRM() {
        return new Long(2);
    }

    public IControladorIntegracao getControladorIntegracao() {
        return controladorIntegracao;
    }

    public void setControladorIntegracao(IControladorIntegracao controladorIntegracao) {
        this.controladorIntegracao = controladorIntegracao;
    }

    public IControladorPlanoDeSituacoesDoPatio getControladorPlano() {
        return controladorPlano;
    }

    public void setControladorPlano(IControladorPlanoDeSituacoesDoPatio controladorPlano) {
        this.controladorPlano = controladorPlano;
    }

    public IControladorNavios getControladorNavios() {
        return controladorNavios;
    }

    public void setControladorNavios(IControladorNavios controladorNavios) {
        this.controladorNavios = controladorNavios;
    }

    public IControladorProduto getControladorProduto() {
        return controladorProduto;
    }

    public void setControladorProduto(IControladorProduto controladorProduto) {
        this.controladorProduto = controladorProduto;
    }

    /**
     * metodo que insere um navio da tabela "IntegracaoNavioCRM" na tabela "FilaDeNavios"
     * @param filaCadastrada
     * @param integraNavio
     * @param listaClientesCadastrados
     * @param listaTiposProdutoCadastrado
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    private MetaNavio insereNavioNaFila(FilaDeNavios filaCadastrada, IntegracaoNavioCRM integraNavio,
                    List<Cliente> listaClientesCadastrados, List<TipoProduto> listaTiposProdutoCadastrado, long timeDefault)
                    throws ErroSistemicoException {

        List<Cliente> novosClientes = new ArrayList<Cliente>();
        MetaNavio meta = new MetaNavio();
        meta.setIdUser(1L);
        meta.setDtInsert(new Date(timeDefault));
        meta.setSap_vbap_vbeln(integraNavio.getSap_vbap_vbeln());

        Navio navio = new Navio();
        navio.setNomeNavio(integraNavio.getNomeNavio());
        navio.setDataEmbarque(integraNavio.getDataEmbarqueNavio());
        navio.setDiaDeChegada(integraNavio.getDataChegadaNavio());
        navio.setDiaDeSaida(integraNavio.getDataSaidaNavio());
        navio.setDwt(integraNavio.getCapacidadeNavio());
        navio.setEta(integraNavio.getDataETA());
        navio.setDataAtracacao(integraNavio.getDataAtracacao());
        navio.setDataChegadaBarra(integraNavio.getDataChegadaBarra());
        navio.setDataDesatracacao(integraNavio.getDataDesatracacao());

        Cliente cliente = carregarCliente(integraNavio, listaClientesCadastrados, novosClientes);

        navio.setCliente(cliente);

        navio.setStatusEmbarque(StatusNavioEnum.CONFIRMADO); // Status
        // Confirmado       
        navio.setDtInsert(new Date(System.currentTimeMillis()));
        navio.setIdUser(1L);

        meta.incluirNovoStatus(navio, new Date(timeDefault));
        meta.addMetaCarga(criaListaCargasNavio(meta, integraNavio, listaTiposProdutoCadastrado, timeDefault, cliente));

        integraNavio.setProcessado(Boolean.TRUE);

        salvarClientes(novosClientes);
        return meta;
    }

    /**
     * salvarClientes
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 25/08/2010
     * @see
     * @param 
     * @return void
     */
    private void salvarClientes(List<Cliente> novosClientes) {
        if (novosClientes.size() > 0) {
            ClienteDAO dao = new ClienteDAO();
            dao.salvar(novosClientes);
        }
    }

    private Cliente carregarCliente(IntegracaoNavioCRM integraNavio, List<Cliente> listaClientesCadastrados,
                    List<Cliente> novosClientes) {
        Cliente cliente = buscarClienteCadastrado(listaClientesCadastrados, integraNavio.getCodigoCliente());

        if (cliente == null) {
            cliente = Cliente.criarCliente(integraNavio.getNomeCliente(), integraNavio.getCodigoCliente());
            novosClientes.add(cliente);
        }
        return cliente;
    }

    /**
     * metodo que insere um navio da tabela "IntegracaoNavioCRM" na tabela "FilaDeNavios"
     * @param filaCadastrada
     * @param integraNavio
     * @param listaClientesCadastrados
     * @param listaTiposProdutoCadastrado
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    private MetaNavio atualizarDados(MetaNavio meta, IntegracaoNavioCRM integraNavio,
                    List<Cliente> listaClientesCadastrados, List<TipoProduto> listaTiposProdutoCadastrado, long timeDefault)
                    throws ErroSistemicoException {

        List<Cliente> novosClientes = new ArrayList<Cliente>();

        
        Navio navio = meta.retornaStatusHorario(new Date(timeDefault));
        // navio atracado gera clone        
        if (navio.getStatusEmbarque().equals(StatusNavioEnum.ATRACADO)) {
            navio = meta.clonarStatus(new Date(timeDefault));
        }                            
            if (integraNavio != null) {
                navio.setNomeNavio(integraNavio.getNomeNavio());
                navio.setDataEmbarque(integraNavio.getDataEmbarqueNavio());
                navio.setDiaDeChegada(integraNavio.getDataChegadaNavio());
                navio.setDiaDeSaida(integraNavio.getDataSaidaNavio());
                navio.setDwt(integraNavio.getCapacidadeNavio());
                navio.setEta(integraNavio.getDataETA());
                navio.setDataAtracacao(integraNavio.getDataAtracacao());
                navio.setDataChegadaBarra(integraNavio.getDataChegadaBarra());
                navio.setDataDesatracacao(integraNavio.getDataDesatracacao());
                Cliente cliente = carregarCliente(integraNavio, listaClientesCadastrados, novosClientes);
                navio.setCliente(cliente);
                // Confirmado       
                navio.setDtInsert(new Date(System.currentTimeMillis()));
                navio.setIdUser(1L);
        // navio atracado não altera dados         
       if (navio.getStatusEmbarque().equals(StatusNavioEnum.BARRA) || navio.getStatusEmbarque().equals(StatusNavioEnum.CONFIRMADO)) {    
           atualizarCargasNavio(meta, integraNavio, listaTiposProdutoCadastrado, timeDefault, cliente);
       }     

        integraNavio.setProcessado(Boolean.TRUE);
      }    
        salvarClientes(novosClientes);

        return meta;
    }

    /**
     * criaListaCargasNavio
     * 
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 23/06/2010
     * @see
     * @param
     * @return List<MetaCarga>
    * @throws ErroSistemicoException 
     */
    private List<MetaCarga> criaListaCargasNavio(MetaNavio metaNavio, IntegracaoNavioCRM integraNavio,
                    List<TipoProduto> tipoProduto, long timeDefault, Cliente cliente) throws ErroSistemicoException {

        List<MetaCarga> listaCargas = new ArrayList<MetaCarga>();

        for (IntegracaoCargaCRM cargaCRM : integraNavio.getListaCargasNavio()) {
            MetaCarga metaCarga = new MetaCarga();
            metaCarga.setDtInsert(new Date(timeDefault));
            metaCarga.setIdUser(1L);
            metaCarga.setIdentificadorCarga(cargaCRM.getDescricaoCarga());

            Carga carga = new Carga();
            carga.setProduto(criaProdutoParaCarga(cargaCRM, tipoProduto, timeDefault));
            metaCarga.incluirNovoStatus(carga, new Date(timeDefault));
            carga.setOrientacaoDeEmbarque(criaOrientacaoEmbarqueCarga(cargaCRM, carga));

            listaCargas.add(metaCarga);
        }

        return listaCargas;
    }

    /**
     * criaListaCargasNavio
     * 
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 23/06/2010
     * @see
     * @param
     * @return List<MetaCarga>
    * @throws ErroSistemicoException 
     */
    private void atualizarCargasNavio(MetaNavio metaNavio, IntegracaoNavioCRM integraNavio, List<TipoProduto> tipoProduto,
                    long timeDefault, Cliente cliente) throws ErroSistemicoException {

        if (metaNavio.getListaMetaCargas() != null && metaNavio.getListaMetaCargas().size() > 0) {
            for (IntegracaoCargaCRM cargaCRM : integraNavio.getListaCargasNavio()) {
                MetaCarga metaCargaPai = null;
                for (MetaCarga metaCarga : metaNavio.getListaMetaCargas()) {
                    if (cargaCRM.getDescricaoCarga().equals(metaCarga.getIdentificadorCarga())) {
                        metaCargaPai = metaCarga;
                        break;
                    }
                }

                if (metaCargaPai != null) {
                    Carga carga = metaCargaPai.retornaStatusHorario(new Date(timeDefault));                                                            
                    if (carga.getProduto() != null) {
                        for (TipoProduto tipoP: tipoProduto) {
                            if (cargaCRM.getCodigoProduto() != null
                                            && tipoP.getCdProduto().trim().equals(cargaCRM.getCodigoProduto().trim())) {                                
                                carga.getProduto().setTipoProduto(tipoP);                                                                               
                                break;
                            }
                        }    
                        if (cargaCRM.getOrientacaoEmbarque() != null) {
                                OrientacaoDeEmbarque oe = carga.getOrientacaoDeEmbarque();
                                if (oe.getListaItemDeControleOrientacaoEmbarque() != null) {
                                    for (ItemDeControleOrientacaoEmbarque item : oe.getListaItemDeControleOrientacaoEmbarque()) {
                                         item.setOrientacao(null);   
                                    }
                                    oe.getListaItemDeControle().addAll(criaListaItensControleOrientacaoEmbarque(cargaCRM.getOrientacaoEmbarque(),carga.getProduto().getTipoProduto()));
                                } else {
                                    oe.setListaItemDeControle(criaListaItensControleOrientacaoEmbarque(cargaCRM.getOrientacaoEmbarque(),carga.getProduto().getTipoProduto()));                                
                                }
                                oe.setDtUpdate(new Date(System.currentTimeMillis()));                                
                                oe.setQuantidadeNecessaria(cargaCRM.getQuantidadeCarga());
                                oe.setTipoProduto(carga.getProduto().getTipoProduto());                                
                        }                        
                    } else {
                        carga.setProduto(criaProdutoParaCarga(cargaCRM, tipoProduto, timeDefault));
                        carga.setOrientacaoDeEmbarque(criaOrientacaoEmbarqueCarga(cargaCRM, carga));
                    }
                    
                    
                } else {
                    MetaCarga metaCarga = new MetaCarga();
                    metaCarga.setDtInsert(new Date(timeDefault));
                    metaCarga.setIdUser(1L);
                    metaCarga.setIdentificadorCarga(cargaCRM.getDescricaoCarga());

                    Carga carga = new Carga();
                    carga.setProduto(criaProdutoParaCarga(cargaCRM, tipoProduto, timeDefault));
                    metaCarga.incluirNovoStatus(carga, new Date(timeDefault));
                    carga.setOrientacaoDeEmbarque(criaOrientacaoEmbarqueCarga(cargaCRM, carga));
                    metaNavio.addMetaCarga(metaCarga);
                }
            }
        }
    }

    /**
    * criaProdutoParaCarga
    * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
    * @since 25/08/2010
    * @see
    * @param 
    * @return Produto
    */
    private Produto criaProdutoParaCarga(IntegracaoCargaCRM integracaoCarga, List<TipoProduto> listaTiposProdutoCadastrado,
                    long timeDefault) throws ErroSistemicoException {
        Produto produto = null;

        TipoProduto tipoPesquisado = new TipoProduto();
        tipoPesquisado.setCdProduto(integracaoCarga.getCodigoProduto());

        for (TipoProduto tipoProduto : listaTiposProdutoCadastrado) {
            if (integracaoCarga.getCodigoProduto() != null
                            && tipoProduto.getCdProduto().trim().equals(integracaoCarga.getCodigoProduto().trim())) {
                produto = Produto.criaProduto(tipoProduto, timeDefault);
                // setando o produto a carga            
                produto.setQuantidade(0D);
                produto.setTipoProduto(tipoProduto);
                produto.setQualidade(criaQualidade());
                produto.getQualidade().setProduto(produto);
            }
        }
        return produto;
    }

    /**
     * cria Qualidade do Produto da Carga
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.text.ParseException
     * @throws java.lang.NumberFormatException
     */
    private Qualidade criaQualidade() throws ErroSistemicoException {
        Qualidade qualidade = new Qualidade();
        qualidade.setIdUser(1L);
        qualidade.setDtInicio(new Date(System.currentTimeMillis()));
        qualidade.setEhReal(false);
        qualidade.setListaDeItensDeControle(criaListaItensControleVazio());

        return qualidade;
    }

    /**
     * Metodo auxiliar que retorna uma lista de itensDeControle, carregando os dados da tabela de itens
     * @param tblItensDeControle
     * @param listaTipoItemDeControle
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.text.ParseException
     * @throws java.lang.NumberFormatException
     */
    private List<ItemDeControle> criaListaItensControleVazio() throws ErroSistemicoException {

        List<TipoItemDeControle> listaTipoItemDeControle = controladorProduto.buscarTiposItemControle();

        List<ItemDeControle> listaItemControle = new ArrayList<ItemDeControle>();

        for (TipoItemDeControle tipoItemControle : listaTipoItemDeControle) {
            ItemDeControle itemControle = new ItemDeControleQualidade();
            itemControle.setTipoItemControle(tipoItemControle);
            itemControle.setIdUser(1L);
            itemControle.setDtInicio(new Date(System.currentTimeMillis()));
            listaItemControle.add(itemControle);
        }
        return listaItemControle;
    }

    private void initServices() {
        if (controladorIntegracao == null) {
            controladorIntegracao = new ControladorIntegracao();
        }
        if (controladorNavios == null) {
            controladorNavios = new ControladorNavios();
        }

        if (controladorProduto == null) {
            controladorProduto = new ControladorProduto();
        }

        if (controladorPlano == null) {
            controladorPlano = new ControladorPlanoDeSituacoesDoPatio();
        }
    }
}
