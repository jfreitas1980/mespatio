package com.hdntec.gestao.cliente.messagens;

import java.awt.Color;
import java.io.File;

import javax.swing.ImageIcon;

import com.hdntec.gestao.util.PropertiesUtil;

public class InterfaceMensagem {

    /** Constante que determina que a interface da mensagem é de informação */
    public static final Integer MENSAGEM_TIPO_INFORMACAO = 1;

    /** Constante que determina que a interface da mensagem é de alerta */
    public static final Integer MENSAGEM_TIPO_ALERTA = 2;

    /** Constante que determina que a interface da mensagem é de erro */
    public static final Integer MENSAGEM_TIPO_ERRO = 3;

    /** Consntante que determina que a interface da mensagem é de processamento */
    public static final Integer MENSAGEM_TIPO_PROCESSAMENTO = 4;

    /** Tipo da Mensagem a ser exibida */
    private Integer tipoMensagem;

    /** Descricao da mensagem a ser exibida */
    private String descricaoMensagem;

    /** Diretorio das imagens */
    private String pathImagens;

    /** Tempo em ms que a mensagem ficara exibida */
    private Long tempoExibicaoMensagem;

    /** identifica se a mensagem de processamento ainda deve ser exibida */
    private Boolean processamentoAtividado;

    public InterfaceMensagem() {
        pathImagens = PropertiesUtil.buscarPropriedade("tela.diretorio.imagens");
        tempoExibicaoMensagem = new Long(PropertiesUtil.buscarPropriedade("mensagem.tempo.exibicao")) * 1000;
        processamentoAtividado = Boolean.FALSE;
    }

    public InterfaceMensagem(String descricaoMensagem, Integer tipoMensagem) {
        pathImagens = PropertiesUtil.buscarPropriedade("tela.diretorio.imagens");
        tempoExibicaoMensagem = new Long(PropertiesUtil.buscarPropriedade("mensagem.tempo.exibicao")) * 1000;
        processamentoAtividado = Boolean.FALSE;
        setDescricaoMensagem(descricaoMensagem);
        setTipoMensagem(tipoMensagem);
    }

    public String getDescricaoMensagem() {
        return descricaoMensagem;
    }

    public void setDescricaoMensagem(String descricaoMensagem) {
        this.descricaoMensagem = descricaoMensagem;
    }

    public Integer getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(Integer tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public Boolean getProcessamentoAtividado() {
        return processamentoAtividado;
    }

    public void setProcessamentoAtividado(Boolean processamentoAtividado) {
        this.processamentoAtividado = processamentoAtividado;
    }

    public ImageIcon getImagemTipoMensagem() {
        StringBuffer value = new StringBuffer();
    	// caso o tipo nao tenha sido setado o padrao é informação
        if (tipoMensagem == null) {
            tipoMensagem = MENSAGEM_TIPO_INFORMACAO;
        }

        ImageIcon imgMsg;
         
        if (tipoMensagem.equals(MENSAGEM_TIPO_ERRO)) {
            value.append(pathImagens).append(File.separator).append("erro.png");
        	imgMsg = new ImageIcon(value.toString());
        } else if (tipoMensagem.equals(MENSAGEM_TIPO_ALERTA)) {
        	value.append(pathImagens).append(File.separator).append("alerta.png");
        	imgMsg = new ImageIcon(value.toString());
        } else {
        	value.append(pathImagens).append(File.separator).append("informacao.png");
            imgMsg = new ImageIcon(value.toString());
        }

        return imgMsg;
    }

    public String getPathImagens() {
        return pathImagens;
    }

    public Long getTempoExibicaoMensagem() {
        return tempoExibicaoMensagem;
    }

    public Color getCorFonteMensagem() {

        Color corFonte;

        if (tipoMensagem.equals(MENSAGEM_TIPO_ALERTA)) {
            corFonte = Color.BLACK;
        } else {
            corFonte = Color.WHITE;
        }

        return corFonte;

    }

    public Color getCorFundoMensagem() {

        Color corFundoMensagem = null;

        if (tipoMensagem.equals(MENSAGEM_TIPO_ALERTA)) {
            corFundoMensagem = Color.YELLOW;
        } else if (tipoMensagem.equals(MENSAGEM_TIPO_ERRO)) {
            corFundoMensagem = Color.RED;
        } else if (tipoMensagem.equals(MENSAGEM_TIPO_INFORMACAO)) {
            corFundoMensagem = Color.BLUE;
        } else if (tipoMensagem.equals(MENSAGEM_TIPO_PROCESSAMENTO)) {
            corFundoMensagem = new Color(51, 153, 0);
        }

        return corFundoMensagem;
    }
}
