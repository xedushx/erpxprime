/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.general;

import ec.com.erpxprime.framework.componentes.Arbol;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;
import org.primefaces.event.NodeSelectEvent;

/**
 *
 * @author user
 */
public class gen_ubicacion extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private Arbol arb_arbol = new Arbol();

    public gen_ubicacion() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("gen_lugar_geografico", "lge_codigo", 1);
        
        tab_tabla1.getColumna("lge_nivel").setCombo(utilitario.getListaNiveles());
        tab_tabla1.setCampoNombre("lge_nombre"); //Para que se configure el arbol
        tab_tabla1.setCampoPadre("lge_codigo_2"); //Para que se configure el arbol
        tab_tabla1.agregarArbol(arb_arbol); //Para que se configure el arbol

        tab_tabla1.getColumna("tlg_codigo").setCombo("gen_tipo_lugar_geografico", "tlg_codigo", "tlg_nombre", "");

        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        arb_arbol.setId("arb_arbol");

        arb_arbol.dibujar();

        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, pat_panel, "21%", "V");

        agregarComponente(div_division);


    }

    public void seleccionar_arbol(NodeSelectEvent evt) {
        arb_arbol.seleccionarNodo(evt);
        tab_tabla1.setValorPadre(arb_arbol.getValorSeleccionado());
        tab_tabla1.ejecutarSql();
    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}
