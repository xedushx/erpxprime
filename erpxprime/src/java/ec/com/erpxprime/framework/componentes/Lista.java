/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.selectonelistbox.SelectOneListbox;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Lista extends SelectOneListbox {

    private Conexion conexion;
    private Framework framework = new Framework();
    private List lista;

    public void SetLista(List lista) {
        formarLista(lista);
    }

    public void SetLista(String sql) {
        conexion = framework.getConexion();
        lista = conexion.consultar(sql);
        formarLista(lista);
    }

    private void formarLista(List lista) {
        this.lista = lista;
        this.getChildren().clear();
        for (int i = 0; i < lista.size(); i++) {
            ItemOpcion ito_item = new ItemOpcion();
            Object[] fila = (Object[]) lista.get(i);
            ito_item.setItemValue(fila[0]);
            String str_label = "";
            for (int j = 1; j < fila.length; j++) {
                if (j != 1) {
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

    public void setMetodoChange(String metodo, String update) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);
        aja_change.setUpdate(update);
        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    public void setMetodoChangeRuta(String metodo, String update) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);
        aja_change.setUpdate(update);
        aja_change.setGlobal(false);
        this.addClientBehavior("change", aja_change);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public List getLista() {
        return lista;
    }
    
}
