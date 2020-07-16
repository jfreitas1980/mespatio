package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem;

import java.awt.Rectangle;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.DefinePropriedadesFixas;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoItemCoeficiente;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.EnumEstadosDoFarol;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Painel que apresenta os índices de valor do blend, estimado e embarcado contra a meta da orientação de embarque
 * 
 * @author andre
 *
 */
public class ItemControleBlendadoPnl extends RepresentacaoGrafica {

    /** o item de controle cujos valores estão representados neste painel*/
    private ItemDeControle itemControle;

    /** o item de controle cujos valores da meta da carga estão representados neste painel*/
    private ItemDeControle itemDeControleOrientacaoCarga;
    
    /** o tipo de produto relacionado com este item de controle */
    private TipoProduto tipoDeProduto;

    /** o coeficiente de degradacao usado neste item de controle */
    private TipoItemCoeficiente coeficienteDegradacao;
       
    /** a data-hora da situação do pátio exibida */
    Date dataHoraSituacaoPatio;
    
    /** o número de pixels que o maior texto da régua terá*/
    Integer nrPixelsDoMaiorTextoDaRegua=0;

    /* interfaceblendagem para modos de pintura e busca de regras de farol*/
    private InterfaceBlendagem interfaceBlendagem;
    
    public ItemControleBlendadoPnl(ItemDeControle itemBlend, ItemDeControle itemDeControleOrientacaoCarga, TipoProduto tipoDeProduto, Date dataHoraSituacaoPatio) {
        initComponents();
        this.itemControle = itemBlend;       
        this.itemDeControleOrientacaoCarga = itemDeControleOrientacaoCarga;
        this.tipoDeProduto = tipoDeProduto;        
        this.dataHoraSituacaoPatio = dataHoraSituacaoPatio;
        
        /***
         * TODO CRIAR METODO PARA RECUPERAR COEFICIENTE CORRENTE
         * 
         */
        this.coeficienteDegradacao = this.itemControle.getTipoItemControle().getTipoItemCoeficiente(dataHoraSituacaoPatio);
        
        desenhaBkgQualidade();
//        montaDesenhoItemBlend();
    }    

