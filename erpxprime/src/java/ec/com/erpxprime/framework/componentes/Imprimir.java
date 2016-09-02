/*
 * Copyright (c) 2013, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.commandbutton.CommandButton;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Imprimir extends CommandButton {

    private String nombreComponente = null;
    private Resource res_script = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("printer/printer.js", "primefaces");

    public Imprimir() {
        this.setIcon("ui-icon-print");
        this.setValue("Imprimir");
        this.setType("button");
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        setTitle(value + "");
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (nombreComponente != null) {
            UIComponent targetComponent = FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:" + nombreComponente);
            if (targetComponent != null) {
                this.setOnclick("$(PrimeFaces.escapeClientId('" + targetComponent.getClientId() + "')).jqprint();return false;");
            } else {
                this.setOnclick(null);
                nombreComponente = null;
                System.out.println("Error no se encuentra el componente " + nombreComponente);
            }
        }
        if (nombreComponente != null) {
            ResponseWriter writer = context.getResponseWriter();
            Map<Object, Object> contextMap = context.getAttributes();
            String key = res_script.getResourceName() + res_script.getLibraryName();
            if (!contextMap.containsKey(key)) {
                contextMap.put(key, Boolean.TRUE);
                writer.startElement("script", null);
                writer.writeAttribute("type", "text/javascript", "type");
                writer.writeURIAttribute("src",
                        ((res_script != null) ? res_script.getRequestPath()
                        : "RES_NOT_FOUND"), "src");
                writer.endElement("script");
            }
        }
        super.encodeBegin(context);
    }

    public String getNombreComponente() {
        return nombreComponente;
    }

    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }
}
