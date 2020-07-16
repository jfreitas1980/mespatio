package com.hdntec.gestao.util.datahora;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import util.texto.DSSStockyardFuncoesTexto;

import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * classe com.hdntec.gestao.cliente.util que deve comportar métodos de acesso e manipulação de datas
 * 
 * @author Leonel
 */
public class DSSStockyardTimeUtil
{

   private static final String FORMAT_ATUALIZACAO_TIMESTAMP = "format.atualizacao.timestamp";

   private static final String TEMPO_DIA_VALOR = "tempo.dia.valor";

   private static final String TEMPO_HORA_VALOR = "tempo.hora.valor";

   private static final String TEMPO_MINUTO_VALOR = "tempo.minuto.valor";

   private static final String TEMPO_SEGUNDO_VALOR = "tempo.segundo.valor";

   /** a constante igual a um campo hora formatado em branco */
   public static String HORA_EM_BRANCO = "  :  :  ";

   /** a constante igual a um campo hora formatado em branco */
   public static String DATA_EM_BRANCO = "  /  /    ";

   /** a constante igual a um campo data/hora formatado em branco */
   public static String DATAHORA_EM_BRANCO = "  /  /       :  ";

   public static void validarHora(String horaDigitada, String nomeCampo) throws ValidacaoCampoException
   {

      List<String> paramMensagens = new ArrayList<String>();

      if (horaDigitada.equals(HORA_EM_BRANCO))
      {
         paramMensagens.add(nomeCampo);
         throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
      }

      String[] arrayHora = horaDigitada.split(":");

      long hora = Long.parseLong(arrayHora[0]);
      long minutos = Long.parseLong(arrayHora[1]);
      long segundos = Long.parseLong(arrayHora[2]);

      if (hora > 23 || hora < 0)
      {
         throw new ValidacaoCampoException("Hora digitada inválida. Use intervalo de 00 até 23");
      }

      if (minutos < 0 || minutos > 59)
      {
         throw new ValidacaoCampoException("Minuto digitado inválido. Use intervalo de 00 até 59");
      }

      if (segundos < 0 || segundos > 59)
      {
         throw new ValidacaoCampoException("Segundos digitado inválido. Use intervalo de 00 até 59");
      }
   }

