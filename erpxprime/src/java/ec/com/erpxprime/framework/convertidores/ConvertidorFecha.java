/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.convertidores;

import java.text.SimpleDateFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author xedushx Fernando Jácome
 */
@FacesConverter("ConvertidorFecha")
public class ConvertidorFecha implements Converter {

    private String formato="dd-MM-yyyy";

    public ConvertidorFecha() {
    }

    public ConvertidorFecha(boolean hora) {
        if(hora){
         formato="dd-MM-yyyy HH:mm:ss";   
        }
    }
    
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        return submittedValue;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value != null) {
            try {
                SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
                return formatoFecha.format(value);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
