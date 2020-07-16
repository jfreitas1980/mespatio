package com.hdntec.gestao.integracao.controladores;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.integracao.IntegracaoSAP;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Interface de acesso ao subsistema de modelo de domínio.
 * <p>
 * Requisitos especiais: - Distribuída.
 * 
 * @author andre
 * 
 */
@Remote
public interface IControladorModelo {

    /**
     * salva um plano de empilhamento e recuperação
     *
     * @param planoDeEmpilhamentoERecuperacao
     */
    public PlanoEmpilhamentoRecuperacao salvarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * atualiza um plano de empilhamento e recuperação
     *
     * @param planoDeEmpilhamentoERecuperacao
     */
    public void atualizarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * Busca o(s) plano(s) de empilhamento e recuperacao
     *
     * @param planoDeEmpilhamentoERecuperacao
     * @return List<PlanoDeEmpilhamentoERecuperacao>
     */
    public List<PlanoEmpilhamentoRecuperacao> buscarPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * Busca o plano de empilhamento e recuperacao que é oficial
     *
     * @return {@link PlanoEmpilhamentoRecuperacao}
     */
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial(Long qtdeHorasSituacoesAnteriores) throws ErroSistemicoException;

     /**
     * Busca o plano de empilhamento e recuperacao que é oficial
     *
     * @return {@link PlanoEmpilhamentoRecuperacao}
     */
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficial() throws ErroSistemicoException;

    /**
     * Remove um plano de empilhamento e recuperacao
     *
     * @param planoDeEmpilhamentoERecuperacao
     */
    public void removerPlanoDeEmpilhamentoERecuperacao(PlanoEmpilhamentoRecuperacao planoDeEmpilhamentoERecuperacao) throws ErroSistemicoException;

    /**
     * Persiste o objeto planta na entidade
     *
     * @param planta
     *            Objeto planta a ser persistido
     */
    public void salvaPlanta(Planta planta) throws ErroSistemicoException;

    /**
     * Persiste o objeto Situação do patio no banco
     *
     * @param situacaoPatio
     */
    public SituacaoPatio salvaSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException;

    /**
     * Persiste o objeto Produto do patio no banco
     *
     * @param produto
     */
    public void salvaProduto(Produto produto) throws ErroSistemicoException;

    /**
     * Persiste o objeto Tipo de Produto do patio no banco
     *
     * @param produto
     */
    public TipoProduto salvaTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException;

    /**
     * busca os tipo de produto do banco de dados remotamente.
     * @param tipoProduto
     * @return
     */
    public List<TipoProduto> buscarTiposProduto(TipoProduto tipoProduto) throws ErroSistemicoException;

    /**
     * busca os tipo de item de controle do banco de dados remotamente.
     * @param tipoItemDeControle
     * @return
     */
    public List<TipoItemDeControle> buscarTiposItemDeControle(TipoItemDeControle tipoItemDeControle) throws ErroSistemicoException;

    /**
     * adiciona as novas situações geradas na atualização dos sistemas externos
     * @param listaDeNovasSituacoesDePatio
     */
    public void adicionaEOrdenaNovasSituacoesNoPlanoOficial(List<SituacaoPatio> listaDeNovasSituacoesDePatio) throws ErroSistemicoException;

    /**
     * Busca novas situações realizadas do plano de empilhamento e recuperacao oficial
     * 
     */
    public List<SituacaoPatio> buscarNovasSituacoesRealizadasDoPlanoEmpilhamentoRecuperacaoOficial(Date timestamp) throws ErroSistemicoException;

    /**
     * salva uma atividade
     *
     * @param Atividade
     */
    public Atividade salvarAtividade(Atividade atividade) throws ErroSistemicoException;

    /**
     * atualiza uma atividade
     *
     * @param Empilhamento
     */
    public void atualizarAtividade(Atividade atividade) throws ErroSistemicoException;

    /**
     * Salva no banco de dados uma lista de balizas
     *
     * @param List<Baliza> listaBalizas
     */
    public void salvaListaBalizas(List<Baliza> listaBalizas) throws ErroSistemicoException;

