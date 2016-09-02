package ec.com.erpxprime.framework.convertidores;

import ec.com.erpxprime.framework.componentes.AutoCompletar;
import ec.com.erpxprime.framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author xedushx
 */
@FacesConverter("ConvertidorAutoCompletar")
public class ConvertidorAutoCompletar implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                String value = submittedValue;

                List lis_opciones = new ArrayList();
                try {
                    Tabla tab_tabla = (Tabla) component.getParent().getParent();
                    lis_opciones = tab_tabla.getColumna(component.getId().substring(0, component.getId().lastIndexOf("_"))).getListaCombo();
                
                } catch (Exception e) {
                    try {
                        lis_opciones = ((AutoCompletar) component).getLista();
                    } catch (Exception e1) {
                    }
                }
                for (Object lis_opcione : lis_opciones) {
                    Object[] fila = (Object[]) lis_opcione;                 
                    if (fila[1] != null) {
                        String f = fila[0] + "";
                        if (f.trim().equals(value)) {
                            return fila;
                        }
                    }
                }
            } catch (Exception exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid "));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {        
        if (value == null) {
            return null;
        } else {
            try {                
                String v = (((Object[]) value)[0]) + "";                                
                return v;
            } catch (Exception e) {
            }
        }
        return null;
    }
}
