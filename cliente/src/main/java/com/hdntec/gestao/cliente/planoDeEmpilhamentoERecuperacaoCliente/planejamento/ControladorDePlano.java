package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.plano.comparadores.ComparadorSituacoesPatio;
import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.plano.dao.SituacaoPatioDAO;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;


/**
 * Classe que gerencia o plano de empilhamento e recuperacao do usuario
 * logado no sistema
 * @author Rodrigo Luchetta
 */
public class ControladorDePlano
{

   /** O plano de empilhamento e recuperacao do usuario */
   private PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;

   /** A lista de situacoes de patio deste plano */
   private List<SituacaoPatio> listaSituacoesPatio;

   /** O controlador de planejamento de empilhamento e recuperacao */
   private ControladorPlanejamento controladorPlanejamento;

   /** a fila de navios alterada apos uma edicao*/
   FilaDeNavios filaDeNaviosAlterada = null;

   public ControladorDePlano(PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao, ControladorPlanejamento controladorPlanejamento)
   {
      this.planoEmpilhamentoRecuperacao = planoEmpilhamentoRecuperacao;
      this.listaSituacoesPatio = planoEmpilhamentoRecuperacao.getListaSituacoesPatio();
      // ... ordena a lista de situacoes persistidas
      Collections.sort(listaSituacoesPatio, new ComparadorSituacoesPatio());
      this.controladorPlanejamento = controladorPlanejamento;
      // inicializando a fila de navios que foi alterada
      filaDeNaviosAlterada = null;
   }

   /** retorna o plano de empilhamento e recuperacao do usuario */
   public PlanoEmpilhamentoRecuperacao getPlanoEmpilhamentoRecuperacao()
   {
      return planoEmpilhamentoRecuperacao;
   }

   /** seta o plano de empilhamento e recuperacao do usuario */
   public void setPlanoEmpilhamentoRecuperacao(PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao)
   {
      this.planoEmpilhamentoRecuperacao = planoEmpilhamentoRecuperacao;
      listaSituacoesPatio = planoEmpilhamentoRecuperacao.getListaSituacoesPatio();
   }

   /**
    * Metodo que adiciona uma nova situacao de patio na lista e a coloca em ordem
    * de data
    * @param situacaoPatio
    */
   public void adicionarSituacaoPatioNaLista(SituacaoPatio situacaoPatio)
   {

      listaSituacoesPatio.add(situacaoPatio);
      situacaoPatio.setPlanoEmpilhamento(this.planoEmpilhamentoRecuperacao);
      Collections.sort(listaSituacoesPatio, new ComparadorSituacoesPatio());
   }

   /** Retorna a lista de situacoes de patio do plano do usuario */
   public List<SituacaoPatio> obterSituacaoPatios()
   {
      return listaSituacoesPatio;
   }

   public void salvarPlanoDeUsuario() throws ErroSistemicoException
   {
      verificaPlanoUsuarioExistente(planoEmpilhamentoRecuperacao.getIdUser());
      
      /**Criando clone das situação para associar ao novo usuario*/
      List<SituacaoPatio> lstSitucaoClone = new ArrayList<SituacaoPatio>();
      //Darley removendo chamada remota
//      IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
      IControladorModelo controladorModelo = new ControladorModelo();
      PlanoEmpilhamentoRecuperacao planoOficial = controladorModelo.buscarPlanoEmpilhamentoRecuperacaoOficial();      
      PlanoEmpilhamentoRecuperacao planoUsuario = new PlanoEmpilhamentoRecuperacao();
      for (SituacaoPatio sit : planoOficial.getListaSituacoesPatio())
      {
         SituacaoPatio nova = new SituacaoPatio();
         nova.setPlanoEmpilhamento(planoUsuario);
        // nova.setIdUser(helper.cloneLong(planoEmpilhamentoRecuperacao.getIdUser()));
         lstSitucaoClone.add(nova);
      }

    //  planoUsuario.setIdUser(helper.cloneLong(planoEmpilhamentoRecuperacao.getIdUser()));
      planoUsuario.setListaSituacoesPatio(lstSitucaoClone);
      planoUsuario.setDtInsert(new Date(System.currentTimeMillis()));
      planoUsuario.setEhOficial(Boolean.FALSE);
     // planoUsuario.setDataInicio(helper.cloneDate(planoEmpilhamentoRecuperacao.getDataInicio()));
     // planoUsuario.setHorizonteDePlanejamento(helper.cloneLong(planoEmpilhamentoRecuperacao.getHorizonteDePlanejamento()));
      controladorModelo.salvarPlanoDeEmpilhamentoERecuperacao(planoUsuario);
      controladorModelo = null;
   }

