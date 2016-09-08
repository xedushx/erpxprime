/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;

/**
 *
 * @author xedushx 
 */
public class Enlace extends HtmlCommandLink {

    public Enlace() {
      //  setTarget("_blank");
    }

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

    public void agregarImagen(String pathImagen) {
        Imagen ima_imagen = new Imagen();
        ima_imagen.setValue(pathImagen);
        this.getChildren().add(ima_imagen);
    }
}
