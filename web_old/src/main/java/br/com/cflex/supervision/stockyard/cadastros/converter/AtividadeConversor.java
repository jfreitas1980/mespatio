package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.plano.Atividade;
import br.com.cflex.supervision.stockyard.util.AtividadeUtil;

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
