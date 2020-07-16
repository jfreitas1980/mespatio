package com.hdntec.gestao.cliente.interfaceDeDialogo.uteis;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 *
 * @author Bruno Gomes
 */
public class DefinePropriedadesFixas {

    //variaveis
    private static HashMap<Long , JPanel> mapaEscalaMetas;
    private static HashMap<Long , JPanel> mapaEscalaMetasProducao;
    private static HashMap<Long , JPanel> mapaEscalaMetasEmbarque;

    private static Integer nrPixelsDoMaiorTextoDaRegua;
    private static JPanel jPanel1;
    private static JLabel lblVariacoesBlend;
    private static JLabel lblPontosEscala;

    /**
     * construtor
     */
    public DefinePropriedadesFixas(){
        lblVariacoesBlend = new JLabel();
        lblVariacoesBlend.setBounds(0, 0,
                Integer.parseInt( PropertiesUtil.buscarPropriedade("dimensao.comprimento.label.variacaoBlend")),
                Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.altura.label.variacaoBlend")));
        lblPontosEscala = new JLabel();
        lblPontosEscala.setBounds(0, 0,
                Integer.parseInt( PropertiesUtil.buscarPropriedade("dimensao.comprimento.label.variacaoBlend")),
                Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.altura.label.variacaoBlend")));

        mapaEscalaMetas = new HashMap<Long, JPanel>();
        mapaEscalaMetasEmbarque = new HashMap<Long, JPanel>();
        mapaEscalaMetasProducao = new HashMap<Long, JPanel>();
    }

    public void criaPainelEscalaMetas(List<TipoItemDeControle> listaDeTipoItemDeControle)
    {
        for (TipoItemDeControle tipoItemDeControle : listaDeTipoItemDeControle) {
            desenhaEscalaParaMetas(tipoItemDeControle);
            if (mapaEscalaMetas.get(tipoItemDeControle.getIdTipoItemDeControle()) == null) {
                mapaEscalaMetas.put(tipoItemDeControle.getIdTipoItemDeControle(), jPanel1);
            }
            desenhaEscalaParaMetas(tipoItemDeControle);
            if(mapaEscalaMetasEmbarque.get(tipoItemDeControle.getIdTipoItemDeControle()) == null){
                mapaEscalaMetasEmbarque.put(tipoItemDeControle.getIdTipoItemDeControle(), jPanel1);
            }
            desenhaEscalaParaMetas(tipoItemDeControle);
            if(mapaEscalaMetasProducao.get(tipoItemDeControle.getIdTipoItemDeControle()) == null){
                mapaEscalaMetasProducao.put(tipoItemDeControle.getIdTipoItemDeControle(), jPanel1);
            }
        }
    }


   public static void desenhaEscalaParaMetas(TipoItemDeControle tipoItemDeControle)
   {
       jPanel1 = new JPanel();
       jPanel1.setLayout(null);
       jPanel1.setOpaque(false);
       jPanel1.setBounds(0,0 ,
               Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.largura.painel.escala.meta")),
               Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.altura.painel.escala.meta")));

       // ... o primeiro passo e descobrir o tamanho da fonte que iremos usar para escrever os numeros...
       Double proximoNumeroRegua = tipoItemDeControle.getInicioEscala();

       while (proximoNumeroRegua <= tipoItemDeControle.getFimEscala())
       {
          String textoRegua = proximoNumeroRegua.toString();

          Integer nrPixelEscala = lblVariacoesBlend.getWidth();

          int nrPixelPorParte = (int) ((nrPixelEscala * (tipoItemDeControle.getMultiplicidadeEscala())) / (tipoItemDeControle.getFimEscala() - tipoItemDeControle.getInicioEscala()));

          nrPixelsDoMaiorTextoDaRegua = DSSStockyardFuncoesTexto.calcularQuantidadePixelTexto(textoRegua, lblVariacoesBlend.getFont());

          //adaptando tamanho da fonte somado de dois pixels em branco ate caber no espaco reservado para aquele item na regua
          while(nrPixelsDoMaiorTextoDaRegua+2>nrPixelPorParte)
          {
             lblVariacoesBlend.setFont(new java.awt.Font("Tahoma", 0, lblVariacoesBlend.getFont().getSize()-1));

             nrPixelsDoMaiorTextoDaRegua = DSSStockyardFuncoesTexto.calcularQuantidadePixelTexto(textoRegua, lblVariacoesBlend.getFont());
          }

          proximoNumeroRegua += tipoItemDeControle.getMultiplicidadeEscala();
       }

       //... depois preenchemos os tracos e numeros
       proximoNumeroRegua = tipoItemDeControle.getInicioEscala();


       while (proximoNumeroRegua <= tipoItemDeControle.getFimEscala())
       {
          /*
           * Primeiro pinta os numeros...
           */
          JLabel numero =  new JLabel();
          numero.setText(String.valueOf(proximoNumeroRegua));
          numero.setFont(lblVariacoesBlend.getFont()); //fonte padrao
          jPanel1.add(numero);
          numero.setBounds(0, 0, 380, 20);
          numero.setVisible(Boolean.TRUE);
          numero.setBounds(calculaDimensaoTextoRegua(lblVariacoesBlend, new Double(proximoNumeroRegua), numero, tipoItemDeControle));

          /*
           *  ...depois pinta os tracos ...
           */
          JLabel traco =  new JLabel();
          traco.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/traco.png"));
          jPanel1.add(traco);
          traco.setBounds(180, 15, 2, 30);
          traco.setVisible(Boolean.TRUE);
          traco.setToolTipText(DSSStockyardFuncoesNumeros.getQtdeFormatada(new Double(proximoNumeroRegua),2));
          traco.setBounds(calculaDimensaoBlend(lblPontosEscala, new Double(proximoNumeroRegua), traco, tipoItemDeControle));

          /*
           * ... passa para o proximo numero da regua.
           */
          proximoNumeroRegua += tipoItemDeControle.getMultiplicidadeEscala();
       }
   }

   private static Rectangle calculaDimensaoTextoRegua(JLabel lblEscala, Double valorCalculado, JLabel lblItem, TipoItemDeControle tipoItemDeControle) {
       Rectangle dimensaoObjeto = lblItem.getBounds();
       int posicaoX = 0;

       // variavel que corresponde a quantidade de pixel que 1 espaco em branco possui
       int nrPixelEscala = lblEscala.getWidth();

       posicaoX = (int) ((nrPixelEscala * (valorCalculado - tipoItemDeControle.getInicioEscala())) / (tipoItemDeControle.getFimEscala() - tipoItemDeControle.getInicioEscala()));

       int nrPixelsDesteTextoRegua = DSSStockyardFuncoesTexto.calcularQuantidadePixelTexto(lblItem.getText(), lblItem.getFont());

       // deslocando os itens do número de pixels do texto para o primeiro não colar na parede e centralizando texto do número no traço
       posicaoX = posicaoX + nrPixelsDoMaiorTextoDaRegua/2 - nrPixelsDesteTextoRegua/2;

       dimensaoObjeto.setBounds(posicaoX, (int) dimensaoObjeto.getY(), (int) dimensaoObjeto.getWidth(), (int) dimensaoObjeto.getHeight());

       return dimensaoObjeto;
   }
   
   /**
     * Metodo auxiliar que calcula a posicao dos indicadores de blendagem
     * 
     */
    private static Rectangle calculaDimensaoBlend(JLabel lblEscala, Double valorCalculado, JLabel lblItem, TipoItemDeControle tipoItemDeControle) {
        Rectangle dimensaoObjeto = lblItem.getBounds();
        int posicaoX = 0;

        // variavel que corresponde a quantidade de pixel que 1 espaco em branco possui
        Integer nrPixelEscala = lblEscala.getWidth();
       
        posicaoX = (int) ((nrPixelEscala * (valorCalculado - tipoItemDeControle.getInicioEscala())) / (tipoItemDeControle.getFimEscala() - tipoItemDeControle.getInicioEscala()));

        // deslocando os itens do número de pixels do texto para o primeiro não colar na parede e subtraindo a largura do desenho
        posicaoX = posicaoX + nrPixelsDoMaiorTextoDaRegua/2;

        dimensaoObjeto.setBounds(posicaoX, (int) dimensaoObjeto.getY(), (int) dimensaoObjeto.getWidth(), (int) dimensaoObjeto.getHeight());
        return dimensaoObjeto;
    }

    public static HashMap<Long, JPanel> getMapaEscalaMetas() {
        return mapaEscalaMetas;
    }

    public static HashMap<Long, JPanel> getMapaEscalaMetasEmbarque() {
        return mapaEscalaMetasEmbarque;
    }

    public static HashMap<Long, JPanel> getMapaEscalaMetasProducao() {
        return mapaEscalaMetasProducao;
    }
    

}//end of class
