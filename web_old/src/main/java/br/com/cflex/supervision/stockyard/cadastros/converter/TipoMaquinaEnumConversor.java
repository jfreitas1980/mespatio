package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.planta.TipoMaquinaEnum;

public class TipoMaquinaEnumConversor  implements  javax.faces.convert.Converter {
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoMaquinaEnum result = null;
		for(TipoMaquinaEnum tipo : TipoMaquinaEnum.values()) {
			if (tipo.toString().trim().equalsIgnoreCase(value.trim())) {
				result = tipo;
			}
		}
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null) {
            return "";
        }
		TipoMaquinaEnum tipo = (TipoMaquinaEnum) obj;
        return tipo.toString();
	}
}