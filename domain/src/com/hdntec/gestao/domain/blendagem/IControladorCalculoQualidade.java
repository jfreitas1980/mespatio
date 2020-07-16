package com.hdntec.gestao.domain.blendagem;

import java.util.List;

import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;


/**
 * Interface que dá acesso às funcionalidades do subsistema de cálculo da qualidade. Este subsistema será acessado principalmente pela interface de diálogo com o usuário, que lhe fornecerá produtos vindos de campanhas ou balizas selecionadas para simulações de possíveis blendagens para atender a cargas a ser embarcadas.
 * 
 * @author andre
 * 
 */
public interface IControladorCalculoQualidade {

    /**
     * Insere uma determinada {@link Campanha} de {@link Usina} selecionada no grupo blendado e retorna uma {@link Blendagem} para ser apresentada na interface de diálogo. Realiza o cálculo de blendagem do {@link Produto} de uma campanha de uma usina selecionada na interface de diálogo do sistema com as outras campanhas ou balizas selecionadas em determinado instante.
     * 
     * 
     * @param campanha
     *           a campanha cujo produto será utilizado na blendagem simulada
     * @param quantidade a quantidade selecionada para a blendagem
     * @param tipoDeProdutoSelecionado o tipo de produto selecionado na usina para blendagem           
     * @return a blendagem resultante da adição do produto da campanha para ser apresentada na interface de diálogo
     * @throws BlendagemInvalidaException 
     * @throws CampanhaIncompativelException caso campanha selecionada seja incompatível com os produtos previamente selecionados para blendagem
     * @throws  ExcessoDeMaterialParaEmbarqueException caso a quantidade de material selecionada para blendagem exceda a quantidade de embarque da carga
     * @throws BlendagemIrrelevanteException quando o material selecionado não é pelota, é irrelevante calcular a blendagem
     */
    public Blendagem blendarCampanhaDeUsina(Campanha campanha, Double quantidade, TipoDeProdutoEnum tipoDeProdutoSelecionado) throws BlendagemInvalidaException, CampanhaIncompativelException,  ExcessoDeMaterialParaEmbarqueException, ProdutoIncompativelException;

    /**
     * Remove uma determinada {@link Campanha} de {@link Usina} selecionada no grupo blendado e retorna uma {@link Blendagem} para ser apresentada na interface de diálogo. Realiza o cálculo de blendagem do {@link Produto} de uma campanha de uma usina selecionada na interface de diálogo do sistema com as outras campanhas ou balizas selecionadas em determinado instante.
     * 
     * 
     * @param campanha
     *           a campanha cujo produto será utilizado na blendagem simulada
     * @return a blendagem resultante da adição do produto da campanha para ser apresentada na interface de diálogo
     * @throws BlendagemInvalidaException 
     * @throws BlendagemIrrelevanteException quando o material selecionado não é pelota, é irrelevante calcular a blendagem
     */
    public Blendagem removerBlendagemCampanhaDeUsina(Campanha campanha) throws BlendagemInvalidaException, ProdutoIncompativelException ;

    /**
     * Insere uma determinada {@link Baliza} selecionada no grupo blendado e retorna uma {@link Blendagem} para ser apresentada na interface de diálogo. Utiliza o {@link Produto} da baliza selecionada para calcular a blendagem simulada com outros produtos previamente selecionados na interface, retornado uma blendagem para ser apresentada na tela.
     * 
     * @param baliza
     *           a baliza cujo produto será utilizado na blendagem
     * @return a blendagem resultante da adição do produto da baliza para ser apresentada na interface de diálogo
     * @throws BlendagemInvalidaException caso a blendagem seja inválida
     * @throws ProdutoIncompativelException  caso o produto selecionado seja incompatível
     * @throws  ExcessoDeMaterialParaEmbarqueException caso o material selecionado para blendagem exceda a quantidade necessária para embarque da carga
     * @throws BlendagemIrrelevanteException quando o material selecionado não é pelota, é irrelevante calcular a blendagem
     */
    public Blendagem blendarBaliza(Baliza baliza) throws BlendagemInvalidaException, ProdutoIncompativelException,  ExcessoDeMaterialParaEmbarqueException;

