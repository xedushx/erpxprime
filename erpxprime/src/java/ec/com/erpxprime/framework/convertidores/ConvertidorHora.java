/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.convertidores;


import java.text.SimpleDateFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author xedushx 
 */
@FacesConverter("ConvertidorHora")
public class ConvertidorHora extends DateTimeConverter{

   private String formato="HH:mm:ss";

    
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
                return value.toString();
            }
        }
        return null;
    }
}
