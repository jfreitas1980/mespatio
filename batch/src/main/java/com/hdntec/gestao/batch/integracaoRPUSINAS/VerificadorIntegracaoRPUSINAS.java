package com.hdntec.gestao.batch.integracaoRPUSINAS;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.batch.utilitarios.DSSStockyardTimeUtil;
import com.hdntec.gestao.batch.utilitarios.arquivo.PropertiesUtil;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;


/**
 *
 * @author Bruno Gomes
 */
public class VerificadorIntegracaoRPUSINAS {

    /**
     * construtor padrao
     */
    public VerificadorIntegracaoRPUSINAS(){
    }

    public void configuraIntegracaoSistemaRPUsinas(final ControladorIntegracaoSistemasExternos controlador){
        try {
            // cria uma classe calendário com a data corrente
            Calendar dataIniAtualizacaoRPUSINAS = Calendar.getInstance();
            //Seta a data inicial para o dia seguinte
            Date dataInicializacaoRPUSINAS = DSSStockyardTimeUtil.criaDataComString(PropertiesUtil.buscarPropriedade("data.inicio.atualizacao.sistema.rpusinas"), PropertiesUtil.buscarPropriedade("formato.campo.data"));
            dataIniAtualizacaoRPUSINAS.setTime(dataInicializacaoRPUSINAS);

            // Seta a hora da inicializacao na data
            String horaInicializacaoRPSUINAS = PropertiesUtil.buscarPropriedade("hora.inicio.atualizacao.sistema.rpusinas");
            dataIniAtualizacaoRPUSINAS.set(Calendar.HOUR, Integer.parseInt(horaInicializacaoRPSUINAS.split(":")[0]));
            dataIniAtualizacaoRPUSINAS.set(Calendar.MINUTE, Integer.parseInt(horaInicializacaoRPSUINAS.split(":")[1]));
            dataIniAtualizacaoRPUSINAS.set(Calendar.SECOND, Integer.parseInt(horaInicializacaoRPSUINAS.split(":")[2]));

            //instancia Timer
            Timer timerRPUSINAS = new Timer();

            //Intervalo para executar a classe novamente # 3600 segundo = 1 hora
            Double periodicidadeHorasRPUSINAS = Double.parseDouble(PropertiesUtil.buscarPropriedade("periodicidade.atualizacao.sistema.rpusinas"));
            long periodoExecucaoRPUSINAS = Math.round(3600 * periodicidadeHorasRPUSINAS * 1000);

            //Agenda a tarefa para atualizacao do MES
            timerRPUSINAS.schedule(new ExecutaIntegracaoSistemaRPUSINAS(controlador), dataInicializacaoRPUSINAS, periodoExecucaoRPUSINAS);
            
        } catch (ValidacaoCampoException ex) {
            Logger.getLogger(VerificadorIntegracaoRPUSINAS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
