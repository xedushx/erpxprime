/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Check extends HtmlSelectBooleanCheckbox {

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public void setMetodoChange(String metodo) {
        Ajax aja_change = new Ajax();
        if (metodo != null) {
            aja_change.setMetodo(metodo);
        }        
        //  aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoChangeRuta(String metodo) {
        Ajax aja_change = new Ajax();
        if (metodo != null) {
            aja_change.setMetodoRuta(metodo);
        }        
        // aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }
}
