/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.faces.component.UIComponent;
import org.primefaces.component.layout.Layout;
import org.primefaces.component.layout.LayoutUnit;

/**
 *
 * @author xedushx 
 */
public class Division extends Layout {

    private LayoutUnit llay_panel1;
    private LayoutUnit llay_panel2;
    private LayoutUnit llay_panel3;

    public Division() {
        this.setStyle("overflow:hidden");
        this.setResizeTitle("");
        buscaAlto();
    }

    private void buscaAlto() {
        Framework framework = new Framework();
        String alto = framework.getVariable("ALTO_PANTALLA");
        if (alto == null) {
            alto = "1000";
        }
        this.setStyle("padding: 0;margin: 0;height:" + alto + "px;border:none");
    }

    public void dividir2(UIComponent componente1, UIComponent componente2, String porcentaje, String tipo) {
        buscaAlto();
        llay_panel1 = new LayoutUnit();
        llay_panel1.setStyle("margin: 0;");
        llay_panel1.setGutter(0);
        llay_panel1.setCollapsible(false);
        llay_panel1.setCollapsed(false);

        llay_panel2 = new LayoutUnit();
        llay_panel2.setCollapsible(false);
        llay_panel2.setCollapsed(false);
        llay_panel2.setStyle("margin: 0;");
        llay_panel2.setGutter(0);


        if (tipo.equalsIgnoreCase("V")) {
            llay_panel1.setPosition("west");
            llay_panel1.setResizable(true);
            llay_panel1.setSize(porcentaje);
            llay_panel2.setPosition("center");
        } else {
            llay_panel1.setPosition("north");
            llay_panel1.setResizable(true);
            llay_panel1.setSize(porcentaje);
            llay_panel2.setPosition("center");
        }
        if (componente1 != null) {
            llay_panel1.getChildren().add(componente1);
        }
        if (componente2 != null) {
            llay_panel2.getChildren().add(componente2);
        }
        this.getChildren().add(llay_panel1);
        this.getChildren().add(llay_panel2);
    }

    public void dividir3(UIComponent componente1, UIComponent componente2, UIComponent componente3, String porcentaje1, String porcentaje2, String tipo) {
        buscaAlto();
        llay_panel1 = new LayoutUnit();
        llay_panel1.setStyle("margin: 0;");
        llay_panel1.setGutter(0);
        llay_panel1.setCollapsible(false);
        llay_panel1.setCollapsed(false);
        llay_panel2 = new LayoutUnit();
        llay_panel2.setCollapsible(false);
        llay_panel2.setCollapsed(false);
        llay_panel2.setStyle("margin: 0;");
        llay_panel2.setGutter(0);
        llay_panel3 = new LayoutUnit();
        llay_panel3.setStyle("margin: 0;");
        llay_panel3.setGutter(0);
        llay_panel3.setCollapsible(false);
        llay_panel3.setCollapsed(false);

        if (tipo.equalsIgnoreCase("V")) {
            llay_panel1.setPosition("west");
            llay_panel1.setResizable(true);
            llay_panel1.setSize(porcentaje1);
            llay_panel2.setPosition("center");
            llay_panel3.setSize(porcentaje2);
            llay_panel2.setResizable(true);
            llay_panel3.setPosition("east");
            llay_panel3.setResizable(true);
        } else {
            llay_panel1.setPosition("north");
            llay_panel1.setResizable(true);
            llay_panel1.setSize(porcentaje1);
            llay_panel2.setPosition("center");
            llay_panel2.setResizable(true);
            llay_panel3.setSize(porcentaje2);
            llay_panel3.setPosition("south");
            llay_panel3.setResizable(true);
        }
        if (componente1 != null) {
            llay_panel1.getChildren().add(componente1);
        }
        if (componente2 != null) {
            llay_panel2.getChildren().add(componente2);
        }
        if (componente3 != null) {
            llay_panel3.getChildren().add(componente3);
        }
        this.getChildren().add(llay_panel1);
        this.getChildren().add(llay_panel2);
        this.getChildren().add(llay_panel3);
    }

    public void dividir1(UIComponent componente) {
        buscaAlto();
        llay_panel1 = new LayoutUnit();
        llay_panel1.setPosition("center");
        llay_panel1.setClosable(false);
        llay_panel1.setStyle("margin: 0;");
        llay_panel1.setGutter(0);
        if (componente != null) {
            llay_panel1.getChildren().add(componente);
        }
        this.getChildren().add(llay_panel1);
    }

    public void setFooter(UIComponent componente, UIComponent footer, String porcentaje) {
        buscaAlto();
        llay_panel1 = new LayoutUnit();
        llay_panel1.setStyle("margin: 0;");
        llay_panel1.setGutter(0);
        llay_panel1.setCollapsible(false);
        llay_panel1.setCollapsed(false);
        llay_panel2 = new LayoutUnit();
        llay_panel2.setCollapsible(false);
        llay_panel2.setCollapsed(false);
        llay_panel2.setStyle("margin: 0;");
        llay_panel2.setGutter(0);
        llay_panel1.setPosition("north");
        llay_panel1.setResizable(false);
        llay_panel1.setSize(porcentaje);
        llay_panel2.setPosition("center");
        if (componente != null) {
            llay_panel1.getChildren().add(componente);
        }
        if (footer != null) {
            llay_panel2.getChildren().add(footer);
        }
        this.getChildren().add(llay_panel1);
        this.getChildren().add(llay_panel2);
    }

    public LayoutUnit getDivision1() {
        return llay_panel1;
    }

    public LayoutUnit getDivision2() {
        return llay_panel2;
    }

    public LayoutUnit getDivision3() {
        return llay_panel3;
    }
}
