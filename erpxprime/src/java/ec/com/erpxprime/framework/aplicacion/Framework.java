package ec.com.erpxprime.framework.aplicacion;

import ec.com.erpxprime.framework.componentes.AutoCompletar;
import ec.com.erpxprime.framework.componentes.Barra;
import ec.com.erpxprime.framework.componentes.Boton;
import ec.com.erpxprime.framework.componentes.Check;
import ec.com.erpxprime.framework.componentes.Combo;
import ec.com.erpxprime.framework.componentes.Confirmar;
import ec.com.erpxprime.framework.componentes.ErrorSQL;
import ec.com.erpxprime.framework.componentes.Grupo;
import ec.com.erpxprime.framework.componentes.Radio;
import ec.com.erpxprime.framework.componentes.SeleccionArchivo;
import ec.com.erpxprime.framework.componentes.Tabla;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx
 */
public class Framework implements Serializable {

    public final static String FORMATO_FECHA = "yyyy-MM-dd";
    public final static String FORMATO_HORA = "HH:mm:ss";
    public final static String FORMATO_FECHA_HORA = "yyyy-MM-dd HH:mm:ss";

    public Object cargarPantalla(String paquete, String tipo_opcion) {
        Object clase = null;
        try {
            try {
                Runtime basurero = Runtime.getRuntime();
                basurero.gc();
            } catch (Exception e) {
            }
            Class pantalla = Class.forName(paquete + "." + tipo_opcion);
            clase = pantalla.newInstance();
            buscarPermisosObjetos();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            crearError(ex.getMessage(), "mbe_index en el método cargar() ", ex);
        }
//        //1
//        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
//        UIViewRoot newView = FacesContext.getCurrentInstance().getApplication().getViewHandler().createView(FacesContext.getCurrentInstance(),
//                "/index.jsf");
//        //1
//        newView.setViewId(viewId);
//        FacesContext.getCurrentInstance().setViewRoot(newView);
        return clase;
    }

    /**
     * Crea un archivo para que pueda ser descargado en el navegador, debe
     * previamente existir el path de archivo
     *
     * @param archivo
     */
    public void descargarFile(File archivo) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        StreamedContent content;
        InputStream stream = null;
        try {
            stream = new FileInputStream(archivo);
        } catch (Exception e) {
            crearError("No se puede generar el archivo path: " + archivo.getAbsolutePath(), "crearArchivo()", e);
        }
        if (stream == null) {
            return;
        }
        content = new DefaultStreamedContent(stream);
        if (content == null) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        String contentDispositionValue = "attachment";
        try {
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", contentDispositionValue + ";filename=\"" + archivo.getName() + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();
            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            archivo.delete();
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (Exception e) {
            crearError("No se puede descargar :  path: " + archivo.getAbsolutePath(), "crearArchivo()", e);
        }
    }

