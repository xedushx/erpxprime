/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.io.IOException;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author xedushx 
 */
public class Grupo extends HtmlPanelGroup {

    private boolean dibuja = false;

    public Grupo() {
        this.setLayout("block");
    }

    public boolean isDibuja() {
        return dibuja;
    }

    public void setDibuja(boolean dibuja) {
        this.dibuja = dibuja;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (dibuja) {
            if (this.getStyle() == null || this.getStyle().isEmpty()) {
                this.setStyle("width: 100%; overflow: hidden;border: 1px solid #d8dce6;");
            }
            String id = this.getClientId();
            id = id.replace(":", "\\\\:");
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("script", this);
            writer.write(" var na = $(window).height() - 20;");
            writer.write("jQuery(function($){ $('#" + id + "').css('height',na+ 'px'); });");
            writer.endElement("script");
        }

        super.encodeBegin(context);
    }
}
