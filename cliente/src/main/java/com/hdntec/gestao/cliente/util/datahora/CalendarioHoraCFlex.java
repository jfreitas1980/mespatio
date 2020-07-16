/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.util.datahora;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos.InterfaceBuscarPlanoOficial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos.InterfacePesquisa;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceEditaNavioNaFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceInserirNavioFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEdicaoDadosBaliza;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEditarCampanha;
import com.hdntec.gestao.util.PropertiesUtil;


/**
 * ************************ COMPONENTE DATA/HORA *********************************
 *
 * Componente customizado de calendario que utiliza a biblioteca do nachoCalendar para o
 * Calendario e uma classe de um componente de selecao de horario.
 * Utilizado em:
 * @see InterfacePesquisa
 * @see InterfaceInserirNavioFilaDeNavios
 * @see InterfaceEditaNavioNaFilaDeNavios
 * @see InterfaceEdicaoDadosBaliza
 * @see InterfaceEditarCampanha
 * @see InterfaceBuscarPlanoOficial
 *
 * @author guilherme
 */
public class CalendarioHoraCFlex extends javax.swing.JPanel implements java.beans.Customizer{

    private Object bean;

    /** Creates new customizer CalendarioHoraCFlex */
    public CalendarioHoraCFlex() {
        initComponents();
        campoData.getFormattedTextField().setEditable(false);
        this.limpaDataHora();
    }

   @Override
    public void setObject(Object bean) {
        this.bean = bean;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        campoHora = new com.hdntec.gestao.util.datahora.DateTimeCflexUtil();
        campoData = new net.sf.nachocalendar.components.DateField();

        campoData.setAllowsInvalid(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(campoData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(campoHora, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(campoHora, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(campoData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private net.sf.nachocalendar.components.DateField campoData;
    private com.hdntec.gestao.util.datahora.DateTimeCflexUtil campoHora;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the dataHora
     */
    public String getDataHora() {
        //Calendar para manipulacao de Hora
        Calendar calendarioHora = Calendar.getInstance();
        calendarioHora.setTime(new Date(getCampoHora().getValue()));

        //Calendar para Manipulacao de Data, onde sera setado o horario.
        Calendar calendarioData = Calendar.getInstance();
        calendarioData.setTime((Date)getCampoData().getValue());
        calendarioData.set(Calendar.HOUR_OF_DAY, calendarioHora.get(Calendar.HOUR_OF_DAY));
        calendarioData.set(Calendar.MINUTE, calendarioHora.get(Calendar.MINUTE));

        SimpleDateFormat sdf = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        
        
        return sdf.format(calendarioData.getTime());
    }

    public Date getDataHoraDate() {
        //Calendar para manipulacao de Hora
        Calendar calendarioHora = Calendar.getInstance();
        calendarioHora.setTime(new Date(getCampoHora().getValue()));

        //Calendar para Manipulacao de Data, onde sera setado o horario.
        Calendar calendarioData = Calendar.getInstance();
        calendarioData.setTime((Date)getCampoData().getValue());
        calendarioData.set(Calendar.HOUR_OF_DAY, calendarioHora.get(Calendar.HOUR_OF_DAY));
        calendarioData.set(Calendar.MINUTE, calendarioHora.get(Calendar.MINUTE));
        calendarioData.set(Calendar.SECOND, 0);
        return calendarioData.getTime();
    }

    /**
     * @param dataHora the dataHora to set
     */
    public void setDataHora(Date dataHora) {
        getCampoData().setValue(dataHora);
        getCampoHora().setDateTime(dataHora.getTime());
    }

    public void limpaDataHora() {
        getCampoData().setBaseDate(new Date());
        
        Calendar hora = Calendar.getInstance();
        hora.set(Calendar.HOUR_OF_DAY, 0);
        hora.set(Calendar.MINUTE, 0);
        getCampoHora().setDateTime(hora.getTimeInMillis());
    }

    /**
     * @return the campoData
     */
    public net.sf.nachocalendar.components.DateField getCampoData() {
        return campoData;
    }

    /**
     * @param campoData the campoData to set
     */
    public void setCampoData(net.sf.nachocalendar.components.DateField campoData) {
        this.campoData = campoData;
    }

    /**
     * @return the campoHora
     */
    public com.hdntec.gestao.util.datahora.DateTimeCflexUtil getCampoHora() {
        return campoHora;
    }

    /**
     * @param campoHora the campoHora to set
     */
    public void setCampoHora(com.hdntec.gestao.util.datahora.DateTimeCflexUtil campoHora) {
        this.campoHora = campoHora;
    }

    

}
