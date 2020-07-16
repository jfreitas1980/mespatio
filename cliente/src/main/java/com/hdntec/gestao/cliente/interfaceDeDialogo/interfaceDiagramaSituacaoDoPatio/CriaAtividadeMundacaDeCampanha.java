package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.exceptions.LeituraPlanilhaException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;


/**
 * Classe que cria as atividades de mudanca de campanha e nova situacaoDePatio lendo os dados da planilha de importação
 * @author Bruno Gomes
 */
public class CriaAtividadeMundacaDeCampanha {

    private EditaMudancaDeCampanhaViaPlanilha editorCampanha;
    private ControladorDSP controladorDSP;
    
     public CriaAtividadeMundacaDeCampanha(EditaMudancaDeCampanhaViaPlanilha edita, ControladorDSP controladorDSP){
         this.editorCampanha = edita;
         this.controladorDSP = controladorDSP;
     }

    public List<Atividade> criaAtividades(SituacaoPatio sitPatio) throws ValidacaoCampoException, LeituraPlanilhaException, Exception{

        List<Atividade> atividadesMudancaCampanha = new ArrayList<Atividade>();
        //verifica quantas sequencias foram editadas na importacao da planilha(situacoes que devem ser geradas)
        int numeroDeAbasPlanilha = editorCampanha.numeroDeSequenciasSelecionadas() - 1; //(-1) pois uma das folhas (sheet) é a de CONSTANTES

        // itera sobre as abas da planilha criando as atividades necessarias.
        for (int aba = 0; aba < numeroDeAbasPlanilha; aba++) {
            atividadesMudancaCampanha.addAll(instanciaAtividadesPorAba(aba, sitPatio)); //le os valores da planilha e seta na atividade
        }

        return atividadesMudancaCampanha;
    }

    /**
     * Cria as atividades de mudanca de campanha de uma aba, podendo gerar ate tres campanhas por aba, pois existem tres usinas. E cada
     * campanha é composta por uma atividade de inicio e uma atividade de finalizacao da atividade
     * @param aba
     * @param sitPatio
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    public List<Atividade> instanciaAtividadesPorAba(int aba, SituacaoPatio sitPatio) throws ValidacaoCampoException, LeituraPlanilhaException, Exception{
        List<Atividade> listaDeAtividades = new ArrayList<Atividade>();
        
        // data de inicio e fim das futuras atividades de mudanca de campanha.
        List<Date> datasAtividade = editorCampanha.buscarDatas(aba);

        List<AtividadeCampanha> listaAtividadesCampanha = criaAtividadesCampanha(aba, sitPatio);

        // cria atividade inicio e fim para cada atividade campanha criada.
        for(AtividadeCampanha atividadeCampanha : listaAtividadesCampanha){
            listaDeAtividades.addAll(criaAtividadeInicioEFim(atividadeCampanha, datasAtividade));
        }

        return listaDeAtividades;
    }

    /**
     * Este metodo cria as atividades de inicio e fim de uma mudanca de campanha, onde as duas atividades contem a mesma atividade campanha,
     * a atividade de finalizacao utiliza a atividade campanha para poder localizar a campanha a ser finalizada. O atividade de inicio cria uma
     * nova campanha na usina.
     * @param atividadeCampanha Atividade campanha que contem os objetos para a execucao desta atividade
     * @param listaDeDatas Datas de inicio e fim, para gerar as datas dos intervalos pontuais
     * @return
     */
    private List<Atividade> criaAtividadeInicioEFim(AtividadeCampanha atividadeCampanha, List<Date> listaDeDatas){
        List<Atividade> listaDeAtividades = new ArrayList<Atividade>();

        Atividade atividade = new Atividade();
        atividade.setTipoAtividade(TipoAtividadeEnum.MUDANCA_DE_CAMPANHA);

        // Mudanca de campanha gera duas atividades pontuais e pontanto a lista de entidades data contem apenas uma data em cada atividade
        atividade.setDtInicio(listaDeDatas.get(0));

        // Como mudanca de campanha no sistema trabalha com apenas uma campanha de cada vez, entao ela contem apenas uma atividade campanha
        List<AtividadeCampanha> atividadesCampanha = new ArrayList<AtividadeCampanha>();
        atividadesCampanha.add(atividadeCampanha);
        atividade.addAtividadeCampanha(atividadesCampanha);

        listaDeAtividades.add(atividade);

        Atividade atividadeFinal = new Atividade();
        atividadeFinal.setTipoAtividade(TipoAtividadeEnum.MUDANCA_DE_CAMPANHA);

        // Mudanca de campanha gera duas atividades pontuais e pontanto a lista de entidades data contem apenas uma data em cada atividade
        atividadeFinal.setDtInicio(listaDeDatas.get(1));

        AtividadeCampanha finalizacaoCampanha = new AtividadeCampanha();
        finalizacaoCampanha.setNomeUsina(atividadeCampanha.getNomeUsina());
        finalizacaoCampanha.setQuantidade(atividadeCampanha.getQuantidade());
        finalizacaoCampanha.setTipoProduto(atividadeCampanha.getTipoProduto());

        // Como mudanca de campanha no sistema trabalha com apenas uma campanha de cada vez, entao ela contem apenas uma atividade campanha
        List<AtividadeCampanha> atividadesCampanhaFinal = new ArrayList<AtividadeCampanha>();
        atividadesCampanhaFinal.add(finalizacaoCampanha);
        atividadeFinal.addAtividadeCampanha(atividadesCampanhaFinal);

        listaDeAtividades.add(atividadeFinal);

        return listaDeAtividades;
    }

    /**
     * Cria uma lista de atividadesCampanha
     * @param sheet - indice da folha que esta criando a nova atividade mundanca de campanha
     * @param sitPatio - situacao de patio clonada
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.LeituraPlanilhaException
     * @throws java.lang.Exception
     */
    private List<AtividadeCampanha> criaAtividadesCampanha(int sheet, SituacaoPatio sitPatio) throws LeituraPlanilhaException, Exception{
        List<AtividadeCampanha> listaDeCampanhas = new ArrayList<AtividadeCampanha>();
        AtividadeCampanha atividadeCampanha;
        Campanha campanha;
        //verifica o numero de usinas selecionadas ao importar a planilha
        List<String> listaNomesUsinas = editorCampanha.buscaUsinasSelecionadas(sheet);
        List<Usina> listaUsinas = sitPatio.getPlanta().getListaUsinas(sitPatio.getDtInicio());
        for(int i=0; i < listaNomesUsinas.size(); i++){
            atividadeCampanha = new AtividadeCampanha();
            for(Usina usina : listaUsinas){
                if(usina.getNomeUsina().equals(listaNomesUsinas.get(i))){
                    campanha = editorCampanha.criaCampanha(sheet);
                    campanha.setNomeCampanha(campanha.getNomeCampanha() + " " + usina.getNomeUsina());
                    atividadeCampanha.setNomeUsina(usina.getNomeUsina());
                    atividadeCampanha.setCampanha(campanha);
                    atividadeCampanha.setQuantidade(campanha.getQuantidadePrevista());
                    atividadeCampanha.setTipoProduto(campanha.getTipoProduto());
                    listaDeCampanhas.add(atividadeCampanha);
                    break;
                }
            }
        }
        return listaDeCampanhas;
    }

    public EditaMudancaDeCampanhaViaPlanilha getEditorCampanha() {
        return editorCampanha;
    }

    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }



}//end of class
