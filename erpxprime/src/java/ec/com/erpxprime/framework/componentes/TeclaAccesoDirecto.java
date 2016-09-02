/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import org.primefaces.component.hotkey.Hotkey;

/**
 *
 * @author xedushx
 */
public class TeclaAccesoDirecto extends Hotkey {

    private String ruta = "mbe_index.clase";

    public void setTeclas(String teclas) {
        this.setBind(teclas);
    }

    public void setMetodo(String metodo) {
        MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + ruta + "." + metodo + "}", null, new Class<?>[0]);
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
        addActionListener(crearActionListener("" + ruta + "." + metodo));
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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
