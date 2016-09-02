/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.inputtext.InputText;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Texto extends InputText {

    private boolean soloNumeros = false;
    private boolean soloEnteros = false;
    private int delay = 0;

    public Texto() {
        this.setAutocomplete("off");
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public boolean isSoloEnteros() {
        return soloEnteros;
    }

    public void setSoloEnteros() {
        this.soloEnteros = true;
        this.soloNumeros = false;
    }

    public boolean isSoloNumeros() {
        return soloNumeros;
    }

    public void setSoloNumeros() {
        this.soloNumeros = true;
        this.soloEnteros = false;
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
        //  aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoKeyPress(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);
        // aja_change.setGlobal(false);
        this.addClientBehavior("keyup", aja_change);
    }

    public void setMetodoKeyPressRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);
        aja_change.setGlobal(false);
        this.addClientBehavior("keyup", aja_change);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (isSoloNumeros()) {
            ResponseWriter writer = context.getResponseWriter();
            String lstr_nom = this.getClientId();
            lstr_nom = lstr_nom.replace(":", "\\\\:");
            writer.startElement("script", this);
            writer.write("jQuery(function($){ $('#" + lstr_nom + "').numeric();});");
            writer.endElement("script");
        }
        if (isSoloEnteros()) {
            ResponseWriter writer = context.getResponseWriter();
            String lstr_nom = this.getClientId();
            lstr_nom = lstr_nom.replace(":", "\\\\:");
            writer.startElement("script", this);
            writer.write("jQuery(function($){ $('#" + lstr_nom + "').numeric(false, function() { alert('Solo números enteros'); this.value = ''; this.focus(); })});");
            writer.endElement("script");
        }
        super.encodeBegin(context);
    }

    public void setPlaceHolder(String placeholder) {
        this.getPassThroughAttributes().put("placeholder", placeholder);
    }

    public void limpiar() {
        this.setValue(null);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
