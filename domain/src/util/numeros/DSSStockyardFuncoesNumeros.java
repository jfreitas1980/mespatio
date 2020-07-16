package util.numeros;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.hdntec.gestao.exceptions.ValidacaoCampoException;

import util.texto.DSSStockyardFuncoesTexto;

public class DSSStockyardFuncoesNumeros {
 
    /**
     * Método que converte uma string numerica em um objeto do tipo Long. Caso a string
     * não puder ser convertida uma excessão será levantada.
     *
     * @param i_value - String a ser convertida
     * @return Long
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public static Long getStringToLong(String i_value) throws ValidacaoCampoException {
        Long lRetorno = new Long(0);
        try {
            lRetorno = Long.parseLong(i_value);
            return lRetorno;
        } catch (NumberFormatException nex) {
            throw new ValidacaoCampoException("Campo Numérico. Informe somente números.");
        }
    }
    
    /**
     * Metodo que formata um double para uma string para um formato valor
     * @param i_value
     * @param nrCasasDecimais
     * @return
     */
    public static String getValorFormatado(Double i_value, Integer nrCasasDecimais) {
        if (i_value == null) return "-";
        
        String formato = "0." + DSSStockyardFuncoesTexto.zerosStr(nrCasasDecimais, 0);
        DecimalFormat fmt = new DecimalFormat(formato);
        return fmt.format(arredondaValor(i_value, nrCasasDecimais));
    }

    /**
     * Metodo que formata um double para uma string para um formato valor, com 2 casas decimais e separador de milhar
     * @param i_value
     * @return
     */
    public static String getValorFormatado2d(Double i_value) throws NumberFormatException{
        String formato = "#,##0.00";
        DecimalFormat fmt = new DecimalFormat(formato);
        return fmt.format(arredondaValor(i_value, 2));
    }
    
    /**
     * Metodo que formata um campo double e retorna uma string do mesmo
     * @param i_value
     * @param nrCasaDecimais
     * @return
     */
    public static String getQtdeFormatada(Double i_value, Integer nrCasaDecimais) {
        String formato = "0." + DSSStockyardFuncoesTexto.zerosStr(nrCasaDecimais, 0);
        DecimalFormat fmt = new DecimalFormat(formato);
        return fmt.format(arredondaValor(i_value, nrCasaDecimais));
    }
    
    /**
     * Metodo que arredonda um valor usando o numero de casas decimais especificada
     * no parametros.
     * @param i_valor
     * @param nrCasasDecimais
     * @return Double
     */
    public static Double arredondaValor(Double i_valor, Integer nrCasasDecimais) {
        String formato = "1" + DSSStockyardFuncoesTexto.zerosStr(nrCasasDecimais, 0);
        Double valor = i_valor * Double.parseDouble(formato);
        return (valor/Long.parseLong(formato));
    }
    
    /**
     * Metodo que transforma um valor Double para uma formato de valor utilizando
     * o numero de casas decimais.
     * @param valor
     * @param nrCasasDecimais
     * @return Double
     */
    public static Double arredondarDoubleParaCimaAPartirDeNumCasasDecimais(Double valor, Integer nrCasasDecimais) {
         Double moeda = new BigDecimal(valor).setScale(nrCasasDecimais,BigDecimal.ROUND_HALF_UP).doubleValue();
         return moeda;
     }    
    
    
    /**
     * Metodo que transforma uma variavel string em uma variavel do tipo Double
     * @param value
     * @return Double
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public static Double getStringToDouble(String value) throws ValidacaoCampoException {
        Double lRetorno = new Double(0);
        try {
            lRetorno = Double.parseDouble(value.replace(',','.'));
            return lRetorno;
        } catch (NumberFormatException nex) {
            throw new ValidacaoCampoException("Campo do tipo valor com informação incorreto.");
        }
    }
}

