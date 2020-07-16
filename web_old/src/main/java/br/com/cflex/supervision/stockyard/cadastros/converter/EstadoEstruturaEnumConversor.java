package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.planta.EstadoMaquinaEnum

public class EstadoEstruturaEnumConversor implements  javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		EstEstadoMaquinaEnumt = null;
		for(EstadoEstadoMaquinaEnum EstadoMaqEstadoMaquinaEnum
			if (estado.toString().trim().equalsIgnoreCase(value.trim())) {
				result = estado;
			}
		}
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null) {
            return "";
        }
		EstadoMaquinEstadoMaquinaEnumdoMaquinaEnEstadoMaquinaEnumturn estado.toString();
	}
}