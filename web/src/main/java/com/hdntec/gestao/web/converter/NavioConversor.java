package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.web.util.NavioUtil;


public class NavioConversor implements  javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Navio navio = NavioUtil.getNavio(Long.valueOf(value));
        return navio;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Navio navio = null;
        String result = "";
        if (obj instanceof Navio) {
        	navio = (Navio)obj;
            result = navio.getIdNavio().toString();
        }   
        return result;
	}

}
