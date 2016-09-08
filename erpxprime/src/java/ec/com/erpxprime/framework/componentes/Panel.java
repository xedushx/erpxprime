/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx 
 */
public class Panel extends org.primefaces.component.panel.Panel {
    
    public void setFooter(UIComponent componente) {
        this.getFacets().put("footer", componente);
    }
    
    public void setTitle(String title) {
        this.setHeader(title);
    }
    
    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }
    
    public void agregarBoton(String metodo, String icono, String titulo) {
        Link link = new Link();
        link.setStyleClass("ui-panel-titlebar-icon ui-corner-all ui-state-default");
        HtmlOutputText eti = new HtmlOutputText();
        eti.setStyleClass(icono);
        link.getChildren().add(eti);
        link.setTitle(titulo);
        link.setMetodo(metodo);
        this.getFacets().put("actions", link);
        
    }
    
    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }
}
