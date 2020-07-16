package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.io.IOException;
import java.util.Date;

import jxl.read.biff.BiffException;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.planilha.CflexStockyardLeitorPlanilha;
import com.hdntec.gestao.cliente.util.planilha.FiltroPlanilha;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Classe que cria amostra para uma Carga e seta os valores de seus ItensDeControle, a partir da leitura de uma planilha(*.xls) de entrada
 * de dados
 * @author Bruno Gomes
 */
public class CriaAmostraParaCarga {

    private InterfaceCarga interfaceCarga;
    private CflexStockyardLeitorPlanilha cflexStockyardLeitorPlanilha;
    private String abaNavio = "Navio";

    public CriaAmostraParaCarga(InterfaceCarga interfaceCarga){
        this.interfaceCarga = interfaceCarga;
    }

    /**
     * Abre janela para escolha do caminho da planilha de importacao da amostra de embarque
     * @throws jxl.read.biff.BiffException
     * @throws java.io.IOException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
     */
    public void abrePlanilha() throws BiffException, IOException, OperacaoCanceladaPeloUsuarioException{
        cflexStockyardLeitorPlanilha = new CflexStockyardLeitorPlanilha(new FiltroPlanilha());
    }

    public void criaPlanilhaLeitura(String pathFile) throws IOException, BiffException{
        cflexStockyardLeitorPlanilha = new CflexStockyardLeitorPlanilha(pathFile);
    }

    /**
     * Cria amostra para a carga, a partir da leitura da planilha de importacao da qualidade de embarque
     * @return - Amostra da carga
     */
    /*private List<Amostra> criaAmostraDoProduto(){
        LeituraPlanilhaAmostraCarga leitura = new LeituraPlanilhaAmostraCarga(interfaceCarga, cflexStockyardLeitorPlanilha);
        int index = indiceNomeAbaNavio(abaNavio);//nome da aba da planilha onde estão os valores dos ItensDeControle

        //cria objeto amostra
        List<Amostra> listaDeAmostra = new ArrayList<Amostra>();
        Amostra amostra = new Amostra();
//        amostra.setQuantidade(leitura.getQuantidadeAmostra(index));
        amostra.setListaDeItensDeControle(leitura.criaItemDeControle(index));
        amostra.setNomeAmostra(leitura.criaNomeDaAmostra(index));
        listaDeAmostra.add(amostra);
        
        return listaDeAmostra;
    }*/
    
    public void efetuaLeituraItensDeControleEmbarcado()
    {
        LeituraPlanilhaAmostraCarga leitura = new LeituraPlanilhaAmostraCarga(interfaceCarga, cflexStockyardLeitorPlanilha);
        int index = indiceNomeAbaNavio(abaNavio);//nome da aba da planilha onde estão os valores dos ItensDeControle
        
        leitura.efetuaLeituraItensEmbarcado(index);
    }

    /**
     * Cria atividade de resultado de amostragem
     * @return
     */
    /*public Atividade criaAtividadeResultadoAmostragem(){
        // TODO - BRUNO verificar como a leitura da planilha sera realizada para suportar a estrutura de lista de Amostras da Qualidade
        //avaliar os metodos desta classe e onde são chamados
        Atividade atividade = new Atividade();
        //atividade.setCarga(interfaceCarga.getCargaVisualizada());
        atividade.setListaResultadoAmostras(criaAmostraDoProduto());
        atividade.setDtInicio(criaDataTexto());
        atividade.setTipoAtividade(TipoAtividadeEnum.RESULTADO_DE_AMOSTRAGEM);

        return atividade;
    }*/

    /**
     * retorna uma data a partir do campo texto de data lido da planilha de importacao
     * @return
     */
    public Date criaDataTexto(){
        LeituraPlanilhaAmostraCarga leitura = new LeituraPlanilhaAmostraCarga(interfaceCarga, cflexStockyardLeitorPlanilha);
        int index = indiceNomeAbaNavio(abaNavio);//nome da aba da planilha onde estão os valores dos ItensDeControle
        String dataTexto = leitura.retornaDataDaAmostra(index);
        Date data = null;
        try {
            data = DSSStockyardTimeUtil.criarDataComString(dataTexto);
        } catch (ValidacaoCampoException ex) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceCarga.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
        }
        return data;
    }

    /**
     * Retorna o indice da aba (sheet) da planilha onde se encontra os dados com os valores dos ItensDeControle da amostra da carga (aba Navio)
     * @param nomeFolha - Nome da aba onde encontram-se os dados da Qualidade da Amostra da Carga
     * @return - O indice do nome da aba <br>
     * <b>(-1)</b> caso não encontre o nome passado
     */
    public int indiceNomeAbaNavio(String nomeFolha){
        String[] nomes = cflexStockyardLeitorPlanilha.getSheetNames();
        int indiceNome = -1;
        for(int i=0; i < nomes.length; i++){
            if(nomes[i].equalsIgnoreCase(nomeFolha)){
                indiceNome = i;
            }
        }
        return indiceNome;
    }

    public CflexStockyardLeitorPlanilha getCflexStockyardLeitorPlanilha() {
        return cflexStockyardLeitorPlanilha;
    }

    public InterfaceCarga getInterfaceCarga() {
        return interfaceCarga;
    }
    
}
