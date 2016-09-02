package ec.com.erpxprime.framework.aplicacion;

import ec.com.erpxprime.framework.componentes.Ajax;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.framework.convertidores.UploadStreamedContent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author xedushx
 */
public class Columna {

    private String id;
    private String nombre;
    private String tipo;
    private String control = "Texto";
    private String valorDefecto;
    private boolean lectura = false;
    private boolean visible = true;
    private String nombreVisual;
    private int longitud;
    private int decimales;
    private String mascara;
    private int orden = -1;
    private int ancho = 11;
    private String tipoJava;
    private List listaCombo;
    private List listaRadio;
    private String sqlCombo;
    private final Framework framework = new Framework();
    private boolean requerida = false;
    private boolean filtro = false;
    private String filtroModo;
    private String sqlFiltro = "";
    private Ajax aja_change;
    private String onClick;
    private String onKeyup;
    private String comentario;
    private boolean etiqueta = false;
    private int imagenAutocompletar = 0;  //Numero de columna del autocompletar q es imagen
    private String estilo;
    private boolean permitirNullCombo = true;
    private boolean radioVertical = false;
    private boolean mayusculas = false;
    private boolean quitarCaracteresEspeciales = false;
    private boolean unico = false;
    private boolean externa = false;
    private boolean buscarenCombo = true;
    private Double total;
    private boolean suma = false;
    private String estiloColumna;
    private boolean formatoNumero = false; //Para implemetar el convertidor de numeros
    private String carpeta;

    private String altoImagen;
    private String anchoImagen;

    private Tabla tab_tabla;

    public void setFormatoNumero(int decimales) {
        formatoNumero = true;
        this.decimales = decimales;
    }

    public boolean isFormatoNumero() {
        return formatoNumero;
    }

    public void alinearIzquierda() {
        estiloColumna = "text-align:left;";
    }

    public void alinearDerecha() {
        estiloColumna = "text-align:right;";
    }

    public void alinearCentro() {
        estiloColumna = "text-align:center;";
    }

    public boolean isEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta() {
        this.etiqueta = true;
        this.setControl("Etiqueta");
    }

    public void setMetodoChange(String metodo) {
        aja_change = new Ajax();
        //  aja_change.setGlobal(false);
        aja_change.setMetodo(metodo);
    }

    public void setAutoCompletar() {
        if (control.equals("Combo")) {
            control = "Autocompletar";
        }
    }

    public void setCalendarioFechaHora() {
        control = "Calendario";
        tipoJava = "java.sql.Timestamp";
    }

    public void setMetodoChangeRuta(String metodo) {
        aja_change = new Ajax();
        // aja_change.setGlobal(false);
        aja_change.setMetodoRuta(metodo);
    }

    public List autocompletar(String query) {
        List suggestions = new ArrayList();
        for (Object listaCombo1 : listaCombo) {
            Object[] f = (Object[]) listaCombo1;
            for (int j = 1; j < f.length; j++) {
                if (f[j] != null) {
                    String fl = f[j] + "";
                    if (fl.trim().toUpperCase().contains(query.trim().toUpperCase())) {
                        suggestions.add(f);
                        break;
                    }
                }
            }
            if (suggestions.size() >= 10) {
                break;
            }
        }
        return suggestions;
    }

    public Object getObjetoAutocompletar(String valor) {

        for (Object listaCombo1 : listaCombo) {
            Object[] f = (Object[]) listaCombo1;
            if (f[0].toString().equals(valor)) {
                return f;
            }
        }
        return null;
    }

