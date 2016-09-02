/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.faces.component.UIComponent;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.Tab;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class PanelAcordion extends AccordionPanel {

    public PanelAcordion() {
        this.setMultiple(true);
        this.setRowIndex(0);
        this.setStyle("width:100%;overflow: auto;");
        
    }

    public void agregarPanel(String titulo, UIComponent componente) {
        Tab tab_nueva = new Tab();
        tab_nueva.setTitle(titulo);
        if (componente != null) {
            tab_nueva.getChildren().add(componente);
        }
        this.getChildren().add(tab_nueva);
    }
}
