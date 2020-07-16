package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.read.biff.BiffException;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.RetornaObjetosDaInterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.util.planilha.CflexStockyardLeitorPlanilha;
import com.hdntec.gestao.cliente.util.planilha.FiltroPlanilha;
import com.hdntec.gestao.cliente.util.planilha.PlanilhaUtil;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.LeituraPlanilhaException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Classe que edita e cria uma atividade mudança de campanha das usinas, a partir de leitura de dados gravados em planilha excel
 * @author Bruno Gomes
 */
public class EditaMudancaDeCampanhaViaPlanilha {

    private static final int NUMERO_ITENS_DE_CONTROLE = 29;

    private ControladorDSP controladorDSP;
    private Campanha campanha;
    /** data inicial da campanha */
    private Date dataInicial;
    /** data final da campanha */
    private Date dataFinal;
    /** usina dona dessa campanha */
//    private Usina usina;
    /** tipo produto da campanha */
    private TipoProduto tipoProduto;
    /** nome da campanha*/
    private String nomeCampanha;
    /** a quantidade prevista de producao da campanha */
    private Double quantidadePrevista;

    private Long codigoProcessoProdutoPelletFeed;
    private Long codigoProcessoProdutoPelota;

    private CflexStockyardLeitorPlanilha cflexStockyardLeitorPlanilha;
    private RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();

    public EditaMudancaDeCampanhaViaPlanilha(ControladorDSP controladorDSP){
        this.controladorDSP = controladorDSP;
    }

    /**
     * chama o metodo construtor do Leitor de planilha
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
     */
    public void abrePlanilha() throws OperacaoCanceladaPeloUsuarioException, LeituraPlanilhaException, IOException, BiffException {
//        try {
            cflexStockyardLeitorPlanilha = new CflexStockyardLeitorPlanilha(new FiltroPlanilha());
//        } catch (BiffException ex) {
//            throw new LeituraPlanilhaException(PropertiesUtil.getMessage("aviso.mensagem.ocorreu.erro.durante.leitura.planilha"));
//        } catch (IOException ex) {
//            throw new LeituraPlanilhaException(PropertiesUtil.getMessage("aviso.mensagem.ocorreu.erro.durante.leitura.planilha"));
//        }
    }

   /**
    * Cria uma lista de ItensDeControle a partir da leitura da planilha de importação
    * @param sheet - a aba "Sequência" de onde se esta lendo
    * @return
    * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
    * @throws java.lang.Exception
    */
    public List<ItemDeControle> criaListaItensDeControle(int sheet) throws ErroSistemicoException, LeituraPlanilhaException, Exception {
        List<ItemDeControle> listaItemControle = new ArrayList<ItemDeControle>();
        ItemDeControle item;
        TipoItemDeControle tipoItemDeControle;
        Double valorMaximo;//seta o valorMaximoGarantido do itemDeControle
        Double valorMinimo;//seta o valorMinimoGarantido do itemDeControle
        Integer linhaPlanilha = 7; // a linha da planilha onde começam os valores dos ItensDeControle
        Integer index = 0;//usado para controlar o laço de repetição sobre o numero de ItensDeControle
        String nomeTipoItem;

        for (TipoItemDeControle tipoItem : controladorDSP.getInterfaceDSP().getInterfaceInicial().getListaTiposItemDeControle()) {
            linhaPlanilha = 7; //atualiza o indice da linha, para procurar o proximo item
            index = 0;//atualiza o somador, para procurar o proximo item
            while (index < NUMERO_ITENS_DE_CONTROLE) {
                nomeTipoItem = cflexStockyardLeitorPlanilha.leCelula(linhaPlanilha, PlanilhaUtil.COLUNA_A, sheet);
                if (tipoItem.getDescricaoTipoItemControle().equalsIgnoreCase(nomeTipoItem)) {
                    valorMinimo = cflexStockyardLeitorPlanilha.leCelulaDouble(linhaPlanilha, PlanilhaUtil.COLUNA_C, sheet);
                    valorMaximo = cflexStockyardLeitorPlanilha.leCelulaDouble(linhaPlanilha, PlanilhaUtil.COLUNA_D , sheet);
                    //cria tipo item controle
                    tipoItemDeControle = tipoItem;
                    //cria item controle
                    /**JESSÉ 18/6*/
                    item = new ItemDeControleQualidade();
                    //item = new ItemDeControle();
                    item.setTipoItemControle(tipoItemDeControle);//adiciona o TipoItemDeControle ao itemDeControle
                    item.setValor(valorMinimo);
                    item.setValor(valorMaximo);
                    if (valorMinimo == 0) {
                       tipoItem.setRelevante(false);
                    } else {
                       tipoItem.setRelevante(true);
                    }
                    //adiciona o item de controle a lista de itens
                    listaItemControle.add(item);
                    break;
                }
                linhaPlanilha++;
                index++;
            }
        }
        return listaItemControle;
    }

