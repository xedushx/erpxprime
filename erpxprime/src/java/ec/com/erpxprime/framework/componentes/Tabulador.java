/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.faces.component.UIComponent;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Tabulador extends TabView {

    public Tabulador() {
        this.setStyle("width:99%;height:96%;overflow: auto;display: block;");
        this.setRowIndex(0);
//        this.setDynamic(true);
//        this.setCache(true);
    }

    public void agregarTab(String titulo, UIComponent componente) {
        Tab tab_nueva = new Tab();
        tab_nueva.setTitle(titulo);
        if (componente != null) {
            tab_nueva.getChildren().add(componente);
        }
        this.getChildren().add(tab_nueva);
    }

    /**
     * Retorna una Tab de acuerdo a un indice
     *
     * @param index
     * @return
     */
    public Tab getTab(int index) {
        try {
            return (Tab) this.getChildren().get(index);
        } catch (Exception e) {
        }
        return null;
    }
}
