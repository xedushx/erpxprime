/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import org.primefaces.component.confirmdialog.ConfirmDialog;

/**
 *
 * @author xedushx 
 */
public class Confirmar extends ConfirmDialog {

    private Boton bot_aceptar = new Boton();
    private Boton bot_cancelar = new Boton();
    private Framework framework = new Framework();
    private boolean visible = false;

    public Confirmar() {
        this.setClosable(false);
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setIcon("ui-icon-check");
        bot_cancelar.setValue("Cancelar");
        bot_cancelar.setIcon("ui-icon-close");
        this.getChildren().add(bot_aceptar);
        this.getChildren().add(bot_cancelar);
    }

    @Override
    public void setVisible(boolean _visible) {
        this.visible = _visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void dibujar() {
        if (bot_cancelar.getActionExpression() == null) {
            bot_cancelar.setUpdate(this.getId());
            bot_cancelar.setActionListenerRuta("mbe_index.cerrarConfirmar");
        }
        setVisible(true);
        framework.ejecutarJavaScript(this.getWidgetVar() + ".show();");
    }

    public void cerrar() {
        setVisible(false);
        framework.ejecutarJavaScript(this.getWidgetVar() + ".hide();");
    }

    public Boton getBot_aceptar() {
        return bot_aceptar;
    }

    public void setBot_aceptar(Boton bot_aceptar) {
        this.bot_aceptar = bot_aceptar;
    }

    public Boton getBot_cancelar() {
        return bot_cancelar;
    }

    public void setBot_cancelar(Boton bot_cancelar) {
        this.bot_cancelar = bot_cancelar;
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.setWidgetVar(id);
    }

    public void setTitle(String titulo) {
        this.setHeader(titulo.toUpperCase());
    }
}
