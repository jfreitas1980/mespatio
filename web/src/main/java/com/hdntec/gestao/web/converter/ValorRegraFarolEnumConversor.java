package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.produto.enums.EnumValorRegraFarol;




public class ValorRegraFarolEnumConversor implements
		javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		EnumValorRegraFarol tipoResult = null;
		for (EnumValorRegraFarol tipo : EnumValorRegraFarol.getTodos()) {
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
		EnumValorRegraFarol tipo = (EnumValorRegraFarol) obj;
        return tipo.toString();
	}

}
