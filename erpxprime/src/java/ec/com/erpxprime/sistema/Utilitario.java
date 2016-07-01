/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import ec.xprime.componentes.pf_buscar_tabla;
import ec.xprime.componentes.pf_confirmar;
import ec.xprime.componentes.pf_error_sql;
import ec.xprime.componentes.pf_notificacion;
import ec.xprime.componentes.pf_panel_grupo;
import ec.xprime.componentes.pf_tabla;
import ec.xprime.persistencia.cla_conexion;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import org.primefaces.util.Constants;

/**
 *
 * @author Diego
 */
public class Utilitario implements Serializable {

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm:ss");

    public String getFechaActual() {
        Date fecha = new Date();
        return formatoFecha.format(fecha);
    }

    public String getHoraActual() {
        Date fecha = new Date();
        return formatoHora.format(fecha);
    }

    public String getFormatoFechaHora(Object fecha) {
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return formatoFechaHora.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getFechaHoraActual() {
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date fecha = new Date();
        return formatoFechaHora.format(fecha);
    }

    public String getFormatoFecha(Object fecha) {
        try {
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getFormatoHora(Object fecha) {
        try {
            return formatoHora.format(fecha);
        } catch (Exception e) {
        }
        return fecha.toString();
    }

    public Date getFecha(String fecha) {
        try {
            Date dat_fecha = new Date();
            dat_fecha = formatoFecha.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    public Date getHora(String hora) {
        try {
            Date dat_fecha = new Date();
            dat_fecha = formatoHora.parse(hora);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    public void crearVariable(String nombre, String valor) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombre.toUpperCase(), valor);
    }

    public String getVariable(String nombre) {
        String str_variable = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nombre.toUpperCase());
        if (str_variable == null && getConexion() != null) {
            List lis = getConexion().consultar("SELECT VALOR_PARA FROM SIS_PARAMETROS WHERE LOWER(NOM_PARA)='" + nombre.toLowerCase() + "'");
            if (lis != null && !lis.isEmpty()) {
                return lis.get(0) + "";
            }
        } else {
            return str_variable;
        }
        return null;
    }

    public cla_conexion getConexion() {
        cla_conexion conexion = null;
        try {
            conexion = (cla_conexion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
            conexion.getConnection().setAutoCommit(false);
        } catch (Exception e) {
        }
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

    public void agregarMensajeInfo(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, detalle));
    }

    public String formarUpdate(String update) {
        if (update == null) {
            update = "";
        }
        String str_update = "";
        if (update.indexOf(",") > 0) {
            update = update + ",";

            String str_aux = "";
            for (int i = 0; i < update.length(); i++) {
                if (update.charAt(i) != ',') {
                    str_aux += update.charAt(i);
                } else {
                    if (!str_aux.isEmpty() && !str_aux.equals("@all") && !str_aux.equals("@form") && !str_aux.equals("@this") && !str_aux.equals("@none") && !str_aux.equals("@parent")) {
                        str_aux = ":formulario:" + str_aux;
                    }
                    str_update += str_aux;
                    str_aux = "";
                    if (i != update.length() - 1) {
                        str_update += ",";
                    }
                }
            }
        } else {
            if (!update.isEmpty() && !update.equals("@all") && !update.equals("@form") && !update.equals("@this") && !update.equals("@none") && !update.equals("@parent")) {
                str_update = ":formulario:" + update;
            } else {
                str_update = update;
            }
        }
        if (str_update.isEmpty()) {
            str_update = null;
        }
        return str_update;
    }

    public void crearError(String mensaje, String ubicacion, Exception e) {
        pf_error_sql error_sql = (pf_error_sql) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
        if (!error_sql.isVisible()) {
            error_sql.setErrorSQL(mensaje, ubicacion, e);
            error_sql.dibujar();
            addUpdate("error_sql");
        }
    }

    public void crearError(String mensaje, String ubicacion, String sql, SQLException excepcion) {
        pf_error_sql error_sql = (pf_error_sql) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
        if (!error_sql.isVisible()) {
            error_sql.setErrorSQL(mensaje, ubicacion, sql, excepcion);
            error_sql.dibujar();
            addUpdate("error_sql");
        }
    }

    public void crearError(String mensaje, String ubicacion, String sql, Exception excepcion) {
        pf_error_sql error_sql = (pf_error_sql) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
        if (!error_sql.isVisible()) {
            error_sql.setErrorSQL(mensaje, ubicacion, sql, excepcion);
            error_sql.dibujar();
            addUpdate("error_sql");
        }
    }

    public void agregarNotificacionInfo(String titulo, String mensaje) {
        pf_notificacion not_notificacion = (pf_notificacion) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:not_notificacion");
        if (not_notificacion != null) {
            not_notificacion.setNotificacion(titulo, mensaje, "");
            RequestContext requestContext = RequestContext.getCurrentInstance();
            addUpdate("not_notificacion");
            requestContext.execute("not_notificacion.show();");
        }
    }

    public void agregarNotificacion(String titulo, String mensaje, String pathImagen) {
        pf_notificacion not_notificacion = (pf_notificacion) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:not_notificacion");
        if (not_notificacion != null) {
            not_notificacion.setNotificacion(titulo, mensaje, pathImagen);
            addUpdate("not_notificacion");
            ejecutarJavaScript("not_notificacion.show();");
        }
    }

    public pf_tabla getTablaisFocus() {
        pf_tabla tabla = (pf_tabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:" + getVariable("TABLA_FOCO"));
        return tabla;
    }

    public pf_panel_grupo getPantalla() {
        pf_panel_grupo pan_dibuja = (pf_panel_grupo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:dibuja");
        return pan_dibuja;
    }

    public pf_confirmar getConfirmaGuardar() {
        pf_confirmar con_guarda = (pf_confirmar) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:con_guarda");
        return con_guarda;
    }

    public pf_panel_grupo getMensajes() {
        return (pf_panel_grupo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:mensajes");
    }

    public UIComponent getComponente(String id) {
        return FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:" + id);
    }

    public List getListaNiveles() {
        //pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
        List lista = new ArrayList();
        Object fila1[] = {
            "HIJO", "HIJO"
        };
        Object fila2[] = {
            "PADRE", "PADRE"
        };
        lista.add(fila1);
        lista.add(fila2);
        return lista;
    }

    public void addUpdate(String update) {
        //Actualiza un componente 
        RequestContext requestContext = RequestContext.getCurrentInstance();
        update = formarUpdate(update);
        update = update.replace(":formulario:", "formulario:");
        if (update.indexOf(",") > 0) {
            String componentes[] = update.split(",");
            for (int i = 0; i < componentes.length; i++) {
                requestContext.update(componentes[i]);
            }
        } else {
            requestContext.update(update);
        }

    }

    public void addUpdateTabla(pf_tabla tabla, String columnas, String componentes) {
        if (columnas != null && !columnas.isEmpty()) {
            String[] campos = columnas.split(",");

            for (int i = 0; i < campos.length; i++) {
                String str_update = tabla.getColumna(campos[i]).getId();

                if (tabla.isTipoFormulario() == false) {
//                    str_update = str_update.replace("**", tabla.getUltimaFilaModificada() + "");
                }
                addUpdate(str_update);
            }
        }
        if (componentes != null && !componentes.isEmpty()) {
            String[] compo = componentes.split(",");

            for (int i = 0; i < compo.length; i++) {
                String str_update = compo[i];
                addUpdate(str_update);
            }
        }
    }

    public void ejecutarJavaScript(String javaScript) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute(javaScript);
    }

    public String getIp() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        return request.getRemoteAddr();
    }

    public boolean validarRUC(String str_ruc) {
        boolean boo_correcto = false;
        try {
            if (str_ruc.length() == 13) {
                int[] int_digitos = new int[10];
                int int_coeficiente = 10;
                String str_valida = str_ruc.substring(10, 13);

                if (str_valida.equals("001")) {
                    for (int i = 0; i < int_digitos.length; i++) {
                        int_digitos[i] = Integer.parseInt(str_ruc.charAt(i) + "");
                    }
                    int int_digito_verifica = int_digitos[9];
                    int[] int_multiplica = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    if (int_digitos[2] == 9) {
                        int[] int_multiplica9 = {4, 3, 2, 7, 6, 5, 4, 3, 2};
                        int_multiplica = int_multiplica9;
                        int_coeficiente = 11;
                    }
                    if (int_digitos[2] == 6) {
                        int[] aint_multiplica6 = {3, 2, 7, 6, 5, 4, 3, 2};
                        int_digito_verifica = int_digitos[8];
                        int_multiplica = aint_multiplica6;
                        int_coeficiente = 11;
                    }
                    int int_suma = 0;
                    for (int i = 0; i < (int_digitos.length - 1); i++) {
                        try {
                            if (int_coeficiente == 10) {
                                int mul = int_digitos[i] * int_multiplica[i];
                                if (mul > 9) {
                                    String aux = mul + "";
                                    mul = Integer.parseInt(aux.charAt(0) + "") + Integer.parseInt(aux.charAt(1) + "");
                                }
                                int_suma += mul;
                            } else {
                                int_suma += (int_digitos[i] * int_multiplica[i]);
                            }
                        } catch (Exception ex) {
                        }
                    }
                    int int_valida = 0;
                    if (int_coeficiente == 10) {
                        if (int_suma % 10 == 0) {
                            int_valida = 0;
                        } else {
                            int_valida = 10 - (int_suma % 10);
                        }
                    } else {
                        if (int_suma % 11 == 0) {
                            int_valida = 0;
                        } else {
                            int_valida = 11 - (int_suma % 11);
                        }
                    }

                    if (int_valida == 0) {
                        int_digito_verifica = 0;
                    }
                    if (int_valida == int_digito_verifica) {
                        boo_correcto = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return boo_correcto;
    }

    public String getUltimaFechaMes(String fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFecha(fecha));
        String fechamax = getAño(fecha) + "-" + getMes(fecha) + "-" + cal.getActualMaximum(cal.DAY_OF_MONTH);
        return getFormatoFecha(fechamax);
    }

    public boolean validarCedula(String str_cedula) {
        boolean boo_correcto = false;
        try {
            if (str_cedula.length() == 10) {

                if (!str_cedula.equals("2222222222")) {
                    int lint_suma = 0;

                    for (int i = 0; i < 9; i++) {
                        int lstr_digito = Integer.parseInt(str_cedula.charAt(i) + "");
                        if (i % 2 == 0) {
                            lstr_digito = lstr_digito * 2;
                            if (lstr_digito > 9) {
                                String lstr_aux = lstr_digito + "";
                                lstr_digito = Integer.parseInt(lstr_aux.charAt(0) + "") + Integer.parseInt(lstr_aux.charAt(1) + "");
                            }
                        }
                        lint_suma += lstr_digito;
                    }
                    if (str_cedula.charAt(9) != '0') {
                        String lstr_aux = lint_suma + "";
                        int lint_superior = (Integer.parseInt(lstr_aux.charAt(0) + "") + 1) * 10;
                        int lint_ultimo_real = lint_superior - lint_suma;
                        int lint_ultimo_digito = Integer.parseInt(str_cedula.charAt(9) + "");
                        if (lint_ultimo_digito == lint_ultimo_real) {
                            boo_correcto = true;
                        }
                    } else {
                        //Para cedulas que terminan en 0
                        if (lint_suma % 10 == 0) {
                            boo_correcto = true;
                        }
                    }

                } else {
                    boo_correcto = false;
                }

            }
        } catch (Exception ex) {
        }
        return boo_correcto;
    }

    public int getAño(String fecha) {
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

    public boolean isFechaValida(String fecha) {
        if (fecha == null) {
            return false;
        }
        try {
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

    public boolean isFechaMayor(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);

        if (cal2.before(cal1)) {
            return true;
        }
        return false;
    }

    public boolean isFechaMenor(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);

        if (cal1.before(cal2)) {
            return true;
        }
        return false;
    }

    public int getDiferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
        String fechaInicioString = formatoFecha.format(fechaInicial);
        try {
            fechaInicial = formatoFecha.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = formatoFecha.format(fechaFinal);
        try {
            fechaFinal = formatoFecha.parse(fechaFinalString);
        } catch (ParseException ex) {
        }
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    public Date sumarDiasFecha(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        return new Date(cal.getTimeInMillis());
    }

    public boolean isEmailValido(String email) {
        Pattern pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        Matcher mat = pat.matcher(email);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
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

    public String getFormatoNumero(Object numero, int numero_decimales) {
        String lstr_formato = "#";
        for (int i = 0; i < numero_decimales; i++) {
            if (i == 0) {
                lstr_formato += ".";
            }
            lstr_formato += "#";
        }
        DecimalFormat formatoNumero;
        DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
        idfs_simbolos.setDecimalSeparator('.');
        formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);
        try {
            double ldob_valor = Double.parseDouble(numero.toString());
            return formatoNumero.format(ldob_valor);
        } catch (Exception ex) {
            return null;
        }
    }

    public Date getDate() {
        return new Date();
    }

    public void mandarMensaje() {
        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        pushContext.push("/notifications", new FacesMessage("aaaa", "eee"));
    }

    public pf_tabla consultar(String sql) {
        pf_tabla tab_sql = new pf_tabla();
        tab_sql.setSql(sql);
        tab_sql.ejecutarSql();
        return tab_sql;

    }

    public String generarCero(int cantidad) {
        String cero = "";
        for (int li_i = 1; li_i <= cantidad; li_i++) {
            cero += "0";
        }
        return cero;
    }

    public pf_buscar_tabla getBuscaTabla() {
        return (pf_buscar_tabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:bus_buscar");
    }

    public double evaluarExpresion(String expresion) {
        //Resuleve el valor de una expresion Ejemplo: 5+3-3
        double resultado = 0;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object operacion;
        try {
            expresion = expresion.replace("[", "(");
            expresion = expresion.replace("]", ")");
            operacion = engine.eval(expresion);
            resultado = Double.parseDouble(operacion.toString());
        } catch (ScriptException e) {
            System.out.println("ERROR al evaluarExpresion( " + expresion + " )  :" + e.toString());
        }
        return resultado;
    }

    public void resetarPantalla() {
        RequestContext.getCurrentInstance().reset("formulario:dibuja");
    }

    public String getLetrasNumero(Object numero) {
        String letras = getFormatoNumero(numero);
        if (letras != null) {
            try {
                letras = recursivoNumeroLetras(Integer.parseInt(letras.substring(0, letras.lastIndexOf(".")))) + " CON" + recursivoNumeroLetras(Integer.parseInt(letras.substring((letras.lastIndexOf(".") + 1), letras.length())));
                letras = letras.toUpperCase();
                letras = letras.trim();
            } catch (Exception e) {
            }
        }
        return letras;
    }

    public String getLetrasDolarNumero(Object numero) {
        String letras = getFormatoNumero(numero);
        if (letras != null) {
            try {
                String centavos = (Integer.parseInt(letras.substring((letras.lastIndexOf(".") + 1), letras.length()))) + "";
                if (centavos.trim().length() == 1) {
                    centavos = "0" + centavos;
                }
                letras = recursivoNumeroLetras(Integer.parseInt(letras.substring(0, letras.lastIndexOf(".")))) + " CON " + centavos + "/100 ";
                letras = letras.toUpperCase();
                letras = letras.trim();
            } catch (Exception e) {
            }
        }
        return letras;
    }

    private String recursivoNumeroLetras(int numero) {
        String cadena = new String();
        // Aqui identifico si lleva millones
        if ((numero / 1000000) > 0) {
            if ((numero / 1000000) == 1) {
                cadena = " Un Millon" + recursivoNumeroLetras(numero % 1000000);
            } else {
                cadena = recursivoNumeroLetras(numero / 1000000) + " Millones" + recursivoNumeroLetras(numero % 1000000);
            }
        } else {
            // Aqui identifico si lleva Miles
            if ((numero / 1000) > 0) {

                if ((numero / 1000) == 1) {
                    cadena = " Mil" + recursivoNumeroLetras(numero % 1000);
                } else {
                    cadena = recursivoNumeroLetras(numero / 1000) + " Mil" + recursivoNumeroLetras(numero % 1000);
                }
            } else {
                // Aqui identifico si lleva cientos
                if ((numero / 100) > 0) {
                    if ((numero / 100) == 1) {
                        if ((numero % 100) == 0) {
                            cadena = " Cien";
                        } else {
                            cadena = " Ciento" + recursivoNumeroLetras(numero % 100);
                        }
                    } else {
                        if ((numero / 100) == 5) {
                            cadena = " Quinientos" + recursivoNumeroLetras(numero % 100);
                        } else {
                            if ((numero / 100) == 9) {
                                cadena = " Novecientos" + recursivoNumeroLetras(numero % 100);
                            } else {
                                cadena = recursivoNumeroLetras(numero / 100) + "cientos" + recursivoNumeroLetras(numero % 100);
                            }
                        }
                    }
                } // Aqui se identifican las Decenas
                else {
                    if ((numero / 10) > 0) {
                        switch (numero / 10) {
                            case 1:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Diez";
                                        break;
                                    case 1:
                                        cadena = " Once";
                                        break;
                                    case 2:
                                        cadena = " Doce";
                                        break;
                                    case 3:
                                        cadena = " Trece";
                                        break;
                                    case 4:
                                        cadena = " Catorce";
                                        break;
                                    case 5:
                                        cadena = " Quince";
                                        break;
                                    default:
                                        cadena = " Diez y " + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 2:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Veinte";
                                        break;
                                    default:
                                        cadena = " Veinti" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 3:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Treinta";
                                        break;
                                    default:
                                        cadena = " Treinta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 4:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cuarenta";
                                        break;
                                    default:
                                        cadena = " Cuarenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 5:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cincuenta";
                                        break;
                                    default:
                                        cadena = " Cincuenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 6:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Sesenta";
                                        break;
                                    default:
                                        cadena = " Sesenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 7:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Setenta";
                                        break;
                                    default:
                                        cadena = " Setenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 8:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Ochenta";
                                        break;
                                    default:
                                        cadena = " Ochenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 9:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Noventa";
                                        break;
                                    default:
                                        cadena = " Noventa y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                        }
                    } else {
                        switch (numero) {
                            case 1:
                                cadena = " Uno";
                                break;
                            case 2:
                                cadena = " Dos";
                                break;
                            case 3:
                                cadena = " Tres";
                                break;
                            case 4:
                                cadena = " Cuatro";
                                break;
                            case 5:
                                cadena = " Cinco";
                                break;
                            case 6:
                                cadena = " Seis";
                                break;
                            case 7:
                                cadena = " Siete";
                                break;
                            case 8:
                                cadena = " Ocho";
                                break;
                            case 9:
                                cadena = " Nueve";
                                break;
                            case 0:
                                //      cadena = " Cero";
                                break;
                        }
                    }
                }
            }
        }
        return cadena;
    }

    public void crearArchivo(String path, String nombre, String tipo) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        StreamedContent content;
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (Exception e) {
            crearError("No se puede generar el archivo : " + nombre + " <br/> path: " + path, "crearArchivo()", e);
        }
        if (stream == null) {
            return;
        }
        content = new DefaultStreamedContent(stream, tipo, nombre);
        if (content == null) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        String contentDispositionValue = "attachment";
        try {
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", contentDispositionValue + ";filename=\"" + content.getName() + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();
            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (Exception e) {
            crearError("No se puede descargar : " + nombre + " <br/> path: " + path, "crearArchivo()", e);
        }
    }

    public String getURL() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        String path = request.getRequestURL() + "";
        path = path.substring(0, path.lastIndexOf("/"));
        return path;
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

    public String generarComillasLista(List lista) {
        String ide_lista = "";
        try {
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i) != null) {
                    ide_lista = ide_lista.concat(lista.get(i).toString().concat(","));
                }
            }
            ide_lista = ide_lista.substring(0, ide_lista.length() - 1);

        } catch (Exception e) {
        }
        return ide_lista;
    }

    public String generarComillaSimple(String cadena) {
        String str_cadena = "";
        String[] vec = cadena.split(",");
        for (int i = 0; i < vec.length; i++) {
            if (!str_cadena.isEmpty()) {
                str_cadena += ",";
            }
            str_cadena += "'" + vec[i] + "'";
        }
        return str_cadena;
    }

    public String getCampoEmpresa(String campo) {
        String valor = null;
        pf_tabla tab_empresa = consultar("SELECT IDE_EMPR," + campo + " FROM SIS_EMPRESA WHERE IDE_EMPR=" + getVariable("IDE_EMPR"));
        if (tab_empresa.getTotalFilas() > 0) {
            valor = tab_empresa.getValor(0, campo);
        }
        return valor;
    }

    public void buscarNombresVisuales(pf_tabla tabla) {
        pf_tabla tab_campos = consultar("SELECT * FROM sis_campo WHERE ide_tabl in(SELECT ide_tabl FROM sis_tabla WHERE lower(tabla_tabl)='" + tabla.getTabla().toLowerCase() + "')");
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
}