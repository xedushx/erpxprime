/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.bootstrap;

import ec.com.erpxprime.framework.componentes.Etiqueta;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;

/**
 *
 * @author xedushxFERNANDOJACOMEG
 */
public class PanelBootstrap extends HtmlPanelGroup {

    private HtmlPanelGroup header = new HtmlPanelGroup();
    private HtmlPanelGroup content = new HtmlPanelGroup();
    private HtmlPanelGroup footer = new HtmlPanelGroup();

    public PanelBootstrap() {
        this.setLayout("block");
        this.setStyleClass("panel panel-default");

        header.setLayout("block");
        header.setStyleClass("panel-heading");
        header.setRendered(false);
        this.getChildren().add(header);

        content.setLayout("block");
        content.setStyleClass("panel-body");
        this.getChildren().add(content);

        footer.setLayout("block");
        footer.setStyleClass("panel-footer");
        footer.setRendered(false);
        this.getChildren().add(footer);

    }

    public void setPanelVerde() {
        this.setStyleClass("panel panel-success");
    }

    public void setPanelAzul() {
        this.setStyleClass("panel panel-primary");
    }

    public void setPanelCeleste() {
        this.setStyleClass("panel panel-info");
    }

    public void setPanelNaranja() {
        this.setStyleClass("panel panel-warning");
    }

    public void setPanelRojo() {
        this.setStyleClass("panel panel-danger");
    }

    public void setTitulo(String titulo) {
        Etiqueta eti_titulo = new Etiqueta();
        eti_titulo.setValue(titulo);
        header.setRendered(true);
        header.getChildren().add(eti_titulo);
    }

    /**
     * Agregar componente en el Contenido del Panel
     *
     * @param componente
     */
    public void agregarComponenteContenido(UIComponent componente) {
        content.getChildren().add(componente);
    }

    /**
     * Agregar componente en el Panel
     *
     * @param componente
     */
    public void agregarComponente(UIComponent componente) {
        this.getChildren().add(componente);
    }

    /**
     * Agregar componente en el Footer del Panel
     *
     * @param componente
     */
    public void agregarComponenteFooter(UIComponent componente) {
        footer.setRendered(true);
        footer.getChildren().add(componente);
    }

    public HtmlPanelGroup getHeader() {
        return header;
    }

    public void setHeader(HtmlPanelGroup header) {
        this.header = header;
    }

    public HtmlPanelGroup getContent() {
        return content;
    }

    public void setContent(HtmlPanelGroup content) {
        this.content = content;
    }

    public HtmlPanelGroup getFooter() {
        return footer;
    }

    public void setFooter(HtmlPanelGroup footer) {
        this.footer = footer;
    }
    

}
