package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Classe que implementa os metodos necessarios para as operações solicitadas na {@link InterfaceEditaNavioNaFilaDeNavios}
 * @author bgomes
 */
public class EditaDadosNavioNaFilaDeNavios {

    private InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios;
    private InterfaceMensagem interfaceMensagem;
    private ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;
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
    private StatusNavioEnum status;
    private Cliente cliente;
   
    public EditaDadosNavioNaFilaDeNavios(InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios){
        this.interfaceEditaNavioNaFilaDeNavios = interfaceEditaNavioNaFilaDeNavios;
    }
    /**
     * seta as edições permitidas no modo de editar
     */
    public void habilitaEditar(){
        interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().setEditable(true);
        //interfaceEditaNavioNaFilaDeNavios.getCmbStatus().setEnabled(true);
        //interfaceEditaNavioNaFilaDeNavios.getJcbCarga().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getCmbCliente().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getTxtDwt().setEditable(true);
    }

    /**
     * seta as edições permitidas no modo de inserir
     */
    public void habilitaInserir(){
        interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().setEditable(true);
        interfaceEditaNavioNaFilaDeNavios.getCmbStatus().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getCmbCliente().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().setEnabled(true);
        interfaceEditaNavioNaFilaDeNavios.getTxtDwt().setEditable(true);
    }

    /**
     * seta as edições bloqueadas apenas para visualizacao.
     */
    public void desabilitaCampos(){
        interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().setEditable(false);
        interfaceEditaNavioNaFilaDeNavios.getCmbStatus().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getCmbCliente().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().getCampoData().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().getCampoHora().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().getCampoData().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().getCampoHora().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().getCampoData().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().getCampoHora().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().getCampoData().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().getCampoHora().setEnabled(false);
        interfaceEditaNavioNaFilaDeNavios.getTxtDwt().setEditable(false);
        interfaceEditaNavioNaFilaDeNavios.getBtnDesistirNavio().setEnabled(true);        
    }