    /**
     * Remove uma determinada {@link Baliza} selecionada no grupo blendado e retorna uma {@link Blendagem} para ser apresentada na interface de diálogo. Utiliza o {@link Produto} da baliza selecionada para calcular a blendagem simulada com outros produtos previamente selecionados na interface, retornado uma blendagem para ser apresentada na tela.
     * 
     * @param baliza
     *           a baliza cujo produto será utilizado na blendagem
     * @return a blendagem resultante da remocao do produto da baliza para ser apresentada na interface de diálogo
     * @throws BlendagemInvalidaException 
     * @throws ProdutoIncompativelException 
     * @throws BlendagemIrrelevanteException quando o material selecionado não é pelota, é irrelevante calcular a blendagem
     */
    public Blendagem removerBlendagemBaliza(Baliza baliza) throws BlendagemInvalidaException, ProdutoIncompativelException;

    /**
     * Seleciona uma determinada {@link Carga} para ser atendida pelo grupo selecionado para {@link Blendagem}. Essa carga irá proporcionar as metas para os valores de cada {@link ItemDeControleBlendado}.
     * 
     * @param carga
     *           a carga selecionada para ser atendida pelo grupo blendado
     * @return a blendagem resultante do atendimento da carga selecionada pelo grupo blendado selecionado para ser apresentada na interface de diálogo
     * @throws BlendagemInvalidaException 
     * @throws ProdutoIncompativelException
     * @throws CargaSelecionadaException
     * @throws  ExcessoDeMaterialParaEmbarqueException
     * @throws BlendagemIrrelevanteException quando o material selecionado não é pelota, é irrelevante calcular a blendagem
     */
    public Blendagem atenderCarga(Carga carga) throws BlendagemInvalidaException, ProdutoIncompativelException, CargaSelecionadaException, ExcessoDeMaterialParaEmbarqueException ;

    /**
     * Remove a selecao de uma determinada {@link Carga} para ser atendida pelo grupo selecionado para {@link Blendagem}. Essa carga irá proporcionar as metas para os valores de cada {@link ItemDeControleBlendado}.
     * 
     * @param carga
     *           a carga selecionada para ser removida pelo grupo blendado
     * @return a blendagem resultante da remocao da carga selecionada pelo grupo blendado selecionado para ser apresentada na interface de diálogo
     * @throws BlendagemInvalidaException 
     * @throws ProdutoIncompativelException  
     * @throws BlendagemIrrelevanteException quando o material selecionado não é pelota, é irrelevante calcular a blendagem
     */
    public Blendagem removerCarga(Carga carga) throws BlendagemInvalidaException, ProdutoIncompativelException;

    /**
     * atribui o objeto de blendagem para seu controlador
     * @param blendagem
     */
    public void setBlendagem(Blendagem blendagem);

    /**
     * Retorna a lista de balizas que tiveram seus produtos blendados
     * @return List<Baliza>
     */
    public List<Baliza> obterListaBalizasBlendada();

    /**
     * Retorna a lista de campanhas que tiveram seus produtos blendados
     * @return List<Campanha>
     */
    public List<Campanha> obterListaCampanhasBlendadas();

    /**
     * Limpa a blendagem quando ha uma mudanca de situacao apresentada
     */
    public void limparBlendagem();
 
    /** 
     * Pega a Blendagem de materiais existente atualmente
     * */
    public Blendagem getBlendagem();
    
    /**
     * Instancia a blendagem dentro do controlador
     */
    public void instanciaBlendagem();
    
    /**
     * 
     * @return o produto resultante da blendagem
     */
    public Produto getProdutoResultanteDaBlendagem();
    
    /**
     * Adiciona o produto passado como parâmetro para a lista de produtos selecionados para blendagem. Assim que o produto é adicionado à  lista, deve-se calcular a blendagem incorporando seus valores nas médias. Caso algum erro ocorra no cálculo, deve-se retirar o produto a lista.
     * 
     * @param produto
     *           o produto a ser inserido na lista de produtos blendados
     * @throws ProdutoIncompativelException
     *            caso o produto selecionado seja incompatível
     */
    public void inserirProdutoNaBlendagem(Produto produto) throws ProdutoIncompativelException;
    
    /**
     * Retira produto da blendagem passando o produto como parametro.
     * 
     * @param produto
     *            Produto da baliza a ser retirada da blendagem.
     * @return
     * @throws ProdutoIncompativelException
     */
    public void retirarProdutoDaBlendagem(Produto produto) throws ProdutoIncompativelException;
}
