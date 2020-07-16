package com.hdntec.gestao.web.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.web.util.SessionUtil;


public class DataConversor implements  javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Date data = new Date();
		String formato = SessionUtil.getBundle().getString("dateFormat");
		SimpleDateFormat format = new SimpleDateFormat(formato);  
		try {
			data = new Date(format.parse(value).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return data;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		String data = "";
		String formato = SessionUtil.getBundle().getString("dateFormat");
		SimpleDateFormat format = new SimpleDateFormat(formato);
		if (obj instanceof Date) {
			data = format.format(obj);
		}
		return data;
	}
	

}
