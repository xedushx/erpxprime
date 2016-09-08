/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import org.primefaces.component.commandlink.CommandLink;

/**
 *
 * @author xedushx 
 */
public class Link extends CommandLink {

    private String codigo = null;

    public void setMetodo(String metodo) {
        MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{mbe_index.clase." + metodo + "}", null, new Class<?>[0]);
        this.setActionExpression(methodExpression);
    }

    public void setMetodoRuta(String metodo) {
        MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + metodo + "}", null, new Class<?>[0]);
        this.setActionExpression(methodExpression);
    }

    @Override
    public void setUpdate(String _update) {
        Framework framework = new Framework();
        super.setUpdate(framework.formarUpdate(_update));
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setActionListener(String metodo) {
        addActionListener(crearActionListener("mbe_index.clase." + metodo));
    }

    public void setActionListenerRuta(String metodo) {
        addActionListener(crearActionListener(metodo));
    }

    private static MethodExpressionActionListener crearActionListener(String actionListenerExpression) {
        FacesContext context = FacesContext.getCurrentInstance();
        return new MethodExpressionActionListener(context.getApplication().getExpressionFactory().createMethodExpression(context.getELContext(), "#{" + actionListenerExpression + "}", null, new Class[]{
            ActionEvent.class
        }));
    }

    public void setIcono(String styleClass) {
        HtmlOutputText txtIcono = new HtmlOutputText();
        txtIcono.setStyleClass(styleClass);
        this.getChildren().add(txtIcono);
    }

    public void agregarImagen(String pathImagen, String alto, String ancho) {
        Imagen ima_imagen = new Imagen();
        if (alto != null && !alto.isEmpty()) {
            ima_imagen.setHeight(alto);
        }
        if (ancho != null && !ancho.isEmpty()) {
            ima_imagen.setWidth(ancho);
        }

        ima_imagen.setValue(pathImagen);
        this.getChildren().add(ima_imagen);
    }

    public void setLinkUpdate(String _update) {
        super.setUpdate(_update);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
