package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.web.util.CampanhaUtil;


public class CampanhaConversor implements javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Campanha campanha = CampanhaUtil.getCampanha(Long.valueOf(value));
        return campanha;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Campanha campanha = null;
        String result = "";
        if (obj instanceof Campanha) {
            campanha = (Campanha)obj;
            result = campanha.getIdCampanha().toString();
        }   
        return result;
	}
}