    /**
     * Busca todos os planos de empilhamento e recuperação da base de dados
     * de um determinado periodo que estão oficionalizados
     * @return List<PlanoEmpilhamentoRecuperacao>
     */
    public List<PlanoEmpilhamentoRecuperacao> buscarListaPlanosOficiaisPorData(Date dataInicial, Date dataFinal) throws ErroSistemicoException;

    /**
     * Salva no banco de dados uma lista de itens de controle
     *
     * @param List<ItemDeControle> listaItensDeControle
     */
    public void salvaListaTipoItemControle(List<TipoItemDeControle> listaItensControle) throws ErroSistemicoException;

    /**
     * busca os tipo de item de controle do banco de dados remotamente.
     * @return List<TipoItemDeControle>
     */
    public List<TipoItemDeControle> buscarTiposItemControle() throws ErroSistemicoException;

    /**
     * Salva no banco de dados uma fila de navios
     *
     * @param List<ItemDeControle> listaItensDeControle
     */
    public FilaDeNavios salvarFilaDeNavios(FilaDeNavios filaNavio) throws ErroSistemicoException;

    /**
     * Busca uma fila de navios persistida
     *
     * @return {@link FilaDeNavios}
     */
    public FilaDeNavios buscarFilaDeNavios(FilaDeNavios filtroFilaNavios) throws ErroSistemicoException;

    /**
     * Busca por todos os clientes cadastrados na base
     * @param cliente
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<Cliente> buscaClientes(Cliente cliente) throws ErroSistemicoException;

    /**
     * Atualiza os dados do cliente
     * @param cliente
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizaCliente(Cliente cliente) throws ErroSistemicoException;


    /**
     * Busca todas as maquinas cadastradas no sistema
     * @return - lista com todos as máquinas cadastradas
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<MaquinaDoPatio> buscarListaMaquinas() throws ErroSistemicoException;

    /**
     * Busca todas as maquinas carregadoras de navios cadastradas no sistema
     * @return - lista com todos as carregadora de navios cadastradas
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<CarregadoraDeNavios> buscarListaCarregadorasNavio() throws ErroSistemicoException;


    /**
     * Metodo que busca todos os totalizadores lidos do SAP a partir da data da ultima atualizacao
     * @param dataUltimaIntegracao
     * @return
     */
    public List<IntegracaoSAP> buscarListaDadosTotalizadosSAP(Date dataInicial, Date dataFinal) throws ErroSistemicoException;

    /**
     * Busca as informações parametrizadas por sistema
     * @param idSistema
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public IntegracaoParametros buscarParametroSistema(Long idSistema) throws ErroSistemicoException;

    /**
     * Atualiza as informações parametrizada, do sistema passado para atualizar
     * @param integracaoParametros
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void atualizaParametrosSistema(IntegracaoParametros integracaoParametros) throws ErroSistemicoException;

    /**
     * Salva as alteracoes na tabela de IntegracaoParametros
     * @param integracaoParametros
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void salvarParametrosSistemas(IntegracaoParametros integracaoParametros) throws ErroSistemicoException;


     /**
     * Atualiza no banco de dados uma fila de navios
     * @param List<FilaDeNavios> listaFilaDeNavios
     */
    public void atualizaFilaDeNavios(FilaDeNavios filaNavios) throws ErroSistemicoException;
    
    /**
     * Salva o cliente no banco
     * @param cliente
     * @return o cliente persistido
     * @throws ErroSistemicoException
     */
    public Cliente salvarCliente(Cliente cliente) throws ErroSistemicoException;

    /**
     * Salva um navio no banco
     * @param Navio
     * @return
     * @throws ErroSistemicoException
     */
    public Navio salvarNavio(Navio Navio) throws ErroSistemicoException;