    /** 
     * Metodo que escreve no painel as informacoes dos itens de controle blendado
     */
    public void montaDesenhoItemBlend() {
        lblDescricaoItemControle.setText(itemControle.getTipoItemControle().getDescricaoTipoItemControle());
        lblUnidadeVariacao.setText(itemControle.getTipoItemControle().getUnidade());

        montaEscalaItemControle();
        EnumEstadosDoFarol estadoFarol = interfaceBlendagem.getControladorItemDeControle().determinaEstadoDoFarol(tipoDeProduto, itemDeControleOrientacaoCarga, this.dataHoraSituacaoPatio);
        if (estadoFarol != null) {
            if (estadoFarol.equals(EnumEstadosDoFarol.COMPLIANT)) {
                ImageIcon imgSemaforoVerde = InterfaceBlendagem.imgSemaforoVerde;
                lblStatusBlendagem.setIcon(imgSemaforoVerde);
                lblStatusBlendagem.setBounds(39, 7, 12, 12);

            } else if (estadoFarol.equals(EnumEstadosDoFarol.NONCOMPLIANT)) {
                ImageIcon imgSemaforoVermelho = InterfaceBlendagem.imgSemaforoVermelho;
                lblStatusBlendagem.setIcon(imgSemaforoVermelho);
                lblStatusBlendagem.setBounds(22, 7, 12, 12);

            } else if (estadoFarol.equals(EnumEstadosDoFarol.OVERSPECIFIED)) {
                ImageIcon imgSemaforoAzul = InterfaceBlendagem.imgSemaforoAzul;
                lblStatusBlendagem.setIcon(imgSemaforoAzul);
                lblStatusBlendagem.setBounds(4, 7, 12, 12);

            }
        }

        // Valor e desvio padrão do valor
        if (itemControle.getValor() != null) {
            lblValor.setVisible(Boolean.TRUE);
            lblValor.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.valor") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemControle.getValor(),2));
            lblValor.setBounds(calculaDimensaoBlend(lblVariacoesBlend, itemControle.getValor(), lblValor));
            lblValorMES.setText(DSSStockyardFuncoesNumeros.getValorFormatado(itemControle.getValor(),2));
        } else {
            lblValor.setVisible(false);
        }
        
        //TODO desvio padrao nao sera mostrado enquanto o calculo nao for revisado
        lblDesvioPadraoValor.setVisible(false);
        /*if (itemControle.getDesvioPadraoValor() != null) {
            lblDesvioPadraoValor.setVisible(Boolean.TRUE);
            lblDesvioPadraoValor.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.desvio.padrao") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemControle.getDesvioPadraoValor(),2));
            lblDesvioPadraoValor.setBounds(calculaDimensaoDesvioPadrao(lblVariacoesBlend, itemControle.getValor(), itemControle.getDesvioPadraoValor(), lblValor, lblDesvioPadraoValor));
        } else {
            lblDesvioPadraoValor.setVisible(false);
        }*/

        //Estimado e desvio padrao do estimado
        if (coeficienteDegradacao != null && itemControle.getValor() != null)
        {
           lblEstimado.setVisible(Boolean.TRUE);
           lblEstimado.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.estimado") + DSSStockyardFuncoesNumeros.getQtdeFormatada( itemControle.getValor() + coeficienteDegradacao.getValorDoCoeficiente(),2));
           lblEstimado.setBounds(calculaDimensaoBlend(lblVariacoesBlend,  ((itemControle.getValor() + coeficienteDegradacao.getValorDoCoeficiente()) ), lblEstimado));
        }else if ((coeficienteDegradacao == null || coeficienteDegradacao.getValorDoCoeficiente() == 0) && itemControle.getValor() != null) {
        	lblEstimado.setVisible(Boolean.TRUE);
            lblEstimado.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.estimado") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemControle.getValor(),2));
            lblEstimado.setBounds(calculaDimensaoBlend(lblVariacoesBlend, itemControle.getValor(), lblEstimado));
		 } else {
            lblEstimado.setVisible(false);
            lblDesvioPadraoEstimado.setVisible(false);
        }
        lblDesvioPadraoEstimado.setVisible(false);
        //TODO desvio padrao nao sera mostrado enquanto o calculo nao for revisado
        lblMetaUnica.setVisible(false);
        lblMeta.setVisible(false);
        /*if (coeficienteDegradacao != null && itemControle.getDesvioPadraoValor() != null) {
            lblDesvioPadraoEstimado.setVisible(Boolean.TRUE);
            lblDesvioPadraoEstimado.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.desvio.padrao.estimado") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemControle.getDesvioPadraoValor() * coeficienteDegradacao.getValorDoCoeficiente(),2));
            lblDesvioPadraoEstimado.setBounds(calculaDimensaoDesvioPadrao(lblVariacoesBlend, (itemControle.getValor() * coeficienteDegradacao.getValorDoCoeficiente()), (itemControle.getDesvioPadraoValor() * coeficienteDegradacao.getValorDoCoeficiente()), lblEstimado, lblDesvioPadraoEstimado));
        } else {
            lblMetaUnica.setVisible(false);
            lblMeta.setVisible(false);
        }*/

        // Embarcado e desvio padrão embarcado
        if (itemControle.getEmbarcado() != null) {
            lblEmbarcado.setVisible(Boolean.TRUE);
            lblEmbarcado.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.embarcado") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemControle.getEmbarcado(),2));
            lblEmbarcado.setBounds(calculaDimensaoBlend(lblVariacoesBlend, itemControle.getEmbarcado(), lblEmbarcado));
        } else {
            lblEmbarcado.setVisible(false);
        }
        
        //TODO desvio padrao nao sera mostrado enquanto o calculo nao for revisado
        lblDesvioPadraoEmbarcado.setVisible(false);
        /*if (itemControle.getDesvioPadraoEmbarcado() != null) {
            lblDesvioPadraoEmbarcado.setVisible(Boolean.TRUE);
            lblDesvioPadraoEmbarcado.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.desvio.padrao.embarcado") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemControle.getDesvioPadraoEmbarcado(),2));
            lblDesvioPadraoEmbarcado.setBounds(calculaDimensaoDesvioPadrao(lblVariacoesBlend, itemControle.getEmbarcado(), itemControle.getDesvioPadraoEmbarcado(), lblEmbarcado, lblDesvioPadraoEmbarcado));
        } else {
            lblDesvioPadraoEmbarcado.setVisible(false);
        }*/

        // Meta da orientação de embarque
        if (itemDeControleOrientacaoCarga != null) {
            if (itemDeControleOrientacaoCarga.getLimSupMetaOrientacaoEmb() != null && itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb() != null) {
                if (itemDeControleOrientacaoCarga.getLimSupMetaOrientacaoEmb().equals(itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb())) {
                    lblMeta.setVisible(false);
                    lblMetaUnica.setVisible(Boolean.TRUE);
                    lblMetaUnica.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.meta") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb(),2));
                    lblMetaUnica.setBounds(calculaDimensaoBlend(lblVariacoesBlend, itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb(), lblMetaUnica));
                } else {
                    lblMetaUnica.setVisible(false);
                    Double centroDaFaixa = (itemDeControleOrientacaoCarga.getLimSupMetaOrientacaoEmb() + itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb()) / 2;
                    Double variacaoDaFaixa = itemDeControleOrientacaoCarga.getLimSupMetaOrientacaoEmb() - centroDaFaixa;
                    lblMeta.setVisible(Boolean.TRUE);
                    StringBuffer value = new StringBuffer();
                    value.append(PropertiesUtil.getMessage("tooltip.controle.blendado.faixa.meta")).append(DSSStockyardFuncoesNumeros.getQtdeFormatada(itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb(),2)).append(" - ").append(DSSStockyardFuncoesNumeros.getQtdeFormatada(itemDeControleOrientacaoCarga.getLimSupMetaOrientacaoEmb(),2));                    
                    lblMeta.setToolTipText(value.toString());
                    //lblMeta.setToolTipText(PropertiesUtil.getMessage("tooltip.controle.blendado.faixa.meta") + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemDeControleOrientacaoCarga.getLimInfMetaOrientacaoEmb(),2) + " - " + DSSStockyardFuncoesNumeros.getQtdeFormatada(itemDeControleOrientacaoCarga.getLimSupMetaOrientacaoEmb(),2));
                    lblMeta.setBounds(calculaDimensaoDesvioPadrao(lblVariacoesBlend, centroDaFaixa, variacaoDaFaixa, lblMeta, lblMeta));
                    value = null;
                }
            } else {
                lblMetaUnica.setVisible(false);
                lblMeta.setVisible(false);
            }
        } else {
            lblMetaUnica.setVisible(false);
            lblMeta.setVisible(false);
        }
    }


