/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.com.erpxprime.framework.componentes.Boton;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;

/**
 *
 * @author xedushx
 */
public class sis_bloqueo extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public sis_bloqueo() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();

        Boton bot_borrar = new Boton();
        bot_borrar.setValue("Borrar Bloqueos");
        bot_borrar.setMetodo("borrar");
        bar_botones.agregarBoton(bot_borrar);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("tbl_bloqueo", "id_bloqueo", 1);
        tab_tabla.setRows(20);
        tab_tabla.getColumna("tabla").setFiltro(true);
        tab_tabla.getColumna("id_usuario").setVisible(false);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    public void borrar() {
        utilitario.getConexion().ejecutarSql("DELETE FROM tbl_bloqueo");
        utilitario.agregarMensaje("Se borró la tabla de bloqueos", "");
        tab_tabla.ejecutarSql();
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
