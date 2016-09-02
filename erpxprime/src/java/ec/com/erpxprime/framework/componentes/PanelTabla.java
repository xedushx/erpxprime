/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class PanelTabla extends Grid implements org.primefaces.component.api.Widget {
    
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
    private MenuContextual menuTabla = new MenuContextual();
    
    public PanelTabla() {
        this.setStyle("width:99%;height:100%;overflow: auto;display: block;");
    }
    
    public void setPanelTabla(Tabla tabla) {
        this.setId("panel_" + tabla.getId());
        this.getChildren().add(tabla);
        Ajax aja_foco_panel = new Ajax();
        aja_foco_panel.setMetodoRuta(tabla.getRuta() + "." + tabla.getId() + ".focus");
        aja_foco_panel.setGlobal(false);
        tabla.setStyle("height:100%;display: block;");
        this.addClientBehavior("mousedown", aja_foco_panel);
        menuTabla.setFor(this.getId());
        menuTabla.crearMenuTabla();
        
        if (tabla.isLectura()) {
            menuTabla.getItem_insertar().setRendered(false);
            menuTabla.getItem_guardar().setRendered(false);
            menuTabla.getItem_eliminar().setRendered(false);
            menuTabla.getItem_importar().setDisabled(true);
        }
        if (tabla.getTabla().isEmpty()) {
            //Tabla generada con sql
            menuTabla.getItem_formato().setDisabled(true);
        }
        this.getChildren().add(menuTabla);
    }
    
    public MenuContextual getMenuTabla() {
        return menuTabla;
    }
    
    public void setMenuTabla(MenuContextual menuTabla) {
        this.menuTabla = menuTabla;
    }
}
