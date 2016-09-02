/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Columna;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class BuscarTabla extends Dialogo {

    private final Texto tex_busqueda_bus = new Texto();
    private final Combo com_campos = new Combo();
    private Tabla tab_busca_bus = new Tabla();
    private final Grid gri_cuerpo = new Grid();
    private final Panel pan_parametro = new Panel();
    private final Grupo pan_resultado = new Grupo();
    private final Grid gri_tab = new Grid();
    //private String sql = "";
    private final List<String> lis_condicion = new ArrayList();
    private final Radio rad_string = new Radio();
    private final Radio rad_int = new Radio();
    private final Radio rad_booleano = new Radio();
    private final Calendario cal_fecha = new Calendario();
    private Tabla tab_tabla;
    private final Check che_acumula_filtros = new Check();

    public BuscarTabla() {
        this.setId("bus_buscar");
        this.setHeader("Buscar");
        this.setResizable(false);

        gri_cuerpo.setColumns(2);
        pan_parametro.setHeader("Parámetros de Búsqueda");
        pan_parametro.setId("pan_parametro");

        gri_cuerpo.getChildren().add(pan_parametro);
        gri_cuerpo.getChildren().add(pan_resultado);
        Grid gri_busca = new Grid();
        gri_busca.setWidth("99%");
        com_campos.setStyle("width:98%");
        com_campos.setMetodoRuta("mbe_index.bus_buscar.cambioCombo");
        gri_busca.getChildren().add(com_campos);
        gri_busca.getChildren().add(new Espacio("1", "10"));
        tex_busqueda_bus.setId("tex_busqueda_bus");
        tex_busqueda_bus.setStyle("width:200px");
        gri_busca.getChildren().add(tex_busqueda_bus);
        cal_fecha.setStyle("width:200px");
        cal_fecha.setSize(26);
        gri_busca.getChildren().add(cal_fecha);

        Panel pan_tipo = new Panel();
        pan_tipo.setTitle("Tipo de búsqueda");
        Grid gri_tipo = new Grid();

        List lis1 = new ArrayList();
        Object[] obj = new Object[2];
        obj[0] = "0";
        obj[1] = "Comience con";
        Object[] obj1 = new Object[2];
        obj1[0] = "1";
        obj1[1] = "Cualquier coincidencia";
        lis1.add(obj);
        lis1.add(obj1);
        rad_string.setRadio(lis1);
        rad_string.setVertical();
        gri_tipo.getChildren().add(rad_string);

        List lis2 = new ArrayList();
        Object[] obj3 = new Object[2];
        obj3[0] = "0";
        obj3[1] = "= (Igual)";
        Object[] obj4 = new Object[2];
        obj4[0] = "1";
        obj4[1] = "<= (Menor o igual)";
        Object[] obj5 = new Object[2];
        obj5[0] = "2";
        obj5[1] = ">= (Mayor o igual)";
        lis2.add(obj3);
        lis2.add(obj4);
        lis2.add(obj5);
        rad_int.setRadio(lis2);
        rad_int.setVertical();
        gri_tipo.getChildren().add(rad_int);

        List lis3 = new ArrayList();
        Object[] obj6 = new Object[2];
        obj6[0] = "true";
        obj6[1] = "Si / Verdadero";
        Object[] obj7 = new Object[2];
        obj7[0] = "false";
        obj7[1] = "No / Falso";
        lis3.add(obj6);
        lis3.add(obj7);
        rad_booleano.setRadio(lis3);
        rad_booleano.setVertical();
        gri_tipo.getChildren().add(rad_booleano);

        pan_tipo.getChildren().add(gri_tipo);
        gri_busca.getChildren().add(pan_tipo);
        gri_busca.getChildren().add(new Espacio("1", "10"));

        Grid gri_acumula = new Grid();
        gri_acumula.setColumns(2);
        gri_acumula.getChildren().add(new Etiqueta("Acumular Filtros"));
        gri_acumula.getChildren().add(che_acumula_filtros);
        gri_busca.getChildren().add(gri_acumula);

        Boton bot_buscar = new Boton();
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setValue("Buscar");
        bot_buscar.setMetodoRuta("mbe_index.bus_buscar.buscar");
        gri_busca.getChildren().add(bot_buscar);
        gri_busca.getChildren().add(new Espacio("1", "10"));

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setValue("Limpiar");
        bot_limpiar.setMetodoRuta("mbe_index.bus_buscar.limpiar");
        gri_busca.getChildren().add(bot_limpiar);

        pan_parametro.getChildren().add(gri_busca);
        MarcaAgua maa_busqueda = new MarcaAgua();
        maa_busqueda.setValue("Ingrese un valor para buscar ");
        maa_busqueda.setFor("tex_busqueda_bus");
        pan_parametro.getChildren().add(maa_busqueda);
        pan_resultado.getChildren().add(gri_tab);
        this.setDialogo(gri_cuerpo);
        this.getBot_aceptar().setMetodo("aceptarBuscar");
    }

    @Override
    public void dibujar() {
        this.setWidth("70%");
        this.setHeight("80%");
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 16) + "px;height:" + (getAltoPanel() - 20) + "px;overflow: hidden;display: block;");
        pan_parametro.setStyle("width:230px;height:" + (getAltoPanel() - 30) + "px;");
        //pan_resultado.setStyle("width:" + (getAnchoPanel() - 295) + "px;height:" + (getAltoPanel() - 30) + "px;display: block;");
        gri_tab.setStyle("width:" + (getAnchoPanel() - 270) + "px;height:" + (getAltoPanel() - 30) + "px;overflow: auto;display: block;");
        super.dibujar();
    }

    public void cambioCombo() {
        String str_columna = (String) com_campos.getValue();
        switch (tab_busca_bus.getColumna(str_columna).getTipoJava()) {
            case "java.lang.Long":
            case "java.lang.Integer":
                cal_fecha.setRendered(false);
                tex_busqueda_bus.setRendered(true);
                rad_booleano.setRendered(false);
                rad_string.setRendered(false);
                rad_int.setRendered(true);
                break;
            case "java.lang.Number":
            case "java.math.BigDecimal":
            case "java.lang.Float":
            case "java.lang.Double":
                cal_fecha.setRendered(false);
                tex_busqueda_bus.setRendered(true);
                rad_booleano.setRendered(false);
                rad_string.setRendered(false);
                rad_int.setRendered(true);
                break;
            case "java.lang.Boolean":
                cal_fecha.setRendered(false);
                tex_busqueda_bus.setRendered(false);
                rad_booleano.setRendered(true);
                rad_string.setRendered(false);
                rad_int.setRendered(false);
                break;
            default:
                cal_fecha.setRendered(false);
                tex_busqueda_bus.setRendered(true);
                rad_booleano.setRendered(false);
                rad_string.setRendered(true);
                rad_int.setRendered(false);
                break;
        }

        switch (tab_busca_bus.getColumna(str_columna).getControl()) {
            case "Calendario":
                cal_fecha.setPattern("d/MM/yyyy");
                cal_fecha.setTimeOnly(false);
                cal_fecha.setRendered(true);
                tex_busqueda_bus.setRendered(false);
                rad_string.setRendered(false);
                rad_int.setRendered(true);
                rad_booleano.setRendered(false);
                break;
            case "Hora":
                cal_fecha.setPattern("HH:mm");
                cal_fecha.setTimeOnly(true);
                cal_fecha.setRendered(true);
                tex_busqueda_bus.setRendered(false);
                rad_string.setRendered(false);
                rad_int.setRendered(true);
                rad_booleano.setRendered(false);
                break;
            case "Combo":
            case "Autocompletar":
                cal_fecha.setRendered(false);
                tex_busqueda_bus.setRendered(true);
                rad_string.setRendered(true);
                rad_int.setRendered(false);
                rad_booleano.setRendered(false);
                break;
            case "Check":
                cal_fecha.setRendered(false);
                tex_busqueda_bus.setRendered(false);
                rad_string.setRendered(false);
                rad_int.setRendered(false);
                rad_booleano.setRendered(false);
                rad_booleano.setRendered(true);
                break;
        }
        framework.addUpdate("pan_parametro");
    }

    public void setBuscar(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
        che_acumula_filtros.setValue(false);
        lis_condicion.clear();
        tex_busqueda_bus.limpiar();
        cal_fecha.limpiar();
        com_campos.getChildren().clear();
        if (tab_tabla.getColumnas().length > 10) {
            com_campos.setFilter(true);
            com_campos.setFilterMatchMode("startsWith");
        } else {
            com_campos.setFilter(false);
            com_campos.setFilterMatchMode(null);
        }
        for (Columna columna : tab_tabla.getColumnas()) {
            if (columna.isVisible()) {
                ItemOpcion ito_campo = new ItemOpcion();
                ito_campo.setItemValue(columna.getNombre());
                ito_campo.setItemLabel(columna.getNombreVisual());
                com_campos.getChildren().add(ito_campo);
                if (com_campos.getChildren().size() == 1) {
                    com_campos.setValue(columna.getNombre());
                }
            }
        }
        rad_int.setValue("0");
        rad_string.setValue("0");
        rad_booleano.setValue("true");
        /*
         String str_sql = tab_tabla.getSql().toUpperCase();
         String str_campos = str_sql.substring(0, str_sql.indexOf("FROM"));
         String str_from = str_sql.substring(str_sql.indexOf("FROM"), str_sql.length());
         if (str_from.indexOf("WHERE") > 0) {
         str_from = str_from.substring(0, str_from.indexOf("WHERE"));
         }
         if (str_from.indexOf("INNER") > 0) {
         str_from = str_from.substring(0, str_from.indexOf("INNER"));
         }
         if (str_from.indexOf("ORDER BY") > 0) {
         str_from = str_from.substring(0, str_from.indexOf("ORDER BY"));
         }
         if (str_from.indexOf("GROUP BY") > 0) {
         str_from = str_from.substring(0, str_from.indexOf("GROUP BY"));
         }
         */ 
        gri_tab.getChildren().clear();
        tab_busca_bus = new Tabla();
        tab_busca_bus.setEmptyMessage("No se encontraron resultados para su busqueda");
        tab_busca_bus.setConexion(tab_tabla.getConexion());
        tab_busca_bus.setId("tab_busca_bus");
        tab_busca_bus.setRuta("mbe_index.bus_buscar");
        tab_busca_bus.setTabla(tab_tabla.getTabla(), tab_tabla.getCampoPrimaria(), -1);
        /////aumente 
        tab_busca_bus.setColumnas(tab_tabla.getColumnas());
        ////
        tab_busca_bus.setLectura(true);
        tab_busca_bus.setRows(15);
        tab_busca_bus.setCondicionForanea(tab_tabla.getCondicionForanea());
        tab_busca_bus.setCondicionPadre(tab_tabla.getCondicionPadre());
        tab_busca_bus.setCondicionSucursal(tab_tabla.isCondicionSucursal());
        String str_condicion = tab_tabla.getCondicion();
        tab_busca_bus.setCondicion(tab_tabla.getCampoPrimaria() + " = -1");
        tab_busca_bus.setColumnas(tab_tabla.getColumnas());
        tab_busca_bus.dibujar();
        tab_busca_bus.setCondicion(str_condicion);
        gri_tab.getChildren().add(tab_busca_bus);
        this.dibujar();
        cambioCombo();
    }

    public Tabla getTab_busca_bus() {
        return tab_busca_bus;
    }

    public void setTab_busca_bus(Tabla tab_busca_bus) {
        this.tab_busca_bus = tab_busca_bus;
    }

    public void limpiar() {
        try {
            tex_busqueda_bus.limpiar();
            lis_condicion.clear();
            tab_busca_bus.limpiar();
            framework.addUpdate("tex_busqueda_bus,tab_busca_bus");
        } catch (Exception e) {
        }
    }

    public void buscar() {
        String condicion_actual = "";
        if (tex_busqueda_bus.isRendered() && (tex_busqueda_bus.getValue() == null || tex_busqueda_bus.getValue().toString().isEmpty())) {
            framework.agregarMensajeInfo("Debe ingresar un valor para realizar la búsqueda", "");
            return;
        }
        if (cal_fecha.isRendered() && (cal_fecha.getValue() == null || cal_fecha.getValue().toString().isEmpty())) {
            framework.agregarMensajeInfo("Debe ingresar un valor para realizar la búsqueda", "");
            return;
        }
        String str_columna = (String) com_campos.getValue();
        String str_tipo1 = rad_string.getValue() + "";
        String str_tipo2 = rad_int.getValue() + "";
        String str_tipo3 = rad_booleano.getValue() + "";
        String str_lower = "LOWER";

        if (tab_tabla.getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("Access")) {
            //para base de datos acces
            str_lower = "LCASE";
        }
        if (str_columna != null && !str_columna.isEmpty()) {
            if (tab_busca_bus.getColumna(str_columna).getTipoJava().equals("java.lang.String") || tab_busca_bus.getColumna(str_columna).getTipoJava().equals("java.lang.Character")) {
                if (str_tipo1.equalsIgnoreCase("0")) {
                    condicion_actual = str_lower + "(" + com_campos.getValue() + ") LIKE '" + tex_busqueda_bus.getValue().toString().toLowerCase() + "%'";
                } else if (str_tipo1.equalsIgnoreCase("1")) {
                    condicion_actual = str_lower + "(" + com_campos.getValue() + ") LIKE '%" + tex_busqueda_bus.getValue().toString().toLowerCase() + "%'";
                }
            } else if (cal_fecha.isRendered()) {
                if (tab_busca_bus.getColumna(str_columna).getControl().equals("Calendario")) {
                    switch (str_tipo2) {
                        case "0":
                            condicion_actual = com_campos.getValue() + "='" + framework.getFormatoFecha(cal_fecha.getValue()) + "'";
                            break;
                        case "1":
                            condicion_actual = com_campos.getValue() + "<='" + framework.getFormatoFecha(cal_fecha.getValue()) + "'";
                            break;
                        case "2":
                            condicion_actual = com_campos.getValue() + ">='" + framework.getFormatoFecha(cal_fecha.getValue()) + "'";
                            break;
                    }
                } else {
                    switch (str_tipo2) {
                        case "0":
                            condicion_actual = com_campos.getValue() + "='" + framework.getFormatoHora(cal_fecha.getValue()) + "'";
                            break;
                        case "1":
                            condicion_actual = com_campos.getValue() + "<='" + framework.getFormatoHora(cal_fecha.getValue()) + "'";
                            break;
                        case "2":
                            condicion_actual = com_campos.getValue() + ">='" + framework.getFormatoHora(cal_fecha.getValue()) + "'";
                            break;
                    }

                }
            } else if (rad_booleano.isRendered()) {
                //cuando es booleano
                if (str_tipo3.equals("true")) {
                    if (framework.getConexion().NOMBRE_MARCA_BASE.equals("oracle")) {
                        condicion_actual = com_campos.getValue() + "=1";
                    } else {
                        condicion_actual = com_campos.getValue() + "=true";
                    }
                } else {
                    if (framework.getConexion().NOMBRE_MARCA_BASE.equals("oracle")) {
                        condicion_actual = com_campos.getValue() + "=0";
                    } else {
                        condicion_actual = com_campos.getValue() + "=false";
                    }
                }
            } else if (tab_busca_bus.getColumna(str_columna).getControl().equals("Autocompletar") || tab_busca_bus.getColumna(str_columna).getControl().equals("Combo")) {
                condicion_actual = "";
                String str_sql_combo = tab_busca_bus.getColumna(str_columna).getSqlCombo();
                if (str_sql_combo != null) {
                    if (str_sql_combo.toUpperCase().indexOf("ORDER BY") > 0) {
                        str_sql_combo = str_sql_combo.substring(0, str_sql_combo.toUpperCase().indexOf("ORDER BY"));
                    }
                    String campo_nombre = str_sql_combo.substring(str_sql_combo.toUpperCase().indexOf("SELECT "), str_sql_combo.toUpperCase().indexOf(" FROM"));
                    int num_comas = 0;
                    String campo = "";
                    for (int i = 0; i < campo_nombre.length(); i++) {
                        if (campo_nombre.charAt(i) == ',') {
                            num_comas++;
                            if (num_comas == 2) {
                                break;
                            }
                        }
                        if (num_comas == 1) {
                            if (campo_nombre.charAt(i) != ',') {
                                campo += campo_nombre.charAt(i);
                            }
                        }
                    }
                    if (str_sql_combo.toUpperCase().indexOf("WHERE") > 0) {
                        if (str_tipo1.equalsIgnoreCase("0")) {
                            str_sql_combo = str_sql_combo + "  AND " + str_lower + "(" + campo + ") LIKE '" + tex_busqueda_bus.getValue().toString().toLowerCase() + "%'";
                        } else if (str_tipo1.equalsIgnoreCase("1")) {
                            str_sql_combo = str_sql_combo + "  AND " + str_lower + "(" + campo + ") LIKE '%" + tex_busqueda_bus.getValue().toString().toLowerCase() + "%'";
                        }
                    } else {
                        if (str_tipo1.equalsIgnoreCase("0")) {
                            str_sql_combo = str_sql_combo + "  WHERE " + str_lower + "(" + campo + ") LIKE '" + tex_busqueda_bus.getValue().toString().toLowerCase() + "%'";
                        } else if (str_tipo1.equalsIgnoreCase("1")) {
                            str_sql_combo = str_sql_combo + "  WHERE " + str_lower + "(" + campo + ") LIKE '%" + tex_busqueda_bus.getValue().toString().toLowerCase() + "%'";
                        }
                    }
                    Tabla tab_combo = new Tabla();
                    tab_combo.setSql(str_sql_combo);
                    tab_combo.ejecutarSql();
                    String str_res = tab_combo.getStringColumna(tab_combo.getColumnas()[0].getNombre());
                    if (str_res.isEmpty()) {
                        str_res = "-1";
                    }
                    condicion_actual = com_campos.getValue() + " IN( " + str_res + ")";
                }
            } else {
                //numeros
                try {
                    double dou_valor = Double.parseDouble(tex_busqueda_bus.getValue().toString());
                    switch (str_tipo2) {
                        case "0":
                            condicion_actual = com_campos.getValue() + "= " + tex_busqueda_bus.getValue().toString().toLowerCase() + "";
                            break;
                        case "1":
                            condicion_actual = com_campos.getValue() + "<= " + tex_busqueda_bus.getValue().toString().toLowerCase() + "";
                            break;
                        case "2":
                            condicion_actual = com_campos.getValue() + ">= " + tex_busqueda_bus.getValue().toString().toLowerCase() + "";
                            break;
                    }
                } catch (Exception e) {
                    framework.agregarMensajeInfo("No se puede buscar", "La columna seleccionada solo admite valores numéricos");
                    tex_busqueda_bus.limpiar();
                    framework.addUpdate("tex_busqueda_bus");
                    return;
                }
            }
            if (che_acumula_filtros.getValue().equals(false)) {
                lis_condicion.clear();
            }
            lis_condicion.add(condicion_actual);

            String condicion = "";
            for (int i = 0; i < lis_condicion.size(); i++) {
                if (i > 0) {
                    condicion += " AND ";
                }
                condicion += lis_condicion.get(i);
            }

            tab_busca_bus.setCondicionBuscar(condicion);
            tab_busca_bus.ejecutarSql();
            framework.addUpdate("tab_busca_bus");
        } else {
            framework.agregarMensajeInfo("Seleccione un parámetro para realizar la búsqueda", "");
        }
    }

    public void aceptarBuscar() {
        if (tab_busca_bus.getFilaSeleccionada() != null) {
            if (!tab_tabla.getTabla().isEmpty()) {
                //busca en tablas  que no se les configura el .setSql()
                //  tab_tabla.setCondicionBuscar(tab_tabla.getCampoPrimaria() + " IN(" + tab_busca_bus.getStringColumna(tab_tabla.getCampoPrimaria()) + ")");
                tab_tabla.ejecutarSql();
                tab_tabla.setFilaActual(tab_busca_bus.getValorSeleccionado());
                //pongo en la pagina seleccionada
                if (tab_tabla.getRows() > 0) {
                    tab_tabla.calcularPaginaActual();
                }
                this.cerrar();
                this.tab_tabla = null;
            }
        } else {
            framework.agregarMensajeInfo("No se puede Aceptar", "Debe seleccionar un registro de la  tabla de busqueda");
        }
    }
}
