package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.web.util.PatioUtil;


public class PatioConversor implements javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Patio patio = PatioUtil.getPatio(Integer.parseInt(value));
        return patio;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Patio patio = null;
        String result = "";
        if (obj instanceof Patio) {
            patio = (Patio)obj;
            result = patio.hashCode() + "";
        }   
        return result;
	}
}
