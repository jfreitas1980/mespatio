package com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.ImageHandler;
import com.hdntec.gestao.util.PropertiesUtil;

@SuppressWarnings("serial")
public class RepresentacaoGrafica extends JPanel {

    private final String TELA_DIRETORIO_IMAGENS = "tela.diretorio.imagens";

    /** Imagem a ser mostrada no DSP */
    private BufferedImage imagemDSP = null;

    /** Define a posicao e o tamanho da imagem */
    private int width = 0,  height = 0;

    public RepresentacaoGrafica() {
        super();
    }

    public RepresentacaoGrafica(String nomeImagem) {
        super();
        imagemDSP = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(TELA_DIRETORIO_IMAGENS), nomeImagem);
    }

    public RepresentacaoGrafica(BufferedImage imagemDSP) {
        super();
        this.imagemDSP = imagemDSP;
    }

    public void setImagemDSP(String nomeImagem) {
        imagemDSP = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(TELA_DIRETORIO_IMAGENS), nomeImagem);
    }

    public void setImagemDSP(BufferedImage imagemDSP) {
        this.imagemDSP = imagemDSP;
    }

    public BufferedImage getImagemDSP() {
        return imagemDSP;
    }
    
    public void setDimensaoImagem(int eixoX, int eixoY, int width, int height) {
        this.width = width;
        this.height = height;

        setBounds(eixoX, eixoY, width, height);

        repaint();
    }

    public void setDimensaoImagem(Rectangle dimensao) {
        this.width = (int) dimensao.getWidth();
        this.height = (int) dimensao.getHeight();

        setBounds((int) dimensao.getX(), (int) dimensao.getY(), width, height);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(imagemDSP, 0, 0, width, height, null);
        g2d.dispose();
    }
}