    public void crearVariable(String nombre, String valor) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombre.toUpperCase(), valor);
    }

    public String getFechaHoraActual() {
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat(FORMATO_FECHA_HORA);
        Date fecha = new Date();
        return formatoFechaHora.format(fecha);
    }

    public String getFormatoFechaHora(Object fecha) {
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat(FORMATO_FECHA_HORA);
        try {
            return formatoFechaHora.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public Grupo getPantalla() {
        Grupo pan_dibuja = (Grupo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:dibuja");
        return pan_dibuja;
    }

    public Barra getBarra() {
        Barra pan_dibuja = (Barra) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:bar_botones");
        return pan_dibuja;
    }

    public String getVariable(String nombre) {
        String str_variable = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nombre.toUpperCase());
        if (str_variable == null && nombre.toUpperCase().startsWith("P_") && getConexion() != null) {
            List lis = getConexion().consultar("SELECT par_valor FROM tbl_parametro WHERE LOWER(par_nombre)='" + nombre.toLowerCase() + "'");
            if (lis != null && !lis.isEmpty()) {
                return lis.get(0) + "";
            }
        } else {
            return str_variable;
        }
        return null;
    }

    public Map<String, String> getVariables(String... nombres) {
        Map<String, String> parametros = new HashMap<>();
        String strCondicionParametro = "";
        for (String nombre : nombres) {
            String valor = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nombre.toUpperCase());
            if (valor == null && nombre.toUpperCase().startsWith("P_") && getConexion() != null) {
                if (strCondicionParametro.isEmpty() == false) {
                    strCondicionParametro += ",";
                }
                strCondicionParametro += "'" + nombre.toLowerCase() + "'";
            } else {
                parametros.put(nombre, valor);
            }
        }

        List lis = getConexion().consultar("SELECT par_nombre,par_valor FROM tbl_parametro WHERE LOWER(par_nombre) in (" + strCondicionParametro + ")");
        for (Object li : lis) {
            Object[] fila = (Object[]) li;
            parametros.put(String.valueOf(fila[0]), String.valueOf(fila[1]));
        }
        return parametros;
    }

    public Conexion getConexion() {
        Conexion conexion = (Conexion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
        return conexion;
    }

    public void agregarMensaje(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle));
    }

    public void agregarMensajeError(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle));
    }

    public Confirmar getConfirmaGuardar() {
        Confirmar con_guarda = (Confirmar) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:con_guarda");
        return con_guarda;
    }

    public void agregarMensajeInfo(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, detalle));
    }

    public String formarUpdate(String update) {
        if (update == null) {
            update = "";
        }
        StringBuilder str_update = new StringBuilder();
        if (update.indexOf(",") > 0) {
            update = update + ",";

            String str_aux = "";
            for (int i = 0; i < update.length(); i++) {
                if (update.charAt(i) != ',') {
                    str_aux = str_aux.concat(String.valueOf(update.charAt(i)));
                } else {
                    if (!str_aux.isEmpty() && !str_aux.equals("@all") && !str_aux.equals("@form") && !str_aux.equals("@this") && !str_aux.equals("@none") && !str_aux.equals("@parent")) {
                        str_aux = ":formulario:".concat(str_aux);
                    }
                    str_update.append(str_aux);
                    str_aux = "";
                    if (i != update.length() - 1) {
                        str_update.append(",");
                    }
                }
            }
        } else {
            if (!update.isEmpty() && !update.equals("@all") && !update.equals("@form") && !update.equals("@this") && !update.equals("@none") && !update.equals("@parent")) {
                str_update.append(":formulario:").append(update);
            } else {
                str_update.append(update);
            }
        }
        if (str_update.length() == 0) {
            return null;
        }
        return str_update.toString();
    }

    public void crearError(String mensaje, String ubicacion, Exception e) {
        try {
            ErrorSQL error_sql = (ErrorSQL) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
            if (error_sql != null && !error_sql.isVisible()) {
                error_sql.setErrorSQL(mensaje, ubicacion, e);
                error_sql.dibujar();
                addUpdate("error_sql");
            }
        } catch (Exception ex) {
        }

    }

    public void crearError(String mensaje, String ubicacion, String sql, SQLException excepcion) {
        ErrorSQL error_sql = (ErrorSQL) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
        if (error_sql != null && !error_sql.isVisible()) {
            error_sql.setErrorSQL(mensaje, ubicacion, sql, excepcion);
            error_sql.dibujar();
            addUpdate("error_sql");
        }
    }

    public void crearError(String mensaje, String ubicacion, String sql, Exception excepcion) {
        ErrorSQL error_sql = (ErrorSQL) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
        if (error_sql != null && !error_sql.isVisible()) {
            error_sql.setErrorSQL(mensaje, ubicacion, sql, excepcion);
            error_sql.dibujar();
            addUpdate("error_sql");
        }
    }

    public void addUpdate(String update) {
        //Actualiza un componente 
        if (update == null) {
            return;
        }
        RequestContext requestContext = RequestContext.getCurrentInstance();
        update = formarUpdate(update);
        update = update.replace(":formulario:", "formulario:");
        if (update.indexOf(",") > 0) {
            String componentes[] = update.split(",");
            for (String componente : componentes) {
                requestContext.update(componente);
            }
        } else {
            requestContext.update(update);
        }

    }

    public void ejecutarJavaScript(String javaScript) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute(javaScript);
    }

    public void buscarPermisosObjetos() {
//        List lis_objetos = getConexion().consultar("SELECT LECTURA_PEOB,VISIBLE_PEOB,NOM_OBOP,ID_OBOP FROM SIS_PERFIL_OBJETO,SIS_OBJETO_OPCION WHERE SIS_PERFIL_OBJETO.IDE_OBOP=SIS_OBJETO_OPCION.IDE_OBOP AND  id_opcion=".concat(getVariable("id_opcion")).concat(" AND SIS_PERFIL_OBJETO.id_perfil=").concat(getVariable("id_perfil")));
//        Grupo pan_dibuja = getPantalla();
//        if (!lis_objetos.isEmpty()) {
//            permisosPantalla(pan_dibuja, lis_objetos);
//        }

        //Si la pantalla esta como solo lectura
        List lis_tabla = getConexion().consultar("SELECT lectura,id_opcion FROM tbl_perfil_opcion WHERE id_opcion=".concat(getVariable("id_opcion")).concat(" AND id_perfil=").concat(getVariable("id_perfil")));
        if (!lis_tabla.isEmpty()) {
            Object[] fila = (Object[]) lis_tabla.get(0);
            if (fila[0] != null) {
                //si es true
                if (fila[0].toString().equalsIgnoreCase("true") || fila[0].toString().equals("1")) {
                    Barra bar_botones = getBarra();
                    if (bar_botones != null) {
                        bar_botones.setLectura();
                    }
                }
            }
        }
    }

    private void permisosPantalla(UIComponent uic_componente, List lis_objetos) {
        for (UIComponent kid : uic_componente.getChildren()) {
            //MEjora para que no busque recursivo en componentes dialogo          
            if (kid.getRendererType() != null && !kid.getRendererType().equals("org.primefaces.component.DialogRenderer")) {
                for (Object lis_objeto : lis_objetos) {
                    Object[] fila = (Object[]) lis_objeto;
                    boolean boo_lectura = true;
                    boolean boo_visible = true;
                    if (fila[0] != null) {
                        boo_lectura = toBoolean(fila[0]);
                    }
                    if (fila[1] != null) {
                        boo_visible = toBoolean(fila[1]);
                    }
                    if (fila[2] == null) {
                        fila[2] = "";
                    }
                    if (fila[3] == null) {
                        fila[3] = "";
                    }
                    //Para los Botones   
                    if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.CommandButtonRenderer")) {
                        if ((kid.getId() != null && ((Boton) kid).getId().equalsIgnoreCase(fila[3].toString())) || (((Boton) kid).getTitle() != null && ((Boton) kid).getTitle().equalsIgnoreCase(fila[2].toString())) || (((Boton) kid).getValue() != null && ((Boton) kid).getValue().toString().equalsIgnoreCase(fila[2].toString()))) {
                            ((Boton) kid).setDisabled(boo_lectura);
                            ((Boton) kid).setRendered(boo_visible);
                        }
                    } //Para los Combos              
                    else if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.SelectOneMenuRenderer")) {
                        if ((kid.getId() != null && ((Combo) kid).getId().equalsIgnoreCase(fila[3].toString())) || (((Combo) kid).getTitle() != null && ((Combo) kid).getTitle().equalsIgnoreCase(fila[2].toString()))) {
                            ((Combo) kid).setDisabled(boo_lectura);
                            ((Combo) kid).setRendered(boo_visible);
                        }
                    } //Para los Autocompletar              
                    else if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.AutoCompleteRenderer")) {
                        if ((kid.getId() != null && ((AutoCompletar) kid).getId().equalsIgnoreCase(fila[3].toString())) || (((AutoCompletar) kid).getTitle() != null && ((AutoCompletar) kid).getTitle().equalsIgnoreCase(fila[2].toString()))) {
                            ((AutoCompletar) kid).setDisabled(boo_lectura);
                            ((AutoCompletar) kid).setRendered(boo_visible);
                        }
                    } //Para los Radio             
                    else if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.SelectOneRadioRenderer")) {
                        if ((kid.getId() != null && ((Radio) kid).getId().equalsIgnoreCase(fila[3].toString())) || (((Radio) kid).getTitle() != null && ((Radio) kid).getTitle().equalsIgnoreCase(fila[2].toString()))) {
                            ((Radio) kid).setDisabled(boo_lectura);
                            ((Radio) kid).setRendered(boo_visible);
                        }
                    } //Para los Check            
                    else if (kid.getRendererType() != null && kid.getRendererType().equals("org.primefaces.component.SelectBooleanCheckboxRenderer")) {
                        if ((kid.getId() != null && ((Check) kid).getId().equalsIgnoreCase(fila[3].toString())) || (((Check) kid).getTitle() != null && ((Check) kid).getTitle().equalsIgnoreCase(fila[2].toString()))) {
                            ((Check) kid).setDisabled(boo_lectura);
                            ((Check) kid).setRendered(boo_visible);
                        }
                    }
                }
                if (kid.getChildren().size() > 0) {
                    permisosPantalla(kid, lis_objetos);
                }
            }
        }
    }

    public boolean tieneCampoEmpresa(String tabla, String campoPrimaria) {
        //Retorna si una tabla tiene id_empresa     
        String sql = "SELECT * FROM ".concat(tabla).concat(" WHERE ").concat(campoPrimaria).concat("=-1");
        Statement sta_sentencia = null;
        ResultSet resultado = null;
        try {
            getConexion().conectar(false);
            sta_sentencia = getConexion().getConnection().createStatement();
            resultado = sta_sentencia.executeQuery(sql);
            ResultSetMetaData rsm_metadata = resultado.getMetaData();
            for (int i = 1; i <= rsm_metadata.getColumnCount(); i++) {
                if (rsm_metadata.getColumnName(i).equalsIgnoreCase("id_empresa")) {
                    return true;
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (sta_sentencia != null) {
                    sta_sentencia.close();
                }
                if (resultado != null) {
                    resultado.close();
                }
            } catch (Exception e) {
            }
            getConexion().desconectar(false);
        }
        return false;
    }

    public String getFormatoFecha(Object fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getFormatoFecha(Object fecha, String formato) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getFormatoHora(Object fecha) {
        try {
            SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_HORA);
            return formatoHora.format(fecha);
        } catch (Exception e) {
        }
        return fecha.toString();
    }

    public String getFechaActual() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
        return formatoFecha.format(fecha);
    }

    public String getHoraActual() {
        Date fecha = new Date();
        SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_HORA);
        return formatoHora.format(fecha);
    }

    public Date getFecha(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            Date dat_fecha = new Date();
            dat_fecha = formatoFecha.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    public Date getFechaHora(String fecha) {
        try {
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat(FORMATO_FECHA_HORA);
            Date dat_fecha = new Date();
            dat_fecha = formatoFechaHora.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    public Date getHora(String hora) {
        try {
            Date dat_fecha = new Date();
            SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_HORA);
            dat_fecha = formatoHora.parse(hora);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    public String getFormatoNumero(Object numero) {
        DecimalFormat formatoNumero;
        DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
        idfs_simbolos.setDecimalSeparator('.');
        formatoNumero = new DecimalFormat("#.##", idfs_simbolos);
        formatoNumero.setMaximumFractionDigits(2);
        formatoNumero.setMinimumFractionDigits(2);
        try {
            double ldob_valor = Double.parseDouble(numero.toString());
            return formatoNumero.format(ldob_valor);
        } catch (Exception ex) {
            return null;
        }
    }

    public UIComponent getComponente(String id) {
        return FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:" + id);
    }

    public String getIp() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        //  return request.getRemoteAddr();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains("0:")) {
            ip = "local";
        }
        return ip;
    }

    public void cerrarSesion() {
        try {
            if (getConexion() != null) {
                // getConexion().desconectar();
            }
            // FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            Runtime basurero = Runtime.getRuntime();
            basurero.gc();
        } catch (Exception e) {
        }
    }

    public String getURL() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        String path = request.getRequestURL() + "";
        path = path.substring(0, path.lastIndexOf("/"));
        if (path.indexOf("portal") > 0) {
            //Si es llamado desde alguna pagina del portal
            path = path.substring(0, path.lastIndexOf("/"));
        }
        return path;
    }

    public boolean isFechaMayor(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);
        return cal2.before(cal1);
    }

    public boolean isFechaMenor(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);
        return cal1.before(cal2);
    }

    public boolean isFechaValida(String fecha) {
        if (fecha == null) {
            return false;
        }
        if (fecha.isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            if (fecha.trim().length() != formatoFecha.toPattern().length()) {
                return false;
            }
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean isHoraValida(String fecha) {
        if (fecha == null) {
            return false;
        }
        if (fecha.isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_HORA);
//            if (fecha.trim().length() != formatoFecha.toPattern().length()) {
//                return false;
//            }
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public int getAnio(String fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFecha(fecha));
        return cal.get(Calendar.YEAR);
    }

    public int getMes(String fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFecha(fecha));
        return (cal.get(Calendar.MONTH) + 1);
    }

    public int getDia(String fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFecha(fecha));
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public String getUltimaFechaMes(String fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFecha(fecha));
        String fechamax = String.valueOf(getAnio(fecha)).concat("-").concat(String.valueOf(getMes(fecha))).concat("-").concat(String.valueOf(cal.getActualMaximum(cal.DAY_OF_MONTH)));
        return getFormatoFecha(fechamax);
    }

    public String generarCero(int cantidad) {
        String cero = "";
        for (int li_i = 1; li_i <= cantidad; li_i++) {
            cero = cero.concat("0");
        }
        return cero;
    }

    public String getPropiedad(String nombre) {
        String valor = null;
        try {
            Properties propiedades = new Properties();
            propiedades.load(this.getClass().getResource("/config.properties").openStream());
            valor = propiedades.getProperty(nombre);
        } catch (Exception e) {
        }
        return valor;
    }

    public TablaGenerica consultar(String sql) {
        TablaGenerica tab_sql = new TablaGenerica();
        tab_sql.setSql(sql);
        tab_sql.ejecutarSql();
        return tab_sql;

    }

    public Tabla consultarTabla(String sql) {
        Tabla tab_sql = new Tabla();
        tab_sql.setSql(sql);
        tab_sql.ejecutarSql();
        return tab_sql;

    }

    public void buscarNombresVisuales(Tabla tabla) {
        TablaGenerica tab_campos = consultar("SELECT * FROM sis_campo WHERE ide_tabl in(SELECT ide_tabl FROM sis_tabla WHERE lower(tabla_tabl)='".concat(tabla.getTabla().toLowerCase()).concat("')"));
        if (tab_campos.getTotalFilas() > 0) {
            for (int i = 0; i < tabla.getTotalColumnas(); i++) {
                String nom_campo = tabla.getColumnas()[i].getNombre();
                for (int j = 0; j < tab_campos.getTotalFilas(); j++) {
                    if (nom_campo.equalsIgnoreCase(tab_campos.getValor(j, "nom_camp"))) {
                        tabla.getColumnas()[i].setNombreVisual(tab_campos.getValor(j, "nom_visual_camp"));
                        break;
                    }
                }
            }
        }
    }

    public boolean toBoolean(Object valor) {
        if (valor == null) {
            return false;
        }
        String str_valor = String.valueOf(valor);
        return str_valor.equals("1") || str_valor.equalsIgnoreCase("true");
    }

    public String getFormatoFechaSQL(String fecha) {
        if (fecha != null) {
            if (getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle")) {
                fecha = "TO_DATE('".concat(fecha).concat("','").concat(FORMATO_FECHA.toUpperCase()).concat("')");
            } else if (getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
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
            if (getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("oracle")) {
                hora = "TO_TIMESTAMP('".concat(hora).concat("','HH24:MI:SS')");
            } else if (getConexion().NOMBRE_MARCA_BASE.equalsIgnoreCase("sqlserver")) {
                hora = "CONVERT(datetime,'".concat(hora).concat("', 108)");
            } else {
                if (hora.trim().startsWith("'") == false) {
                    hora = "'".concat(hora).concat("'");
                }
            }
        }
        return hora;
    }

    public String generarComillasLista(List lista) {
        String ide_lista = "";
        for (Object lista1 : lista) {
            if (lista1 != null) {
                ide_lista = ide_lista.concat(lista1.toString().concat(","));
            }
        }
        ide_lista = ide_lista.substring(0, ide_lista.length() - 1);
        return ide_lista;
    }

    /**
     * Retorna el componente seleccionarArchivo
     *
     * @return
     */
    public SeleccionArchivo getSeleccionArchivo() {
        return (SeleccionArchivo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:sar_upload");
    }

    /**
     * Retorna la tabla que tiene el foco, es decir la que el usuario esta
     * manipulando
     *
     * @return
     */
    public Tabla getTablaisFocus() {
        Tabla tabla = (Tabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:" + getVariable("TABLA_FOCO").replace(":0:", ":")); //por el error rowindex 0        
        return tabla;
    }

    /**
     * Actualiza mediante ajax una columna o columnas de un componente tipo
     * Tabla
     *
     * @param tabla
     * @param columnas
     * @param componentes
     */
    public void addUpdateTabla(Tabla tabla, String columnas, String componentes) {
        if (columnas != null && !columnas.isEmpty()) {
            String[] campos = columnas.split(",");

            for (String campo : campos) {
                String str_update = tabla.getColumna(campo).getId();
                if (tabla.isTipoFormulario() == false) {
                    str_update = str_update.replace("**", tabla.getFilaActual() + "");
                }
                addUpdate(str_update);
            }
        }
        if (componentes != null && !componentes.isEmpty()) {
            String[] compo = componentes.split(",");

            for (String str_update : compo) {
                addUpdate(str_update);
            }
        }
    }

}
