
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao.AtualizacaoEdicaoOrientacaoDeEmbarque;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.produto.dao.MetaInternaDAO;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 *
 * @author bgomes
 */
public class EditaDadosOrientacaoDeEmbarqueDaCarga {

    private RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
    private InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios;
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;
    private InterfaceMensagem interfaceMensagem;
    private AtualizacaoEdicaoOrientacaoDeEmbarque atualizacao;
    private OrientacaoDeEmbarque orientacaoDeEmbarque;

    public EditaDadosOrientacaoDeEmbarqueDaCarga(InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios) {
        this.interfaceEditaNavioNaFilaDeNavios = interfaceEditaNavioNaFilaDeNavios;
    }

    public void atualizaDadosCampos(){
        Carga carga;
        try{
          //adicionando os tipos de produtos ao comboBox
            interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().removeAllItems();
            for (TipoProduto tipoProduto : controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaTiposProduto()) {
                interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().addItem(tipoProduto);
            }
            carga = roifn.retornaCargaSelecionada((String)interfaceEditaNavioNaFilaDeNavios.getCmbCarga().getSelectedItem(), interfaceEditaNavioNaFilaDeNavios.getListaCargas());
            if(carga != null && carga.getOrientacaoDeEmbarque() != null){
                //seleciona o TipoDeProduto da OrientacaoDeEmbarque da Carga
                interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().setSelectedItem(carga.getOrientacaoDeEmbarque().getTipoProduto());
                atualizaPenalizacao(carga);
                roifn.insereItensDeControleDaOrientacaoDeEmbarque(carga, interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable(), interfaceEditaNavioNaFilaDeNavios.getListaColunasOrientEmbarqueItensControle());
                //atualiza campo quantidade
                interfaceEditaNavioNaFilaDeNavios.getTxtQtd().setText(String.valueOf(carga.getOrientacaoDeEmbarque().getQuantidadeNecessaria()));
            }else {
                interfaceEditaNavioNaFilaDeNavios.getCmbPenalizacao().setSelectedIndex(0);
                interfaceEditaNavioNaFilaDeNavios.getTxtQtd().setText("0");
                roifn.insereItensDeControleValorZero(interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable(), interfaceEditaNavioNaFilaDeNavios.getListaColunasOrientEmbarqueItensControle(), controladorInterfaceFilaDeNavios);
            }
        }
        catch (Exception e) {
            Logger.getLogger(EdicaoDadosCarga.class.getName()).log(Level.SEVERE, null, e);
            controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.validacao.ItensDeControle"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        }
    }

    private void atualizaPenalizacao(Carga carga){
        if(carga.getOrientacaoDeEmbarque().getPenalizacao().equals(true))
             interfaceEditaNavioNaFilaDeNavios.getCmbPenalizacao().setSelectedIndex(0);
        else
             interfaceEditaNavioNaFilaDeNavios.getCmbPenalizacao().setSelectedIndex(1);
    }

    public boolean validaDadosEditados() {
        try {
            List<String> paramMensagens = new ArrayList<String>();

            Double valorTeste = Double.parseDouble(interfaceEditaNavioNaFilaDeNavios.getTxtQtd().getText());

            Carga carga = roifn.retornaCargaSelecionada((String)interfaceEditaNavioNaFilaDeNavios.getCmbCarga().getSelectedItem(), interfaceEditaNavioNaFilaDeNavios.getListaCargas());

            if (carga == null) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblCarga().getText());
                throw new ValidacaoCampoException("mensagem.validacao.edicao.orientacao.embarque", paramMensagens);
            }

            /*if (carga.getIdCarga() != null) {
                CloneHelper clone = new CloneHelper();
                carga.geraClone(carga, clone);
            }*/

            Double valor;

            if (carga == null) {
                return true;
            }
        
            //cria orientacao de embarque
            criaOrientacaoDeEmbarque(carga);
            //Edita informacoes dos itensDeControle e TipoItemControle
            for (int i = 0; i < interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable().getRowCount(); i++) {
                for (ItemDeControle item : carga.getOrientacaoDeEmbarque().getListaItemDeControle()) {
                    //set na Qualidade real
                    if (item.getTipoItemControle().getDescricaoTipoItemControle().equals(interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable().getValueAt(i, 0))) {
                       if (!interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable().getValueAt(i, 1).toString().equals(""))
                       {
                          valor = Double.valueOf(interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable().getValueAt(i, 1).toString());
                          item.setValor(valor);
                          
                       }
                    }
                }
            }
            //verifica se o tipo de Produto da Orientacao de embarque é diferente do produto da carga
            
            if(carga.getProduto() != null && carga.getProduto().getTipoProduto().equals((interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().getSelectedItem())))
            {//em caso positivo muda o tipoProduto da Orientacao de embarque
                carga.getOrientacaoDeEmbarque().setTipoProduto((TipoProduto) interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().getSelectedItem());
            }else
            {//...porem se não for igual, muda o tipoDeProduto da CARGA e da ORIENTACAODEEMBARQUE para o tipoDeProduto editado
                carga.getOrientacaoDeEmbarque().setTipoProduto((TipoProduto) interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().getSelectedItem());
                carga.getProduto().setTipoProduto((TipoProduto) interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().getSelectedItem());
                //e avisa ao usuario que o tipoDeProduto tanto da Carga como da OrientacaoDeEmbarque foram modificados para o editado
                ativaMensagem(PropertiesUtil.buscarPropriedade("mensagem.produtoDaCarga.mais.orientacaoDeEmbarque.modificados"));
            }
            
            

            //edicao do atributo penalizacao
            if (interfaceEditaNavioNaFilaDeNavios.getCmbPenalizacao().getSelectedIndex() == 0) {
                carga.getOrientacaoDeEmbarque().setPenalizacao(true);
            } else {
                carga.getOrientacaoDeEmbarque().setPenalizacao(false);
            }

            //validacao do campo quantidade
            
            valor = DSSStockyardFuncoesNumeros.getStringToDouble(interfaceEditaNavioNaFilaDeNavios.getTxtQtd().getText());
            if(valor < 0) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblQuantidade().getText());
                throw new ValidacaoCampoException("exception.validacao.campo.negativo", paramMensagens);
            }
            if(interfaceEditaNavioNaFilaDeNavios.getTxtQtd().getText().length() > 15){
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblQuantidade().getText());
                paramMensagens.add("15");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
            }
            carga.getOrientacaoDeEmbarque().setQuantidadeNecessaria(valor);
            
            if (carga.getIdCarga() != null) {
                interfaceEditaNavioNaFilaDeNavios.getListaCargasClonadas().add(carga);
            }

            return true;

        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            interfaceEditaNavioNaFilaDeNavios.getInterfaceNavio().getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
            Logger.getLogger(EditaDadosOrientacaoDeEmbarqueDaCarga.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return false;
        } catch (ParseException ex) {
            Logger.getLogger(EditaDadosOrientacaoDeEmbarqueDaCarga.class.getName()).log(Level.SEVERE, null, ex);
            controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.validacao.campo.invalido"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            return false;
        } catch (NumberFormatException ex) {
            Logger.getLogger(EdicaoDadosCarga.class.getName()).log(Level.SEVERE, null, ex);
            controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.validacao.campo.invalido"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            return false;
        } catch (ValidacaoCampoException vce) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(vce.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            return false;
        }
    }


        /**
     *  Valida os itens de controle da carga de acordo com o range aceito e se o
     * valor digitado é numérico.
     */
    private void validaItensControle(Double valor, String tipoItem, ItemDeControle itemControle) throws ValidacaoCampoException
   {
      List<String> paramMensagens = new ArrayList<String>();
      if (valor > itemControle.getTipoItemControle().getFimEscala() || valor < itemControle.getTipoItemControle().getInicioEscala())
      {
         paramMensagens.add(itemControle.getTipoItemControle().getDescricaoTipoItemControle());
         paramMensagens.add(itemControle.getTipoItemControle().getInicioEscala().toString());
         paramMensagens.add(itemControle.getTipoItemControle().getUnidade());
         paramMensagens.add(itemControle.getTipoItemControle().getInicioEscala().toString());
         paramMensagens.add(itemControle.getTipoItemControle().getUnidade());
         throw new ValidacaoCampoException("exception.validacao.campo.valor.incorreto", paramMensagens);
      }
   }

    private void criaOrientacaoDeEmbarque(Carga carga) throws ErroSistemicoException, ParseException, NumberFormatException, ValidacaoCampoException {
        //caso a Carga não tenha orientação de embarque, criar este objeto
    	
    	/*if (carga.getOrientacaoDeEmbarque() != null) {
    		carga.getOrientacaoDeEmbarque().setCarga(null);
    		carga.setOrientacaoDeEmbarque(null);
    	}*/
    	MetaInternaDAO dao = new MetaInternaDAO();
    
    	
    	if (carga.getOrientacaoDeEmbarque() == null) {
            orientacaoDeEmbarque = new OrientacaoDeEmbarque();
            orientacaoDeEmbarque.setDtInsert(carga.getDtInsert());
            orientacaoDeEmbarque.setDtInicio(carga.getDtInicio());
            orientacaoDeEmbarque.setListaItemDeControle(roifn.criaListaItensControle(interfaceEditaNavioNaFilaDeNavios.getOrientEmbarqueCFlexStockyardJTable(), controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaTiposItemDeControle(), true));
            
            orientacaoDeEmbarque.setTipoProduto((TipoProduto) interfaceEditaNavioNaFilaDeNavios.getCmbTipoProduto().getSelectedItem());
            
            for(ItemDeControleOrientacaoEmbarque item : orientacaoDeEmbarque.getListaItemDeControleOrientacaoEmbarque()) {
            	MetaInterna meta = new MetaInterna();
            	meta.setTipoItemDeControle(item.getTipoItemControle());
            	meta.setTipoPelota(orientacaoDeEmbarque.getTipoProduto());
            	meta.setTipoDaMetaInterna(TipoMetaInternaEnum.META_DE_EMBARQUE);
            	
            	MetaInterna metaInterna = dao.buscarPorPeriodo(meta, interfaceEditaNavioNaFilaDeNavios.getInterfaceNavio().getHoraSituacao());
            	
            	item.setDtInicio(carga.getDtInicio());
            	item.setDtInsert(carga.getDtInsert());
            	item.setOrientacao(orientacaoDeEmbarque);
            	
            	 
            	if (metaInterna != null) {
            		item.setLimInfMetaOrientacaoEmb(metaInterna.getLimiteInferiorValorMetaInterna());
            		item.setLimSupMetaOrientacaoEmb(metaInterna.getLimiteSuperiorValorMetaInterna());
            	}	
            	
            }
            
            orientacaoDeEmbarque.setCarga(carga);
            carga.setOrientacaoDeEmbarque(orientacaoDeEmbarque);
        }
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    public AtualizacaoEdicaoOrientacaoDeEmbarque getAtualizacao() {
        return atualizacao;
    }

    /**
     * apresenta mensagens de aviso ao usuario
     */
    private void ativaMensagem(String mensagem)
    {
    	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
        interfaceMensagem = new InterfaceMensagem();
        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
        interfaceMensagem.setDescricaoMensagem(mensagem);
        controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
    }

}