    public Ajax getMetodoChange() {
        return aja_change;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnKeyup() {
        return onKeyup;
    }

    public void setOnKeyup(String onKeyup) {
        this.onKeyup = onKeyup;
    }

    public String getSqlFiltro() {
        return sqlFiltro;
    }

    public void setSqlFiltro(String sqlFiltro) {
        this.sqlFiltro = sqlFiltro;
    }

    public boolean isFiltro() {
        return filtro;
    }

    public void setFiltro(boolean filtro) {
        this.filtro = filtro;
        this.filtroModo = "startsWith";
    }

    public boolean isLectura() {
        return lectura;
    }

    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
        this.setControl("Mascara");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreVisual() {
        return nombreVisual;
    }

    public void setNombreVisual(String nombreVisual) {
        this.nombreVisual = nombreVisual;
    }

    public boolean isRadioVertical() {
        return radioVertical;
    }

    public void setRadioVertical(boolean radioVertical) {
        this.radioVertical = radioVertical;
    }

    public String getValorDefecto() {
        return valorDefecto;
    }

    public void setValorDefecto(String valorDefecto) {
        if (valorDefecto == null) {
            valorDefecto = "";
        }
        this.valorDefecto = valorDefecto;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public int getDecimales() {
        return decimales;
    }

    public void setDecimales(int decimales) {
        this.decimales = decimales;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public String getTipoJava() {
        return tipoJava;
    }

    public void setTipoJava(String tipoJava) {
        this.tipoJava = tipoJava;
    }

    public List getListaCombo() {
        return listaCombo;
    }

    public void setListaCombo(List listaCombo) {
        this.listaCombo = listaCombo;
    }

    public List getListaRadio() {
        return listaRadio;
    }

    public void setListaRadio(List listaRadio) {
        this.listaRadio = listaRadio;
    }

    public boolean isRequerida() {
        return requerida;
    }

    public void setRequerida(boolean requerida) {
        this.requerida = requerida;
    }

    public String getVar() {
        return "opcion";
    }

    public boolean isPermitirNullCombo() {
        return permitirNullCombo;
    }

    public void setPermitirNullCombo(boolean permitirNullCombo) {
        this.permitirNullCombo = permitirNullCombo;
    }

    public void setCombo(String tabla, String campo_codigo, String campo_nombre, String condicion) {
        this.setControl("Combo");
        if (condicion != null && !condicion.isEmpty()) {
            condicion = " WHERE " + condicion;
            if (framework.tieneCampoEmpresa(tabla, campo_codigo)) {
                condicion = condicion + " AND id_empresa=" + framework.getVariable("empresa");
            }
        }
        //Agrego espacios a los campos nombres
        campo_nombre = campo_nombre.replace(",", " || ' ',");
        this.setSqlCombo("SELECT " + campo_codigo + "," + campo_nombre + " FROM " + tabla + " " + condicion + " ORDER BY " + campo_nombre);
        listaCombo = new ArrayList();
        listaCombo = tab_tabla.getConexion().consultar(getSqlCombo());
    }

    public void setCombo(List listaCombo) {
        this.setControl("Combo");
        this.listaCombo = new ArrayList();
        this.listaCombo = listaCombo;
    }

    public void setCombo(String sql) {
        //Considerar siempre La condicion id_empresa
        this.setControl("Combo");
        this.setSqlCombo(sql);
        listaCombo = new ArrayList();
        listaCombo = tab_tabla.getConexion().consultar(getSqlCombo());

    }

    public void actualizarCombo() {
        listaCombo = new ArrayList();
        listaCombo = framework.getConexion().consultar(getSqlCombo());
    }

    public String getSqlCombo() {
        if (sqlCombo != null) {
            return sqlCombo.toUpperCase();
        }
        return sqlCombo;
    }

    public void setSqlCombo(String sqlCombo) {
        this.sqlCombo = sqlCombo;
    }

    public int getNumeroColumnasCombo() {
        if (listaCombo != null && !listaCombo.isEmpty()) {
            Object[] fila = (Object[]) listaCombo.get(0);
            return fila.length;
        } else {
            if (getSqlCombo() != null) {
                String str_campos = getSqlCombo().substring(0, getSqlCombo().trim().toUpperCase().indexOf(" FROM"));
                return str_campos.split(",").length;
            }
            return 0;
        }
    }

    public Object[] getFilaCombo(Object value) {
        if (value != null) {
            for (Object listaCombo1 : listaCombo) {
                Object[] fila = (Object[]) listaCombo1;
                if (fila[0] != null) {
                    String f = fila[0] + "";
                    if (f.equals(value.toString())) {
                        return fila;
                    }
                }
            }
        }

        return null;
    }

    public Object[] getFilaAutocompletar(Object value) {
        if (value != null) {
            for (Object listaCombo1 : listaCombo) {
                Object[] fila = (Object[]) listaCombo1;
                if (fila[1] != null) {
                    String f = String.valueOf(fila[0]);
                    if (f.equals(value.toString())) {
                        return fila;
                    }
                }
            }
        }

        return null;
    }

    public void setRadio(List listaRadio, String valorDefecto) {
        this.setControl("Radio");
        this.valorDefecto = valorDefecto;
        this.listaRadio = new ArrayList();
        this.listaRadio = listaRadio;
    }

    public void setUpload() {
        this.setControl("Upload");
    }

    public void setUpload(String carpeta) {
        this.carpeta = carpeta;
        this.setControl("Upload");
    }

    public void setImagen(String alto, String ancho) {
        if (alto == null || alto.isEmpty()) {
            alto = "130";
        }
        if (ancho == null || ancho.isEmpty()) {
            ancho = "210";
        }
        this.anchoImagen = ancho;
        this.altoImagen = alto;
    }

    public void setImagen() {
        this.anchoImagen = "0";
        this.altoImagen = "0";
    }

    public String getAltoImagen() {
        return altoImagen;
    }

    public String getAnchoImagen() {
        return anchoImagen;
    }

    public boolean isImagen() {
        return altoImagen != null;
    }

    public void subirArchivo(FileUploadEvent event) {
        if (carpeta != null) {
            try {
                String str_nombre = framework.getVariable("id_usuario") + framework.getFechaActual().replace("-", "") + framework.getHoraActual().replace(":", "") + event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."), event.getFile().getFileName().length());
                str_nombre = str_nombre.toLowerCase();
                String str_path = framework.getPropiedad("rutaUpload") + "/" + carpeta;
                File path = null;
                File result = null;
                try {
                    path = new File(str_path);
                    path.mkdirs();//Creo el Directorio de respaldos de archivos
                    result = new File(str_path + "/" + str_nombre);
                } catch (Exception e) {
                }

                ///Para el .war 
                ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
                str_path = extContext.getRealPath("/upload/");
                File result1 = null;
                //try
                try {

                    path = new File(str_path + "/" + carpeta);
                    path.mkdirs();//Creo el Directorio
                    str_path = extContext.getRealPath("/upload/" + carpeta);
                    result1 = new File(str_path + "/" + str_nombre);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Tabla tab_formulario = framework.getTablaisFocus();
                    tab_formulario.setValor(getNombre(), "/upload/" + carpeta + "/" + str_nombre);
                    if (!tab_formulario.isFilaInsertada()) {
                        tab_formulario.modificar(tab_formulario.getFilaActual());
                    }
                } catch (Exception e) {
                }
                int BUFFER_SIZE = 6124;
                try {
                    if (result != null) {
                        FileOutputStream fileOutputStream = new FileOutputStream(result);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bulk;
                        InputStream inputStream = event.getFile().getInputstream();
                        while (true) {
                            bulk = inputStream.read(buffer);
                            if (bulk < 0) {
                                break;
                            }
                            fileOutputStream.write(buffer, 0, bulk);
                            fileOutputStream.flush();
                        }
                        fileOutputStream.close();
                        inputStream.close();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {
                    if (result1 != null) {
                        FileOutputStream fileOutputStream = new FileOutputStream(result1);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bulk;
                        InputStream inputStream = event.getFile().getInputstream();
                        while (true) {
                            bulk = inputStream.read(buffer);
                            if (bulk < 0) {
                                break;
                            }
                            fileOutputStream.write(buffer, 0, bulk);
                            fileOutputStream.flush();
                        }
                        fileOutputStream.close();
                        inputStream.close();
                    }
                } catch (Exception e) {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                //Funciona como stream
                UploadedFile file = event.getFile();
                UploadStreamedContent stream = new UploadStreamedContent(file.getInputstream(), file.getContentType(), file.getContents());
                try {
                    Tabla tab_formulario = framework.getTablaisFocus();
                    tab_formulario.setStreamedContent(getNombre(), stream);
                    if (!tab_formulario.isFilaInsertada()) {
                        tab_formulario.modificar(tab_formulario.getFilaActual());
                    }
                } catch (Exception e) {
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        try {
            framework.getSeleccionArchivo().cerrar();
        } catch (Exception e) {
        }
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public int getImagenAutocompletar() {
        return imagenAutocompletar;
    }

    public void setImagenAutocompletar(int imagenAutocompletar) {
        this.imagenAutocompletar = imagenAutocompletar;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setClave() {
        this.setControl("Clave");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public boolean isMayusculas() {
        return mayusculas;
    }

    public void setMayusculas(boolean mayusculas) {
        this.mayusculas = mayusculas;
    }

    public boolean isQuitarCaracteresEspeciales() {
        return quitarCaracteresEspeciales;
    }

    public void setQuitarCaracteresEspeciales(boolean quitarCaracteresEspeciales) {
        this.quitarCaracteresEspeciales = quitarCaracteresEspeciales;
    }

    public boolean isUnico() {
        return unico;
    }

    public void setUnico(boolean unico) {
        this.unico = unico;
    }

    public boolean isExterna() {
        return externa;
    }

    public void setExterna(boolean externa) {
        this.externa = externa;
    }

    public void setCheck() {
        this.control = "Check";
        if (valorDefecto == null) {
            //Para q por defecto en oracle se ponga false y no null
            this.setValorDefecto("false");
        }
    }

    public boolean isBuscarenCombo() {
        return buscarenCombo;
    }

    public void setBuscarenCombo(boolean buscarenCombo) {
        this.buscarenCombo = buscarenCombo;
    }

    public Double getTotal() {
        return total;
    }

    public String getTotalFormato() {
        if (total != null) {
            try {
                if (tipoJava.equals("java.lang.Integer") || tipoJava.equals("java.lang.Long")) {
                    return total.longValue() + "";
                } else {
                    return framework.getFormatoNumero(total);
                }
            } catch (Exception e) {
            }

        }

        return "";
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public boolean isSuma() {
        return suma;
    }

    public void setSuma(boolean suma) {
        this.suma = suma;
    }

    public String getEstiloColumna() {
        return estiloColumna;
    }

    public void setEstiloColumna(String estiloColumna) {
        this.estiloColumna = estiloColumna;
    }

    public String getFiltroModo() {
        return filtroModo;
    }

    public void setFiltroContenido() {
        this.filtroModo = "contains";
        this.filtro = true;
    }

    public void setFiltroExacto() {
        this.filtroModo = "exact";
        this.filtro = true;
    }

    public void setFiltroTermina() {
        this.filtroModo = "endsWith";
        this.filtro = true;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

}
