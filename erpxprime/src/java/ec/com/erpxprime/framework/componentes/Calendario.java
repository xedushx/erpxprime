/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.util.Date;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.event.DateSelectEvent;

/**
 *
 * @author xedushx 
 */
public class Calendario extends Calendar {

    private final Framework framework = new Framework();
    private boolean tipoBoton = false;

    public Calendario() {
        this.setAutocomplete("off");
        this.setPattern("dd/MM/yyyy");
        this.setLocale(FacesContext.getCurrentInstance().getViewRoot().getLocale());
        this.setSize(15);
        this.setTitle("dd/MM/yyyy");
        this.setNavigator(true);
        this.setYearRange("c-50:c+10");
    }

    public void limpiar() {
        this.setValue(null);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setTipoCalendarioHora() {
        this.setPattern("MM/dd/yyyy HH:mm:ss");
        this.setSize(25);
        this.setShowButtonPanel(false);
    }

    public void onSelect(DateSelectEvent evt) {
        this.setValue(evt.getDate());
    }

    public String getFecha() {
        if (getValue() != null) {
            return framework.getFormatoFecha(this.getValue());
        } else {
            return null;
        }
    }

    public Date getDate() {
        if (getValue() != null) {
            return framework.getFecha(this.getValue() + "");
        } else {
            return null;
        }
    }

    public void setFechaActual() {
        this.setValue(framework.getFecha(framework.getFechaActual()));
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
