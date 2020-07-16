package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gr�fica que representa a {@link Usina}.
 *
 * @author andre
 *
 */
public class InterfaceFiltragem extends InterfaceUsina {

    Filtragem filtragemVisualizada;    
    
    public InterfaceFiltragem(Date horaSituacao) {
        super(horaSituacao);
        //this.setImagemDSP("filtro_de_ar_AP2710_1.jpg");
        // TODO Auto-generated constructor stub
    }

    private InterfaceFiltragem obterFiltragemClicada(ActionEvent evt) {
        JMenuItem mnuItem = (JMenuItem) evt.getSource();
        JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
        return (InterfaceFiltragem) popupMenu.getInvoker();
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Filtragem getFiltragemVisualizada() {
        return filtragemVisualizada;
    }

    public void setFiltragemVisualizada(Filtragem filtragemVisualizada) {
        this.filtragemVisualizada = filtragemVisualizada;
    }

    
    @Override
    public void inicializaInterface() {        
        StringBuffer dados = new StringBuffer(); 
        dados.append("<html>").append(PropertiesUtil.getMessage("label.filtragem")).append(filtragemVisualizada.getMetaFiltragem().getNomeFiltragem()).append("<br>");
        
        
        if (filtragemVisualizada.getAtividade() != null) {
            
            dados.append("<br>").append(PropertiesUtil.getMessage("label.atividade")).append(filtragemVisualizada.getAtividade().getTipoAtividade().toString());
        
            if (filtragemVisualizada.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO) && filtragemVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                dados.append("<br>").append(PropertiesUtil.getMessage("label.maquina"));                    
                //dados.append(filtragemVisualizada.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getMaquinaDoPatio().getNomeMaquina());
            }
            
            if (filtragemVisualizada.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO) && filtragemVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                dados.append("<br>").append(PropertiesUtil.getMessage("label.carga.tooltip")); 
                dados.append(filtragemVisualizada.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getIdentificadorCarga()).append("<br>").append(PropertiesUtil.getMessage("label.navio"));
                //dados.append(filtragemVisualizada.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(filtragemVisualizada.getAtividade().getDtInicio()).getNomeNavio());
            } 
        }       
        
        
        this.setToolTipText(dados.toString());
 
        defineDimensoesFixas();
        calculaPosicaoUsina();
        defineEventosParaUsina();
        criaPopMenuParaFiltragem();
        adicionaNomeFiltragem();
        desabilitaMenusPermissaoUsuario();
        controladorDSP.setInterfaceDadosEdicao(new InterfaceDadosEdicao());
        
        if (filtragemVisualizada.isFiltragemSelecionada()) {            
          this.desenhaFiltragemSelecionada();
       }
        
    }

    private void criaPopMenuParaFiltragem() {

        JPopupMenu popMnuEditarUsina = new JPopupMenu();



//        JMenuItem
        mnuAtualizacaoRecuperacao = new JMenuItem();
        mnuAtualizacaoRecuperacao.setText(PropertiesUtil.getMessage("menu.atualizacao.de.recuperacao"));
        mnuAtualizacaoRecuperacao.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atualizacaoRecuperacaoFiltragem(evt);
            }
        });
        popMnuEditarUsina.add(mnuAtualizacaoRecuperacao);

        this.setComponentPopupMenu(popMnuEditarUsina); 
   }
    
    @Override
    protected void desabilitaMenusPermissaoUsuario()
    {
        if(controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()){            
            mnuAtualizacaoRecuperacao.setEnabled(false);            
        }
    }

 
    @Override
    public void deselecionar()
    {
          desenhaFiltragem();                 
    }

    
    private void desenhaFiltragem(){
        this.setImagemDSP("usina-a.png");
    }
    private void desenhaFiltragemSelecionada()
    {
        this.setImagemDSP("usina-b.png");
    }

    private void atualizacaoRecuperacaoFiltragem(ActionEvent evt) {
        InterfaceFiltragem filtragemClicada = obterFiltragemClicada(evt);
        if (filtragemClicada.getFiltragemVisualizada().getMetaFiltragem().equals(getFiltragemVisualizada().getMetaFiltragem()) )  
        {
           try
           {           
               Boolean achouCampanha = Boolean.FALSE;
               for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
                   if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem() != null 
                                   && interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(this.getHoraSituacao()) != null) {                        
                       achouCampanha = Boolean.TRUE;
                       break;
                   }
               }
               
               //if (this.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(this.getHoraSituacao()) == null) {
                   if (!achouCampanha) {
                   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                   interfaceMensagem = new InterfaceMensagem();
                   interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                   interfaceMensagem.setDescricaoMensagem("Usinas da filtragem não possuem campanha atual !");
                   controladorDSP.ativarMensagem(interfaceMensagem);             
                   return;
               }
               
               
           if (this.getFiltragemVisualizada().getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                  && this.getFiltragemVisualizada().getAtividade() != null && !this.getFiltragemVisualizada().getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)  ) {
               controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
               interfaceMensagem = new InterfaceMensagem();
               interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
               interfaceMensagem.setDescricaoMensagem("Filtragem executando atividade " + this.getFiltragemVisualizada().getAtividade().getTipoAtividade().toString() + "!");
               controladorDSP.ativarMensagem(interfaceMensagem);             
               return;
           }
               controladorDSP.atualizaRecuperacaoFiltragem(this.getFiltragemVisualizada(),this.getUsinaVisualizada(), this);
               controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();          
           }
           catch (OperacaoCanceladaPeloUsuarioException ex)
           {
               controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
              interfaceMensagem = new InterfaceMensagem();
              interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
              interfaceMensagem.setDescricaoMensagem(ex.getMessage());
              controladorDSP.ativarMensagem(interfaceMensagem);
           }
           catch (ErroSistemicoException erroSis)
           {
               controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
              interfaceMensagem = new InterfaceMensagem();
              interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
              interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
              controladorDSP.ativarMensagem(interfaceMensagem);
           }
        }
      }

    private void adicionaNomeFiltragem(){        
        getLabelNome().setText(this.filtragemVisualizada.getMetaFiltragem().getNomeFiltragem());        
    }

}