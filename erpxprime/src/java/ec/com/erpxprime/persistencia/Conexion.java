package ec.com.erpxprime.persistencia;

import ec.com.erpxprime.framework.aplicacion.Framework;
import static ec.com.erpxprime.framework.aplicacion.Framework.FORMATO_FECHA;
import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.Tabla;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.sql.DataSource;

/**
 *
 * @author Dxedushx
 */
public class Conexion implements Serializable {

    private String unidad_persistencia;
    private EntityManagerFactory fabrica;
    private EntityManager manejador;
    private List<String> sqlPantalla = new ArrayList();
    private List<ArchivosSQL> sqlArchivos = new ArrayList();
    private final Framework framework = new Framework();
    private String servidorWeb = "";
    private boolean imprimirSqlConsola = false;
    private String nombreDataSource;
    private Connection con_conexion = null;
    /**
     * Sirve para obtener en nombre de la base de datos por defecto es postgres
     */
    public String NOMBRE_MARCA_BASE = "oracle";

    public Conexion() {
//        if (unidad_persistencia != null) {
//            conectar();
//        }
    }

    /**
     *
     * @param tipo true EntityManager false; Connection true
     */
    public void conectar(boolean tipo) {
        try {
            servidorWeb = framework.getPropiedad("servidorWeb");

            NOMBRE_MARCA_BASE = framework.getPropiedad("marcaBase");
            if (NOMBRE_MARCA_BASE == null) {
                NOMBRE_MARCA_BASE = "oracle";
            }
            if (servidorWeb != null && servidorWeb.equals("tomcat")) {
                nombreDataSource = "java:/comp/env/".concat(unidad_persistencia);
            } else if (servidorWeb != null && servidorWeb.equals("jboss6")) {
                nombreDataSource = "java:/".concat(unidad_persistencia);
            } else {
                nombreDataSource = unidad_persistencia;
            }

            if (tipo) {
                fabrica = Persistence.createEntityManagerFactory(unidad_persistencia);
                manejador = fabrica.createEntityManager();
                manejador.setFlushMode(FlushModeType.COMMIT);
            } else {
                try {
                    Context initContext = new InitialContext();
                    DataSource datasource = (DataSource) initContext.lookup(nombreDataSource);
                    con_conexion = datasource.getConnection();
                    //con_conexion.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                } catch (NamingException | SQLException e) {
                    System.out.println("ERROR getDataSource() ".concat(nombreDataSource).concat("  ").concat(e.getMessage()));
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR al Conectar -.-: " + e.getMessage());
            framework.crearError(e.getMessage(), "Clase Conexion ", e);
        }
    }

    public void desconectar(boolean tipo) {
        if (tipo) {
            try {
                manejador.close();
            } catch (Exception e) {
            }
            try {
                fabrica.close();
            } catch (Exception e) {
            }
        } else {
            try {
                if (con_conexion != null) {
                    con_conexion.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public String ejecutarSql(String sql) {
        conectar(false);
        String mensaje = "";
        sql = validarSentecia(sql);

        Statement sta_sentencia = null;
        try {
            con_conexion.setAutoCommit(false);
            sta_sentencia = con_conexion.createStatement();
            sta_sentencia.executeUpdate(sql);
            if (imprimirSqlConsola) {
                System.out.println(sql);
            }
            con_conexion.commit();
        } catch (SQLException e) {
            try {
                con_conexion.rollback();
            } catch (Exception ex) {
            }
            System.out.println("ERROR ejecutar() : ".concat(sql).concat(" : ").concat(e.getMessage()));
            framework.crearError(e.getMessage(), "Clase Conexion en el método ejecutar()", sql, e);
            mensaje = e.getMessage();
        } finally {
            if (con_conexion != null) {
                try {
                    sta_sentencia.close();
                } catch (Exception e) {
                }
            }
        }
        desconectar(false);
        return mensaje;
    }

    public String validarSentecia(String sql) {
        //si es oracle repmlaza true = 1 false =0
        if (NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle") || NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
            sql = sql.trim();
            sql = sql.replace("true", "1");
            sql = sql.replace("TRUE", "1");
            sql = sql.replace("false", "0");
            sql = sql.replace("FALSE", "0");
            //Si una linea termina en ; se quita en oracle
            if (sql.endsWith(";")) {
                sql = sql.replace(";", "");
            }
        }

        if (NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
            sql = sql.trim();
            sql = sql.replace("||", "+");
        }

        return sql;
    }

    public List consultar(String sql) {
        conectar(true);
        sql = validarSentecia(sql);
        List lista = new ArrayList();
        //Si tiene manejador        
        if (manejador != null) {
            try {
                if(imprimirSqlConsola){
                    System.out.println(sql);
                }
                lista = manejador.createNativeQuery(sql).getResultList();
            } catch (Exception e) {
                System.out.println("ERROR consultar() : ".concat(sql).concat(" : ").concat(e.getMessage()));
                framework.crearError(e.getMessage(), "Clase Conexion en el método consultar()", sql, e);
            }
        }
        desconectar(true);
        return lista;
    }

    public void agregarArchivos(String sql, List<byte[]> archivos) {
        sqlArchivos.add(new ArchivosSQL(sql, archivos));
    }

    private long bloquearTabla(String nombreTabla, String campoPrimaria, String condicion, int numero) {
        long lon_maximo = 0;
        TablaGenerica tab_bloqueo = framework.consultar("SELECT * FROM tbl_bloqueo WHERE tabla='".concat(nombreTabla.toUpperCase()).concat("'"));
        if (tab_bloqueo.isEmpty()) {
            //Obtiene el maximo
            List lmax = consultar("SELECT MAX(" + campoPrimaria + ") FROM " + nombreTabla + " " + condicion);
            long lon_max = 0;
            if (lmax != null) {
                try {
                    lon_max = Long.parseLong(lmax.get(0) + "");
                } catch (Exception e) {
                }
            }
            lon_max += numero;

            //No existe bloqueo de esa tabla
            String str_sql = "INSERT INTO tbl_bloqueo "
                    .concat("(id_bloqueo,id_usuario,tabla,maximo,nombre_usuario) ")
                    .concat("VALUES(")
                    .concat("(select (case when MAX(id_bloqueo) is null then 0 else MAX(id_bloqueo)+1 end) as MAXIMO FROM tbl_bloqueo),")
                    .concat(framework.getVariable("id_usuario")).concat(",")
                    .concat("'").concat(nombreTabla.toUpperCase()).concat("',")
                    .concat(lon_max + "").concat(",")
                    .concat("'").concat(framework.getVariable("nombre_usuario") + "'")
                    .concat(")");
            framework.getConexion().ejecutarSql(str_sql);
        }
        //Recupero el maximo de la tabla de bloqueos
        List lis_maximo = framework.getConexion().consultar("SELECT maximo FROM tbl_bloqueo WHERE tabla='".concat(nombreTabla.toUpperCase()).concat("'"));
        try {
            lon_maximo = Long.parseLong(lis_maximo.get(0) + "");
        } catch (Exception e) {
        }
        //Actualizar el bloqueo mas el numero de inserts
        long lon_nuevo = lon_maximo + numero;
        lon_maximo = lon_maximo + 1;
        String str_update = "UPDATE tbl_bloqueo SET maximo=".concat(String.valueOf(lon_nuevo)).concat(" WHERE tabla='").concat(nombreTabla.toUpperCase()).concat("'");
        framework.getConexion().ejecutarSql(str_update);
        return lon_maximo;
    }

    public long getMaximo(String tabla, String campoPrimaria, int numero) {
        return bloquearTabla(tabla, campoPrimaria, "", numero);
    }

    public long getMaximoMultiple(String tabla, String campoPrimaria, String condicion, int numero) {
        return bloquearTabla(tabla, campoPrimaria, condicion, numero);
    }

    public void agregarSqlPantalla(String sql) {
        if (!sqlPantalla.contains(sql)) {
            sqlPantalla.add(sql);
        }
    }

    public void agregarSql(String sql) {
        agregarSqlPantalla(sql);
    }

    public String ejecutarListaSql() {
        conectar(false);
        String mensaje = "";
        if (!sqlPantalla.isEmpty()) {
            String sql = "";
            Statement sta_sentencia = null;
            try {
                con_conexion.setAutoCommit(false);
                sta_sentencia = con_conexion.createStatement();
                for (String sqlPantalla1 : sqlPantalla) {
                    sql = sqlPantalla1;
                    sql = validarSentecia(sql);
                    if (imprimirSqlConsola) {
                        System.out.println(sql);
                    }
                    sta_sentencia.executeUpdate(sql);
                }
                con_conexion.commit();
            } catch (SQLException e) {
                try {
                    con_conexion.rollback();
                } catch (Exception ex) {
                }
                System.out.println("FALLO: ".concat(e.getMessage()));
                mensaje = e.getMessage();
                framework.crearError(mensaje, "En el método guardarPantalla()", sql, e);
            } finally {
                if (con_conexion != null) {
                    try {
                        if (sta_sentencia != null) {
                            sta_sentencia.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
            sqlPantalla.clear();
        }
        desconectar(false);
        return mensaje;
    }

    public String guardarPantalla() {
        conectar(false);
        String mensaje = "";
        if (!sqlPantalla.isEmpty()) {
            String sql = "";
            Statement sta_sentencia = null;
            try {
                con_conexion.setAutoCommit(false);
                sta_sentencia = con_conexion.createStatement();
                for (String sqlPantalla1 : sqlPantalla) {
                    sql = sqlPantalla1;
                    sql = validarSentecia(sql);
                    if (imprimirSqlConsola) {
                        System.out.println(sql);
                    }
                    sta_sentencia.executeUpdate(sql);
                }
                ///Guarda archivos en la bdd
                for (ArchivosSQL archivo : sqlArchivos) {
                    try (PreparedStatement ps = con_conexion.prepareStatement(archivo.getSql())) {                        
                        int contador = 1;
                        for (byte[] byteActual : archivo.getArchivos()) {
                            try {                                                                
                                ps.setBytes(contador, byteActual);
                                contador++;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        ps.executeUpdate();                        
                    }
                }

                con_conexion.commit();
            } catch (SQLException e) {
                try {
                    con_conexion.rollback();
                } catch (Exception ex) {
                }
                System.out.println("fallo  ".concat(e.getMessage()));
                mensaje = e.getMessage();
                rollbackPantalla(framework.getPantalla());
                framework.crearError(mensaje, "En el método guardarPantalla()", sql, e);
            } finally {
                if (con_conexion != null) {
                    try {
                        if (sta_sentencia != null) {
                            sta_sentencia.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
            if (mensaje.isEmpty()) {
                if (sqlPantalla.size() > 0) {
                    framework.agregarMensaje("Se Guardo correctamente", "");
                    commitPantalla(framework.getPantalla());
                }
            }
            sqlPantalla.clear();
            sqlArchivos.clear();
        }
        desconectar(false);
        return mensaje;
    }

    private void commitPantalla(UIComponent componente) {
        if (componente != null) {
            for (UIComponent kid : componente.getChildren()) {
                if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.DataTableRenderer")) {
                    ((Tabla) kid).onCommit();
                }
                if (kid.getChildren().size() > 0) {
                    commitPantalla(kid);
                }
            }
        }

    }

    public void rollbackPantalla(UIComponent componente) {
        if (componente != null) {
            for (UIComponent kid : componente.getChildren()) {
                if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.DataTableRenderer")) {
                    ((Tabla) kid).onRollback();
                }
                if (kid.getChildren().size() > 0) {
                    rollbackPantalla(kid);
                }
            }
        }
        sqlPantalla.clear();
        sqlArchivos.clear();
    }

    public String getUnidad_persistencia() {
        return unidad_persistencia;
    }

    public void setUnidad_persistencia(String unidad_persistencia) {
        this.unidad_persistencia = unidad_persistencia;
//        if (unidad_persistencia != null) {
//            conectar();
//        }
    }

    public List<String> getSqlPantalla() {
        return sqlPantalla;
    }

    public void setSqlPantalla(List<String> sqlPantalla) {
        this.sqlPantalla = sqlPantalla;
    }

    public String getServidorWeb() {
        return servidorWeb;
    }

    public void setServidorWeb(String servidorWeb) {
        this.servidorWeb = servidorWeb;
    }

    public boolean isImprimirSqlConsola() {
        return imprimirSqlConsola;
    }

    public void setImprimirSqlConsola(boolean imprimirSqlConsola) {
        this.imprimirSqlConsola = imprimirSqlConsola;
    }

    public Connection getConnection() {
        return con_conexion;
    }

    public String getFormatoFechaSQL(String fecha) {
        if (fecha != null) {
            if (NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle")) {
                fecha = "TO_DATE('".concat(fecha).concat("','").concat(FORMATO_FECHA.toUpperCase()).concat("')");
            } else if (NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
                fecha = "CONVERT(datetime,'".concat(fecha).concat("', 111)");
            } else {
                if (fecha.trim().startsWith("'") == false) {
                    fecha = "'".concat(fecha).concat("'");
                }
            }
        }
        return fecha;
    }

    public String getFormatoHoraSQL(String hora) {
        if (hora != null) {
            if (NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle")) {
                hora = "TO_TIMESTAMP('".concat(hora).concat("','HH24:MI:SS')");
            } else if (NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
                hora = "CONVERT(datetime,'".concat(hora).concat("', 108)");
            } else {
                if (hora.trim().startsWith("'") == false) {
                    hora = "'".concat(hora).concat("'");
                }
            }
        }
        return hora;
    }
}

class ArchivosSQL {

    String sql;
    List<byte[]> archivos;

    public ArchivosSQL(String sql, List<byte[]> archivos) {
        this.sql = sql;
        this.archivos = archivos;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<byte[]> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<byte[]> archivos) {
        this.archivos = archivos;
    }

}