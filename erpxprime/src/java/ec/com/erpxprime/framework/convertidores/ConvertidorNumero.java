/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.convertidores;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author xedushx Fernando Jácome
 */
@FacesConverter("ConvertidorNumero")
public class ConvertidorNumero implements Converter {

    private int decimales=2;
    private StringBuilder formato = new StringBuilder("#.##");   

    public ConvertidorNumero() {
    }

    public ConvertidorNumero(int decimales) {
        this.decimales=decimales;
        formato.delete(0, formato.length()).append("#");
        for (int i = 0; i < decimales; i++) {
            if (i == 0) {
                formato.append(".");
            }
            formato.append("#");
        }
    }

    
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        return submittedValue;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value != null) {
            DecimalFormat formatoNumero;
            DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
            idfs_simbolos.setDecimalSeparator('.');
            formatoNumero = new DecimalFormat(formato.toString(), idfs_simbolos);
            formatoNumero.setMaximumFractionDigits(decimales);
            formatoNumero.setMinimumFractionDigits(decimales);
            try {
                double ldob_valor = Double.parseDouble(value.toString());
                return formatoNumero.format(ldob_valor);
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public int getDecimales() {
        return decimales;
    }

    public void setDecimales(int decimales) {
        this.decimales = decimales;
    }
    
    
    
}
