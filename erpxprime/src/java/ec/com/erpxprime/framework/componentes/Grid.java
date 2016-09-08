/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx 
 */
public class Grid extends HtmlPanelGrid {

    public void setFooter(UIComponent componente) {
        this.getFacets().put("footer", componente);
    }

    public void setHeader(UIComponent componente) {
        this.getFacets().put("header", componente);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public void setMensajeInfo(String titulo) {
        this.setHeader(new Etiqueta("<div class='ui-message-info ui-widget ui-corner-all'>  <span class='ui-message-info'>" + titulo + "</span> </div>"));
    }

    public void setMensajeWarn(String titulo) {
        this.setHeader(new Etiqueta("<div class='ui-message-warn ui-widget ui-corner-all'>  <span class='ui-message-warn'>" + titulo + "</span> </div>"));
    }

    public void setMensajeError(String titulo) {
        this.setHeader(new Etiqueta("<div class='ui-message-error ui-widget ui-corner-all'>  <span class='ui-message-error'>" + titulo + "</span> </div>"));
    }
}
