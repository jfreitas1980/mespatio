package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;


public class TipoMaquinaEnumConversor  implements  javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoMaquinaEnum result = null;
		for(TipoMaquinaEnum tipo : TipoMaquinaEnum.values()) {
			if (tipo.toString().trim().equalsIgnoreCase(value.trim())) {
				result = tipo;
			}
		}
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null) {
            return "";
        }
		TipoMaquinaEnum tipo = (TipoMaquinaEnum) obj;
        return tipo.toString();
	}
}