package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.navios.Navio;
import br.com.cflex.supervision.stockyard.util.NavioUtil;

public class NavioConversor implements  javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Navio navio = NavioUtil.getNavio(Long.valueOf(value));
        return navio;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		Navio navio = null;
        String result = "";
        if (obj instanceof Navio) {
        	navio = (Navio)obj;
            result = navio.getIdNavio().toString();
        }   
        return result;
	}

}
