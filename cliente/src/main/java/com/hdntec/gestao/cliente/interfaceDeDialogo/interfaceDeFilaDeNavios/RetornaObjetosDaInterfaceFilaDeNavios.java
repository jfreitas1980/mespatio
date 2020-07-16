
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFormattedTextField;

import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Classe utilizada para retornar objetos procurados no Pacote InterfaceFilaDeNavios, que serão usados nas classes de edição de dados
 * @author bgomes
 */
public class RetornaObjetosDaInterfaceFilaDeNavios {

    /**
     * Construtor padrao
     */
    public RetornaObjetosDaInterfaceFilaDeNavios(){
    }


    /**
     * Retorna uma {@link Carga} a partir da listaDeCargas atraves da idendificacao da carga (String)
     * @param codCarga   String com o "nome" da carga
     * @param listaDeCargas lista de cargas do navio
     * @return objeto Carga
     */
    public Carga retornaCargaSelecionada(String codCarga, List<Carga> listaDeCargas){
        for(Carga carga : listaDeCargas){
            if(carga.getIdentificadorCarga().toString().equalsIgnoreCase(codCarga)){
                return carga;
            }
        }
        return null;
    }

    /**
     * Retorna uma {@link InterfaceCarga} a partir da listaDeInterfacesCargas utilizando como comparador a idendificacao da carga (String)
     * @param codCarga
     * @param listaDeInterfacesCargas
     * @return
     */
    public InterfaceCarga retornaInterfaceCargaSelecionada(String codCarga, List<InterfaceCarga> listaDeInterfacesCargas){
        for(InterfaceCarga interfaceCarga1 : listaDeInterfacesCargas){
            if(interfaceCarga1.getCargaVisualizada().getIdentificadorCarga().equalsIgnoreCase(codCarga)){
                return interfaceCarga1;
            }
        }
        return null;
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
    public List<ItemDeControle> criaListaItensControle(CFlexStockyardJTable tblItensDeControle, List<TipoItemDeControle> listaTipoItemDeControle, boolean itemOrientacaoEmbarque) throws ErroSistemicoException, ParseException, NumberFormatException, ValidacaoCampoException {
        List<ItemDeControle> listaItemControle = new ArrayList<ItemDeControle>();
        ItemDeControle item;
        //TipoItemDeControle tipoItemDeControle;
        Double valor;

        for (int i = 0; i < tblItensDeControle.getRowCount(); i++) {
            for (TipoItemDeControle tipoItem : listaTipoItemDeControle) {
                if (tipoItem.getDescricaoTipoItemControle().equals(tblItensDeControle.getValueAt(i, 0))) {
                    //cria tipo item controle
                    //tipoItemDeControle = new TipoItemDeControle();
                    //tipoItemDeControle = tipoItem;
                    //cria item controle
                    /**JESSÉ 18/6*/
                    if (itemOrientacaoEmbarque) 
                    {
                    	item = new ItemDeControleOrientacaoEmbarque();
                    } 
                    else 
                    {
                    	item = new ItemDeControleQualidade();
                    }
                    //item = new ItemDeControle();
                    //item.setTipoItemControle(tipoItemDeControle);//adiciona o TipoItemDeControle ao itemDeControle
                    item.setTipoItemControle(tipoItem);
                    if (!tblItensDeControle.getValueAt(i, 1).toString().trim().equals(""))
                    {
                        valor = Double.valueOf(tblItensDeControle.getValueAt(i, 1).toString());
                        item.setValor(valor);
                        Double desvioPadraoValor = valor*0.1;
//                        item.setDesvioPadraoValor(desvioPadraoValor);
                        tipoItem.setRelevante(true);
                    }
                    //adiciona o item de controle a lista de itens
                    listaItemControle.add(item);
                }
            }
        }

        return listaItemControle;
    }

    /**
     * Metodo auxiliar que carrega os dados da tabela de itens de controle, onde o valor sai da {@link Qualidade} do {@link Produto} da {@link Carga}
     * @param carga
     * @param tblItensDeControle
     * @param listaColunas
     * @throws java.lang.Exception
     */
    public void insereCargas(List<Carga> listaCarga, CFlexStockyardJTable tblItensDeControle, List<ColunaTabela> listaColunas) throws Exception{
         List<List> m_preDados = new ArrayList<List>();
        for (Carga carga : listaCarga) {
            List<String> dados = new ArrayList<String>();
            dados.add(carga.getIdentificadorCarga());
            if (carga.getProduto() != null) {
                dados.add(""+carga.getProduto().getQuantidade());
                dados.add(""+carga.getProduto().getTipoProduto());
                dados.add(""+carga.getProduto().getTipoProduto().getDescricaoTipoProduto());
            }    
            m_preDados.add(dados);
        }

        //setando o table com os tiposItensDeControle
        CFlexStockyardFuncoesTabela.setDadosTabela(tblItensDeControle, m_preDados, listaColunas);

    }

    /**
     * Metodo auxiliar que carrega os dados da tabela de itens de controle, onde o valor sai da {@link Qualidade} do {@link Produto} da {@link Carga}
     * @param carga
     * @param tblItensDeControle
     * @param listaColunas
     * @throws java.lang.Exception
     */
    public void insereItensDeControleDeProduto(Carga carga, CFlexStockyardJTable tblItensDeControle, List<ColunaTabela> listaColunas) throws Exception{
        List<List> m_preDados = new ArrayList<List>();
        for (ItemDeControle itemDeControle : carga.getProduto().getQualidade().getListaDeItensDeControle()) {
            List dados = new ArrayList();
            dados.add(itemDeControle.getTipoItemControle().getDescricaoTipoItemControle());
            if(itemDeControle.getValor()!=null)
               dados.add(itemDeControle.getValor());
            else
               dados.add("");
//            if(itemDeControle.getDesvioPadraoValor() != null)
//               dados.add(itemDeControle.getDesvioPadraoValor());
//            else
//               dados.add("");
            dados.add(itemDeControle.getTipoItemControle().getUnidade());
            m_preDados.add(new ArrayList(dados));
        }

        //setando o table com os tiposItensDeControle
        CFlexStockyardFuncoesTabela.setDadosTabela(tblItensDeControle, m_preDados, listaColunas);

    }
    /**
     * Metodo auxiliar que carrega os dados da tabela de itens de controle, onde o valor sai da lista de {@link ItemDeControle} da {@link OrientacaoDeEmbarque} da {@link Carga}
     * @param carga
     * @param tblItensDeControle
     * @param listaColunas
     * @throws java.lang.Exception
     */
    public void insereItensDeControleDaOrientacaoDeEmbarque(Carga carga, CFlexStockyardJTable tblItensDeControle, List<ColunaTabela> listaColunas) throws Exception{
         List<List> m_preDados = new ArrayList<List>();
        for (ItemDeControle itemDeControle : carga.getOrientacaoDeEmbarque().getListaItemDeControle()) {
            List dados = new ArrayList();
            dados.add(itemDeControle.getTipoItemControle().getDescricaoTipoItemControle());
            if(itemDeControle.getValor()!=null)
               dados.add(itemDeControle.getValor());
            else
               dados.add("");
            
//            if(itemDeControle.getDesvioPadraoValor()!=null)
//               dados.add(itemDeControle.getDesvioPadraoValor());
//            else
//               dados.add("");

            dados.add(itemDeControle.getTipoItemControle().getUnidade());
            m_preDados.add(new ArrayList(dados));
        }

        //setando o table com os tiposItensDeControle
        CFlexStockyardFuncoesTabela.setDadosTabela(tblItensDeControle, m_preDados, listaColunas);

    }

    /**
     * Metodo auxiliar que carrega a tabela de itensDeControle para edicao com os valores setados para Zero
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.lang.Exception
     */
    public void insereItensDeControleValorZero(CFlexStockyardJTable tblItensDeControle, List<ColunaTabela> listaColunas, ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) throws ErroSistemicoException, Exception{
        List<List> m_preDados = new ArrayList<List>();
        List<TipoItemDeControle> listaTipos = controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaTiposItemDeControle();
        for (TipoItemDeControle tipoItemDeControle :  listaTipos)
        {
            List dados = new ArrayList();
            dados.add(tipoItemDeControle.getDescricaoTipoItemControle());
            dados.add("");
            //dados.add("");
            dados.add(tipoItemDeControle.getUnidade());
            m_preDados.add(new ArrayList(dados));
        }
        //setando o table com os tiposItensDeControle
        CFlexStockyardFuncoesTabela.setDadosTabela(tblItensDeControle, m_preDados, listaColunas);

    }

    /**
     * Retorna o objeto Carga a partir de uma lista de Cargas
     * @param listaCarga lista de objetos Carga
     * @param carga      a carga que se deseja procurar
     * @return
     */
    public Carga retornaCargaDoNavio(List<Carga> listaCarga, Carga carga){
        for(Carga carga1 : listaCarga){
            if(carga1.getIdCarga().equals(carga.getIdCarga())){
                return carga1;
            }
        }
        return null;
    }

     /**
     * Metodo que seta o Date a partir da leitura dos dados editados no JFormaterTextField
     * @param jftHorario O JFormaterTextField no formato DataHora
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public Date setObjetosRetorno(JFormattedTextField jftHorario) throws ValidacaoCampoException {
        Date data = new Date();
        SimpleDateFormat fmtDataHora = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        
        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add("Data / hora");

        try {
            data = (fmtDataHora.parse(jftHorario.getText()));
        } catch (ParseException ex) {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
        }
        
        return data;
    }

    /**
     * Metodo que seta o Date a partir da leitura dos dados editados no JFormaterTextField
     * @param jftHorario O JFormaterTextField no formato Data
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public Date setObjetoData(JFormattedTextField jftHorario) throws ValidacaoCampoException {
        Date data = new Date();
        SimpleDateFormat fmtDataHora = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.data"));
        Calendar calDataHora = Calendar.getInstance();

        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add("Data");

        try {
            calDataHora.setTime(fmtDataHora.parse(jftHorario.getText()));
        } catch (ParseException ex) {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
        }
        data = calDataHora.getTime();

        return data;
    }

    /**
     * Metodo que retorna um Date a partir de uma string
     * @param dataTexto - String no formato data (dd/MM/yyyy)
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
     public Date setObjetoData(String dataTexto) throws ValidacaoCampoException {
        Date data = new Date();
        SimpleDateFormat fmtData = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.data"));
        Calendar calData = Calendar.getInstance();

        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add("Data");

        try {
            calData.setTime(fmtData.parse(dataTexto));
        } catch (ParseException ex) {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
        }
        data = calData.getTime();

        return data;
    }

     /**
      * Metodo que retorna um Date a partir de uma string
      * @param dataTexto - String no formato Data e hora (dd/MM/yyyy HH:mm:ss)
      * @return
      * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
      */
      public Date setObjetoDataHora(String dataTexto) throws ValidacaoCampoException {
        Date data = new Date();
        SimpleDateFormat fmtData = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        Calendar calData = Calendar.getInstance();

        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add("Data");

        try {
            calData.setTime(fmtData.parse(dataTexto));
        } catch (ParseException ex) {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
        }
        data = calData.getTime();

        return data;
    }

    /**
     * Metodo que verifica a unicidade de um novo nome para um navio
     * @param controladorInterfaceFilaDeNavios
     * @param novoNome nome que se deseja procurar.
     * @return <b>true</b>   Se o nome for válido, ou seja, não existe um navio cadastrado com este nome. <br>
     *         <b>false</b>  Se o nome for inválido, já existe um navio cadastrado com o nome procurado.
     */
    public boolean verificaNomeNovoNavio(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios, String novoNome){
        for(Navio navio : controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila()){
            if(navio.getNomeNavio().equalsIgnoreCase(novoNome)){
                return false;
            }
        }
        return true;
    }

    /**
     * Procura um Navio em uma listaDeNavios pelo atributo IdNavio
     * @param listaNavios
     * @param navioProcurado
     * @return O indice do navioProcurado na listaDeNavios<br>
     *         -1 caso não encontre
     */
    public Integer buscaNavioNaLista(List<Navio> listaNavios, Navio navioProcurado){
        for(int index=0; index < listaNavios.size(); index++){
            if(listaNavios.get(index).getIdNavio().equals(navioProcurado.getIdNavio())){
                return index;
            }
        }
        return -1;
    }

    /**
     * busca um Cliente em uma listaDeNavios pelo atributo idCliente
     * @param listaDeNavios
     * @param clienteProcurado
     * @return um cliente
     */
    public Cliente buscaClinteEquivalenteNaListaNavios(List<Navio> listaDeNavios, Cliente clienteProcurado){
        for(Navio navio : listaDeNavios){
            if(navio.getCliente().equals(clienteProcurado)){
                return navio.getCliente();
            }
        }
        return null;
    }
   
}
