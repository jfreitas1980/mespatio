package com.hdntec.gestao.cliente.interfaceDeDialogo.uteis;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHandler {

    /**
     * Carrega o buffer da imagem usando do diretorio e do nome recebidos
     * 
     * @param caminhoCompleto
     * @param nomeImagem
     * @return
         */
    public static BufferedImage carregarImagem(final String caminhoCompleto, final String nomeImagem) {
        try {
            final BufferedImage imagem = ImageIO.read(carregarImagemArquivo(caminhoCompleto, nomeImagem));
            return imagem;
        } catch (IOException ioEx) {
            return null;
        }
    }

    /**
     * Carrega a imagem em um arquivo
     * 
     * @param caminhoCompleto
     * @param nomeImagem
     * @return
     */
    public static File carregarImagemArquivo(final String caminhoCompleto, final String nomeImagem) {
        final File arquivoImagem = new File(criaURLDaImagem(caminhoCompleto, nomeImagem));
        return arquivoImagem;
    }

    private static String criaURLDaImagem(final String caminhoCompleto, final String nomeImagem) {

        final StringBuilder sb = new StringBuilder();
        sb.append(caminhoCompleto);

        if (!caminhoCompleto.endsWith(File.separator)) {
            sb.append(File.separatorChar);
        }

        sb.append(nomeImagem);

        return sb.toString();
    }

    /**
         *
         * @param im
         * @param scale
         * @return
         */
    public static Image getEscalaImagem(final Image im, final float scale) {

        int width = (int) (im.getWidth(null) * scale);
        int height = (int) (im.getHeight(null) * scale);

        if (width < 1) { // checking scale results
            width = 1;
        }

        if (height < 1) {
            height = 1;
        }

        final BufferedImage novaImagem = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        final Graphics g = novaImagem.getGraphics();

        g.drawImage(im, 0, 0, width, height, null);

        return novaImagem;

    }
}