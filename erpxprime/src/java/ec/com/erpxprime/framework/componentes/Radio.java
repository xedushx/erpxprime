/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.util.List;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx 
 */
public class Radio extends HtmlSelectOneRadio {

    public void setVertical(){
      this.setLayout("pageDirection");   
    }
    
    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public void setMetodoChange(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);        
        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoChangeRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);        
        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setRadio(List lista) {
        formarRadio(lista);
    }

    private void formarRadio(List lista) {
        this.getChildren().clear();
        for (Object lista1 : lista) {
            Object[] fila = (Object[]) lista1;
            ItemOpcion ito_item = new ItemOpcion();
            ito_item.setItemValue(fila[0]);
            String str_label = fila[1] + "";
            ito_item.setItemLabel(str_label);
            this.getChildren().add(ito_item);
        }
    }
}
