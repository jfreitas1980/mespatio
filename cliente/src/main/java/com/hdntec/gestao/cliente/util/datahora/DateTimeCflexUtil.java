/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.cliente.util.datahora;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;

/** Componente de escolha de horario. 
 *
 * @author guilherme
 */
public class DateTimeCflexUtil extends JPanel{

    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    private String sampleProperty;
    private PropertyChangeSupport propertySupport;
    private SpinnerDateModel timeModel;
    private JSpinner timeJS;
    long dataDefault;


    public DateTimeCflexUtil() {
        propertySupport = new PropertyChangeSupport(this);
        this.dataDefault = Calendar.getInstance().getTimeInMillis();
        this.createComponents();
        this.createLayout();
    }

    public String getSampleProperty() {
        return sampleProperty;
    }

    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    private void createComponents() {
        timeModel = new SpinnerDateModel(new Date(dataDefault), null, null, Calendar.MINUTE);
        timeJS = new JSpinner(timeModel);
        JSpinner.DateEditor editorA = new JSpinner.DateEditor(timeJS, "HH:mm");
        editorA.getTextField().setEnabled(false);
        editorA.getTextField().setEditable(false);

    }

    private void createLayout() {
        this.setLayout(null);
        this.setBounds(0, 0, 140, 20);
        timeJS.setBounds(0, 0, (int) (this.getWidth() * 0.65), 20);
        this.add(timeJS);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getBounds().width, getBounds().height);
    }

    public long getValue() {
        Date dateTime = ((SpinnerDateModel) timeJS.getModel()).getDate();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.AM_PM, 0);
        cal2.setTime(dateTime);
        return cal2.getTimeInMillis();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        timeJS.setEnabled(enabled);
    }

    public void setEditable(boolean enabled) {
        timeJS.getEditor().setEnabled(false);
    }

    public void setWidth(int width) {
        this.setSize(width, this.getHeight());
        timeJS.setBounds(0, 0, (int) (this.getWidth() * 0.65), this.getHeight());
    }

    public void setHeight(int height) {
        this.setSize(this.getWidth(), height);
        timeJS.setBounds(0, 0, (int) (this.getWidth() * 0.65), height);
    }

    public void setDateTime(long newDateTime) {
        timeJS.setModel(new SpinnerDateModel(new Date(newDateTime), null, null, Calendar.MINUTE));
        timeJS.setEditor(new JSpinner.DateEditor(timeJS, "HH:mm"));
    }

    public void clear() {
        String[] lista = {"00:00"};
        timeJS.setModel(new SpinnerListModel(lista));
        timeJS.setEditor(new JSpinner.ListEditor(timeJS));
    }

}
