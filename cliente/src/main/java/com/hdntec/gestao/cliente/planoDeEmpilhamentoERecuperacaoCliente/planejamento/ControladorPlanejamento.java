package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ControladorInterfaceInicial;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.enums.PeriodicidadeEnum;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.AtividadeNaoManipulaPilhaException;
import com.hdntec.gestao.exceptions.AtividadePontualException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CarregarOficialNaoNecessariaException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.RemocaoDePlanosNaoPermitidaException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Controlador das operaÃ§Ãµes do subsistema de planejamento que gerencia toda a
 * parte de execucao de atividades e criacao de situacoes de patio.
 * 
 * @author andre
 * 
 */
public class ControladorPlanejamento implements IControladorPlanejamento {

    private ControladorDePlano controladorDePlano = null;
   // private ControladorDeIntervalosDeExecucao controladorDeIntervalosDeExecucao = null;
    private ControladorInterfaceInicial controladorInterfaceInicial = null;
    /** variavel que garante que a consolidacao nao sera realizada 2 vezes sem necessidade
     * pelo usuario
     */
    private Boolean consolidacaoRealizada;

    /** variavel que garante que a consolidacao nao sera realizada 2 vezes sem necessidade
     * pelo usuario
     */
    private Boolean planoOficialCarregado;

    /**
     * Contrutor de controlador planejamento, inicia o plano virtual e a lista de
     * pontos de corte no tempo, onde sao gerados as situacoes virtuais.
     * @param listaDePlanos
     */
    public ControladorPlanejamento(PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) {

        // cria controlador de planos reais com todos os planos de execucao a serem exibidos.
        this.controladorDePlano = new ControladorDePlano(planoEmpilhamentoRecuperacao, this);

        // cria controlador do plano virtual ja carregando os intervalos de execucao existentes nos plano reais.
       // this.controladorDeIntervalosDeExecucao = new ControladorDeIntervalosDeExecucao(this, controladorDePlano.obterIntervalosDeExecucao());
        // inicia-se com true pois o usuario pode tentar abrir o sistema e consololidadar em seguida
        this.consolidacaoRealizada = Boolean.TRUE;
        // inicia-se com true pois o usuario pode tentar abrir o sistema e consololidadar em seguida
        this.planoOficialCarregado = Boolean.FALSE;
    }

    public void adicionarSituacaoPatioNaLista(SituacaoPatio situacaoPatio) {
        controladorDePlano.adicionarSituacaoPatioNaLista(situacaoPatio);
    }

