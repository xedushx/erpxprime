/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.calendar.Calendar;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Hora extends Calendar {

    private final Framework framework = new Framework();
    private boolean tipoBoton = false;

    public Hora() {
        this.setLabel("Configurar Hora");
        this.setPattern("HH:mm");
        this.setTimeOnly(true);
        this.setSize(13);
        this.setTitle("HH:mm");
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public String getHora() {
        if (getValue() != null) {
            return framework.getFormatoHora(this.getValue());
        } else {
            return null;
        }
    }

    public void setHoraActual() {
        this.setValue(framework.getHoraActual());
    }

    public boolean isTipoBoton() {
        return tipoBoton;
    }

    public void setTipoBoton(boolean tipoBoton) {
        this.tipoBoton = tipoBoton;
        if (tipoBoton) {
            this.setShowOn("button");
        } else {
            this.setShowOn(null);
        }
    }

    public void setPlaceHolder(String placeholder) {
        this.getPassThroughAttributes().put("placeholder", placeholder);
    }
}
