package com.hdntec.gestao.batch;

import org.apache.log4j.Logger;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.batch.integracaoCRM.VerificadorIntegracaoCRM;
import com.hdntec.gestao.batch.integracaoMES.VerificadorIntegracaoMES;
import com.hdntec.gestao.batch.integracaoRPUSINAS.VerificadorIntegracaoRPUSINAS;


public class IntegracaoSistemasExternosInicio implements  WrapperListener{

   private static IntegracaoSistemasExternosInicio me = new IntegracaoSistemasExternosInicio();
    
    /** logando informacoes */
   private static Logger logger = Logger.getLogger(IntegracaoSistemasExternosInicio.class);

   /** identifica o sistema que esta sendo usado para integracao */
   private static Integer sistema;

   public void atualizaSistemasExternos(ControladorIntegracaoSistemasExternos controlador)
   {
      //chamar os metodos de configuracao dos sitemas externos

      //sistema externo CRM
      VerificadorIntegracaoCRM verificadorIntegracaoCRM = new VerificadorIntegracaoCRM();
      verificadorIntegracaoCRM.configuraIntegracaoSistemaCRM(controlador);

      //sistema externo MES
      VerificadorIntegracaoMES verificadorIntegracaoMES = new VerificadorIntegracaoMES();
      verificadorIntegracaoMES.configuraIntegracaoSistemaMES(controlador);     
/*
      //sistema externo SAP
      VerificadorIntegracaoSAP verificadorIntegracaoSAP = new VerificadorIntegracaoSAP();
      verificadorIntegracaoSAP.configuraIntegracaoSistemaSAP(controlador);*/

      //sistema externo MES para ritimo de producao das usinas
      VerificadorIntegracaoRPUSINAS verificadorIntegracaoRPUsinas = new VerificadorIntegracaoRPUSINAS();
      verificadorIntegracaoRPUsinas.configuraIntegracaoSistemaRPUsinas(controlador);

   }

   public static void main(String[] args)
   {
      
       System.out.println( "TestWrapper: Initializing..." );
       //me = new IntegracaoSistemasExternosInicio();
       // Start the application.  If the JVM was launched from the native
       //  Wrapper then the application will wait for the native Wrapper to
       //  call the application's start method.  Otherwise the start method
       //  will be called immediately.
        WrapperManager.start( me, args );      
   }

@Override
public void controlEvent(int arg0) {
    // TODO Auto-generated method stub
    
}

@Override
public Integer start(String[] arg0) {
    // TODO Auto-generated method stub
           try
       {
          ControladorIntegracaoSistemasExternos controlador = new ControladorIntegracaoSistemasExternos();
          
          me.atualizaSistemasExternos(controlador);
       }
       catch (NumberFormatException nbEx)
       {
          nbEx.printStackTrace();
          logger.error("Erro ao obter codigo do sistema para integração.");
       }
       catch (Exception ex)
       {
          ex.printStackTrace();
          logger.error("Ocorreu um erro fatal na integração do sistema" + sistema.intValue() + " : " + ex.getMessage());
       }    
       return null; 
}


@Override
public int stop(int arg0) {
    // TODO Auto-generated method stub
    return 0;
}

}
