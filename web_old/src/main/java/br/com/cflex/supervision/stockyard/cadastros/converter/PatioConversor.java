package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Patio;
import br.com.cflex.supervision.stockyard.util.PatioUtil;

public class PatioConversor implements javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Patio patio = PatioUtil.getPatio(Integer.parseInt(value));
        return patio;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Patio patio = null;
        String result = "";
        if (obj instanceof Patio) {
            patio = (Patio)obj;
            result = patio.hashCode() + "";
        }   
        return result;
	}
}
