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
public class Alerta extends HtmlPanelGroup {
    
    String mensaje;
    
    public Alerta() {
        this.setLayout("block");
        this.getPassThroughAttributes().put("role", "alert");
    }
    
    public void setAlertaVerde(String mensaje) {
        dibujar("alert alert-success", mensaje);
    }
    
    public void setAlertaCeleste(String mensaje) {
        dibujar("alert alert-info", mensaje);
    }
    
    public void setAlertaNaranja(String mensaje) {
        dibujar("alert alert-warning", mensaje);
    }
    
    public void setAlertaRoja(String mensaje) {
        dibujar("alert alert-danger", mensaje);
    }
    
    private void dibujar(String tipo, String mensaje) {
        this.getChildren().clear();
        this.setStyleClass(tipo);
        HtmlOutputText etiMensaje = new HtmlOutputText();
        etiMensaje.setEscape(false);
        etiMensaje.setStyle("font-size: 12px;  ");
        etiMensaje.setValue(mensaje);
        this.getChildren().add(etiMensaje);
    }
    
}
