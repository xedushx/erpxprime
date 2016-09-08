/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.inputtextarea.InputTextarea;

/**
 *
 * @author xedushx 
 */
public class AreaTexto extends InputTextarea {

    public AreaTexto() {
    this.setAutoResize(true);
    }
    
    

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setMetodoChange(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);

        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoChangeRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);        
        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoKeyPress(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);        
        aja_change.setGlobal(false);
        this.addClientBehavior("keyup", aja_change);
    }

    public void setMetodoKeyPressRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);        
        aja_change.setGlobal(false);
        this.addClientBehavior("keyup", aja_change);
    }

    public void limpiar() {
        this.setValue(null);
    }
}