    /**
     * Calcula a posicao das informacoes do desvio padrao da blendagem de cada item de controle
     *
     * @param lblEscala
     * @param itemBlend
     * @param valorDesvio
     */
    private Rectangle calculaDimensaoDesvioPadrao(JLabel lblEscala, Double valorBaseDesvio, Double valorDesvio, JLabel lblItem, JLabel itemDesvioPadrao) {

        // calcula a posicao do desvio padrao abaixo do blend
        Rectangle rectDesvioInicio = calculaDimensaoBlend(lblEscala, valorBaseDesvio - valorDesvio, lblItem);

        // calcula a posicao do desvio padrao acima do blend
        Rectangle rectDesvioFim = calculaDimensaoBlend(lblEscala, valorBaseDesvio + valorDesvio, lblItem);
        int larguraDesvioPadrao = (int) (rectDesvioFim.getX() - rectDesvioInicio.getX());

        // cria um novo posicionamento e tamanho para o desvio padrao utilizando o inicio e o fim do desvio
        Rectangle rectDesvioPadrao = new Rectangle();
        rectDesvioPadrao.setBounds((int) rectDesvioInicio.getX(), (int) itemDesvioPadrao.getBounds().getY(), larguraDesvioPadrao, (int) itemDesvioPadrao.getBounds().getHeight());
        return rectDesvioPadrao;
    }

