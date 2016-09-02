/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.com.erpxprime.framework.componentes.Boton;
import ec.com.erpxprime.framework.componentes.Calendario;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.Etiqueta;
import ec.com.erpxprime.framework.componentes.Hora;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kruger
 */
public class sis_auditoria extends Pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_auditoria.class.getName());
    private Tabla tab_auditoria = new Tabla();
    private Division div_division = new Division();
    private Calendario cal_fecha_inicial = new Calendario();
    private Calendario cal_fecha_final = new Calendario();
    private Hora hor_inicial = new Hora();
    private Hora hor_final = new Hora();

    public sis_auditoria() {
        LOGGER.log(Level.INFO, "sis_auditoria cargando");
        bar_botones.limpiar();

        bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
        cal_fecha_inicial.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_inicial);

        bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
        cal_fecha_final.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_final);

        bar_botones.agregarComponente(new Etiqueta("Desde :"));
        hor_inicial.setValue(utilitario.getHora(utilitario.getFormatoHora("07:00:00")));
        bar_botones.agregarComponente(hor_inicial);

        bar_botones.agregarComponente(new Etiqueta("Hasta :"));
        hor_final.setValue(utilitario.getHora(utilitario.getFormatoHora("18:00:00")));
        bar_botones.agregarComponente(hor_final);

        Boton bot_filtrar = new Boton();
        bot_filtrar.setValue("Actualizar");
        bot_filtrar.setMetodo("actualizarAuditoria");
        bot_filtrar.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_filtrar);

        tab_auditoria.setId("tab_auditoria");
        tab_auditoria.setTabla("tbl_auditoria", "id_auditoria", 1);
        tab_auditoria.setLectura(true);
        tab_auditoria.getColumna("id_usuario").setCombo("tbl_usuario", "id_usuario", "nombre_completo", "");
        tab_auditoria.getColumna("id_opcion").setCombo("tbl_opcion", "id_opcion", "nombre", "");
        tab_auditoria.getColumna("id_accion").setCombo("tbl_accion", "id_accion", "nombre", "");
        tab_auditoria.setRows(20);
        tab_auditoria.getColumna("id_usuario").setFiltro(true);
        tab_auditoria.getColumna("id_accion").setFiltro(true);
        tab_auditoria.getColumna("id_opcion").setFiltro(true);
        tab_auditoria.getColumna("valor").setLongitud(300);
        tab_auditoria.setCondicion(getFiltrosAuditoria());
        tab_auditoria.setCampoOrden("id_auditoria desc");
        tab_auditoria.dibujar();

        PanelTabla pat_auditoria = new PanelTabla();
        pat_auditoria.setId("pat_auditoria");
        tab_auditoria.setHeader("Transacciones realizadas en el sistema");
        pat_auditoria.setPanelTabla(tab_auditoria);

        div_division.setId("div_division");
        div_division.dividir1(pat_auditoria);

        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        
    }

    @Override
    public void guardar() {
        
    }

    @Override
    public void eliminar() {
        
    }

    private String getFiltrosAuditoria() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cal_fecha_inicial.getValue() != null
                && cal_fecha_final.getValue() != null
                && hor_inicial.getValue() != null
                && hor_final.getValue() != null) {

            str_filtros = " fecha BETWEEN "
                    + utilitario.getFormatoFechaSQL(cal_fecha_inicial.getFecha()) + " AND "
                    + utilitario.getFormatoFechaSQL(cal_fecha_final.getFecha());
            str_filtros += " AND hora BETWEEN "
                    + utilitario.getFormatoHoraSQL(hor_inicial.getHora())
                    + " AND "
                    + utilitario.getFormatoHoraSQL(hor_final.getHora());

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe ingresar los fitros de fechas y horas");
        }
        return str_filtros;
    }

    public void actualizarAuditoria() {
        if (!getFiltrosAuditoria().isEmpty()) {
            tab_auditoria.setCondicion(getFiltrosAuditoria());
            tab_auditoria.ejecutarSql();
            utilitario.addUpdate("div_division");
        }
    }

    public Tabla getTab_auditoria() {
        return tab_auditoria;
    }

    public void setTab_auditoria(Tabla tab_auditoria) {
        this.tab_auditoria = tab_auditoria;
    }
}
