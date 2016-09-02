/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.com.erpxprime.framework.componentes.Arbol;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.framework.componentes.Tabulador;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class sis_opcion extends Pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_opcion.class.getName());
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Arbol arb_arbol = new Arbol();

    public sis_opcion() {
        LOGGER.log(Level.INFO, "sis_opcion cargando");

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("tbl_opcion", "id_opcion", 1);
        tab_tabla.setCampoPadre("id_opcion_2");
        tab_tabla.setCampoNombre("nombre");
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.getColumna("auditada").setCheck();
        tab_tabla.agregarArbol(arb_arbol);
        tab_tabla.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla);

        arb_arbol.setId("arb_arbol");
        arb_arbol.dibujar();

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
        tab_tabla2.setTabla("tbl_reporte", "id_reporte", 2);
        tab_tabla2.dibujar();
        PanelTabla pat_paneltab2 = new PanelTabla();
        pat_paneltab2.setPanelTabla(tab_tabla2);

        tab_tabulador.agregarTab("REPORTES", pat_paneltab2);

        Division div3 = new Division(); //UNE OPCION Y DIV 2
        div3.dividir2(pat_panel1, tab_tabulador, "60%", "H");
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, div3, "21%", "V");  //arbol y div3
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            tab_tabla2.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}
