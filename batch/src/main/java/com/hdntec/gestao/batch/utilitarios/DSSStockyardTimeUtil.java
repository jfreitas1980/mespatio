package com.hdntec.gestao.batch.utilitarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.batch.utilitarios.arquivo.PropertiesUtil;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;



/**
 * classe util que deve comportar métodos de acesso e manipulação de datas
 * 
 * @author Leonel
 */
public class DSSStockyardTimeUtil {

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
    public static String DATAHORA_EM_BRANCO = "  /  /       :  :  ";

    public static void validarHora(String horaDigitada, String nomeCampo) throws ValidacaoCampoException {

        List<String> paramMensagens = new ArrayList<String>();

        if (horaDigitada.equals(HORA_EM_BRANCO)) {
            paramMensagens.add(nomeCampo);
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }

        String[] arrayHora = horaDigitada.split(":");

        long hora = Long.parseLong(arrayHora[0]);
        long minutos = Long.parseLong(arrayHora[1]);
        long segundos = Long.parseLong(arrayHora[2]);

        if (hora > 23 || hora < 0) {
            throw new ValidacaoCampoException("Hora digitada inválida. Use intervalo de 00 até 23");
        }

        if (minutos < 0 || minutos > 59) {
            throw new ValidacaoCampoException("Minuto digitado inválido. Use intervalo de 00 até 59");
        }

        if (segundos < 0 || segundos > 59) {
            throw new ValidacaoCampoException("Segundos digitado inválido. Use intervalo de 00 até 59");
        }
    }

    /**
     * Valida um campo Data/Hora digitado
     * @param dataHoraDigitada
     * @param nomeCampo
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public static void validarDataHora(String dataHoraDigitada, String nomeCampo) throws ValidacaoCampoException {

        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add(nomeCampo);

        if (dataHoraDigitada.equals(DATAHORA_EM_BRANCO)) {
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }

        String formatoDataHora = PropertiesUtil.buscarPropriedade("formato.campo.datahora");
        SimpleDateFormat fmtDataHora = new SimpleDateFormat(formatoDataHora);

        try {
            // converte a data de string para date para checar a formatacao
            Date dataFormatada = fmtDataHora.parse(dataHoraDigitada);

            // verifica se a data convertida é igual a data digitada
            if (!dataHoraDigitada.equals(fmtDataHora.format(dataFormatada))) {
                throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
            }
        } catch (ParseException parseEx) {
            throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
        }
    }

    /**
     * Valida um campo data digitado.
     * @param dataDigitada
     * @param nomeCampo
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public static void validarData(String dataDigitada, String nomeCampo) throws ValidacaoCampoException {

        List<String> paramMensagens = new ArrayList<String>();
        paramMensagens.add(nomeCampo);

        if (dataDigitada.equals(DATAHORA_EM_BRANCO)) {
            throw new ValidacaoCampoException("exception.validacao.campo.vazio", paramMensagens);
        }

        String formatoDataHora = PropertiesUtil.buscarPropriedade("formato.campo.data");
        SimpleDateFormat fmtDataHora = new SimpleDateFormat(formatoDataHora);

        try {
            // converte a data de string para date para checar a formatacao
            Date dataFormatada = fmtDataHora.parse(dataDigitada);

            // verifica se a data convertida é igual a data digitada
            if (!dataDigitada.equals(fmtDataHora.format(dataFormatada))) {
                throw new ValidacaoCampoException("exception.validacao.campo.data.invalida", paramMensagens);
            }
        } catch (ParseException parseEx) {
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
    public static Date criarDataComString(String dataDDMMYYYY, Boolean inicioDia) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(PropertiesUtil.buscarPropriedade(FORMAT_ATUALIZACAO_TIMESTAMP));
        String dia = dataDDMMYYYY.substring(0, 2);
        String mes = dataDDMMYYYY.substring(2, 4);
        String ano = dataDDMMYYYY.substring(4, 8);
        try {
            if (inicioDia) {
                return sdf.parse(ano + "-" + mes + "-" + dia + " 00:00:00.0");
            } else {
                return sdf.parse(ano + "-" + mes + "-" + dia + " 23:59:59.9");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
    public static Date somarHoraNaData(Date dataParaSoma, int valor, int tipoValor) {
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
    public static Date somarHoraNaData(Date dataParaSoma, String horaParaSoma) {
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
    public static Long obterValorDeIntervalos(Long dias, Long horas, Long minutos, Long segundos) {

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
    public static String formatarData(Date data, String formatoData) {
        SimpleDateFormat fmt = new SimpleDateFormat(formatoData);
        String dataFormatada = fmt.format(data);
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
    public static Date criaDataComString(String data, String formatoData) throws ValidacaoCampoException {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(formatoData);
            Date dataConvertida = fmt.parse(data);
            return dataConvertida;
        } catch (ParseException pex) {
            throw new ValidacaoCampoException("Campo data inválido.");
        }
    }

    public static Double calculaTempoHorasEntreDatas(Date dataInicial, Date dataFinal) {

        Calendar calDataInicial = Calendar.getInstance();
        calDataInicial.setTime(dataInicial);

        Calendar calDataFinal = Calendar.getInstance();
        calDataFinal.setTime(dataFinal);

        Double direfencaHoras = (double)(calDataFinal.getTimeInMillis() - calDataInicial.getTimeInMillis());

        Double diferenca = new Double(direfencaHoras / (1000*3600));

        return diferenca;

    }
}