    /**
     * Retorna os dados das primeiras linhas da planilha<br>
     * Dados setados: nomeCampanha, dataInicial e final, nomeCliente, quantidadePrevista de producao, codigoDoProcesso
     * @param sheet
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    public void setaDadosCampanha(Integer sheet) throws ValidacaoCampoException, LeituraPlanilhaException, Exception{
        int linha = 0; //a primeira linha da planilha
        nomeCampanha = cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_B, sheet);
        //linha da planilha onde estão os dados da: DataInicial, DataFinal, QuantidadePrevista, Cliente e CodigoFaseProcesso, o Produto e as usinas são lidos no vinculo com a aba Constantes
         linha = 3; 
        dataInicial = roifn.setObjetoData(cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_A, sheet));
        dataFinal = roifn.setObjetoData(cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_B, sheet));
        //TODO RICARDO cliente nao tem campanha ?
        //nomeCliente = cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_H, sheet);
        //calculo da quantidade prevista por usina
        calculaQuantidadePrevista(sheet);
        //le o codigo do processo
         codigoProcessoProdutoPelletFeed = Long.parseLong(cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_I, sheet));
        //TODO tem que ler o codigo para a pelota e pellet feed separados
        codigoProcessoProdutoPelota = Long.parseLong(cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_I, sheet));
   }

    /**
     * cria um objeto Campanha com os dados da folha e da usina a que ele vai referenciar
     * @param sheet - a folha com os dados da campanha (aba Sequencia)
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    public Campanha criaCampanha(int sheet) throws ValidacaoCampoException, LeituraPlanilhaException, Exception{
        campanha = new Campanha();
        setaDadosCampanha(sheet);
        campanha.setNomeCampanha(nomeCampanha);
        campanha.setDataInicial(dataInicial);
        campanha.setDataFinal(dataFinal);

        
        //cria qualidade
        Qualidade qualidade = new Qualidade();
        qualidade.setEhReal(Boolean.FALSE);
        qualidade.setListaDeItensDeControle(criaListaItensDeControle(sheet));
        //atribui a qualidade para a campanha
        campanha.setQualidadeEstimada(qualidade);
        //instancia tipoProduto selecionado
        tipoProduto = criaTipoProduto();
        campanha.setTipoProduto(tipoProduto);
        // Insumo de um tipo produto é o pellet que é produzido na usina durante a produção deste tipo produto.
        campanha.setTipoPellet(tipoProduto.getCodigoInsumoTipoProduto());

        // tipo de pellet screening produzido pela usina nesta campanha.
        campanha.setTipoScreening(buscarScreening());

        //seta a quantidade prevista
        campanha.setQuantidadePrevista(quantidadePrevista);
        //codifo fase do processo da campanha
        campanha.setCodigoFaseProcessoPelletFeed(codigoProcessoProdutoPelletFeed);
        campanha.setCodigoFaseProcessoPelota(codigoProcessoProdutoPelota);
        
        return campanha;
    }

    /**
     * Busca datas das novas atividades a serem criadas, de acordo com o index.
     * @param index
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    public List<Date> buscarDatas(int index) throws ValidacaoCampoException, LeituraPlanilhaException, Exception {
        List<Date> datas = new ArrayList<Date>();
        setaDadosCampanha(index);
        datas.add(dataInicial);
        datas.add(dataFinal);
        return datas;
    }

    /**
     * Busca o tipo de produto pellet screening
     * @return
     */
    private TipoProduto buscarScreening() throws ErroSistemicoException {
        TipoProduto retorno = null;
        for(TipoProduto tipo : controladorDSP.getInterfaceDSP().getInterfaceInicial().getListaTiposProduto()){
            if((tipo.getTipoDeProduto()).equals(TipoDeProdutoEnum.PELLET_SCREENING)){
                retorno = tipo;
            }
        }
        return retorno;
    }

//    public Cliente criaCliente() throws ErroSistemicoException{
//        for(Cliente cliente : controladorDSP.getInterfaceDSP().getInterfaceInicial().getListaDeClientes()){
//            if(cliente.getNomeCliente().equalsIgnoreCase(nomeCliente)){
//                return cliente;
//            }
//        }
//        return null;
//    }

