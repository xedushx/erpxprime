/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.faces.component.UIComponent;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.toolbar.Toolbar;
import org.primefaces.component.toolbar.ToolbarGroup;

/**
 *
 * @author xedushx 
 */
public class Barra extends Toolbar {

    private final ToolbarGroup grupo_botones = new ToolbarGroup();
    private Boton bot_insertar = new Boton();
    private Boton bot_guardar = new Boton();
    private Boton bot_eliminar = new Boton();
    private Boton bot_reporte = new Boton();
    private Boton bot_calendario = new Boton();
    private Boton bot_inicio = new Boton();
    private Boton bot_siguiente = new Boton();
    private Boton bot_atras = new Boton();
    private Boton bot_fin = new Boton();
    private final Grupo gri_botones_framework = new Grupo();

    public Barra() {
        this.setTransient(true);
        this.setId("bar_botones");
        bot_insertar.setId("bot_insertar");
        bot_insertar.setTitle("Insertar");
        bot_insertar.setMetodo("insertar");
        bot_insertar.setIcon("ui-insertar");
        bot_insertar.setOnmouseover("this.focus();");
        bot_insertar.setOnmouseout("this.blur();");

        gri_botones_framework.setLayout(null);
        bot_guardar.setId("bot_guardar");
        bot_guardar.setTitle("Guardar");
        bot_guardar.setMetodo("guardar");
        bot_guardar.setIcon("ui-guardar");
        bot_guardar.setOnmouseover("this.focus();");
        bot_guardar.setOnmouseout("this.blur();");

        bot_eliminar.setId("bot_eliminar");
        bot_eliminar.setMetodo("eliminar");
        bot_eliminar.setTitle("Eliminar");
        bot_eliminar.setIcon("ui-eliminar");
        bot_eliminar.setOnmouseover("this.focus();");
        bot_eliminar.setOnmouseout("this.blur();");

        gri_botones_framework.getChildren().add(bot_insertar);
        gri_botones_framework.getChildren().add(new Espacio("5", "0"));
        gri_botones_framework.getChildren().add(bot_guardar);
        gri_botones_framework.getChildren().add(new Espacio("5", "0"));
        gri_botones_framework.getChildren().add(bot_eliminar);
        gri_botones_framework.getChildren().add(new Espacio("5", "0"));
        grupo_botones.getChildren().add(gri_botones_framework);

        bot_inicio.setTitle("Inicio");
        bot_inicio.setId("bot_inicio");
        bot_inicio.setMetodo("inicio");
        bot_inicio.setIcon("ui-icon-seek-first");

        bot_siguiente.setTitle("Siguiente");
        bot_siguiente.setId("bot_siguiente");
        bot_siguiente.setMetodo("siguiente");
        bot_siguiente.setIcon("ui-icon-seek-next");

        bot_atras.setTitle("Anterior");
        bot_atras.setId("bot_atras");
        bot_atras.setMetodo("atras");
        bot_atras.setIcon("ui-icon-seek-prev");

        bot_fin.setTitle("Fin");
        bot_fin.setId("bot_fin");
        bot_fin.setMetodo("fin");
        bot_fin.setIcon("ui-icon-seek-end");

        grupo_botones.getChildren().add(bot_inicio);
        grupo_botones.getChildren().add(bot_atras);
        grupo_botones.getChildren().add(bot_siguiente);
        grupo_botones.getChildren().add(bot_fin);
        agregarSeparador();
        this.getChildren().add(grupo_botones);

    }

    public void setConfirmarGuardar() {
        bot_guardar.setOnclick("con_guarda.show();");
        bot_guardar.setActionExpression(null);
        Framework framework = new Framework();
        framework.addUpdate("con_guarda");
    }

    public void setLectura() {
        //Bloquea todos los botones que esten agregados
        bot_insertar.setDisabled(true);
        bot_guardar.setDisabled(true);
        bot_eliminar.setDisabled(true);
        bloquearBotones(grupo_botones);
        bot_inicio.setDisabled(false);
        bot_siguiente.setDisabled(false);
        bot_fin.setDisabled(false);
        bot_atras.setDisabled(false);
    }

