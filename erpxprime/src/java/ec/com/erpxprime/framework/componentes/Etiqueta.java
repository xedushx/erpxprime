/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Etiqueta extends HtmlOutputText {

    public Etiqueta() {
        this.setStyle("");
        this.setEscape(false);
    }

    public Etiqueta(String valor) {
        this.setStyle("");
        this.setEscape(false);
        this.setValue(valor);
    }

    public void setEstiloCabecera(String estilo) {
        this.setEstilo("width:100%;" + estilo);
        this.setStyleClass("ui-widget-header ui-corner-all");
    }

    public void setEstiloContenido(String estilo) {
        this.setEstilo("width:100%;" + estilo);
        this.setStyleClass("ui-widget-content");
    }

    private void setEstilo(String style) {
        super.setStyle(style);
    }

    @Override
    public void setStyle(String style) {
        super.setStyle("border:none;background: none;" + style);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }
}
