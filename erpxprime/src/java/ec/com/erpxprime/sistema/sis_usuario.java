/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.com.erpxprime.framework.componentes.Boton;
import ec.com.erpxprime.framework.componentes.Dialogo;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.Encriptar;
import ec.com.erpxprime.framework.componentes.Etiqueta;
import ec.com.erpxprime.framework.componentes.Grid;
import ec.com.erpxprime.framework.componentes.Grupo;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Reporte;
import ec.com.erpxprime.framework.componentes.SeleccionFormatoReporte;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.framework.componentes.Tabulador;
import ec.com.erpxprime.framework.componentes.Texto;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author HP
 */
public class sis_usuario extends Pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_usuario.class.getName());
    private Tabla tab_tabla = new Tabla();
    private Dialogo dia_clave = new Dialogo();
    private Etiqueta eti_clave = new Etiqueta();
    private Texto tex_nick;
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Map map_parametros = new HashMap();
    private final Encriptar encriptar = new Encriptar();

    public sis_usuario() {

        Boton bot_generar = new Boton();
        Boton bot_activar = new Boton();

        bot_generar.setValue("Generar Nueva Clave");
        bot_generar.setMetodo("abrirGenerarClave");
        bar_botones.agregarBoton(bot_generar);

        bot_activar.setValue("Activar / Desactivar");
        bot_activar.setMetodo("activarUsuario");
        bot_activar.setTitle("Activar / Desactivar al usuario actual");
        bar_botones.agregarBoton(bot_activar);

        String lstr_condicion = " id_empresa = " + 1;

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setTabla("tbl_usuario", "id_usuario", 1);
        tab_tabla.getColumna("id_perfil").setCombo("tbl_perfil", "id_perfil", "nombre", "");
        tab_tabla.getColumna("id_perfil").setRequerida(true);
        tab_tabla.getColumna("nombre_completo").setNombreVisual("NOMBRES");
        tab_tabla.getColumna("id_perfil").setNombreVisual("PERFIL");
        tab_tabla.getColumna("nombre").setNombreVisual("NICK NAME");
        tab_tabla.getColumna("nombre").setUnico(true);
        tab_tabla.getColumna("estado_sesion").setCheck();
        tab_tabla.getColumna("estado_sesion").setValorDefecto("false");
        tab_tabla.getColumna("estado_sesion").setVisible(false);
        tab_tabla.getColumna("id_usuario").setVisible(false);
        tab_tabla.getColumna("tema").setVisible(false);
        tab_tabla.getColumna("clave").setVisible(false);
        tab_tabla.getColumna("tema").setValorDefecto("sam");
        tab_tabla.getColumna("fecha_registro").setVisible(false);
        tab_tabla.getColumna("nombre").setMetodoChange("asignarClave");
        tab_tabla.getColumna("correo").setLongitud(150);
        tab_tabla.getColumna("correo2").setLongitud(150);
        tab_tabla.getColumna("ruta_foto").setNombreVisual("FOTOGRAFÍA");
        tab_tabla.getColumna("ruta_foto").setUpload("fotos_usuarios");
        tab_tabla.getColumna("ruta_foto").setImagen("100", "200");
        tab_tabla.setFocus();
        tab_tabla.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setMensajeWarn("Cuando se crean un usuario nuevo la clave es la misma que el valor del campo NICK NAME");
        pat_panel1.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel1);
        agregarComponente(div_division);

        dia_clave.setId("dia_clave");
        dia_clave.setTitle("Generar Nueva Clave");
        dia_clave.setWidth("40%");
        dia_clave.setHeight("18%");
        dia_clave.getBot_aceptar().setMetodo("aceptarClave");
        dia_clave.setResizable(false);
        Grupo gru_cuerpo = new Grupo();
        Etiqueta eti_mensaje = new Etiqueta();
        eti_mensaje.setValue("El sistema generó una nueva clave para el usuario seleccionado, para asignar la clave presionar el botón aceptar");
        eti_mensaje.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
        eti_clave.setStyle("font-size: 25px;");
        Grid gri_clave = new Grid();
        gri_clave.setWidth("100%");
        gri_clave.setStyle("text-align: center;");
        gri_clave.getChildren().add(eti_clave);
        gru_cuerpo.getChildren().add(eti_mensaje);
        gru_cuerpo.getChildren().add(gri_clave);

        dia_clave.setDialogo(gru_cuerpo);
        agregarComponente(dia_clave);

        bar_botones.agregarReporte();
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        //Para activar o desactivar el campo nick	
        tex_nick = ((Texto) utilitario.getComponente(tab_tabla.getColumna("nombre").getId()));
        cambiarEstadoNick();

    }

    @Override
    public void aceptarReporte() {
        // TODO Auto-generated method stub
        if (rep_reporte.getReporteSelecionado().equals("Reporte de Usuarios")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                map_parametros.put("titulo", "REPORTE DE USUARIOS");
                sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                sel_rep.dibujar();
                rep_reporte.cerrar();
                utilitario.addUpdate("rep_reporte");
            }
        }

    }

    @Override
    public void abrirListaReportes() {
        // TODO Auto-generated method stub
        rep_reporte.dibujar();
    }

    /**
     * Activa o desactiva el cuadro de texto del nick
     */
    private void cambiarEstadoNick() {
        if (tab_tabla.isFilaInsertada()) {
            //si la fila es insertada activo el cuadro de texto
            tex_nick.setDisabled(false);
        } else {
            tex_nick.setDisabled(true);
        }
    }

    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
        cambiarEstadoNick();
    }

    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
        cambiarEstadoNick();
    }

    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
        cambiarEstadoNick();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
        cambiarEstadoNick();
    }

    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        super.actualizar();
        cambiarEstadoNick();
    }

    @Override
    public void aceptarBuscar() {
        // TODO Auto-generated method stub
        super.aceptarBuscar();
        cambiarEstadoNick();
    }

    /**
     * Activa el usuario seleccionado
     */
    public void activarUsuario() {
        if (tab_tabla.getValor("estado_sesion").equals("true")) {
            desactivarUsuario(tab_tabla.getValor("id_usuario"));
            utilitario.agregarMensaje("Se desactivó al usuario " + tab_tabla.getValor("nombre_completo"), "");
            tab_tabla.setValor("estado_sesion", "false");
            utilitario.addUpdate("tab_tabla");
        } else {
            activarUsuario(tab_tabla.getValor("id_usuario"));
            utilitario.agregarMensaje("Se activó al usuario " + tab_tabla.getValor("nombre_completo"), "");
            tab_tabla.setValor("estado_sesion", "true");
            utilitario.addUpdate("tab_tabla");
        }
    }

    /**
     * Activa un usuario
     *
     * @param id_uduario Clave primaria del usuario
     */
    public void activarUsuario(String id_uduario) {
        // Activa estado usuario
        String str_sql = "UPDATE tbl_usuario SET estado_sesion=true WHERE id_uduario="
                + id_uduario + "";
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para activacion del usuario
//        utilitario.getConexion().agregarSql(
//                crearSQLAuditoriaAcceso(ide_usua, P_SIS_ACTIVA_USUARIO,
//                        "Activar usuario"));
        utilitario.getConexion().ejecutarListaSql();
    }

    /**
     * Desbloquea un usuario
     *
     * @param id_usuario Clave primaria del usuario
     */
    public void desactivarUsuario(String id_usuario) {
        // Bloquea el estado
        String str_sql = "UPDATE tbl_usuario SET estado_sesion=false WHERE id_uduario="
                + id_usuario + "";
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para bloqueo
//        utilitario.getConexion().agregarSql(
//                crearSQLAuditoriaAcceso(id_usuario, P_SIS_DESBLOQUEA_USUARIO,
//                        "Desbloquear Usuario"));
        //pongo fin a todos sus seciones no finalizadas

//        str_sql = "update SIS_AUDITORIA_ACCESO set  FIN_AUAC=true WHERE IDE_USUA="
//                + id_usuario + "";
//        utilitario.getConexion().agregarSql(str_sql);
        utilitario.getConexion().ejecutarListaSql();
    }

    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            if (tab_tabla.isFilaInsertada() == false) {
                tab_tabla.getColumna("fecha_registro").setValorDefecto(utilitario.getFechaActual() + " " + utilitario.getHoraActual());
                tab_tabla.insertar();
            } else {
                utilitario.agregarMensajeInfo("No se puede Insertar",
                        "Debe guardar el Usuario actual");
            }
        }

        cambiarEstadoNick();
    }

    public void asignarClave(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        if (tab_tabla.isFilaInsertada()) {
            tab_tabla.setValor("clave", encriptar.getEncriptar(tab_tabla.getValor("nombre")));
            utilitario.addUpdate("tab_tabla");
        }
    }

    @Override
    public void guardar() {
        // valida la longitud minima del campo nick si inserto o modifico
        if (tab_tabla.isFilaInsertada() || tab_tabla.isFilaModificada()) {
            if (tab_tabla.getValor("nombre") == null) {
                utilitario.agregarMensajeError("No se puede guardar",
                        "El campo de login debe estar ingresado");
                return;
            }
        }

        tab_tabla.guardar();
        guardarPantalla();
        cambiarEstadoNick();
    }

    @Override
    public void eliminar() {
        if (tab_tabla.isFocus()) {
            if (tab_tabla.isFilaInsertada()) {
                tab_tabla.eliminar();
            } else {
                desactivarUsuario(tab_tabla.getValor("id_usuario"));
                tab_tabla.setValor("estado_sesion", "true");
                utilitario.agregarMensajeInfo("Usuario deshabilitado", "");
            }
        }

        cambiarEstadoNick();
    }

    public void abrirGenerarClave() {
        if (!tab_tabla.isFilaInsertada()) {
            eti_clave.setValue(encriptar.getGenerarClave());
            dia_clave.dibujar();
        } else {
            utilitario.agregarMensaje(
                    "No se puede generar una nueva clave a usuarios nuevos",
                    "");
        }
    }

    /**
     * Resetea la clave de un usuario
     *
     * @param ide_usua Clave primaria del usuario
     * @param clave Nueva clave
     */
    public void resetearClave(String ide_usua, String clave) {
        // Resete la Clave
//        String str_sql = "UPDATE sis_usuario_clave SET CLAVE_USCL='"
//                + encriptar.getEncriptar(clave) + "' WHERE IDE_USUA="
//                + ide_usua + " AND ACTIVO_USCL=true";
        // Actualiza el campo para que a lo que ngrese al sistema le obligue a
        // cambiar la clave
//        utilitario.getConexion().agregarSql(str_sql);
        String str_sql = "UPDATE tbl_usuario SET clave= '" + clave + "' WHERE id_usuario=" + ide_usua;
        utilitario.getConexion().agregarSql(str_sql);
        // Auditoria para reseteo de clavel
//        utilitario.getConexion().agregarSql(
//                crearSQLAuditoriaAcceso(ide_usua, P_SIS_RESETEO_CLAVE,
//                        "Reseteo de clave"));
//        utilitario.getConexion().agregarSql(
//                crearSQLHistorialClave(ide_usua, clave));
        utilitario.getConexion().ejecutarListaSql();
    }

    public void aceptarClave() {
        // Resetea la Clave
        resetearClave(tab_tabla.getValorSeleccionado(), eti_clave.getValue().toString());
        utilitario.agregarMensaje("Cambio clave",
                "La clave a sido cambiada correctamente");
        utilitario.addUpdate("tab_tabla");
        dia_clave.cerrar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Dialogo getDia_clave() {
        return dia_clave;
    }

    public void setDia_clave(Dialogo dia_clave) {
        this.dia_clave = dia_clave;
    }

    public Etiqueta getEti_clave() {
        return eti_clave;
    }

    public void setEti_clave(Etiqueta eti_clave) {
        this.eti_clave = eti_clave;
    }

    public Texto getTex_nick() {
        return tex_nick;
    }

    public void setTex_nick(Texto tex_nick) {
        this.tex_nick = tex_nick;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public Map getMap_parametros() {
        return map_parametros;
    }

    public void setMap_parametros(Map map_parametros) {
        this.map_parametros = map_parametros;
    }

}
