/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.bootstrap;

import ec.com.erpxprime.framework.componentes.AutoCompletar;
import ec.com.erpxprime.framework.componentes.Boton;
import ec.com.erpxprime.framework.componentes.Texto;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;

/**
 *
 * @author xedushxFERNANDOJACOMEG
 */
public class GrupoBootstrap extends HtmlPanelGroup {

    public GrupoBootstrap() {
        this.setLayout("block");
        this.setStyleClass("input-group");
    }

    public void setEtiqueta(String texto) {
        HtmlOutputText etiqueta = new HtmlOutputText();
        etiqueta.setStyleClass("input-group-addon");
        etiqueta.setEscape(false);
        etiqueta.setValue(texto);
        this.getChildren().add(etiqueta);
    }

    public void setEtiqueta(String texto, String icono) {
        HtmlOutputText etiqueta = new HtmlOutputText();
        etiqueta.setStyleClass("input-group-addon");
        etiqueta.setEscape(false);
        etiqueta.setValue(texto);
        this.getChildren().add(etiqueta);
        HtmlInputText hico = new HtmlInputText();
        hico.setStyleClass(icono);
        etiqueta.getChildren().add(hico);
    }

    public void setTexto(Texto componente) {
        componente.setStyleClass("form-control");
        this.getChildren().add(componente);
    }

    public void setAutocompletar(AutoCompletar componente) {
        componente.setStyleClass("form-control");
        this.getChildren().add(componente);
    }

    public void setBotonBootstrap(BotonBootstrap componente) {
        HtmlOutputText span = new HtmlOutputText();
        span.setEscape(false);
        span.setValue("<span class=\"input-group-btn\">");
        this.getChildren().add(span);
        this.getChildren().add(componente);
        HtmlOutputText spanclose = new HtmlOutputText();
        spanclose.setEscape(false);
        spanclose.setValue("</span>");
        this.getChildren().add(spanclose);
    }

    public void setBotonBoton(Boton componente) {
        HtmlOutputText span = new HtmlOutputText();
        span.setStyleClass("input-group-btn");
        span.getChildren().add(componente);
        this.getChildren().add(span);
    }

}
