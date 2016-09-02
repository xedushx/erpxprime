/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.bootstrap;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;

/**
 *
 * @author xedushx
 */
public class CajaBootstrap extends HtmlPanelGroup {

    HtmlPanelGroup caja = new HtmlPanelGroup();
    HtmlOutputText icono = new HtmlOutputText();
    HtmlOutputText contenido = new HtmlOutputText();

    public CajaBootstrap() {
        this.setLayout("block");
        this.setStyleClass("col-md-3 col-sm-6 col-xs-12"); //tamanio por defecto
        this.getChildren().add(caja);
        caja.setLayout("block");
        caja.setStyleClass("info-box");
        icono.setEscape(false);
        this.icono.setStyle("info-box-icon bg-aqua");
        this.icono.setValue("<i class='fa fa-cogs'></i>");
        caja.getChildren().add(icono);
        contenido.setEscape(false);
        caja.getChildren().add(contenido);
    }

    public void setIcono(String icono, String color) {
        this.icono.setStyleClass("info-box-icon text-white " + color);
        this.icono.setValue("<i class='" + icono + "'></i>");
    }

    public void setCajaBootstrap(String titulo, String mensaje) {
        this.contenido.setValue(" <div class='info-box-content'>\n"
                + "<span class='info-box-text'>" + titulo + "</span>\n"
                + "<span class='info-box-number'>" + mensaje + "</span>\n"
                + "</div>");
    }

    public HtmlOutputText getContenido() {
        return contenido;
    }

}
