/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.persistencia.Conexion;
import ec.com.erpxprime.framework.aplicacion.Columna;
import ec.com.erpxprime.framework.aplicacion.Fila;
import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.convertidores.ConvertidorAutoCompletar;
import ec.com.erpxprime.framework.convertidores.ConvertidorFecha;
import ec.com.erpxprime.framework.convertidores.ConvertidorHora;
import ec.com.erpxprime.framework.convertidores.ConvertidorNumero;
import ec.com.erpxprime.framework.convertidores.UploadStreamedContent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Tabla extends DataTable {

    private final Framework framework = new Framework();
    /**
     * Ruta donde se dibuja la tabla
     */
    private String ruta = "mbe_index.clase";
    /**
     * Lista que contiene todos los registros de la tabla
     */
    private LinkedList<Fila> filas = new LinkedList();
    /**
     * Arreglo de tipo Columna que contiene las columnas de la tabla
     */
    private Columna[] columnas;
    /**
     * Nombre de la tabla de la base de datos
     */
    private String tabla = "";
    /**
     * Nombre del campo Primario o llave primaria de la tabla
     */
    private String campoPrimaria = "";
    /**
     * Nombre del campo Foranea o llave foranea de la tabla que tiene relación
     */
    private String campoForanea = "";
    /**
     * Nombre del campo que se visualizará en el árbol cuando una tabla es
     * recursiva
     */
    private String campoNombre = "";
    /**
     * Nombre del campo padre, cuando una tabla es recursiva
     */
    private String campoPadre = "";
    /**
     * Nombre del campo o campos por el que va a ser ordenada la tabla
     */
    private String campoOrden = "";
    /**
     * Sentencia sql que contiene la tabla
     */
    private String sql = "";
    /**
     * Condición sql de la tabla, no debe incluir la palabra WHERE
     */
    private String condicion = "";
    /**
     * Condición del campo Foraneo, cuando la tabla tiene relación
     */
    private String condicionForanea = "";
    /**
     * Condición del campo padre, cuando la tabla es recursiva
     */
    private String condicionPadre = "";
    /**
     * Condición buscar, se implementa cuando el usuario realiza una busqueda en
     * la tabla
     */
    private String condicionBuscar = "";
    /**
     * Condición multiempres y multisucursal, filtra solo los registros de la
     * sucursa logeada
     */
    private String condicionEmpresa = "";
    /**
     * Valor del campo foranea
     */
    private String valorforanea = "";
    /**
     * Valor del campo padre
     */
    private String valorPadre = "";
    /**
     * Objeto conexion con la que la tabla realiza los sql, por defecto es la
     * conexión con la que se realiza el login
     */
    private Conexion conexion;
    /**
     * Numero de fila Actual
     */
    private int filaActual = 0;
    /**
     * Contiene las claves primarias de las filas que se insertan
     */
    private List<String> insertadas = new ArrayList();
    /**
     * Contiene los maximos generados por el framework
     */
    private List<String> insertadasCodigos = new ArrayList(); // Guarada el maximo generado
    /**
     * Guarda la condición sql para eliminar un registro
     */
    private final Map<String, String> eliminadasCondicion = new HashMap(); // Guarada la condicionsql para eliminar
    /**
     * Guarda las claves primarias de las filas que han sido modificadas
     */
    private List<String> modificadas = new ArrayList();
    /**
     * Guarda las claves primarias de las filas eliminadas
     */
    private List<String> eliminadas = new ArrayList();
    /**
     * Metodo que se ejecuta cuando selecciono una tabla
     */
    private String metodo = "";
    /**
     * Secuensial que sirve para insertar en una tabla, solo a las filas nuevas
     */
    private char key = 'A';
    /**
     * Define si la tabla es de lectura o no
     */
    private boolean lectura = false;
    /**
     * Asigna objetos Tabla como relaciones
     */

    private Integer limite = null;

    private List<Tabla> relacion = new ArrayList();
    public Tabla TABLA_PADRE;
    public String ide_tabl = "-1";
    private int numeroTabla = -1;
    private String metodoFiltro = "";
    private boolean tipoSeleccion = false;
    private boolean tipoFormulario = false;
    private Arbol arbol = null;
    private boolean auditoria = false;
    private boolean recuperarLectura = false;
    private boolean generarPrimaria = true;
    private String idCompleto = "";
    private boolean validarInsertar = false; //Para que inserte uno y no le deje insertar mas hasta que guarde
    private boolean mostrarNumeroRegistros = true;
    private Fila foco_valida_insertar; //Guarda el foco para q cuando cambie de fila y este validando el insertar no permita seleccionar
    private List<Fila> filtro; //Para guardar el filtro de primefaces en tabla lectura,check y seleccion
    private Fila[] seleccionados;
    private boolean tipoSeleccionMultiple = false;
    private boolean condicionSucursal = false;
    private String campoRecursivo = "";
    private String columnaSuma = "";
    private String metodoCheck = "";
    private String metodoUnselectCheck = "";
    private boolean dibujo = false;
    private boolean convertidor = false;
    private String seleccionTabla = "multiple";  //para seleccion tabla  check y radio
    private boolean mostrarcampoSucursal = false;
    private String claveCompuesta = "";
    private final String id_empresa;
    private final String id_sucursal;
    private Grid grid;
    private Fila filaSeleccionada;
    private boolean tieneArchivos = false;

    private boolean ordenar = true;

    public Tabla() {
        this.setEmptyMessage("");
        conexion = framework.getConexion();
        String str_auditoria = framework.getVariable("AUDITORIA_OPCI");
        id_empresa = framework.getVariable("empresa");
        id_sucursal = framework.getVariable("id_sucursal");
        if (str_auditoria != null) {
            auditoria = framework.toBoolean(str_auditoria);
        }
    }

    public void setColumnaSuma(String columnaSuma) {
        this.columnaSuma = columnaSuma;
    }

    public void setNombreTabla(String tabla) {
        this.tabla = tabla;
    }

    public void setCampoPrimaria(String campoPrimaria) {
        this.campoPrimaria = campoPrimaria;
        this.claveCompuesta = campoPrimaria;
    }

    public void setTabla(String tabla, String campoPrimaria, int numeroTabla) {
        if (campoPrimaria == null) {
            campoPrimaria = "";
        }
        if (campoPrimaria.indexOf(",") > 0) {
            this.claveCompuesta = campoPrimaria;
            this.campoPrimaria = "";
        } else {
            this.campoPrimaria = campoPrimaria;
            this.claveCompuesta = campoPrimaria;
        }
        this.tabla = tabla;
        this.sql = new StringBuilder().append("SELECT * FROM ").append(tabla).append(" WHERE ").append(getCondicionPrimaria()).toString();
        if (columnas == null) {
            formarColumnas();
        }
        this.numeroTabla = numeroTabla;
    }

    public void ejecutarSql() {
        //filtros
        if (tabla.trim().equalsIgnoreCase("Sql")) {
            tabla = "";
        }

        String filtros = "";
        if (!tabla.isEmpty()) {
            //Campos 
            String campos = "";
            for (Columna columnaActual : columnas) {
                if (!campos.isEmpty()) {
                    campos = campos.concat(", ");
                }
                campos = campos.concat(columnaActual.getNombre());
                if (!columnaActual.getSqlFiltro().isEmpty()) {
                    if (!columnaActual.getSqlFiltro().isEmpty()) {
                        if (!filtros.isEmpty()) {
                            filtros += " AND ";
                        }
                        switch (columnaActual.getTipoJava()) {
                            case "java.lang.String":
                            case "java.lang.Character":
                                filtros += "" + columnaActual.getNombre() + " LIKE '" + columnaActual.getSqlFiltro().toLowerCase() + "%' ";
                                break;
                            case "java.sql.Date":
                                filtros += columnaActual.getNombre() + " ='" + framework.getFormatoFecha(columnaActual.getSqlFiltro()) + "'";
                                break;
                            case "java.sql.Timestamp":
                                filtros += columnaActual.getNombre() + " ='" + framework.getFormatoFechaHora(columnaActual.getSqlFiltro()) + "'";
                                break;
                            case "java.sql.Time":
                                filtros += columnaActual.getNombre() + " ='" + framework.getFormatoHora(columnaActual.getSqlFiltro()) + "'";
                                break;
                            default:
                                if (columnaActual.getControl().equals("Autocompletar") || columnaActual.getControl().equals("Combo")) {
                                    /////
                                    String str_sql_combo = columnaActual.getSqlCombo();
                                    if (str_sql_combo != null) {
                                        if (str_sql_combo.toUpperCase().indexOf("ORDER BY") > 0) {
                                            str_sql_combo = str_sql_combo.substring(0, str_sql_combo.toUpperCase().indexOf("ORDER BY"));
                                        }
                                        String campo_nombre = str_sql_combo.substring(str_sql_combo.toUpperCase().indexOf("SELECT "), str_sql_combo.toUpperCase().indexOf(" FROM"));
                                        int num_comas = 0;
                                        String campo = "";
                                        for (int k = 0; k < campo_nombre.length(); k++) {
                                            if (campo_nombre.charAt(k) == ',') {
                                                num_comas++;
                                                if (num_comas == 2) {
                                                    break;
                                                }
                                            }
                                            if (num_comas == 1) {
                                                if (campo_nombre.charAt(k) != ',') {
                                                    campo = campo.concat(String.valueOf(campo_nombre.charAt(k)));
                                                }
                                            }
                                        }
                                        if (str_sql_combo.toUpperCase().indexOf("WHERE") > 0) {
                                            str_sql_combo = str_sql_combo.concat("  AND  ").concat(campo).concat(" LIKE '").concat(columnaActual.getSqlFiltro().toLowerCase()).concat("%'");
                                        } else {
                                            str_sql_combo = str_sql_combo.concat("  WHERE ").concat(campo).concat(" LIKE '").concat(columnaActual.getSqlFiltro().toLowerCase()).concat("%'");
                                        }
                                        Tabla tab_combo = new Tabla();
                                        tab_combo.setSql(str_sql_combo);
                                        tab_combo.ejecutarSql();
                                        String str_res = tab_combo.getStringColumna(tab_combo.getColumnas()[0].getNombre());
                                        if (str_res.isEmpty()) {
                                            str_res = "'-1'";
                                        }
                                        filtros = filtros.concat(columnaActual.getNombre()).concat(" IN(").concat(str_res).concat(")");
                                        /////
                                    }
                                } else {
                                    filtros = filtros.concat(columnaActual.getNombre()).concat(" =").concat(columnaActual.getSqlFiltro()).concat("");
                                }
                                break;
                        }
                    }
                }
                ////PARA MULTI EMPRESA
                if (columnaActual.getNombre().equalsIgnoreCase("id_empresa") && !columnaActual.getNombre().equalsIgnoreCase(campoPrimaria)) {
                    columnaActual.setVisible(false);
                    columnaActual.setValorDefecto(id_empresa);
                    if (isCondicionSucursal() == false) {
                        condicionEmpresa = " id_empresa =".concat(id_empresa).concat(" ");
                    } else {
                        condicionEmpresa = " id_sucursal =".concat(id_sucursal).concat(" ");
                    }
                }
//                ////PARA MULTI SUCURSAL
//                if (mostrarcampoSucursal == false && (columnaActual.getNombre().equalsIgnoreCase("id_sucursal") && !columnaActual.getNombre().equalsIgnoreCase(campoPrimaria))) {
//                    columnaActual.setVisible(false);
//                    columnaActual.setValorDefecto(id_sucursal);
//                }
            }
            //FORMA LAS CONDICIONES 
            StringBuilder condiciones = new StringBuilder();
            if (!condicionForanea.isEmpty()) {
                condiciones = condiciones.append(" AND ").append(condicionForanea);
            }
            if (!condicionPadre.isEmpty()) {
                condiciones = condiciones.append(" AND ").append(condicionPadre);
            }
            if (!condicionEmpresa.isEmpty()) {
                condiciones = condiciones.append(" AND ").append(condicionEmpresa);
            }
            if (!condicionBuscar.isEmpty()) {
                condiciones = condiciones.append(" AND ").append(condicionBuscar);

            }
            if (!condicion.isEmpty()) {
                condiciones = condiciones.append(" AND ").append(condicion);
            }
            if (!filtros.isEmpty()) {
                condiciones = condiciones.append(" AND ").append(filtros);
            }

            if (campoOrden.isEmpty()) {
                //setCampoOrden(campoPrimaria); antes
                setCampoOrden(columnas[0].getNombre());
            }
            String strLimite = "";
            if (limite != null) {
                strLimite = " LIMIT " + limite;
            }

            sql = "SELECT ".concat(campos).concat(" FROM ").concat(tabla).concat(" WHERE 1=1 ").concat(condiciones.toString()).concat(" ").concat(" ORDER BY ").concat(campoOrden).concat(strLimite);

        } else {
            //Si tiene filtros el sql ingresado
            for (Columna columnaActual : columnas) {
                if (!columnaActual.getSqlFiltro().isEmpty()) {
                    if (!columnaActual.getSqlFiltro().isEmpty()) {
                        if (!filtros.isEmpty()) {
                            filtros = filtros.concat(" AND ");
                        }
                        switch (columnaActual.getTipoJava()) {
                            case "java.lang.String":
                            case "java.lang.Character":
                                filtros += "LOWER(".concat(columnaActual.getNombre()).concat(") LIKE '").concat(columnaActual.getSqlFiltro().toLowerCase()).concat("%' ");
                                break;
                            case "java.sql.Date":
                                filtros += columnaActual.getNombre().concat(" ='").concat(framework.getFormatoFecha(columnaActual.getSqlFiltro())).concat("'");
                                break;
                            case "java.sql.Timestamp":
                                filtros += columnaActual.getNombre().concat(" ='").concat(framework.getFormatoFechaHora(columnaActual.getSqlFiltro())).concat("'");
                                break;
                            case "java.sql.Time":
                                filtros += columnaActual.getNombre().concat(" ='").concat(framework.getFormatoHora(columnaActual.getSqlFiltro())).concat("'");
                                break;
                            default:
                                if (columnaActual.getControl().equals("Autocompletar") || columnaActual.getControl().equals("Combo")) {
                                    /////
                                    String str_sql_combo = columnaActual.getSqlCombo();
                                    if (str_sql_combo != null) {
                                        if (str_sql_combo.toUpperCase().indexOf("ORDER BY") > 0) {
                                            str_sql_combo = str_sql_combo.substring(0, str_sql_combo.toUpperCase().indexOf("ORDER BY"));
                                        }

                                        String campo_nombre = str_sql_combo.substring(str_sql_combo.toUpperCase().indexOf("SELECT "), str_sql_combo.toUpperCase().indexOf(" FROM"));
                                        int num_comas = 0;
                                        String campo = "";
                                        for (int k = 0; k < campo_nombre.length(); k++) {
                                            if (campo_nombre.charAt(k) == ',') {
                                                num_comas++;
                                                if (num_comas == 2) {
                                                    break;
                                                }
                                            }
                                            if (num_comas == 1) {
                                                if (campo_nombre.charAt(k) != ',') {
                                                    campo = campo.concat(String.valueOf(campo_nombre.charAt(k)));
                                                }
                                            }
                                        }
                                        if (str_sql_combo.toUpperCase().indexOf("WHERE") > 0) {
                                            str_sql_combo = str_sql_combo.concat("  AND  LOWER(").concat(campo).concat(") LIKE '").concat(columnaActual.getSqlFiltro().toLowerCase()).concat("%'");
                                        } else {
                                            str_sql_combo = str_sql_combo.concat("  WHERE LOWER(").concat(campo).concat(") LIKE '").concat(columnaActual.getSqlFiltro().toLowerCase()).concat("%'");
                                        }
                                        Tabla tab_combo = new Tabla();
                                        tab_combo.setSql(str_sql_combo);
                                        tab_combo.ejecutarSql();
                                        String str_res = tab_combo.getStringColumna(tab_combo.getColumnas()[0].getNombre());
                                        if (str_res.isEmpty()) {
                                            str_res = "'-1'";
                                        }
                                        filtros = filtros.concat(columnaActual.getNombre()).concat(" IN(").concat(str_res).concat(")");
                                        /////
                                    }
                                } else {
                                    filtros = filtros.concat(columnaActual.getNombre()).concat(" =").concat(columnaActual.getSqlFiltro()).concat("");
                                }
                                break;
                        }
                    }
                }
                ////PARA MULTI EMPRESA
                if (columnaActual.getNombre().equalsIgnoreCase("id_empresa") && !columnaActual.getNombre().equalsIgnoreCase(campoPrimaria)) {
                    columnaActual.setVisible(false);
                    columnaActual.setValorDefecto(id_empresa);
                    condicionEmpresa = " id_empresa =".concat(id_empresa).concat(" ");
                }
//                ////PARA MULTI SUCURSAL
//                if (mostrarcampoSucursal == false && (columnaActual.getNombre().equalsIgnoreCase("id_sucursal") && !columnaActual.getNombre().equalsIgnoreCase(campoPrimaria))) {
//                    columnaActual.setVisible(false);
//                    columnaActual.setValorDefecto(id_sucursal);
//                }
            }

            if (!filtros.isEmpty()) {
                //Añade los filtros al sql 
                StringBuilder stb_sql = new StringBuilder(getSql().toUpperCase());
                if (stb_sql.indexOf("WHERE") > 0) {
                    stb_sql.insert(stb_sql.indexOf("WHERE") + 6, filtros + " AND ");
                } else {
                    if (stb_sql.indexOf(" ORDER BY") > 0) {
                        stb_sql.insert(stb_sql.indexOf(" ORDER BY") + 6, " WHERE ".concat(filtros).concat(" "));
                    } else {
                        stb_sql.insert(stb_sql.length(), " WHERE ".concat(filtros));
                    }
                }
                sql = stb_sql + "";
            }
        }
        insertadas.clear();
        modificadas.clear();
        eliminadas.clear();
        insertadasCodigos.clear();

        if (!columnaSuma.isEmpty()) {
            String[] col = columnaSuma.split(",");
            for (String col1 : col) {
                columnas[getNumeroColumna(col1)].setSuma(true);
                columnas[getNumeroColumna(col1)].setTotal(new Double(0));
            }
        }

        //System.out.println(" ....  " + str_sql);
        List<String> lista = conexion.consultar(sql);
        formarFilas(lista);
        if (!campoPadre.isEmpty()) {
            this.getColumna(campoPadre).setVisible(false);
        }
        if (!campoForanea.isEmpty()) {
            this.getColumna(campoForanea).setVisible(false);
        }

        if (dibujo) {
            if (!tipoFormulario) {
                if (ordenar) {
                    if (!campoOrden.isEmpty() && campoOrden.indexOf(" ") > 0) {
                        //SI TIENE ASC O DESC
                        String campo = campoOrden.substring(0, campoOrden.indexOf(" "));
                        campo = campo.trim();
                        this.setValueExpression("sortBy", crearValueExpression("fila.campos[" + getNumeroColumna(campo) + "]"));
                    } else {
                        this.setValueExpression("sortBy", crearValueExpression("fila.campos[" + getNumeroColumna(campoOrden) + "]"));
                    }
                }

            }
            setFirst(0);
            updateTablas();
        }
    }

    private void formarFilas(List lista) {
        filas = new LinkedList();
        key = 'A';
        int num_campoPrimaria = 0;
        if (!campoPrimaria.isEmpty()) {
            num_campoPrimaria = getNumeroColumna(campoPrimaria);
        }
        if (!lista.isEmpty()) {
            for (int i = 0; i < lista.size(); i++) {
                Object[] campos = (Object[]) lista.get(i);
                Fila fila_nueva = new Fila();
                fila_nueva.setIndice(i);
                if (recuperarLectura) {
                    fila_nueva.setLectura(true);
                }

                if (convertidor || !columnaSuma.isEmpty() || tieneArchivos) {
                    for (int j = 0; j < columnas.length; j++) {
                        ///SI GUARDA ARCHIVOS     
                        if (columnas[j].getControl().equals("Upload") && columnas[j].getCarpeta() == null) {
                            if (campos[j] != null) {
                                //Convierte en Stream
                                try {
                                    try (InputStream myInputStream = new ByteArrayInputStream((byte[]) campos[j])) {
                                        myInputStream.mark(0);
                                        String mimeType = URLConnection.guessContentTypeFromStream(myInputStream);
                                        StreamedContent stream = new DefaultStreamedContent(myInputStream, mimeType);
                                        campos[j] = stream;
                                        myInputStream.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        ///PARA EL CONVERTER 
                        if (columnas[j].getControl().equals("Autocompletar") || columnas[j].getControl().equals("Combo")) {
                            Object[] obj = columnas[j].getFilaAutocompletar(campos[j]);
                            if (obj != null) {
                                campos[j] = obj;
                            }
                        } else if (conexion.NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle") && columnas[j].getControl().equals("Check")) {
                            String str_valor = "";
                            if (campos[j] == null) {
                                campos[j] = "0";
                            }
                            str_valor = campos[j] + "";
                            campos[j] = str_valor.trim().equals("1");
                        } else if (conexion.NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
                            switch (columnas[j].getControl()) {
                                case "Calendario":
                                    if (campos[j] != null) {
                                        if (columnas[j].getTipoJava().equals("java.sql.Timestamp")) {
                                            campos[j] = framework.getFechaHora(campos[j].toString());
                                        } else {
                                            campos[j] = framework.getFecha(campos[j].toString());
                                        }
                                    }
                                    break;
                                case "Hora":
                                    if (campos[j] != null) {
                                        if (framework.getHora(campos[j].toString()) == null) {
                                            try {
                                                campos[j] = campos[j].toString().substring(campos[j].toString().indexOf(" "), campos[j].toString().length());
                                                campos[j] = campos[j].toString().trim();
                                                campos[j] = framework.getHora(campos[j].toString());
                                            } catch (Exception e) {
                                            }
                                        } else {
                                            campos[j] = framework.getHora(campos[j].toString());
                                        }
                                    }
                                    break;
                            }
                        }
                        if (columnas[j].isSuma()) {
                            if (campos[j] != null) {
                                Double antes = columnas[j].getTotal();
                                double nuevo = 0.0;
                                try {
                                    nuevo = Double.parseDouble(String.valueOf(campos[j]));
                                } catch (Exception e) {
                                }
                                columnas[j].setTotal(antes + nuevo);
                            }
                        }
                    }
                }
                fila_nueva.setCampos(campos);
                ///Si no tiene clave primaria le asigno al rowkey el valor del for
                if (!campoPrimaria.isEmpty()) {
                    fila_nueva.setRowKey(campos[num_campoPrimaria] + "");
                } else {
                    fila_nueva.setRowKey(fila_nueva.getIndice() + "");
                }
                filas.add(fila_nueva);
                if (i == 0 && tipoSeleccion == false) {
                    this.filaActual = 0;
                    filaSeleccionada = fila_nueva;
                    //Asigna clave foranea a las relaciones
                    for (Tabla relacion1 : relacion) {
                        if (this.getChildren().isEmpty()) {
                            relacion1.setValorForanea(getValorSeleccionado());
                        } else {
                            relacion1.ejecutarValorForanea(getValorSeleccionado());
                        }
                    }
                }
            }
        } else {
            setFilaActual(0);
            limpiar();
        }
        filtro = null;
        seleccionados = null;
    }

    private void formarColumnas() {

        try {
            conexion.conectar(false);
            Statement sta_sentencia = conexion.getConnection().createStatement();
            ResultSet resultado = sta_sentencia.executeQuery(getSql());
            ResultSetMetaData rsm_metadata = resultado.getMetaData();
            columnas = new Columna[rsm_metadata.getColumnCount()];
            for (int i = 1; i <= rsm_metadata.getColumnCount(); i++) {

                Columna columna_nueva = new Columna();
                if (rsm_metadata.isNullable(i) == 0) {
                    columna_nueva.setRequerida(true);
                } else {
                    columna_nueva.setRequerida(false);
                }
                columna_nueva.setNombre(rsm_metadata.getColumnName(i).toUpperCase());
                columna_nueva.setNombreVisual(rsm_metadata.getColumnName(i).toUpperCase());
                columna_nueva.setOrden(i);
                columna_nueva.setLongitud(rsm_metadata.getPrecision(i));
                columna_nueva.setAncho(rsm_metadata.getPrecision(i));
                columna_nueva.setDecimales(rsm_metadata.getScale(i));
                columna_nueva.setTipoJava(rsm_metadata.getColumnClassName(i) == null ? "java.lang.String" : rsm_metadata.getColumnClassName(i));
                columna_nueva.setTipo(rsm_metadata.getColumnTypeName(i).toUpperCase());
                columna_nueva.setTab_tabla(this);

                //para oracle
                if (conexion.NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle")) {
                    //Para saber si es date
                    if (columna_nueva.getTipo().equals("DATE")) {
                        columna_nueva.setTipoJava("java.sql.Date");
                    } //Para saber si es entero                    
                    else if (columna_nueva.getTipo().equalsIgnoreCase("NUMBER") && (columna_nueva.getAncho() == 38 || columna_nueva.getDecimales() == 0)) {
                        columna_nueva.setTipoJava("java.lang.Integer");
                        columna_nueva.setLongitud(20);
                    } //Para saber si es de tipo Hora
                    else if (columna_nueva.getTipo().equalsIgnoreCase("TIMESTAMP")) {
                        columna_nueva.setTipoJava("java.sql.Time");
                    } //tipo clob
                    else if (columna_nueva.getTipo().equalsIgnoreCase("CLOB")) {
                        columna_nueva.setTipo("Text");
                        columna_nueva.setTipoJava("java.lang.String");
                    }
                }

                //para sqlserver
                if (conexion.NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
                    //problema con identificar fecha y horas
                    //  if (columna_nueva.getTipoJava().equals("java.sql.Timestamp")) {
                    if (columna_nueva.getNombre().toUpperCase().startsWith("FECHA")) {
                        columna_nueva.setTipoJava("java.sql.Date");
                        convertidor = true;
                    } else if (columna_nueva.getNombre().toUpperCase().startsWith("HORA")) {
                        columna_nueva.setTipoJava("java.sql.Time");
                        convertidor = true;
                    }
                    //   }
                }

                if (columna_nueva.getTipoJava().equals("java.sql.Date") || columna_nueva.getTipoJava().equals("java.sql.Timestamp")) {
                    columna_nueva.setControl("Calendario");
                }
                if (columna_nueva.getTipoJava().equals("java.sql.Time")) {
                    columna_nueva.setControl("Hora");
                }
                if (columna_nueva.getTipoJava().equals("java.lang.Boolean")) {
                    columna_nueva.setControl("Check");
                }
                if (columna_nueva.getTipo().equalsIgnoreCase("Text") || columna_nueva.getLongitud() > 200) {
                    columna_nueva.setControl("AreaTexto");

                }
                if (columna_nueva.getLongitud() > 180) {
                    columna_nueva.setLongitud(180);
                }
                columnas[(i - 1)] = columna_nueva;
                ///Si no le asigno clave primaria
                if (i == 1 && campoPrimaria.isEmpty()) {
                    campoPrimaria = columna_nueva.getNombre();
                    claveCompuesta = columna_nueva.getNombre();
                }
                configuraCampoPistaAuditoria(columna_nueva.getNombre());
            }
            resultado.close();
            sta_sentencia.close();
            conexion.desconectar(false);
        } catch (SQLException e) {
            framework.crearError("No se pudo formar las columans de la tabla " + tabla + " : " + e.getMessage(), "en el método formarColumnas() " + sql, e);
            System.out.println("ERROR formarColumnas():" + e.getMessage() + "   " + sql);
        }
    }

    private void ordenarColumnas() {
        for (int i = 0; i < columnas.length; i++) {
            for (int j = (i + 1); j < columnas.length; j++) {
                if (columnas[j].getOrden() < columnas[i].getOrden()) {
                    Columna aux = columnas[i];
                    columnas[i] = columnas[j];
                    columnas[j] = aux;
                }
            }
        }
    }

    private void buscarPermisos() {
//Permiso a la tabla si la tabla no esta en lectura desde la el código
        if (isLectura() == false) {
            List lis_tabla = framework.getConexion().consultar("SELECT lectura,id_opcion FROM tbl_perfil_opcion WHERE id_opcion=".concat(framework.getVariable("id_opcion")).concat(" AND id_perfil=").concat(framework.getVariable("id_perfil")));
            if (!lis_tabla.isEmpty()) {
                Object[] fila = (Object[]) lis_tabla.get(0);
                if (fila[0] != null) {
                    this.setLectura(framework.toBoolean(fila[0]));
                }
            }
        }
//Permisos a los Campos        
//        List lis_campos = framework.getConexion().consultar("SELECT NOM_CAMP,LECTURA_PECA,VISIBLE_PECA,DEFECTO_PECA FROM SIS_PERFIL_CAMPO,SIS_CAMPO WHERE SIS_PERFIL_CAMPO.IDE_CAMP=SIS_CAMPO.IDE_CAMP AND  IDE_TABL=".concat(ide_tabl).concat(" AND SIS_PERFIL_CAMPO.id_perfil=").concat(framework.getVariable("id_perfil")));
//        for (Object lis_campo : lis_campos) {
//            Object[] fila = (Object[]) lis_campo;
//            if (fila[0] != null) {
//                boolean boo_lectura = false;
//                boolean boo_visible = true;
//                if (fila[1] != null) {
//                    boo_lectura = framework.toBoolean(fila[1]);
//                }
//                if (fila[2] != null) {
//                    boo_visible = framework.toBoolean(fila[2]);
//                }
//                if (fila[3] != null) {
//                    this.getColumna(fila[0].toString()).setValorDefecto(fila[3].toString());
//                }
//                this.getColumna(fila[0].toString()).setLectura(boo_lectura);
//                this.getColumna(fila[0].toString()).setVisible(boo_visible);
//            }
//        }
    }

    public void updateTablas() {

        if (this.getClientId().contains("formulario:")) { //solo si ya se dibujo
            //actualiza la tabla y todas
            String ids = this.getIdCompleto();
            for (Tabla relacion1 : relacion) {
                ids += "," + relacion1.getIdCompleto();
            }
            framework.addUpdate(ids);
        }

    }

    public void addUpdateArbol() {
        //actualiza el arbol
        if (arbol != null) {
            framework.addUpdate(arbol.getId());
        }
    }

    public void dibujar() {
        dibujo = true;
        if (idCompleto.isEmpty()) {
            idCompleto = getId();
        }
        this.getChildren().clear();

        if (numeroTabla != -1) {  //cuando es -1 no hace estas validaciones
//            cargarConfiguracionTabla();
            ordenarColumnas();
        }

        for (Columna columna : columnas) {
            if (columna.getControl().equals("Autocompletar") || columna.getControl().equals("Combo")) {
                convertidor = true;
                if (tieneArchivos) {
                    break;
                }
            }
            if (conexion.NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle") && columna.getControl().equals("Check")) {
                convertidor = true;
                if (tieneArchivos) {
                    break;
                }
            }
            if (columna.getControl().equals("Upload") && columna.getCarpeta() == null) { //Guarda en la bdd el archivo
                tieneArchivos = true;
                if (convertidor) {
                    break;
                }

            }
        }
        ejecutarSql();
        if (numeroTabla != -1) {  //cuando es -1 no hace estas validaciones
            buscarPermisos();
        }

        if (isGenerarPrimaria() && !campoPrimaria.isEmpty()) {
            getColumna(campoPrimaria).setControl("Etiqueta");
        }
        if (!tipoFormulario) {
            String str_id = this.getId();
            if (tipoSeleccion) {
                if (getId().indexOf("_tab_seleccion") > 0) {
                    str_id = "tab_seleccion";
                    construirTablaSeleccion();
                } else {
                    construirTablaCheck();
                }

            } else if (tipoSeleccionMultiple) {
                construirTablaSeleccionMultiple();
            } else {
                if (lectura) {
                    construirTablaLectura();
                } else {
                    construirTabla();
                }
            }

            if (this.getRows() > 0) {
                this.setPaginator(true);
                this.setPaginatorPosition("top");
                this.setPaginatorAlwaysVisible(false);
                this.setCurrentPageReportTemplate("( REGISTROS {totalRecords} ) PÁGINA {currentPage} DE {totalPages}");
                this.setPaginatorTemplate("{CurrentPageReport} {FirstPageLink} {PreviousPageLink} {NextPageLink} {LastPageLink} IR A PÁGINA: {JumpToPageDropdown} &nbsp;");
            }

//////            if (!columnaSuma.isEmpty()) {
//////                //         ColumnGroup cgr_totales = new ColumnGroup();
//////                //          Row filasuma = new Row();
//////                int contador = -1;
//////                for (int i = 0; i < getTotalColumnas(); i++) {
//////                    if (columnas[i].isVisible()) {
//////                        contador++;
//////                        // if (columnaSuma.toUpperCase().contains(columnas[i].getNombre().toUpperCase())) {
//////                        Column col_padre = (Column) this.getChildren().get(contador);
//////                        //  col_padre.setValueExpression("footerText", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
//////
//////                        Etiqueta eti_total = new Etiqueta();
//////                        eti_total.setStyle("text-align:right;font-size:13px;font-weight: bold;");
//////                        eti_total.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
//////                        col_padre.setFooter(eti_total);
//////    // }
//////                        // Column col_suma = new Column();
//////                        //  col_suma.setStyle("text-align:right;font-size:14px;;font-weight: bold;");
//////
//////                        //    col_suma.setWidth(col_padre.getWidth() - 3);
//////                        //  filasuma.getChildren().add(col_suma);
//////                    }
//////                }
//////                //cgr_totales.setType("footer");
//////                //cgr_totales.getChildren().add(filasuma);
//////                //this.getChildren().add(cgr_totales);
//////            }
        } else {

            if (lectura) {
                construirFormularioLectura();
            } else {
                construirFormulario();
            }

            Etiqueta eti_actual = new Etiqueta();
            eti_actual.setValueExpression("value", crearValueExpression2(new StringBuilder("<div style='text-align: left;padding-left:5px;' class='ui-panel-titlebar ui-widget-header ui-helper-clearfix ui-corner-all'> <span class='ui-panel-title'>  REGISTRO #{").append(ruta).append(".").append(this.getId()).append(".auxNumeroFormulario} DE  #{").append(ruta).append(".").append(this.getId()).append(".totalFilas} </span></div>").toString()));
            eti_actual.setValueExpression("rendered", new StringBuilder(ruta).append(".").append(this.getId()).append(".mostrarNumeroRegistros").toString());
            grid.setStyleClass("ui-widget-content");
            grid.setStyle("border:none");
            grid.setFooterClass(null);
            grid.setHeaderClass(null);
            grid.setCaptionClass(null);
            grid.setValueExpression("rendered", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filaSeleccionada != null").toString()));
            grid.setHeader(eti_actual);
            this.setFooter(grid);
        }
        //Asigno configuracion al arbol
        if (arbol != null) {
            arbol.setArbol(tabla, campoPrimaria, campoNombre, campoPadre);
        }
    }

    public void setHeader(String titulo) {
        super.setHeader(new Etiqueta(titulo.toUpperCase()));
    }

    public int getAuxNumeroFormulario() {
        return getFilaActual() + 1;
    }

    private void construirTabla() {

        //si tiene mas de 30 registros y no tiene paginador
        if (getTotalFilas() > 20 || getRows() == 0) {
            setRows(20);
        }

        Ajax aja_selec_fila = new Ajax();
        Ajax aja_filtro_columna = new Ajax();
        Ajax aja_modifico_fila = new Ajax();
        aja_modifico_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".modificar").toString());

        if (!metodo.isEmpty()) {
            aja_selec_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(metodo).toString());
        } else {
            aja_selec_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".seleccionarFila").toString());
            if (relacion.isEmpty()) {
                aja_selec_fila.setGlobal(false);
                aja_modifico_fila.setGlobal(false);
            }
        }

        this.addClientBehavior("rowSelect", aja_selec_fila);

        this.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filas").toString()));
        this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filaSeleccionada").toString()));
        this.setSelectionMode("single");
        this.setVar("fila");
        this.setValueExpression("rowKey", crearValueExpression("fila.rowKey"));

        if (metodoFiltro.isEmpty()) {
            aja_filtro_columna.setMetodoRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".filtrarColumna").toString());
        } else {
            aja_filtro_columna.setMetodoRuta(new StringBuilder(ruta).append(".").append(metodoFiltro).toString());
        }
        this.setResizableColumns(true);

        for (int i = 0; i < columnas.length; i++) {
            columnas[i].setId(new StringBuilder(idCompleto).append(":**:").append(columnas[i].getNombre()).append("_").append(i).toString());
            Grid gri_cabecera = new Grid();
            gri_cabecera.setColumns(1);
            gri_cabecera.setStyle("padding-left:10px;vertical-align: middle;text-align: center;width:100%");
            Etiqueta eti_titulo = new Etiqueta();
            Column columna = new Column();
            columna.setValueExpression("rendered", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].visible").toString()));
            columna.setId(columnas[i].getNombre());
            if (ordenar) {
                columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
            }
            columna.setResizable(true);
            eti_titulo.setId(new StringBuilder("eti_").append(columnas[i].getNombre()).toString());
            eti_titulo.setValue(columnas[i].getNombreVisual());
            if (columnas[i].isRequerida()) {
                eti_titulo.setValue(new StringBuilder(columnas[i].getNombreVisual()).append(" <span style=\"color:red;font-weight: bold;\"> *</span>").toString());
            }

            eti_titulo.setStyleClass(null);
            gri_cabecera.getChildren().add(eti_titulo);
            if (columnas[i].isFiltro()) {
                Texto tex_filtro = new Texto();
                tex_filtro.setStyle("width:85%;");
                tex_filtro.addClientBehavior("change", aja_filtro_columna);
                tex_filtro.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].sqlFiltro").toString()));
                gri_cabecera.getChildren().add(tex_filtro);
            }
            if (columnas[i].getComentario() != null) {
                eti_titulo.setTitle(columnas[i].getComentario());
            }
            if (columnas[i].getEstiloColumna() != null) {
                columna.setStyle(columnas[i].getEstiloColumna());
            }
            columna.setHeader(gri_cabecera);
            if (columnas[i].isLectura()) {
                ///Si la columna es de lectura
                switch (columnas[i].getControl()) {
                    case "Etiqueta":
                    case "Texto":
                    case "Mascara":
                    case "Calendario":
                    case "Hora":
                    case "AreaTexto": {
                        Etiqueta eti_dato = new Etiqueta();
                        eti_dato.setTitle(columnas[i].getNombreVisual());
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                        if (columnas[i].getControl().equals("Calendario")) {
                            if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                                eti_dato.setConverter(new ConvertidorFecha(true));
                            } else {
                                eti_dato.setConverter(new ConvertidorFecha());
                            }
                        } else if (columnas[i].getControl().equals("Hora")) {
                            eti_dato.setConverter(new ConvertidorHora());
                        } else if (columnas[i].isFormatoNumero()) {
                            eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        columna.getChildren().add(eti_dato);
                        break;
                    }
                    case "Autocompletar":
                    case "Combo": {
                        if (ordenar) {
                            columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        }
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        String str = "";
                        int vueltas = 0;
                        for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                            if (j > 1) {
                                str = str.concat("    ");
                            }
                            str = str.concat("#{fila.campos[" + i + "][" + j + "]}");
                            vueltas = vueltas + 70;
                        }
                        if (columnas[i].getLongitud() != 0) { //calcula longitud
                            columnas[i].setLongitud(vueltas);
                        }

                        eti_dato.setValueExpression("value", crearValueExpression2(str));
                        eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Check":
                        Check che_dato = new Check();
                        che_dato.setDisabled(true);
                        che_dato.setTitle(columnas[i].getNombreVisual());
                        if (columnas[i].getEstilo() != null) {
                            che_dato.setStyle(columnas[i].getEstilo());
                        }
                        che_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        columna.getChildren().add(che_dato);
                        break;
                    case "Clave": {
                        Etiqueta eti_dato = new Etiqueta();
                        eti_dato.setTitle(columnas[i].getNombreVisual());
                        eti_dato.setValue("**********");
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        columna.getChildren().add(eti_dato);
                        break;
                    }
                    case "Radio":
                        Radio rad_dato = new Radio();
                        if (columnas[i].isRadioVertical()) {
                            rad_dato.setVertical();
                        }
                        columnas[i].setLongitud(30);
                        rad_dato.setDisabled(columnas[i].isLectura());
                        rad_dato.setTitle(columnas[i].getNombreVisual());
                        rad_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        rad_dato.setValueExpression("rendered", "fila.visible");
                        rad_dato.setDisabled(true);
                        if (columnas[i].getEstilo() != null) {
                            rad_dato.setStyle(new StringBuilder("width:200%;display:block;border:none;").append(columnas[i].getEstilo()).toString());
                        } else {
                            rad_dato.setStyle("width:200%;display:block;border:none;");
                        }
                        UISelectItems usi_opciones = new UISelectItems();
                        usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].listaRadio").toString()));
                        usi_opciones.setValueExpression("var", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].var").toString()));
                        usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion[0]"));
                        usi_opciones.setValueExpression("itemLabel", crearValueExpression("opcion[1]"));
                        rad_dato.getChildren().add(usi_opciones);
                        columna.getChildren().add(rad_dato);
                        break;
                    case "Upload":
                        Etiqueta eti_link = new Etiqueta();
                        eti_link.setValueExpression("rendered", new StringBuilder(ruta).append(".").append(this.getId()).append(".filaSeleccionada.visible").toString());
                        if (columnas[i].getEstilo() != null) {
                            eti_link.setStyle(columnas[i].getEstilo());
                        }
                        eti_link.setValueExpression("value", crearValueExpression2(new StringBuilder("<a style='color: #0000ff;text-decoration: underline;' href='").append(framework.getURL()).append("#{fila.campos[").append(i).append("]}' >").append("#{fila.campos[").append(i).append("]}</a>").toString()));
                        eti_link.setValueExpression("rendered", "fila.visible");
                        columna.getChildren().add(eti_link);
                        break;
                }

            } else {
                //Columnas editables
                switch (columnas[i].getControl()) {
                    case "Etiqueta": {
                        Etiqueta eti_dato = new Etiqueta();
                        eti_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].isFormatoNumero()) {
                            eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                        columna.getChildren().add(eti_dato);
                        break;
                    }
                    case "Texto": {
                        Texto tex_dato = new Texto();
                        tex_dato.setStyleClass("ui-widget-content");
                        tex_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        tex_dato.setTitle(columnas[i].getNombreVisual());
                        tex_dato.addClientBehavior("focus", aja_selec_fila);
                        if (columnas[i].getEstilo() != null) {
                            tex_dato.setStyle(new StringBuilder("width:97%;").append(columnas[i].getEstilo()).toString());
                        } else {
                            tex_dato.setStyle("width:97%;");
                        }
                        tex_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        tex_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        if (columnas[i].getOnKeyup() != null) {
                            tex_dato.setOnkeyup(columnas[i].getOnKeyup());
                        }
                        if (columnas[i].getOnClick() != null) {
                            tex_dato.setOnclick(columnas[i].getOnClick());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            tex_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            tex_dato.addClientBehavior("change", aja_modifico_fila);
                        }       //Validar numeros
                        switch (columnas[i].getTipoJava()) {
                            case "java.lang.Long":
                            case "java.lang.Integer":
                                tex_dato.setSoloEnteros();
                                break;
                            case "java.lang.Number":
                            case "java.math.BigDecimal":
                            case "java.lang.Float":
                            case "java.lang.Double":
                                tex_dato.setSoloNumeros();
                                break;
                            default:
                                tex_dato.setMaxlength(columnas[i].getAncho());
                                break;
                        }
                        if (columnas[i].isFormatoNumero()) {
                            tex_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        columna.getChildren().add(tex_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        if (columnas[i].isFormatoNumero()) {
                            eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Upload":
                        Grid matriz = new Grid();
                        matriz.setId(new StringBuilder(this.getId()).append("_").append(columnas[i].getNombre()).append("_").append(i).toString());
                        matriz.setColumns(2);
                        Link lin_download = new Link();
                        lin_download.setAjax(false);
                        lin_download.setValue(" Descargar Archivo");
                        lin_download.setStyleClass("fa fa-download fa-lg");
                        lin_download.setValueExpression("rendered", "fila.campos[" + i + "] !=null");
                        lin_download.addActionListener(new FileDownloadActionListener(crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString())));

                        Boton bot_subir = new Boton();
                        bot_subir.setIcon("ui-icon-arrowthickstop-1-n");
                        bot_subir.setValue("Cargar archivo");
                        bot_subir.setActionListener("abrirSeleccionarArchivo");

                        MethodExpression methodExpression;
                        methodExpression
                                = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), new StringBuilder("#{" + ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].subirArchivo}").toString(), null, new Class[]{
                                    FileUploadEvent.class
                                }
                                );
                        bot_subir.setUploadMetodo(methodExpression);
                        matriz.getChildren().add(lin_download);
                        matriz.getChildren().add(bot_subir);
                        columna.getChildren().add(matriz);
                        break;
                    case "Clave": {
                        Clave cla_dato = new Clave();
                        cla_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        cla_dato.setMaxlength(columnas[i].getAncho());
                        cla_dato.setTitle(columnas[i].getNombreVisual());
                        cla_dato.addClientBehavior("focus", aja_selec_fila);
                        cla_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        cla_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        cla_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        if (columnas[i].getEstilo() != null) {
                            cla_dato.setStyle(new StringBuilder("width:97%;").append(columnas[i].getEstilo()).toString());
                        } else {
                            cla_dato.setStyle("width:97%;");
                        }
                        if (columnas[i].getOnClick() != null) {
                            cla_dato.setOnclick(columnas[i].getOnClick());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            cla_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            cla_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        columna.getChildren().add(cla_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        eti_dato.setValue("**********");
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Mascara": {
                        Mascara mas_dato = new Mascara();
                        mas_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        mas_dato.setMaxlength(columnas[i].getAncho());
                        mas_dato.setTitle(columnas[i].getNombreVisual());
                        mas_dato.addClientBehavior("focus", aja_selec_fila);
                        mas_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        mas_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        mas_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        mas_dato.setValueExpression("mask", new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].mascara").toString());
                        if (columnas[i].getTipoJava().equals("java.sql.Time")) {
                            //Se implemento esto debido a que se ponia el foco en la ultima fila don de existte un calendario tipo fecha
                            mas_dato.setConverter(new ConvertidorHora());
                        }
                        if (columnas[i].getEstilo() != null) {
                            mas_dato.setStyle(new StringBuilder("width:97%;").append(columnas[i].getEstilo()).toString());
                        } else {
                            mas_dato.setStyle("width:97%;");
                        }
                        if (columnas[i].getOnKeyup() != null) {
                            mas_dato.setOnkeyup(columnas[i].getOnKeyup());
                        }
                        if (columnas[i].getOnClick() != null) {
                            mas_dato.setOnclick(columnas[i].getOnClick());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            mas_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            mas_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        columna.getChildren().add(mas_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Calendario": {
                        Calendario cal_dato = new Calendario();
                        cal_dato.setId(columnas[i].getNombre() + "_" + i);
                        cal_dato.addClientBehavior("focus", aja_selec_fila);
                        cal_dato.setTitle(columnas[i].getNombreVisual());
                        cal_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        cal_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            cal_dato.setTipoCalendarioHora();
                        }
                        if (columnas[i].getEstilo() != null) {
                            cal_dato.setStyle(new StringBuilder("width:97%;").append(columnas[i].getEstilo()).toString());
                        } else {
                            cal_dato.setStyle("width:97%;");
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            cal_dato.addClientBehavior("dateSelect", columnas[i].getMetodoChange());
                        }
                        if (columnas[i].getOnClick() != null) {
                            cal_dato.setOnclick(columnas[i].getOnClick());
                        }
                        cal_dato.addValueChangeListener(crearValueChange(new StringBuilder(ruta).append(".").append(this.getId()).append(".modificar").toString()));
                        columna.getChildren().add(cal_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            eti_dato.setConverter(new ConvertidorFecha(true));
                        } else {
                            eti_dato.setConverter(new ConvertidorFecha());
                        }
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Hora": {
                        Hora hor_dato = new Hora();
                        hor_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        hor_dato.setTitle(columnas[i].getNombreVisual());
                        hor_dato.setSize(8);
                        hor_dato.addClientBehavior("focus", aja_selec_fila);
                        hor_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        hor_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        if (columnas[i].getEstilo() != null) {
                            hor_dato.setStyle(new StringBuilder("width:97%;").append(columnas[i].getEstilo()).toString());
                        } else {
                            hor_dato.setStyle("width:97%;");
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            hor_dato.addClientBehavior("dateSelect", columnas[i].getMetodoChange());
                        }
                        if (columnas[i].getOnClick() != null) {
                            hor_dato.setOnclick(columnas[i].getOnClick());
                        }
                        hor_dato.addValueChangeListener(crearValueChange(new StringBuilder(ruta).append(".").append(this.getId()).append(".modificar").toString()));
                        columna.getChildren().add(hor_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        eti_dato.setConverter(new ConvertidorHora());
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Check":
                        Check che_dato = new Check();
                        che_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        che_dato.setTitle(columnas[i].getNombreVisual());
                        che_dato.addClientBehavior("focus", aja_selec_fila);
                        che_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        che_dato.setValueExpression("rendered", "fila.visible");
                        che_dato.setValueExpression("disabled", new StringBuilder("fila.lectura or ").append(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].lectura").toString());
                        if (columnas[i].getEstilo() != null) {
                            che_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            che_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            che_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            che_dato.setOnclick(columnas[i].getOnClick());
                        }
                        columna.getChildren().add(che_dato);
                        break;
                    case "Combo": {
                        Combo com_dato = new Combo();
                        com_dato.setId(columnas[i].getNombre() + "_" + i);
                        com_dato.setTitle(columnas[i].getNombreVisual());
                        com_dato.addClientBehavior("focus", aja_selec_fila);
                        com_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        com_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        com_dato.getChildren().clear();
                        if (columnas[i].isPermitirNullCombo()) {
                            ItemOpcion ito_vacio = new ItemOpcion();
                            ito_vacio.setItemLabel("");
                            ito_vacio.setItemValue(null);
                            com_dato.getChildren().add(ito_vacio);
                        }
                        UISelectItems usi_opciones = new UISelectItems();
                        usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].listaCombo").toString()));
                        usi_opciones.setValueExpression("var", crearValueExpression2("op"));
                        usi_opciones.setValueExpression("itemLabelEscaped", crearValueExpression2("false"));
                        com_dato.setVar("combo");
                        if (ordenar) {
                            columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        }
                        usi_opciones.setValueExpression("itemValue", crearValueExpression("op"));
                        com_dato.setConverter(new ConvertidorAutoCompletar());
                        String str = "";
                        //Si en el select del combo hay mas de dos campos
                        int vueltas = 0;
                        for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                            if (j > 1) {
                                str = str.concat("    ");
                            }
                            str = str.concat("#{op[" + j + "]} ");
                            Column col = new Column();
                            Etiqueta eti = new Etiqueta();
                            eti.setValueExpression("value", crearValueExpression2(new StringBuilder("#{combo[").append(j).append("]}").toString()));
                            col.getChildren().add(eti);
                            com_dato.getChildren().add(col);
                            vueltas = vueltas + 60;
                        }
                        usi_opciones.setValueExpression("itemLabel", crearValueExpression2(str));
                        if (columnas[i].getLongitud() != -1) {
                            columnas[i].setLongitud(vueltas);
                        } else {
                            columnas[i].setLongitud(30);
                        }

                        if (columnas[i].getEstilo() != null) {
                            com_dato.setStyle("width:97%;" + columnas[i].getEstilo());
                        } else {
                            com_dato.setStyle("width:97%;");
                        }
                        if (columnas[i].isBuscarenCombo() && columnas[i].getListaCombo().size() > 10) {
                            com_dato.setFilter(true);
                            com_dato.setFilterMatchMode(columnas[i].getFiltroModo());
                        }
                        com_dato.getChildren().add(usi_opciones);
                        if (columnas[i].getMetodoChange() != null) {
                            com_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            com_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            com_dato.setOnclick(columnas[i].getOnClick());
                        }
                        columna.getChildren().add(com_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        if (columnas[i].getNumeroColumnasCombo() > 2) {
                            eti_dato.setValueExpression("value", crearValueExpression2(str.replace("op", "fila.campos[" + i + "]")));
                        } else {
                            eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        }
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Autocompletar": {
                        if (ordenar) {
                            columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        }
                        AutoCompletar aut_dato = new AutoCompletar();
                        aut_dato.setId(columnas[i].getNombre() + "_" + i);
                        aut_dato.setTitle(columnas[i].getNombreVisual());
                        aut_dato.addClientBehavior("focus", aja_selec_fila);
                        aut_dato.setValueExpression("value", new StringBuffer("fila.campos[").append(i).append("]").toString());
                        aut_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        aut_dato.setMetodoCompletarRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].autocompletar").toString());
                        aut_dato.setVar("auto");
                        aut_dato.setConverter(new ConvertidorAutoCompletar());
                        if (columnas[i].getEstilo() != null) {
                            aut_dato.setStyle("width:97%;" + columnas[i].getEstilo());
                        } else {
                            aut_dato.setStyle("width:97%;");
                        }
                        StringBuilder str = new StringBuilder();
                        if (columnas[i].getNumeroColumnasCombo() > 2) {
                            aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                            int vueltas = 25;
                            for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                                Column col = new Column();
                                if (columnas[i].getImagenAutocompletar() != j) {
                                    if (j > 1) {
                                        str.append("  ");
                                    }
                                    str.append("#{").append("auto[").append(j).append("]}");
                                    Etiqueta eti = new Etiqueta();
                                    eti.setValueExpression("value", new StringBuilder("auto[").append(j).append("]").toString());
                                    col.getChildren().add(eti);
                                    aut_dato.getChildren().add(col);
                                } else {
                                    Imagen ima = new Imagen();
                                    ima.setValueExpression("value", new StringBuilder("auto[").append(j).append("]").toString());
                                    ima.setWidth("64");
                                    ima.setHeight("64");
                                    aut_dato.setMaxResults(5);
                                    col.getChildren().add(ima);
                                    aut_dato.getChildren().add(col);
                                }
                                vueltas += 85;
                            }
                            columnas[i].setLongitud(vueltas);
                            aut_dato.setSize((vueltas + 25) / columnas[i].getNumeroColumnasCombo());
                            aut_dato.setValueExpression("itemLabel", crearValueExpression2(str.toString()));
                        } else {
                            columnas[i].setLongitud(70);
                            aut_dato.setSize(50);
                            aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                            aut_dato.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            aut_dato.addClientBehavior("itemSelect", columnas[i].getMetodoChange());
                        } else {
                            aut_dato.addClientBehavior("itemSelect", aja_modifico_fila);
                        }
                        aut_dato.addClientBehavior("change", aja_modifico_fila);
                        if (columnas[i].getOnClick() != null) {
                            aut_dato.setOnclick(columnas[i].getOnClick());
                        }
                        columna.getChildren().add(aut_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        if (columnas[i].getNumeroColumnasCombo() > 2) {
                            eti_dato.setEscape(true);
                            eti_dato.setValueExpression("value", crearValueExpression2(str.toString().replace("auto", "fila.campos[" + i + "]")));
                        } else {
                            eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        }
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                    case "Radio": {
                        Radio rad_dato = new Radio();
                        if (columnas[i].isRadioVertical()) {
                            rad_dato.setVertical();
                        }
                        columnas[i].setLongitud(30);
                        rad_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        rad_dato.addClientBehavior("focus", aja_selec_fila);
                        rad_dato.setTitle(columnas[i].getNombreVisual());
                        rad_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        rad_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        rad_dato.setValueExpression("disabled", new StringBuilder("fila.lectura or ").append(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].lectura").toString());
                        if (columnas[i].getEstilo() != null) {
                            rad_dato.setStyle(new StringBuilder("border:none;").append(columnas[i].getEstilo()).toString());
                        } else {
                            rad_dato.setStyle("border:none;");
                        }
                        columnas[i].setLongitud(columnas[i].getListaRadio().size() * 15);

                        UISelectItems usi_opciones = new UISelectItems();
                        usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].listaRadio").toString()));
                        usi_opciones.setValueExpression("var", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].var").toString()));
                        usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion[0]"));
                        usi_opciones.setValueExpression("itemLabel", crearValueExpression("opcion[1]"));
                        rad_dato.getChildren().add(usi_opciones);
                        if (columnas[i].getMetodoChange() != null) {
                            rad_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            rad_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            rad_dato.setOnclick(columnas[i].getOnClick());
                        }
                        columna.getChildren().add(rad_dato);
                        break;
                    }
                    case "AreaTexto": {
                        AreaTexto ate_dato = new AreaTexto();
                        ate_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                        ate_dato.addClientBehavior("focus", aja_selec_fila);
                        ate_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                        ate_dato.setValueExpression("rendered", "fila.lectura==false and fila.visible");
                        ate_dato.setRows(1);
                        ate_dato.setAutoResize(false);
                        if (columnas[i].getEstilo() != null) {
                            ate_dato.setStyle((columnas[i].getEstilo()));
                        }
                        if (columnas[i].getOnKeyup() != null) {
                            ate_dato.setOnkeyup(columnas[i].getOnKeyup());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            ate_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            ate_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            ate_dato.setOnclick(columnas[i].getOnClick());
                        }
                        columna.getChildren().add(ate_dato);
                        CellEditor cell_editor = new CellEditor();
                        cell_editor.setValueExpression("rendered", crearValueExpression("fila.lectura"));
                        cell_editor.getFacets().put("input", new Etiqueta());
                        Etiqueta eti_dato = new Etiqueta();
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle(columnas[i].getEstilo());
                        }
                        eti_dato.setValueExpression("rendered", "fila.visible");
                        eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                        cell_editor.getFacets().put("output", eti_dato);
                        columna.getChildren().add(cell_editor);
                        break;
                    }
                }
            }
            int int_l = columnas[i].getLongitud();

            if (int_l < 20) {
                int_l = 17;
            }
            if (int_l < 100) {
                int_l = int_l * 5;
            } else if (int_l <= 250) {
                int_l = int_l * 3;
            }
            columna.setWidth(int_l);

            if (!columnaSuma.isEmpty()) {
                Etiqueta eti_total = new Etiqueta();
                eti_total.setStyle("text-align:right;font-size:13px;font-weight: bold;");
                eti_total.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
                columna.setFooter(eti_total);
            }

            this.getChildren().add(columna);
        }
    }

    public void filtrarColumna(AjaxBehaviorEvent evt) {
        this.getColumna(evt.getComponent().getParent().getParent().getId()).setSqlFiltro(((Texto) evt.getComponent()).getValue() + "");
        ejecutarSql();
    }

    public List<Fila> getFiltro() {
        return filtro;
    }

    public void setFiltro(List<Fila> filtro) {
        this.filtro = filtro;
    }

    private void construirTablaLectura() {
        Ajax aja_selec_fila = new Ajax();
        if (!campoPrimaria.isEmpty()) {
            getColumna(campoPrimaria).setAncho(6);
        }

        if (!metodo.isEmpty()) {
            aja_selec_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(metodo).toString());
        } else {
            aja_selec_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".seleccionarFila").toString());
            if (relacion.isEmpty()) {
                aja_selec_fila.setGlobal(false);
            }
        }
        this.addClientBehavior("rowSelect", aja_selec_fila);
        this.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filas").toString()));
        this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filaSeleccionada").toString()));
        this.setResizableColumns(true);
        this.setVar("fila");
        this.setValueExpression("rowKey", crearValueExpression("fila.rowKey"));
        this.setSelectionMode("single");
        this.setValueExpression("filteredValue", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filtro").toString()));

        for (int i = 0; i < columnas.length; i++) {
            Etiqueta eti_titulo = new Etiqueta();
            Column columna = new Column();
            columna.setValueExpression("rendered", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].visible").toString()));
            if (ordenar) {
                columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
            }
            columna.setResizable(true);
            eti_titulo.setValue(columnas[i].getNombreVisual());

            eti_titulo.setStyleClass(null);

            if (columnas[i].isFiltro()) {
                columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                columna.setFilterMatchMode(columnas[i].getFiltroModo());
            }
            if (columnas[i].getComentario() != null) {
                eti_titulo.setTitle(columnas[i].getComentario());
            }
            if (columnas[i].getEstiloColumna() != null) {
                columna.setStyle(columnas[i].getEstiloColumna());
            }
            columna.setHeader(eti_titulo);
            switch (columnas[i].getControl()) {
                case "Etiqueta":
                case "Texto":
                case "Mascara":
                case "Calendario":
                case "Hora":
                case "AreaTexto": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    if (columnas[i].getControl().equals("Calendario")) {
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            eti_dato.setConverter(new ConvertidorFecha(true));
                        } else {
                            eti_dato.setConverter(new ConvertidorFecha());
                        }
                    } else if (columnas[i].getControl().equals("Hora")) {
                        eti_dato.setConverter(new ConvertidorHora());
                    } else if (columnas[i].isFormatoNumero()) {
                        eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                    }
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Autocompletar":
                case "Combo": {
                    if (columnas[i].isFiltro()) {
                        columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        columna.setFilterMatchMode(columnas[i].getFiltroModo());
                    }
                    if (ordenar) {
                        columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    }
                    CellEditor cell_editor = new CellEditor();
                    cell_editor.getFacets().put("input", new Etiqueta());
                    Etiqueta eti_dato = new Etiqueta();
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    StringBuilder str = new StringBuilder();
                    for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                        if (j > 1) {
                            str.append("  ");
                        }
                        str.append("#{").append("fila.campos[").append(i).append("][").append(j).append("]}");
                    }
                    eti_dato.setValueExpression("value", crearValueExpression2(str.toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    cell_editor.getFacets().put("output", eti_dato);
                    columna.getChildren().add(cell_editor);
                    break;
                }
                case "Check":
                    Check che_dato = new Check();
                    che_dato.setDisabled(true);
                    che_dato.setTitle(columnas[i].getNombreVisual());
                    if (columnas[i].getEstilo() != null) {
                        che_dato.setStyle(columnas[i].getEstilo());
                    }
                    che_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    columna.getChildren().add(che_dato);
                    break;
                case "Clave": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    eti_dato.setValue("**********");
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    eti_dato.setValueExpression("rendered", "fila.visible");
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Radio":
                    Radio rad_dato = new Radio();
                    if (columnas[i].isRadioVertical()) {
                        rad_dato.setVertical();
                    }
                    columnas[i].setLongitud(30);
                    rad_dato.setDisabled(columnas[i].isLectura());
                    rad_dato.setTitle(columnas[i].getNombreVisual());
                    rad_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    rad_dato.setValueExpression("rendered", "fila.visible");
                    rad_dato.setDisabled(true);
                    if (columnas[i].getEstilo() != null) {
                        rad_dato.setStyle(new StringBuilder("width:200%;display:block;border:none;").append(columnas[i].getEstilo()).toString());
                    } else {
                        rad_dato.setStyle("width:200%;display:block;border:none;");
                    }
                    UISelectItems usi_opciones = new UISelectItems();
                    usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].listaRadio").toString()));
                    usi_opciones.setValueExpression("var", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].var").toString()));
                    usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion[0]"));
                    usi_opciones.setValueExpression("itemLabel", crearValueExpression("opcion[1]"));
                    rad_dato.getChildren().add(usi_opciones);
                    columna.getChildren().add(rad_dato);
                    break;
                case "Upload":
                    Link lin_download = new Link();
                    lin_download.setAjax(false);
                    lin_download.setValue(" Descargar Archivo");
                    lin_download.setStyleClass("fa fa-download fa-lg");
                    lin_download.setValueExpression("rendered", "fila.campos[" + i + "] !=null or fila.visible");
                    lin_download.addActionListener(new FileDownloadActionListener(crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString())));
                    columna.getChildren().add(lin_download);
                    break;
            }
            int int_l = columnas[i].getLongitud();
            if (int_l < 20) {
                int_l = 17;
            }
            if (int_l < 100) {
                int_l = int_l * 4;
            } else if (int_l <= 250) {
                int_l = int_l * 2;
            }

            columna.setWidth(int_l);

            if (!columnaSuma.isEmpty()) {
                Etiqueta eti_total = new Etiqueta();
                eti_total.setStyle("text-align:right;font-size:13px;font-weight: bold;");
                eti_total.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
                columna.setFooter(eti_total);
            }

            this.getChildren().add(columna);
        }
    }

    private void construirTablaSeleccion() {
        if (!campoPrimaria.isEmpty()) {
            this.getColumna(campoPrimaria).setVisible(false);
        }

        this.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.filas").toString()));
        this.setVar("fila");
        this.setValueExpression("rowKey", crearValueExpression("fila.rowKey"));
        this.setResizableColumns(true);
        this.setValueExpression("filteredValue", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.filtro").toString()));

        if (seleccionTabla.equals("multiple")) {
            this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.seleccionados").toString()));
            if (!metodoCheck.isEmpty()) {
                Ajax aja_check = new Ajax();
                if (metodoCheck.startsWith("mbe_index")) {
                    aja_check.setMetodoRuta(metodoCheck);
                } else {
                    aja_check.setMetodo(metodoCheck);
                }
                this.addClientBehavior("rowSelectCheckbox", aja_check);
            }

            if (!metodoUnselectCheck.isEmpty()) {
                Ajax aja_check = new Ajax();
                if (metodoUnselectCheck.startsWith("mbe_index")) {
                    aja_check.setMetodoRuta(metodoUnselectCheck);
                } else {
                    aja_check.setMetodo(metodoUnselectCheck);
                }
                this.addClientBehavior("rowUnselectCheckbox", aja_check);
            }

            Column col_seleccion = new Column();
            col_seleccion.setResizable(true);
            col_seleccion.setWidth(17);
            col_seleccion.setSelectionMode("multiple");
            this.getChildren().add(col_seleccion);
        } else {
            this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.filaSeleccionada").toString()));
            Column col_seleccion = new Column();
            col_seleccion.setResizable(true);
            col_seleccion.setWidth(17);
            col_seleccion.setSelectionMode("single");
            this.getChildren().add(col_seleccion);
        }

        for (int i = 0; i < columnas.length; i++) {
            columnas[i].setId(new StringBuilder(idCompleto).append(":").append(columnas[i].getNombre()).append("_").append(i).toString());
            Column columna = new Column();
            columna.setValueExpression("rendered", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.columnas[").append(i).append("].visible").toString()));
            columna.setId(columnas[i].getNombre());

            Etiqueta eti_titulo = new Etiqueta();
            eti_titulo.setValue(columnas[i].getNombreVisual());

            eti_titulo.setStyleClass(null);
            columna.setHeader(eti_titulo);
            if (ordenar) {
                columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
            }
            columna.setResizable(true);
            if (columnas[i].isFiltro()) {
                columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                columna.setFilterMatchMode(columnas[i].getFiltroModo());
            }
            if (columnas[i].getComentario() != null) {
                eti_titulo.setTitle(columnas[i].getComentario());
            }
            if (columnas[i].getEstiloColumna() != null) {
                columna.setStyle(columnas[i].getEstiloColumna());
            }
            switch (columnas[i].getControl()) {
                case "Texto":
                case "Mascara":
                case "Calendario":
                case "Hora":
                case "Upload":
                case "AreaTexto": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    if (columnas[i].getControl().equals("Calendario")) {
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            eti_dato.setConverter(new ConvertidorFecha(true));
                        } else {
                            eti_dato.setConverter(new ConvertidorFecha());
                        }
                    } else if (columnas[i].getControl().equals("Hora")) {
                        eti_dato.setConverter(new ConvertidorHora());
                    } else if (columnas[i].isFormatoNumero()) {
                        eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                    }
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Autocompletar":
                case "Combo": {
                    if (ordenar) {
                        columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    }
                    if (columnas[i].isFiltro()) {
                        columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        columna.setFilterMatchMode(columnas[i].getFiltroModo());
                    }
                    AutoCompletar aut_dato = new AutoCompletar();
                    aut_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    aut_dato.setTitle(columnas[i].getNombreVisual());
                    aut_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    aut_dato.setValueExpression("rendered", "fila.visible");
                    aut_dato.setDisabled(true);
                    aut_dato.setMetodoCompletarRuta(ruta + new StringBuilder(".tab_seleccion.columnas[").append(i).append("].autocompletar").toString());
                    aut_dato.setVar("auto");
                    aut_dato.setConverter(new ConvertidorAutoCompletar());
                    if (columnas[i].getNumeroColumnasCombo() > 2) {
                        aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                        StringBuilder str = new StringBuilder();
                        int vueltas = 25;
                        for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                            if (j > 1) {
                                str.append("  ");
                            }
                            str.append("#{").append("auto[").append(j).append("]}");
                            Column col = new Column();
                            Etiqueta eti = new Etiqueta();
                            eti.setValueExpression("value", new StringBuilder("auto[").append(j).append("]").toString());
                            col.getChildren().add(eti);
                            aut_dato.getChildren().add(col);
                            vueltas += 65;
                        }
                        if (columnas[i].getLongitud() != 0) { //calcula longitud
                            columnas[i].setLongitud(vueltas);
                        }
                        aut_dato.setSize((vueltas + 25) / columnas[i].getNumeroColumnasCombo());
                        aut_dato.setValueExpression("itemLabel", crearValueExpression2(str.toString()));
                    } else {
                        if (columnas[i].getLongitud() != 0) {
                            columnas[i].setLongitud(70); //valor x defecto   
                        }
                        aut_dato.setSize(50);
                        aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                        aut_dato.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
                    }
                    CellEditor cell_editor = new CellEditor();
                    cell_editor.getFacets().put("input", aut_dato);
                    Etiqueta eti_dato = new Etiqueta();
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    cell_editor.getFacets().put("output", eti_dato);
                    columna.getChildren().add(cell_editor);
                    break;
                }
                case "Check":
                    Check che_dato = new Check();
                    che_dato.setDisabled(true);
                    che_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    che_dato.setTitle(columnas[i].getNombreVisual());
                    che_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    columna.getChildren().add(che_dato);
                    break;
                case "Clave": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    eti_dato.setValue("**********");
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Radio":
                    Radio rad_dato = new Radio();
                    if (columnas[i].isRadioVertical()) {
                        rad_dato.setVertical();
                    }
                    columnas[i].setLongitud(30);
                    rad_dato.setDisabled(columnas[i].isLectura());
                    rad_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    rad_dato.setTitle(columnas[i].getNombreVisual());
                    rad_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    rad_dato.setValueExpression("rendered", "fila.visible");
                    rad_dato.setDisabled(true);
                    if (columnas[i].getEstilo() != null) {
                        rad_dato.setStyle(new StringBuilder("width:200%;display:block;border:none;").append(columnas[i].getEstilo()).toString());
                    } else {
                        rad_dato.setStyle("width:200%;display:block;border:none;");
                    }
                    UISelectItems usi_opciones = new UISelectItems();
                    usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.columnas[").append(i).append("].listaRadio").toString()));
                    usi_opciones.setValueExpression("var", crearValueExpression(new StringBuilder(ruta).append(".tab_seleccion.columnas[").append(i).append("].var").toString()));
                    usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion[0]"));
                    usi_opciones.setValueExpression("itemLabel", crearValueExpression("opcion[1]"));
                    rad_dato.getChildren().add(usi_opciones);
                    columna.getChildren().add(rad_dato);
                    break;
            }
            int int_l = columnas[i].getLongitud();
            if (int_l < 20) {
                int_l = 17;
            }
            if (int_l < 100) {
                int_l = int_l * 4;
            } else if (int_l <= 200) {
                int_l = int_l * 2;
            }
            if (int_l > 200) {
                int_l = 550;
            }

            columna.setWidth(int_l);

            if (!columnaSuma.isEmpty()) {
                Etiqueta eti_total = new Etiqueta();
                eti_total.setStyle("text-align:right;font-size:13px;font-weight: bold;");
                eti_total.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
                columna.setFooter(eti_total);
            }

            this.getChildren().add(columna);
        }
    }

    private void construirTablaCheck() {
        if (!campoPrimaria.isEmpty()) {
            this.getColumna(campoPrimaria).setVisible(false);
        }

        this.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filas").toString()));
        this.setVar("fila");
        this.setValueExpression("rowKey", crearValueExpression("fila.rowKey"));
        this.setResizableColumns(true);
        this.setValueExpression("filteredValue", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filtro").toString()));

        if (seleccionTabla.equals("multiple")) {
            //Check
            if (!metodoCheck.isEmpty()) {
                Ajax aja_check = new Ajax();
                if (metodoCheck.startsWith("mbe_index")) {
                    aja_check.setMetodoRuta(metodoCheck);
                } else {
                    aja_check.setMetodo(metodoCheck);
                }
                this.addClientBehavior("rowSelectCheckbox", aja_check);
            }
            if (!metodoUnselectCheck.isEmpty()) {
                Ajax aja_check = new Ajax();
                if (metodoUnselectCheck.startsWith("mbe_index")) {
                    aja_check.setMetodoRuta(metodoUnselectCheck);
                } else {
                    aja_check.setMetodo(metodoUnselectCheck);
                }
                this.addClientBehavior("rowUnselectCheckbox", aja_check);
            }
            this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".seleccionados").toString()));
            Column col_seleccion = new Column();
            col_seleccion.setResizable(true);
            col_seleccion.setWidth(17);
            col_seleccion.setSelectionMode("multiple");
            this.getChildren().add(col_seleccion);
        } else {
            //Radio
            this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filaSeleccionada").toString()));
            Column col_seleccion = new Column();
            col_seleccion.setResizable(true);
            col_seleccion.setWidth(17);
            col_seleccion.setSelectionMode("single");
            this.getChildren().add(col_seleccion);
        }
        for (int i = 0; i < columnas.length; i++) {
            Column columna = new Column();
            columna.setValueExpression("rendered", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].visible").toString()));
            Etiqueta eti_titulo = new Etiqueta();
            eti_titulo.setValue(columnas[i].getNombreVisual());

            eti_titulo.setStyleClass(null);
            columna.setHeader(eti_titulo);
            if (ordenar) {
                columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
            }
            columna.setResizable(true);
            if (columnas[i].isFiltro()) {
                columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                columna.setFilterMatchMode(columnas[i].getFiltroModo());
            }
            if (columnas[i].getComentario() != null) {
                eti_titulo.setTitle(columnas[i].getComentario());
            }
            if (columnas[i].getEstiloColumna() != null) {
                columna.setStyle(columnas[i].getEstiloColumna());
            }
            switch (columnas[i].getControl()) {
                case "Texto":
                case "Mascara":
                case "Calendario":
                case "Hora":
                case "Upload":
                case "AreaTexto": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    if (columnas[i].getControl().equals("Calendario")) {
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            eti_dato.setConverter(new ConvertidorFecha(true));
                        } else {
                            eti_dato.setConverter(new ConvertidorFecha());
                        }
                    } else if (columnas[i].getControl().equals("Hora")) {
                        eti_dato.setConverter(new ConvertidorHora());
                    } else if (columnas[i].isFormatoNumero()) {
                        eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                    }
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Autocompletar":
                case "Combo": {
                    if (ordenar) {
                        columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    }
                    if (columnas[i].isFiltro()) {
                        columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        columna.setFilterMatchMode(columnas[i].getFiltroModo());
                    }
                    AutoCompletar aut_dato = new AutoCompletar();
                    aut_dato.setTitle(columnas[i].getNombreVisual());
                    aut_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    aut_dato.setValueExpression("rendered", "fila.visible");
                    aut_dato.setDisabled(true);
                    aut_dato.setMetodoCompletarRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].autocompletar").toString());
                    aut_dato.setVar("auto");
                    aut_dato.setConverter(new ConvertidorAutoCompletar());
                    if (columnas[i].getNumeroColumnasCombo() > 2) {
                        aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                        StringBuilder str = new StringBuilder();
                        int vueltas = 25;
                        for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                            if (j > 1) {
                                str.append("  ");
                            }
                            str.append("#{").append("auto[").append(j).append("]}");
                            Column col = new Column();
                            Etiqueta eti = new Etiqueta();
                            eti.setValueExpression("value", new StringBuilder("auto[").append(j).append("]").toString());
                            col.getChildren().add(eti);
                            aut_dato.getChildren().add(col);
                            vueltas += 65;
                        }
                        if (columnas[i].getLongitud() != 0) { //calcula longitud
                            columnas[i].setLongitud(vueltas);
                        }
                        aut_dato.setSize((vueltas + 25) / columnas[i].getNumeroColumnasCombo());
                        aut_dato.setValueExpression("itemLabel", crearValueExpression2(str.toString()));
                    } else {
                        if (columnas[i].getLongitud() != 0) {
                            columnas[i].setLongitud(70); //valor x defecto   
                        }
                        aut_dato.setSize(50);
                        aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                        aut_dato.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
                    }
                    CellEditor cell_editor = new CellEditor();
                    cell_editor.getFacets().put("input", aut_dato);
                    Etiqueta eti_dato = new Etiqueta();
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    cell_editor.getFacets().put("output", eti_dato);
                    columna.getChildren().add(cell_editor);
                    break;
                }
                case "Check":
                    Check che_dato = new Check();
                    che_dato.setDisabled(true);
                    che_dato.setTitle(columnas[i].getNombreVisual());
                    che_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    columna.getChildren().add(che_dato);
                    break;
                case "Clave": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    eti_dato.setValue("**********");
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Radio":
                    Radio rad_dato = new Radio();
                    if (columnas[i].isRadioVertical()) {
                        rad_dato.setVertical();
                    }
                    if (columnas[i].getEstilo() != null) {
                        rad_dato.setStyle(new StringBuilder("width:200%;display:block;border:none;").append(columnas[i].getEstilo()).toString());
                    } else {
                        rad_dato.setStyle("width:200%;display:block;border:none;");
                    }
                    columnas[i].setLongitud(30);
                    rad_dato.setDisabled(columnas[i].isLectura());
                    rad_dato.setTitle(columnas[i].getNombreVisual());
                    rad_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    rad_dato.setValueExpression("rendered", "fila.visible");
                    rad_dato.setDisabled(true);
                    UISelectItems usi_opciones = new UISelectItems();
                    usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].listaRadio").toString()));
                    usi_opciones.setValueExpression("var", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].var").toString()));
                    usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion[0]"));
                    usi_opciones.setValueExpression("itemLabel", crearValueExpression("opcion[1]"));
                    rad_dato.getChildren().add(usi_opciones);
                    columna.getChildren().add(rad_dato);
                    break;
            }
            int int_l = columnas[i].getLongitud();
            if (int_l < 20) {
                int_l = 17;
            }
            if (int_l < 100) {
                int_l = int_l * 4;
            } else if (int_l <= 200) {
                int_l = int_l * 2;
            }
            if (int_l > 200) {
                int_l = 550;
            }

            columna.setWidth(int_l);

            if (!columnaSuma.isEmpty()) {
                Etiqueta eti_total = new Etiqueta();
                eti_total.setStyle("text-align:right;font-size:13px;font-weight: bold;");
                eti_total.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
                columna.setFooter(eti_total);
            }

            this.getChildren().add(columna);
        }
    }

    private void construirTablaSeleccionMultiple() {
        Ajax aja_selec_fila = new Ajax();
        if (!campoPrimaria.isEmpty()) {
            this.getColumna(campoPrimaria).setVisible(false);
        }
        this.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filas").toString()));
        this.setVar("fila");
        this.setValueExpression("rowKey", crearValueExpression("fila.rowKey"));
        this.setResizableColumns(true);

        if (!metodo.isEmpty()) {
            aja_selec_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(metodo).toString());
        } else {
            aja_selec_fila.setMetodoRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".seleccionarFila").toString());
            if (relacion.isEmpty()) {
                aja_selec_fila.setGlobal(false);
            }
        }

        this.addClientBehavior("rowSelect", aja_selec_fila);
        this.setValueExpression("filteredValue", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".filtro").toString()));
        this.setValueExpression("selection", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".seleccionados").toString()));
        this.setSelectionMode("multiple");

        for (int i = 0; i < columnas.length; i++) {
            columnas[i].setId(new StringBuilder(idCompleto).append(":").append(columnas[i].getNombre()).append("_").append(i).toString());
            Column columna = new Column();
            columna.setValueExpression("rendered", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].visible").toString()));
            columna.setId(columnas[i].getNombre());
            Etiqueta eti_titulo = new Etiqueta();
            eti_titulo.setValue(columnas[i].getNombreVisual());

            eti_titulo.setStyleClass(null);

            columna.setHeader(eti_titulo);
            if (ordenar) {
                columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
            }
            columna.setResizable(true);
            if (columnas[i].isFiltro()) {
                columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                columna.setFilterMatchMode(columnas[i].getFiltroModo());
            }
            if (columnas[i].getComentario() != null) {
                eti_titulo.setTitle(columnas[i].getComentario());
            }
            if (columnas[i].getEstiloColumna() != null) {
                columna.setStyle(columnas[i].getEstiloColumna());
            }
            switch (columnas[i].getControl()) {
                case "Texto":
                case "Mascara":
                case "Calendario":
                case "Hora":
                case "Upload":
                case "AreaTexto": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    if (columnas[i].getControl().equals("Calendario")) {
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            eti_dato.setConverter(new ConvertidorFecha(true));
                        } else {
                            eti_dato.setConverter(new ConvertidorFecha());
                        }
                    } else if (columnas[i].getControl().equals("Hora")) {
                        eti_dato.setConverter(new ConvertidorHora());
                    } else if (columnas[i].isFormatoNumero()) {
                        eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                    }
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Autocompletar":
                case "Combo": {
                    if (ordenar) {
                        columna.setValueExpression("sortBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    }
                    if (columnas[i].isFiltro()) {
                        columna.setValueExpression("filterBy", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                        columna.setFilterMatchMode(columnas[i].getFiltroModo());
                    }
                    AutoCompletar aut_dato = new AutoCompletar();
                    aut_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    aut_dato.setTitle(columnas[i].getNombreVisual());
                    aut_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    aut_dato.setValueExpression("rendered", "fila.visible");
                    aut_dato.setDisabled(true);
                    aut_dato.setMetodoCompletarRuta(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].autocompletar").toString());
                    aut_dato.setVar("auto");
                    aut_dato.setConverter(new ConvertidorAutoCompletar());
                    if (columnas[i].getNumeroColumnasCombo() > 2) {
                        aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                        StringBuilder str = new StringBuilder();
                        int vueltas = 25;
                        for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                            if (j > 1) {
                                str.append("  ");
                            }
                            str.append("#{").append("auto[").append(j).append("]}");
                            Column col = new Column();
                            Etiqueta eti = new Etiqueta();
                            eti.setValueExpression("value", new StringBuilder("auto[").append(j).append("]").toString());
                            col.getChildren().add(eti);
                            aut_dato.getChildren().add(col);
                            vueltas += 65;
                        }
                        if (columnas[i].getLongitud() != 0) { //calcula longitud
                            columnas[i].setLongitud(vueltas);
                        }
                        aut_dato.setSize((vueltas + 25) / columnas[i].getNumeroColumnasCombo());
                        aut_dato.setValueExpression("itemLabel", crearValueExpression2(str.toString()));
                    } else {
                        if (columnas[i].getLongitud() != 0) {
                            columnas[i].setLongitud(70); //valor x defecto   
                        }

                        aut_dato.setSize(50);
                        aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                        aut_dato.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
                    }
                    CellEditor cell_editor = new CellEditor();
                    cell_editor.getFacets().put("input", aut_dato);
                    Etiqueta eti_dato = new Etiqueta();
                    if (columnas[i].getEstilo() != null) {
                        eti_dato.setStyle(columnas[i].getEstilo());
                    }
                    eti_dato.setValueExpression("value", crearValueExpression(new StringBuilder("fila.campos[").append(i).append("][1]").toString()));
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    cell_editor.getFacets().put("output", eti_dato);
                    columna.getChildren().add(cell_editor);
                    break;
                }
                case "Check":
                    Check che_dato = new Check();
                    che_dato.setDisabled(true);
                    che_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    che_dato.setTitle(columnas[i].getNombreVisual());
                    che_dato.setValueExpression("value", new StringBuilder("fila.campos[").append(i).append("]").toString());
                    columna.getChildren().add(che_dato);
                    break;
                case "Clave": {
                    Etiqueta eti_dato = new Etiqueta();
                    eti_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    eti_dato.setTitle(columnas[i].getNombreVisual());
                    eti_dato.setValue("**********");
                    eti_dato.setValueExpression("rendered", crearValueExpression("fila.visible"));
                    columna.getChildren().add(eti_dato);
                    break;
                }
                case "Radio":
                    Radio rad_dato = new Radio();
                    if (columnas[i].isRadioVertical()) {
                        rad_dato.setVertical();
                    }
                    columnas[i].setLongitud(30);
                    rad_dato.setDisabled(columnas[i].isLectura());
                    rad_dato.setId(new StringBuilder(columnas[i].getNombre()).append("_").append(i).toString());
                    rad_dato.setTitle(columnas[i].getNombreVisual());
                    rad_dato.setValueExpression("value", "fila.campos[" + i + "]");
                    rad_dato.setValueExpression("rendered", "fila.visible");
                    rad_dato.setDisabled(true);
                    if (columnas[i].getEstilo() != null) {
                        rad_dato.setStyle(new StringBuilder("width:200%;display:block;border:none;").append(columnas[i].getEstilo()).toString());
                    } else {
                        rad_dato.setStyle("width:200%;display:block;border:none;");
                    }
                    UISelectItems usi_opciones = new UISelectItems();
                    usi_opciones.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].listaRadio").toString()));
                    usi_opciones.setValueExpression("var", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].var").toString()));
                    usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion[0]"));
                    usi_opciones.setValueExpression("itemLabel", crearValueExpression("opcion[1]"));
                    rad_dato.getChildren().add(usi_opciones);
                    columna.getChildren().add(rad_dato);
                    break;
            }
            int int_l = columnas[i].getLongitud();
            if (int_l < 20) {
                int_l = 17;
            }
            if (int_l < 100) {
                int_l = int_l * 4;
            } else if (int_l <= 200) {
                int_l = int_l * 2;
            }
            if (int_l > 200) {
                int_l = 550;
            }

            columna.setWidth(int_l);

            if (!columnaSuma.isEmpty()) {
                Etiqueta eti_total = new Etiqueta();
                eti_total.setStyle("text-align:right;font-size:13px;font-weight: bold;");
                eti_total.setValueExpression("value", crearValueExpression(new StringBuilder(ruta).append(".").append(this.getId()).append(".columnas[").append(i).append("].totalFormato").toString()));
                columna.setFooter(eti_total);
            }

            this.getChildren().add(columna);
        }
    }

    public void onSelect(String metodo) {
        this.metodo = metodo;
    }

    public void onSelectCheck(String metodo) {
        this.metodoCheck = metodo;

    }

    public void onUnselectCheck(String metodo) {
        this.metodoUnselectCheck = metodo;

    }

    public void onFilter(String metodofiltro) {
        this.metodoFiltro = metodofiltro;

    }

    public void seleccionarFila(DateSelectEvent evt) {
        if (isValidarInsertar() && foco_valida_insertar != null) {
            setFilaSeleccionada(foco_valida_insertar);
            pintarFilaActual();
        } else {
            int nueva_fila = this.getRowIndex();
            if (filaActual != nueva_fila) {
                //Si cambio de fila
                filaActual = nueva_fila;
                filaSeleccionada = filas.get(filaActual);
                for (Tabla relacion1 : relacion) {
                    relacion1.ejecutarValorForanea(getValorSeleccionado());
                }
                pintarFilaActual();
            }
        }
    }

    public void seleccionarFila(AjaxBehaviorEvent evt) {
        if (isValidarInsertar() && foco_valida_insertar != null) {
            setFilaSeleccionada(foco_valida_insertar);
            pintarFilaActual();
        } else {
            int nueva_fila = this.getRowIndex();
            if (filaActual != nueva_fila) {
                //Si cambio de fila
                filaActual = nueva_fila;
                filaSeleccionada = filas.get(filaActual);
                for (Tabla relacion1 : relacion) {
                    relacion1.ejecutarValorForanea(getValorSeleccionado());
                }
                pintarFilaActual();
            }
        }
    }

    private void pintarFilaActual() {
        //pinta la fila actual sin necesidad del dar click
        framework.ejecutarJavaScript(new StringBuilder(this.getId()).append(".unselectAllRows();").toString());//borra la seleccion                
        framework.ejecutarJavaScript(new StringBuilder(this.getId()).append(".selectRow(").append((getFilaActual() - getFirst())).append(", false);").toString()); //selecciona nueva fila    
    }

    public void seleccionarFila(SelectEvent evt) {
        if (isValidarInsertar() && foco_valida_insertar != null) {
            setFilaSeleccionada(foco_valida_insertar);
            int nueva_fila = filas.indexOf((Fila) evt.getObject());
            if (filaActual != nueva_fila) {
                framework.addUpdate(idCompleto);
            }
        } else {
            int nueva_fila = filas.indexOf((Fila) evt.getObject());
            if (filaActual != nueva_fila) {
                filaActual = nueva_fila;
                for (Tabla relacion1 : relacion) {
                    relacion1.ejecutarValorForanea(getValorSeleccionado());
                }
            }
        }
    }

    public void modificar(AjaxBehaviorEvent evt) {
        this.modificar();
    }

    public void modificar(DateSelectEvent evt) {
        this.modificar();
    }

    private void modificar() {
        int num_fila;
        if (isTipoFormulario()) {
            num_fila = filaActual;
        } else {
            num_fila = this.getRowIndex();
        }
        //si se modifica otra fila y no ejecuto el selectevent o el focus
        if (filaActual != num_fila) {
            filaActual = num_fila;
            filaSeleccionada = filas.get(filaActual);
            for (Tabla relacion1 : relacion) {
                relacion1.ejecutarValorForanea(getValorSeleccionado());
            }
            pintarFilaActual();
        }
        if (insertadas.indexOf(filas.get(num_fila).getRowKey()) < 0) {
            if (modificadas.indexOf(filas.get(num_fila).getRowKey()) < 0) {
                modificadas.add(filas.get(num_fila).getRowKey());
            }
        }
    }

    public void modificar(ValueChangeEvent evt) {
        this.modificar();
    }

    public void modificar(SelectEvent evt) {
        ((AutoCompletar) evt.getComponent()).onSelect(evt);
        this.modificar();
    }

    public void modificar(int numero_fila) {
        if (modificadas.indexOf(filas.get(numero_fila).getRowKey()) < 0) {
            modificadas.add(filas.get(numero_fila).getRowKey());
        }
    }

    public void insertar() {
        if (!lectura) {
            //Para que no inserte si tiene tabla padre
            if (!campoForanea.isEmpty()) {
                if (dibujo) {
                    if (TABLA_PADRE == null) {
                        buscarComponentePadre(framework.getPantalla());
                    }
                    if (TABLA_PADRE != null && TABLA_PADRE.getTotalFilas() > 0) {
                        if (validarInsertar == true && insertadas.size() > 0 && getTotalFilas() > 0) {
                            framework.agregarMensajeInfo("No se puede Insertar", "Debe guardar el registro que esta trabajando");
                        } else {
                            crearFila();
                        }
                    } else {
                        framework.agregarMensajeInfo("No se puede Insertar", "La tabla esta relacionada ");
                    }
                } else {
                    //Si no se dibujo la tabla
                    crearFila();
                }
            } else {
                if (validarInsertar == true && insertadas.size() > 0 && getTotalFilas() > 0) {
                    framework.agregarMensajeInfo("No se puede Insertar", "Debe guardar el registro que esta trabajando");
                } else {
                    crearFila();
                }
            }
            ///para que no me deje seleccionar
            if (validarInsertar && insertadas.size() > 0) {
                foco_valida_insertar = getFilaSeleccionada();
            }
        }
    }

    private void crearFila() {
        Object campos[] = new Object[columnas.length];
        Fila fila_nueva = new Fila();
        fila_nueva.setCampos(campos);
        fila_nueva.setRowKey(String.valueOf(key));
        filaActual = 0;
        insertadas.add(String.valueOf(key));
        filas.addFirst(fila_nueva);
        filaSeleccionada = fila_nueva;
        if (!campoForanea.isEmpty()) {
            setValor(campoForanea, valorforanea);
        }
        if (!campoPadre.isEmpty()) {
            setValor(campoPadre, valorPadre);
        }
        //Valor por defecto
        for (Columna columnaActual : columnas) {
            if (columnaActual.getValorDefecto() != null) {
                setValor(columnaActual.getNombre(), columnaActual.getValorDefecto());
            }
        }

        //Asigna clave foranea a las relaciones
        for (Tabla relacionActual : relacion) {
            relacionActual.setValorForanea(getValorSeleccionado());
            relacionActual.limpiar();
        }
        key++;

        if (dibujo) {
            //JQuery que pone el foco sobre el primer input que no este bloqueado de la tabla
            String str_nombre = this.getClientId();
            str_nombre = str_nombre.replace(":", "\\\\:");
            framework.ejecutarJavaScript(new StringBuilder("$('#").append(str_nombre).append(" td:not(.filter) :input:visible:enabled:first').focus();").toString());
            paginaInicio();
            updateTablas();
        }
    }

    public void paginaInicio() {
        if (dibujo && !tipoFormulario) {
            framework.ejecutarJavaScript(this.getWidgetVar() + ".getPaginator().setPage(0)");
        }
    }

    private Fila buscaFila(String rowkey) {
        //Retorna la Fila segun la rowKey
        for (Fila filaActua : filas) {
            if (filaActua.getRowKey().equals(rowkey)) {
                return filaActua;
            }
        }
        return null;
    }

    public int getNumeroFila(String rowkey) {
        //Retorna la Fila segun la rowKey

        for (int i = 0; i < filas.size(); i++) {
            if (filas.get(i).getRowKey().equals(rowkey)) {
                return i;
            }
        }
        return -1;
    }

    public Fila getFila(String rowkey) {
        //Retorna la Fila segun la rowKey
        for (Fila fila : filas) {
            if (fila.getRowKey().equals(rowkey)) {
                return fila;
            }
        }
        return null;
    }

    public boolean guardar() {
        boolean boo_sql = false;
        if (insertadas.size() > 0) {
            boo_sql = true;
            insertadasCodigos = new ArrayList();
            String campos = "";
            String tipoArchivos = "";
            for (int i = 0; i < columnas.length; i++) {

                if (columnas[i].isExterna() == false) {
                    if (columnas[i].getControl().equals("Upload") && columnas[i].getCarpeta() == null) { //Guarda en la bdd el archivo
                        if (!tipoArchivos.isEmpty()) {
                            tipoArchivos += ", ";
                        }
                        tipoArchivos += columnas[i].getNombre() + "=? ";
                    }
                    if (i != 0 && !campos.isEmpty()) {
                        campos = campos.concat(", ");
                    }
                    campos = campos.concat(columnas[i].getNombre());
                }
            }

            long maximo = 0;
            if (generarPrimaria) {
                if (getNumeroPrimaria() == 1) {
                    maximo = conexion.getMaximo(tabla, campoPrimaria, insertadas.size());
                } else {
                    maximo = conexion.getMaximoMultiple(tabla, getPrimariaCalcula(), " WHERE ".concat(getCondicionPrimaria()), insertadas.size());
                }
            }

            String columnasUnique = "";
            String valoresUnique = "";
            String nombresUnique = "";

            for (int i = 0; i < insertadas.size(); i++) {

                String sqlArchivo = null;
                List<byte[]> lisArchivos = null;
                if (tieneArchivos) {
                    sqlArchivo = "UPDATE ".concat(tabla).concat(" SET ").concat(tipoArchivos).concat(" WHERE ").concat(getPrimariaCalcula()).concat("=").concat(String.valueOf(maximo));
                    lisArchivos = new ArrayList();
                }

                int indice = filas.indexOf(buscaFila(insertadas.get(i)));
                Object[] fila = filas.get(indice).getCampos();
                String valores = "";
                if (!campoRecursivo.isEmpty()) {
                    this.setValor(indice, campoRecursivo, getRecursivo(valorPadre, i));
                }
                for (int j = 0; j < fila.length; j++) {
                    if (columnas[j].isExterna() == false) {
                        Object valor = fila[j];
                        // valido si la columna es requerida 
                        if (columnas[j].isRequerida() && (!columnas[j].getNombre().equalsIgnoreCase(getPrimariaCalcula())) && (valor == null || valor.toString().isEmpty())) {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción null, debe ingresar un valor en el campo ".concat(columnas[j].getNombreVisual()).concat(" "));
                            conexion.getSqlPantalla().clear();
                            conexion.rollbackPantalla(framework.getPantalla());
                            return false;
                        }
                        if (valor == null) {
                            //si tiene campo de pista de auditoria
                            if (isCampoPistaAuditoriaInsert(columnas[j].getNombre())) {
                                valor = getValorCampoPistaAuditoria(columnas[j].getNombre());
                            } else {
                                valor = "NULL";
                                if (columnas[j].getControl().equals("Upload")) {
                                    if (tieneArchivos) {
                                        if (columnas[j].getCarpeta() == null) { //Guarda en la bdd el archivo null
                                            lisArchivos.add(null);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (valor.toString().isEmpty()) {
                                valor = "NULL";
                            } else {
                                /////////////CONVERTER                    
                                if (columnas[j].getControl().equals("Autocompletar") || columnas[j].getControl().equals("Combo")) {
                                    valor = ((Object[]) valor)[0];
                                }
                                if (columnas[j].getControl().equals("Mascara") && columnas[j].isQuitarCaracteresEspeciales()) {
                                    valor = quitarCaracteres(valor + "");
                                }
                                if (columnas[j].getControl().equals("Upload")) {
                                    if (tieneArchivos) {
                                        if (columnas[j].getCarpeta() == null) { //Guarda en la bdd el archivo                                            
                                            lisArchivos.add(((UploadStreamedContent) valor).getBytes());
                                            valor = "NULL";
                                        }
                                    }
                                }

                                switch (columnas[j].getTipoJava()) {
                                    case "java.lang.String":
                                    case "java.lang.Character":
                                        ///////////////////////////PARA QUE SOLO GUARDE EN MAYUSCULAS
                                        if (!columnas[j].isMayusculas()) {
                                            valor = "'".concat(valor.toString().trim()).concat("'");
                                        } else {
                                            valor = "'".concat(valor.toString().toUpperCase().trim()).concat("'");
                                        }
                                        break;
                                    case "java.sql.Date":
                                        valor = conexion.getFormatoFechaSQL(framework.getFormatoFecha(valor));
                                        break;
                                    case "java.sql.Timestamp":
                                        valor = "" + conexion.getFormatoHoraSQL(framework.getFormatoHora(valor)) + "";
                                        break;
                                    case "java.sql.Time":
                                        valor = "" + conexion.getFormatoHoraSQL(framework.getFormatoHora(valor)) + "";
                                        break;
                                }
                            }
                            //UNIQUE
                            if (columnas[j].isUnico()) {
                                if (!columnasUnique.isEmpty()) {
                                    columnasUnique = columnasUnique.concat(",");
                                    valoresUnique = valoresUnique.concat(",");
                                    nombresUnique = nombresUnique.concat(", ");
                                }
                                columnasUnique = columnasUnique.concat(columnas[j].getNombre());
                                valoresUnique = valoresUnique.concat(valor.toString().replace(",", "^"));
                                nombresUnique = nombresUnique.concat(columnas[j].getNombreVisual());
                            }
                        }
                        if (columnas[j].getNombre().equalsIgnoreCase(getPrimariaCalcula())) {
                            if (relacion.size() > 0) {
                                actualizarForaneaRelaciones(String.valueOf(maximo));
                            }
                            if (isGenerarPrimaria()) {
                                insertadasCodigos.add(String.valueOf(maximo));
                                valor = maximo;
                                setValor(indice, getPrimariaCalcula(), String.valueOf(maximo));
                                maximo++;
                            }
                        }
                        if (j != 0 && !valores.isEmpty()) {
                            valores = valores.concat(", ");
                        }
                        valores += valor;
                    }
                }

                ///Validacion unique en los insertados
                if (!columnasUnique.isEmpty()) {
                    if (validarUnicoInsertar(columnasUnique.split(","))) {
                        if (columnasUnique.split(",").length > 1) {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción única, ya existe  un registro con el valor de los campos : " + nombresUnique + " ");
                        } else {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción única, ya existe  un registro con el valor del campo : " + nombresUnique + " ");
                        }
                        conexion.getSqlPantalla().clear();
                        conexion.rollbackPantalla(framework.getPantalla());
                        return false;
                    }
                }

                ///Validacion unique
                if (!columnasUnique.isEmpty()) {
                    if (validarUnico(columnasUnique.split(","), valoresUnique.split(","))) {
                        if (columnasUnique.split(",").length > 1) {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción única, ya existe  un registro con el valor de los campos : " + nombresUnique + " ");
                        } else {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción única, ya existe  un registro con el valor del campo : " + nombresUnique + " ");
                        }
                        conexion.getSqlPantalla().clear();
                        conexion.rollbackPantalla(framework.getPantalla());
                        return false;
                    }
                }

                String sentencia = "INSERT INTO ".concat(tabla).concat("(").concat(campos).concat(") VALUES (").concat(valores).concat(")");
                conexion.agregarSqlPantalla(sentencia);
                if (tieneArchivos) {
                    conexion.agregarArchivos(sqlArchivo, lisArchivos);
                }

                if (auditoria == true) {
                    String sql_auditoria = "INSERT INTO tbl_auditoria "
                            .concat("(ide_audi,fecha_audi,hora_audi,ide_usua,sql_audi,accion_audi,ip_audi,tabla_audi,id_empresa,id_sucursal,id_opcion) ")
                            .concat(" values ( (SELECT  COALESCE(MAX(ide_audi),0)+1  FROM tbl_auditoria),").concat(framework.getFormatoFechaSQL(framework.getFechaActual())).concat(", ")
                            .concat(" ").concat(framework.getFormatoHoraSQL(framework.getHoraActual())).concat(",")
                            .concat(" ").concat(framework.getVariable("id_usuario")).concat(",")
                            .concat(" '").concat(sentencia.replace("'", "''")).concat("',")
                            .concat(" 'INSERTAR',")
                            .concat(" '").concat(framework.getIp()).concat("',")
                            .concat(" '").concat(tabla).concat("',")
                            .concat(" ").concat(id_empresa).concat(",")
                            .concat(" ").concat(id_sucursal).concat(",")
                            .concat(" ").concat(framework.getVariable("id_opcion")).concat(")");
                    framework.getConexion().agregarSqlPantalla(sql_auditoria);
                }

            }
        }
        if (eliminadas.size() > 0) {
            boo_sql = true;
            for (String eliminada : eliminadas) {
                String sentencia = "";
                if (getNumeroPrimaria() == 1) {
                    sentencia = "DELETE FROM ".concat(tabla).concat(" WHERE ").concat(campoPrimaria).concat(" =").concat(eliminada);
                } else if (getNumeroPrimaria() > 1) {
                    sentencia = "DELETE FROM ".concat(tabla).concat(" WHERE ").concat(eliminadasCondicion.get(eliminada));
                }
                conexion.agregarSqlPantalla(sentencia);
                if (auditoria == true) {
                    String sql_auditoria = "INSERT INTO tbl_auditoria "
                            .concat("(ide_audi,fecha_audi,hora_audi,ide_usua,sql_audi,accion_audi,ip_audi,tabla_audi,id_empresa,id_sucursal,id_opcion) ")
                            .concat(" values ( (SELECT  COALESCE(MAX(ide_audi),0)+1  FROM tbl_auditoria),").concat(framework.getFormatoFechaSQL(framework.getFechaActual())).concat(", ")
                            .concat(" ").concat(framework.getFormatoHoraSQL(framework.getHoraActual())).concat(",")
                            .concat(" ").concat(framework.getVariable("id_usuario")).concat(",")
                            .concat(" '").concat(sentencia.replace("'", "''")).concat("',")
                            .concat(" 'ELIMINAR',")
                            .concat(" '").concat(framework.getIp()).concat("',")
                            .concat(" '").concat(tabla).concat("',")
                            .concat(" ").concat(id_empresa).concat(",")
                            .concat(" ").concat(id_sucursal).concat(",")
                            .concat(" ").concat(framework.getVariable("id_opcion")).concat(")");
                    framework.getConexion().agregarSqlPantalla(sql_auditoria);
                }
            }
        }

        if (modificadas.size() > 0) {
            String columnasUnique = "";
            String valoresUnique = "";
            String nombresUnique = "";

            String tipoArchivos = "";
            boo_sql = true;

            for (String modificada : modificadas) {

                Object[] fila = buscaFila(modificada).getCampos();
                String valores = "";
                List<byte[]> lisArchivos = null;
                if (tieneArchivos) {
                    lisArchivos = new ArrayList();
                    tipoArchivos = "";
                }
                for (int j = 0; j < fila.length; j++) {

                    if (columnas[j].isExterna() == false && isCampoPistaAuditoriaInsert(columnas[j].getNombre()) == false) { //excluye externas y campos de pista de auditoria
                        Object valor = fila[j];
                        if (columnas[j].isRequerida() && (!columnas[j].getNombre().equalsIgnoreCase(campoPrimaria)) && (valor == null || valor.toString().isEmpty())) {
                            framework.agregarMensajeInfo("No se puede Guardar", "Debe ingresar un valor en el campo " + columnas[j].getNombreVisual() + " ");
                            conexion.getSqlPantalla().clear();
                            conexion.rollbackPantalla(framework.getPantalla());
                            return false;
                        }
                        if (valor == null) {
                            //si tiene campo de pista de auditoria
                            if (isCampoPistaAuditoriaUpdate(columnas[j].getNombre())) {
                                valor = getValorCampoPistaAuditoria(columnas[j].getNombre());
                            } else {
                                valor = "NULL";
                            }
                        } else {
                            if (valor.toString().isEmpty()) {
                                valor = "NULL";
                            } else {
                                /////////////CONVERTER                           
                                if (columnas[j].getControl().equals("Autocompletar") || columnas[j].getControl().equals("Combo")) {
                                    valor = ((Object[]) valor)[0];
                                }
                                if (columnas[j].getControl().equals("Mascara") && columnas[j].isQuitarCaracteresEspeciales()) {
                                    valor = quitarCaracteres(valor + "");
                                }

                                if (columnas[j].getControl().equals("Upload")) { ///DFJ                                   
                                    if (tieneArchivos) {
                                        if (columnas[j].getCarpeta() == null) { //Guarda en la bdd el archivo
                                            //Solo si cambio la imagen realizo cambio en la imagen
                                            try {
                                                lisArchivos.add(((UploadStreamedContent) valor).getBytes());
                                                if (!tipoArchivos.isEmpty()) {
                                                    tipoArchivos += ", ";
                                                }
                                                tipoArchivos += columnas[j].getNombre() + "=? ";
                                                valor = "NULL";
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                continue; // no modifica la imagen
                                            }
                                        }
                                    }
                                }

                                if (columnas[j].getTipoJava().equals("java.lang.String") || columnas[j].getTipoJava().equals("java.lang.Character")) {
                                    if (!columnas[j].isMayusculas()) {
                                        valor = "'" + valor.toString().trim() + "'";
                                    } else {
                                        valor = "'" + valor.toString().toUpperCase().trim() + "'";
                                    }
                                }
                                switch (columnas[j].getTipoJava()) {
                                    case "java.sql.Date":
                                        valor = conexion.getFormatoFechaSQL(framework.getFormatoFecha(valor));
                                        break;
                                    case "java.sql.Timestamp":
                                        valor = "" + conexion.getFormatoHoraSQL(framework.getFormatoHora(valor)) + "";
                                        break;
                                    case "java.sql.Time":
                                        valor = "" + conexion.getFormatoHoraSQL(framework.getFormatoHora(valor)) + "";
                                        break;
                                }
                            }

                            //UNIQUE
                            if (columnas[j].isUnico()) {
                                if (!columnasUnique.isEmpty()) {
                                    columnasUnique += ",";
                                    valoresUnique += ",";
                                    nombresUnique += ", ";
                                }
                                columnasUnique += columnas[j].getNombre();
                                valoresUnique += valor.toString().replace(",", "^");
                                nombresUnique += columnas[j].getNombreVisual();
                            }
                        }
                        if (j != 0 && !valores.isEmpty()) {
                            valores += ",";
                        }
                        valores += " " + columnas[j].getNombre() + "=" + valor;
                    }
                }
                ///Validacion unique
                if (!columnasUnique.isEmpty()) {
                    if (validarUnicoModificar(modificada, columnasUnique.split(","), valoresUnique.split(","))) {
                        if (columnasUnique.split(",").length > 1) {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción única, ya existe  un registro con el valor de los campos : " + nombresUnique + " ");
                        } else {
                            framework.agregarMensajeInfo("No se puede Guardar", "Restricción única, ya existe  un registro con el valor del campo : " + nombresUnique + " ");
                        }
                        conexion.rollbackPantalla(framework.getPantalla());
                        return false;
                    }
                }
                //cambio x clave compuesta
                String sentencia = "UPDATE ".concat(tabla).concat(" SET ").concat(valores).concat(" WHERE ").concat(getCondicionPrimaria(modificada));

                if (tieneArchivos && !tipoArchivos.isEmpty()) {
                    String sqlArchivo = "UPDATE ".concat(tabla).concat(" SET ").concat(tipoArchivos).concat(" WHERE ").concat(getCondicionPrimaria(modificada));
                    conexion.agregarArchivos(sqlArchivo, lisArchivos);
                }

                if (auditoria == true) {
                    //recupera el valor anterior para registrar en la auditoria
                    String str_sql_antes = "";
                    TablaGenerica tab_antes = new TablaGenerica();
                    tab_antes.setConexion(conexion);
                    tab_antes.setSql("SELECT * FROM ".concat(tabla).concat(" WHERE ").concat(getCondicionPrimaria(modificada)));
                    tab_antes.ejecutarSql();
                    for (int j = 0; j < tab_antes.getTotalColumnas(); j++) {
                        String str_nombre_columna = tab_antes.getColumnas()[j].getNombre();
                        str_sql_antes += " *" + str_nombre_columna + " = " + tab_antes.getValor(str_nombre_columna) + " ";
                    }
                    String sql_auditoria = "INSERT INTO tbl_auditoria "
                            .concat("(ide_audi,fecha_audi,hora_audi,ide_usua,sql_audi,accion_audi,ip_audi,tabla_audi,id_empresa,id_sucursal,id_opcion) ")
                            .concat(" values ((SELECT  COALESCE(MAX(ide_audi),0)+1  FROM tbl_auditoria),").concat(framework.getFormatoFechaSQL(framework.getFechaActual())).concat(", ")
                            .concat(" ").concat(framework.getFormatoHoraSQL(framework.getHoraActual())).concat(",")
                            .concat(" ").concat(framework.getVariable("id_usuario")).concat(",")
                            .concat(" '").concat(str_sql_antes + " *** ").concat(sentencia.replace("'", "''")).concat("',")
                            .concat(" 'MODIFICAR',")
                            .concat(" '").concat(framework.getIp()).concat("',")
                            .concat(" '").concat(tabla).concat("',")
                            .concat(" ").concat(id_empresa).concat(",")
                            .concat(" ").concat(id_sucursal).concat(",")
                            .concat(" ").concat(framework.getVariable("id_opcion")).concat(")");
                    framework.getConexion().agregarSqlPantalla(sql_auditoria);
                }
                conexion.agregarSqlPantalla(sentencia);
            }
        }
        if (boo_sql && dibujo) {
            framework.addUpdate(idCompleto);
            addUpdateArbol();
        }
        return true;
    }

    public Arbol getArbol() {
        return arbol;
    }

    public void agregarArbol(Arbol arbol) {
        this.arbol = arbol;
        arbol.onSelect(this.getId() + ".seleccionarArbol");
    }

    public void seleccionarArbol(NodeSelectEvent evt) {
        if (arbol != null) {
            arbol.seleccionarNodo(evt);
            ejecutarValorPadre(arbol.getValorSeleccionado());
        }
    }

    public void onRollback() {
        if (!campoPrimaria.isEmpty()) {
            for (int i = (insertadas.size() - 1); i >= 0; i--) {
                for (Fila fila : filas) {
                    if (fila.getRowKey().equals(insertadas.get(i))) {
                        fila.getCampos()[getNumeroColumna(campoPrimaria)] = null;
                        break;
                    }
                }
            }
        }

    }

    public void onCommit() {
        try {
            if (!campoPrimaria.isEmpty()) {
                for (int i = (insertadas.size() - 1); i >= 0; i--) {
                    for (Fila fila : filas) {
                        if (fila.getRowKey().equals(insertadas.get(i))) {
                            if (generarPrimaria) {
                                fila.getCampos()[getNumeroColumna(campoPrimaria)] = insertadasCodigos.get(i);
                                fila.setRowKey(insertadasCodigos.get(i) + "");
                                if (arbol != null) {
                                    arbol.onCommitInsertar(fila.getRowKey(), fila.getCampos()[getNumeroColumna(campoNombre)] + "");
                                }
                            }
                            if (recuperarLectura) {
                                fila.setLectura(true);
                            }
                            break;
                        }
                    }
                }
            }
            if (arbol != null) {
                for (int i = (modificadas.size() - 1); i >= 0; i--) {
                    Fila fila = buscaFila(modificadas.get(i));
                    arbol.onCommitModificar(fila.getRowKey(), fila.getCampos()[getNumeroColumna(campoNombre)] + "");
                }
                for (int i = (eliminadas.size() - 1); i >= 0; i--) {
                    arbol.onCommitEliminar(eliminadas.get(i));
                }
            }
            for (Tabla relacion1 : relacion) {
                relacion1.setValorForanea(getValorSeleccionado());
            }
        } catch (Exception e) {
        }
        foco_valida_insertar = null;
        insertadas = new ArrayList();
        modificadas = new ArrayList();
        eliminadas = new ArrayList();
        insertadasCodigos.clear();
        key = 'A';
    }

    public boolean eliminar() {
        boolean boo_elimino = false;
        if (!lectura) {
            if (filas.size() > 0) {
                if (insertadas.indexOf(filas.get(filaActual).getRowKey()) >= 0) {
                    insertadas.remove(filas.get(filaActual).getRowKey());
                    filas.remove(filaActual);
                    boo_elimino = true;
                    for (Tabla relacion1 : relacion) {
                        relacion1.ejecutarValorForanea(getValorSeleccionado());
                    }
                    ///para que  me deje seleccionar si valida el insertar
                    if (validarInsertar) {
                        foco_valida_insertar = null;
                    }
                } else {
                    try {
                        if (recuperarLectura == false) {
                            //cambio para claves compuestas       
                            String str_condi = getCondicionPrimaria(filas.get(filaActual).getRowKey());

                            String str_mensaje = "";
                            Statement sta_sentencia = null;
                            try {
                                getConexion().conectar(false);
                                getConexion().getConnection().setAutoCommit(false);
                                sta_sentencia = getConexion().getConnection().createStatement();
                                sta_sentencia.executeUpdate("DELETE FROM ".concat(tabla).concat(" WHERE ").concat(str_condi));
                            } catch (SQLException e) {
                                str_mensaje = e.getMessage();
                                try {
                                    sta_sentencia.getConnection().rollback();
                                } catch (Exception e1) {
                                }
                            } finally {
                                try {

                                    sta_sentencia.close();
                                } catch (Exception e) {
                                }
                                getConexion().desconectar(false);
                            }
                            if (str_mensaje.isEmpty()) {
                                eliminadas.add(filas.get(filaActual).getRowKey());
                                eliminadasCondicion.put(filas.get(filaActual).getRowKey(), str_condi);
                                modificadas.remove(filas.get(filaActual).getRowKey());
                                filas.remove(filaActual);
                                boo_elimino = true;
                                for (Tabla relacion1 : relacion) {
                                    relacion1.ejecutarValorForanea(getValorSeleccionado());
                                }
                            } else {
                                framework.agregarMensajeError("No se puede eliminar la fila", str_mensaje);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("ERROR AL ELIMINAR() ".concat(e.getMessage()));
                    }
                }
                calculaFilaActual();
            }
        }
        if (boo_elimino && dibujo) {
            updateTablas();
        }
        return boo_elimino;
    }

    private void calculaFilaActual() {
        //Cuando se elimina se calcula la nueva fila seleccionada
        int nuevo_acual = 0;
        if (getTotalFilas() > 0) {
            boolean lboo_siguiente_pag = false;
            for (int i = filaActual; i >= 0; i--) {
                try {
                    if (filas.get(i).isVisible() == true) {
                        nuevo_acual = i;
                        lboo_siguiente_pag = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }
            if (lboo_siguiente_pag == false) {
                for (int i = filaActual; i < filas.size(); i++) {
                    if (filas.get(i).isVisible() == true) {
                        nuevo_acual = i;
                        break;
                    }
                }
            }
            setFilaActual(nuevo_acual);
            calcularPaginaActual();
        } else {
            filaSeleccionada = null;
            filaActual = 0;
            setFirst(0);
        }

    }

    /**
     * Guarda archivos e imagenes
     *
     * @param nombre_campo
     * @param valor
     */
    public void setStreamedContent(String nombre_campo, StreamedContent valor) {
        filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = valor;
    }

    public void setValor(String nombre_campo, String valor) {
        //Configura el valor de un campo de la fila actual
        if (valor == null || valor.equalsIgnoreCase("null")) {
            valor = "";
        }
        try {
            int num_coulumna = getNumeroColumna(nombre_campo);
            if (columnas[num_coulumna].getControl().equals("Combo") || columnas[num_coulumna].getControl().equals("Autocompletar")) {
                filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = columnas[num_coulumna].getObjetoAutocompletar(valor);
            } else if (columnas[getNumeroColumna(nombre_campo)].getControl().equals("Calendario")) {
                if (columnas[getNumeroColumna(nombre_campo)].getTipoJava().equals("java.sql.Date")) {
                    filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFecha(valor);
                } else {
                    filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFechaHora(valor);
                }
            } else if (columnas[getNumeroColumna(nombre_campo)].getControl().equals("Hora")) {
                filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getHora(valor);
            } else {
                filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = valor;
            }
        } catch (Exception e) {
            framework.crearError("No se pudo asignar el valor : ".concat(valor).concat(" al campo :").concat(nombre_campo).concat("  <br/>").concat(e.getMessage()), "setValor()", e);
            System.out.println("ERROR setValor() ".concat(nombre_campo).concat(" : ").concat(e.getMessage()));
        }
    }

    public void setValor(int fila, String nombre_campo, String valor) {
        if (valor == null || valor.equalsIgnoreCase("null")) {
            valor = "";
        }
        try {
            int num_coulumna = getNumeroColumna(nombre_campo);
            if (columnas[num_coulumna].getControl().equals("Combo") || columnas[num_coulumna].getControl().equals("Autocompletar")) {
                filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = columnas[num_coulumna].getObjetoAutocompletar(valor);
            } else if (columnas[getNumeroColumna(nombre_campo)].getControl().equals("Calendario")) {
                if (columnas[getNumeroColumna(nombre_campo)].getTipoJava().equals("java.sql.Date")) {
                    filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFecha(valor);
                } else {
                    filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFechaHora(valor);
                }
            } else if (columnas[getNumeroColumna(nombre_campo)].getControl().equals("Hora")) {
                filas.get(fila).getCampos()[getNumeroColumna(nombre_campo)] = framework.getHora(valor);
            } else {
                filas.get(fila).getCampos()[getNumeroColumna(nombre_campo)] = valor;
            }
        } catch (Exception e) {
            framework.crearError("No se pudo asignar el valor : ".concat(valor).concat(" al campo :").concat(nombre_campo).concat("  <br/>").concat(e.getMessage()), "setValor()", e);
            System.out.println("ERROR setValor() " + nombre_campo + " : " + e.getMessage());
        }
    }

    public String getValorArreglo(String nombre_campo, int columna) {
        //Retorna el valor de un campo que es arreglo de objetos, de la fila actual
        Object valor = null;
        if (filas.size() > 0) {
            try {
                int num_coulumna = getNumeroColumna(nombre_campo);
                if (columnas[num_coulumna].getControl().equals("Combo") || columnas[num_coulumna].getControl().equals("Autocompletar")) {
                    valor = filas.get(filaActual).getCampos()[num_coulumna];
                    if (valor != null) {
                        try {
                            valor = ((Object[]) valor)[columna];
                        } catch (Exception e) {
                            valor = filas.get(filaActual).getCampos()[num_coulumna];
                        }
                    }
                }
            } catch (Exception e) {
                framework.crearError("No se pudo obtener el Label del campo :" + nombre_campo + "  <br/>" + e.getMessage(), "getValor()", e);
                System.out.println("ERROR getLabelCombo() " + nombre_campo + " : " + e.getMessage());
            }
        }
        if (valor != null) {
            return valor + "";
        } else {
            return null;
        }
    }

    public String getValor(String nombre_campo) {
        //Retorna el valor de un campo de la fila actual
        Object valor = null;
        if (filas.size() > 0) {
            try {
                int num_coulumna = getNumeroColumna(nombre_campo);
                valor = filas.get(filaActual).getCampos()[num_coulumna];
                if (columnas[num_coulumna].getControl().equals("Combo") || columnas[num_coulumna].getControl().equals("Autocompletar")) {
                    if (valor != null) {
                        try {
                            valor = ((Object[]) valor)[0];
                        } catch (Exception e) {
                            valor = filas.get(filaActual).getCampos()[num_coulumna];
                        }
                    }
                } else if ((columnas[num_coulumna].getControl().equals("Calendario"))) {
                    if (valor != null) {
                        if ((columnas[num_coulumna].getTipoJava().equals("java.sql.Date"))) {
                            valor = framework.getFormatoFecha(valor);
                        } else {
                            valor = framework.getFormatoFechaHora(valor);
                        }
                    }
                } else if ((columnas[num_coulumna].getControl().equals("Hora"))) {
                    if (valor != null) {
                        valor = framework.getFormatoHora(valor);
                    }
                } else if (columnas[num_coulumna].getControl().equals("Mascara") && columnas[num_coulumna].isQuitarCaracteresEspeciales()) {
                    valor = quitarCaracteres(valor + "");
                }
            } catch (Exception e) {
                framework.crearError("No se pudo obtener el valor del campo :" + nombre_campo + "  <br/>" + e.getMessage(), "getValor()", e);
                System.out.println("ERROR getValor() " + nombre_campo + " : " + e.getMessage());
            }
        }
        if (valor != null) {
            return valor + "";
        } else {
            return null;
        }
    }

    public Object getValorObjeto(String nombre_campo) {
        //Retorna el valor de un campo de la fila actual
        Object valor = null;
        if (filas.size() > 0) {
            try {
                int num_coulumna = getNumeroColumna(nombre_campo);
                valor = filas.get(filaActual).getCampos()[num_coulumna];
            } catch (Exception e) {
                framework.crearError("No se pudo obtener el valor del campo :" + nombre_campo + "  <br/>" + e.getMessage(), "getValor()", e);
                System.out.println("ERROR getValorObjeto() " + nombre_campo + " : " + e.getMessage());
            }
        }
        if (valor != null) {
            return valor;
        } else {
            return null;
        }
    }

    public Object getValorObjeto(int fila, String nombre_campo) {
        //Retorna el valor de un campo de la fila actual
        Object valor = null;
        if (filas.size() > 0) {
            try {
                int num_coulumna = getNumeroColumna(nombre_campo);
                valor = filas.get(fila).getCampos()[num_coulumna];
            } catch (Exception e) {
                framework.crearError("No se pudo obtener el valor del campo :" + nombre_campo + "  <br/>" + e.getMessage(), "getValor()", e);
                System.out.println("ERROR getValorObjeto() " + nombre_campo + " : " + e.getMessage());
            }
        }
        if (valor != null) {
            return valor;
        } else {
            return null;
        }
    }

    public String getValor(int fila, String nombre_campo) {
        //Retorna el valor de un campo de una fila determinada
        Object valor = null;
        try {
            int num_coulumna = getNumeroColumna(nombre_campo);
            valor = filas.get(fila).getCampos()[num_coulumna];
            if (columnas[num_coulumna].getControl().equals("Combo") || columnas[num_coulumna].getControl().equals("Autocompletar")) {

                try {
                    valor = ((Object[]) valor)[0];
                } catch (Exception e) {
                    valor = filas.get(fila).getCampos()[num_coulumna];
                }
            } else if ((columnas[num_coulumna].getControl().equals("Calendario"))) {

                if (valor != null) {
                    if ((columnas[num_coulumna].getTipoJava().equals("java.sql.Date"))) {
                        valor = framework.getFormatoFecha(valor);
                    } else {
                        valor = framework.getFormatoFechaHora(valor);
                    }
                }
            } else if ((columnas[num_coulumna].getControl().equals("Hora"))) {
                if (valor != null) {
                    valor = framework.getFormatoHora(valor);
                }
            } else if (columnas[num_coulumna].getControl().equals("Mascara") && columnas[num_coulumna].isQuitarCaracteresEspeciales()) {
                valor = quitarCaracteres(valor + "");
            }
        } catch (Exception e) {
            framework.crearError("No se pudo obtener el valor del campo :" + nombre_campo + "  <br/>" + e.getMessage(), "getValor()", e);
            System.out.println("ERROR getValor() " + nombre_campo + " : " + e.getMessage());
        }
        if (valor != null) {
            return valor + "";
        } else {
            return null;
        }
    }

    public String getValorArreglo(int fila, String nombre_campo, int columna) {
        //Retorna el valor de un campo que es arreglo de objetos de una fila determinada
        Object valor = null;
        try {
            int num_coulumna = getNumeroColumna(nombre_campo);
            if (columnas[num_coulumna].getControl().equals("Combo") || columnas[num_coulumna].getControl().equals("Autocompletar")) {
                valor = filas.get(fila).getCampos()[num_coulumna];
                try {
                    valor = ((Object[]) valor)[columna];
                } catch (Exception e) {
                    valor = filas.get(fila).getCampos()[num_coulumna];
                }
            }
        } catch (Exception e) {
            framework.crearError("No se pudo obtener el valor del campo :" + nombre_campo + "  <br/>" + e.getMessage(), "getValor()", e);
            System.out.println("ERROR getValor() " + nombre_campo + " : " + e.getMessage());
        }
        if (valor != null) {
            return valor + "";
        } else {
            return null;
        }
    }

    public int getNumeroTabla() {
        return numeroTabla;
    }

    public void setNumeroTabla(int numeroTabla) {
        this.numeroTabla = numeroTabla;
    }

//    private void cargarConfiguracionTabla() {
//        List lis_busca = framework.getConexion().consultar("SELECT TABLA_TABL,PRIMARIA_TABL,FORANEA_TABL,PADRE_TABL,IDE_TABL,ORDEN_TABL,FILAS_TABL,NOMBRE_TABL,FORMULARIO_TABL,genera_primaria_tabl FROM SIS_TABLA WHERE NUMERO_TABL=" + numeroTabla + " AND id_opcion=" + framework.getVariable("id_opcion"));
//        if (!lis_busca.isEmpty()) {
//            Object[] obj = (Object[]) lis_busca.get(0);
//            if ((obj[0] != null && !obj[0].toString().isEmpty()) && (obj[1] != null && !obj[1].toString().isEmpty())) {
//                setTabla(obj[0].toString(), obj[1].toString(), numeroTabla);
//            }
//            if (obj[2] != null && !obj[2].toString().isEmpty()) {
//                setCampoForanea(obj[2].toString());
//            }
//            if (obj[3] != null && !obj[3].toString().isEmpty()) {
//                setCampoPadre(obj[3].toString());
//            }
//            if (obj[5] != null && !obj[5].toString().isEmpty()) {
//                setCampoOrden(obj[5].toString());
//            }
//            if (obj[6] != null && !obj[6].toString().isEmpty()) {
//                try {
//                    setRows(Integer.parseInt(obj[6].toString()));
//                } catch (Exception e) {
//                }
//            }
//            if (obj[7] != null && !obj[7].toString().isEmpty()) {
//                setCampoNombre(obj[7].toString());
//            }
//            if (obj[8] != null) {
//                setTipoFormulario(framework.toBoolean(obj[8]));
//            }
//
//            if (obj[9] != null) {
//                setGenerarPrimaria(framework.toBoolean(obj[9]));
//            }
//            ide_tabl = obj[4].toString();
//            formarCampos();
//            formarCombos();
//
//        }
//        if (numeroTabla == 1) {
//            setFocus();
//        }
//    }

//    private void formarCampos() {
//        List lis_busca = framework.getConexion().consultar("SELECT NOM_CAMP,NOM_VISUAL_CAMP,ORDEN_CAMP,VISIBLE_CAMP,LECTURA_CAMP,DEFECTO_CAMP,MASCARA_CAMP,FILTRO_CAMP,MAYUSCULA_CAMP,requerido_camp,UNICO_camp FROM SIS_CAMPO WHERE IDE_TABL=" + ide_tabl);
//        if (!lis_busca.isEmpty()) {
//            for (Iterator it = lis_busca.iterator(); it.hasNext();) {
//                Object[] obj = (Object[]) it.next();
//                if (obj[1] != null && !obj[1].toString().isEmpty()) {
//                    getColumna(obj[0].toString()).setNombreVisual(obj[1].toString());
//                }
//                if (obj[2] != null && !obj[2].toString().isEmpty()) {
//                    try {
//                        getColumna(obj[0].toString()).setOrden(Integer.parseInt(obj[2].toString()));
//                    } catch (Exception e) {
//                    }
//                }
//                if (obj[3] != null && !obj[3].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).isVisible()) {
//                        getColumna(obj[0].toString()).setVisible(framework.toBoolean(obj[3]));
//                    }
//                }
//                if (obj[4] != null && !obj[4].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).isLectura() == false) {
//                        getColumna(obj[0].toString()).setLectura(framework.toBoolean(obj[4]));
//                    }
//                }
//                if (obj[5] != null && !obj[5].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).getValorDefecto() == null) {
//                        getColumna(obj[0].toString()).setValorDefecto(obj[5].toString());
//                    }
//                }
//                if (obj[6] != null && !obj[6].toString().isEmpty()) {
//                    //  if (getColumna(obj[0].toString()).getMascara().isEmpty()) {
//                    if (getColumna(obj[0].toString()).getMascara() == null) {
//                        getColumna(obj[0].toString()).setControl("Mascara");
//                        getColumna(obj[0].toString()).setMascara(obj[6].toString());
//                    }
//                }
//                if (obj[7] != null && !obj[7].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).isFiltro() == false) {
//                        getColumna(obj[0].toString()).setFiltro(framework.toBoolean(obj[7]));
//                    }
//                }
//                if (obj[8] != null && !obj[8].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).isMayusculas() == false) {
//                        getColumna(obj[0].toString()).setMayusculas(framework.toBoolean(obj[8]));
//                    }
//                }
//                if (obj[9] != null && !obj[9].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).isRequerida() == false) {
//                        getColumna(obj[0].toString()).setRequerida(framework.toBoolean(obj[9]));
//                    }
//                }
//                if (obj[10] != null && !obj[10].toString().isEmpty()) {
//                    if (getColumna(obj[0].toString()).isUnico() == false) {
//                        getColumna(obj[0].toString()).setUnico(framework.toBoolean(obj[10]));
//                    }
//                }
//            }
//        }
//    }

//    private void formarCombos() {
//        List lis_busca = framework.getConexion().consultar("SELECT CAMPO_COMB,TABLA_COMB,PRIMARIA_COMB,NOMBRE_COMB,CONDICION_COMB FROM SIS_COMBO WHERE IDE_TABL=" + ide_tabl);
//        if (!lis_busca.isEmpty()) {
//            for (Iterator it = lis_busca.iterator(); it.hasNext();) {
//                Object[] obj = (Object[]) it.next();
//                if (obj[4] == null) {
//                    obj[4] = "";
//                }
//                if (framework.tieneCampoEmpresa(obj[1].toString(), obj[2].toString())) {
//                    if (!obj[4].toString().isEmpty()) {
//                        obj[4] = obj[4] + " AND id_empresa=" + id_empresa + " ";
//                    } else {
//                        obj[4] = " id_empresa=" + id_empresa + " ";
//                    }
//                }
//                //AUMENTAR LA CONDICION DE LA SUCURSAL A LOS COMBOS QUE TIENE id_empresa
//                getColumna(obj[0].toString()).setCombo(obj[1].toString(), obj[2].toString(), obj[3].toString(), obj[4].toString());
//            }
//
//        }
//    }

    public int getNumeroColumna(String str_nombre_campo) {
        int int_numero_campo = -1;
        for (int i = 0; i < columnas.length; i++) {
            if (columnas[i].getNombre().equalsIgnoreCase(str_nombre_campo.trim())) {
                int_numero_campo = i;
                break;
            }
        }
        return int_numero_campo;
    }

    public Columna getColumna(String nombre_campo) {
        try {
            for (Columna columnaActual : columnas) {
                if (columnaActual.getNombre().equalsIgnoreCase(nombre_campo.trim())) {
                    return columnaActual;
                }
            }
        } catch (Exception e) {
            framework.crearError("El campo " + nombre_campo + " no existe en la tabla " + tabla, "en el método getColumna()", e);
        }
        System.out.println("ERROR : El campo " + nombre_campo + " no existe en la tabla " + tabla);

        return null;

    }

    public String getValorSeleccionado() {
        //Retorna el valor de la clave primaria
        String valor = null;
        try {
            valor = filas.get(filaActual).getRowKey();
        } catch (Exception e) {
            valor = null;
        }
        if (valor != null) {
            try {
                Integer.parseInt(valor);
            } catch (Exception e) {
                valor = "-1";
            }
        }
        return valor;
    }

    public Columna[] getColumnas() {
        return columnas;
    }

    public void setColumnas(Columna[] columnas) {
        this.columnas = columnas;
    }

    public List<Fila> getFilas() {
        return filas;
    }

    public void setFilas(LinkedList<Fila> filas) {
        this.filas = filas;
    }

    public int getFilaActual() {
        return filaActual;
    }

    public void setFilaActual(int filaActual) {
        this.filaActual = filaActual;
        try {
            filaSeleccionada = filas.get(filaActual);
            for (Tabla relacion1 : relacion) {
                relacion1.ejecutarValorForanea(getValorSeleccionado());
            }
        } catch (Exception e) {
            this.filaActual = 0;
            filaSeleccionada = null;
            limpiar();
        }
    }

    public void setFilaActual(String valorPrimaria) {
        try {
            filaSeleccionada = buscaFila(valorPrimaria);
            this.filaActual = getNumeroFila(valorPrimaria);
            for (Tabla relacion1 : relacion) {
                relacion1.ejecutarValorForanea(valorPrimaria);
            }
        } catch (Exception e) {
            this.filaActual = 0;
            filaSeleccionada = null;
            limpiar();
        }
    }

    public String getCampoPrimaria() {
        return campoPrimaria;
    }

    public String getTabla() {
        return tabla;
    }

    public String getCampoOrden() {
        return campoOrden;
    }

    public void setCampoOrden(String campoOrden) {
        this.campoOrden = campoOrden;
    }

    public String getSql() {
        sql = conexion.validarSentecia(sql);
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
        if (columnas == null) {
            formarColumnas();
        }
    }

    public String getCampoForanea() {
        return campoForanea;
    }

    public void setCampoForanea(String campoForanea) {
        if (campoForanea == null) {
            campoForanea = "";
        }
        this.campoForanea = campoForanea;
        if (this.valorforanea.isEmpty()) {
            // this.condicionForanea.delete(0, condicionForanea.length()).append(" ").append(this.campoForanea).append(" IS NULL");
            this.condicionForanea = " ".concat(this.campoForanea).concat(" IS NULL");
        } else {
            try {
                //this.condicionForanea.delete(0, condicionForanea.length()).append(" ").append(this.campoForanea).append(" =").append(Integer.parseInt(valorforanea));
                this.condicionForanea = " ".concat(this.campoForanea).concat(" =").concat(String.valueOf(Integer.parseInt(valorforanea)));
            } catch (Exception e) {
                //   this.condicionForanea.delete(0, condicionForanea.length()).append(" ").append(this.campoForanea).append(" =-1");
                this.condicionForanea = " ".concat(this.campoForanea).concat(" =").concat("=-1");
            }
        }
    }

    public String getCampoPadre() {
        return campoPadre;
    }

    public void setCampoPadre(String campoPadre) {
        if (campoPadre == null) {
            campoPadre = "";
        }
        this.campoPadre = campoPadre;
        if (this.valorPadre.isEmpty()) {
            this.condicionPadre = " " + this.campoPadre + " IS NULL";
        } else {
            this.condicionPadre = " " + this.campoPadre + " =" + valorPadre + "";
        }
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        if (condicion == null) {
            condicion = "";
        }
        this.condicion = condicion;
    }

    public String getCondicionForanea() {
        return condicionForanea;
    }

    public void setCondicionForanea(String condicionForanea) {
        this.condicionForanea = condicionForanea;
    }

    public String getCondicionPadre() {
        return condicionPadre;
    }

    public void setCondicionPadre(String condicionPadre) {
        this.condicionPadre = condicionPadre;
    }

    public String getValorForanea() {
        return valorforanea;
    }

    public String getCondicionBuscar() {
        return condicionBuscar;
    }

    public void setCondicionBuscar(String condicionBuscar) {
        if (condicionBuscar == null) {
            condicionBuscar = "";
        }
        this.condicionBuscar = condicionBuscar;
    }

    public String getCampoNombre() {
        return campoNombre;
    }

    public void setCampoNombre(String campoNombre) {
        if (campoNombre == null) {
            campoNombre = "";
        }
        this.campoNombre = campoNombre;
    }

    public Fila getFila(int numero_fila) {
        return filas.get(numero_fila);
    }

    public boolean isFilaInsertada(int numero_fila) {
        try {
            if (insertadas.indexOf(getFila(numero_fila).getRowKey()) >= 0) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean isFilaInsertada() {
        try {
            if (insertadas.indexOf(getFila(filaActual).getRowKey()) >= 0) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean isFilaModificada(int numero_fila) {
        try {
            if (modificadas.indexOf(getFila(numero_fila).getRowKey()) >= 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isFilaModificada() {
        try {
            if (modificadas.indexOf(getFila(filaActual).getRowKey()) >= 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void ejecutarValorForanea(String valorforanea) {
        setValorForanea(valorforanea);
        ejecutarSql();
    }

    public void setValorForanea(String valorforanea) {
        if (valorforanea == null) {
            valorforanea = "";
        }
        this.valorforanea = valorforanea;
        if (this.valorforanea.isEmpty()) {
            // this.condicionForanea.delete(0, condicionForanea.length()).append(" ").append(this.campoForanea).append(" IS NULL");
            this.condicionForanea = " ".concat(this.campoForanea).concat(" IS NULL");

        } else {
            try {
                //this.condicionForanea.delete(0, condicionForanea.length()).append(" ").append(this.campoForanea).append(" =").append(Integer.parseInt(valorforanea));
                this.condicionForanea = " ".concat(this.campoForanea).concat(" =").concat(String.valueOf(Integer.parseInt(valorforanea)));
            } catch (Exception e) {
                //   this.condicionForanea.delete(0, condicionForanea.length()).append(" ").append(this.campoForanea).append(" =-1");
                this.condicionForanea = " ".concat(this.campoForanea).concat(" =").concat("=-1");
            }
        }
    }

    public String getValorPadre() {
        return valorPadre;
    }

    public void setValorPadre(String valorPadre) {
        if (valorPadre == null) {
            valorPadre = "";
        }
        this.valorPadre = valorPadre;
        if (this.valorPadre.isEmpty()) {
            this.condicionPadre = " " + this.campoPadre + " IS NULL";
        } else {
            this.condicionPadre = " " + this.campoPadre + " =" + valorPadre + "";
        }
    }

    public void ejecutarValorPadre(String valorPadre) {
        setValorPadre(valorPadre);
        ejecutarSql();
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<String> getEliminadas() {
        return eliminadas;
    }

    public void setEliminadas(List<String> eliminadas) {
        this.eliminadas = eliminadas;
    }

    public List<String> getInsertadas() {
        return insertadas;
    }

    public void setInsertadas(List<String> insertadas) {
        this.insertadas = insertadas;
    }

    public List<String> getModificadas() {
        return modificadas;
    }

    public void setModificadas(List<String> modificadas) {
        this.modificadas = modificadas;
    }

    private ValueExpression crearValueExpression(String valueExpression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + valueExpression + "}", Object.class
        );
    }

    private ValueExpression crearValueExpression2(String valueExpression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), valueExpression, Object.class
        );
    }

    public boolean isLectura() {
        return lectura;
    }

    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }

    public void focus(AjaxBehaviorEvent evt) {
        setFocus();
    }

    public void setFocus() {
        framework.crearVariable("TABLA_FOCO", idCompleto);
    }

    public boolean isFocus() {
        return framework.getVariable("TABLA_FOCO").equals(idCompleto);
    }

    public void limpiar() {
        filas.clear();
        filaSeleccionada = null;
        restablecer();
        for (Tabla relacion1 : relacion) {
            relacion1.limpiar();
            relacion1.restablecer();
        }

        if (dibujo) {
            updateTablas();
            setFirst(0);
        }

    }

    public void agregarRelacion(Tabla tabla) {
        tabla.setCampoForanea(campoPrimaria);
        relacion.add(tabla);
    }

    private void actualizarForaneaRelaciones(String valor) {
//Actualiza la clave foranea de las tablas relacionadas
        for (Tabla relacion1 : relacion) {
            for (int i = relacion1.getInsertadas().size() - 1; i >= 0; i--) {
                relacion1.buscaFila(relacion1.getInsertadas().get(i)).getCampos()[relacion1.getNumeroColumna(relacion1.getCampoForanea())] = valor;
            }
        }
    }

    public void buscarComponentePadre(UIComponent componente) {
        for (UIComponent kid : componente.getChildren()) {
            if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.DataTableRenderer")) {
                if (((Tabla) kid).getCampoPrimaria().equalsIgnoreCase(campoForanea)) {
                    TABLA_PADRE = ((Tabla) kid);
                    break;
                }
            }
            //Esto && deja de buscar en los dialogos
            //        if (kid.getChildren().size() > 0 && (kid.getParent().getRendererType() != null && !kid.getParent().getRendererType().equals("org.primefaces.component.DialogRenderer"))) {
            buscarComponentePadre(kid);
            //  }
        }
    }

    public void actualizar() {
        filtro = null;
        seleccionados = null;
        filaSeleccionada = null;
        limpiar();
        setCondicionBuscar("");
        insertadas.clear();
        modificadas.clear();
        insertadasCodigos.clear();
        eliminadas.clear();
        eliminadasCondicion.clear();
        foco_valida_insertar = null;
        key = 'A';
        //actualiza los combos
        for (Columna columna : columnas) {
            if (columna.getSqlCombo() != null) {
                columna.actualizarCombo();
            }
            columna.setSqlFiltro("");
        }
        ejecutarSql();
        for (Tabla relacion1 : relacion) {
            relacion1.ejecutarValorForanea(getValorSeleccionado());
        }
    }

    public void establecerConfiguracion() {
        //Guarda en sis_tabla la configuracion 
        if (numeroTabla != -1) {

            if (ide_tabl.equals("-1")) {
                Tabla tab_busca_campos = new Tabla();
                tab_busca_campos.setSql("SELECT * FROM SIS_CAMPO WHERE IDE_TABL=".concat(ide_tabl));
                if (tab_busca_campos.getFilas().isEmpty()) {
                    ide_tabl = framework.getConexion().getMaximo("SIS_TABLA", "IDE_TABL", 1) + "";
                    framework.getConexion().ejecutarSql("INSERT INTO SIS_TABLA (IDE_TABL,id_opcion,NUMERO_TABL,TABLA_TABL,PRIMARIA_TABL,NOMBRE_TABL,FORANEA_TABL,PADRE_TABL,FILAS_TABL,FORMULARIO_TABL,genera_primaria_tabl) VALUES(".concat(ide_tabl).concat(",").concat(framework.getVariable("id_opcion")).concat(",").concat(String.valueOf(numeroTabla)).concat(",'").concat(tabla).concat("','").concat(claveCompuesta).concat("','").concat(campoNombre).concat("','").concat(campoForanea).concat("','" + campoPadre).concat("'," + this.getRows()).concat(",").concat(String.valueOf(tipoFormulario)).concat(",").concat(String.valueOf(generarPrimaria)).concat(")"));
                }
            } else {
                framework.getConexion().ejecutarSql("UPDATE SIS_TABLA SET  id_opcion=".concat(framework.getVariable("id_opcion")).concat(", NUMERO_TABL=").concat(String.valueOf(numeroTabla)).concat(", TABLA_TABL='").concat(tabla).concat("', PRIMARIA_TABL='").concat(claveCompuesta).concat("', NOMBRE_TABL='").concat(campoNombre).concat("' ,FORANEA_TABL='").concat(campoForanea).concat("',PADRE_TABL='").concat(campoPadre).concat("',FILAS_TABL=").concat(String.valueOf(this.getRows())).concat(",FORMULARIO_TABL=").concat(String.valueOf(tipoFormulario)).concat(", genera_primaria_tabl=").concat(String.valueOf(generarPrimaria)).concat(" WHERE IDE_TABL=").concat(ide_tabl));
            }
        }
    }

    public void actualizarCombos() {
        //actualiza los combos
        for (Columna columna : columnas) {
            if (columna.getSqlCombo() != null) {
                columna.actualizarCombo();
            }
            columna.setSqlFiltro("");
        }
    }

    public void actualizarCombosFormulario() {
        //actualiza los combos  
        if (isEmpty() == false) {
            String campos = "";
            for (int i = 0; i < columnas.length; i++) {
                if (i > 0) {
                    campos += ", ";
                }
                campos += columnas[i].getNombre();
            }
            Tabla tab_aux = new Tabla();
            tab_aux.setSql("SELECT " + campos + " FROM " + tabla + " where " + getCondicionPrimaria());
            tab_aux.setColumnas(columnas);
            tab_aux.setConvertidor(true);
            tab_aux.ejecutarSql();
            filas.set(filaActual, tab_aux.getFilaSeleccionada());
            filaSeleccionada = tab_aux.getFilaSeleccionada();
        }
    }

    public void actualizarRelaciones() {
        for (Tabla relacion1 : relacion) {
            relacion1.ejecutarValorForanea(getValorSeleccionado());
        }
    }

    public int getTotalFilas() {
        return filas.size();
    }

    public int getTotalFilasVisibles() {
        int aux = 0;
        for (Fila fila : filas) {
            if (fila.isVisible()) {
                aux++;
            }
        }
        return aux;
    }

    public boolean getTipoSeleccion() {
        return tipoSeleccion;
    }

    public void setTipoSeleccion(boolean tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
        this.tipoSeleccionMultiple = !tipoSeleccion;
    }

    public String getFilasSeleccionadas() {
        String str_seleccionadas = "";
        if (seleccionados != null) {
            for (Fila seleccionado : seleccionados) {
                if (!str_seleccionadas.isEmpty()) {
                    str_seleccionadas += ",";
                }
                if (generarPrimaria) {
                    str_seleccionadas += seleccionado.getRowKey();
                } else {
                    str_seleccionadas += "'" + seleccionado.getRowKey() + "'";
                }
            }
        }
        return str_seleccionadas;
    }

    public List<Fila> getListaFilasSeleccionadas() {
        List<Fila> lis_filas = new ArrayList();
        for (Fila seleccionado : seleccionados) {
            lis_filas.add(seleccionado);
        }
        return lis_filas;
    }

    ////Formulario
    public boolean isTipoFormulario() {
        return tipoFormulario;
    }

    public void setTipoFormulario(boolean tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
        if (tipoFormulario) {
            if (grid == null) {
                grid = new Grid();
                grid.setColumns(2);
            }
            this.setValidarInsertar(true);
        }
    }

    public Grid getGrid() {
        if (grid == null) {
            grid = new Grid();
            grid.setColumns(2);
        }
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Fila getFilaSeleccionada() {
        return filaSeleccionada;
    }

    public void setFilaSeleccionada(Fila filaSeleccionada) {
        this.filaSeleccionada = filaSeleccionada;
    }

    private void construirFormulario() {
        Ajax aja_modifico_fila = new Ajax();
        grid.getChildren().clear();
        aja_modifico_fila.setMetodoRuta(ruta + "." + this.getId() + ".modificar");
        //  aja_modifico_fila.setProcess("@this");
        aja_modifico_fila.setGlobal(false);
        for (int i = 0; i < columnas.length; i++) {
            columnas[i].setId("" + idCompleto + ":" + columnas[i].getNombre() + "_" + i);
            if (columnas[i].isVisible()) {
                switch (columnas[i].getControl()) {
                    case "Etiqueta": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " : ");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Etiqueta eti_dato = new Etiqueta();
                        eti_dato.setId(columnas[i].getNombre() + "_" + i);
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle("width:" + columnas[i].getAncho() + "em;" + columnas[i].getEstilo());
                        } else {
                            eti_dato.setStyle("width:" + columnas[i].getAncho() + "em;");
                        }
                        if (columnas[i].isFormatoNumero()) {
                            eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        eti_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        eti_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        grid.getChildren().add(eti_dato);
                        break;
                    }
                    case "Texto": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " : ");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Texto tex_dato = new Texto();
                        tex_dato.setId(columnas[i].getNombre() + "_" + i);
                        tex_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        tex_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        tex_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        tex_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getEstilo() != null) {
                            tex_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getOnKeyup() != null) {
                            tex_dato.setOnkeyup(columnas[i].getOnKeyup());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            tex_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            tex_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            tex_dato.setOnclick(columnas[i].getOnClick());
                        }       //Validar numeros
                        switch (columnas[i].getTipoJava()) {
                            case "java.lang.Long":
                            case "java.lang.Integer":
                                tex_dato.setSoloEnteros();
                                break;
                            case "java.lang.Number":
                            case "java.math.BigDecimal":
                            case "java.lang.Float":
                            case "java.lang.Double":
                                tex_dato.setSoloNumeros();
                                break;
                            default:
                                tex_dato.setMaxlength(columnas[i].getAncho());
                                break;
                        }
                        if (columnas[i].isFormatoNumero()) {
                            tex_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        grid.getChildren().add(tex_dato);
                        break;
                    }
                    case "Clave": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Clave cla_dato = new Clave();
                        cla_dato.setId(columnas[i].getNombre() + "_" + i);
                        cla_dato.setMaxlength(columnas[i].getAncho());
                        cla_dato.setTitle(columnas[i].getNombreVisual());
                        if (columnas[i].getEstilo() != null) {
                            cla_dato.setStyle(columnas[i].getEstilo());
                        }
                        cla_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        cla_dato.setValueExpression("value", crearValueExpression(ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]"));
                        cla_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        cla_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getMetodoChange() != null) {
                            cla_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            cla_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            cla_dato.setOnclick(columnas[i].getOnClick());
                        }
                        grid.getChildren().add(cla_dato);
                        break;
                    }
                    case "Mascara": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Mascara mas_dato = new Mascara();
                        mas_dato.setId(columnas[i].getNombre() + "_" + i);
                        mas_dato.setMaxlength(columnas[i].getAncho());
                        mas_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        if (columnas[i].getEstilo() != null) {
                            mas_dato.setStyle(columnas[i].getEstilo());
                        }
                        mas_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        mas_dato.setValueExpression("mask", ruta + "." + this.getId() + ".columnas[" + i + "].mascara");
                        mas_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        mas_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getMetodoChange() != null) {
                            mas_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            mas_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            mas_dato.setOnclick(columnas[i].getOnClick());
                        }
                        grid.getChildren().add(mas_dato);
                        break;
                    }
                    case "Calendario": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Calendario cal_dato = new Calendario();
                        cal_dato.setId(columnas[i].getNombre() + "_" + i);
                        if (columnas[i].getEstilo() != null) {
                            cal_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            cal_dato.setTipoCalendarioHora();
                        }
                        cal_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        cal_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        cal_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getMetodoChange() != null) {
                            cal_dato.addClientBehavior("dateSelect", columnas[i].getMetodoChange());
                        }
                        if (columnas[i].getOnClick() != null) {
                            cal_dato.setOnclick(columnas[i].getOnClick());
                        }
                        cal_dato.addValueChangeListener(crearValueChange(ruta + "." + this.getId() + ".modificar"));
                        grid.getChildren().add(cal_dato);
                        break;
                    }
                    case "Hora": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " : ");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Hora hor_dato = new Hora();
                        hor_dato.setId(columnas[i].getNombre() + "_" + i);
                        hor_dato.setSize(8);
                        hor_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        hor_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        hor_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getMetodoChange() != null) {
                            hor_dato.addClientBehavior("dateSelect", columnas[i].getMetodoChange());
                        }
                        if (columnas[i].getOnClick() != null) {
                            hor_dato.setOnclick(columnas[i].getOnClick());
                        }
                        hor_dato.addValueChangeListener(crearValueChange(ruta + "." + this.getId() + ".modificar"));
                        grid.getChildren().add(hor_dato);
                        break;
                    }
                    case "Check": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Check che_dato = new Check();
                        che_dato.setId(columnas[i].getNombre() + "_" + i);
                        che_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        che_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        che_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getEstilo() != null) {
                            che_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            che_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            che_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            che_dato.setOnclick(columnas[i].getOnClick());
                        }
                        grid.getChildren().add(che_dato);
                        break;
                    }
                    case "Combo": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Combo com_dato = new Combo();
                        com_dato.setId(columnas[i].getNombre() + "_" + i);
                        com_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        com_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        com_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getEstilo() != null) {
                            com_dato.setStyle(columnas[i].getEstilo());
                        }
                        com_dato.getChildren().clear();
                        if (columnas[i].isPermitirNullCombo()) {
                            // ito_vacio.setValueExpression("rendered", crearValueExpression(ruta + "." + this.getId() + ".columnas[" + i + "].numeroColumnasCombo > 0"));
                            ItemOpcion ito_vacio = new ItemOpcion();
                            Object[] obj_vacio = new Object[2];
                            obj_vacio[0] = null;
                            obj_vacio[1] = "";
                            ito_vacio.setItemValue(obj_vacio);
                            ito_vacio.setItemLabel("");
                            com_dato.getChildren().add(ito_vacio);
                        }
                        UISelectItems usi_opciones = new UISelectItems();
                        usi_opciones.setValueExpression("value", crearValueExpression(ruta + "." + this.getId() + ".columnas[" + i + "].listaCombo"));
                        usi_opciones.setValueExpression("var", crearValueExpression(ruta + "." + this.getId() + ".columnas[" + i + "].var"));
                        com_dato.setVar("combo");
                        com_dato.setConverter(new ConvertidorAutoCompletar());
                        usi_opciones.setValueExpression("itemValue", crearValueExpression("opcion"));
                        String str = "";
                        //Si en el select del combo hay mas de dos campos
                        for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {
                            if (j > 1) {
                                str += "  -  ";
                            }
                            str += "#{" + "opcion[" + j + "]} ";
                            Column col = new Column();
                            Etiqueta eti = new Etiqueta();
                            eti.setValueExpression("value", "combo[" + j + "]");
                            col.getChildren().add(eti);
                            com_dato.getChildren().add(col);
                        }
                        usi_opciones.setValueExpression("itemLabel", crearValueExpression2(str));
                        com_dato.getChildren().add(usi_opciones);
                        if (columnas[i].getMetodoChange() != null) {
                            com_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            com_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            com_dato.setOnclick(columnas[i].getOnClick());
                        }
                        if (columnas[i].isBuscarenCombo() && columnas[i].getListaCombo().size() > 10) {
                            com_dato.setFilter(true);
                            com_dato.setFilterMatchMode(columnas[i].getFiltroModo());
                        }
                        grid.getChildren().add(com_dato);
                        break;
                    }
                    case "Autocompletar": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        AutoCompletar aut_dato = new AutoCompletar();
                        aut_dato.setId(columnas[i].getNombre() + "_" + i);
                        aut_dato.setTitle(columnas[i].getNombreVisual());
                        aut_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        aut_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        aut_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        aut_dato.setMetodoCompletarRuta(ruta + "." + this.getId() + ".columnas[" + i + "].autocompletar");
                        aut_dato.setVar("auto");
                        aut_dato.setConverter(new ConvertidorAutoCompletar());
                        if (columnas[i].getEstilo() != null) {
                            aut_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getNumeroColumnasCombo() > 2) {
                            aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                            String str = "";
                            int size = 27;
                            for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {

                                Column col = new Column();
                                if (columnas[i].getImagenAutocompletar() != j) {
                                    if (j > 1) {
                                        str += "  ";
                                    }
                                    str += "#{" + "auto[" + j + "]}";
                                    Etiqueta eti = new Etiqueta();
                                    eti.setValueExpression("value", "auto[" + j + "]");
                                    col.getChildren().add(eti);
                                    aut_dato.getChildren().add(col);
                                } else {
                                    Imagen ima = new Imagen();
                                    ima.setValueExpression("value", "auto[" + j + "]");
                                    ima.setWidth("64");
                                    ima.setHeight("64");
                                    aut_dato.setMaxResults(5);
                                    col.getChildren().add(ima);
                                    aut_dato.getChildren().add(col);
                                }
                                size += 15;
                            }
                            aut_dato.setSize(size);
                            aut_dato.setValueExpression("itemLabel", crearValueExpression2(str));
                        } else {
                            aut_dato.setSize(45);
                            aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                            aut_dato.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            aut_dato.addClientBehavior("itemSelect", columnas[i].getMetodoChange());
                        } else {
                            aut_dato.addClientBehavior("itemSelect", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            aut_dato.setOnclick(columnas[i].getOnClick());
                        }
                        aut_dato.addClientBehavior("change", aja_modifico_fila);
                        grid.getChildren().add(aut_dato);
                        break;
                    }
                    case "Radio": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Radio rad_dato = new Radio();
                        if (columnas[i].isRadioVertical()) {
                            rad_dato.setVertical();
                        }
                        rad_dato.setId(columnas[i].getNombre() + "_" + i);
                        rad_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        rad_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        rad_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        rad_dato.setRadio(columnas[i].getListaRadio());
                        if (columnas[i].getEstilo() != null) {
                            rad_dato.setStyle("width:200%;display:block;border:none;" + columnas[i].getEstilo());
                        } else {
                            rad_dato.setStyle("width:200%;display:block;border:none;");
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            rad_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            rad_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            rad_dato.setOnclick(columnas[i].getOnClick());
                        }
                        grid.getChildren().add(rad_dato);
                        break;
                    }
                    case "AreaTexto": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        AreaTexto ate_dato = new AreaTexto();
                        ate_dato.setId(columnas[i].getNombre() + "_" + i);
                        ate_dato.setMaxlength(columnas[i].getAncho());
                        if (columnas[i].getEstilo() != null) {
                            ate_dato.setStyle("width:320px;height:70px;" + columnas[i].getEstilo());

                        } else {
                            ate_dato.setStyle("width:320px;height:70px");
                        }
                        ate_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        ate_dato.setValueExpression("disabled", ruta + "." + this.getId() + ".filaSeleccionada.lectura or " + ruta + "." + this.getId() + ".columnas[" + i + "].lectura");
                        ate_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getOnKeyup() != null) {
                            ate_dato.setOnclick(columnas[i].getOnKeyup());
                        }
                        if (columnas[i].getMetodoChange() != null) {
                            ate_dato.addClientBehavior("change", columnas[i].getMetodoChange());
                        } else {
                            ate_dato.addClientBehavior("change", aja_modifico_fila);
                        }
                        if (columnas[i].getOnClick() != null) {
                            ate_dato.setOnclick(columnas[i].getOnClick());
                        }
                        grid.getChildren().add(ate_dato);
                        break;
                    }
                    case "Upload": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Grid matriz = new Grid();
                        matriz.setColumns(3);
                        matriz.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");

                        Boton bot_subir = new Boton();
                        bot_subir.setIcon("ui-icon-arrowthickstop-1-n");
                        bot_subir.setValue(!columnas[i].isImagen() ? "Cargar Archivo" : "Cargar Imagen");
                        if (columnas[i].isImagen()) {
                            Imagen im = new Imagen();
                            im.setValueExpression("value", ruta.concat(".").concat(this.getId()).concat(".filaSeleccionada.campos[").concat(String.valueOf(i)).concat("]"));
                            im.setWidth(columnas[i].getAnchoImagen().equals("0") ? null : columnas[i].getAnchoImagen());
                            im.setHeight(columnas[i].getAltoImagen().equals("0") ? null : columnas[i].getAltoImagen());
                            matriz.getChildren().add(im);
                            bot_subir.setDir("img");
                        } else {
                            Link lin_download = new Link();
                            lin_download.setAjax(false);
                            lin_download.setValue(" Descargar Archivo");
                            lin_download.setStyleClass("fa fa-download  fa-lg");
                            lin_download.setValueExpression("rendered", crearValueExpression2(new StringBuilder("#{").append(ruta).append(".").append(this.getId()).append(".filaSeleccionada.campos[").append(i).append("] !=null").append(" }").toString()));
                            lin_download.addActionListener(new FileDownloadActionListener(crearValueExpression2("#{" + ruta.concat(".").concat(this.getId()).concat(".filaSeleccionada.campos[").concat(String.valueOf(i)).concat("]}"))));
                            matriz.getChildren().add(lin_download);
                        }
                        bot_subir.setDisabled(columnas[i].isLectura());
                        bot_subir.setActionListener("abrirSeleccionarArchivo");
                        MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + ruta + "." + this.getId() + ".columnas[" + i + "].subirArchivo}", null, new Class[]{
                                    FileUploadEvent.class
                                }
                                );
                        bot_subir.setUploadMetodo(methodExpression);
                        matriz.getChildren().add(bot_subir);
                        grid.getChildren().add(matriz);
                        break;
                    }
                }
            }
        }
    }

    private void construirFormularioLectura() {
        grid.getChildren().clear();
        for (int i = 0; i < columnas.length; i++) {
            columnas[i].setId("" + idCompleto + ":" + columnas[i].getNombre() + "_" + i);
            if (columnas[i].isVisible()) {
                switch (columnas[i].getControl()) {
                    case "Etiqueta": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " : ");
                        grid.getChildren().add(eti_nombre);
                        Etiqueta eti_dato = new Etiqueta();
                        eti_dato.setId(columnas[i].getNombre() + "_" + i);
                        if (columnas[i].getEstilo() != null) {
                            eti_dato.setStyle("width:" + columnas[i].getAncho() + "em;" + columnas[i].getEstilo());
                        } else {
                            eti_dato.setStyle("width:" + columnas[i].getAncho() + "em;");
                        }
                        eti_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        eti_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].isFormatoNumero()) {
                            eti_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        grid.getChildren().add(eti_dato);
                        break;
                    }
                    case "Texto": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " : ");
                        grid.getChildren().add(eti_nombre);
                        Texto tex_dato = new Texto();
                        tex_dato.setId(columnas[i].getNombre() + "_" + i);
                        tex_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        tex_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        tex_dato.setDisabled(true);
                        tex_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getEstilo() != null) {
                            tex_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].isFormatoNumero()) {
                            tex_dato.setConverter(new ConvertidorNumero(columnas[i].getDecimales()));
                        }
                        grid.getChildren().add(tex_dato);
                        break;
                    }
                    case "Clave": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        Clave cla_dato = new Clave();
                        cla_dato.setId(columnas[i].getNombre() + "_" + i);
                        cla_dato.setMaxlength(columnas[i].getAncho());
                        cla_dato.setTitle(columnas[i].getNombreVisual());
                        if (columnas[i].getEstilo() != null) {
                            cla_dato.setStyle(columnas[i].getEstilo());
                        }
                        cla_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        cla_dato.setValueExpression("value", crearValueExpression(ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]"));
                        cla_dato.setDisabled(true);
                        cla_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        grid.getChildren().add(cla_dato);
                        break;
                    }
                    case "Mascara": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        Mascara mas_dato = new Mascara();
                        mas_dato.setId(columnas[i].getNombre() + "_" + i);
                        mas_dato.setMaxlength(columnas[i].getAncho());
                        mas_dato.setSize((int) (columnas[i].getLongitud() + 20) / 2);
                        if (columnas[i].getEstilo() != null) {
                            mas_dato.setStyle(columnas[i].getEstilo());
                        }
                        mas_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        mas_dato.setValueExpression("mask", ruta + "." + this.getId() + ".columnas[" + i + "].mascara");
                        mas_dato.setDisabled(true);
                        mas_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        grid.getChildren().add(mas_dato);
                        break;
                    }
                    case "Calendario": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        Calendario cal_dato = new Calendario();
                        cal_dato.setId(columnas[i].getNombre() + "_" + i);
                        if (columnas[i].getEstilo() != null) {
                            cal_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getTipoJava().equals("java.sql.Timestamp")) {
                            cal_dato.setTipoCalendarioHora();
                        }
                        cal_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        cal_dato.setDisabled(true);
                        cal_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        grid.getChildren().add(cal_dato);
                        break;
                    }
                    case "Hora": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " : ");
                        grid.getChildren().add(eti_nombre);
                        Hora hor_dato = new Hora();
                        hor_dato.setId(columnas[i].getNombre() + "_" + i);
                        hor_dato.setSize(8);
                        hor_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        hor_dato.setDisabled(true);
                        hor_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        grid.getChildren().add(hor_dato);
                        break;
                    }
                    case "Check": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        Check che_dato = new Check();
                        che_dato.setId(columnas[i].getNombre() + "_" + i);
                        che_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        che_dato.setDisabled(true);
                        che_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].getEstilo() != null) {
                            che_dato.setStyle(columnas[i].getEstilo());
                        }
                        grid.getChildren().add(che_dato);
                        break;
                    }
                    case "Autocompletar":
                    case "Combo": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        AutoCompletar aut_dato = new AutoCompletar();
                        aut_dato.setId(columnas[i].getNombre() + "_" + i);
                        aut_dato.setTitle(columnas[i].getNombreVisual());
                        aut_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        aut_dato.setDisabled(true);
                        aut_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        aut_dato.setMetodoCompletarRuta(ruta + "." + this.getId() + ".columnas[" + i + "].autocompletar");
                        aut_dato.setVar("auto");
                        aut_dato.setConverter(new ConvertidorAutoCompletar());
                        if (columnas[i].getEstilo() != null) {
                            aut_dato.setStyle(columnas[i].getEstilo());
                        }
                        if (columnas[i].getNumeroColumnasCombo() > 2) {
                            aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                            String str = "";
                            int size = 27;
                            for (int j = 1; j < columnas[i].getNumeroColumnasCombo(); j++) {

                                Column col = new Column();
                                if (columnas[i].getImagenAutocompletar() != j) {
                                    if (j > 1) {
                                        str += "  ";
                                    }
                                    str += "#{" + "auto[" + j + "]}";
                                    Etiqueta eti = new Etiqueta();
                                    eti.setValueExpression("value", "auto[" + j + "]");
                                    col.getChildren().add(eti);
                                    aut_dato.getChildren().add(col);
                                } else {
                                    Imagen ima = new Imagen();
                                    ima.setValueExpression("value", "auto[" + j + "]");
                                    ima.setWidth("64");
                                    ima.setHeight("64");
                                    aut_dato.setMaxResults(5);
                                    col.getChildren().add(ima);
                                    aut_dato.getChildren().add(col);
                                }
                                size += 15;
                            }
                            aut_dato.setSize(size);
                            aut_dato.setValueExpression("itemLabel", crearValueExpression2(str));
                        } else {
                            aut_dato.setSize(45);
                            aut_dato.setValueExpression("itemValue", crearValueExpression("auto"));
                            aut_dato.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
                        }
                        grid.getChildren().add(aut_dato);
                        break;
                    }
                    case "Radio": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        Radio rad_dato = new Radio();
                        if (columnas[i].isRadioVertical()) {
                            rad_dato.setVertical();
                        }
                        rad_dato.setId(columnas[i].getNombre() + "_" + i);
                        rad_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        rad_dato.setDisabled(true);
                        rad_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        rad_dato.setRadio(columnas[i].getListaRadio());
                        if (columnas[i].getEstilo() != null) {
                            rad_dato.setStyle("width:200%;display:block;border:none;" + columnas[i].getEstilo());
                        } else {
                            rad_dato.setStyle("width:200%;display:block;border:none;");
                        }
                        grid.getChildren().add(rad_dato);
                        break;
                    }
                    case "AreaTexto": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        grid.getChildren().add(eti_nombre);
                        AreaTexto ate_dato = new AreaTexto();
                        ate_dato.setId(columnas[i].getNombre() + "_" + i);
                        ate_dato.setMaxlength(columnas[i].getAncho());
                        if (columnas[i].getEstilo() != null) {
                            ate_dato.setStyle("width:320px;height:70px;" + columnas[i].getEstilo());

                        } else {
                            ate_dato.setStyle("width:320px;height:70px");
                        }
                        ate_dato.setValueExpression("value", ruta + "." + this.getId() + ".filaSeleccionada.campos[" + i + "]");
                        ate_dato.setDisabled(true);
                        ate_dato.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        grid.getChildren().add(ate_dato);
                        break;
                    }
                    case "Upload": {
                        Etiqueta eti_nombre = new Etiqueta();
                        eti_nombre.setValue(columnas[i].getNombreVisual() + " :");
                        if (columnas[i].isRequerida()) {
                            eti_nombre.setValue(columnas[i].getNombreVisual() + " : " + "<span style=\"color:red;font-weight: bold;\">*</span>");
                        }
                        grid.getChildren().add(eti_nombre);
                        Grid matriz = new Grid();
                        matriz.setColumns(3);
                        matriz.setValueExpression("rendered", ruta + "." + this.getId() + ".filaSeleccionada.visible");
                        if (columnas[i].isImagen()) {
                            Imagen im = new Imagen();
                            im.setValueExpression("value", ruta.concat(".").concat(this.getId()).concat(".filaSeleccionada.campos[").concat(String.valueOf(i)).concat("]"));
                            matriz.getChildren().add(im);
                        } else {
                            Link lin_download = new Link();
                            lin_download.setAjax(false);
                            lin_download.setValue(" Descargar Archivo");
                            lin_download.setStyleClass("fa fa-download  fa-lg");
                            lin_download.setValueExpression("rendered", crearValueExpression2(new StringBuilder("#{").append(ruta).append(".").append(this.getId()).append(".filaSeleccionada.campos[").append(i).append("] !=null").append(" }").toString()));
                            lin_download.addActionListener(new FileDownloadActionListener(crearValueExpression2("#{" + ruta.concat(".").concat(this.getId()).concat(".filaSeleccionada.campos[").concat(String.valueOf(i)).concat("]}"))));
                            matriz.getChildren().add(lin_download);
                        }
                        grid.getChildren().add(matriz);
                        break;
                    }
                }
            }
        }
    }

    public void inicio() {
        try {
            this.getFilas().get(0);
            this.setFilaActual(0);
            calcularPaginaActual();
            framework.addUpdate(this.getIdCompleto());
        } catch (Exception e) {
        }
    }

    public void fin() {
        try {
            this.getFilas().get(this.getFilas().size() - 1);
            this.setFilaActual(this.getFilas().size() - 1);
            calcularPaginaActual();
            framework.addUpdate(this.getIdCompleto());
        } catch (Exception e) {
        }
    }

    public void siguiente() {
        try {
            this.getFilas().get(this.getFilaActual() + 1);
            this.setFilaActual(this.getFilaActual() + 1);
            calcularPaginaActual();
            framework.addUpdate(this.getIdCompleto());
        } catch (Exception e) {
        }
    }

    public void atras() {
        try {
            this.getFilas().get(this.getFilaActual() - 1);
            this.setFilaActual(this.getFilaActual() - 1);
            calcularPaginaActual();
            framework.addUpdate(this.getIdCompleto());
        } catch (Exception e) {
        }
    }

    public void calcularPaginaActual() {
        //calcula la pagina que se mostrara
        if (this.getRows() > 0 && dibujo && !tipoFormulario) {
            int paginaActual = (int) filaActual / getRows();
            this.setFirst(paginaActual * getRows());
        }
    }

    public List<Tabla> getRelacion() {
        return relacion;
    }

    public void setRelacion(List<Tabla> relacion) {
        this.relacion = relacion;
    }

    public int getTotalColumnas() {
        return columnas.length;
    }
    //EXPORTAR

    public void exportarXLS() {
        try {
            ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
            if (tabla.isEmpty()) {
                tabla = "Sql";
            }
            File result = File.createTempFile(extContext.getRealPath("/") + tabla, ".xls");
            WritableWorkbook archivo_xls = Workbook.createWorkbook(result);
            WritableSheet hoja_xls = archivo_xls.createSheet("Tabla", 0);
            WritableFont fuente = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat formato_celda = new WritableCellFormat(fuente);
            formato_celda.setAlignment(jxl.format.Alignment.CENTRE);
            formato_celda.setAlignment(jxl.format.Alignment.LEFT);
            formato_celda.setOrientation(Orientation.HORIZONTAL);
            formato_celda.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            int int_columna = 0;
            for (int i = 0; i < getTotalFilas(); i++) {
                int_columna = 0;
                for (int j = 0; j < getTotalColumnas(); j++) {
                    if (columnas[j].isVisible()) {
                        if (i == 0) {
                            //CABECERAS COLUMNAS
                            jxl.write.Label lab = new jxl.write.Label(int_columna, 0, columnas[j].getNombreVisual() + "", formato_celda);
                            hoja_xls.addCell(lab);
                            CellView cv = new CellView();
                            cv.setAutosize(true);
                            hoja_xls.setColumnView(int_columna, cv);
                        }

                        if (i == (getTotalFilas() - 1)) {
                            //SUMATORIAS
                            if (columnaSuma.isEmpty() == false) {
                                if (columnas[j].isSuma()) {
                                    String formatoNumeros = "###,###.##";
                                    if (columnas[j].getTipoJava().equals("java.lang.Integer") || columnas[j].getTipoJava().equals("java.lang.Long")) {
                                        formatoNumeros = "###,####";
                                    }
                                    NumberFormat decimalNo = new NumberFormat(formatoNumeros);
                                    WritableCellFormat formato_celda_suma = new WritableCellFormat(decimalNo);
                                    formato_celda_suma.setFont(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
                                    formato_celda_suma.setAlignment(jxl.format.Alignment.RIGHT);
                                    formato_celda_suma.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                                    formato_celda_suma.setOrientation(Orientation.HORIZONTAL);
                                    formato_celda_suma.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);

                                    jxl.write.Number num = new jxl.write.Number(int_columna, i + 2, columnas[j].getTotal(), formato_celda_suma);
                                    hoja_xls.addCell(num);
                                }
                            }
                        }
                        switch (columnas[j].getTipoJava()) {
                            case "java.lang.Double":
                            case "java.lang.Float":
                            case "java.lang.Number":
                            case "java.math.BigDecimal":
                                try {
                                    NumberFormat decimalNo = new NumberFormat("###,###.##");
                                    WritableCellFormat formato_celda_decimal = new WritableCellFormat(decimalNo);
                                    formato_celda_decimal.setFont(new WritableFont(WritableFont.ARIAL, 10));
                                    formato_celda_decimal.setAlignment(jxl.format.Alignment.RIGHT);
                                    formato_celda_decimal.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                                    formato_celda_decimal.setOrientation(Orientation.HORIZONTAL);
                                    formato_celda_decimal.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
                                    Double douValor = null;
                                    try {
                                        douValor = Double.parseDouble(filas.get(i).getCampos()[j] + "");
                                    } catch (Exception e) {
                                        douValor = null;
                                    }
                                    if (douValor != null) {
                                        jxl.write.Number num = new jxl.write.Number(int_columna, i + 1, douValor, formato_celda_decimal);
                                        hoja_xls.addCell(num);
                                    } else {
                                        jxl.write.Label lab = new jxl.write.Label(int_columna, i + 1, "", formato_celda);
                                        hoja_xls.addCell(lab);
                                    }

                                } catch (Exception e) {
                                }
                                break;
                            case "java.lang.Integer":
                            case "java.lang.Long":
                                try {
                                    NumberFormat decimalNo = new NumberFormat("###,###");
                                    WritableCellFormat formato_celda_entero = new WritableCellFormat(decimalNo);
                                    formato_celda_entero.setFont(new WritableFont(WritableFont.ARIAL, 10));
                                    formato_celda_entero.setAlignment(jxl.format.Alignment.RIGHT);
                                    formato_celda_entero.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                                    formato_celda_entero.setOrientation(Orientation.HORIZONTAL);
                                    formato_celda_entero.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
                                    Long lonValor = null;
                                    try {
                                        lonValor = Long.parseLong(filas.get(i).getCampos()[j] + "");
                                    } catch (Exception e) {
                                        lonValor = null;
                                    }
                                    if (lonValor != null) {
                                        jxl.write.Number num = new jxl.write.Number(int_columna, i + 1, lonValor, formato_celda_entero);
                                        hoja_xls.addCell(num);
                                    } else {
                                        jxl.write.Label lab = new jxl.write.Label(int_columna, i + 1, "", formato_celda);
                                        hoja_xls.addCell(lab);
                                    }
                                } catch (Exception e) {
                                }
                                break;
                            case "java.sql.Date":
                                try {
                                    jxl.write.Label lab = new jxl.write.Label(int_columna, i + 1, framework.getFormatoFecha(filas.get(i).getCampos()[j]) + "", formato_celda);
                                    hoja_xls.addCell(lab);
                                } catch (Exception e) {
                                }
                                break;
                            case "java.sql.Timestamp":
                                try {
                                    jxl.write.Label lab = new jxl.write.Label(int_columna, i + 1, framework.getFormatoFechaHora(filas.get(i).getCampos()[j]) + "", formato_celda);
                                    hoja_xls.addCell(lab);
                                } catch (Exception e) {
                                }
                                break;
                            case "java.sql.Time":
                                try {
                                    jxl.write.Label lab = new jxl.write.Label(int_columna, i + 1, framework.getFormatoHora(filas.get(i).getCampos()[j]) + "", formato_celda);
                                    hoja_xls.addCell(lab);
                                } catch (Exception e) {
                                }
                                break;
                            default:
                                if (columnas[j].getControl().equals("Combo") || columnas[j].getControl().equals("Autocompletar")) {
                                    try {
                                        String str_valor = "";
                                        for (int k = 1; k < columnas[j].getNumeroColumnasCombo(); k++) {
                                            if (str_valor.isEmpty() == false) {
                                                str_valor += " ";
                                            }
                                            str_valor += ((Object[]) filas.get(i).getCampos()[j])[k];
                                        }
                                        jxl.write.Label lab = new jxl.write.Label(int_columna, i + 1, str_valor, formato_celda);
                                        hoja_xls.addCell(lab);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    jxl.write.Label lab = null;
                                    if (filas.get(i).getCampos()[j] != null) {
                                        lab = new jxl.write.Label(int_columna, i + 1, filas.get(i).getCampos()[j] + "", formato_celda);
                                    } else {
                                        lab = new jxl.write.Label(int_columna, i + 1, "", formato_celda);
                                    }

                                    hoja_xls.addCell(lab);
                                }
                                break;
                        }
                        int_columna++;
                    }
                }
            }
            archivo_xls.write();
            archivo_xls.close();
            framework.descargarFile(result);
        } catch (IOException | WriteException e) {
            framework.crearError("No se pudo exportar a XLS la tabla: " + this.getTabla(), "exportarXLS()", e);
            System.out.println("Error no se genero el XLS :" + e.getMessage());
        }
    }

    public boolean isAuditoria() {
        return auditoria;
    }

    public void setAuditoria(boolean auditoria) {
        this.auditoria = auditoria;
    }

    public boolean isRecuperarLectura() {
        return recuperarLectura;
    }

    public void setRecuperarLectura(boolean recuperarLectura) {
        this.recuperarLectura = recuperarLectura;
        for (Tabla relacion1 : relacion) {
            relacion1.setRecuperarLectura(recuperarLectura);
        }
    }

    public boolean isGenerarPrimaria() {
        return generarPrimaria;
    }

    public void setGenerarPrimaria(boolean generarPrimaria) {
        this.generarPrimaria = generarPrimaria;
        if (!campoPrimaria.isEmpty()) {
            this.getColumna(campoPrimaria).setExterna(!generarPrimaria);
        }

    }

    public String getIdCompleto() {
        return idCompleto;
    }

    public void setIdCompleto(String idCompleto) {
        if (idCompleto.contains(":")) {
            //Por el error de rowindex 0
            idCompleto = idCompleto.replace(":", ":0:");
        }
        this.idCompleto = idCompleto;
    }

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    public void restablecer() {
        insertadas.clear();
        modificadas.clear();
        insertadasCodigos.clear();
        eliminadas.clear();
        eliminadasCondicion.clear();
    }

    public void sumarColumnas() {
        if (!columnaSuma.isEmpty() && dibujo) {
            String col[] = columnaSuma.split(",");
            for (String col1 : col) {
                columnas[getNumeroColumna(col1)].setTotal(getSumaColumna(col1));
            }
        }
    }

    public double getSumaColumna(String nombreColumna) {
        double dou_suma = 0;
        for (int i = 0; i < getTotalFilas(); i++) {
            try {
                double actual = Double.parseDouble(getValor(i, nombreColumna));
                dou_suma += actual;
            } catch (Exception e) {
            }
        }
        return dou_suma;
    }

    public double getPromedioColumna(String nombreColumna) {
        double dou_suma = getSumaColumna(nombreColumna);
        try {
            dou_suma = dou_suma / getTotalFilas();
        } catch (Exception e) {
        }
        return dou_suma;
    }
    /*
     * Retorna valores de una columna de la siguiente froma '1','2','3'
     */

    public String getStringColumna(String nombreColumna) {
        String str_datos = "";
        for (int i = 0; i < getTotalFilas(); i++) {
            if (i > 0) {
                str_datos += ",";
            }
            str_datos += "'" + getValor(i, nombreColumna) + "'";
        }
        return str_datos;
    }

    public String getStringColumnaSeleccionados(String nombreColumna) {
        String str_datos = "";
        int num_columna = getNumeroColumna(nombreColumna);

        for (Fila seleccionado : seleccionados) {
            if (!str_datos.isEmpty()) {
                str_datos += ",";
            }
            str_datos += "'" + seleccionado.getCampos()[num_columna] + "'";
        }
        return str_datos;

    }

    private MethodExpressionValueChangeListener crearValueChange(String valueExpression) {
        MethodExpression listener = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + valueExpression + "}", null,
                new Class[]{
                    ValueChangeEvent.class
                }
        );
        MethodExpressionValueChangeListener valueChangeListener = new MethodExpressionValueChangeListener(listener);
        return valueChangeListener;
    }

    public boolean isValidarInsertar() {
        return validarInsertar;
    }

    public void setValidarInsertar(boolean validarInsertar) {
        this.validarInsertar = validarInsertar;
    }

    public int getNivelNodo(String valorPadre) {
        //Obtiene el nivel del padre  PARA GENERAR EL CODIGOR RECURSIVO 
        int nivel = 0;
        while (valorPadre != null) {
            nivel++;
            String lstr_sql = "SELECT ".concat(campoPadre).concat(" FROM ").concat(tabla).concat(" WHERE ").concat(campoPrimaria).concat("='").concat(valorPadre).concat("'");
            List lrse_fila = conexion.consultar(lstr_sql);

            if (lrse_fila != null && lrse_fila.isEmpty()) {
                valorPadre = lrse_fila.get(0).toString();
            } else {
                valorPadre = null;
            }
        }
        return nivel;
    }

    public boolean isMostrarNumeroRegistros() {
        return mostrarNumeroRegistros;
    }

    public void setMostrarNumeroRegistros(boolean mostrarNumeroRegistros) {
        //muestra en tipo formulario el numero de registros
        this.mostrarNumeroRegistros = mostrarNumeroRegistros;
    }

    public Fila[] getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(Fila[] seleccionados) {
        this.seleccionados = seleccionados;
    }

    public void inicializar() {
        this.getChildren().clear();
        filas = new LinkedList();
        columnas = null;
        tabla = "";
        campoPrimaria = "";
        campoForanea = "";
        campoNombre = "";
        campoPadre = "";
        campoOrden = "";
        sql = "";
        condicion = "";
        condicionForanea = "";
        condicionPadre = "";
        condicionBuscar = "";
        condicionEmpresa = "";
        valorforanea = "";
        valorPadre = "";
        filaActual = 0;
        insertadas.clear();
        insertadasCodigos.clear();
        modificadas = new ArrayList();

        eliminadas.clear();
        eliminadasCondicion.clear();
        metodo = "";
        key = 'A';
        lectura = false;
        relacion = new ArrayList();
        TABLA_PADRE = null;
        setFirst(0);
        ide_tabl = "-1";
        numeroTabla = -1;
        metodoFiltro = "";
        tipoSeleccion = false;
        tipoFormulario = false;
        arbol = null;
        auditoria = false;
        recuperarLectura = false;
        generarPrimaria = true;
        idCompleto = "";
        validarInsertar = false; //Para que inserte uno y no le deje insertar mas hasta que guarde
        mostrarNumeroRegistros = true;
        foco_valida_insertar = null; //Guarda el foco para q cuando cambie de fila y este validando el insertar no permita seleccionar
        filtro = null; //Para guardar el filtro de primefaces en tabla lectura,check y seleccion
        seleccionados = null;

        try {
            this.getClientBehaviors().clear();
        } catch (Exception e) {
        }
    }

    public boolean isTipoSeleccionMultiple() {
        return tipoSeleccionMultiple;
    }

    public void setTipoSeleccionMultiple(boolean tipoSeleccionMultiple) {
        this.tipoSeleccionMultiple = tipoSeleccionMultiple;
        this.tipoSeleccion = !tipoSeleccionMultiple;
    }

    public void setFilasSeleccionados(String seleccionados) {
        if (seleccionados != null && !seleccionados.isEmpty()) {
            seleccionados = seleccionados.replace("'", "");
            seleccionados += ",";
            String[] vec = seleccionados.split(",");
            Fila selec[] = new Fila[vec.length];
            for (int i = 0; i < vec.length; i++) {
                Fila fila = this.getFila(vec[i]);
                if (fila != null) {
                    selec[i] = fila;
                }
            }
            this.setSeleccionados(selec);
        } else {
            this.setSeleccionados(null);
        }

    }

    public boolean isCondicionSucursal() {
        return condicionSucursal;
    }

    public void setCondicionSucursal(boolean condicionSucursal) {
        this.condicionSucursal = condicionSucursal;
    }

    private String quitarCaracteres(String valor) {
        //Quita los caraceres especiales de un valor
        String str = "";
        for (int i = 0; i < valor.length(); i++) {
            if (Character.isLetterOrDigit(valor.charAt(i))) {
                str += valor.charAt(i);
            }
        }
        return str;
    }

    private boolean validarUnicoInsertar(String columnas[]) {
        //Valida que si existen 
        boolean existe = false;
        if (insertadas.size() > 0) {
            String str[] = new String[insertadas.size()];
            for (int i = 0; i < insertadas.size(); i++) {
                int indice = filas.indexOf(buscaFila(insertadas.get(i)));
                Object[] fila = filas.get(indice).getCampos();
                StringBuilder str_valor = new StringBuilder();
                for (String columna : columnas) {
                    str_valor.append(fila[getNumeroColumna(columna)]);
                }
                str[i] = str_valor.toString();
            }
            for (int i = 0; i < str.length; i++) {
                String str_cad1 = str[i];
                for (int j = (i + 1); j < str.length; j++) {
                    String str_cad2 = str[j];
                    if (str_cad1.equals(str_cad2)) {
                        existe = true;
                        break;
                    }
                }
                if (existe) {
                    return existe;
                }
            }
        }
        return existe;
    }

    private boolean validarUnico(String columnas[], String valores[]) {
        String str_sql = "SELECT * FROM " + tabla + " WHERE ";
        boolean existe = false;
        for (int i = 0; i < columnas.length; i++) {
            if (i > 0) {
                str_sql += " AND ";
            }
            str_sql += columnas[i] + "=" + valores[i].replace("^", ",") + " ";
        }
        List lis_sql = conexion.consultar(str_sql);
        if (!lis_sql.isEmpty()) {
            existe = true;
        }
        return existe;
    }

    private boolean validarUnicoModificar(String rowkey, String columnas[], String valores[]) {
        TablaGenerica tab_antes = new TablaGenerica();
        tab_antes.setConexion(conexion);
        tab_antes.setSql("SELECT * FROM " + tabla + " WHERE " + getCondicionPrimaria(rowkey));
        tab_antes.ejecutarSql();
        boolean boo_cambio = false;
        for (int i = 0; i < columnas.length; i++) {
            String str_valor_base = tab_antes.getValor(columnas[i]);
            String str_valor_actual = valores[i];
            str_valor_actual = str_valor_actual.replace("'", "");
            str_valor_actual = str_valor_actual.replace("^", ",");
            if (str_valor_base == null && str_valor_actual != null) {
                boo_cambio = true;
                break;
            }
            if (str_valor_actual == null && str_valor_base != null) {
                boo_cambio = true;
                break;
            }
            if (!str_valor_actual.equalsIgnoreCase(str_valor_base)) {
                boo_cambio = true;
                break;
            }
        }
        if (boo_cambio) {
            String str_sql = "SELECT * FROM " + tabla + " WHERE ";
            boolean existe = false;
            for (int i = 0; i < columnas.length; i++) {
                if (i > 0) {
                    str_sql += " AND ";
                }
                str_sql += columnas[i] + "=" + valores[i] + " ";
            }
            List lis_sql = conexion.consultar(str_sql);
            if (!lis_sql.isEmpty()) {
                existe = true;
            }
            return existe;
        }
        return false;
    }

    public String getRecursivo(String astr_valor_padre, int num) {
        String codigo = "";
        //Genera el codigo recursivo de las pantallas recursivas 
        if (!campoRecursivo.isEmpty()) {
            try {
                String lstr_mascara = getColumna(campoRecursivo).getMascara();
                if (lstr_mascara != null && !lstr_mascara.isEmpty()) {
                    lstr_mascara = lstr_mascara.replace('?', ' ');
                    lstr_mascara = lstr_mascara.trim();
                    lstr_mascara = lstr_mascara.replace('9', '0');
                    //1 si no se inserta en la raiz
                    String padres = "";
                    String maximo = "0";
                    if (!valorPadre.isEmpty()) {
                        String sql_padre = "SELECT " + campoRecursivo + " from " + tabla + " where " + campoPrimaria + " =" + valorPadre;
                        List lis = conexion.consultar(sql_padre);
                        if (lis != null && !lis.isEmpty()) {
                            padres = lis.get(0) + "";
                            sql_padre = "SELECT MAX(" + campoRecursivo + ") FROM " + tabla + " where " + campoPadre + " =" + valorPadre;
                            lis = conexion.consultar(sql_padre);
                            if (lis.get(0) != null) {
                                maximo = lis.get(0) + "";
                            }
                        }
                    } else {
                        sql = "SELECT MAX(" + campoRecursivo + ") FROM " + tabla + " where " + campoPadre + " is null ";
                        List lis = conexion.consultar(sql);
                        if (lis.get(0) != null) {
                            maximo = lis.get(0) + "";
                        }
                    }
                    String l = "";
                    if (!maximo.equals("0")) {
                        l = maximo.substring(padres.length(), maximo.length());
                    } else {
                        l = lstr_mascara.substring(padres.length(), lstr_mascara.length());
                    }

                    if (l.indexOf(".") == 0) {
                        l = l.substring(1, l.length());
                    }
                    if (l.indexOf(".") > 0) {
                        l = l.substring(0, l.indexOf("."));
                    }
                    maximo = (Integer.parseInt(l) + (num + 1)) + "";
                    maximo = framework.generarCero(l.length() - maximo.length()) + maximo;
                    if (padres.isEmpty()) {
                        codigo = maximo;
                    } else {
                        //codigo = padres + maximo + ".";
                        codigo = padres + maximo;
                    }
                }
            } catch (Exception ex) {
                framework.crearError("No se pudo generar el codigo recursivo", "", ex);
            }
        }
        return codigo;
    }

    public String getCampoRecursivo() {
        return campoRecursivo;
    }

    public void setCampoRecursivo(String campoRecursivo, String mascara) {
        this.campoRecursivo = campoRecursivo;
        this.getColumna(campoRecursivo).setMascara(mascara);
        this.getColumna(campoRecursivo).setLectura(true);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        setWidgetVar(id);
    }

    public void setSeleccionTabla(String seleccionTabla) {
        this.seleccionTabla = seleccionTabla;
    }

    public boolean isEmpty() {
        return filas.isEmpty();
    }

    private boolean isCampoPistaAuditoriaInsert(String campo) {
        String str_campos = "USUARIO_INGRE,FECHA_INGRE,HORA_INGRE";
        if (str_campos.contains(campo.toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCampoPistaAuditoriaUpdate(String campo) {
        String str_campos = "USUARIO_ACTUA,FECHA_ACTUA,HORA_ACTUA";
        return str_campos.contains(campo.toUpperCase());
    }

    private void configuraCampoPistaAuditoria(String campo) {
        String str_campos = "USUARIO_INGRE,FECHA_INGRE,HORA_INGRE,USUARIO_ACTUA,FECHA_ACTUA,HORA_ACTUA";
        if (str_campos.contains(campo.toUpperCase())) {
            //es campo de pista de auditoria
            columnas[getNumeroColumna(campo)].setVisible(false);
        }
    }

    private String getValorCampoPistaAuditoria(String campo) {
        switch (campo.toUpperCase()) {
            case "USUARIO_ACTUA":
            case "USUARIO_INGRE":
                return "'" + framework.getVariable("nombre_usuario") + "'";
            case "FECHA_ACTUA":
            case "FECHA_INGRE":
                return conexion.getFormatoFechaSQL(framework.getFechaActual());
            case "HORA_ACTUA":
            case "HORA_INGRE":
                return conexion.getFormatoHoraSQL(framework.getHoraActual());
        }
        return null;
    }

    public boolean isMostrarcampoSucursal() {
        return mostrarcampoSucursal;
    }

    public void setMostrarcampoSucursal(boolean mostrarcampoSucursal) {
        this.mostrarcampoSucursal = mostrarcampoSucursal;
    }

    public void agregarColumnaEliminar(String metodo) {
        Column col_elimina = new Column();
        col_elimina.setWidth(10);
        Link lin_enlace = new Link();
        lin_enlace.agregarImagen("/imagenes/im_cerrar.png", "", "");
        lin_enlace.setTitle("Eliminar");
        lin_enlace.setMetodo(metodo);
        col_elimina.getChildren().add(lin_enlace);
        this.getChildren().add(col_elimina);
    }

    public void agregarColumnaModificar(String metodo) {
        Column col_modificar = new Column();
        col_modificar.setWidth(10);
        Link lin_enlace = new Link();
        lin_enlace.agregarImagen("/imagenes/im_modificar.png", "", "");
        lin_enlace.setMetodo(metodo);
        lin_enlace.setTitle("Modificar");
        col_modificar.getChildren().add(lin_enlace);
        this.getChildren().add(col_modificar);
    }

    public void agregarColumna(String metodo, String pathImagen, String titulo) {
        Column col_modificar = new Column();
        col_modificar.setWidth(10);
        Link lin_enlace = new Link();
        lin_enlace.agregarImagen(pathImagen, "", "");
        lin_enlace.setMetodo(metodo);
        lin_enlace.setTitle(titulo);
        col_modificar.getChildren().add(lin_enlace);
        this.getChildren().add(col_modificar);
    }

    public String getClaveCompuesta() {
        return claveCompuesta;
    }

    public void setClaveCompuesta(String claveCompuesta) {
        this.claveCompuesta = claveCompuesta;
    }

    private String getCondicionPrimaria(String rowkey) {
        String str_condicion = "";
        String[] str_campos = claveCompuesta.split(",");

        int int_fila = getNumeroFila(rowkey);
        for (int i = 0; i < str_campos.length; i++) {

            if (i > 0) {
                str_condicion += " AND ";
            }
            String str_valor = null;
            if (int_fila >= 0) {
                str_valor = getValor(int_fila, str_campos[i]);
            }
            if (str_valor == null) {
                str_valor = "-1";
            }
            str_condicion += str_campos[i] + "=" + str_valor + " ";
        }
        return str_condicion;
    }

    private String getCondicionPrimaria() {
        //si tiene varios campos de primaria y esta generar primaria true descarta el primer campo , ya q se asume q ese es el q se va a calcular el max
        String str_condicion = "";
        String[] str_campos = claveCompuesta.split(",");
        for (int i = 0; i < str_campos.length; i++) {
            if (i == 0 && campoPrimaria.isEmpty() && generarPrimaria == true) {
                continue;
            }
            if (i > 0 && str_condicion.isEmpty() == false) {
                str_condicion += " AND ";
            }
            String str_valor = getValor(str_campos[i]);
            if (str_valor == null) {
                str_valor = "-1";
            }
            str_condicion += str_campos[i] + "=" + str_valor + " ";
        }
        return str_condicion;
    }

    private int getNumeroPrimaria() {
        return claveCompuesta.split(",").length;
    }

    private String getPrimariaCalcula() {
        //retorna el campo q se calcula en una clave compuesta
        return claveCompuesta.split(",")[0];
    }

    public void imprimirSql() {
        System.out.println(this.getSql());
    }

    public boolean isConvertidor() {
        return convertidor;
    }

    public void setConvertidor(boolean convertidor) {
        this.convertidor = convertidor;
    }

    public void limpiarFiltros() {
        if (dibujo && !tipoFormulario) {
            framework.ejecutarJavaScript(this.getId() + ".clearFilters();");//borra la seleccion                   
        }
    }

    public boolean isDibujo() {
        return dibujo;
    }

    public void setDibujo(boolean dibujo) {
        this.dibujo = dibujo;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public boolean isOrdenar() {
        return ordenar;
    }

    public void setOrdenar(boolean ordenar) {
        this.ordenar = ordenar;
    }

}
