package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.produto.enums.TipoItemControleEnum;


public class TipoItemControleEnumConversor implements  javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoItemControleEnum result = null;
		for(TipoItemControleEnum tipo : TipoItemControleEnum.values()) {
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
		TipoItemControleEnum tipo = (TipoItemControleEnum) obj;
        return tipo.toString();
	}
}