   private void apagarPlanoUsuario(PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) throws ErroSistemicoException
   {
       //Darley removendo chamada remota
//     IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
       IControladorModelo controladorModelo = new ControladorModelo();
       controladorModelo.removerPlanoDeEmpilhamentoERecuperacao(planoEmpilhamentoRecuperacao);
       controladorModelo = null;
   }

   /**
    * Salva o plano de empilhamento e recuperacao do usuario na base
    */
   public void consolidarPlano() throws ErroSistemicoException
   {
      // ... percorre a lista de situacoes de patio e limpa os ids das situacoes
      // ... que ainda nao foram persistidas, pois estas estao com id fake
      for (SituacaoPatio situacaoPatio : listaSituacoesPatio)
      {
         if (situacaoPatio.getSituacaoPatioPersistida() == null || !situacaoPatio.getSituacaoPatioPersistida())
         {
            situacaoPatio.setPlanoEmpilhamento(planoEmpilhamentoRecuperacao);
            situacaoPatio.setIdUser(planoEmpilhamentoRecuperacao.getIdUser());
         }
      }
      planoEmpilhamentoRecuperacao.setDtUpdate(new Date(System.currentTimeMillis()));      
      // ... salva na base de dados o plano de empilhamento e recuperacao do ususario

      //Darley removendo chamada remota
    //  IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
      IControladorModelo controladorModelo = new ControladorModelo();
      
      listaSituacoesPatio = controladorModelo.salvaSituacaoPatio(listaSituacoesPatio);      
      
      AtividadeDAO dao = new AtividadeDAO();
      
      dao.removePilhaPendente();
      
      planoEmpilhamentoRecuperacao.getListaSituacoesPatio().clear();
      planoEmpilhamentoRecuperacao.getListaSituacoesPatio().addAll(listaSituacoesPatio);
      
      // ... atribuindo a lista salva para a lista da memoria
      listaSituacoesPatio = planoEmpilhamentoRecuperacao.getListaSituacoesPatio();

      // ... percorrendo as situacoes de patio do plano e setando o atributo sitacaoPatioPersistida para true
      for (SituacaoPatio situacaoPatio : listaSituacoesPatio)
      {
         situacaoPatio.setSituacaoPatioPersistida(Boolean.TRUE);
      }

      // ordena a lista de situacoes da memoria
      Collections.sort(listaSituacoesPatio, new ComparadorSituacoesPatio());

      // atualiza o patio apos a consolidacao
      controladorPlanejamento.getControladorInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorPlanejamento.getControladorInterfaceInicial().getInterfaceInicial().montaPatio();
      controladorModelo = null;

   }

   public void removerPlano(Integer indiceDaSituacaoDePatioExibida) throws ErroSistemicoException {

       IControladorModelo controladorModelo = new ControladorModelo();
       try {       
       // ... carrega uma lista temporaria de todas as situacoes que serao removidas da base
      // ... para evitar erro de concorrencia da lista de situacoes
      List<SituacaoPatio> listaSituacoesRemocao = new ArrayList<SituacaoPatio>();

      if (indiceDaSituacaoDePatioExibida == listaSituacoesPatio.size()) {
          indiceDaSituacaoDePatioExibida--;
      }
      for (int i = indiceDaSituacaoDePatioExibida; i < listaSituacoesPatio.size(); i++) {
       // ...obtem a situacao de patio anterior caso a situacao de patio selecionada
       // ...nao for a situacao inicial
       //SituacaoPatio situacaoPatioAnterior = null;
       if (indiceDaSituacaoDePatioExibida == 0) {
           throw new ErroSistemicoException("Não é possivel excluir situação inicial !");
       }    


       // ...obtem a situacao de patio selecionada para remocao
       SituacaoPatio situacaoPatioSelecionada = listaSituacoesPatio.get(i);
       listaSituacoesRemocao.add(situacaoPatioSelecionada);
      }

      controladorModelo = null;
      // ordena a lista de situacoes da memoria
      Collections.sort(listaSituacoesRemocao, new ComparadorSituacoesPatio());

      Collections.reverse(listaSituacoesRemocao);
      SituacaoPatioDAO dao = new SituacaoPatioDAO();   
      if (!listaSituacoesRemocao.isEmpty()) {
        
          for (SituacaoPatio situacao : listaSituacoesRemocao) {
            dao.removeSituacaoPatio(situacao);
        	          	            
          }

          //AtividadeDAO dao1 = new AtividadeDAO();
          
         // dao1.removePilhaPendente();
          
          
          planoEmpilhamentoRecuperacao.getListaSituacoesPatio().removeAll(listaSituacoesRemocao);
      
                    
          listaSituacoesPatio = planoEmpilhamentoRecuperacao.getListaSituacoesPatio(); 

            // ... percorrendo as situacoes de patio do plano e setando o atributo sitacaoPatioPersistida para true
            for (SituacaoPatio situacaoPatio : planoEmpilhamentoRecuperacao.getListaSituacoesPatio()) {
                situacaoPatio.setSituacaoPatioPersistida(Boolean.TRUE);
            }
            
            // ordena a lista de situacoes da memoria
            Collections.sort(listaSituacoesPatio, new ComparadorSituacoesPatio());

            // atualiza o patio apos a consolidacao
            controladorPlanejamento.getControladorInterfaceInicial().getInterfaceInicial().atualizarDSP();
            
            //this.consolidarPlano();            
            
      }
       } catch (Exception e) {
        // TODO: handle exception
           e.printStackTrace();
           throw new ErroSistemicoException(e);
    } 
   }
   /**
    * Retorna a situacao de patio inicial do plano de empilhamento do
    * usuario logado
    * @return
    */
   public SituacaoPatio obterSituacaoPatioInicial()
   {
      SituacaoPatio situacaoPatioInicial = new LinkedList<SituacaoPatio>(listaSituacoesPatio).getFirst();
      return situacaoPatioInicial;
   }

