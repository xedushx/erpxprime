/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.com.erpxprime.framework.componentes.Calendario;
import ec.com.erpxprime.framework.componentes.Clave;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.Efecto;
import ec.com.erpxprime.framework.componentes.Etiqueta;
import ec.com.erpxprime.framework.componentes.Grid;
import ec.com.erpxprime.framework.componentes.Grupo;
import ec.com.erpxprime.framework.componentes.Imagen;
import ec.com.erpxprime.framework.componentes.ItemMenu;
import ec.com.erpxprime.framework.componentes.ItemOpcion;
import ec.com.erpxprime.framework.componentes.Panel;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.component.menu.Menu;
import org.primefaces.component.themeswitcher.ThemeSwitcher;

/**
 *
 * @author HP
 */
public final class sis_admin_usuario extends Pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_admin_usuario.class.getName());
    private Tabla tab_form_usuario = new Tabla();
    private Panel pan_opcion = new Panel();
    private int int_opcion = 1;
    private ThemeSwitcher ths_temas = new ThemeSwitcher();
    private Calendario cal_calendario = new Calendario();
    private Clave cla_clave_actual = new Clave();
    private Clave cla_nueva = new Clave();
    private Clave cla_confirmar = new Clave();
    private Efecto efecto = new Efecto();
    private Imagen ima_usuario = new Imagen();
    
    public sis_admin_usuario() {
        
        ItemMenu mit_datos = new ItemMenu();
        mit_datos.setIcon("ui-icon-person");
        mit_datos.setValue("Datos del Usuario");
        mit_datos.setUpdate("pan_opcion");
        mit_datos.setMetodo("dibujarDatosUusario");

        ItemMenu mit_clave = new ItemMenu();
        mit_clave.setValue("Cambiar Clave ");
        mit_clave.setIcon("ui-icon-key");
        mit_clave.setUpdate("pan_opcion");
        mit_clave.setMetodo("dibujarCambiarClave");

        ItemMenu mit_tema = new ItemMenu();
        mit_tema.setValue("Cambiar Tema ");
        mit_tema.setIcon("ui-icon-image");
        mit_tema.setUpdate("pan_opcion");
        mit_tema.setMetodo("dibujarTemas");

        Menu men_menu = new Menu();
        men_menu.getChildren().add(mit_datos);
        men_menu.getChildren().add(mit_clave);
        men_menu.getChildren().add(mit_tema);

        Panel pan_menu = new Panel();
        pan_menu.setHeader("OPCIONES");
        pan_menu.getChildren().add(men_menu);

        cal_calendario.setMode("inline");
        Panel pan_calendario = new Panel();
        pan_calendario.setHeader("CALENDARIO");
        pan_calendario.getChildren().add(cal_calendario);
        Grupo gru_panel_izquierda = new Grupo();
        gru_panel_izquierda.getChildren().add(pan_menu);
        gru_panel_izquierda.getChildren().add(pan_calendario);

        Panel pan_empresa = new Panel();
        pan_empresa.setHeader("EMPRESA");
        Imagen ima_empresa = new Imagen();
        ima_empresa.setValueExpression("value", "mbe_index.logo");
        ima_empresa.setWidth("200");
        ima_empresa.setHeight("100");
        pan_empresa.getChildren().add(ima_empresa);
        Etiqueta eti_sucursal = new Etiqueta();
        eti_sucursal.setStyle("width: 100%;font-size: 13px;text-align: center;font-weight: bold;");
        eti_sucursal.setValue("<br/>" + utilitario.getConexion().consultar("SELECT nombre from tbl_empresa where id_empresa=" + utilitario.getVariable("empresa")).get(0));
        pan_empresa.getChildren().add(eti_sucursal);
        gru_panel_izquierda.getChildren().add(pan_empresa);
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setStyle("overflow: auto");

        efecto.setType("drop");
        efecto.setSpeed(150);
        efecto.setPropiedad("mode", "'show'");
        efecto.setEvent("load");

        pan_opcion.getChildren().add(efecto);

        dibujarDatosUusario();
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(gru_panel_izquierda, pan_opcion, "22%", "V");
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        agregarComponente(div_division);

        ima_usuario.setId("ima_usuario");
    }

    public void dibujarTemas() {
        pan_opcion.setHeader("Tema del Usuario");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        Grupo gru_panel = new Grupo();
        gru_panel.setStyle("overflow:auto; display:block;");
        Grid gri_tema = new Grid();
        gri_tema.setColumns(2);
        gri_tema.getChildren().add(new Etiqueta("Tema"));
        List<String> lis_temas = new ArrayList<String>();
        lis_temas.add("afterdark");
        lis_temas.add("afternoon");
        lis_temas.add("afterwork");
        lis_temas.add("aristo");
        lis_temas.add("black-tie");
        lis_temas.add("blitzer");
        lis_temas.add("bluesky");
        lis_temas.add("bootstrap");
        lis_temas.add("casablanca");
        lis_temas.add("cupertino");
        lis_temas.add("eggplant");
        lis_temas.add("excite-bike");
        lis_temas.add("flick");
        lis_temas.add("glass-x");
        lis_temas.add("home");
        lis_temas.add("hot-sneaks");
        lis_temas.add("humanity");
        lis_temas.add("le-frog");
        lis_temas.add("midnight");
        lis_temas.add("mint-choc");
        lis_temas.add("overcast");
        lis_temas.add("pepper-grinder");
        lis_temas.add("redmond");
        lis_temas.add("rocket");
        lis_temas.add("sam");
        lis_temas.add("smoothness");
        lis_temas.add("south-street");
        lis_temas.add("start");
        lis_temas.add("sunny");
        lis_temas.add("swanky-purse");
        lis_temas.add("trontastic");
        lis_temas.add("ui-lightness");
        lis_temas.add("vader");

        Grid gri_imagenes = new Grid();
        gri_imagenes.setColumns(5);
        gri_imagenes.setWidth("100%");
        gri_imagenes.setStyle("font-size: 13px;text-align: center;");

        for (int i = 0; i < lis_temas.size(); i++) {
            ItemOpcion ito_tema = new ItemOpcion();
            ito_tema.setItemValue(lis_temas.get(i));
            ito_tema.setItemLabel(lis_temas.get(i) + "");
            ths_temas.getChildren().add(ito_tema);
            Grid gri_actual = new Grid();
            gri_actual.getChildren().add(new Etiqueta(lis_temas.get(i) + ""));
            Imagen ima_tema = new Imagen();
            ima_tema.setValue("/imagenes/temas/" + lis_temas.get(i) + ".png");
            gri_actual.getChildren().add(ima_tema);
            gri_imagenes.getChildren().add(gri_actual);
        }

        ths_temas.setStyle("width:180px");
        ths_temas.setValue(utilitario.getVariable("tema"));
        gri_tema.getChildren().add(ths_temas);
        gru_panel.getChildren().add(gri_tema);
        gru_panel.getChildren().add(gri_imagenes);
        pan_opcion.getChildren().add(gru_panel);
        int_opcion = 3;

    }

    public void dibujarDatosUusario() {
        pan_opcion.setHeader("Datos del Usuario");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);

        tab_form_usuario = new Tabla();
        tab_form_usuario.setTipoFormulario(true);
        tab_form_usuario.setMostrarNumeroRegistros(false);
        tab_form_usuario.setId("tab_form_usuario");
        tab_form_usuario.setTabla("tbl_usuario", "id_usuario", 0);
        tab_form_usuario.getColumna("id_usuario").setVisible(false);
        tab_form_usuario.getColumna("id_usuario").setLectura(true);
        tab_form_usuario.getColumna("id_perfil").setNombreVisual("PERFIL");
        tab_form_usuario.getColumna("id_perfil").setLectura(true);
        tab_form_usuario.getColumna("id_perfil").setCombo("tbl_perfil", "id_perfil", "nombre", "");
        tab_form_usuario.getColumna("estado_sesion").setVisible(false);
        tab_form_usuario.getColumna("tema").setVisible(false);
        tab_form_usuario.getColumna("fecha_registro").setVisible(false);
        tab_form_usuario.getColumna("tema").setNombreVisual("TEMA");
        tab_form_usuario.getColumna("nombre_completo").setNombreVisual("NOMBRE COMPLETO");
        tab_form_usuario.getColumna("nombre").setNombreVisual("NOMBRE LOGIN");
        tab_form_usuario.getColumna("correo").setNombreVisual("CORREO PRINCIPAL");
        tab_form_usuario.getColumna("correo2").setNombreVisual("CORREO ADICIONAL");
        tab_form_usuario.getColumna("clave").setVisible(false);
        tab_form_usuario.getColumna("ruta_foto").setVisible(false);
