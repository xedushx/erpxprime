/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.bootstrap;
import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.framework.componentes.Link;
import javax.faces.component.html.HtmlOutputText;

/**
 *
 * @author xedushxFERNANDOJACOMEG
 */
public class BotonBootstrap extends Link {

    public BotonBootstrap() {
        this.setStyleClass("btn btn-default text-white");
    }

    public void setBotonVerde() {
        this.setStyleClass("btn btn-success text-white");
    }

    public void setBotonAzul() {
        this.setStyleClass("btn btn-primary text-white");
    }

    public void setBotonCeleste() {
        this.setStyleClass("btn btn-info text-white");
    }

    public void setBotonNaranja() {
        this.setStyleClass("btn btn-warning text-white");
    }

    public void setBotonRojo() {
        this.setStyleClass("btn btn-danger text-white");
    }

    @Override
    public void setValue(Object value) {
        HtmlOutputText txtValor = new HtmlOutputText();
        txtValor.setEscape(false);
        txtValor.setStyleClass("text-white");
        txtValor.setValue("&nbsp;" + value);
        this.getChildren().add(txtValor);
    }

    @Override
    public void setUpdate(String _update) {
        Framework framework = new Framework();
        super.setUpdate(framework.formarUpdate(_update));
    }

    public void setBotonUpdate(String _update) {
        super.setLinkUpdate(_update);
    }

}
