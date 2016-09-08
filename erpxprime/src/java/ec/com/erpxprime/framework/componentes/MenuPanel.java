/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.faces.component.UIComponent;
import org.primefaces.component.submenu.Submenu;

/**
 *
 * @author xedushx
 */
public class MenuPanel extends Division {
    
    private final MenuLista pam_menu = new MenuLista();
    private final Panel pan_panel = new Panel();
    private Submenu sbm_actual;
    private int opcion = -1;
    private boolean efecto = true;
    
    public void setMenuPanel(String tituloSubMenu, String porcentaje) {

        //sbm_actual.setLabel(tituloSubMenu);
        //pam_menu.getChildren().add(sbm_actual);
        pam_menu.setStyle("width:97%");
        pan_panel.setStyle("width:99%;height:99.2%;overflow: auto;display: block;");
        this.dividir2(pam_menu, pan_panel, porcentaje, "V");
        this.getDivision1().setCollapsible(true);
        this.getDivision1().setHeader(tituloSubMenu);
        this.getDivision1().setResizable(true);
    }

    /**
     * Agrega un item al submenu actual
     *
     * @param titulo
     * @param metodo
     * @param icono
     */
    public void agregarItem(String titulo, String metodo, String icono) {
        ItemMenu itm_item = new ItemMenu();
        itm_item.setValue(titulo);
        itm_item.setMetodo(metodo);
        itm_item.setIcon(icono);
        if (sbm_actual != null) {
            sbm_actual.getChildren().add(itm_item);
        } else {
            pam_menu.getChildren().add(itm_item);
        }
    }

    /**
     * Agrega un submenu
     *
     * @param titulo
     */
    public void agregarSubMenu(String titulo) {
        sbm_actual = new Submenu();
        sbm_actual.setLabel(titulo);
        pam_menu.getChildren().add(sbm_actual);
    }

    /**
     * Dibuja un componente y lo asocia a una opcion
     *
     * @param opcion entero 1,2,3,4......
     * @param titulo
     * @param componente
     */
    public void dibujar(int opcion, String titulo, UIComponent componente) {
        if (titulo != null && titulo.isEmpty() == false) {
            pan_panel.setTitle(titulo);
        }
        this.opcion = opcion;
        pan_panel.getChildren().clear();

        //Agrega efecto
        if (efecto) {
            Efecto efe = new Efecto();
            efe.setType("drop");
            efe.setSpeed(150);
            efe.setPropiedad("mode", "'show'");
            efe.setEvent("load");
            pan_panel.getChildren().add(efe);
        }
        
        pan_panel.getChildren().add(componente);
        Framework framework = new Framework();
        framework.addUpdate(pan_panel.getId());
    }

    /**
     * Limpia el Panle de Contenido
     */
    public void limpiar() {
        this.opcion = -1;
        pan_panel.getChildren().clear();
        pan_panel.setTitle(null);
        Framework framework = new Framework();
        framework.addUpdate(pan_panel.getId());
    }
    
    public int getOpcion() {
        return opcion;
    }
    
    public boolean isEfecto() {
        return efecto;
    }
    
    public void setEfecto(boolean efecto) {
        this.efecto = efecto;
    }
    
}
