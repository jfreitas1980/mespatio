package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.web.util.TipoItemControleUtil;


/**
 * 
 * <P><B>Description :</B><BR>
 * General TipoItemDeControleConversor
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 15/07/2009
 * @version $Revision: 1.1 $
 */
public class TipoItemControleConversor implements  javax.faces.convert.Converter {
	/*
	 * Esse conversor utiliza a classe TipoItemDeControleUtil. Sempre que utilizar esse
	 * conversor em algum componente deve-se garantir que o map estatico de 
	 * tipos de produto do TipoItemDeControleUtil esteja populado.
	 * Exemplos de utilizacao: TipoItemDeControleController, RegraFarolController 
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
        TipoItemDeControle tipo = TipoItemControleUtil.getTipoItem(Long.valueOf(value));
        return tipo;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		
		TipoItemDeControle TipoItemDeControle = null;
        String result = "";
        if (obj instanceof TipoItemDeControle) {
            TipoItemDeControle = (TipoItemDeControle)obj;
            result = TipoItemDeControle.getIdTipoItemDeControle().toString();
        }   
        return result;
	}

}