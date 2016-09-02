/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.bootstrap;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;

/**
 *
 * @author xedushx
 */
public class ContenidoBootstrap extends HtmlPanelGroup {

    public ContenidoBootstrap(String styleClass) {
        this.setLayout("block");
        this.setStyleClass(styleClass);

    }

    public void agregarComponente(UIComponent componente) {
        this.getChildren().add(componente);
    }

}
