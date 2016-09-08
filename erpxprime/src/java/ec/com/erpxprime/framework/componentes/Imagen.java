/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.convertidores.UploadStreamedContent;
import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author xedushx 
 */
public class Imagen extends GraphicImage {

    public Imagen() {
        this.setCache(true);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {        
        try {
            ((DefaultStreamedContent) this.getValue()).getStream().reset();
        } catch (Exception e) {
            try {
                ((UploadStreamedContent) this.getValue()).getStream().reset();
            } catch (Exception e1) {
            }
        }
        super.encodeBegin(context);
    }

}
