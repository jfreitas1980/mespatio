package com.hdntec.gestao.cliente.util.datahora;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class DateTimeGUI extends JPanel implements FocusListener {

	private static final long serialVersionUID = 1L;
	private SpinnerDateModel dataModel;
	private SpinnerDateModel timeModel;
	private JSpinner dateJS;
	private JSpinner timeJS;
	private long dataDefault;
	private boolean resetDateInFocusGained = false;

	public DateTimeGUI(long dataInicial) {
		this.dataDefault = dataInicial;
		this.createComponents();
		this.createLayout();
	}

	private void createComponents() {
		dataModel = new SpinnerDateModel(new Date(dataDefault), null, null, Calendar.DAY_OF_WEEK);
		dateJS = new JSpinner(dataModel);
		dateJS.setEditor(new JSpinner.DateEditor(dateJS, "dd/MM/yyyy"));
		timeModel = new SpinnerDateModel(new Date(dataDefault), null, null, Calendar.MINUTE);
		timeJS = new JSpinner(timeModel);
		JSpinner.DateEditor editorA = new JSpinner.DateEditor(timeJS, "HH:mm");
		timeJS.setEditor(editorA);
		// Listener de foco aos componentes de data e hora.
		((JSpinner.DefaultEditor) dateJS.getEditor()).getTextField().addFocusListener(this);
	}

	private void createLayout() {
		this.setLayout(null);
		this.setBounds(0, 0, 165, 20);
		dateJS.setBounds(0, 0, (int) (this.getWidth() * 0.65), 20);
		timeJS.setBounds((int) (this.getWidth() * 0.65) - 1, 0, (int)(this.getWidth() * 0.35) + 1, 20);
		this.add(dateJS);
		this.add(timeJS);
	}

	@Override
    public Dimension getPreferredSize() {
		return new Dimension(getBounds().width, getBounds().height);
	}

	public long getValue() {
		Date dateTime = ((SpinnerDateModel) dateJS.getModel()).getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		dateTime = ((SpinnerDateModel) timeJS.getModel()).getDate();
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.AM_PM, 0);
		cal2.setTime(dateTime);
		cal2.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		cal2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		cal2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		return cal2.getTimeInMillis();
	}

	@Override
    public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		dateJS.setEnabled(enabled);
		timeJS.setEnabled(enabled);
	}

	public void setWidth(int width) {
		this.setSize(width, this.getHeight());
		dateJS.setBounds(0, 0, (int) (this.getWidth() * 0.65), this.getHeight());
		timeJS.setBounds((int) (this.getWidth() * 0.65) - 1, 0, (int)(this.getWidth() * 0.35) + 1, this.getHeight());
	}

	public void setHeight(int height) {
		this.setSize(this.getWidth(), height);
		dateJS.setBounds(0, 0, (int) (this.getWidth() * 0.65), height);
		timeJS.setBounds((int) (this.getWidth() * 0.65) - 1, 0, (int)(this.getWidth() * 0.35) + 1, height);
	}

	public void setDateTime(long newDateTime) {
		dateJS.setModel(new SpinnerDateModel(new Date(newDateTime), null, null,	Calendar.DAY_OF_WEEK));
		dateJS.setEditor(new JSpinner.DateEditor(dateJS, "dd/MM/yyyy"));
		timeJS.setModel(new SpinnerDateModel(new Date(newDateTime), null, null, Calendar.MINUTE));
		timeJS.setEditor(new JSpinner.DateEditor(timeJS, "HH:mm"));
	}

	public void clear() {
		((JSpinner.DefaultEditor) dateJS.getEditor()).getTextField().setText("");
		((JSpinner.DefaultEditor) timeJS.getEditor()).getTextField().setText("");
	}

	public boolean isResetDateInFocusGained() {
		return resetDateInFocusGained;
	}

	public void setResetDateInFocusGained(boolean resetDateInFocusGained) {
		this.resetDateInFocusGained = resetDateInFocusGained;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (resetDateInFocusGained)
			setDateTime(Calendar.getInstance().getTime().getTime());
	}

	@Override
	public void focusLost(FocusEvent e) {
		//System.out.println("Focus Lost");
	}
}