    /**
     * Carrega os campos da interface GUI com os dados do navio selecionado
     */
    public void atualizaDados(InterfaceNavio interfaceNavio) {
        interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().setText(interfaceNavio.getNavioVisualizado().getNomeNavio());
        interfaceEditaNavioNaFilaDeNavios.getTxtDwt().setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(interfaceNavio.getNavioVisualizado().getDwt(), 0));
        //atualiza campos de data
        interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().setDataHora(interfaceNavio.getNavioVisualizado().getEta());
        interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().setDataHora(interfaceNavio.getNavioVisualizado().getDiaDeChegada());
        interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().setDataHora(interfaceNavio.getNavioVisualizado().getDiaDeSaida());
        interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().setDataHora(interfaceNavio.getNavioVisualizado().getDataEmbarque());
        //atualiza ComboBoxes
        //comboBox status do navio
        interfaceEditaNavioNaFilaDeNavios.getCmbStatus().setSelectedItem(interfaceNavio.getNavioVisualizado().getStatusEmbarque());
        //dados do cliente
        interfaceEditaNavioNaFilaDeNavios.getCmbCliente().removeAllItems();
        interfaceEditaNavioNaFilaDeNavios.getCmbCliente().addItem(interfaceNavio.getNavioVisualizado().getCliente().getNomeCliente());
    }

    /**
     * Metodo que inicializa todos os campos com valor zero ou vazio, utilizado no modo de inserir navio
     */
    public void inicializaCamposValorZero() {
        try {
            interfaceEditaNavioNaFilaDeNavios.getTxtDwt().setText("0");
            interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().setText("");
            //campos de data
            interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().limpaDataHora();
            interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().limpaDataHora();
            interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().limpaDataHora();
            interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().limpaDataHora();
            //campos de comboBox
            interfaceEditaNavioNaFilaDeNavios.getCmbStatus().setSelectedIndex(0);
            //cliente
            interfaceEditaNavioNaFilaDeNavios.getCmbCliente().removeAllItems();
            for (Cliente cliente1 : controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaDeClientes()) {
                interfaceEditaNavioNaFilaDeNavios.getCmbCliente().addItem(cliente1.getNomeCliente());
            }
            
        } catch (ErroSistemicoException ex) {
        	getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
            ex.printStackTrace();
            Logger.getLogger(EditaDadosNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * valida os dados inseridos nos campos de tela
     * @param interfaceNavio
     */
    public boolean validaDados(InterfaceNavio interfaceNavio) {
        try {
            List<String> paramMensagens = new ArrayList<String>();
            String novoNome = "";
            if (interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().getText().length() <= 60) {
                novoNome = interfaceEditaNavioNaFilaDeNavios.getTxtNomeNavio().getText();
            } else {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblNomeNavio().getText());
                paramMensagens.add("60");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
            }
            
            //se a interfacenavio for igual a null, seignifica de que este é o primeiro navio inserido na fila
            if(interfaceNavio != null)
            {//se o nome do navio editado for igual ao nome digitado no campo nome, nada precisa ser feito, apenas preserva-se o nome anterior
                if(!(novoNome.equalsIgnoreCase(interfaceNavio.getNavioVisualizado().getNomeNavio()))){
                    //caso o nome digitado for diferente do navio editado, verificar se nenhum outro navio ja possui este nome
                    if (roifn.verificaNomeNovoNavio(controladorInterfaceFilaDeNavios, novoNome)) {
                        this.nomeNavio = novoNome;
                    }
                } else {
                    this.nomeNavio = interfaceNavio.getNavioVisualizado().getNomeNavio();
                }
            }else{
                this.nomeNavio = novoNome;
            }

            if (novoNome == null || novoNome.equals("")) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblCarga().getText());
                throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
            }

            Double valor;

            valor = DSSStockyardFuncoesNumeros.getStringToDouble(interfaceEditaNavioNaFilaDeNavios.getTxtDwt().getText());
            if(valor < 0){
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDwt().getText());
                throw new ValidacaoCampoException("exception.validacao.campo.negativo", paramMensagens);
            }else{
                this.dwt = valor;
            }
            if(DSSStockyardFuncoesTexto.limpaCampoDouble(interfaceEditaNavioNaFilaDeNavios.getTxtDwt().getText()).length() > 15){
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDwt().getText());
                paramMensagens.add("15");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", paramMensagens);
            }
            // valida data hora Eta
            if (validaData(interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().getDataHora(), interfaceEditaNavioNaFilaDeNavios.getLblEta().getText())) {
                this.eta = DSSStockyardTimeUtil.criaDataComString(interfaceEditaNavioNaFilaDeNavios.getEtaCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            //valida data/hora Chegada
            if (validaData(interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().getDataHora(), interfaceEditaNavioNaFilaDeNavios.getLblDtChegada().getText())) {
                this.dataChegada = DSSStockyardTimeUtil.criaDataComString(interfaceEditaNavioNaFilaDeNavios.getDtChegadaCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            //valida data/hora saida
            if (validaData(interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().getDataHora(), interfaceEditaNavioNaFilaDeNavios.getLblDtSaida().getText())) {
                this.dataSaida = DSSStockyardTimeUtil.criaDataComString(interfaceEditaNavioNaFilaDeNavios.getDtSaidaCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            //valida data/hora embarque
            if (validaData(interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().getDataHora(), interfaceEditaNavioNaFilaDeNavios.getLblDtEmbarque().getText())) {
                this.dataEmbarque = DSSStockyardTimeUtil.criaDataComString(interfaceEditaNavioNaFilaDeNavios.getDtEmbarqueCalendarioHoraCFlex().getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            }
            // Valida se a data de chegada antecede a data de saida
            if (dataChegada.after(dataSaida)) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDtChegada().getText());
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDtSaida().getText());
                throw new ValidacaoCampoException("mensagem.validacao.edicao.navio.datasaida.maior.datachegada", paramMensagens);
            }

            // Valida se a data de embarque esta entre a data de chegada e saida
            if (dataEmbarque.before(dataChegada) || dataEmbarque.after(dataSaida)) {
                paramMensagens = new ArrayList<String>();
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDtEmbarque().getText());
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDtChegada().getText());
                paramMensagens.add(interfaceEditaNavioNaFilaDeNavios.getLblDtSaida().getText());
                throw new ValidacaoCampoException("mensagem.validacao.edicao.navio.dataembarque.entre.datasaida.datachegada", paramMensagens);
            }
            //valida status
            //this.status.valueOf((String)interfaceEditaNavioNaFilaDeNavios.getCmbStatus().getSelectedItem());

            //valida e atualiza cliente cliente
             for (Cliente cliente1 : controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial().getListaDeClientes()) {
                //teoricamente cada cliente é unico na tabela Cliente da base, então a comparação esta sendo feita pelo nome do cliente
                 if(String.valueOf(interfaceEditaNavioNaFilaDeNavios.getCmbCliente().getSelectedItem()).equalsIgnoreCase(cliente1.getNomeCliente())){
                     this.cliente = cliente1;
                     break;
                 }
            }

             return true;
        }  catch (ValidacaoCampoException ex) {
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
            ex.printStackTrace();
            Logger.getLogger(EditaDadosNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }catch (NumberFormatException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem("O campo " + interfaceEditaNavioNaFilaDeNavios.getLblDwt().getText() + " só aceita números");
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            ex.printStackTrace();
            Logger.getLogger(EditaDadosNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean validaData(String dataHora, String campo){
        try{
            DSSStockyardTimeUtil.validarDataHora(dataHora, campo);
            return true;
        }catch(ValidacaoCampoException vce){
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

    public void atualizaCargas (List<Carga> cargasAtuais, List<Carga> cargasClonadas) {
        for (Carga cargaAtual : cargasAtuais) {
            for (Carga cargaClonada : cargasClonadas) {
                if (cargaAtual.getMetaCarga().equals(cargaClonada.getMetaCarga())) {
                    //cargaAtual.setIdentificadorCarga(cargaClonada.getIdentificadorCarga());
                    cargaAtual.getMetaCarga().setCaminhoCompletoPlanilha(cargaClonada.getCaminhoCompletoPlanilha());
                    cargaAtual.setProduto(cargaClonada.getProduto());
                    cargaAtual.setOrientacaoDeEmbarque(cargaClonada.getOrientacaoDeEmbarque());
                    if (cargaAtual.getOrientacaoDeEmbarque() != null) {
                        cargaAtual.getOrientacaoDeEmbarque().setCarga(cargaAtual);
                    }
                }
            }
        }
        for (Carga cargaClonada : cargasClonadas) {
            if (cargaClonada.getIdCarga() == null) {
                cargasAtuais.add(cargaClonada);
            }
        }
    }
    
    public Navio criaNavio(){
        //cria o navio
        navio = new Navio();
        navio.setNomeNavio(nomeNavio);
        navio.setDwt(dwt);
        navio.setDataEmbarque(dataEmbarque);
        navio.setDiaDeChegada(dataChegada);
        navio.setDiaDeSaida(dataSaida);
        navio.setEta(eta);
       // navio.setStatusEmbarque(status);
        

        Cliente cliente1 = roifn.buscaClinteEquivalenteNaListaNavios(controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila(), this.cliente);
        if(cliente1 == null){
            //adiciona o novo cliente editado para o objeto navio
            navio.setCliente(cliente);
        }else{
        //adiciona o novo cliente editado para o objeto navio
            navio.setCliente(cliente1);
        }
        //adiciona na listaDeNavios deste novo Cliente o Navio
        //navio.getCliente().getListaDeNaviosDoCliente().add(navio);

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

    public StatusNavioEnum getStatus() {
        return status;
    }

    
}// end of class
