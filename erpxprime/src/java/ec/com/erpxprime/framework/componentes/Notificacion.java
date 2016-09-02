/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import org.primefaces.component.notificationbar.NotificationBar;


/**
 *
 * @author xedushx Fernando Jácome
 */
public class Notificacion extends NotificationBar {

    private Etiqueta eti_titulo = new Etiqueta();
    private Etiqueta eti_mensaje = new Etiqueta();
    private Imagen ima_icono = new Imagen();
    private Link lin_cerrar = new Link();

    public Notificacion() {
        this.setStyle("background: rgba(0,0,0,0.8);color: white;height:85px; z-index: 5;");
        this.setId("not_notificacion");
        this.setWidgetVar("not_notificacion");
        this.setEffect("slide");


        Grid gri_grid = new Grid();
        eti_titulo.setValue("Titulo  ");
        eti_titulo.setStyle("font-size: 15px;color:white");
        gri_grid.setHeader(eti_titulo);
        gri_grid.setColumns(2);
        ima_icono.setWidth("64");
        ima_icono.setHeight("64");
        ima_icono.setValue("imagenes/im_info.png");
        gri_grid.getChildren().add(ima_icono);
        eti_mensaje.setStyle("font-size: 12px;;color:white");
        eti_mensaje.setValue("");
        gri_grid.getChildren().add(eti_mensaje);
        this.getChildren().add(gri_grid);
        Imagen ima_cerrar = new Imagen();
        lin_cerrar.setStyle("position:absolute;right:3em; top:1em;  cursor:pointer;");
        ima_cerrar.setValue("imagenes/im_cerrar.png");
        lin_cerrar.getChildren().add(ima_cerrar);
        lin_cerrar.setOncomplete("not_notificacion.hide();");
        this.getChildren().add(lin_cerrar);
    }

    public void setNotificacion(String titulo, String mensje, String pathImagen) {
        eti_titulo.setValue(titulo);
        eti_mensaje.setValue(mensje);
        if (pathImagen != null && !pathImagen.isEmpty()) {
            ima_icono.setValue(pathImagen);
        }
    }
}
