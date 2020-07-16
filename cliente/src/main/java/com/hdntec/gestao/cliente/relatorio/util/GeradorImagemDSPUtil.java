package com.hdntec.gestao.cliente.relatorio.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.ControladorDSP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceCorreia;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceDSP;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceDadosEdicao;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceMaquinaDoPatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePatio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePilha;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceDeDialogo.uteis.ImageHandler;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorCorreias;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorPatios;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * 
 * <P><B>Description :</B><BR>
 * General GeradorImagemDSPUtil
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 07/08/2009
 * @version $Revision: 1.1 $
 */
public class GeradorImagemDSPUtil {
    
    private static BufferedImage imagemProduto = null;
    private static BufferedImage imagemTriangulo = null;

    /**
     *
     * getPanelInterfaceDSP
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 09/06/2009
     * @see
     * @return
     * @return Returns the JPanel.
     */
    @SuppressWarnings("unchecked")
    public static JPanel getPanelInterfaceDSP(SituacaoPatio situacaoPatio,
            IControladorInterfaceInicial controladorInterfaceInicial)
    {

        JPanel panelInterfaceDSP = new JPanel();
        panelInterfaceDSP.setBackground(new java.awt.Color(255, 255, 255));
        panelInterfaceDSP.setLayout(null);
        panelInterfaceDSP.setBounds(0, 0, 1320, 730);
        // Eventos acionado qdo algum componente for adicionado ao painel do DSP
        panelInterfaceDSP.addContainerListener(new java.awt.event.ContainerAdapter()
        {

            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt)
            {
                ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
            }
        });

        // cria uma nova interfaceDSP e adiciona os componentes necessarios para sua exibicao
        InterfaceDSP interDSP = new InterfaceDSP();
        interDSP.setComponenteInterfaceDSP(panelInterfaceDSP);
        interDSP.setComponenteInterfaceUsina(null);
        interDSP.getControladorDSP().setInterfaceInicial(controladorInterfaceInicial);
        interDSP.getControladorDSP().setInterfaceDadosEdicao(new InterfaceDadosEdicao());

        //ordena patios para evitar problemas futuros
        List patios = situacaoPatio.getPlanta().getListaPatios(situacaoPatio.getDtInicio());
        Collections.sort(patios, new ComparadorPatios());
        criaInterfacePatio(panelInterfaceDSP, situacaoPatio, interDSP);

        //ordena as correias pelos seus numeros antes de inserir as maquinas nelas para nÃ£o haver problemas de exibiÃ§Ã£o
        List correias = situacaoPatio.getPlanta().getListaCorreias(situacaoPatio.getDtInicio());
        Collections.sort(correias, new ComparadorCorreias());
        // monta a interface com as correias existentes no plano oficial
        criaInterfaceCorreia(panelInterfaceDSP, situacaoPatio, interDSP);

