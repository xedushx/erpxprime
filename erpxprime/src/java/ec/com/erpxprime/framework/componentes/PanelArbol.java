/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import org.primefaces.component.contextmenu.ContextMenu;
import org.primefaces.component.separator.Separator;

/**
 *
 * @author xedushx
 */
public class PanelArbol extends HtmlPanelGroup implements org.primefaces.component.api.Widget {

    @Override
    public String resolveWidgetVar() {
        FacesContext context = FacesContext.getCurrentInstance();
        String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null) {
            return userWidgetVar;
        } else {
            return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
        }
    }

    private ContextMenu menuArbol = new ContextMenu();

    public ContextMenu getMenuArbol() {
        return menuArbol;
    }

    public PanelArbol() {
        this.setStyle("overflow: auto;");
    }
    public void setPanelArbol(Arbol arbol) {
        this.setId("panel_" + arbol.getId());
        menuArbol.setId("menuc_" + arbol.getId());
        this.getChildren().add(arbol);
        menuArbol.setFor(this.getId());

        ItemMenu item_mover = new ItemMenu();
        ItemMenu item_aceptar = new ItemMenu();
        ItemMenu item_cancelar = new ItemMenu();

        item_mover.setValue("Mover");
        item_mover.setIcon("ui-icon-arrowthick-2-n-s");
        item_mover.setMetodoRuta("mbe_index.clase." + arbol.getId() + ".seleccionarMover");
        item_mover.setValueExpression("disabled", "mbe_index.clase." + arbol.getId() + ".mover and "+"mbe_index.clase." + arbol.getId() + ".nodoSeleccionado != null");
        menuArbol.getChildren().add(item_mover);

        item_aceptar.setValue("Aceptar");
        item_aceptar.setIcon("ui-icon-circle-check");
        item_aceptar.setMetodoRuta("mbe_index.clase." + arbol.getId() + ".aceptarMoverNodo");
        item_aceptar.setValueExpression("disabled", "mbe_index.clase." + arbol.getId() + ".mover == false");
        menuArbol.getChildren().add(item_aceptar);

        item_cancelar.setValue("Cancelar");
        item_cancelar.setIcon("ui-icon-circle-close");
        item_cancelar.setMetodoRuta("mbe_index.clase." + arbol.getId() + ".cancelarMoverNodo");
        item_cancelar.setValueExpression("disabled", "mbe_index.clase." + arbol.getId() + ".mover == false");
        menuArbol.getChildren().add(item_cancelar);

        Separator separar = new Separator();
        menuArbol.getChildren().add(separar);

        ItemMenu item_actualizar = new ItemMenu();
        item_actualizar.setValue("Actualizar");
        item_actualizar.setIcon("ui-icon-refresh");
        item_actualizar.setMetodoRuta("mbe_index.clase." + arbol.getId() + ".actualizar");
    
        menuArbol.getChildren().add(item_actualizar);

        this.getChildren().add(menuArbol);

    }

    public void setMenuArbol(MenuContextual menuArbol) {
        this.menuArbol = menuArbol;
    }

}