    /**
     * Remove um navio do banco
     * @param navio
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void removerNavio(Navio navio) throws ErroSistemicoException;

    /**
     * Salva uma lista de tipos de produto
     * @param listaTiposProduto
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void salvaListaTiposProduto(List<TipoProduto> listaTiposProduto) throws ErroSistemicoException;



    /**
     * Salva uma lista de planos
     * @param listaPlanos
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<PlanoEmpilhamentoRecuperacao> salvarOuAtualizarPlanosDaLista(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException;

    /**
     * Remove uma lista de planos de empilhamento e recuperacao
     * @param listaPlanos
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void removeListaDePlanoEmpilhamentoRecuperacao(List<PlanoEmpilhamentoRecuperacao> listaPlanos) throws ErroSistemicoException;


    /**
     * Busca e retorna um plano nao oficial de empilhamento e recuperacao do usuario logado no sistema
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public PlanoEmpilhamentoRecuperacao  buscarPlanoEmpilhamentoRecuperacaoPorUsuario(Long idUser) throws ErroSistemicoException;
    /**
     * Salva uma situaçoes
     * @param listaPlanos
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public List<SituacaoPatio> salvaSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException;
    
    /**
     * Busca um plano oficial somente com as situacoes do periodo informado   
     * @param dataInicial
     * @param dataFinal
     * @return
     * @throws ErroSistemicoException
     */
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoOficialPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException;
    
    /**
     * Busca um plano do usuario somente com as situacoes do periodo informado
     * @param dataInicial
     * @param dataFinal
     * @return
     * @throws ErroSistemicoException
     */
    public PlanoEmpilhamentoRecuperacao buscarPlanoEmpilhamentoRecuperacaoUsuarioPorPeriodo(Long idUser, Date dataInicial, Date dataFinal) throws ErroSistemicoException;

    /**
     * Metodo que retorna a ultima situacao de patio do dia passado como parametro
     * @param dataPesquisa
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public SituacaoPatio buscarUltimaSituacaoPatioDoDia(Date dataPesquisa) throws ErroSistemicoException;

    /**
     * Salva o Pier
     * @param pier
     * @return Pier
     * @throws ErroSistemicoException
     */
    public Pier salvaPier(Pier pier) throws ErroSistemicoException;

    /**
     * Salva uma lista de manutencoes.
     * @param listaManutencoes
     * @return
     * @throws ErroSistemicoException
     */
    public List<Manutencao> salvarOuAtualizarListaDeManutencoes(List<Manutencao> listaManutencoes) throws ErroSistemicoException;

    public MaquinaDoPatio salvaMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException;

    public void removeManutencao(Manutencao manutencao) throws ErroSistemicoException;

    public MaquinaDoPatio buscarMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException;

    public List<MaquinaDoPatio> buscaListaDeMaquinasDoPatio() throws ErroSistemicoException;

    public Correia buscarCorreia(Correia correia) throws ErroSistemicoException;

    public Correia salvaCorreia(Correia correia) throws ErroSistemicoException;
    
    /**
     * Busca a situacao do patio oficial mais atual dentro do periodo informado
     * @param dataInicial
     * @param dataFinal
     * @return
     * @throws ErroSistemicoException
     */
    public SituacaoPatio buscarSituacaoPatioOficialAtualPorPeriodo(Date dataInicial, Date dataFinal) throws ErroSistemicoException;

    /**
     * Busca as situacoes de patio oficiais por um periodo e por um tipo de 
     * atividade. 
     * @return
     * @throws ErroSistemicoException
     */
    public List<SituacaoPatio> buscarListaSPOficialPorPeriodoEAtividade(
 		   Date dataInicial, Date dataFinal, 
 		   TipoAtividadeEnum tipoAtividade) throws ErroSistemicoException;
    
    /**
     * 
     * buscarFaixaAmostragem
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param faixa
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<FaixaAmostragemFrequencia>.
     */
    List<FaixaAmostragemFrequencia> buscarFaixaAmostragem(FaixaAmostragemFrequencia faixa)
	throws ErroSistemicoException;
    