    /**
     * cria um objeto TipoProduto lendo dados da planilha de importação na aba "Constantes"
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    private TipoProduto criaTipoProduto() throws LeituraPlanilhaException, Exception{
        String sheet = PropertiesUtil.buscarPropriedade("nome.aba.de.constantes");
        //linha e coluna da folha "Constantes" da planilha de importacao de campanha, que contem o indice do tipoDeProduto selecionado
        int linha = 1;
        int indice =  Integer.parseInt(cflexStockyardLeitorPlanilha.leCelula(linha, PlanilhaUtil.COLUNA_M, sheet));
        String produto = cflexStockyardLeitorPlanilha.leCelula(indice , PlanilhaUtil.COLUNA_A, sheet);//nome do tipoProduto selecionado na planilha

        for(TipoProduto tipoProduto1 : controladorDSP.getInterfaceDSP().getInterfaceInicial().getListaTiposProduto()){
            if((tipoProduto1.getCodigoFamiliaTipoProduto()+" "+tipoProduto1.getCodigoTipoProduto()).equalsIgnoreCase(produto)){
                return tipoProduto1;
            }
        }
         return null;
    }

    /**
     * seta no objeto Double a quantidadePrevista de produção a partir da leitura deste dado da planilha e dividindo pelo numero de
     * usinas selecionadas
     * @param sheet
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    private void calculaQuantidadePrevista(int sheet) throws LeituraPlanilhaException, Exception{
        int linha = 3; // linha na planilha onde se encontra o campo "QuantidadePrevista" para leitura
        Double quantidade;
        //le a quantidade prevista editada
        quantidade = cflexStockyardLeitorPlanilha.leCelulaDouble(linha, PlanilhaUtil.COLUNA_G, sheet);
        //calcula a quantidade prevista de procução por campanha de cada usina
        quantidadePrevista = quantidade / numeroDeUsinasSelecionadas(sheet);
    }

    /**
     * retorna o numero de usinas selecionadas
     * @param linhaSequencia - o indice a aba Sequência que se deseja verificar
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    public int numeroDeUsinasSelecionadas(int linhaSequencia) throws LeituraPlanilhaException, Exception{
        linhaSequencia++;//esta soma é realizada pois a primeira aba tem indice zero, e na aba Constantes de VERDADEIRO/FALSO inicia na linha 2 Dois(indice 1(um))
        Integer usinasSelecionadas = 0;
        String sheet = PropertiesUtil.buscarPropriedade("nome.aba.de.constantes");
        String selecaoUsina1, selecaoUsina2, selecaoUsina3;
        List<String> listaParam = new ArrayList<String>();
        selecaoUsina1 = cflexStockyardLeitorPlanilha.leCelula(linhaSequencia, PlanilhaUtil.COLUNA_J, sheet);
        selecaoUsina2 = cflexStockyardLeitorPlanilha.leCelula(linhaSequencia, PlanilhaUtil.COLUNA_K, sheet);
        selecaoUsina3 = cflexStockyardLeitorPlanilha.leCelula(linhaSequencia, PlanilhaUtil.COLUNA_L, sheet);
        //verifica se existe alguma usina selecionada
        if (selecaoUsina1.equalsIgnoreCase("false") && selecaoUsina2.equalsIgnoreCase("false") && selecaoUsina3.equalsIgnoreCase("false")) {
            listaParam.add(String.valueOf(sheet));
            throw new LeituraPlanilhaException("exception.celula.usina.nao.selecionada", listaParam);
        }
        // verifica usina 01
        if (selecaoUsina1.equalsIgnoreCase("true")) {
            usinasSelecionadas++;
        }
        //verifica usina 02
       
        if(selecaoUsina2.equalsIgnoreCase("true") ){
            usinasSelecionadas++;
        }
        //verifica usina 03
      
        if(selecaoUsina3.equalsIgnoreCase("true") ){
            usinasSelecionadas++;
        }
        
        return usinasSelecionadas;
    }

    /**
     * retorna uma lista com o nome das usinas
     * @param linhaSequencia - a aba Sequência que esta sendo verificado quais usinas estão selecionadas
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    public List<String> buscaUsinasSelecionadas(int linhaSequencia) throws LeituraPlanilhaException, Exception {
        linhaSequencia++;//esta soma é realizada pois a primeira aba tem indice zero, e na aba Constantes de VERDADEIRO/FALSO inicia na linha 2 Dois(indice 1(um))
        String sheet = PropertiesUtil.buscarPropriedade("nome.aba.de.constantes");
        List<String> listaUsinas = new ArrayList<String>();
        String selecaoUsina1, selecaoUsina2, selecaoUsina3;
        List<String> listaParam = new ArrayList<String>();
        selecaoUsina1 = cflexStockyardLeitorPlanilha.leCelula(linhaSequencia, PlanilhaUtil.COLUNA_J, sheet);
        selecaoUsina2 = cflexStockyardLeitorPlanilha.leCelula(linhaSequencia, PlanilhaUtil.COLUNA_K, sheet);
        selecaoUsina3 = cflexStockyardLeitorPlanilha.leCelula(linhaSequencia, PlanilhaUtil.COLUNA_L, sheet);
        //verifica se existe alguma usina selecionada
        if(selecaoUsina1.equalsIgnoreCase("false") && selecaoUsina2.equalsIgnoreCase("false") && selecaoUsina3.equalsIgnoreCase("false")){
            listaParam.add(String.valueOf(sheet));
            throw new LeituraPlanilhaException("exception.celula.usina.nao.selecionada", listaParam);
        }

        // O numero da linha na aba de constantes no quadro Selecoes, os nomes das usinas que devem ser iguais ao cadastrado no nome da usina
        //no sistema, a primeira linha é zero.
        int linhaNomeUsina = 0;
        // verifica usina 01
        if(selecaoUsina1.equalsIgnoreCase("true") ){
            listaUsinas.add(cflexStockyardLeitorPlanilha.leCelula(linhaNomeUsina, PlanilhaUtil.COLUNA_J, sheet));
        }
        //verifica usina 02
        if(selecaoUsina2.equalsIgnoreCase("true") ){
            listaUsinas.add(cflexStockyardLeitorPlanilha.leCelula(linhaNomeUsina, PlanilhaUtil.COLUNA_K , sheet));
        }
        //verifica usina 03
        if(selecaoUsina3.equalsIgnoreCase("true") ){
            listaUsinas.add(cflexStockyardLeitorPlanilha.leCelula(linhaNomeUsina, PlanilhaUtil.COLUNA_L, sheet));
        }
        
        return listaUsinas;
    }

    /**
     * retorna quantas "sequências" estão sendo criadas
     * @return
     */
    public int numeroDeSequenciasSelecionadas(){
        return cflexStockyardLeitorPlanilha.numeroDeAbas();
    }
   
}//end of class
