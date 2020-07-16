package br.com.cflex.supervision.stockyard.cadastros.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoDeProdutoEnum;

public class TipoDeProdutoEnumConversor implements  javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoDeProdutoEnum tipoResult = null;
		for(TipoDeProdutoEnum tipo : TipoDeProdutoEnum.getTodos()) {
			if (tipo.toString().trim().equalsIgnoreCase(value.trim())) {
				tipoResult = tipo;
			}
		}
		return tipoResult;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (obj == null) {
            return "";
        }
        TipoDeProdutoEnum tipo = (TipoDeProdutoEnum) obj;
        return tipo.toString();
	}
}
