package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Campanha;
import br.com.cflex.supervision.stockyard.util.CampanhaUtil;

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
