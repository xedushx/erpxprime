/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.editor.Editor;

/**
 *
 * @author xedushx
 */
public class VisualizarTexto extends Dialogo {

    private final Resource res_javascript = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("highlight.pack.js", "highlight");
    private final Resource res_estilo = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("color-brewer.css", "highlight/styles");
    private final Grid gri_cuerpo = new Grid();
    private final Editor eti_texto = new Editor();

    public VisualizarTexto() {
        this.setWidth("80%");
        this.setHeight("80%");
        this.setTitle("Visualizador de Texto");
        this.setResizable(false);
        gri_cuerpo.getChildren().add(eti_texto);
        this.setDialogo(gri_cuerpo);
        eti_texto.setControls("copy print indent outdent");
    }

    public void setVisualizarTexto(String texto, String clase) {
        if (clase == null || clase.isEmpty()) {
            clase = "nohighlight";
        }
        eti_texto.setValue("<pre><code class='" + clase + "'>" + texto + "</code></pre>");
    }

    @Override
    public void dibujar() {
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 1) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        eti_texto.setStyle("width:100%;height:100%");
        super.dibujar(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
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

        key = res_javascript.getResourceName() + res_javascript.getLibraryName();
        if (!contextMap.containsKey(key)) {
            contextMap.put(key, Boolean.TRUE);
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", "type");
            writer.writeURIAttribute("src",
                    ((res_javascript != null) ? res_javascript.getRequestPath()
                            : "RES_NOT_FOUND"), "src");
            writer.endElement("script");
        }
        writer.startElement("script", null);
        writer.writeText("hljs.initHighlightingOnLoad();", null);
        writer.endElement("script");
        super.encodeEnd(context); //To change body of generated methods, choose Tools | Templates.
    }

}
