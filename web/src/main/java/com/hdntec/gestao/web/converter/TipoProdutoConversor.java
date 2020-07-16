package com.hdntec.gestao.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.web.util.TipoProdutoUtil;


/**
 * 
 * <P><B>Description :</B><BR>
 * General TipoProdutoConversor
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 15/07/2009
 * @version $Revision: 1.1 $
 */
public class TipoProdutoConversor implements  javax.faces.convert.Converter {
	/*
	 * Esse conversor utiliza a classe TipoProdutoUtil. Sempre que utilizar esse
	 * conversor em algum componente deve-se garantir que o map estatico de 
	 * tipos de produto do TipoProdutoUtil esteja populado.
	 * Exemplos de utilizacao: TipoProdutoController, RegraFarolController 
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
        TipoProduto tipo = TipoProdutoUtil.getTipoProduto(Long.valueOf(value));
        return tipo;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		
		TipoProduto tipoProduto = null;
        String result = "";
        if (obj instanceof TipoProduto) {
            tipoProduto = (TipoProduto)obj;
            result = tipoProduto.getIdTipoProduto().toString();
        }   
        return result;
	}

}