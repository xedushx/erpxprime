/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.bootstrap;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author xedushx
 */
public class RowBootstrap extends HtmlPanelGroup {

    private final Resource res_estilo = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("bootstrap.min.css", "components/bootstrap/dist/css");

    public RowBootstrap() {
        this.setLayout("block");
        this.setStyleClass("row");
    }

    public void agregarComponente(UIComponent componente) {
        this.getChildren().add(componente);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        //importo el script y el estilo del thumbnail
        Map<Object, Object> contextMap = context.getAttributes();
        ResponseWriter writer = context.getResponseWriter();
        String key = res_estilo.getResourceName() + res_estilo.getLibraryName();
        if (!contextMap.containsKey(key)) {
            contextMap.put(key, Boolean.TRUE);
            writer.startElement("link", null);
            writer.writeAttribute("type", "text/css", "type");
            writer.writeAttribute("rel", "Stylesheet", "rel");
            writer.writeURIAttribute("href",
                    ((res_estilo != null) ? res_estilo.getRequestPath()
                            : "RES_NOT_FOUND"), "href");
            writer.endElement("link");
        }

        super.encodeBegin(context); //To change body of generated methods, choose Tools | Templates.
    }

}
