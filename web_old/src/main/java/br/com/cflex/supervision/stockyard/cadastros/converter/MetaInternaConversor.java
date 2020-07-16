package br.com.cflex.supervision.stockyard.cadastros.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.produto.MetaInterna;
import br.com.cflex.supervision.stockyard.cadastros.controller.TipoItemDeControleController;

public class MetaInternaConversor implements javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Map<String, Object> atributosSession = context.getExternalContext().getSessionMap();
        TipoItemDeControleController controller = (TipoItemDeControleController) atributosSession.get("tipoItemDeControleController");
        MetaInterna metaInterna = controller.getMetaInternaMap().get(new Long(value));
        return metaInterna;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		MetaInterna metaInterna = null;
        String result = "";
        if (obj instanceof MetaInterna) {
            metaInterna = (MetaInterna)obj;
            result = String.valueOf(metaInterna.getIdMetaInterna());
        }
        return result;
	}

}
