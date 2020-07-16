package br.com.cflex.supervision.stockyard.cadastros.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.produto.RegraFarol;
import br.com.cflex.supervision.stockyard.cadastros.controller.TipoItemDeControleController;

public class RegraFarolConversor implements javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Map<String, Object> atributosSession = context.getExternalContext().getSessionMap();
        TipoItemDeControleController controller = (TipoItemDeControleController) atributosSession.get("tipoItemDeControleController");
        RegraFarol regraFarol = controller.getRegraFarolMap().get(new Long(value));
        return regraFarol;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		RegraFarol regraFarol = null;
        String result = "";
        if (obj instanceof RegraFarol) {
        	regraFarol = (RegraFarol)obj;
            result = String.valueOf(regraFarol.getIdRegraFarol());
        }
        return result;
	}
}
