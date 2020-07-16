package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.navios.dao.MetaCargaDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Classe que edita os dados inseridos na {@link interfaceEditaNavioNaFilaDeNavios }
 * @author bgomes
 */
public class EdicaoDadosCarga {

    private InterfaceMensagem interfaceMensagem;
    private InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios;
    private InterfaceEdicaoCarga interfaceEdicaoCarga;
    //cria os objetos Carga e Produto
    private InterfaceCarga interfaceCarga;
    ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;
    private RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
    private Carga carga;
	

    public EdicaoDadosCarga(InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios, InterfaceEdicaoCarga interfaceEdicaoCarga) {
        this.interfaceEditaNavioNaFilaDeNavios = interfaceEditaNavioNaFilaDeNavios;
        this.interfaceEdicaoCarga = interfaceEdicaoCarga;
    }

    /**
     * Metodo que carrega os dados do Navio selecionado na tela
     */
    public void atualizaDadosTela() {
        try {
            roifn.insereCargas(getInterfaceEditaNavioNaFilaDeNavios().getListaCargas(), getInterfaceEditaNavioNaFilaDeNavios().getCargaCFlexStockyardJTable(), getInterfaceEditaNavioNaFilaDeNavios().getListaColunasCargas());
        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            getInterfaceEditaNavioNaFilaDeNavios().getInterfaceNavio().getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
            Logger.getLogger(EditaDadosOrientacaoDeEmbarqueDaCarga.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (Exception e) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.validacao.ItensDeControle"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            Logger.getLogger(EdicaoDadosCarga.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Metodo que carrega os dados do Navio selecionado na tela
     */
    public void atualizaDadosCarga(String identificadorCarga) throws Exception {
        try {
            carga = roifn.retornaCargaSelecionada(identificadorCarga, interfaceEdicaoCarga.getListaCargas());
            carga.setCargaEditada(Boolean.TRUE);
            interfaceEdicaoCarga.getTxtNomeCarga().setText(getCarga().getIdentificadorCarga());
            interfaceEdicaoCarga.getJcbTipoProduto().setSelectedItem(getCarga().getProduto().getTipoProduto());
            interfaceEdicaoCarga.getJtfDescricao().setText(getCarga().getProduto().getTipoProduto().getDescricaoTipoProduto());
            interfaceEdicaoCarga.getTxtQtdEmbarcada().setText(getCarga().getProduto().getQuantidade().toString());
            if (getCarga().getOrientacaoDeEmbarque() != null) {
                interfaceEdicaoCarga.getTxtQtdContratada().setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(getCarga().getOrientacaoDeEmbarque().getQuantidadeNecessaria(), 0));
            }

            if (getCarga().getProduto() != null) {
                roifn.insereItensDeControleDeProduto( getCarga(), interfaceEdicaoCarga.getTblItensDeControle(),interfaceEdicaoCarga.getListaColunas());
            } else {
                roifn.insereItensDeControleValorZero(interfaceEdicaoCarga.getTblItensDeControle(), interfaceEdicaoCarga.getListaColunas(), controladorInterfaceFilaDeNavios);
            }
        } catch (ErroSistemicoException ese) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            getInterfaceEditaNavioNaFilaDeNavios().getInterfaceNavio().getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
            Logger.getLogger(EditaDadosOrientacaoDeEmbarqueDaCarga.class.getName()).log(Level.SEVERE, null, ese);
            ese.printStackTrace();
        }
    }

    /**
     * Apresenta dados zerado do {@link Produto} para preenchimento
     * @param carga
     */
    public void atualizaProdutoValorZero() throws ParseException, ErroSistemicoException, Exception {
    	interfaceEdicaoCarga.getTxtQtdEmbarcada().setText("0");
        interfaceEdicaoCarga.getJcbTipoProduto().setSelectedIndex(0);
        interfaceEdicaoCarga.getJtfDescricao().setText(((TipoProduto) interfaceEdicaoCarga.getJcbTipoProduto().getSelectedItem()).getDescricaoTipoProduto());

        roifn.insereItensDeControleValorZero(interfaceEdicaoCarga.getTblItensDeControle(), interfaceEdicaoCarga.getListaColunas(), controladorInterfaceFilaDeNavios);
    }

    /**
     * Metodo que valida os dados editados na interface grafica ({@link interfaceEditaNavioNaFilaDeNavios}) de edição da carga
     * @param interfaceNavio
     * @return true caso os dados estejam corretos
     */
    public Boolean validaDados(InterfaceNavio interfaceNavio) {
        Double valor;
        List<String> paramMensagens = new ArrayList<String>();
        
        try {
            if (interfaceEdicaoCarga.getTxtNomeCarga().getText().isEmpty()) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEdicaoCarga.getLblNomeCarga().getText());
                throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
            }

            if (interfaceEdicaoCarga.getOperacaoSelecionada() == InterfaceEdicaoCarga.MODO_INSERIR) {
                criaInterfaceCarga(interfaceNavio);
            } else {
                
                if (getCarga().getOrientacaoDeEmbarque() != null) {   //edita os dados do produto da carga
                    // Valida se existe nome duplicado
                    if (interfaceEdicaoCarga.getOperacaoSelecionada() != InterfaceEdicaoCarga.MODO_EXCLUIR) {
                        for (Carga cargaDaLista : interfaceEdicaoCarga.getListaCargas()) {
                            if (cargaDaLista.getIdentificadorCarga().equals(interfaceEdicaoCarga.getTxtNomeCarga().getText()) && !cargaDaLista.getIdCarga().equals(carga.getIdCarga())) {
                                throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.navio.carga.duplicada"));
                            }
                        }
                    }
                    // Valida se o nome da carga excede limite do banco
                    if (interfaceEdicaoCarga.getTxtNomeCarga().getText().length() <= 255) {
                        getCarga().getMetaCarga().setIdentificadorCarga(interfaceEdicaoCarga.getTxtNomeCarga().getText());
                    } else {
                        paramMensagens = new ArrayList<String>();
                        paramMensagens.add(interfaceEdicaoCarga.getLblNomeCarga().getText());
                        paramMensagens.add("255");
                        throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
                    }

                    getCarga().getProduto().setQuantidade(DSSStockyardFuncoesNumeros.getStringToDouble(interfaceEdicaoCarga.getJtfQuantidadeProduto().getText()));
                    //verifica se o produto editado é compativel com a Orientacao de embarque
                     if (((TipoProduto) interfaceEdicaoCarga.getJcbTipoProduto().getSelectedItem()).equals(getCarga().getOrientacaoDeEmbarque().getTipoProduto()))
                     {
                         getCarga().getProduto().setTipoProduto((TipoProduto) interfaceEdicaoCarga.getJcbTipoProduto().getSelectedItem());
                     } else
                     {//caso contrario retorna um erro
                         throw new ProdutoIncompativelException(PropertiesUtil.buscarPropriedade("exception.adicionarProduto"));
                     }

                    //Edita informacoes dos itensDeControle e TipoItemControle
                     
                    // clona a qualidade para compara-la com a qualidade do produto da carga apos a edicao
                    Qualidade qualidadeProdutoCargaClonada = getCarga().getProduto().getQualidade().copiarStatus();
                     
                    for (int i = 0; i < interfaceEdicaoCarga.getTblItensDeControle().getRowCount(); i++) {
                        for (ItemDeControle item : qualidadeProdutoCargaClonada.getListaDeItensDeControle()) {
                            //set na Qualidade real
                            if (item.getTipoItemControle().getDescricaoTipoItemControle().equals(interfaceEdicaoCarga.getTblItensDeControle().getValueAt(i, 0))){
                            	if(interfaceEdicaoCarga.getTblItensDeControle().getValueAt(i, 1).toString().isEmpty()){
                            		valor = new Double(0);
                            	}else{
                            		valor = Double.valueOf(interfaceEdicaoCarga.getTblItensDeControle().getValueAt(i, 1).toString());
                            	}
                                //Valida o valor do item de controle
                                
                                // TODO: Guilherme - Segundo planilha de correcoes, retirar validacao de escala de itens de controle
                                //validaItensControle(valor, interfaceEdicaoCarga.getTblItensDeControle().getValueAt(i, 0).toString(), item);
                                item.setValor(valor);
                            }
                        }
                    }
                    
                    if (!qualidadeProdutoCargaClonada.equals(getCarga().getProduto().getQualidade()))
                    {
                    	getCarga().getProduto().setQualidade(qualidadeProdutoCargaClonada);
                    	getCarga().getProduto().getQualidade().setProduto(getCarga().getProduto());
                    }
                    
                    interfaceCarga = roifn.retornaInterfaceCargaSelecionada(getCarga().getIdentificadorCarga(), interfaceEdicaoCarga.getInterfaceNavio().getListaDecarga());
                    if (interfaceCarga == null) {
                        interfaceCarga = roifn.retornaInterfaceCargaSelecionada(getCarga().getIdentificadorCarga(), getInterfaceEditaNavioNaFilaDeNavios().getListaInterfaceCargasIncluidas());
                    }
                } else {
                    throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("exception.carga.OrientacaoEmbarque.nulo"));
                }
            }
            return true;
        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            Logger.getLogger(EditaDadosOrientacaoDeEmbarqueDaCarga.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return false;
        } catch (ParseException ex) {
            Logger.getLogger(EdicaoDadosCarga.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (ValidacaoCampoException vcex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(vcex.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            return false;
        } catch (ProdutoIncompativelException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            return false;
        }
    }
    
    public void exclusaoCarga() {
    	interfaceCarga = roifn.retornaInterfaceCargaSelecionada(getCarga().getIdentificadorCarga(), interfaceEdicaoCarga.getInterfaceNavio().getListaDecarga());
        if (interfaceCarga != null) {
	    	MetaNavio navio = interfaceCarga.getNavio().getNavioVisualizado().getMetaNavio();
	    	interfaceEditaNavioNaFilaDeNavios.getListaCargas().remove(interfaceCarga.getCargaVisualizada());    	
			interfaceEditaNavioNaFilaDeNavios.getInterfaceNavio().getNavioVisualizado().getMetaNavio().getListaMetaCargas().remove(interfaceCarga.getCargaVisualizada().getMetaCarga());    	
	    	MetaNavioDAO dao = new MetaNavioDAO();
	    	MetaCargaDAO dao1 = new MetaCargaDAO();
	    	try {
	    		dao1.removeMetaCarga(interfaceCarga.getCargaVisualizada().getMetaCarga());
	    		dao.salvaMetaNavio(navio);
	
	    	} catch (ErroSistemicoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 	
    }

   /**
    * Valida os itens de controle da carga de acordo com o range aceito e se o valor digitado é numérico.
    */
   private void validaItensControle(Double valor, String tipoItem, ItemDeControle itemControle) throws ValidacaoCampoException
   {
      List<String> paramMensagens = new ArrayList<String>();
      if (valor > itemControle.getTipoItemControle().getFimEscala() || valor < itemControle.getTipoItemControle().getInicioEscala())
      {
         paramMensagens.add(itemControle.getTipoItemControle().getDescricaoTipoItemControle());
         paramMensagens.add(itemControle.getTipoItemControle().getInicioEscala().toString());
         paramMensagens.add(itemControle.getTipoItemControle().getUnidade());
         paramMensagens.add(itemControle.getTipoItemControle().getFimEscala().toString());
         paramMensagens.add(itemControle.getTipoItemControle().getUnidade());
         throw new ValidacaoCampoException("exception.validacao.campo.valor.incorreto", paramMensagens);
      }
   }

    /**
     * Cria um novo objeto InterfaceCarga para ser inserido no modo de insercao
     * @param interfaceNavio
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.text.ParseException
     * @throws java.lang.NumberFormatException
     */
    private void criaInterfaceCarga(InterfaceNavio interfaceNavio) throws ValidacaoCampoException, ErroSistemicoException, ParseException, NumberFormatException, ProdutoIncompativelException {
        interfaceCarga = new InterfaceCarga();
        interfaceCarga.setNavio(interfaceNavio);
        interfaceCarga.setCargaVisualizada(criaObjetoCarga(interfaceNavio));
    }

    private Carga criaObjetoCarga(InterfaceNavio interfaceNavio) throws ValidacaoCampoException, ErroSistemicoException, ParseException, NumberFormatException, ProdutoIncompativelException {
    	Date horaEdicao = controladorInterfaceFilaDeNavios.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();
    	Carga cargaNova = new Carga();
		// cria a meta carga
        MetaCarga metaCarga = new MetaCarga();
        metaCarga.setIdentificadorCarga(interfaceEdicaoCarga.getTxtNomeCarga().getText());
        metaCarga.setDtInsert(horaEdicao);
        metaCarga.setIdUser(1L);
        metaCarga.incluirNovoStatus(cargaNova, horaEdicao);
        interfaceNavio.getNavioVisualizado().getMetaNavio().addMetaCarga(metaCarga);
     
        /*   cargaNova.setNavio(interfaceNavio.getNavioVisualizado());
        for (Carga cargaDaLista : interfaceEdicaoCarga.getListaCargas()) {
            if (cargaDaLista.getIdentificadorCarga().equals(interfaceEdicaoCarga.getTxtNomeCarga().getText())) {
                throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.navio.carga.duplicada"));
            }
        }
        if (interfaceEdicaoCarga.getTxtNomeCarga().getText().length() <= 255) {
            cargaNova.setIdentificadorCarga(interfaceEdicaoCarga.getTxtNomeCarga().getText());
        } else {
            List<String> paramMensagens = new ArrayList<String>();
            paramMensagens.add(interfaceEdicaoCarga.getLblNomeCarga().getText());
            paramMensagens.add(new Integer(255).toString());
            throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
        }
*/        
    	
        cargaNova.setProduto(criaObjetoProdutoDaCarga(cargaNova));

        return cargaNova;
    }

    /**
     * cria produto da carga do navio
     * @param carga
     * @return
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.text.ParseException
     * @throws java.lang.NumberFormatException
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private Produto criaObjetoProdutoDaCarga(Carga carga) throws ValidacaoCampoException, ErroSistemicoException, ParseException, NumberFormatException, ErroSistemicoException, ErroSistemicoException, ValidacaoCampoException, ProdutoIncompativelException {//verifica se a carga possui orientacao de embarque
        if (carga.getOrientacaoDeEmbarque() != null) { //...em caso positivo, verificar se os tipoDeProdutos são compativeis
            if (carga.getProduto().getTipoProduto().equals(carga.getOrientacaoDeEmbarque().getTipoProduto())) {//se forem do mesmo tipo de produto cria o produto
                return criaObjetoProduto();
            } else {//...do contrario retorna uma mensagem de erro
                throw new ProdutoIncompativelException(PropertiesUtil.buscarPropriedade("exception.adicionarProduto"));
            }

        }
        //se não possuir Orientacao de embarque cria o Produto normalmente
        return criaObjetoProduto();
    }

    /**
     * cria produto
     * @param carga
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.text.ParseException
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private Produto criaObjetoProduto() throws ErroSistemicoException, ParseException, ValidacaoCampoException {
        Produto produto = new Produto();
        produto.setDtInicio(new Date(System.currentTimeMillis()));
        produto.setDtInsert(new Date(System.currentTimeMillis()));
        produto.setQuantidade(new Double(interfaceEdicaoCarga.getTxtQtdEmbarcada().getText()));
        produto.setTipoProduto((TipoProduto) interfaceEdicaoCarga.getJcbTipoProduto().getSelectedItem());
        produto.setQualidade(criaQualidade());
        produto.getQualidade().setProduto(produto);
        return produto;
    }

    /**
     * cria Qualidade do Produto da Carga
     * @return
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     * @throws java.text.ParseException
     * @throws java.lang.NumberFormatException
     */
    private Qualidade criaQualidade() throws ErroSistemicoException, ParseException, NumberFormatException, ValidacaoCampoException {
        Qualidade qualidade = new Qualidade();
        qualidade.setDtInicio(new Date(System.currentTimeMillis()));
        qualidade.setDtInsert(new Date(System.currentTimeMillis()));
        qualidade.setEhReal(false);
        
        
        qualidade.setListaDeItensDeControle(roifn.criaListaItensControle(interfaceEdicaoCarga.getTblItensDeControle(), controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaTiposItemDeControle(), false));
        
        for (ItemDeControle ic : qualidade.getListaDeItensDeControle())
        {
        	ic.setDtInicio(new Date(System.currentTimeMillis()));
        	ic.setDtInsert(new Date(System.currentTimeMillis()));
        }
        
        return qualidade;
    }

    public InterfaceCarga getInterfaceCarga() {
        return interfaceCarga;
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    /**
     * @return the interfaceEditaNavioNaFilaDeNavios
     */
    public InterfaceEditaNavioNaFilaDeNavios getInterfaceEditaNavioNaFilaDeNavios() {
        return interfaceEditaNavioNaFilaDeNavios;
    }

    /**
     * @param interfaceEditaNavioNaFilaDeNavios the interfaceEditaNavioNaFilaDeNavios to set
     */
    public void setInterfaceEditaNavioNaFilaDeNavios(InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios) {
        this.interfaceEditaNavioNaFilaDeNavios = interfaceEditaNavioNaFilaDeNavios;
    }

    /**
     * @return the carga
     */
    public Carga getCarga() {
        return carga;
    }

    
}
