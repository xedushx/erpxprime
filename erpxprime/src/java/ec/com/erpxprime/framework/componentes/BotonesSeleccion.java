/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.selectonebutton.SelectOneButton;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx 
 */
public class BotonesSeleccion extends SelectOneButton {

    private Conexion conexion;
    private Framework framework = new Framework();


    public void setBotonesSeleccion(List lista) {
        formarCombo(lista);
    }

    public void setBotonesSeleccion(String sql) {
        conexion = framework.getConexion();
        List lista = conexion.consultar(sql);
        formarCombo(lista);
    }

    private void formarCombo(List lista) {
         for (int i = 0; i < lista.size(); i++) {
            ItemOpcion ito_item = new ItemOpcion();
            Object[] fila = (Object[]) lista.get(i);
            ito_item.setItemValue(fila[0]);
            String str_label = "";
            for (int j = 1; j < fila.length; j++) {
                if (j != 1 && !str_label.isEmpty()) {
                    str_label += "       -        ";
                }
                str_label += fila[j] + "";
            }
            ito_item.setItemLabel(str_label);
            this.getChildren().add(ito_item);
        }
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public void setMetodo(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);
        this.addClientBehavior("change", aja_change);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }
}
