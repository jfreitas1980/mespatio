package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.dao.ClienteDAO;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.integracao.navios.IControladorNavios;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Classe que implementa os metodos necessarios para as operações solicitadas na {@link InterfaceEditaNavioNaFilaDeNavios}
 * @author bgomes
 */
public class EditarInserirDadosNavioNaFilaDeNavios {

    private InterfaceInserirNavioFilaDeNavios interfaceInserirNavioFilaDeNavios;
    private InterfaceMensagem interfaceMensagem;
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;
    /** Acesso ao subsistema Navios */
    private IControladorNavios controladorNavios;

    private RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();

    //No modo de inserir cria o objeto InterfaceNavio
    private Navio navio;
    //guarda os dados dos atributos do navio
    private String nomeNavio;
    private Double dwt;
    private Date eta;
    private Date dataChegada;
    private Date dataSaida;
    private Date dataEmbarque;
    private String status;
    private Cliente cliente;
    private String nomeNovoCliente;
    private String codigoNovoCliente;
    private Double taxaEstadiaNovoCliente;

    public EditarInserirDadosNavioNaFilaDeNavios(InterfaceInserirNavioFilaDeNavios interfaceInserirNavioFilaDeNavios) {
        this.interfaceInserirNavioFilaDeNavios = interfaceInserirNavioFilaDeNavios;
        //controladorNavios = new controla
    }

    /**
     * seta as edições permitidas no modo de inserir
     */
    public void habilitaInserir() {
        interfaceInserirNavioFilaDeNavios.getTxtNomeNavio().setEditable(true);
        interfaceInserirNavioFilaDeNavios.getCmbStatus().setEnabled(false);
        interfaceInserirNavioFilaDeNavios.getCmbCliente().setEnabled(true);
        interfaceInserirNavioFilaDeNavios.getDtChegadaCalendarioHoraCFlex().setEnabled(true);
        interfaceInserirNavioFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().setEnabled(true);
        interfaceInserirNavioFilaDeNavios.getDtSaidaCalendarioHoraCFlex().setEnabled(true);
        interfaceInserirNavioFilaDeNavios.getEtaCalendarioHoraCFlex().setEnabled(true);
        interfaceInserirNavioFilaDeNavios.getTxtDwt().setEditable(true);
    }

