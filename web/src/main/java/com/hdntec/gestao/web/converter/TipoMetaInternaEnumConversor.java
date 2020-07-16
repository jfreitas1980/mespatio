package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;


public class TipoMetaInternaEnumConversor implements
javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoMetaInternaEnum tipoResult = null;
		for (TipoMetaInternaEnum tipo : TipoMetaInternaEnum.getTodos()) {
			if (tipo.toString().trim().equalsIgnoreCase(value.trim())) {
				tipoResult = tipo;
			}
		}
		return tipoResult;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null) {
            return "";
        }
		TipoMetaInternaEnum tipo = (TipoMetaInternaEnum) obj;
        return tipo.toString();
	}
}
