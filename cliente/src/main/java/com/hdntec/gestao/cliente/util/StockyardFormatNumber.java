package com.hdntec.gestao.cliente.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Bruno Gomes
 */
public class StockyardFormatNumber {

      public static String getNumberLenghtFormat(double i_value) throws Exception {
       try {
           NumberFormat formatoNumero = NumberFormat.getNumberInstance(new Locale("pt","BR"));
           return formatoNumero.format(i_value);
       } catch (Exception ex) {
           throw new Exception(ex.getMessage());
       }
   }

}
