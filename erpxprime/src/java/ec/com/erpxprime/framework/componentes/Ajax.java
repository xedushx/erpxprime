/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;

/**
 *
 * @author xedushx 
 */
public class Ajax extends AjaxBehavior {

    public void setMetodo(String str_metodo) {
        MethodExpression methodExpression;
        methodExpression =
                FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{mbe_index.clase." + str_metodo + "}", Object.class, new Class<?>[0]);
        this.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(methodExpression, methodExpression));
    }

    public void setMetodoRuta(String str_metodo) {
        MethodExpression methodExpression;
        methodExpression =
                FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + str_metodo + "}", Object.class, new Class<?>[0]);
        this.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(methodExpression, methodExpression));
    }

    @Override
    public void setUpdate(String _update) {
        Framework framework = new Framework();
        super.setUpdate(framework.formarUpdate(_update));
    }
}
