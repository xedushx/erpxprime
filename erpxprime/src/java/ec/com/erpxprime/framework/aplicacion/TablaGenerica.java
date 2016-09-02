/*
 * Copyright (c) 2013, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.aplicacion;

import ec.com.erpxprime.persistencia.Conexion;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class TablaGenerica {

    private final Framework framework = new Framework();
    private LinkedList<Fila> filas = new LinkedList();
    private Columna[] columnas;
    private String tabla = "";
    private String campoPrimaria = "";
    private String sql = "";
    private String condicion = "";
    private Conexion conexion;
    private String campoOrden = "";
    private int filaActual = 0;
    private List<String> insertadas = new ArrayList();
    private List<String> insertadasCodigos = new ArrayList(); // Guarada el maximo generado
    private final Map<String, String> eliminadasCondicion = new HashMap(); // Guarada la condicionsql para eliminar
    private List<String> modificadas = new ArrayList();
    private List<String> eliminadas = new ArrayList();
    private char key = 'A';
    public String ide_tabl = "-1";
    private int numeroTabla = -1;
    private boolean auditoria = false;
    private boolean generarPrimaria = true;
    private String claveCompuesta = "";
    private final String id_empresa;
    private final String id_sucursal;

    public TablaGenerica() {
        conexion = framework.getConexion();
        String str_auditoria = framework.getVariable("AUDITORIA_OPCI");
        id_empresa = framework.getVariable("empresa");
        id_sucursal = framework.getVariable("id_sucursal");
        if (str_auditoria != null) {
            auditoria = framework.toBoolean(str_auditoria);
        }
    }

    public void setNombreTabla(String tabla) {
        this.tabla = tabla;
    }

    public void setCampoPrimaria(String campoPrimaria) {
        this.campoPrimaria = campoPrimaria;
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

    public void setTabla(String tabla, String campoPrimaria) {
        this.setTabla(tabla, campoPrimaria, -1);
    }

    public void ejecutarSql() {
        if (!tabla.isEmpty()) {
            //Campos 
            String campos = "";
            for (Columna columnaActual : columnas) {
                if (!campos.isEmpty()) {
                    campos = campos.concat(", ");
                }
                campos = campos.concat(columnaActual.getNombre());
                ////PARA MULTI EMPRESA
                if (columnaActual.getNombre().equalsIgnoreCase("id_empresa") && !columnaActual.getNombre().equalsIgnoreCase(campoPrimaria)) {
                    columnaActual.setVisible(false);
                    columnaActual.setValorDefecto(id_empresa);
                } ////PARA MULTI SUCURSAL
                if (columnaActual.getNombre().equalsIgnoreCase("id_sucursal") && !columnaActual.getNombre().equalsIgnoreCase(campoPrimaria)) {
                    columnaActual.setVisible(false);
                    columnaActual.setValorDefecto(id_sucursal);
                }
            }
            if (!condicion.isEmpty()) {
                condicion = " AND " + condicion;
            }
            if (campoOrden.isEmpty()) {
                //setCampoOrden(campoPrimaria); antes
                setCampoOrden(columnas[0].getNombre());
            }
            sql = "SELECT ".concat(campos).concat(" FROM ").concat(tabla).concat(" WHERE 1=1 ").concat(condicion).concat(" ORDER BY ").concat(campoOrden);
        }
        insertadas.clear();
        modificadas.clear();
        eliminadas.clear();
        insertadasCodigos.clear();
        //System.out.println(" ....  " + str_sql);
        List<String> lista = conexion.consultar(sql);
        formarFilas(lista);
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
                fila_nueva.setCampos(campos);
                ///Si no tiene clave primaria le asigno al rowkey el valor del for
                if (!campoPrimaria.isEmpty()) {
                    fila_nueva.setRowKey(campos[num_campoPrimaria] + "");
                } else {
                    fila_nueva.setRowKey(campos[num_campoPrimaria] + "");
                }
                filas.add(fila_nueva);
                this.filaActual = 0;
            }
        } else {
            setFilaActual(0);
            limpiar();
        }
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

                //para oracle
                if (framework.getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle")) {
                    //Para saber si es date
                    if (columna_nueva.getTipo().equals("DATE")) {
                        columna_nueva.setTipoJava("java.sql.Date");
                    }
                    //Para saber si es entero                    
                    if (columna_nueva.getTipo().equalsIgnoreCase("NUMBER") && (columna_nueva.getAncho() == 38 || columna_nueva.getDecimales() == 0)) {
                        columna_nueva.setTipoJava("java.lang.Integer");
                        columna_nueva.setLongitud(20);
                    }
                    //Para saber si es de tipo Hora
                    if (columna_nueva.getTipo().equalsIgnoreCase("TIMESTAMP")) {
                        columna_nueva.setTipoJava("java.sql.Time");
                    }
                    //tipo clob
                    if (columna_nueva.getTipo().equalsIgnoreCase("CLOB")) {
                        columna_nueva.setTipo("Text");
                        columna_nueva.setTipoJava("java.lang.String");
                    }
                }

                //para sqlserver
                if (framework.getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
                    //problema con identificar fecha y horas
                    //  if (columna_nueva.getTipoJava().equals("java.sql.Timestamp")) {
                    if (columna_nueva.getNombre().toUpperCase().startsWith("FECHA")) {
                        columna_nueva.setTipoJava("java.sql.Date");

                    } else if (columna_nueva.getNombre().toUpperCase().startsWith("HORA")) {
                        columna_nueva.setTipoJava("java.sql.Time");
                    }
                    //  }
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
            }
            resultado.close();
            sta_sentencia.close();
            conexion.desconectar(false);
        } catch (SQLException e) {
            framework.crearError("No se pudo formar las columans de la tabla " + tabla + " : " + e.getMessage(), "en el método formarColumnas() " + sql, e);
            System.out.println("ERROR formarColumnas():" + e.getMessage() + "   " + sql);
        }
    }

    public void modificar(int numero_fila) {
        if (modificadas.indexOf(filas.get(numero_fila).getRowKey()) < 0) {
            modificadas.add(filas.get(numero_fila).getRowKey());
        }
    }

    public void insertar() {
        crearFila();
    }

    private void crearFila() {
        Object campos[] = new Object[columnas.length];
        Fila fila_nueva = new Fila();
        fila_nueva.setCampos(campos);
        fila_nueva.setRowKey(String.valueOf(key));
        filaActual = 0;
        insertadas.add(String.valueOf(key));
        filas.addFirst(fila_nueva);
        //Valor por defecto
        for (Columna columnaActual : columnas) {
            if (columnaActual.getValorDefecto() != null) {
                setValor(columnaActual.getNombre(), columnaActual.getValorDefecto());
            }
        }
        key++;
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

            for (int i = 0; i < columnas.length; i++) {
                if (columnas[i].isExterna() == false) {
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

            for (String insertada : insertadas) {
                int indice = filas.indexOf(buscaFila(insertada));
                Object[] fila = filas.get(indice).getCampos();
                String valores = "";
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
                if (auditoria == true) {
                    String sql_auditoria = "INSERT INTO tbl_auditoria "
                            .concat("(fecha_audi,hora_audi,ide_usua,sql_audi,accion_audi,ip_audi,tabla_audi,id_empresa,id_sucursal,id_opcion) ")
                            .concat(" values (").concat(framework.getFormatoFechaSQL(framework.getFechaActual())).concat(", ")
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
                            .concat("(fecha_audi,hora_audi,ide_usua,sql_audi,accion_audi,ip_audi,tabla_audi,id_empresa,ide_sucu,id_opcion) ")
                            .concat(" values (").concat(framework.getFormatoFechaSQL(framework.getFechaActual())).concat(", ")
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

            boo_sql = true;
            for (String modificada : modificadas) {
                Object[] fila = buscaFila(modificada).getCampos();
                String valores = "";
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
                        conexion.getSqlPantalla().clear();
                        conexion.rollbackPantalla(framework.getPantalla());
                        return false;
                    }
                }
                //cambio x clave compuesta
                String sentencia = "UPDATE ".concat(tabla).concat(" SET ").concat(valores).concat(" WHERE ").concat(getCondicionPrimaria(modificada));
                if (auditoria == true) {
                    //recupera el valor anterior para registrar en la auditoria
                    String str_sql_antes = "";
                    TablaGenerica tab_antes = framework.consultar("SELECT * FROM ".concat(tabla).concat(" WHERE ").concat(getCondicionPrimaria(modificada)));
                    for (int j = 0; j < tab_antes.getTotalColumnas(); j++) {
                        String str_nombre_columna = tab_antes.getColumnas()[j].getNombre();
                        str_sql_antes += " *" + str_nombre_columna + " = " + tab_antes.getValor(str_nombre_columna) + " ";
                    }
                    String sql_auditoria = "INSERT INTO tbl_auditoria "
                            .concat("(fecha_audi,hora_audi,ide_usua,sql_audi,accion_audi,ip_audi,tabla_audi,id_empresa,ide_sucu,id_opcion) ")
                            .concat(" values (").concat(framework.getFormatoFechaSQL(framework.getFechaActual())).concat(", ")
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
        return true;
    }

    public boolean eliminar() {
        boolean boo_elimino = false;
        if (filas.size() > 0) {
            if (insertadas.indexOf(filas.get(filaActual).getRowKey()) >= 0) {
                insertadas.remove(filas.get(filaActual).getRowKey());
                filas.remove(filaActual);
                boo_elimino = true;
            } else {
                try {
                    //cambio para claves compuestas       
                    String str_condi = getCondicionPrimaria(filas.get(filaActual).getRowKey());
                    String str_mensaje = "";
                    Statement sta_sentencia = null;
                    try {
                        getConexion().conectar(false);
                        getConexion().getConnection().setAutoCommit(false);
                        sta_sentencia = getConexion().getConnection().createStatement();
                        sta_sentencia.executeQuery("DELETE FROM ".concat(tabla).concat(" WHERE ").concat(str_condi));
                    } catch (SQLException e) {
                        str_mensaje = e.getMessage();
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
                    } else {
                        framework.agregarMensajeError("No se puede eliminar la fila", str_mensaje);
                    }

                } catch (Exception e) {
                    System.out.println("ERROR AL ELIMINAR() ".concat(e.getMessage()));
                }
            }
        }
        return boo_elimino;
    }

    public void setValor(String nombre_campo, String valor) {
        //Configura el valor de un campo de la fila actual
        if (valor == null || valor.equalsIgnoreCase("null")) {
            valor = "";
        }
        try {
            switch (columnas[getNumeroColumna(nombre_campo)].getControl()) {
                case "Calendario":
                    if (columnas[getNumeroColumna(nombre_campo)].getTipoJava().equals("java.sql.Date")) {
                        filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFecha(valor);
                    } else {
                        filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFechaHora(valor);
                    }
                    break;
                case "Hora":
                    filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getHora(valor);
                    break;
                default:
                    filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = valor;
                    break;
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
            switch (columnas[getNumeroColumna(nombre_campo)].getControl()) {
                case "Calendario":
                    if (columnas[getNumeroColumna(nombre_campo)].getTipoJava().equals("java.sql.Date")) {
                        filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFecha(valor);
                    } else {
                        filas.get(filaActual).getCampos()[getNumeroColumna(nombre_campo)] = framework.getFechaHora(valor);
                    }
                    break;
                case "Hora":
                    filas.get(fila).getCampos()[getNumeroColumna(nombre_campo)] = framework.getHora(valor);
                    break;
                default:
                    filas.get(fila).getCampos()[getNumeroColumna(nombre_campo)] = valor;
                    break;
            }
        } catch (Exception e) {
            framework.crearError("No se pudo asignar el valor : ".concat(valor).concat(" al campo :").concat(nombre_campo).concat("  <br/>").concat(e.getMessage()), "setValor()", e);
            System.out.println("ERROR setValor() " + nombre_campo + " : " + e.getMessage());
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

    public String getValor(String nombre_campo) {
        //Retorna el valor de un campo de la fila actual
        Object valor = null;
        if (filas.size() > 0) {
            try {
                int num_coulumna = getNumeroColumna(nombre_campo);
                valor = filas.get(filaActual).getCampos()[num_coulumna];
                if ((columnas[num_coulumna].getControl().equals("Calendario"))) {
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

    public String getValor(int fila, String nombre_campo) {
        //Retorna el valor de un campo de una fila determinada
        Object valor = null;
        try {
            int num_coulumna = getNumeroColumna(nombre_campo);
            valor = filas.get(fila).getCampos()[num_coulumna];
            if ((columnas[num_coulumna].getControl().equals("Calendario"))) {
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

    public int getNumeroTabla() {
        return numeroTabla;
    }

    public void setNumeroTabla(int numeroTabla) {
        this.numeroTabla = numeroTabla;
    }

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
    }

    public void setFilaActual(String valorPrimaria) {
        try {
            this.filaActual = getNumeroFila(valorPrimaria);
        } catch (Exception e) {
            this.filaActual = 0;
            limpiar();
        }
    }

    public String getCampoPrimaria() {
        return campoPrimaria;
    }

    public String getTabla() {
        return tabla;
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

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        if (condicion == null) {
            condicion = "";
        }
        this.condicion = condicion;
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

    public void limpiar() {
        filas.clear();
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

    public int getTotalColumnas() {
        return columnas.length;
    }

    public boolean isAuditoria() {
        return auditoria;
    }

    public void setAuditoria(boolean auditoria) {
        this.auditoria = auditoria;
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
        TablaGenerica tab_antes = framework.consultar("SELECT * FROM " + tabla + " WHERE " + getCondicionPrimaria(rowkey));
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

    public boolean isEmpty() {
        return filas.isEmpty();
    }

    private boolean isCampoPistaAuditoriaInsert(String campo) {
        String str_campos = "USUARIO_INGRE,FECHA_INGRE,HORA_INGRE";
        return str_campos.contains(campo.toUpperCase());
    }

    private boolean isCampoPistaAuditoriaUpdate(String campo) {
        String str_campos = "USUARIO_ACTUA,FECHA_ACTUA,HORA_ACTUA";
        return str_campos.contains(campo.toUpperCase());
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

    public String getCampoOrden() {
        return campoOrden;
    }

    public void setCampoOrden(String campoOrden) {
        this.campoOrden = campoOrden;
    }
}
