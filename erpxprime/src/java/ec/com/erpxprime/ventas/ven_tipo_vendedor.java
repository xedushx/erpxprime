/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package ec.com.erpxprime.ventas;

import ec.com.erpxprime.sistema.sis_pantalla;
import ec.xprime.componentes.pf_barra_boton;
import ec.xprime.componentes.pf_layout;
import ec.xprime.componentes.pf_panel_tabla;
import ec.xprime.componentes.pf_tabla;

/**
 *
 * @author user
 */
public class ven_tipo_vendedor extends sis_pantalla{

    private pf_tabla tab_tabla = new pf_tabla();
    private pf_layout div_division = new pf_layout();

    public ven_tipo_vendedor() {


        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("ven_tipo_vendedor","ide_vgtve" , 1);
        tab_tabla.dibujar();
        pf_panel_tabla pat_panel = new pf_panel_tabla();
        pat_panel.setPanelTabla(tab_tabla);
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);
    }

    public void insertar() {
        tab_tabla.insertar();
    }

    public void guardar() {
        tab_tabla.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public void eliminar() {
        tab_tabla.eliminar();
    }

    public pf_tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(pf_tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public pf_barra_boton getBar_botones() {
        return bar_botones;
    }

    public void setBar_botones(pf_barra_boton bar_botones) {
        this.bar_botones = bar_botones;
    }
}
