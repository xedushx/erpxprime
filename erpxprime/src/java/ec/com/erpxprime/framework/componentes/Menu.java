/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.util.List;
import org.primefaces.component.menubar.Menubar;
import org.primefaces.component.submenu.Submenu;

/**
 *
 * @author xedushx 
 */
public class Menu extends Menubar {

    public Menu() {
        //Para la pantalla de Inicio
        ItemMenu mei_item = new ItemMenu();
        mei_item.setIcon("ui-icon-home");
        mei_item.setMetodoRuta("mbe_index.cargar_inicio");
        mei_item.setMenuUpdate("dibuja,:fortitulo");
        mei_item.setTitle("Inicio");
        mei_item.setOnclick("dimensionesDisponibles()");
        this.getChildren().add(mei_item);
        this.setAutoDisplay(false);

        ItemMenu mei_ayuda = new ItemMenu();
        mei_ayuda.setIcon("ui-icon-help");
        mei_ayuda.setMetodoRuta("mbe_index.ayuda");
        mei_ayuda.setTitle("Ayuda");
        this.getChildren().add(mei_ayuda);

        ItemMenu mei_cambia_sucursal = new ItemMenu();
        mei_cambia_sucursal.setIcon("ui-icon-transferthick-e-w");
        mei_cambia_sucursal.setMetodoRuta("mbe_index.cambiarSucursal");
        mei_cambia_sucursal.setTitle("Cambiar Sucursal");
        mei_cambia_sucursal.setValueExpression("rendered", "mbe_index.dia_sucu_usuario !=null");
        this.getChildren().add(mei_cambia_sucursal);
    }

    public void dibujar() {
        Framework framework = new Framework();
        List lista = framework.getConexion().consultar("SELECT a.id_opcion,a.nombre,a.id_opcion_2,\n"
                + "(SELECT count(id_opcion_2) from tbl_opcion where id_opcion_2=a.id_opcion)\n"
                + "FROM tbl_opcion a ,tbl_perfil_opcion b\n"
                + "WHERE a.id_opcion=b.id_opcion \n"
                + "AND b.id_perfil= "+framework.getVariable("id_perfil")+" "
                + "ORDER BY a.id_opcion_2 desc,nombre");
        for (Object actual : lista) {
            Object[] fila = (Object[]) actual;
            //Los que son padres
            if (fila[2] == null) {
                //Agrega Menus principales

                int numHijos = 0;
                try {
                    numHijos = Integer.parseInt(String.valueOf(fila[3]));
                } catch (Exception e) {
                }
                //Si tiene hijos busca los hijos
                if (numHijos > 0) {
                    Submenu sub_menu = new Submenu();
                    sub_menu.setLabel(fila[1] + "");
                    this.getChildren().add(sub_menu);
                    formar_menu_recursivo(sub_menu, fila, lista);
                } else {
                    ItemMenu mei_item = new ItemMenu();
                    mei_item.setValue(fila[1]);
                    mei_item.setCodigo(fila[0] + "");
                    mei_item.setActionListenerRuta("mbe_index.cargar");
                    mei_item.setMenuUpdate("dibuja,:fortitulo");
                    mei_item.setIcon("ui-pantalla");
                    mei_item.setOnclick("dimensionesDisponibles()");
                    this.getChildren().add(mei_item);
                }
            } else {
                break;
            }
        }
    }

    private void formar_menu_recursivo(Submenu sub_menu, Object[] fila, List lista) {
        //Si no tiene hijos        
        boolean encontro = false;
        for (Object actual : lista) {
            //Busca los hijos            
            Object[] filaActual = (Object[]) actual;
            //Optimiza si ya encontro y el siguiente registro es diferente, fin de la busqueda
            if (encontro) {
                if (!fila[0].equals(filaActual[2])) {
                    break;
                }
            }

            if (fila[0].equals(filaActual[2])) {
                encontro = true;
                //System.out.print("Busca : " + fila[0] + "   " + filaActual[2]);
                int numHijos = 0;
                try {
                    numHijos = Integer.parseInt(String.valueOf(filaActual[3]));
                } catch (Exception e) {
                }
                //Si tiene hijos busca los hijos                
                if (numHijos > 0) {
                    Submenu sub_menu_nuevo = new Submenu();
                    sub_menu_nuevo.setLabel(filaActual[1] + "");
                    sub_menu.getChildren().add(sub_menu_nuevo);
                    formar_menu_recursivo(sub_menu_nuevo, filaActual, lista);
                } else {
                    ItemMenu mei_item = new ItemMenu();
                    mei_item.setValue(filaActual[1]);
                    mei_item.setCodigo(filaActual[0] + "");
                    mei_item.setActionListenerRuta("mbe_index.cargar");
                    mei_item.setMenuUpdate("dibuja,:fortitulo");
                    mei_item.setIcon("ui-pantalla");
                    mei_item.setOnclick("dimensionesDisponibles()");
                    sub_menu.getChildren().add(mei_item);
                }
            }
        }
    }

}