        return panelInterfaceDSP;
    }

    /**
     *
     * getInterfacePatio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 10/06/2009
     * @see
     * @param panelInterfaceDSP
     * @param situacaoPatio
     * @param interDSP
     * @return Returns the void.
     */
    private static void criaInterfacePatio(JPanel panelInterfaceDSP,
            SituacaoPatio situacaoPatio,
            InterfaceDSP interDSP)
    {

        // cria uma lista de interfaces de patio
        List<InterfacePatio> listaInterfacePatio = new ArrayList<InterfacePatio>();
        
        
        //Criando um novo controlador
        ControladorDSP controlDSP = new ControladorDSP();
        
        InterfacePatio interfacePatio;

        for (Patio patio : situacaoPatio.getPlanta().getListaPatios(situacaoPatio.getDtInicio()))
        {

            // cria uma nova objeto interface de patio
            interfacePatio = new InterfacePatio(situacaoPatio.getDtInicio());

            interfacePatio.setInterfaceDSP(interDSP);
            interfacePatio.setPatioVisualizado(patio);
                        interfacePatio.setControladorDSP(interDSP.getControladorDSP());

            List<InterfacePilha> listaInterfacePilhaDoPatio = new ArrayList<InterfacePilha>();
            for (Pilha pilha : situacaoPatio.getListaDePilhasNosPatios(situacaoPatio.getDtInicio()))
            {
                if (pilha.verificarPatioDaPilha(patio))
                {

                    InterfacePilha interfacePilha = new InterfacePilha();
                    interfacePilha.setPilhaVisualizada(pilha);

                    List<InterfaceBaliza> listaInterfaceBaliza = new ArrayList<InterfaceBaliza>();
                    for (Baliza baliza : pilha.getListaDeBalizas())
                    {
                        InterfaceBaliza interfaceBaliza = new InterfaceBaliza();

                        interfaceBaliza.setBalizaVisualizada(baliza);
                        interfaceBaliza.setInterfacePatio(interfacePatio);

                        listaInterfaceBaliza.add(interfaceBaliza);
                    }

                    interfacePilha.setListaDeBalizas(listaInterfaceBaliza);
                    interfacePilha.setInterfaceDSP(interDSP);

                    listaInterfacePilhaDoPatio.add(interfacePilha);
                }
            }

            interfacePatio.setListaDePilhas(listaInterfacePilhaDoPatio);
            listaInterfacePatio.add(interfacePatio);
        }

        // adiciona a lista de interfaces de patio na interface DSP
        interDSP.setListaDePatios(listaInterfacePatio);
        for (InterfacePatio iPatio : interDSP.getListaDePatios())
        {
            // adiciona as interfaces de patio na tela
            panelInterfaceDSP.add(iPatio);
        }
    }

    /**
     *
     * criaInterfaceCorreia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 10/06/2009
     * @see
     * @return Returns the void.
     */
    private static void criaInterfaceCorreia(JPanel panelInterfaceDSP,
            SituacaoPatio situacaoPatio,
            InterfaceDSP interDSP)
    {

        List<InterfaceCorreia> listaInterfacesCorreia = new ArrayList<InterfaceCorreia>();

        // percorre a lista de correias da planta
        for (Correia correiaPatio : situacaoPatio.getPlanta().getListaCorreias(situacaoPatio.getDtInicio()))
        {
            // cria uma nova interface de correia
            InterfaceCorreia interfaceCorreia = new InterfaceCorreia();
            interfaceCorreia.setCorreiaVisualizada(correiaPatio);
            interfaceCorreia.setInterfaceDSP(interDSP);
            interfaceCorreia.setHoraSituacao(situacaoPatio.getDtInicio());
            // monta a interface das maquinas que pertencem a correia
            
            criaInterfaceMaquinasDaCorreia(interDSP, interfaceCorreia);
            // adiciona a interfaceCorreia na lista
            listaInterfacesCorreia.add(interfaceCorreia);
        }
        // adiciona as correias da planta no DSP
        interDSP.setListaCorreias(listaInterfacesCorreia);
        for (InterfaceCorreia iCorreia : listaInterfacesCorreia)
        {
            panelInterfaceDSP.add(iCorreia);
        }

        // adiciona as maquinas das correias da planta no DSP
        for (InterfaceCorreia iCorreia : listaInterfacesCorreia)
        {
            // adiciona a lista de mÃ¡quinas
            if (iCorreia.getListaDeMaquinas() != null)
            {
                for (InterfaceMaquinaDoPatio itMaq : iCorreia.getListaDeMaquinas())
                {
                    panelInterfaceDSP.add(itMaq);
                }
            }
        }
    }

    /**
     *
     * criaInterfaceMaquinasDaCorreia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 25/06/2009
     * @see
     * @param interfaceCorreia
     * @return Returns the void.
     */
    private static void criaInterfaceMaquinasDaCorreia(InterfaceDSP interDSP, InterfaceCorreia interfaceCorreia)
    {

        // verifica se existe alguma mÃ¡quina na correia
        if (interfaceCorreia.getCorreiaVisualizada().getListaDeMaquinas(interfaceCorreia.getHoraSituacao()) != null)
        {
            // percorre a lista de mÃ¡quinas da correia
            List<InterfaceMaquinaDoPatio> listaInterfaceMaquina = new ArrayList<InterfaceMaquinaDoPatio>();
            for (MaquinaDoPatio maquina : interfaceCorreia.getCorreiaVisualizada().getListaDeMaquinas(interfaceCorreia.getHoraSituacao()))
            {
                // cria uma interface mÃ¡quina
                /*InterfaceMaquinaDoPatio interfaceMaquina = new InterfaceMaquinaDoPatio();
                interfaceMaquina.setMaquinaDoPatioVisualizada(maquina);
                interfaceMaquina.setCorreia(interfaceCorreia);
                interfaceMaquina.setPosicao(procuraInterfaceBalizaDoPatio(interDSP, maquina.getPosicao(), maquina.getPosicao().getPatio()));*/

                InterfaceMaquinaDoPatio interfaceMaquina = new InterfaceMaquinaDoPatio();
                interfaceMaquina.setMaquinaDoPatioVisualizada(maquina);
                interfaceMaquina.setCorreia(interfaceCorreia);
                interfaceMaquina.setPosicao(procuraInterfaceBalizaDoPatio(interDSP, maquina.getPosicao(), maquina.getPosicao().getPatio()));
                interfaceMaquina.setControladorDSP(interDSP.getControladorDSP());
                
                listaInterfaceMaquina.add(interfaceMaquina);
            }
            // adiciona a lista de interfaceMaquina na interface correia
            interfaceCorreia.setListaDeInterfaceMaquinas(listaInterfaceMaquina);
        }
    }

    /**
     *
     * procuraInterfaceBalizaDoPatio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 25/06/2009
     * @see
     * @param interDSP
     * @param baliza
     * @param patio
     * @return
     * @return Returns the InterfaceBaliza.
     */
    private static InterfaceBaliza procuraInterfaceBalizaDoPatio(InterfaceDSP interDSP, Baliza baliza, Patio patio)
    {
        InterfaceBaliza interfaceBalizaEncontrada = null;
        for (InterfacePatio interfacePatio : interDSP.getListaDePatios()) {
            if (interfacePatio.getPatioVisualizado().getMetaPatio().equals(patio.getMetaPatio())) {
                for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas()) {
                    if (interfaceBaliza.getBalizaVisualizada().getMetaBaliza().equals(baliza.getMetaBaliza())) {
                        interfaceBalizaEncontrada = interfaceBaliza;
                        break;
                    }
                }
                if (interfaceBalizaEncontrada != null) {
                    break;
                }
            }
        }
        return interfaceBalizaEncontrada;
    }

    /**
     *
     * getImagemLegendaRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 25/06/2009
     * @see
     * @return Returns the void.
     * @throws ErroSistemicoException
     */
    public static BufferedImage getImagemLegendaRelatorio(List<TipoProduto> listaTiposProduto) throws ErroSistemicoException
    {

        final String diretorioImagem = "tela.diretorio.imagens";
        //Melhorando a performance do relatorio
        if (imagemProduto == null) {
            imagemProduto = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "bkg_produtos.png");
        }
        if (imagemTriangulo == null) {
            imagemTriangulo = ImageHandler.carregarImagem(PropertiesUtil.buscarPropriedade(diretorioImagem), "triangulo-legenda-produto.png");
        }
        
        //define a identificacao da legenda
        RepresentacaoGrafica pnlIdentificacaoTipoProduto = new RepresentacaoGrafica(imagemProduto);
        pnlIdentificacaoTipoProduto.setDimensaoImagem(new Rectangle(75, 33));
        pnlIdentificacaoTipoProduto.setBounds(new Rectangle(75, 33));
        pnlIdentificacaoTipoProduto.setMinimumSize(new Dimension(75, 33));
        pnlIdentificacaoTipoProduto.setPreferredSize(new Dimension(75, 33));
        pnlIdentificacaoTipoProduto.setLayout(null);
        
        Color cor;

        int width = 75;
        int height = 0;
        Image[] representacaoList = new Image[listaTiposProduto.size()];
        int i = 0;
        for (TipoProduto tipoProduto : listaTiposProduto)
        {
            pnlIdentificacaoTipoProduto.removeAll();
            
            //label com a descricao do tipo de produto
            JLabel lblDescricao = new JLabel();
            if (!tipoProduto.getCodigoFamiliaTipoProduto().equalsIgnoreCase("-"))
            {
                lblDescricao.setText(tipoProduto.getCodigoFamiliaTipoProduto() + " - " + tipoProduto.getCodigoTipoProduto());
            }
            else
            {
                lblDescricao.setText(tipoProduto.getCodigoTipoProduto());
            }
            lblDescricao.setToolTipText(tipoProduto.getDescricaoTipoProduto());
            lblDescricao.setFont(new Font("Arial", Font.PLAIN, 9));
            lblDescricao.setBounds(5, 5, 70, 9);

            pnlIdentificacaoTipoProduto.add(lblDescricao);

            String[] rgb = tipoProduto.getCorIdentificacao().split(",");
            RepresentacaoGrafica pnlCorIndentificacao = new RepresentacaoGrafica(imagemTriangulo);
            cor = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            pnlCorIndentificacao.setBackground(Color.WHITE);
            pnlCorIndentificacao.setDimensaoImagem(new Rectangle(25, 13));

            pintaFundoImagemLegendaProduto(pnlCorIndentificacao.getImagemDSP(), cor.getRGB());

            pnlIdentificacaoTipoProduto.add(pnlCorIndentificacao);
            pnlCorIndentificacao.setBounds((pnlIdentificacaoTipoProduto.getWidth() / 2) - pnlCorIndentificacao.getImagemDSP().getWidth() + 10, 15, 26, 14);

            representacaoList[i++] = getImageFromPanel(pnlIdentificacaoTipoProduto, 75, 33);

            height += 33;
        }
        
        pnlIdentificacaoTipoProduto = null;

        return getImageFromBuffer(representacaoList, width, height);
    }

    /**
     *
     * getImageFromPanel
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 25/06/2009
     * @see
     * @param panel
     * @param width
     * @param height
     * @return
     * @return Returns the BufferedImage.
     */
    public static BufferedImage getImageFromPanel(JPanel panel, int width, int height)
    {
        BufferedImage image = new BufferedImage(width, height,
                Transparency.BITMASK);
        Graphics2D g2 = image.createGraphics();
        panel.paint(g2);
        g2.dispose();
        return image;
    }

    /**
     *
     * getImageFromBuffer
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 25/06/2009
     * @see
     * @param buffer
     * @param width
     * @param height
     * @return
     * @return Returns the BufferedImage.
     */
    private static BufferedImage getImageFromBuffer(Image[] buffer, int width, int height)
    {

        BufferedImage imagens = new BufferedImage(width, height, Transparency.BITMASK);
        Graphics2D g = imagens.createGraphics();
        int altura = 0;
        for (int x = 0; x < buffer.length; x++)
        {
            g.drawImage(buffer[x], 0, altura, null);
            altura += buffer[x].getHeight(null);
        }
        return imagens;
    }

    /**
     * pinta a imagem do triangulo da legenda com as cores dos produtos da carga
     * @param imagemTriangulo
     *
     *    .       .    .
     *   ..       .    .
     *  ....  =  ..  + ..
     * ......   ...    ...
     */
    private static void pintaFundoImagemLegendaProduto(BufferedImage imagemTriangulo, int rgb)
    {
        int largura = imagemTriangulo.getWidth();
        int altura = imagemTriangulo.getHeight();
        int meioTriangulo = largura / 2;
        int colunas = 0;

        //pinta a primeira metado do triandulo de baixo para cima, e da direita para esquerda
        for (int x = altura; x > 0; x--)
        {//movimenta sobre as linhas
            for (int y = meioTriangulo; y > colunas; y--)
            {//movimento sobre as colunas de cada linha
                imagemTriangulo.setRGB(x, y, rgb);
            }
            colunas += 1;
        }

        //pinta a segunda metade do triangulo de baixo para cima, e da esquerda paa direita
        for (int y = altura - 1; y > 0; y--)
        {
            for (int x = meioTriangulo; x < largura - 1; x++)
            {
                imagemTriangulo.setRGB(x, y, rgb);
            }
            largura -= 1;
        }
    }

}