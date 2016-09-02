/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import org.primefaces.component.contextmenu.ContextMenu;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class MenuContextual extends ContextMenu {

    private ItemMenu item_insertar = new ItemMenu();
    private ItemMenu item_guardar = new ItemMenu();
    private ItemMenu item_guardar2 = new ItemMenu();
    private ItemMenu item_eliminar = new ItemMenu();
    private ItemMenu item_buscar = new ItemMenu();
    private ItemMenu item_actualizar = new ItemMenu();
    private ItemMenu item_excel = new ItemMenu();
    private ItemMenu item_importar = new ItemMenu();
    private ItemMenu item_formato = new ItemMenu();
    private Submenu sub_otros = new Submenu();
    private Framework framework=new Framework();

    public void crearMenuTabla() {
        item_insertar.setValue("Insertar");
        item_insertar.setIcon("ui-icon-document");
        item_insertar.setMetodo("insertar");

        item_insertar.setValueExpression("rendered", "mbe_index.clase.bar_botones.bot_insertar.rendered");
        item_insertar.setValueExpression("disabled", "mbe_index.clase.bar_botones.bot_insertar.disabled");
        this.getChildren().add(item_insertar);
        item_guardar.setValue("Guardar");
        item_guardar.setIcon("ui-icon-disk");
        item_guardar.setMetodo("guardar");        
        

        item_guardar.setValueExpression("rendered", "mbe_index.clase.bar_botones.bot_guardar.rendered and mbe_index.clase.bar_botones.bot_guardar.actionExpression != null");
        item_guardar.setValueExpression("disabled", "mbe_index.clase.bar_botones.bot_guardar.disabled");
        this.getChildren().add(item_guardar);

        item_guardar2.setOnclick("con_guarda.show();");
        item_guardar2.setValue("Guardar");
        item_guardar2.setIcon("ui-icon-disk");
        item_guardar2.setValueExpression("rendered", "mbe_index.clase.bar_botones.bot_guardar.actionExpression == null");
        item_guardar2.setValueExpression("disabled", "mbe_index.clase.bar_botones.bot_guardar.disabled");
        this.getChildren().add(item_guardar2);

        item_eliminar.setValue("Eliminar");
        item_eliminar.setIcon("ui-icon-close");
        item_eliminar.setMetodo("eliminar");

        item_eliminar.setValueExpression("rendered", "mbe_index.clase.bar_botones.bot_eliminar.rendered");
        item_eliminar.setValueExpression("disabled", "mbe_index.clase.bar_botones.bot_eliminar.disabled");
        this.getChildren().add(item_eliminar);
        item_buscar.setValue("Buscar");
        item_buscar.setIcon("ui-icon-search");
        item_buscar.setMetodo("buscar");
       // item_buscar.setValueExpression("rendered", "mbe_index.clase.bar_botones.bot_eliminar.rendered");
       // item_buscar.setValueExpression("disabled", "mbe_index.clase.bar_botones.bot_eliminar.disabled");
        this.getChildren().add(item_buscar);

        item_actualizar.setValue("Actualizar");
        item_actualizar.setIcon("ui-icon-refresh");
        item_actualizar.setMetodo("actualizar");

        this.getChildren().add(item_actualizar);

        Separator separar = new Separator();
        this.getChildren().add(separar);


        sub_otros.setLabel("Otras");
        sub_otros.setIcon("ui-icon-carat-1-e");


        item_excel.setAjax(false);
        item_excel.setIcon("ui-icon-calculator");
        item_excel.setValue("Exportar a XLS");
        item_excel.setTarget("_blank");
        item_excel.setMetodo("exportarXLS");
        sub_otros.getChildren().add(item_excel);

        item_importar.setIcon("ui-icon-transferthick-e-w");
        item_importar.setValue("Importar XLS");
        item_importar.setMetodo("importarXLS");
        sub_otros.getChildren().add(item_importar);

        item_formato.setValue("Formato Tabla");
        item_formato.setIcon("ui-icon-contact");
        item_formato.setMetodo("formatoTabla");
        sub_otros.getChildren().add(item_formato);
        ItemMenu item_terminal=new ItemMenu();
        item_terminal.setValue("Terminal");
        item_terminal.setIcon("ui-icon-gear");
        item_terminal.setMetodo("terminal");
        sub_otros.getChildren().add(item_terminal);
        
        this.getChildren().add(sub_otros);
        
        //Permiso a componentes especiales
        String str_permiso=framework.getVariable("PERM_UTIL_PERF");
        if(str_permiso==null || str_permiso.equals("false") ){
            item_formato.setRendered(false);
            item_terminal.setRendered(false);
        }
    }

    public void quitarItemEliminar() {
        item_eliminar.setRendered(false);
    }

    public void quitarItemGuardar() {
        item_guardar.setRendered(false);
    }

    public void quitarSubmenuOtros() {
        sub_otros.setRendered(false);
    }

    public void quitarItemInsertar() {
        item_insertar.setRendered(false);
    }

    public void quitarItemActualizar() {
        item_actualizar.setRendered(false);
    }

    public void quitarItemExcel() {
        item_excel.setRendered(false);
    }

    public void quitarItemImportar() {
        item_importar.setRendered(false);
    }

    public ItemMenu getItem_eliminar() {
        return item_eliminar;
    }

    public void setItem_eliminar(ItemMenu item_eliminar) {
        this.item_eliminar = item_eliminar;
    }

    public Submenu getSub_otros() {
        return sub_otros;
    }

    public void setSub_otros(Submenu sub_otros) {
        this.sub_otros = sub_otros;
    }

    public ItemMenu getItem_guardar() {
        return item_guardar;
    }

    public void setItem_guardar(ItemMenu item_guardar) {
        this.item_guardar = item_guardar;
    }

    public ItemMenu getItem_insertar() {
        return item_insertar;
    }

    public void setItem_insertar(ItemMenu item_insertar) {
        this.item_insertar = item_insertar;
    }

    public ItemMenu getItem_actualizar() {
        return item_actualizar;
    }

    public void setItem_actualizar(ItemMenu item_actualizar) {
        this.item_actualizar = item_actualizar;
    }

    public ItemMenu getItem_buscar() {
        return item_buscar;
    }

    public void setItem_buscar(ItemMenu item_buscar) {
        this.item_buscar = item_buscar;
    }

    public ItemMenu getItem_excel() {
        return item_excel;
    }

    public void setItem_excel(ItemMenu item_excel) {
        this.item_excel = item_excel;
    }

    public ItemMenu getItem_importar() {
        return item_importar;
    }

    public void setItem_importar(ItemMenu item_importar) {
        this.item_importar = item_importar;
    }

    public ItemMenu getItem_formato() {
        return item_formato;
    }

    public void setItem_formato(ItemMenu item_formato) {
        this.item_formato = item_formato;
    }
    
    
}
