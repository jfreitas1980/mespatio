package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class CorTipoProdutoConversor implements  javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		String result = null;
		if (value != null) {
			result = value;
			result = result.replace("rgb(", "").replace(")", "").replace(" ", "");
		}
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		String result = null;
		if (obj != null) {
			String valor = obj.toString();
			String[] rgb = valor.split(",");
			if (rgb != null && rgb.length == 3) {
				result = "rgb("+rgb[0]+", "+rgb[1]+", "+rgb[2]+")";
			}
		}
		return result;
	}

	
}
