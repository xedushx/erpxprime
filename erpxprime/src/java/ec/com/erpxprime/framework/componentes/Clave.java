/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.password.Password;

/**
 *
 * @author xedushx 
 */
public class Clave extends Password {

    private Encriptar encriptar;

    public Clave() {
        this.setFeedback(false);
        this.setGoodLabel("Buena");
        this.setWeakLabel("Débil");
        this.setStrongLabel("Segura");
        this.setPromptLabel("Ingrese una clave");
        this.setRedisplay(true);
    }

    public void setMetodoChange(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);        
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoChangeRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);        
        this.addClientBehavior("change", aja_change);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public Encriptar getEncriptar() {
        encriptar = new Encriptar();
        return encriptar;
    }
}
