/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;

/**
 *
 * @author xEdushx
 */
public class sis_empresa extends Pantalla{

    private Tabla tab_tabla = new Tabla();

    public sis_empresa() {
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setTabla("tbl_empresa", "id_empresa", 1);
        tab_tabla.getColumna("nombre").setNombreVisual("EMPRESA");
        tab_tabla.getColumna("telefono").setNombreVisual("TElÉFONOS");
        tab_tabla.getColumna("representante").setNombreVisual("REPRESENTANTE");
        tab_tabla.getColumna("direccion").setNombreVisual("DIRECCIÓN");
        tab_tabla.getColumna("emp_estado").setNombreVisual("ACTIVA");
        tab_tabla.getColumna("logo_empr").setNombreVisual("LOGO");
        tab_tabla.getColumna("logo_empr").setUpload("logos");
        tab_tabla.getColumna("logo_empr").setImagen("100", "200");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
}
