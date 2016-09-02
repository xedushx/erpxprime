/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import javax.faces.component.UIParameter;
import org.primefaces.component.effect.Effect;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Efecto extends Effect {

    public Efecto() {
        this.setEvent("load");
        this.setType("pulsate");
    }

    public void setPropiedad(String nombre, String valor) {
        UIParameter par_efect = new UIParameter();
        par_efect.setName(nombre);
        if (valor.indexOf("'") < 0) {
            valor = "'" + valor + "'";
        }
        par_efect.setValue(valor);
        this.getChildren().add(par_efect);
    }
}