    /**
     * Metodo que inicializa todos os campos com valor zero ou vazio, utilizado no modo de inserir navio
     */
    public void inicializaCamposValorZero() {
        try {
            interfaceInserirNavioFilaDeNavios.getTxtDwt().setText("0");
            interfaceInserirNavioFilaDeNavios.getTxtNomeNavio().setText("");
            //campos de data
            interfaceInserirNavioFilaDeNavios.getEtaCalendarioHoraCFlex().limpaDataHora();
            interfaceInserirNavioFilaDeNavios.getDtChegadaCalendarioHoraCFlex().limpaDataHora();
            interfaceInserirNavioFilaDeNavios.getDtSaidaCalendarioHoraCFlex().limpaDataHora();
            interfaceInserirNavioFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().limpaDataHora();
            //campos de comboBox
            interfaceInserirNavioFilaDeNavios.getCmbStatus().setSelectedIndex(0);
            //cliente
            interfaceInserirNavioFilaDeNavios.getCmbCliente().removeAllItems();
            //interfaceInserirNavioFilaDeNavios.getCmbCliente().addItem(PropertiesUtil.getMessage("combo.novo.cliente"));
            for (Cliente cliente1 : controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaDeClientes()) {
                interfaceInserirNavioFilaDeNavios.getCmbCliente().addItem(cliente1.getNomeCliente());
            }

        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
            Logger.getLogger(EditaDadosNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void montaExceptionCampoVazio(String nomeCampo) throws ValidacaoCampoException{
        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add(nomeCampo);
        throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
    }

    /**
     * valida os dados inseridos nos campos de tela
     * @param interfaceNavio
     */
    public boolean validaDados(InterfaceNavio interfaceNavio) {
        try {
            String novoNome = interfaceInserirNavioFilaDeNavios.getTxtNomeNavio().getText();

            if (novoNome == null || novoNome.equals("")) {
                montaExceptionCampoVazio(interfaceInserirNavioFilaDeNavios.getLblNomeNavio().getText());
            }
            if (novoNome.length() > 60) {
                List<String> paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblNomeNavio().getText());
                paramMensagens.add("60");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
            }

            //se a interfacenavio for igual a null, seignifica de que este é o primeiro navio inserido na fila
            if (interfaceNavio != null) {//se o nome do navio editado for igual ao nome digitado no campo nome, nada precisa ser feito, apenas preserva-se o nome anterior
                if (!(novoNome.equalsIgnoreCase(interfaceNavio.getNavioVisualizado().getNomeNavio()))) {
                    //caso o nome digitado for diferente do navio editado, verificar se nenhum outro navio ja possui este nome
                    if (roifn.verificaNomeNovoNavio(controladorInterfaceFilaDeNavios, novoNome)) {
                        this.nomeNavio = novoNome;
                    }
                } else {
                    this.nomeNavio = interfaceNavio.getNavioVisualizado().getNomeNavio();
                }
            } else {
                this.nomeNavio = novoNome;
            }

            Double valor;
            List<String> paramMensagens = new ArrayList<String>();

            valor = Double.parseDouble(interfaceInserirNavioFilaDeNavios.getTxtDwt().getText());
            if (valor < 0) {
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDwt().getText());
                throw new ValidacaoCampoException("exception.validacao.campo.negativo", paramMensagens);
            } else {
                this.dwt = Double.parseDouble(interfaceInserirNavioFilaDeNavios.getTxtDwt().getText());
            }
            if(interfaceInserirNavioFilaDeNavios.getTxtDwt().getText().length() > 15){
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDwt().getText());
                paramMensagens.add("15");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
            }
            // valida data hora Eta
            if (validaData(interfaceInserirNavioFilaDeNavios.getEtaCalendarioHoraCFlex().getDataHora(), interfaceInserirNavioFilaDeNavios.getLblEta().getText())) {
                this.eta = DSSStockyardTimeUtil.criaDataComString(interfaceInserirNavioFilaDeNavios.getEtaCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            //valida data/hora Chegada
            if (validaData(interfaceInserirNavioFilaDeNavios.getDtChegadaCalendarioHoraCFlex().getDataHora(), interfaceInserirNavioFilaDeNavios.getLblDtChegada().getText())) {
                this.dataChegada = DSSStockyardTimeUtil.criaDataComString(interfaceInserirNavioFilaDeNavios.getDtChegadaCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            //valida data/hora saida
            if (validaData(interfaceInserirNavioFilaDeNavios.getDtSaidaCalendarioHoraCFlex().getDataHora(), interfaceInserirNavioFilaDeNavios.getLblDtSaida().getText())) {
                this.dataSaida = DSSStockyardTimeUtil.criaDataComString(interfaceInserirNavioFilaDeNavios.getDtSaidaCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            //valida data/hora embarque
            if (validaData(interfaceInserirNavioFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().getDataHora(), interfaceInserirNavioFilaDeNavios.getLblDtEmbarque().getText())) {
                this.dataEmbarque = DSSStockyardTimeUtil.criaDataComString(interfaceInserirNavioFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }

            // Valida se a data de chegada antecede a data de saida
            if (dataChegada.after(dataSaida)) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDtChegada().getText());
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDtSaida().getText());
                throw new ValidacaoCampoException("mensagem.validacao.edicao.navio.datasaida.maior.datachegada", paramMensagens);
            }

            // Valida se a data de embarque esta entre a data de chegada e saida
            if (dataEmbarque.before(dataChegada) || dataEmbarque.after(dataSaida)) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDtEmbarque().getText());
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDtChegada().getText());
                paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblDtSaida().getText());
                throw new ValidacaoCampoException("mensagem.validacao.edicao.navio.dataembarque.entre.datasaida.datachegada", paramMensagens);
            }

            //valida status
            this.status = (String) interfaceInserirNavioFilaDeNavios.getCmbStatus().getSelectedItem();

            if (interfaceInserirNavioFilaDeNavios.isNovoClienteCriado())
            {
                //valida novo cliente inserido
                if(interfaceInserirNavioFilaDeNavios.getTxtNomeCliente().getText() == null || interfaceInserirNavioFilaDeNavios.getTxtNomeCliente().getText().isEmpty()){
                    montaExceptionCampoVazio(interfaceInserirNavioFilaDeNavios.getLblStatus1().getText());
                }else{
                    this.nomeNovoCliente = interfaceInserirNavioFilaDeNavios.getTxtNomeCliente().getText();
                }
                //valida taxa de estadia do cliente inserido
                //valor = Double.parseDouble(interfaceInserirNavioFilaDeNavios.getTxtTaxaEstadia().getText());
                //if(valor < 0){
                    this.taxaEstadiaNovoCliente = 0.0D;
                    //paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblStatus2().getText());
                    //throw new ValidacaoCampoException("exception.validacao.campo.negativo", paramMensagens);
                //}
                this.taxaEstadiaNovoCliente = valor;
                //valida campo de codigo do cliente
                String novoCodCliente = interfaceInserirNavioFilaDeNavios.getTxtCodigoCliente().getText();
                if (novoCodCliente == null || novoCodCliente.equals("")) {
                    montaExceptionCampoVazio(interfaceInserirNavioFilaDeNavios.getLblStatus3().getText());
                }
                else if (novoCodCliente.length() > 4) {
                    paramMensagens = new ArrayList<String>();
                    paramMensagens.add(interfaceInserirNavioFilaDeNavios.getLblStatus3().getText());
                    paramMensagens.add("3");
                    throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
                }else{
                    this.codigoNovoCliente = novoCodCliente;
                }

            } else
            {//valida e atualiza cliente cliente
                if (interfaceInserirNavioFilaDeNavios.getCmbCliente().getSelectedIndex() == -1 ) {
                	throw new ValidacaoCampoException("mensagem.validacao.edicao.navio.selecione.cliente", paramMensagens);
                }
            	
            	for (Cliente cliente1 : controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaDeClientes()) {
                    //teoricamente cada cliente é unico na tabela Cliente da base, então a comparação esta sendo feita pelo nome do cliente
                    if (String.valueOf(interfaceInserirNavioFilaDeNavios.getCmbCliente().getSelectedItem()).equalsIgnoreCase(cliente1.getNomeCliente())) {
                        this.cliente = cliente1;
                    }
                }
            }

            return true;
        } catch (ValidacaoCampoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            return false;
        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
//            Logger.getLogger(EditaDadosNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NumberFormatException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.aviso.informar.numeros"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
//            Logger.getLogger(EditaDadosNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean validaData(String dataHora, String campo) {
        try {
            DSSStockyardTimeUtil.validarDataHora(dataHora, campo);
            return true;
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
     * Metodo que cria um objeto navio com os dados inseridos na interfaceEditaNavioNaFilaDeNavios
     * @return
     */
    public Navio criaNavio(Date horaInclusao) {
        //cria o navio
        // busca a lista de clientes cadastrados no sistema
      
    	MetaNavio metaNavio = new MetaNavio();
    	metaNavio.setIdUser(1L);
    	metaNavio.setDtInsert(horaInclusao);    	
        navio = new Navio();
        metaNavio.incluirNovoStatus(navio, horaInclusao);
        navio.setNomeNavio(nomeNavio);
        navio.setDwt(dwt);
        navio.setDataEmbarque(dataEmbarque);
        navio.setDiaDeChegada(dataChegada);
        navio.setDiaDeSaida(dataSaida);
        navio.setEta(eta);
        navio.setStatusEmbarque(StatusNavioEnum.CONFIRMADO);        
        //navio.setStatusEmbarque(status);
       // navio.setListaDeCargasDoNavio(new ArrayList<Carga>());

        
        
        
        if(interfaceInserirNavioFilaDeNavios.isNovoClienteCriado()){
            //cria o novo cliente cadastrado
            Cliente newClient = new Cliente();
            newClient.setNomeCliente(nomeNovoCliente);
            newClient.setTaxaDeEstadia(taxaEstadiaNovoCliente);
            newClient.setCodigoCliente(codigoNovoCliente);

            ClienteDAO dao = new ClienteDAO();
            dao.salvar(newClient);
            
            navio.setCliente(newClient);
        }else{
        	 navio.setCliente(this.cliente);
        	//        	Cliente cliente1 =  this.cliente;  //roifn.buscaClinteEquivalenteNaListaNavios(controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila(), this.cliente);
            //if (cliente1 == null) {
                //adiciona o novo cliente editado para o objeto navio
              //  navio.setCliente(cliente);
            //} else {
                //adiciona o novo cliente editado para o objeto navio
              //  navio.setCliente(cliente1);
            //}
        }
        
        MetaNavioDAO dao = new MetaNavioDAO();
        try {
			dao.salvaMetaNavio(metaNavio);
		} catch (ErroSistemicoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        dao = null;

        return navio;
    }

    public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios() {
        return controladorInterfaceFilaDeNavios;
    }

    public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios) {
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public Date getDataEmbarque() {
        return dataEmbarque;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public Double getDwt() {
        return dwt;
    }

    public Date getEta() {
        return eta;
    }

    public Navio getNavio() {
        return navio;
    }

    public String getNomeNavio() {
        return nomeNavio;
    }

    public String getStatus() {
        return status;
    }
}// end of class