    /**
     * Metodo auxiliar que calcula a posicao dos indicadores de blendagem
     * @param lblEscala
     * @param itemBlend
     * @param valorCalculado
     * @return
     */
    private Rectangle calculaDimensaoBlend(JLabel lblEscala, Double valorCalculado, JLabel lblItem) {
        Rectangle dimensaoObjeto = lblItem.getBounds();
        int posicaoX = 0;

        // variavel que corresponde a quantidade de pixel que 1 espaco em branco possui
        Integer nrPixelEscala = lblEscala.getWidth();
       
        posicaoX = (int) ((nrPixelEscala * (valorCalculado - itemControle.getTipoItemControle().getInicioEscala())) / (itemControle.getTipoItemControle().getFimEscala() - itemControle.getTipoItemControle().getInicioEscala()));

        // deslocando os itens do número de pixels do texto para o primeiro não colar na parede e subtraindo a largura do desenho
        posicaoX = posicaoX + nrPixelsDoMaiorTextoDaRegua/2;

        dimensaoObjeto.setBounds(posicaoX, (int) dimensaoObjeto.getY(), (int) dimensaoObjeto.getWidth(), (int) dimensaoObjeto.getHeight());
        return dimensaoObjeto;
    }
    
    /** Retorno a situacao do item de controle blendado que teve sua especificacao atendida */
    private Integer getSituacaoItemBlendadoAtendido() {
        return new Integer(PropertiesUtil.buscarPropriedade("situacao.item.controle.blendado.atendido"));
    }

    /** Retorno a situacao do item de controle blendado que nao teve sua especificacao atendida */
    private Integer getSituacaoItemBlendadoNaoAtendido() {
        return new Integer(PropertiesUtil.buscarPropriedade("situacao.item.controle.blendado.nao.atendido"));
    }