    @Override
    public boolean planejarRecuperacao(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws ValidacaoObjetosOperacaoException {
        boolean sucesso = false;

        sucesso = this.executar(atividade, situacaoPatioAtual);
        if (sucesso) {
           // controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
            this.consolidacaoRealizada = Boolean.FALSE;
        }
        return sucesso;
    }

    @Override
    public boolean planejarEmpilhamento(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws BlendagemInvalidaException, CampanhaIncompativelException, TempoInsuficienteException, ValidacaoObjetosOperacaoException {
        
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividade);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
    	//this.quebraAtividade(atividade);
        //muda variavel para false, possibilitando consolidacao
        this.consolidacaoRealizada = Boolean.FALSE;
        ///(controladorDeIntervalosDeExecucao);
        
        return true;
        
    }

    @Override
    public void planejarMudancaCampanha(List<Atividade> atividadesMudancaCampanha) throws AtividadeException {
       /* for (Atividade atividade : atividadesMudancaCampanha) {
            Date dataInicial = atividade.getDatas().get(0);

            if (dataInicial.compareTo(controladorDePlano.obterSituacaoPatioInicial().getDtInicio()) < 0) {
                throw new AtividadeException(PropertiesUtil.buscarPropriedade("exeption.erro.atividade.dataInicial"));
            }
            this.quebraAtividade(atividade);
        }*/
        this.consolidacaoRealizada = Boolean.FALSE;
        //controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
    }

    @Override
    public void planejarResultadoAmostragem(Atividade atividade) throws AtividadeException {
     /*   Date dataInicial = atividade.getDatas().get(0);
        if (dataInicial.compareTo(controladorDePlano.obterSituacaoPatioInicial().getDtInicio()) < 0) {
            throw new AtividadeException(PropertiesUtil.buscarPropriedade("exeption.erro.atividade.dataInicial"));
        }
        this.quebraAtividade(atividade);
        //muda variavel para false, possibilitando consolidacao
        this.consolidacaoRealizada = Boolean.FALSE;*/
       // controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
    }

    @Override
    public void planejarMovimentacao(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException {
/*        atividade.setDatas(new ArrayList<DataEntity>());

        DataEntity dtEntityInicial = new DataEntity();
        dtEntityInicial.setDt(situacaoPatioAtual.getDtInicio());
        atividade.getDatas().add(dtEntityInicial);

        DataEntity dtEntityFinal = new DataEntity();

         Cria Calendar com a data da situacao de patio atual e adiciona 1 minuto
         * para a gerar a situacao de patio com a atividade pontual de movimentacao.
         
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(situacaoPatioAtual.getDtInicio());
        calendar.add(Calendar.SECOND, 1);
        dtEntityFinal.setDt(calendar.getTime());


        atividade.getDatas().add(dtEntityFinal);

        Date dataInicial = atividade.getDatas().get(0);
        if (dataInicial.compareTo(this.controladorDePlano.buscarSituacaoPatio(0).getDtInicio()) < 0) {
            throw new AtividadeException(PropertiesUtil.buscarPropriedade("exeption.erro.atividade.dataInicial"));
        }
        this.quebraAtividade(atividade);
          //apos criar a atividade verifica se a lista de balizas existe alguma que esteja interditada
        ControladorPlanejamento.validaInterdicaoBaliza(atividade);

        //muda variavel para false, possibilitando consolidacao
        this.consolidacaoRealizada = Boolean.FALSE;
        controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
*/      
    }

    @Override
    public void planejarTratamentoPSM(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws AtividadeException {
/*        atividade.setDatas(new ArrayList<DataEntity>());

        DataEntity dtEntityInicial = new DataEntity();
        dtEntityInicial.setDt(situacaoPatioAtual.getDtInicio());
        atividade.getDatas().add(dtEntityInicial);

        DataEntity dtEntityFinal = new DataEntity();

         Cria Calendar com a data da situacao de patio atual e adiciona 1 minuto
         * para a gerar a situacao de patio com a atividade pontual de movimentacao.
         
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(situacaoPatioAtual.getDtInicio());
        calendar.add(Calendar.SECOND, 1);
        dtEntityFinal.setDt(calendar.getTime());


        atividade.getDatas().add(dtEntityFinal);

        Date dataInicial = atividade.getDatas().get(0);
        if (dataInicial.compareTo(this.controladorDePlano.buscarSituacaoPatio(0).getDtInicio()) < 0) {
            throw new AtividadeException(PropertiesUtil.buscarPropriedade("exeption.erro.atividade.dataInicial"));
        }
        this.quebraAtividade(atividade);
        //muda variavel para false, possibilitando consolidacao
        this.consolidacaoRealizada = Boolean.FALSE;
        controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
*/
    }
    
    /**
     * Retorna situacoes de patio de plano virtual criado anteriormente pelo metodo cria plano virtual.
     * @return
     */
    @Override
    public List<SituacaoPatio> obterSituacoesDePatio() {
        return controladorDePlano.obterSituacaoPatios();
    }

    @Override
    public Integer obterNumeroDeSituacoes() {
        return this.controladorDePlano.obterSituacaoPatios().size();
    }

    @Override
    public SituacaoPatio buscarSituacaoPatio(Integer posicaoSituacao) {
        return this.controladorDePlano.buscarSituacaoPatio(posicaoSituacao);
    }

    private boolean executar(Atividade atividade, SituacaoPatio situacaoPatioAtual) throws ValidacaoObjetosOperacaoException {
        boolean resultado = true;
        try {
            this.preencherDados(situacaoPatioAtual, atividade);
        } catch (AtividadePontualException e) {
            System.err.println(e.getMessage());
        } catch (AtividadeNaoManipulaPilhaException e) {
            System.err.println(e.getMessage());
        }
        // adiciona atividade em um ou mais intervalos de execucao existentes ou cria novos
        // e adiciona a atividade neles.
        return resultado;
    }
  
    public void preencherDados(SituacaoPatio situacaoInicial, Atividade atividade) throws AtividadePontualException, AtividadeNaoManipulaPilhaException, ValidacaoObjetosOperacaoException {

       /* List<DataEntity> datasRelevantes = new ArrayList<DataEntity>();

        DataEntity dataEntity = new DataEntity();
        dataEntity.setDt(situacaoInicial.getDtInicio());
        datasRelevantes.add(dataEntity);

        // para cada maquina cria a lista de lugares que ela deve atender
        HashMap<MaquinaDoPatio, ArrayList<LugarEmpilhamentoRecuperacao>> mapaLugaresPorMaquina = new HashMap<MaquinaDoPatio, ArrayList<LugarEmpilhamentoRecuperacao>>();
        List<LugarEmpilhamentoRecuperacao> listaLugaresER = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao();
        for (LugarEmpilhamentoRecuperacao lugarER : listaLugaresER) {
            MaquinaDoPatio maq = lugarER.getMaquinaDoPatio();
            ArrayList<LugarEmpilhamentoRecuperacao> listaLugares = mapaLugaresPorMaquina.get(maq);
            if (listaLugares == null) {
                listaLugares = new ArrayList<LugarEmpilhamentoRecuperacao>();
            }
            listaLugares.add(lugarER);
            mapaLugaresPorMaquina.put(maq, listaLugares);
        }

        // para cada maquina gera a lista de evento no tempo para recuperar a lista de balizas que ela deve atender. 
        // A lista eh ordenada pela distancia da maquina as pilhas e pelo sentido de recuperacao definido
        Set<MaquinaDoPatio> listaMaquinas = mapaLugaresPorMaquina.keySet();
        if (listaMaquinas.size() > 0) {
	        for (MaquinaDoPatio maq : listaMaquinas)
	        {
	           ArrayList<LugarEmpilhamentoRecuperacao> listaLugaresOrdenada = ordenaListaLugaresEmpilhamentoRecuperacao(maq, mapaLugaresPorMaquina.get(maq));
	           // gera a lista de eventos para a lista de balizas
	           ArrayList<DataEntity> listaEventos = geraListaDeEventos(atividade, maq, listaLugaresOrdenada, situacaoInicial.getDtInicio());
	           datasRelevantes.addAll(listaEventos);
	        }
        } 
        if (atividade.getListaDeAtividadesCampanha().size() > 0) {
        	if (atividade.getListaDeAtividadesCampanha() != null) {
          	  double tempoAtual = 0;
      			for (AtividadeCampanha atividadeCampanha : atividade.getListaDeAtividadesCampanha()) {
      				if (atividadeCampanha.getCorreia() != null) {
      					tempoAtual = atividadeCampanha.getQuantidade()	/ atividadeCampanha.getTaxaOperacaoUsina();
      					Date dataAtividade = dataAPartirDeDoubleSegundos(tempoAtual * 3600,
      							situacaoInicial.getDtInicio());
      					if (!this.verificaDataEntityJahExsitente(datasRelevantes, dataAtividade)) {
      						dataEntity = new DataEntity();
      						dataEntity.setDt(dataAtividade);
      						datasRelevantes.add(dataEntity);
      					}
      				}
      			}
      		}
        }

        // ordena a lista de eventos
        //Collections.sort(datasRelevantes);

        // lista de datas relevantes é adicionada a atividade.
        //atividade.setDatas(datasRelevantes);
        ControladorPlanejamento.validaInterdicaoBaliza(atividade);
        this.validaInterdicaoDePier(atividade);
		this.validaPeriodoManutencao(atividade,situacaoInicial);
		//Veio do merge com o branch revision 3601
		//this.validaPeriodoManutencao(atividade);
*/    }
    
    /* private Boolean verificaDataEntityJahExsitente(List<DataEntity> listaEventos, Date dataAtividade) {
    
    	for (DataEntity dataEntity : listaEventos) {
			if (dataEntity.equals(dataAtividade)) {
				return Boolean.TRUE;
			}
		}
    	return Boolean.FALSE;
    }*/
    
  /*private ArrayList<DataEntity> geraListaDeEventos(Atividade atividade, MaquinaDoPatio maquina, ArrayList<LugarEmpilhamentoRecuperacao> listaLugaresOrdenada, Date dataInicial)
  {
      ArrayList<DataEntity> listaEventos = new ArrayList<DataEntity>();
      int posicaoAtualMaquina = 0;
      if (maquina != null) {
    	  posicaoAtualMaquina = maquina.getPosicao().getNumero();
      }

      double tempoAtual = 0;
      if (listaLugaresOrdenada != null) {
         for (LugarEmpilhamentoRecuperacao lugarER : listaLugaresOrdenada)
         {
            // ordena a lista de balizas a partir da lista de lugares ordenada. A lista de balizas deve ser ordenada de acordo com o sentido
            ArrayList<Baliza> listaBalizas = new ArrayList<Baliza>();
            if (lugarER.getSentido().equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL))
            {
               listaBalizas.addAll(lugarER.getListaDeBalizas());
            }
            else
            {
               listaBalizas.addAll(inverteListaBalizas(((ArrayList)lugarER.getListaDeBalizas())));
            }

            // desloca a maquina ate a pilha inicial a ser atendida
            Baliza ultimaBalizaTratada = listaBalizas.get(0);
            int distancia = Math.abs(posicaoAtualMaquina - ultimaBalizaTratada.getNumero());
            tempoAtual += distancia / lugarER.getMaquinaDoPatio().getVelocidadeDeslocamento();
            DataEntity dataEntity = new DataEntity();
            dataEntity.setDt(dataAPartirDeDoubleSegundos(tempoAtual * 3600, dataInicial));
            listaEventos.add(dataEntity);

            // a maquina agora esta na posicao inicial do lugar
            posicaoAtualMaquina = ultimaBalizaTratada.getNumero();

            // gera eventos para as balizas restantes do lugar
            int sizeListaBalizas = listaBalizas.size();

            double quantidadeAEmpilhar = lugarER.getQuantidade();

            for (int i = 1; i < sizeListaBalizas; i++) {
                // se for baliza contigua entao nao precisa gerar evento
                Baliza balizaAtual = listaBalizas.get(i);
                if (Math.abs(balizaAtual.getNumero() - posicaoAtualMaquina) == 1) {
                    if (atividade.getTipoAtividade() == TipoAtividadeEnum.RECUPERACAO) {
                        // TODO revisar este calculo de acordo com a quantidade definida para a recuperacao
                        tempoAtual += ultimaBalizaTratada.getProduto().getQuantidade() / lugarER.getTaxaDeOperacaoNaPilha();
                    } else if (atividade.getTipoAtividade() == TipoAtividadeEnum.EMPILHAMENTO) {
                        double tempoBaliza = calculaTempoEmpilhamentoBaliza(ultimaBalizaTratada, lugarER.getTaxaDeOperacaoNaPilha(), quantidadeAEmpilhar);
                        tempoAtual += tempoBaliza;
                        quantidadeAEmpilhar -= tempoBaliza * lugarER.getTaxaDeOperacaoNaPilha();
                    }
                } // se nao for baliza contigua entao gera evento precisa deslocar para o proximo grupo
                else {
                    // termina a recuperacao do grupo de balizas atual
                    if (atividade.getTipoAtividade() == TipoAtividadeEnum.RECUPERACAO) {
                        // TODO revisar este calculo de acordo com a quantidade definida para a recuperacao
                        tempoAtual += ultimaBalizaTratada.getProduto().getQuantidade() / lugarER.getTaxaDeOperacaoNaPilha();
                    } else if (atividade.getTipoAtividade() == TipoAtividadeEnum.EMPILHAMENTO) {
                        double tempoBaliza = calculaTempoEmpilhamentoBaliza(ultimaBalizaTratada, lugarER.getTaxaDeOperacaoNaPilha(), quantidadeAEmpilhar);
                        tempoAtual += tempoBaliza;
                        quantidadeAEmpilhar -= tempoBaliza * lugarER.getTaxaDeOperacaoNaPilha();
                    }
                    dataEntity = new DataEntity();
                    dataEntity.setDt(dataAPartirDeDoubleSegundos(tempoAtual * 3600, dataInicial));
                    listaEventos.add(dataEntity);
                    // desloca a maquina para o proximo grupo
                    distancia = Math.abs(posicaoAtualMaquina - balizaAtual.getNumero());
                    tempoAtual += distancia / lugarER.getMaquinaDoPatio().getVelocidadeDeslocamento();
                    dataEntity = new DataEntity();
                    dataEntity.setDt(dataAPartirDeDoubleSegundos(tempoAtual * 3600, dataInicial));
                    listaEventos.add(dataEntity);
                }
                posicaoAtualMaquina = balizaAtual.getNumero();
                ultimaBalizaTratada = balizaAtual;
            }
            // gera o evento da ultima baliza do lugar
            if (atividade.getTipoAtividade() == TipoAtividadeEnum.RECUPERACAO) {
                // TODO revisar este calculo de acordo com a quantidade definida para a recuperacao
                tempoAtual += ultimaBalizaTratada.getProduto().getQuantidade() / lugarER.getTaxaDeOperacaoNaPilha();
            } else if (atividade.getTipoAtividade() == TipoAtividadeEnum.EMPILHAMENTO) {
                double tempoBaliza = calculaTempoEmpilhamentoBaliza(ultimaBalizaTratada, lugarER.getTaxaDeOperacaoNaPilha(), quantidadeAEmpilhar);
                tempoAtual += tempoBaliza;
                quantidadeAEmpilhar -= tempoBaliza * lugarER.getTaxaDeOperacaoNaPilha();
            }
            dataEntity = new DataEntity();
            dataEntity.setDt(dataAPartirDeDoubleSegundos(tempoAtual * 3600, dataInicial));
            listaEventos.add(dataEntity);
           }
      }

     return listaEventos;
    }
*/
    
    @Override
    public ControladorDePlano getControladorDePlano() {
        return this.controladorDePlano;
    }
   
    @Override
    public void consolidarPlano() throws ErroSistemicoException {
        controladorDePlano.consolidarPlano();
        this.consolidacaoRealizada = Boolean.TRUE;
        this.planoOficialCarregado = Boolean.FALSE;
    }

    @Override
    public void oficializaPlano() throws ErroSistemicoException {
    	if(controladorDePlano.validaSalvarPlanoUsuario()){
            /**Consolida Plano do Usuario*/
            controladorDePlano.consolidarPlano();
        } else {
            /**Consolida Plano do Usuario*/
            controladorDePlano.consolidarPlano();
            /**Oficializa Plano do usuÃ¡rio*/
            controladorDePlano.salvarPlanoDeUsuario();
        }
    	
    }

    @Override
    public void removerPlano(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException {
        //controladorDePlano.consolidarPlano();
    	controladorDePlano.removerPlano(indiceDaSituacaoDePatioExibida);    	
    	//controladorDePlano.consolidarPlano();
    	this.consolidacaoRealizada = Boolean.TRUE;
    }

    @Override
    public ControladorInterfaceInicial getControladorInterfaceInicial() {
        return controladorInterfaceInicial;
    }

    @Override
    public void setControladorInterfaceInicial(ControladorInterfaceInicial controladorInterfaceInicial) {
        this.controladorInterfaceInicial = controladorInterfaceInicial;
    }

    @Override
    public SituacaoPatio obterSituacaoDePatio(Date data) {
        return controladorDePlano.obterSituacao(data);
    }

    @Override
    public void atualizacaoEmpilhamento(Atividade atividadeAtualizacaoEmpilhamento, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException
    {
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividadeAtualizacaoEmpilhamento);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
        
        if (atividadeAtualizacaoEmpilhamento.getDtFim() != null && atividadeAtualizacaoEmpilhamento.getDtFim() != atividadeAtualizacaoEmpilhamento.getDtInicio())
        {
         
        	this.consolidacaoRealizada = Boolean.FALSE;
            try {
                this.consolidarPlano();
            } catch (ErroSistemicoException e) {
                // TODO Auto-generated catch block
                throw new AtividadeException(e);
            }
        	
        	PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;
            try {
                planoEmpilhamentoRecuperacao = controladorInterfaceInicial.executarIntegracaoSistemaMES(atividadeAtualizacaoEmpilhamento.getDtFim(), controladorDePlano.getPlanoEmpilhamentoRecuperacao());
            } catch (ErroSistemicoException e) {
                // TODO Auto-generated catch block
                throw new AtividadeException(e);
            }                                                    
            controladorDePlano.setPlanoEmpilhamentoRecuperacao(planoEmpilhamentoRecuperacao);
        }                
        
        this.consolidacaoRealizada = Boolean.FALSE;
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }
    }
    
    @Override
    public void atualizacaoEmpilhamentoEmergencia(Atividade atividadeAtualizacaoEmpilhamentoEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException
    {
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividadeAtualizacaoEmpilhamentoEmergencia);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
        this.consolidacaoRealizada = Boolean.FALSE;
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }
    }
    
    @Override
    public void atualizacaoEdicaoBalizas(Atividade atividadeEdicaoBalizas, SituacaoPatio situacaoPatioAtual) throws AtividadeException
    {
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividadeEdicaoBalizas);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
        this.consolidacaoRealizada = Boolean.FALSE;
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }
    }

    
    @Override
    public void atualizacaoEdicaoMaquinas(Atividade atividadeEdicaoBalizas, SituacaoPatio situacaoPatioAtual) throws AtividadeException
    {
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividadeEdicaoBalizas);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
        this.consolidacaoRealizada = Boolean.FALSE;
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }
    }

    
    @Override
    public void atualizacaoRecuperacao(Atividade atividadeAtualizacaoRecuperacao, SituacaoPatio situacaoPatioAtual) throws ValidacaoObjetosOperacaoException
    {
        try
        {
        	SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividadeAtualizacaoRecuperacao);
            getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
           
            if (atividadeAtualizacaoRecuperacao.getDtFim() != null && atividadeAtualizacaoRecuperacao.getDtFim() != atividadeAtualizacaoRecuperacao.getDtInicio())
            {
            	this.consolidacaoRealizada = Boolean.FALSE;
                try {
                    this.consolidarPlano();
                } catch (ErroSistemicoException e) {
                    // TODO Auto-generated catch block
                    throw new AtividadeException(e);
                }
            	PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;
                try {
                    planoEmpilhamentoRecuperacao = controladorInterfaceInicial.executarIntegracaoSistemaPIMS(atividadeAtualizacaoRecuperacao.getDtFim(), controladorDePlano.getPlanoEmpilhamentoRecuperacao());
                } catch (ErroSistemicoException e) {
                    // TODO Auto-generated catch block
                    throw new AtividadeException(e);
                }                                                    
                
                controladorDePlano.setPlanoEmpilhamentoRecuperacao(planoEmpilhamentoRecuperacao);
            }            
            this.consolidacaoRealizada = Boolean.FALSE;
            this.consolidarPlano();
         
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(controladorInterfaceInicial.getInterfaceInicial().getInterfaceDSP().getInterfaceInicial(), ex.getMessage() , PropertiesUtil.getMessage("titulo.erro.sistemico"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public Boolean getConsolidacaoRealizada() {
		return consolidacaoRealizada;
	}

	public void setConsolidacaoRealizada(Boolean consolidacaoRealizada) {
		this.consolidacaoRealizada = consolidacaoRealizada;
	}

	@Override
    public void verificarPossibilidadeDeConsolidacao() throws ConsolidacaoNaoNecessariaException {
        if (consolidacaoRealizada) {
            throw new ConsolidacaoNaoNecessariaException(PropertiesUtil.buscarPropriedade("exception.consolidacao.nao.necessaria"));
        }
    }

    
    
    @Override
    public void verificarPossibilidadeDeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException {
        if (planoOficialCarregado) {
            throw new CarregarOficialNaoNecessariaException(PropertiesUtil.buscarPropriedade("exception.carregar.oficial.nao.necessaria"));
        }
    }
    
    @Override
    public void verificarPossibilidadeDeRemocaoDePlanos() throws RemocaoDePlanosNaoPermitidaException {
        if (!consolidacaoRealizada) {
            throw new RemocaoDePlanosNaoPermitidaException(PropertiesUtil.buscarPropriedade("exception.remocao.plano.nao.permitida"));
        }
    }

    @Override
    public void ativarNecessidadeDeConsolidacao() {
        this.consolidacaoRealizada = Boolean.FALSE;
    }

    @Override
    public void desativarNecessidadeDeConsolidacao() {
        this.consolidacaoRealizada = Boolean.TRUE;
    }

    
    @Override
    public void planejaAtracacaoDeNavio(Atividade atividade) {
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividade);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
    	//this.quebraAtividade(atividade);
        //muda variavel para false, possibilitando consolidacao

        this.consolidacaoRealizada = Boolean.FALSE;
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ///(controladorDeIntervalosDeExecucao);
    }

    @Override
    public void carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(PlanoEmpilhamentoRecuperacao plano) throws ErroSistemicoException {    	
    	controladorDePlano.carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(plano);
    	this.planoOficialCarregado = Boolean.TRUE;
    	this.consolidacaoRealizada = Boolean.FALSE;
    }

   @Override
   public void retornarPelletFeed(Atividade atividadeRetornoPelletFeed, SituacaoPatio situacaoPatioAtual)
   {
       // adiciona atividade em um ou mais intervalos de execucao existentes ou cria novos
       // e adiciona a atividade neles.       
       //controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
       this.consolidacaoRealizada = Boolean.FALSE;
   }

   public static void validaInterdicaoBaliza(Atividade atividade) throws ValidacaoObjetosOperacaoException
   {
   /*   List<DataEntity> datasRelevantes = atividade.getDatas();
      Date inicio = datasRelevantes.get(0);
      Date fim = datasRelevantes.get(datasRelevantes.size() - 1);

      for (LugarEmpilhamentoRecuperacao lugarEmpilhamentoRecuperacao : atividade.getListaDeLugaresDeEmpilhamentoRecuperacao()) {
         if (lugarEmpilhamentoRecuperacao.getListaDeBalizas() != null && !lugarEmpilhamentoRecuperacao.getListaDeBalizas().isEmpty())
         {
            for (Baliza baliza : lugarEmpilhamentoRecuperacao.getListaDeBalizas())
            {
               List<String> listaParametros = new ArrayList<String>();
               listaParametros.add(baliza.getNumero().toString());

               if (baliza.getListaInterdicao() != null && baliza.getListaInterdicao().size() > 0)
               {
                  for (Interdicao interdicao : baliza.getListaInterdicao())
                  {
                     if ((inicio.after(interdicao.getDataInicial()) && inicio.before(interdicao.getDataFinal())) || (fim.after(interdicao.getDataInicial()) && fim.before(interdicao.getDataFinal())))
                     {
                        throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage("baliza.interdicao", listaParametros));
                     }
                  }
               }
            }
         }
      }
*/   }

    /**
     * Valida se o Pier se encontra em algum periodo de interdicao, impedindo
     * que ocorra alguma atividade na qual exista a participao do Pier.
     *
     * @param pier
     * @param dataInicial
     * @param dataFinal
     * @throws ValidacaoObjetosOperacaoException
     */
    public void validaInterdicaoDePier(Atividade atividade) throws ValidacaoObjetosOperacaoException {

/*        List<DataEntity> datasRelevantes = atividade.getDatas();
        Date inicio = datasRelevantes.get(0);
        Date fim = datasRelevantes.get(datasRelevantes.size() - 1);

        if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.RECUPERACAO)) {
            
             * Busca realizada entre a lista de Pier para encontrar a qual pier
             * o navio da atividade se refere.
             
            List<Pier> listaDePier = getControladorInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getPlanta().getListaPiers();
            for (Pier pier : listaDePier) {
                for (Berco berco : pier.getListaDeBercosDeAtracacao()) {
                    if (berco.equals(atividade.getCarga().getNavio().getBercoDeAtracacao())) {
                        if (pier.getListaInterdicao() != null && pier.getListaInterdicao().size() > 0) {
                            for (Interdicao interdicao : pier.getListaInterdicao()) {
                                if ((inicio.after(interdicao.getDataInicial()) && inicio.before(interdicao.getDataFinal())) || (fim.after(interdicao.getDataInicial()) && fim.before(interdicao.getDataFinal()))) {
                                    throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage("pier.interdicao"));
                                }
                            }
                        }
                    }
                }
            }
        }
*/	 }

    
    /**
     * Metodo que faz a chamada para validar os periodos de manutencao durante uma atividade.
     * (Vale para manutencao de correia e maquina)
     *
     * @param atividade
     * @throws ValidacaoObjetosOperacaoException
     */
    private void validaPeriodoManutencao(Atividade atividade, SituacaoPatio situacaoPatio) throws ValidacaoObjetosOperacaoException {
    /*	List<DataEntity> datasRelevantes = atividade.getDatas();
    	Date inicio = datasRelevantes.get(0);
    	Date fim = datasRelevantes.get(datasRelevantes.size() - 1);
    	
    	//Verificando se é uma atividade que possui lugares de empilhamento 
    	if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao() != null && atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().size()>0) {
    		LugarEmpilhamentoRecuperacao lugarEmp = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);

    		MaquinaDoPatio maquina = lugarEmp.getMaquinaDoPatio();

    		if (maquina != null) {
    			
    			 * Verifica se a atividade que serah criada utiliza alguma maquina. Caso
    			 * utilize, verifica se a maquina estara em periodo de manutencao.
    			 
    			if (maquina.getCorreia() != null) {

    				Correia correiaDaMaquina = maquina.getCorreia();

    				if (correiaDaMaquina.getListaManutencao() != null && correiaDaMaquina.getListaManutencao().size() > 0) {
    					for (Manutencao manutencao : correiaDaMaquina.getListaManutencao()) {

    						this.validaPeriodicidadeDatas("correia.manutencao", inicio, fim, manutencao);
    					}
    				}
    			}
    			if (maquina.getListaManutencao() != null && maquina.getListaManutencao().size() > 0) {
    				for (Manutencao manutencao : maquina.getListaManutencao()) {

    					this.validaPeriodicidadeDatas("maquina.manutencao", inicio, fim, manutencao);
    				}
    			}
    		}
    	}
    	else {
            //Verificando se é uma atualizacao de recuperacao feita direto da usina
            if (TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO.equals(atividade.getTipoAtividade()) &&
            		atividade.getListaDeAtividadesCampanha() != null && atividade.getListaDeAtividadesCampanha().size()>0) {
            	
                Correia correiaC1 = obterCorreiaC1Equivalente(situacaoPatio);
    			if (correiaC1.getListaManutencao() != null && correiaC1.getListaManutencao().size() > 0) {
    				for (Manutencao manutencao : correiaC1.getListaManutencao()) {
    					this.validaPeriodicidadeDatas("correia.manutencao", inicio, fim, manutencao);
    				}
    			}
                
            }
    	}
*/    }

    /**
     * Esse metodo valida a periodicidade das datas de inicio e fim de manutencao, afim de conferir se
     * em alguma atividade futura, existirá uma manutencao agendada.
     *
     * @param mensagem
     * @param inicio
     * @param fim
     * @param manutencao
     * @throws ValidacaoObjetosOperacaoException
     */
    private void validaPeriodicidadeDatas(String mensagem, Date inicio, Date fim, Manutencao manutencao) throws ValidacaoObjetosOperacaoException {
        // Cria calendar para a data do inicio da atividade
        Calendar calendarioInicioAtividade = Calendar.getInstance();
        calendarioInicioAtividade.setTime(inicio);

        //Cria calendar para a data de inicio da manutencao
        Calendar calendarioInicioManutencao = Calendar.getInstance();
        calendarioInicioManutencao.setTime(manutencao.getDataInicial());

        //Cria calendar para a data de fim da atividade
        Calendar calendarioFimAtividade = Calendar.getInstance();
        calendarioFimAtividade.setTime(fim);

        //Cria calendar para a data de fim da manutencao
        Calendar calendarioFimManutencao = Calendar.getInstance();
        calendarioFimManutencao.setTime(manutencao.getDataFinal());

        // Verifica se a manutencao possui periodicidade definida como "NUNCA"
        if (manutencao.getPeriodicidade().equals(PeriodicidadeEnum.NUNCA)) {
            if (calendarioInicioAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime())
            		|| calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().before(calendarioFimManutencao.getTime()) 
            		|| calendarioInicioAtividade.getTime().before(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime())
            		&& calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().after(calendarioFimManutencao.getTime())) {

                throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage(mensagem));
            }
        } // Verifica se a manutencao possui periodicidade definida como "DIARIA"
        else if (manutencao.getPeriodicidade().equals(PeriodicidadeEnum.DIARIA)) {
            /* Verifica qual o periodo de manutencao e trás o periodo para uma data futura, para realizar a comparacao com as datas da atividade */
            Double diferencaInicioFimManutencaoEmMinutos = DSSStockyardTimeUtil.diferencaEmMinutos(calendarioInicioManutencao.getTime(), calendarioFimManutencao.getTime());
            calendarioInicioManutencao.set(calendarioInicioAtividade.get(Calendar.YEAR), calendarioInicioAtividade.get(Calendar.MONTH), calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH));
            calendarioFimManutencao.set(calendarioFimAtividade.get(Calendar.YEAR), calendarioInicioAtividade.get(Calendar.MONTH), calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH), calendarioInicioManutencao.get(Calendar.HOUR_OF_DAY), calendarioInicioManutencao.get(Calendar.MINUTE));
            calendarioFimManutencao.add(Calendar.MINUTE, diferencaInicioFimManutencaoEmMinutos.intValue());
            /* ******************************* */
            // Verifica se os periodos da atividade e da manutencao possuem algum conflito que impeca a realizacao da atividade
            if (calendarioInicioAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                    calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                    calendarioInicioAtividade.getTime().before(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) &&
                    calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().after(calendarioFimManutencao.getTime())) {

                throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage(mensagem));
            }
        } // Verifica se a manutencao possui periodicidade definida como "SEMANAL"
        else if (manutencao.getPeriodicidade().equals(PeriodicidadeEnum.SEMANAL)) {

            /* Verifica qual o periodo de manutencao e trás o periodo para uma data futura, para realizar a comparacao com as datas da atividade */
            Double diferencaInicioFimManutencaoEmMinutos = DSSStockyardTimeUtil.diferencaEmMinutos(calendarioInicioManutencao.getTime(), calendarioFimManutencao.getTime());
            calendarioInicioManutencao.set(calendarioInicioAtividade.get(Calendar.YEAR), calendarioInicioAtividade.get(Calendar.MONTH), calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH));
            calendarioFimManutencao.set(calendarioFimAtividade.get(Calendar.YEAR), calendarioInicioAtividade.get(Calendar.MONTH), calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH), calendarioInicioManutencao.get(Calendar.HOUR_OF_DAY), calendarioInicioManutencao.get(Calendar.MINUTE));
            calendarioFimManutencao.add(Calendar.MINUTE, diferencaInicioFimManutencaoEmMinutos.intValue());
            /* ******************************* */

            // Verifica se o dia da atividade na semana eh o mesmo da manutencao
            if (calendarioInicioAtividade.get(Calendar.DAY_OF_WEEK) == calendarioInicioManutencao.get(Calendar.DAY_OF_WEEK)) {

                // Verifica se os periodos da atividade e da manutencao possuem algum conflito que impeca a realizacao da atividade
                if (calendarioInicioAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                        calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                        calendarioInicioAtividade.getTime().before(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) &&
                        calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().after(calendarioFimManutencao.getTime())) {

                    throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage(mensagem));
                }
            }
        } // Verifica se a manutencao possui periodicidade definida como "QUINZENAL"
        else if (manutencao.getPeriodicidade().equals(PeriodicidadeEnum.QUINZENAL)) {
            if (calendarioInicioAtividade.get(Calendar.DAY_OF_WEEK) == calendarioInicioManutencao.get(Calendar.DAY_OF_WEEK)) {

                Calendar dataTmp = Calendar.getInstance();
                dataTmp.setTime(calendarioInicioManutencao.getTime());
                while (!calendarioInicioAtividade.getTime().equals(dataTmp.getTime())) {
                    if ((dataTmp.get(Calendar.DAY_OF_MONTH) == calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH)) &&
                            (dataTmp.get(Calendar.MONTH) == calendarioInicioAtividade.get(Calendar.MONTH)) &&
                            (dataTmp.get(Calendar.YEAR) == calendarioInicioAtividade.get(Calendar.YEAR))) {

                        /* Verifica qual o periodo de manutencao e trás o periodo para uma data futura, para realizar a comparacao com as datas da atividade */
                        Double diferencaInicioFimManutencaoEmMinutos = DSSStockyardTimeUtil.diferencaEmMinutos(calendarioInicioManutencao.getTime(), calendarioFimManutencao.getTime());
                        calendarioFimManutencao.set(dataTmp.get(Calendar.YEAR), dataTmp.get(Calendar.MONTH), dataTmp.get(Calendar.DAY_OF_MONTH), dataTmp.get(Calendar.HOUR_OF_DAY), dataTmp.get(Calendar.MINUTE));
                        calendarioFimManutencao.add(Calendar.MINUTE, diferencaInicioFimManutencaoEmMinutos.intValue());
                        /* ******************************* */

                        // Verifica se o dia da atividade na semana eh o mesmo da manutencao
                        if (calendarioInicioAtividade.get(Calendar.DAY_OF_WEEK) == calendarioInicioManutencao.get(Calendar.DAY_OF_WEEK)) {

                            // Verifica se os periodos da atividade e da manutencao possuem algum conflito que impeca a realizacao da atividade
                            if (calendarioInicioAtividade.getTime().after(dataTmp.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                                    calendarioFimAtividade.getTime().after(dataTmp.getTime()) && calendarioFimAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                                    calendarioInicioAtividade.getTime().before(dataTmp.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) &&
                                    calendarioFimAtividade.getTime().after(dataTmp.getTime()) && calendarioFimAtividade.getTime().after(calendarioFimManutencao.getTime())) {

                                throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage(mensagem));
                            }
                        }
                    }
                    dataTmp.add(Calendar.DAY_OF_MONTH, 14);
                }
            }
        } // Verifica se a manutencao possui periodicidade definida como "MENSAL"
        else if (manutencao.getPeriodicidade().equals(PeriodicidadeEnum.MENSAL)) {
            /* Verifica qual o periodo de manutencao e trás o periodo para uma data futura, para realizar a comparacao com as datas da atividade */
            Double diferencaInicioFimManutencaoEmMinutos = DSSStockyardTimeUtil.diferencaEmMinutos(calendarioInicioManutencao.getTime(), calendarioFimManutencao.getTime());
            calendarioInicioManutencao.set(calendarioInicioAtividade.get(Calendar.YEAR), calendarioInicioAtividade.get(Calendar.MONTH), calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH));
            calendarioFimManutencao.set(calendarioFimAtividade.get(Calendar.YEAR), calendarioInicioAtividade.get(Calendar.MONTH), calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH), calendarioInicioManutencao.get(Calendar.HOUR_OF_DAY), calendarioInicioManutencao.get(Calendar.MINUTE));
            calendarioFimManutencao.add(Calendar.MINUTE, diferencaInicioFimManutencaoEmMinutos.intValue());
            /* ******************************* */

            // Verifica se o dia da atividade na semana eh o mesmo da manutencao
            if (calendarioInicioAtividade.get(Calendar.DAY_OF_MONTH) == calendarioInicioManutencao.get(Calendar.DAY_OF_MONTH)) {

                // Verifica se os periodos da atividade e da manutencao possuem algum conflito que impeca a realizacao da atividade
                if (calendarioInicioAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                        calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().before(calendarioFimManutencao.getTime()) ||
                        calendarioInicioAtividade.getTime().before(calendarioInicioManutencao.getTime()) && calendarioInicioAtividade.getTime().before(calendarioFimManutencao.getTime()) &&
                        calendarioFimAtividade.getTime().after(calendarioInicioManutencao.getTime()) && calendarioFimAtividade.getTime().after(calendarioFimManutencao.getTime())) {

                    throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage(mensagem));
                }
            }
        }
    }

    @Override
    public void atualizacaoPilhaEmergencia(Atividade atividadeAtualizacaoPilhaEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException
    {
    
       this.consolidacaoRealizada = Boolean.FALSE;
    }

    @Override
    public void transportarPilhaEmergencia(Atividade atividadeTransportarPilhaEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException
    {
      /* // adiciona atividade em um ou mais intervalos de execucao existentes ou cria novos
       thior// e adiciona a atividade neles.
       this.quebraAtividade(atividadeTransportarPilhaEmergencia);
       ControladorPlanejamento.validaInterdicaoBaliza(atividadeTransportarPilhaEmergencia);
       Date dataInicial = atividadeTransportarPilhaEmergencia.getDatas().get(0);
       if (dataInicial.compareTo(controladorDePlano.obterSituacaoPatioInicial().getDtInicio()) <= 0) {
           throw new AtividadeException(PropertiesUtil.buscarPropriedade("exeption.erro.atividade.dataInicial"));
       }
       //controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
*/       this.consolidacaoRealizada = Boolean.FALSE;
    }

    @Override
    public void movimentarPilhaEmergencia(Atividade atividadeMovimentarPilhaEmergencia, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException
    {
       /*// adiciona atividade em um ou mais intervalos de execucao existentes ou cria novos
       // e adiciona a atividade neles.
       this.quebraAtividade(atividadeMovimentarPilhaEmergencia);
       ControladorPlanejamento.validaInterdicaoBaliza(atividadeMovimentarPilhaEmergencia);
       Date dataInicial = atividadeMovimentarPilhaEmergencia.getDatas().get(0);
       if (dataInicial.compareTo(controladorDePlano.obterSituacaoPatioInicial().getDtInicio()) <= 0) {
           throw new AtividadeException(PropertiesUtil.buscarPropriedade("exeption.erro.atividade.dataInicial"));
       }
      // controladorDePlano.atualizarPlano(controladorDeIntervalosDeExecucao);
*/       this.consolidacaoRealizada = Boolean.FALSE;
    }
    
    @Override
    public void movimentarPilhaPSMPelletFeed(Atividade atividadeMovimentar, SituacaoPatio situacaoPatioAtual) throws AtividadeException, ValidacaoObjetosOperacaoException
    {
        SituacaoPatio situacaoPatio = getControladorDePlano().getPlanoEmpilhamentoRecuperacao().gerarSituacao(atividadeMovimentar);
        getControladorDePlano().adicionarSituacaoPatioNaLista(situacaoPatio);
        this.consolidacaoRealizada = Boolean.FALSE;
        try {
            this.consolidarPlano();
        } catch (ErroSistemicoException e) {
            // TODO Auto-generated catch block
            throw new AtividadeException(e);
        }
    }
      
    /**
     * Retorna o objeto correia C1 para passar o produto das usinas
     * @param sitPatio
     * @return
     */
    private Correia obterCorreiaC1Equivalente(SituacaoPatio sitPatio)
    {
       Correia correiaC1 = null;
       try {
		for(Correia correia : sitPatio.getPlanta().getListaCorreias(sitPatio.getDtInicio())){
		       if(correia.getNomeCorreia().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("parametro.nome.correiaC1"))){
		           correiaC1 = correia;
		           break;
		       }
		   }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       return correiaC1;
    }

   

}