    private void bloquearBotones(UIComponent uic_componente) {
        for (UIComponent kid : uic_componente.getChildren()) {
            //Para los Botones   
            if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.CommandButtonRenderer")) {
                Boton bot_boton = ((Boton) kid);
                bot_boton.setDisabled(true);
                if (bot_boton.getIcon() != null && bot_boton.getIcon().equals("ui-icon-cancel")) {
                    //Si es un boton que limpia la pantalla no le desactiva
                    if (bot_boton.getValue() == null || bot_boton.getValue().toString().isEmpty()) {
                        bot_boton.setDisabled(false);
                    }
                }
                if (bot_boton.isExcluirLectura() == true) {
                    bot_boton.setDisabled(false);
                }
            }
        }

    }

    public void quitarBotonInsertar() {
        bot_insertar.setRendered(false);
    }

    public void quitarBotonGuardar() {
        bot_guardar.setRendered(false);
    }

    public void quitarBotonEliminar() {
        bot_eliminar.setRendered(false);
    }

    public void quitarBotonsNavegacion() {
        bot_inicio.setRendered(false);
        bot_fin.setRendered(false);
        bot_siguiente.setRendered(false);
        bot_atras.setRendered(false);
    }

    public void agregarBoton(Boton boton) {
        grupo_botones.getChildren().add(new Espacio("5", "0"));
        grupo_botones.getChildren().add(boton);
    }

    public void agregarComponente(UIComponent componente) {
        grupo_botones.getChildren().add(new Espacio("5", "0"));
        //Si es combo se le alinea en el medio
        if (componente.getRendererType() != null && componente.getRendererType().equals("org.primefaces.component.SelectOneMenuRenderer")) {
            ((Combo) componente).setStyleClass("alinear");
        }

        grupo_botones.getChildren().add(componente);
    }

    public void agregarSeparador() {
        grupo_botones.getChildren().add(new Separator());
    }

    public void agregarReporte() {
        bot_reporte.setId("bot_reporte");
        bot_reporte.setTitle("Reportes");
        bot_reporte.setMetodo("abrirListaReportes");
        bot_reporte.setUpdate("rep_reporte");
        bot_reporte.setIcon("ui-reporte");
        gri_botones_framework.getChildren().add(bot_reporte);
        gri_botones_framework.getChildren().add(new Espacio("5", "0"));
    }

    public void agregarCalendario() {
        bot_calendario.setId("bot_calendario");
        bot_calendario.setTitle("Rango de Fechas");
        bot_calendario.setMetodo("abrirRangoFecha");
        bot_calendario.setUpdate("sec_calendario");
        bot_calendario.setIcon("ui-calendario");
        gri_botones_framework.getChildren().add(bot_calendario);
        gri_botones_framework.getChildren().add(new Espacio("5", "0"));
    }

    public void limpiar() {
        grupo_botones.getChildren().clear();
        gri_botones_framework.getChildren().clear();
        grupo_botones.getChildren().add(gri_botones_framework);
    }

    public Boton getBot_eliminar() {
        return bot_eliminar;
    }

    public void setBot_eliminar(Boton bot_eliminar) {
        this.bot_eliminar = bot_eliminar;
    }

    public Boton getBot_guardar() {
        return bot_guardar;
    }

    public void setBot_guardar(Boton bot_guardar) {
        this.bot_guardar = bot_guardar;
    }

    public Boton getBot_insertar() {
        return bot_insertar;
    }

    public void setBot_insertar(Boton bot_insertar) {
        this.bot_insertar = bot_insertar;
    }

    public Boton getBot_reporte() {
        return bot_reporte;
    }

    public void setBot_reporte(Boton bot_reporte) {
        this.bot_reporte = bot_reporte;
    }

    public Boton getBot_atras() {
        return bot_atras;
    }

    public void setBot_atras(Boton bot_atras) {
        this.bot_atras = bot_atras;
    }

    public Boton getBot_fin() {
        return bot_fin;
    }

    public void setBot_fin(Boton bot_fin) {
        this.bot_fin = bot_fin;
    }

    public Boton getBot_inicio() {
        return bot_inicio;
    }

    public void setBot_inicio(Boton bot_inicio) {
        this.bot_inicio = bot_inicio;
    }

    public Boton getBot_siguiente() {
        return bot_siguiente;
    }

    public void setBot_siguiente(Boton bot_siguiente) {
        this.bot_siguiente = bot_siguiente;
    }

    public ToolbarGroup getGrupoBotones() {
        return grupo_botones;
    }

    public Boton getBot_calendario() {
        return bot_calendario;
    }

    public void setBot_calendario(Boton bot_calendario) {
        this.bot_calendario = bot_calendario;
    }
}
