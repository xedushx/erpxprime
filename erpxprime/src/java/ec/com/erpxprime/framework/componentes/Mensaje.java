/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import org.primefaces.component.dialog.Dialog;

/**
 *
 * @author xedushx
 */
public class Mensaje extends Dialog {

    private String titulo;
    private String mensaje;

    public Mensaje() {
        this.setWidth("450");
        this.setHeight("200");
        this.setShowEffect("fade");
        this.setHideEffect("fade");
        this.setClosable(true);
        this.setModal(true);
        this.setResizable(false);
        this.setPosition("center");

    }

    public void setMensajeError(String titulo, String mensaje) {
        this.mensaje = mensaje;
        this.titulo = titulo;
        this.getChildren().add(new Etiqueta(construir("fa fa-times-circle", "text-danger")));

    }

    public void setMensajeExito(String titulo, String mensaje) {
        this.mensaje = mensaje;
        this.titulo = titulo;
        this.getChildren().add(new Etiqueta(construir("fa fa-check-circle", "text-success")));
    }

    public void setMensajeInfo(String titulo, String mensaje) {
        this.mensaje = mensaje;
        this.titulo = titulo;
        this.getChildren().add(new Etiqueta(construir("fa fa-info-circle", "text-info")));
    }

    public void setMensajeAdvertencia(String titulo, String mensaje) {
        this.mensaje = mensaje;
        this.titulo = titulo;
        this.getChildren().add(new Etiqueta(construir("fa fa-exclamation-circle", "text-warning")));
    }

    private String construir(String icono, String tipo) {
        this.setWidgetVar(this.getId() + "_wv");
        this.getChildren().clear();
        this.setHeader(titulo);
        return "<div style='width: 397px;font-size: 13px;height:167px;  vertical-align: middle;'> "
                + "  <table class='" + tipo + "'  style='width: 397px;font-size: 14px;height:167px;  vertical-align: middle;'> "
                + "    <tr style='vertical-align: middle;'> "
                + "    	<td><i class='" + icono + " fa-5x'></i> </td>"
                + "    	<td><p style='padding-left: 5px;'> " + mensaje + "</p> </td>"
                + "    </tr> "
                + "  </table>"
                + "</div> ";
    }

    public void dibujar() {
        Framework framework = new Framework();
        framework.addUpdate(this.getId());
        framework.ejecutarJavaScript(this.getWidgetVar() + ".show();");
    }
}