    /** Retorno a situacao do item de controle blendado que teve sua especificacao atendida com qualidade superior */
    private Integer getSituacaoItemBlendadoAtendidoSuperior() {
        return new Integer(PropertiesUtil.buscarPropriedade("situacao.item.controle.blendado.atendido.superior"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDescricaoItemControle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblUnidadeVariacao = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblDesvioPadraoValor = new javax.swing.JLabel();
        lblValor = new javax.swing.JLabel();
        lblEstimado = new javax.swing.JLabel();
        lblDesvioPadraoEstimado = new javax.swing.JLabel();
        lblPontosEscala = new javax.swing.JLabel();
        lblVariacoesBlend = new javax.swing.JLabel();
        lblMeta = new javax.swing.JLabel();
        lblEmbarcado = new javax.swing.JLabel();
        lblDesvioPadraoEmbarcado = new javax.swing.JLabel();
        lblMetaUnica = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblStatusBlendagem = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblValorMES = new javax.swing.JLabel();

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(427, 93));

        lblDescricaoItemControle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel1.setText("Unidade");

        lblUnidadeVariacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        lblDesvioPadraoValor.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jPanel1.add(lblDesvioPadraoValor);
        lblDesvioPadraoValor.setBounds(40, 20, 50, 12);
        StringBuffer value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/blendRP.png");        
       // lblValor.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/blendRP.png"));
        lblValor.setIcon(new javax.swing.ImageIcon(value.toString()));
        jPanel1.add(lblValor);
        lblValor.setBounds(50, 10, 2, 30);
        value = null;
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/blendEstimadoRP.png");
        //lblEstimado.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/blendEstimadoRP.png"));
        lblEstimado.setIcon(new javax.swing.ImageIcon(value.toString()));
        jPanel1.add(lblEstimado);
        lblEstimado.setBounds(140, 10, 2, 30);
        value = null;
        lblDesvioPadraoEstimado.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 255), new java.awt.Color(0, 0, 255)));
        jPanel1.add(lblDesvioPadraoEstimado);
        lblDesvioPadraoEstimado.setBounds(120, 20, 30, 12);

        lblPontosEscala.setBackground(java.awt.Color.white);
        lblPontosEscala.setFont(new java.awt.Font("Tahoma", 0, 9));
        jPanel1.add(lblPontosEscala);
        lblPontosEscala.setBounds(10, 20, 360, 10);

        lblVariacoesBlend.setBackground(java.awt.Color.white);
        lblVariacoesBlend.setFont(new java.awt.Font("Tahoma", 0, 9));
        jPanel1.add(lblVariacoesBlend);
        lblVariacoesBlend.setBounds(10, 0, 360, 10);

        lblMeta.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(26, 255, 0), new java.awt.Color(26, 255, 0)));
        jPanel1.add(lblMeta);
        lblMeta.setBounds(200, 20, 50, 12);
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/blendEmbarcadoRP.png");
        //lblEmbarcado.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/blendEmbarcadoRP.png"));
        lblEmbarcado.setIcon(new javax.swing.ImageIcon(value.toString()));
        value = null;
        jPanel1.add(lblEmbarcado);
        lblEmbarcado.setBounds(300, 10, 2, 30);

        lblDesvioPadraoEmbarcado.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 128, 0), new java.awt.Color(255, 128, 0)));
        jPanel1.add(lblDesvioPadraoEmbarcado);
        lblDesvioPadraoEmbarcado.setBounds(280, 20, 30, 12);
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/blendMetaRP.png");
        //lblMetaUnica.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/blendMetaRP.png"));
        lblMetaUnica.setIcon(new javax.swing.ImageIcon(value.toString()));
        jPanel1.add(lblMetaUnica);
        lblMetaUnica.setBounds(180, 10, 2, 30);
        value = null;
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(93, 16));
        jPanel2.setLayout(null);
        jPanel2.add(lblStatusBlendagem);
        lblStatusBlendagem.setBounds(0, 0, 0, 0);

        jLabel2.setText("MES:");

        lblValorMES.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblDescricaoItemControle, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUnidadeVariacao, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorMES, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(lblValorMES, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblUnidadeVariacao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(lblDescricaoItemControle, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblDescricaoItemControle;
    private javax.swing.JLabel lblDesvioPadraoEmbarcado;
    private javax.swing.JLabel lblDesvioPadraoEstimado;
    private javax.swing.JLabel lblDesvioPadraoValor;
    private javax.swing.JLabel lblEmbarcado;
    private javax.swing.JLabel lblEstimado;
    private javax.swing.JLabel lblMeta;
    private javax.swing.JLabel lblMetaUnica;
    private javax.swing.JLabel lblPontosEscala;
    private javax.swing.JLabel lblStatusBlendagem;
    private javax.swing.JLabel lblUnidadeVariacao;
    private javax.swing.JLabel lblValor;
    private javax.swing.JLabel lblValorMES;
    private javax.swing.JLabel lblVariacoesBlend;
    // End of variables declaration//GEN-END:variables

    private void desenhaBkgQualidade()
   {
       this.setImagemDSP(InterfaceBlendagem.imagemFundoBlend);
       this.setDimensaoImagem(new Rectangle(416, 65));
   }

    public InterfaceBlendagem getInterfaceBlendagem() {
        return interfaceBlendagem;
    }

    public void setInterfaceBlendagem(InterfaceBlendagem interfaceBlendagem) {
        this.interfaceBlendagem = interfaceBlendagem;
    }

    private void montaEscalaItemControle(){
        jPanel1.add(DefinePropriedadesFixas.getMapaEscalaMetas().get(itemControle.getTipoItemControle().getIdTipoItemDeControle()));
    }

    
}
