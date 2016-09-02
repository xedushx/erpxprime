package ec.com.erpxprime.general;

import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class gen_tipo_identificacion extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();

    public gen_tipo_identificacion() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("gen_tipo_identificacion", "tid_codigo", 1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
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
        utilitario.getConexion().guardarPantalla();
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
