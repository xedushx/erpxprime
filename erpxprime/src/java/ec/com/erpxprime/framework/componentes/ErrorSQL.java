/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.sql.SQLException;
import java.util.Arrays;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class ErrorSQL extends Dialog {

    private String metodoAceptar = "mbe_index.cerrarSql";
    private String mensaje = "";
    private String sql = "";
    private String ubicacion = "";
    private SQLException excepcionSQL;
    private Exception excepcion;
    private final TabView paneles = new TabView();
    private final Tab tab_detalle = new Tab();
    private final Tab tab_sql = new Tab();
    private final Tab tab_ubicacion = new Tab();
    private final Boton bot_aceptar = new Boton();

    public ErrorSQL() {
        this.setId("error_sql");
        this.setWidgetVar("error_sql");
        this.setWidth("600");
        this.setHeight("300");
        this.setHeader("Mensaje");
        this.setModal(true);
        this.setResizable(false);
        this.setDraggable(true);
        this.setClosable(false);
        this.setDynamic(true);
        this.setVisible(false);
        tab_detalle.setId("tab_detalle");
        tab_detalle.setTitle("DETALLE");
        tab_sql.setId("tab_sql");
        tab_sql.setTitle("SQL");
        tab_ubicacion.setTitle("UBICACIÓN");

        paneles.setId("paneles");

        paneles.setStyle("height: 88%;overflow: auto;display: block;");
        paneles.getChildren().add(tab_detalle);
        paneles.getChildren().add(tab_sql);
        paneles.getChildren().add(tab_ubicacion);

        this.getChildren().add(paneles);
        bot_aceptar.setId("bot_aceptar_sqlerror");
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setStyle("float: right;");
        bot_aceptar.setIcon("ui-icon-check");
        bot_aceptar.setUpdate("error_sql");
        this.getChildren().add(bot_aceptar);
    }

    public void setErrorSQL(String mensaje, String ubicacion, Exception e) {
        this.mensaje = mensaje;
        this.ubicacion = ubicacion;
        this.excepcion = e;
        this.sql = "";
        this.setVisible(false);
    }

    public void setErrorSQL(String mensaje, String ubicacion, String sql, SQLException excepcion) {
        this.mensaje = mensaje;
        this.ubicacion = ubicacion;
        this.excepcionSQL = excepcion;
        this.sql = sql;
        this.setVisible(false);
    }

    public void setErrorSQL(String mensaje, String ubicacion, String sql, Exception excepcion) {
        this.mensaje = mensaje;
        this.ubicacion = ubicacion;
        this.excepcion = excepcion;
        this.sql = sql;
        this.setVisible(false);
    }

    public void dibujar() {
        bot_aceptar.setMetodoRuta(metodoAceptar);
        bot_aceptar.setUpdate("error_sql");
        tab_detalle.getChildren().clear();
        tab_sql.getChildren().clear();
        tab_ubicacion.getChildren().clear();
        paneles.setActiveIndex(0);
        Grid matriz = new Grid();
        matriz.setColumns(1);
        matriz.setStyle("widht: 90%;padding-left: 5px;padding-top: 10px;");
        Etiqueta eti_mensaje = new Etiqueta();
        eti_mensaje.setValue("<strong>Mensaje : </strong>" + mensaje);
        matriz.getChildren().add(eti_mensaje);

        Etiqueta eti_ubicacion = new Etiqueta();
        eti_ubicacion.setValue("<br/><strong>Ubicación : </strong>" + ubicacion);
        matriz.getChildren().add(eti_ubicacion);
        tab_detalle.getChildren().add(matriz);
        if (excepcionSQL != null) {
            Grid matriz2 = new Grid();
            matriz2.setColumns(1);
            matriz2.setStyle("widht: 90%;padding-left: 5px;padding-top: 10px;overflow: auto;");
            Etiqueta eti_sql = new Etiqueta();
            eti_sql.setValue("<strong>SQL : </strong> " + sql);
            matriz2.getChildren().add(eti_sql);

            Etiqueta eti_codigo = new Etiqueta();
            eti_codigo.setValue("<br/><strong> ErrorCode :  </strong> " + excepcionSQL.getErrorCode());
            matriz2.getChildren().add(eti_codigo);

            Etiqueta eti_estado = new Etiqueta();
            eti_estado.setValue("<br/><strong> SQLState :  </strong> " + excepcionSQL.getSQLState());
            matriz2.getChildren().add(eti_estado);
            tab_sql.getChildren().add(matriz2);

            Etiqueta eti_ubic = new Etiqueta();
            eti_ubic.setValue(Arrays.toString(excepcionSQL.getStackTrace()));
            tab_ubicacion.getChildren().add(eti_ubic);
        }
        if (excepcion != null) {
            Grid matriz2 = new Grid();
            matriz2.setColumns(1);
            matriz2.setStyle("widht: 90%;padding-left: 5px;padding-top: 10px;overflow: auto;");
            Etiqueta eti_sql = new Etiqueta();
            eti_sql.setValue("<strong>SQL : </strong> " + sql);
            matriz2.getChildren().add(eti_sql);
            Etiqueta eti_estado = new Etiqueta();
            eti_estado.setValue("<br/><strong> SQL Error :  </strong> " + excepcion.getLocalizedMessage());
            matriz2.getChildren().add(eti_estado);
            tab_sql.getChildren().add(matriz2);

            Etiqueta eti_ubic = new Etiqueta();
            eti_ubic.setValue(Arrays.toString(excepcion.getStackTrace()));
            tab_ubicacion.getChildren().add(eti_ubic);
            if (sql == null || sql.trim().isEmpty()) {
                tab_sql.setRendered(false);
            }
        }

        this.setVisible(true);
    }

    public SQLException getExcepcionSQL() {
        return excepcionSQL;
    }

    public void setExcepcionSQL(SQLException excepcion) {
        this.excepcionSQL = excepcion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Exception getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(Exception excepcion) {
        this.excepcion = excepcion;
    }

    public void limpiar() {
        this.mensaje = "";
        this.ubicacion = "";
        this.excepcionSQL = null;
        this.excepcion = null;
        this.sql = "";
    }

    public String getMetodoAceptar() {
        return metodoAceptar;
    }

    public void setMetodoAceptar(String metodoAceptar) {
        this.metodoAceptar = metodoAceptar;
    }
}
