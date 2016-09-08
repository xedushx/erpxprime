/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.util.List;

/**
 *
 * @author xedushx
 */
public class MenuLista extends org.primefaces.component.menu.Menu {
    
    private String metodo;
    private String update;
    
    public void setActionListener(String metodo) {
        this.metodo = "mbe_index.clase.".concat(metodo);
    }
    
    public void setActionListenerRuta(String metodo) {
        this.metodo = metodo;
    }
    
    public void setMenuLista(List lista) {
        formarLista(lista);
    }
    
    public void setMenuLista(String sql) {
        Framework framework = new Framework();
        List lista = framework.getConexion().consultar(sql);
        formarLista(lista);
    }
    
    private void formarLista(List lista) {
        for (Object lista1 : lista) {
            Object[] fila = (Object[]) lista1;
            ItemMenu it = new ItemMenu();
            it.setValue(fila[1]);
            it.setIcon("ui-icon-bullet");
            it.setCodigo(String.valueOf(fila[0]));
            if (update != null) {
                it.setUpdate(update);
            }
            if (metodo != null) {
                it.setActionListenerRuta(metodo);
            }
            this.getChildren().add(it);
        }
    }
    
    public void agregarItemMenu(ItemMenu item) {
        this.getChildren().add(item);
    }
    
    public void setUpdate(String update) {
        this.update = update;
    }
    
}
