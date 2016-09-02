/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.inputmask.InputMask;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Mascara extends InputMask {

    public Mascara() {
        this.setAutocomplete("off");
    }
    
    public void setMetodoChange(String metodo, String update) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);
        aja_change.setUpdate(update);
        this.addClientBehavior("change", aja_change);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public void setMetodoChangeRuta(String metodo, String update) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);
        aja_change.setUpdate(update);
        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoKeyPress(String metodo, String update) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);
        aja_change.setUpdate(update);
        aja_change.setGlobal(false);
        this.addClientBehavior("keyup", aja_change);
    }

    public void setMetodoKeyPressRuta(String metodo, String update) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);
        aja_change.setUpdate(update);
        aja_change.setGlobal(false);
        this.addClientBehavior("keyup", aja_change);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void limpiar() {
        this.setValue(null);
    }
}
