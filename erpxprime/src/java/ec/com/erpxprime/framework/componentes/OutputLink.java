/*
 * Copyright (c) 2013, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx 
 */
public class OutputLink extends HtmlOutputLink {
    
    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }
}