//        tab_form_usuario.getColumna("ruta_foto").setNombreVisual("FOTOGRAFÍA");
//        tab_form_usuario.getColumna("ruta_foto").setUpload("fotos_usuarios");
//        tab_form_usuario.getColumna("ruta_foto").setImagen("20", "20");
//        tab_form_usuario.getColumna("ruta_foto").setMetodoChange("actualizarFoto");
        tab_form_usuario.getColumna("nombre").setLectura(true);
//        tab_form_usuario.setLectura(true);
        tab_form_usuario.setCondicion("id_usuario=" + utilitario.getVariable("id_usuario"));
        tab_form_usuario.dibujar();

//        ima_usuario.setValue((utilitario.getConexion().consultar("SELECT ruta_foto from tbl_usuario where id_usuario=" + utilitario.getVariable("id_usuario")).get(0)));
//        ima_usuario.setWidth("150");
//        ima_usuario.setHeight("200");

        actualizarFoto();
        
        Grid gri_tabla = new Grid();
        gri_tabla.setColumns(2);
        gri_tabla.getChildren().add(ima_usuario);
        gri_tabla.getChildren().add(tab_form_usuario);

        pan_opcion.getChildren().add(gri_tabla);
        int_opcion = 1;
    }

    public void actualizarFoto() {
        ima_usuario.setValue((utilitario.getConexion().consultar("SELECT ruta_foto from tbl_usuario where id_usuario=" + utilitario.getVariable("id_usuario")).get(0)));
        ima_usuario.setWidth("150");
        ima_usuario.setHeight("200");
        utilitario.addUpdate("ima_usuario,tab_form_usuario");
    }
    
    public void dibujarCambiarClave() {
        pan_opcion.setHeader("Cambiar la clave");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        cla_clave_actual.setValue("");
        cla_confirmar.setValue("");
        cla_nueva.setValue("");
        cla_nueva.setImmediate(false);
        cla_nueva.setFeedback(true);
        cla_confirmar.setFeedback(true);
        cla_confirmar.setImmediate(false);
        Grid gri_matriz = new Grid();
        gri_matriz.setColumns(2);
        gri_matriz.getChildren().add(new Etiqueta("<strong>CLAVE ACTUAL :</strong>"));
        gri_matriz.getChildren().add(cla_clave_actual);
        gri_matriz.getChildren().add(new Etiqueta("<strong>CLAVE NUEVA :</strong>"));
        gri_matriz.getChildren().add(cla_nueva);
        gri_matriz.getChildren().add(new Etiqueta("<strong>CONFIRMAR CLAVE NUEVA :</strong>"));
        gri_matriz.getChildren().add(cla_confirmar);
        pan_opcion.getChildren().add(gri_matriz);
        int_opcion = 2;
    }

    private void guardarClave() {
        LOGGER.log(Level.INFO, "Guardando clave");
        if (cla_clave_actual.getValue() != null && !cla_clave_actual.getValue().toString().isEmpty()) {
            if ((cla_nueva.getValue() != null && !cla_nueva.getValue().toString().isEmpty()) && (cla_confirmar.getValue() != null && !cla_confirmar.getValue().toString().isEmpty())) {

                if (cla_nueva.getValue().equals(cla_confirmar.getValue())) {
                    String lstr_clave_actual = (String) utilitario.getConexion().consultar("SELECT clave FROM tbl_usuario WHERE id_usuario=" + utilitario.getVariable("id_usuario").replace("'", "")).get(0);
                    if (lstr_clave_actual.toString().equals(cla_clave_actual.getValue())) {
                        String sql = "UPDATE tbl_usuario SET clave='" + cla_nueva.getValue() + "' WHERE id_usuario=" + utilitario.getVariable("id_usuario").replace("'", "");
                        getConexion().ejecutarSql(sql);
                        try {
                            getConexion().getConnection().commit();
                        } catch (SQLException ex) {
                            Logger.getLogger(sis_admin_usuario.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        utilitario.agregarMensaje("Cambió clave", "La clave ha sido cambiada correctamente");
                    } else {
                        utilitario.agregarMensajeError("Error", "La clave es incorrecta");
                    }
                } else {
                    utilitario.agregarMensajeInfo("Validación", "Las claves deben ser iguales.");
                }

            } else {
                utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar un valor a la nueva clave");
            }
        } else {
            utilitario.agregarMensajeInfo("Validación", "Es necesario ingresar la clave actual");
        }
    }

    private void guardarDatosUsuario() {
        tab_form_usuario.guardar();
        guardarPantalla();
    }

    @Override
    public void guardar() {
        if (int_opcion == 1) {
            actualizarFoto();
            guardarDatosUsuario();
        } else if (int_opcion == 2) {
            guardarClave();
        } else if (int_opcion == 3) {
            //Guarda si cambio de tema
            if (utilitario.getVariable("tema") == null || !utilitario.getVariable("tema").equals(ths_temas.getValue())) {
                String sql = "UPDATE tbl_usuario SET tema='" + ths_temas.getValue() + "' WHERE id_usuario=" + utilitario.getVariable("id_usuario");
                getConexion().ejecutarSql(sql);
                utilitario.crearVariable("tema", ths_temas.getValue() + "");
                utilitario.agregarMensaje("Cambió Tema", "El tema se cambió correctamente");
                if (int_opcion == 1) {
                    dibujarDatosUusario();
                }
            }
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_form_usuario() {
        return tab_form_usuario;
    }

    public void setTab_form_usuario(Tabla tab_form_usuario) {
        this.tab_form_usuario = tab_form_usuario;
    }

    public Imagen getIma_usuario() {
        return ima_usuario;
    }

    public void setIma_usuario(Imagen ima_usuario) {
        this.ima_usuario = ima_usuario;
    }
}