package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.ObjetoPesquisadoNaoEncontradoException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;


/**
 * Interface de acesso às operações do subsistema de interface gráfica de fila de navios.
 * 
 * @author andre
 * 
 */
public interface IControladorInterfaceFilaDeNavios {

    /**
     * Seleciona uma determinada carga e cacula o seu blend
     * @param cargaSelecionada
     * @throws BlendagemInvalidaException
     * @throws ProdutoIncompativelException
     * @throws CampanhaIncompativelException
     * @throws ExcessoDeMaterialParaEmbarqueException
     */
    public void selecionaCarga(Carga cargaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException, CargaSelecionadaException, ExcessoDeMaterialParaEmbarqueException;

    /**
     * Remove a selecao uma determinada carga e cacula o seu blend
     * @param cargaSelecionada
     * @throws BlendagemInvalidaException
     * @throws ProdutoIncompativelException
     */
    public void deselecionaCarga(Carga cargaSelecionada) throws BlendagemInvalidaException, ProdutoIncompativelException;

    /**
     * Ativa a exibicao da mensagem na tela inicial
     * @param InterfaceMensagem
     */
    public void ativarMensagem(InterfaceMensagem interfaceMensagem);

    /** Retorno o modo de operacao ativo */
    public ModoDeOperacaoEnum obterModoOperacao();

    /** Buscar fila de navios cadastrada no sistema
     *
     * @return a fila de navios cadastrada
     */
    public void buscarFilaDeNaviosCadastrada() throws ObjetoPesquisadoNaoEncontradoException, ErroSistemicoException;

    /**verifica se os componentes pertencentes a FilaDeNavios podem ser editados, caso contrario retorna uma Exception */
     public void verificarModoDeEdicao() throws ModoDeEdicaoException;

     /** cria atividade para desatracar um navio do pier (berco) */
     public Atividade criaAtividadeDesatracarNavio(Navio navio, Date date);

     /** verifica se o navio deve ser desatracado do pier(berco)*/
     public Atividade verificaDesatracacaoDoNavioNoBerco(ControladorInterfaceInicial controlador);

     /** verifica se existe algum navio na fila com status de embarque em "A"( atracado )*/
     public Navio verificaStatusAtracadoDoNavio(FilaDeNavios filaNavio);

     /** cria atividade para atracar um navio */
     public Atividade criaAtividadeAtracarNavio(AtividadeAtracarDesAtracarNavioVO movimentacaoVO,Date data)  throws AtividadeException;

     /** Metodo que procura um determinado navio em uma determinada fila */
     public Navio verificarNavioExistenteNaFila(FilaDeNavios filaNavios, Navio navio);

     /** Metodo que procura uma determinada carga em um determinado navio */
     public Carga verificaCargaExistenteNoNavio(Navio navio, Carga carga);
     public List<Cliente> buscarClientes() throws ErroSistemicoException;

}