   /**
    * Valida um campo Data/Hora digitado
    * @param dataHoraDigitada
    * @param nomeCampo
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   public static void validarDataHora(String dataHoraDigitada, String nomeCampo) throws ValidacaoCampoException
   {

      List<String> paramMensagens = new ArrayList<String>();
      paramMensagens.add(nomeCampo);

      if (dataHoraDigitada.equals(DATAHORA_EM_BRANCO))
      {
         throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
      }

      String formatoDataHora = PropertiesUtil.buscarPropriedade("formato.campo.datahora");
      SimpleDateFormat fmtDataHora = new SimpleDateFormat(formatoDataHora);

      try
      {
         // converte a data de string para date para checar a formatacao
         Date dataFormatada = fmtDataHora.parse(dataHoraDigitada);

         // verifica se a data convertida é igual a data digitada
         if (!dataHoraDigitada.equals(fmtDataHora.format(dataFormatada)))
         {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
         }
      }
      catch (ParseException parseEx)
      {
         throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
      }
   }

   /**
    * Valida um campo data digitado.
    * @param dataDigitada
    * @param nomeCampo
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   public static void validarData(String dataDigitada, String nomeCampo) throws ValidacaoCampoException
   {

      List<String> paramMensagens = new ArrayList<String>();
      paramMensagens.add(nomeCampo);

      if (dataDigitada.equals(DATAHORA_EM_BRANCO))
      {
         throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
      }

      String formatoDataHora = PropertiesUtil.buscarPropriedade("formato.campo.data");
      SimpleDateFormat fmtDataHora = new SimpleDateFormat(formatoDataHora);

      try
      {
         // converte a data de string para date para checar a formatacao
         Date dataFormatada = fmtDataHora.parse(dataDigitada);

         // verifica se a data convertida é igual a data digitada
         if (!dataDigitada.equals(fmtDataHora.format(dataFormatada)))
         {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
         }
      }
      catch (ParseException parseEx)
      {
         parseEx.printStackTrace();
         throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
      }
   }

   /**
    * cria data com uma string no formato dia mes ano, ddMMyyyy
    *
    * @param dataDDMMYYYY -
    *           data formata com Dia DD mes MM e ano YYYY
    * @param inicioDia -
    *           se true seta o horário como tudo 0, pra inicio de dia, se false seta pra ultima hora do dia
    * @return - retorna a data deseja em java.util.Date
    */
   public static Date criarDataComString(String dataDDMMYYYY, Boolean inicioDia)
   {
      SimpleDateFormat sdf = new SimpleDateFormat();
      sdf.applyPattern(PropertiesUtil.buscarPropriedade(FORMAT_ATUALIZACAO_TIMESTAMP));
      String dia = dataDDMMYYYY.substring(0, 2);
      String mes = dataDDMMYYYY.substring(2, 4);
      String ano = dataDDMMYYYY.substring(4, 8);
      try
      {
         if (inicioDia)
         {
            return sdf.parse(ano + "-" + mes + "-" + dia + " 00:00:00.0");
         }
         else
         {
            return sdf.parse(ano + "-" + mes + "-" + dia + " 23:59:59.9");
         }
      }
      catch (ParseException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Cria uma data a partir de uma string no formato dd-MM-yyyy as HH:mm ou formato dd/MM/yyyy as HH:mm
    * @param dataTexto
    * @return
    */
   public static Date criarDataComString(String dataTexto) throws ValidacaoCampoException
   {
      Date data = null;
      try
      {
         //formato da data parametrizado dd/MM/yyyy HH:mm:ss
         SimpleDateFormat sdf = new SimpleDateFormat();
         sdf.applyPattern(PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
         //trata a string passada por parametro removendo caracteres especiais
         dataTexto = DSSStockyardFuncoesTexto.removeCaracteresEspeciais(dataTexto);
         //...e removendo espacos em branco e quebra de linha caso haja alguma
         dataTexto = DSSStockyardFuncoesTexto.removeCaracterEspecificado(dataTexto, " ");
         //.. remove dois-pontos (":")
         dataTexto = DSSStockyardFuncoesTexto.removeCaracterEspecificado(dataTexto, ":");
         //separando campos
         String dia = dataTexto.substring(0, 2);
         String mes = dataTexto.substring(2, 4);
         String ano = dataTexto.substring(4, 8);
         String hora = dataTexto.substring(10, 12);
         String minuto = dataTexto.substring(12, 14);
         data = sdf.parse(dia + "/" + mes + "/" + ano + " " + hora + ":" + minuto + ":" + "00.0");

      }
      catch (ParseException ex)
      {
         throw new ValidacaoCampoException("exception.converter.texto.para.data");
      }

      return data;
   }

   /**
    * Soma o valor passado como parametro na data recebida como parametro. O campo tipo valor
    * deve ser um tipo valido do Calendar (Calendar.HOUR, CALENDAR.SECOND)
    *
    * @param dataParaSoma
    * @param valor
    * @param tipoValor
    * @return
    * @throws java.lang.Exception
    */
   public static Date somarHoraNaData(Date dataParaSoma, int valor, int tipoValor)
   {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dataParaSoma);

      calendar.add(tipoValor, valor);

      return calendar.getTime();
   }

   /**
    * Soma a hora passada como parametro na data recebida também como parametro.
    * A hora deve estar no formato HH:MM:SS
    * @param dataParaSoma
    * @param horaParaSoma (HH:MM:SS)
    * @return
    */
   public static Date somarHoraNaData(Date dataParaSoma, String horaParaSoma)
   {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dataParaSoma);

      Integer hora = new Integer(horaParaSoma.split(":")[0]);
      Integer minuto = new Integer(horaParaSoma.split(":")[1]);
      Integer segundos = new Integer(horaParaSoma.split(":")[2]);

      calendar.add(Calendar.HOUR, hora);
      calendar.add(Calendar.MINUTE, minuto);
      calendar.add(Calendar.SECOND, segundos);

      return calendar.getTime();
   }

   /**
    * método que retorna o intervalo somado de todos os valores informados
    *
    * @param dias -
    *           numero de dias desejados no intervalo
    * @param horas -
    *           numero de horas desejados no intervalo
    * @param minutos -
    *           numero de minutos desejados no intervalo
    * @param segundos -
    *           numero de segundos desejados no intervalo
    * @return - valor long do total do intervalo
    */
   public static Long obterValorDeIntervalos(Long dias, Long horas, Long minutos, Long segundos)
   {

      Long valorIntervalos = ((dias == null) ? 0 : dias.longValue() * new Long(PropertiesUtil.buscarPropriedade(TEMPO_DIA_VALOR)).longValue()) +
            ((horas == null) ? new Long(0).longValue() : horas.longValue() * new Long(PropertiesUtil.buscarPropriedade(TEMPO_HORA_VALOR)).longValue()) +
            ((minutos == null) ? new Long(0).longValue() : minutos.longValue() * new Long(PropertiesUtil.buscarPropriedade(TEMPO_MINUTO_VALOR)).longValue()) +
            ((segundos == null) ? new Long(0).longValue() : segundos.longValue() * new Long(PropertiesUtil.buscarPropriedade(TEMPO_SEGUNDO_VALOR)).longValue());

      return valorIntervalos;
   }

   /**
    * Metodo que formata uma data de acordo com o parametro de formatacao desejada
    * @param data @link{java.util.Date} - a data a ser formatada
    * @param formatoData - O formato da data desejado
    * @return a data formatada
    */
   public static String formatarData(Date data, String formatoData)
   {
      String dataFormatada = "";
      if (data != null)
      {
         SimpleDateFormat fmt = new SimpleDateFormat(formatoData);
         dataFormatada = fmt.format(data);
      }
      return dataFormatada;
   }

   /**
    * Converte uma data digita no formato String para um formato Date de acordo
    * com o formato passado como parametro.
    * @param data - String da data para conversão
    * @param formatoData - O formato para qual a data será convertida
    * @return a data convertida
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   public static Date criaDataComString(String data, String formatoData) throws ValidacaoCampoException
   {
      try
      {
         SimpleDateFormat fmt = new SimpleDateFormat(formatoData);
         Date dataConvertida = fmt.parse(data);
         return dataConvertida;
      }
      catch (ParseException pex)
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.mensagem.campo.data.invalido"));
      }
   }

   public static Double calculaTempoHorasEntreDatas(Date dataInicial, Date dataFinal)
   {

      Calendar calDataInicial = Calendar.getInstance();
      calDataInicial.setTime(dataInicial);

      Calendar calDataFinal = Calendar.getInstance();
      calDataFinal.setTime(dataFinal);

      Double direfencaHoras = (double) (calDataFinal.getTimeInMillis() - calDataInicial.getTimeInMillis());

      Double diferenca = new Double(direfencaHoras / (1000 * 3600));

      return diferenca;

   }

   /**
    * Calcula a diferenÃ§a de duas datas em minutos
    * <br>
    * <b>Importante:</b> Quando realiza a diferenÃ§a em minutos entre duas datas, este mÃ©todo considera os segundos restantes e os converte em fraÃ§Ã£o de minutos.
    * @param dataInicial
    * @param dataFinal
    * @return quantidade de minutos existentes entre a dataInicial e dataFinal.
    */
   public static double diferencaEmMinutos(Date dataInicial, Date dataFinal)
   {
      double result = 0;
      long diferenca = dataFinal.getTime() - dataInicial.getTime();
      double diferencaEmMinutos = (diferenca / 1000) / 60; //resultado Ã© diferenÃ§a entre as datas em minutos
      result = diferencaEmMinutos; //transforma os segundos restantes em minutos

      return result;
   }

   /**
    * Subtrai a data inicial da data final
    * subtraiDatas
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 20/08/2009
    * @see
    * @param dataFinal
    * @param dataInicial
    * @return
    * @return Returns the Date.
    */
   public static long subtraiDatas(Date dataFinal, Date dataInicial)
   {
      long tempo = 0;
      if (dataFinal != null && dataInicial != null)
      {
         tempo = dataFinal.getTime() - dataInicial.getTime();
      }
      return tempo;
   }

   /**
    *
    * somaDatas
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 21/08/2009
    * @see
    * @param datas
    * @return
    * @return Returns the long.
    */
   public static long somaDatas(List<Long> datas)
   {
      long result = 0;
      for (long tempo : datas)
      {
         result += tempo;
      }
      return result;
   }

   /**
    * Converte um tempo de milesegundos em hora,minuto,segundo
    * converteTempoEmHms
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 21/08/2009
    * @see
    * @param tempo
    * @return
    * @return Returns the long[].
    */
   public static long[] converteTempoEmHms(long tempo)
   {
      //array de hora,minuto,segundo
      long[] result = {0, 0, 0};

      long secs = (int) tempo / 1000L;
      if (tempo > 0)
      {
         // calcula nmero de horas, minutos e segundos
         result[0] = secs / 3600;
         secs = secs % 3600;
         result[1] = secs / 60;
         secs = secs % 60;
         result[2] = secs;

      }
      return result;
   }



   public static Date obterDataHoraUltimaSituacaoParaAtividades(Date dataHoraBaseIncremento)
   {
      // somando 1 segundo na datahora atual
      Calendar calEvento = Calendar.getInstance();
      calEvento.setTimeInMillis(dataHoraBaseIncremento.getTime());
      calEvento.add(Calendar.MILLISECOND, 1);

      return calEvento.getTime();
   }
   
   public static Date obterDataHoraUltimaSituacaoParaAtividadeRecuperacao(Date dataHoraBaseIncremento)
   {
      // somando 1 segundo na datahora atual
	   return obterDataHoraUltimaSituacaoParaAtividades(dataHoraBaseIncremento);
	   /* Calendar calEvento = Calendar.getInstance();
      calEvento.setTimeInMillis(dataHoraBaseIncremento.getTime());
      calEvento.add(Calendar.SECOND, 1);

      return calEvento.getTime();*/
   }
   
   

   public static double diferencaEmHoras(Date dataInicial, Date dataFinal)
   {
      double diferenca = dataFinal.getTime() - dataInicial.getTime();
      double diferencaEmHoras = (((diferenca / 1000) / 60) / 60); //resultado da diferenca entre as datas em horas
      return diferencaEmHoras;
   }

   /**
    * caso a primeira data seja igual a segunda adicina um milisegundo para diferencia-las
    * @param dataNovaSituacao
    * @param dataSituacaoOriginal
    * @return
    */
    public static Date verificaAtualizaDataSituacaoClone(Date dataNovaSituacao, Date dataSituacaoOriginal){
       if(dataNovaSituacao.compareTo(dataSituacaoOriginal) <= 0){
           dataNovaSituacao = obterDataHoraUltimaSituacaoParaAtividades(dataSituacaoOriginal);
       }
       return dataNovaSituacao;
   }

    public static Date verificaAtualizaDataSituacaoCloneRecuperacao(Date dataNovaSituacao, Date dataSituacaoOriginal){
    	if(dataNovaSituacao.compareTo(dataSituacaoOriginal) <= 0){
    		dataNovaSituacao = obterDataHoraUltimaSituacaoParaAtividadeRecuperacao(dataSituacaoOriginal);
    	}
    	return dataNovaSituacao;
    }


}
