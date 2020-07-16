package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JRadioButton;

import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Anotacao;
import com.hdntec.gestao.domain.planta.entity.status.RegistroDaAnotacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 *
 * @author Bruno Gomes
 */
public class EditarInserirAnotacao {

    private InterfaceEditarInserirAnotacao interfaceEditarInserirAnotacao;
    private ControladorDSP controladorDSP;

    //constantes para referencia das colunas da tabela de anotacoes
    public static final int COLUNA_SELECAO = 0;
    public static final int COLUNA_ID = 1;
    public static final int COLUNA_USUARIO = 2;
    public static final int COLUNA_DATAHORA = 3;
    public static final int COLUNA_DESCRICAO = 4;


    /**
     * construtor padr?o
     */
    public EditarInserirAnotacao(InterfaceEditarInserirAnotacao interfaceEditarInserirAnotacao){
        this.interfaceEditarInserirAnotacao = interfaceEditarInserirAnotacao;
    }

    /**
     * 
     * @param anotacao
     * @param IdRegistroProcurado
     * @return
     */
    public RegistroDaAnotacao obterRegistroAnotacaoSelecionado(Anotacao anotacao, long IdRegistroProcurado){
        RegistroDaAnotacao registroProcurado = null;
        for(RegistroDaAnotacao registro : anotacao.getListaRegistrosDaAnotacao()){
            if(registro.getIdRegistroAnotacao().equals(IdRegistroProcurado)){
                registroProcurado = registro;
                break;
            }
        }
        return registroProcurado;
    }

   /**
    * Insere ou atualiza a lista de anotacoes da planta, dependendo do valor do parametro novaAnotacao
    * @param situacaoPatioVisualizada - situacaoDePatio que contem a planta onde sera atualizada a lista de anotacoes
    * @param anotacao     - objeto Anotacao que sera inserida ou atualizada
    * @param novaAnotacao - boolean que indica se a anotacao sera inserida ou atualizada;
    */
    public void insereAnotacaoListaPlata(SituacaoPatio situacaoPatioVisualizada, Anotacao anotacao, boolean novaAnotacao)
    {
        if(situacaoPatioVisualizada.getPlanta().getListaDeAnotacoes() == null) {
            List<Anotacao> listaDeAnotacoes = new ArrayList<Anotacao>();
            situacaoPatioVisualizada.getPlanta().setListaDeAnotacoes(listaDeAnotacoes);
        }
        int index;

        if(novaAnotacao)
        {//adiciona na lista da planta a anotacao
            situacaoPatioVisualizada.getPlanta().getListaDeAnotacoes().add(anotacao);
        }else
        {//atualiza a anotacao que a plata ja contem
            index = situacaoPatioVisualizada.getPlanta().getListaDeAnotacoes().indexOf(anotacao);
            situacaoPatioVisualizada.getPlanta().getListaDeAnotacoes().set(index, anotacao);
        }

    }

    /**
     * Verifica se o campo de descricao da anotacao n?o esta vazio
     * @return true se houver texto valido <br> false caso contrario
     */
    public boolean validaDados(){
        boolean resultado;
        if(interfaceEditarInserirAnotacao.getJtNovaDescricao().getText().isEmpty()){
            resultado = false;
        }
        else{
            resultado = true;
        }
        return resultado;
    }

    /**
     * Adiciona um novo registro no objeto Anotacao
     * @param anotacao
     * @param user
     * @param data
     * @param descricao
     */
    public void acrescentaNaListaAnotacao(Anotacao anotacao, String user, Date data, String descricao){
        RegistroDaAnotacao registro = new RegistroDaAnotacao();        
        registro.setUsuario(user);
        registro.setDataAnotacao(data);
        registro.setDescricao(descricao);
        registro.setAnotacao(anotacao);
        anotacao.getListaRegistrosDaAnotacao().add(registro);
    }

    /**
     * 
     * @param listaRegistro
     * @param user
     * @param data
     * @param descricao
     */
    public void adicionaRegistroAListaDeRegistroAnotacao(List<RegistroDaAnotacao> listaRegistro, String user, Date data, String descricao,Anotacao anotacao)
    {
        RegistroDaAnotacao registro = new RegistroDaAnotacao();
        registro.setUsuario(user);
        registro.setDataAnotacao(data);
        registro.setDescricao(descricao);
        registro.setAnotacao(anotacao);
        listaRegistro.add(registro);
    }
    
    /**
     * 
     * @param registro
     * @param novaData
     * @param descricao
     * @param user
     */
    public void editaRegistroAnotacao(RegistroDaAnotacao registro, Date novaData, String descricao, String user)
    {
        registro.setDataAnotacao(novaData);
        registro.setUsuario(user);
        registro.setDescricao(descricao);
    }

