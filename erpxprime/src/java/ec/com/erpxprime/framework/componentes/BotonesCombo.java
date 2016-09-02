/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.splitbutton.SplitButton;

/**
 *
 * @author xedushx
 */
public class BotonesCombo extends SplitButton{
    
    public void agregarBoton(ItemMenu boton){
        this.getChildren().add(boton);
    }
    public void agregarSeparacion(){
        Separator separa=new Separator();
        this.getChildren().add(separa);
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

    @Override
    public void setUpdate(String _update) {
        Framework framework = new Framework();
        super.setUpdate(framework.formarUpdate(_update));
    }

     
}