   /**
    * Retorna a situacao de patio final do plano de empilhamento do
    * usuario logado
    * @return
    */
   public SituacaoPatio obterSituacaoPatioFinal()
   {
      SituacaoPatio situacaoPatioFinal = new LinkedList<SituacaoPatio>(listaSituacoesPatio).getLast();
      return situacaoPatioFinal;
   }

   /**
    * Retorna a situacao de patio de acordo com o
    * indice passado como parametro
    * @param posicaoSituacao
    * @return
    */
   public SituacaoPatio buscarSituacaoPatio(Integer posicaoSituacao)
   {
      if (posicaoSituacao >= listaSituacoesPatio.size())
      {
         posicaoSituacao = listaSituacoesPatio.size() - 1;
      }
      return listaSituacoesPatio.get(posicaoSituacao);
   }

   /**
    * Obtem uma situacao de patio mais proxima da data passada por parametro
    * @param data
    * @return
    */
   public SituacaoPatio obterSituacao(Date data)
   {
      SituacaoPatio retorno = obterSituacaoPatioInicial();
      for (SituacaoPatio situacaoPatio : listaSituacoesPatio)
      {
         if (situacaoPatio.getDtInicio().compareTo(data) <= 0)
         {
            retorno = situacaoPatio;
         }
         else
         {
            break;
         }
      }
      return retorno;
   }

   
   /**
    * Retorna a ultima situa??o de patio realizada dentre as situacoes
    * de patio que est?o exibidas na linha do tempo
    * @retur
    */
   public SituacaoPatio obterUltimaSituacaoPatioRealizada()
   {
      SituacaoPatio situacaoPatioRealizada = null;
      for (SituacaoPatio situacaoPatio : listaSituacoesPatio)
      {
         if (situacaoPatioRealizada == null && situacaoPatio.getEhRealizado())
         {
            situacaoPatioRealizada = situacaoPatio;
         }
         else if (situacaoPatio.getEhRealizado() && situacaoPatioRealizada.getDtInicio().compareTo(situacaoPatio.getDtInicio()) <= 0)
         {
            situacaoPatioRealizada = situacaoPatio;
         }
      }
      return situacaoPatioRealizada;
   }

   public FilaDeNavios getFilaDeNaviosAlterada()
   {
      return filaDeNaviosAlterada;
   }

 
    
   /**
    * Retorna o indice da situa??o de patio mais proxima a data passada como parametro
    * @param data
    * @return O indice da situacao; <br> -1 caso n?o encontre
    */
   public int retornaIndiceDaSitucaoDePatio(Date data)
   {
      SituacaoPatio situacaoPatioCriada = obterSituacao(data);
      int indiceSitPatio = -1;
      //procura o indice da situacao de patio criada, para atualizar as situacoes futuras se existirem
      for (int i = 0; i < listaSituacoesPatio.size(); i++)
      {
         if (listaSituacoesPatio.get(i).equals(situacaoPatioCriada))
         {
            indiceSitPatio = i;
            break;
         }
      }
      return indiceSitPatio;
   }

