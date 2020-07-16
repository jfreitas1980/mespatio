package br.com.cflex.supervision.stockyard.cadastros.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.cflex.supervision.stockYard.servidor.modelo.produto.CoeficienteDeDegradacao;
import br.com.cflex.supervision.stockyard.cadastros.controller.TipoItemDeControleController;

public class CoeficienteDeDegradacaoConversor implements javax.faces.convert.Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Map<String,Object> atributosSession = context.getExternalContext().getSessionMap();
        TipoItemDeControleController controller = (TipoItemDeControleController) atributosSession.get("tipoItemDeControleController");
        CoeficienteDeDegradacao coeficienteDeDegradacao = controller.getCoeficienteMap().get(new Long(value));
        return coeficienteDeDegradacao;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		CoeficienteDeDegradacao coeficienteDeDegradacao = null;
        String result = "";
        if (obj instanceof CoeficienteDeDegradacao) {
        	coeficienteDeDegradacao = (CoeficienteDeDegradacao)obj;
            result = String.valueOf(coeficienteDeDegradacao.getIdCoeficiente());
        }
        return result;
	}
}