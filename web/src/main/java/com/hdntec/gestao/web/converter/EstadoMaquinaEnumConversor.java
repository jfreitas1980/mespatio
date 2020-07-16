package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


public class EstadoMaquinaEnumConversor implements  javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		EstadoMaquinaEnum result = null;
		for(EstadoMaquinaEnum estado : EstadoMaquinaEnum.values()) {
			if (estado.toString().trim().equalsIgnoreCase(value.trim())) {
				result = estado;
			}
		}
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null) {
            return "";
        }
		EstadoMaquinaEnum estado = (EstadoMaquinaEnum) obj;
        return estado.toString();
	}
}