   /**
    *
    * obterSituacao
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 15/06/2009
    * @see
    * @param situacaoPatioList
    * @param dataIni
    * @param dataFim
    * @return
    * @return Returns the List<SituacaoPatio>.
    */
   //TODO Darley que implementou para teste
   public List<SituacaoPatio> obterSituacao(List<SituacaoPatio> situacaoPatioList, Date dataIni, Date dataFim)
   {

      List<SituacaoPatio> retornoList = new ArrayList<SituacaoPatio>();
      for (SituacaoPatio situacaoPatio : situacaoPatioList)
      {
         if (situacaoPatio.getDtInicio().compareTo(dataIni) > 0 &&
               situacaoPatio.getDtInicio().compareTo(dataFim) <= 0)
         {
            retornoList.add(situacaoPatio);
         }
      }
      return retornoList;
   }

   /**
    *
    * obterSituacao
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 15/06/2009
    * @see
    * @param dataIni
    * @param dataFim
    * @return
    * @return Returns the List<SituacaoPatio>.
    */
   //TODO Darley que implementou para teste
   public List<SituacaoPatio> obterSituacao(Date dataIni, Date dataFim)
   {

      List<SituacaoPatio> retornoList = new ArrayList<SituacaoPatio>();
      //SituacaoPatio retorno = obterSituacaoPatioInicial();
      for (SituacaoPatio situacaoPatio : listaSituacoesPatio)
      {
         if (situacaoPatio.getDtInicio().compareTo(dataIni) >= 0 &&
               situacaoPatio.getDtInicio().compareTo(dataFim) <= 0)
         {
            retornoList.add(situacaoPatio);
         }
      }
      return retornoList;
   }

   public void carregarPlanoEmpilhamentoRecuperacaoOficialToUsuario(PlanoEmpilhamentoRecuperacao plano)
   {
      listaSituacoesPatio = plano.getListaSituacoesPatio();
      planoEmpilhamentoRecuperacao.getListaSituacoesPatio().clear();
      planoEmpilhamentoRecuperacao.getListaSituacoesPatio().addAll(listaSituacoesPatio);
      planoEmpilhamentoRecuperacao = plano;

      // ... percorrendo as situacoes de patio do plano e setando o atributo sitacaoPatioPersistida para true
      for (SituacaoPatio situacaoPatio : listaSituacoesPatio)
      {
         situacaoPatio.setSituacaoPatioPersistida(Boolean.TRUE);
//         situacaoPatio.setPlanoEmpilhamento(planoEmpilhamentoRecuperacao);
      }

      // ordena a lista de situacoes da memoria
      Collections.sort(listaSituacoesPatio, new ComparadorSituacoesPatio());

      // atualiza o patio apos a consolidacao
      controladorPlanejamento.getControladorInterfaceInicial().getInterfaceInicial().setIndiceSituacaoPatioSelecionada(listaSituacoesPatio.size() - 1);

      controladorPlanejamento.getControladorInterfaceInicial().getInterfaceInicial().atualizarDSP();
//      controladorPlanejamento.getControladorInterfaceInicial().getInterfaceInicial().montaPatio();

      //planoEmpilhamentoRecuperacao.getListaSituacoesPatio().addAll(listaSituacoesPatio);

   }

   /**
    * Verifica se o plano atual é de usuario ou oficial
    * @return true se o plano for de usuario;<br>
    * false caso seja plano oficial
    */
   public boolean validaSalvarPlanoUsuario()
   {
      boolean result = Boolean.TRUE;
      if (planoEmpilhamentoRecuperacao.getEhOficial())
      {
         result = Boolean.FALSE;
      }
      return result;
   }

   /**
    * se existir um plano do usuario na base, caso positivo exclui este plano para que possa ser gerado um novo a partir do Oficial
    */
   public void verificaPlanoUsuarioExistente(long idUser) throws ErroSistemicoException
   {
       //Darley removendo a chamada remota
//     IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
       IControladorModelo controladorModelo = new ControladorModelo();
       PlanoEmpilhamentoRecuperacao planoUsuario = controladorModelo.buscarPlanoEmpilhamentoRecuperacaoPorUsuario(idUser);
       controladorModelo = null;
      if (planoUsuario != null)
      {
         apagarPlanoUsuario(planoUsuario);
      }
   }
}
