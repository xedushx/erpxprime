/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.faces.component.UIComponent;
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.component.dialog.Dialog;

/**
 *
 * @author xedushx 
 */
public class Dialogo extends Dialog {

    protected Framework framework = new Framework();
    private Grid gri_cuerpo = new Grid();
    private Grupo gru_botones = new Grupo();
    private Boton bot_aceptar = new Boton();
    private Boton bot_cancelar = new Boton();

    private int altoPanel = 0;
    private int anchoPanel = 0;
    private boolean aux_dibujo = false;

    public Dialogo() {
        this.setShowEffect("fade");
        this.setStyle("overflow: hidden;");
        this.setHideEffect("fade");
        this.setWidth("60%");
        this.setHeight("50%");
        this.setClosable(false);
        this.setVisible(false);
        this.setModal(true);
        this.setDynamic(true);
        this.getChildren().add(gri_cuerpo);
        gri_cuerpo.setWidth("100%");
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setStyle("font-weight: bold;");
        bot_aceptar.setIcon("ui-icon-check");

        bot_aceptar.setStyleClass("pull-right");
        bot_cancelar.setValue("Cancelar");
        bot_cancelar.setIcon("ui-icon-close");
        bot_cancelar.setStyleClass("pull-left");
        Espacio esp_espacio = new Espacio();
        esp_espacio.setWidth("30");
        esp_espacio.setHeight("0");
        gru_botones.setStyleClass("ui-widget-header ui-corner-all");
        gru_botones.setStyle("height:25px;width:97%;padding-top: 2px;padding-bottom: 2px;display:block;padding-left: 10px;padding-right: 10px");
        this.getChildren().add(gru_botones);
        gru_botones.getChildren().add(bot_aceptar);
        gru_botones.getChildren().add(esp_espacio);
        gru_botones.getChildren().add(bot_cancelar);

    }

    public void dibujar() {

        if (!aux_dibujo) {
            gru_botones.setId(this.getId() + "_panelbotones");
            if (bot_aceptar.isRendered()) {
                BlockUI bloc = new BlockUI();
                bloc.setBlock(gru_botones.getId());
                bloc.setWidgetVar(gru_botones.getId());
                this.getChildren().add(bloc);
            }

            bot_aceptar.setOnstart(gru_botones.getId() + ".show();");
            bot_aceptar.setOncomplete(gru_botones.getId() + ".hide();");
            aux_dibujo = true;
        }
        if (bot_cancelar.getActionExpression() == null) {
            bot_cancelar.setActionListenerRuta("mbe_index.cerrarDialogo");
        }
        gri_cuerpo.setStyle("height:" + getAltoPanel() + "px;overflow: hidden;display: block;");
        this.setVisible(true);
        framework.addUpdate(this.getId());
    }

    public void cerrar() {
        this.setVisible(false);
        framework.addUpdate(this.getId());
    }

    public void setDialogo(UIComponent componente) {
        gri_cuerpo.getChildren().add(componente);
    }

    @Override
    public void setWidth(String _width) {
        double dou_width = 0;
        double dou_ancho = 1000;
        try {
            dou_ancho = Double.parseDouble(framework.getVariable("ANCHO"));
        } catch (Exception e) {
            dou_ancho = 1024;
        }
        try {
            _width = _width.replaceAll("%", "");
            dou_width = Double.parseDouble(_width);
            dou_width = ((dou_width) * dou_ancho / 100);
        } catch (Exception e) {
            dou_width = 1024;
        }
        anchoPanel = ((int) dou_width - 25);
        super.setWidth((int) dou_width + "");
    }

    @Override
    public void setHeight(String _height) {
        double dou_height = 0;
        double dou_alto = 730;
        try {
            dou_alto = Double.parseDouble(framework.getVariable("ALTO"));
        } catch (Exception e) {
            dou_alto = 768;
        }
        try {
            _height = _height.replaceAll("%", "");
            dou_height = Double.parseDouble(_height);

            dou_height = ((dou_height) * dou_alto / 100);
        } catch (Exception e) {
            dou_height = 768;
        }
        altoPanel = ((int) dou_height - 35);
        super.setHeight((int) dou_height + "");
    }

    public int getAltoPanel() {
        return altoPanel;
    }

    public int getAnchoPanel() {
        return anchoPanel;
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

    public Grupo getGru_botones() {
        return gru_botones;
    }

    public void setGru_botones(Grupo gru_botones) {
        this.gru_botones = gru_botones;
    }

    public void setTitle(String titulo) {
        this.setHeader(titulo.toUpperCase());
    }

    public void Limpiar() {
        gri_cuerpo.getChildren().clear();
    }

    public Grid getGri_cuerpo() {
        return gri_cuerpo;
    }

    public void setGri_cuerpo(Grid gri_cuerpo) {
        this.gri_cuerpo = gri_cuerpo;
    }

}
