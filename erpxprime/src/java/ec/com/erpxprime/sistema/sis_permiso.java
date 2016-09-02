/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

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
public class sis_permiso extends Pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_permiso.class.getName());
    private Tabla tab_tabla_pri = new Tabla();
    private Tabla tab_tabla_sec = new Tabla();
    private Tabla tab_tabla_reporte = new Tabla();

    public sis_permiso() {
        LOGGER.log(Level.INFO, "sis_permiso cargando");

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla_pri.setId("tab_tabla_pri");
        tab_tabla_pri.setTabla("tbl_perfil", "id_perfil", 1);
        tab_tabla_pri.getColumna("id_perfil2").setCombo("tbl_perfil", "id_perfil", "nombre", "");
        tab_tabla_pri.agregarRelacion(tab_tabla_sec);
        tab_tabla_pri.agregarRelacion(tab_tabla_reporte);
        tab_tabla_pri.setRows(20);
        tab_tabla_pri.dibujar();
        PanelTabla pat_panel_pri = new PanelTabla();
        pat_panel_pri.setPanelTabla(tab_tabla_pri);

        tab_tabla_sec.setId("tab_tabla_sec");
        tab_tabla_sec.setIdCompleto("tab_tabulador:tab_tabla_sec");
        tab_tabla_sec.setTabla("tbl_perfil_opcion", "id_perfil_opcion", 2);
        tab_tabla_sec.getColumna("id_opcion").setCombo("select a.id_opcion,a.nombre, "
                + "(case when b.id_opcion_2 is null then 'PANTALLA' else 'MENU' end) as nuevo "
                + "from tbl_opcion a left join (select DISTINCT id_opcion_2 from tbl_opcion "
                + "where id_opcion_2 in (select id_opcion from tbl_opcion)) b on a.id_opcion=b.id_opcion_2 "
                + "order by a.nombre");
        tab_tabla_sec.getColumna("lectura").setCheck();
        tab_tabla_sec.getColumna("id_opcion").setAutoCompletar();
        tab_tabla_sec.setRows(20);
        tab_tabla_sec.dibujar();
        PanelTabla pat_panel_sec = new PanelTabla();
        pat_panel_sec.setPanelTabla(tab_tabla_sec);

        tab_tabla_reporte.setId("tab_tabla_reporte");
        tab_tabla_reporte.setIdCompleto("tab_tabulador:tab_tabla_reporte");
        tab_tabla_reporte.setTabla("tbl_perfil_reporte", "id_perfil_reporte", 3);
        tab_tabla_reporte.getColumna("id_reporte").setCombo("select rep.id_reporte, "
                + "concat(op.nombre ,concat(' - ' , rep.nombre)) as nuevo "
                + "from tbl_reporte rep, tbl_opcion op "
                + "where rep.id_opcion = op.id_opcion "
                + "order by op.nombre, rep.nombre");
        tab_tabla_reporte.setRows(20);
        tab_tabla_reporte.dibujar();
        PanelTabla pat_panel_reporte = new PanelTabla();
        pat_panel_reporte.setPanelTabla(tab_tabla_reporte);
//        
        tab_tabulador.agregarTab("OPCIONES", pat_panel_sec);
        tab_tabulador.agregarTab("REPORTES", pat_panel_reporte);

//        Division div_division_permisos = new Division();
//        div_division_permisos.dividir2(pat_panel_sec, pat_panel_reporte, "50%", "V");

        Division div_division = new Division();
        div_division.dividir2(pat_panel_pri, tab_tabulador, "30%", "H");
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla_pri.guardar()) {
            if (tab_tabla_sec.guardar()) {
                if (tab_tabla_reporte.guardar()) {
                    guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla_pri() {
        return tab_tabla_pri;
    }

    public void setTab_tabla_pri(Tabla tab_tabla_pri) {
        this.tab_tabla_pri = tab_tabla_pri;
    }

    public Tabla getTab_tabla_sec() {
        return tab_tabla_sec;
    }

    public void setTab_tabla_sec(Tabla tab_tabla_sec) {
        this.tab_tabla_sec = tab_tabla_sec;
    }

    public Tabla getTab_tabla_reporte() {
        return tab_tabla_reporte;
    }

    public void setTab_tabla_reporte(Tabla tab_tabla_reporte) {
        this.tab_tabla_reporte = tab_tabla_reporte;
    }
}
