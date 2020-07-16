package com.hdntec.gestao.cliente.util.texto;

import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JLabel;

public class DSSStockyardFuncoesTexto {
 
    /**
     * Metodo que formata um codigo colocando zeros a esquerda
     * @param i_size - Quantidade casas do formato
     * @param i_value - Valor a ser formatado
     */
    public static String getCodigoFormatado(long i_size, long i_value) {
        String formato = "";
        for (int i = 0; i < i_size; i++) {
            formato += "0";
        }

        DecimalFormat fmt = new DecimalFormat(formato);
        return fmt.format(i_value);
    }

    /**
     * Metodo que preenche com zeros a esquerda um texto específico
     * de acordo com a quantidade de zeros solicitada
     * @param i_size
     * @param i_value
     * @return
     */
    public static String zerosStr(long i_size, long i_value) {
        String formato = "";
        for (int i = 0; i < i_size; i++) {
            formato += "0";
        }

        DecimalFormat fmt = new DecimalFormat(formato);
        return fmt.format(i_value);
    }

    /**
     * cria uma string com um determinado texto repetidas vezes
     * @param i_size
     * @param i_value
     * @return
     */
    public static String repetirTexto(long nrVezesRepeticao, String textoRepeticao) {
        String textoRepetido = "";
        for (int i = 0; i < nrVezesRepeticao; i++) {
            textoRepetido += textoRepeticao;
        }
        return textoRepetido;
    }

    /**
     * Metodo que remove caracteres especiais de um texto específico
     * @param valor
     * @return
     */
    public static String removeCaracteresEspeciais(String valor) {
        String retorno = "";
        for (int i = 0; i < valor.length(); i++) {
            if (!valor.substring(i,i+1).equals(".") && !valor.substring(i,i+1).equals("-")
                    && !valor.substring(i,i+1).equals("/") && !valor.substring(i,i+1).equals("(")
                    && !valor.substring(i,i+1).equals(")")) {
                retorno += valor.substring(i,i+1);
            }
        }

        return retorno;
    }

    /**
     * Calcula a quantidade de pixel de um determinado texto de acordo com
     * a sua fonte.
     * @param texto
     * @param fonteTexto
     * @return
     */
    public static Integer calcularQuantidadePixelTexto(String texto, Font fonteTexto) {
        
        // cria um label com um espaco em branco para dimensionar o numero 
        // de espacos adicionados entre os itens da escala
        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(fonteTexto);
        
        Integer nrPixelTexto = lblTexto.getFontMetrics(lblTexto.getFont()).stringWidth(lblTexto.getText());
        
        return nrPixelTexto;
    }

     /**
     * Metodo que remove um caracter de um texto especifico + o quebra de linhya "\n"
     * @param valor - (String)O texto que se deseja filtrar
     * @param caracter - (String) O caracter que se deseja remover
     * @return
     */
    public static String removeCaracterEspecificado(String valor, String caracter){
        String retorno = "";
        for (int i = 0; i < valor.length(); i++) {
            if (!valor.substring(i, i + 1).equals(caracter))
            {
                retorno += valor.substring(i, i + 1);
            }
        }
        return  removeBarraN(retorno);
    }

    public static String removeBarraN(String valor)
    {
        return valor.replaceAll("\n", "");
    }

    /**
     * 
     * @param valor
     * @return
     */
    public static String limpaCampoDouble(String valor){
        String novoValor = "";
        if(valor.endsWith(",0")){
            int index = valor.indexOf(",");
            novoValor = valor.substring(0, index);
        }else{
            novoValor = valor;
        }
        return novoValor;
    }

}