    /**
     * retorna o Id do registroDeAnotacao que esta representado na tabela de informacoes
     * @param cflexJtable
     * @return o Id do registro, ou null caso n?o exista linha selecionada
     */
    public Long retornaLinhaSelecionadaTabela(CFlexStockyardJTable cflexJtable) throws ValidacaoCampoException{
        Long idRegistro = null;
        //se o retorno deste condicional for igual a -1 significa que nenhuma linha esta selecionada
        if(cflexJtable.getSelectedRow() != -1)
        {
            
            Object dado = cflexJtable.getValueAt(cflexJtable.getSelectedRow(), EditarInserirAnotacao.COLUNA_ID);
            if(dado != null){
               String id = dado.toString();
               idRegistro = new Long(id);
            }else{
                throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("exception.editar.item.na.memoria"));
            }

        }
        return idRegistro;
    }

    /**
     * copia para uma lista os registros da lista da Anotacao
     * @param anotacao
     * @param listaRegistro
     */
    public void copiaListaDaAnotacao(Anotacao anotacao, List<RegistroDaAnotacao> listaRegistro){
        listaRegistro = new ArrayList<RegistroDaAnotacao>();
        listaRegistro.addAll(anotacao.getListaRegistrosDaAnotacao());
    }

    /**
     * atualiza a lista de registros da Anotacao com uma nova lista passada
     * @param listaRegistro
     * @param anotacao
     */
    public void atualizaListaAnotacao(List<RegistroDaAnotacao> listaRegistro, Anotacao anotacao){
        anotacao.addRegistrosDaAnotacao(listaRegistro);
    }

    /**
     * 
     * @param anotacao
     * @param registroDaAnotacao
     * @param idRegistro
     */
    public void editaRegistroDaListaDeAnotacao(Anotacao anotacao, RegistroDaAnotacao registroDaAnotacao, long idRegistro)
    {
        for(RegistroDaAnotacao registro : anotacao.getListaRegistrosDaAnotacao())
        {
            if(registro.getIdRegistroAnotacao().equals(registroDaAnotacao.getIdRegistroAnotacao()))
            {
                registro = registroDaAnotacao;
            }
        }
    }

    /**
     * Se a lista de registros da anotacao for null instancia uma nova lista para a Anotacao
     * @param anotacao
     */
    public void verificaListaAnotacaoNula(Anotacao anotacao){
        if(anotacao.getListaRegistrosDaAnotacao() == null){
            List<RegistroDaAnotacao> lista = new ArrayList<RegistroDaAnotacao>();
            anotacao.addRegistrosDaAnotacao(lista);
        }
    }

    /**
     * carrega a tabela de dados da anotacao com os dados contidos na lista de registros da Anotacao
     * @param listaAnotacoes
     * @param cflexTable
     * @param listaColunas
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void carregaTabelaAnotacoes(List<RegistroDaAnotacao> listaAnotacoes, CFlexStockyardJTable cflexTable, List<ColunaTabela> listaColunas) throws ErroSistemicoException
    {
         Vector m_preDados = new Vector();
          String formatoData = PropertiesUtil.buscarPropriedade("formato.campo.datahora");
          for(RegistroDaAnotacao registro : listaAnotacoes){
              Object[] dados = new Object[5];

              dados[COLUNA_SELECAO]  = new JRadioButton();
              dados[COLUNA_ID] = registro.getIdRegistroAnotacao();
              dados[COLUNA_USUARIO] = registro.getUsuario();
              dados[COLUNA_DATAHORA] = DSSStockyardTimeUtil.formatarData( registro.getDataAnotacao(), formatoData);
              dados[COLUNA_DESCRICAO] = registro.getDescricao();

              m_preDados.add(new Vector(Arrays.asList(dados)));
          }

          //adicionado os dados da tabela
          CFlexStockyardFuncoesTabela.setInformacoesTabela(cflexTable, m_preDados, listaColunas);
    }

    /**
     * Cria objeto anotacao
     * @param posicaoX - posicao no eixoX onde a anotacao sera criada
     * @param posicaoY - posicao no eixoY onde a anotacao sera criada
     * @return
     */
    public Anotacao criaObjetoAnotacao(int posicaoX, int posicaoY){
        Anotacao anotacao = new Anotacao();
        anotacao.setEixoX(posicaoX);
        anotacao.setEixoY(posicaoY);

        return anotacao;
    }

    /**
     * carrega a tabela de dados da anotacao com os dados contidos na listaDeRegistros da Anotacao, se este dado n?o estiver no mapa, do contrario usa o valor do mapa
     * @param listaAnotacoes
     * @param mapaDescricao
     * @param cflexTable
     * @param listaColunas
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void carregaTabelaAnotacoes(List<RegistroDaAnotacao> listaAnotacoes, HashMap<Long, String> mapaDescricao ,CFlexStockyardJTable cflexTable, List<ColunaTabela> listaColunas) throws ErroSistemicoException
    {
         Vector m_preDados = new Vector();
          String formatoData = PropertiesUtil.buscarPropriedade("formato.campo.datahora");
          for(RegistroDaAnotacao registro : listaAnotacoes){
              String mensagem;
              if(mapaDescricao.get(registro.getIdRegistroAnotacao()) != null){
                mensagem = mapaDescricao.get(registro.getIdRegistroAnotacao());
              }else{
                  mensagem = registro.getDescricao();
              }
              
              Object[] dados = new Object[5];

              dados[COLUNA_SELECAO]  = new JRadioButton();
              dados[COLUNA_ID] = registro.getIdRegistroAnotacao();
              dados[COLUNA_USUARIO] = registro.getUsuario();
              dados[COLUNA_DATAHORA] = DSSStockyardTimeUtil.formatarData( registro.getDataAnotacao(), formatoData);
              dados[COLUNA_DESCRICAO] = mensagem;

              m_preDados.add(new Vector(Arrays.asList(dados)));
          }

          //adicionado os dados da tabela
          CFlexStockyardFuncoesTabela.setInformacoesTabela(cflexTable, m_preDados, listaColunas);
    }

}
