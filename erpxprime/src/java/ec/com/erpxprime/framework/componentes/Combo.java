/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Combo extends SelectOneMenu {

    private Conexion conexion;
    private int totalFilas = 0;

    public Combo() {
        ItemOpcion ito_vacio = new ItemOpcion();
        ito_vacio.setItemValue(null);
        ito_vacio.setItemLabel("");
        this.getChildren().add(ito_vacio);
    }

    public void setCombo(List lista) {
        formarCombo(lista);
    }

    public void setCombo(String sql) {
        if (conexion == null) {
            Framework framework = new Framework();
            conexion = framework.getConexion();
        }
        List lista = conexion.consultar(sql);
        formarCombo(lista);
    }

    public void limpiar() {
        this.setValue(null);
        Framework framework = new Framework();
        framework.addUpdate(this.getId());

    }

    private void formarCombo(List lista) {
        totalFilas = lista.size();
        this.getChildren().clear();
        ItemOpcion ito_vacio = new ItemOpcion();
        ito_vacio.setItemValue(null);
        ito_vacio.setItemLabel("");
        this.getChildren().add(ito_vacio);
        for (Object lista1 : lista) {
            ItemOpcion ito_item = new ItemOpcion();
            Object[] fila = (Object[]) lista1;
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
        if (lista.size() > 10) {
            this.setFilter(true);
            this.setFilterMatchMode("contains");
        }
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public void eliminarVacio() {
        try {
            if (totalFilas > 0) {
                this.getChildren().remove(0);
                setValue(((ItemOpcion) getChildren().get(0)).getItemValue());
            }
        } catch (Exception e) {
        }
    }

//    @Override
//    public void setValue(Object value) {
//        if (totalFilas == 0) {
//            value = null;
//        }
//        super.setValue(value); //To change body of generated methods, choose Tools | Templates. 
//    }
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

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    public int getTotalFilas() {
        return totalFilas;
    }

    public void setTotalFilas(int totalFilas) {
        this.totalFilas = totalFilas;
    }

}
