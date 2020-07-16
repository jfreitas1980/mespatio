package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.web.util.AtividadeUtil;


public class AtividadeConversor implements javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Atividade atividade = AtividadeUtil.getAtividade(Long.valueOf(value));
        return atividade;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Atividade atividade = null;
        String result = "";
        if (obj instanceof Atividade) {
            atividade = (Atividade)obj;
            result = atividade.getId().toString();
        }   
        return result;
	}
}
