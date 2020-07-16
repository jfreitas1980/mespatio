package com.hdntec.gestao.batch.integracaoMES;

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
public class VerificadorIntegracaoMES {

    /**
     * construtor padrao
     */
    public VerificadorIntegracaoMES(){
    }

    public void configuraIntegracaoSistemaMES(final ControladorIntegracaoSistemasExternos controlador){
        try {
            // cria uma classe calendário com a data corrente
            Calendar dataIniAtualizacaoMES = Calendar.getInstance();
            //Seta a data inicial para o dia seguinte
            Date dataInicializacaoMES = DSSStockyardTimeUtil.criaDataComString(PropertiesUtil.buscarPropriedade("data.inicio.atualizacao.sistema.mes"), PropertiesUtil.buscarPropriedade("formato.campo.data"));
            dataIniAtualizacaoMES.setTime(dataInicializacaoMES);

              // Seta a hora da inicializacao na data
            String horaInicializacaoMES = PropertiesUtil.buscarPropriedade("hora.inicio.atualizacao.sistema.mes");
            dataIniAtualizacaoMES.set(Calendar.HOUR, Integer.parseInt(horaInicializacaoMES.split(":")[0]));
            dataIniAtualizacaoMES.set(Calendar.MINUTE, Integer.parseInt(horaInicializacaoMES.split(":")[1]));
            dataIniAtualizacaoMES.set(Calendar.SECOND, Integer.parseInt(horaInicializacaoMES.split(":")[2]));

            //instancia Timer
            Timer timerMES = new Timer();

            //Intervalo para executar a classe novamente # 60 segundos = 1 minuto
            Double periodicidadeMinutosMES = Double.parseDouble(PropertiesUtil.buscarPropriedade("periodicidade.atualizacao.sistema.mes"));
            long periodoExecucaoMES = Math.round(60 * periodicidadeMinutosMES * 1000);

            //Agenda a tarefa para atualizacao do MES
            timerMES.schedule(new ExecutaIntegracaoSistemaMES(controlador), dataInicializacaoMES, periodoExecucaoMES);
            
        } catch (ValidacaoCampoException ex) {
            Logger.getLogger(VerificadorIntegracaoMES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