    /**
     * 
     * removerFaixaAmostragemFrequencia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param faixa
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removerFaixaAmostragemFrequencia(FaixaAmostragemFrequencia faixa) throws ErroSistemicoException;
    
    /**
     * 
     * salvarFaixaAmostragemFrequencia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param faixa
     * @return
     * @throws ErroSistemicoException
     * @return Returns the FaixaAmostragemFrequencia.
     */
    FaixaAmostragemFrequencia salvarFaixaAmostragemFrequencia(FaixaAmostragemFrequencia faixa) throws ErroSistemicoException;
    
	/**
	 * 
	 * buscarTabelaAmostragem
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param tabela
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<TabelaAmostragemFrequencia>.
	 */
	List<TabelaAmostragemFrequencia> buscarTabelaAmostragem(TabelaAmostragemFrequencia tabela) throws ErroSistemicoException;
	
	/**
	 * 
	 * salvarTabelaAmostragemFrequencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param tabela
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the TabelaAmostragemFrequencia.
	 */
	TabelaAmostragemFrequencia salvarTabelaAmostragemFrequencia(TabelaAmostragemFrequencia tabela) throws ErroSistemicoException;
	
	/**
	 * 
	 * removerTabelaAmostragemFrequencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param tabela
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	void removerTabelaAmostragemFrequencia(TabelaAmostragemFrequencia tabela) throws ErroSistemicoException;
	
	/**
	 * 
	 * recuperarTabelaAmostragemAtual
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @return
	 * @return Returns the TabelaAmostragemFrequencia.
	 */
	TabelaAmostragemFrequencia recuperarTabelaAmostragemAtual();
	
	/**
     * Busca as situacoes de patio por periodo e carrega também a lista de 
     * relatorios (Deslocamento de maquina) da atividade de cada situacao.  
     * buscarSituacaoPatioRelAtividade
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 13/08/2009
     * @see
     * @param dataInicial
     * @param dataFinal
     * @param ehOficial
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<SituacaoPatio>.
     */
    List<SituacaoPatio> buscarSituacaoPatioRelAtividade(Date dataInicial,
    		Date dataFinal, boolean ehOficial) throws ErroSistemicoException;
    
    public void removerSituacaoPatio(List<SituacaoPatio> lstSituacaoPatio) throws ErroSistemicoException;

    /**
     * Remove uma unica situacao de patio
     * @param situacaoPatio
     * @throws ErroSistemicoException
     */
    public void removerSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException;

    /**
     * Atualiza a lista de balizas de cada patio
     * @param listaPatios
     * @throws ErroSistemicoException
     */
    //TODO Darley SAxxxx
    public List<Patio> atualizarListaBalizasPorPatios(List<Patio> listaPatios) throws ErroSistemicoException;
    
    /**
     * 
     * @param listaPatios
     * @return
     * @throws ErroSistemicoException
     */
    //TODO Darley SAxxxx
    public List<Patio> atualizarMaquinasDoPatio(List<Patio> listaPatios) throws ErroSistemicoException;

    /**
     * 
     * @return
     * @throws ErroSistemicoException
     */
    public List<Correia> atualizarListaMaquinasCorreias(List<Correia> correias);
    
    /**
     * 
     * @param usinas
     * @return
     */
    public List<Usina> atualizarListaEmergenciaUsinas(List<Usina> usinas);
    
    /**
     * 
     * @return
     */
    public void encerrarSessao();
    
    /**
     * Atualiza uma unica situacao de patio
     * @param situacaoPatio
     * @throws ErroSistemicoException
     */
    public void atualizarSituacaoPatio(SituacaoPatio situacaoPatio) throws ErroSistemicoException;

    /**
     * 
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    public List<MaquinaDoPatio> buscaListaMaquinaDoPatioPorManutencao(
    		MaquinaDoPatio maquina, Date dataInicial, Date dataFinal);
    
    public List<Correia> buscaListaCorreiaPorManutencao(
    		Correia correia, Date dataInicial, Date dataFinal);
    
    public List<MetaMaquinaDoPatio> buscarMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException;
    public List<Cliente> buscaClientes() throws ErroSistemicoException;

        
}
