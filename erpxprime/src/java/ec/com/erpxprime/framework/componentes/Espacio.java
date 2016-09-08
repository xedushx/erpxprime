/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import org.primefaces.component.spacer.Spacer;

/**
 *
 * @author xedushx 
 */
public class Espacio extends Spacer {

    public Espacio() {
        this.setWidth("1");
        this.setHeight("1");
    }

    public Espacio(String ancho, String alto) {
        this.setWidth(ancho);
        this.setHeight(alto);
    }

    public void setEspacio(String ancho, String alto) {
        this.setWidth(ancho);
        this.setHeight(alto);
    }
}
