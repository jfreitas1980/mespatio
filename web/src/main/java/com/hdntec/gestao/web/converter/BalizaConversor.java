package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.web.util.BalizaUtil;


public class BalizaConversor implements  javax.faces.convert.Converter {
	/*
	 * Esse conversor utiliza a classe BalizaUtil. Sempre que utilizar esse
	 * conversor em algum componente deve-se garantir que o map estatico de 
	 * balizas do BalizaUtil esteja populado.
	 * Exemplos de utilizacao: TipoProdutoController, RegraFarolController 
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
        Baliza baliza = BalizaUtil.getBaliza(value);
        return baliza;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		
		Baliza baliza = null;
        String result = "";
        if (obj instanceof Baliza) {
            baliza = (Baliza)obj;
            result = baliza.getNomeBaliza();
        }   
        return result;
	}
}