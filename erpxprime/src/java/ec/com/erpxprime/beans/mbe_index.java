/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.beans;

import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.AutoCompletar;
import ec.com.erpxprime.framework.componentes.BuscarTabla;
import ec.com.erpxprime.framework.componentes.Confirmar;
import ec.com.erpxprime.framework.componentes.Dialogo;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.ErrorSQL;
import ec.com.erpxprime.framework.componentes.Espacio;
import ec.com.erpxprime.framework.componentes.Etiqueta;
import ec.com.erpxprime.framework.componentes.FormatoTabla;
import ec.com.erpxprime.framework.componentes.Grid;
import ec.com.erpxprime.framework.componentes.Grupo;
import ec.com.erpxprime.framework.componentes.Imagen;
import ec.com.erpxprime.framework.componentes.ImportarTabla;
import ec.com.erpxprime.framework.componentes.ItemMenu;
import ec.com.erpxprime.framework.componentes.Link;
import ec.com.erpxprime.framework.componentes.Menu;
import ec.com.erpxprime.framework.componentes.MetodoRemoto;
import ec.com.erpxprime.framework.componentes.Notificacion;
import ec.com.erpxprime.framework.componentes.SeleccionArchivo;
import ec.com.erpxprime.framework.componentes.TerminalTabla;
import ec.com.erpxprime.framework.componentes.bootstrap.Alerta;
import ec.com.erpxprime.framework.componentes.bootstrap.CajaBootstrap;
import ec.com.erpxprime.framework.componentes.bootstrap.PanelBootstrap;
import ec.com.erpxprime.framework.componentes.bootstrap.RowBootstrap;
import ec.com.erpxprime.servicios.sistema.ServicioSistema;
import ec.com.erpxprime.sistema.aplicacion.Utilitario;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class mbe_index {

    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = Logger.getLogger(mbe_index.class.getName());
    private final Utilitario utilitario = new Utilitario();
    private HtmlForm formulario = new HtmlForm();
    private Grupo dibuja = new Grupo();
    private Object clase;
    private HtmlInputHidden ith_alto = new HtmlInputHidden(); //Alto disponible
    private HtmlInputHidden alto = new HtmlInputHidden(); //Alto Browser
    private HtmlInputHidden ancho = new HtmlInputHidden();//Ancho Browser
    private Grupo mensajes = new Grupo();
    private ErrorSQL error_sql = new ErrorSQL();
    private BuscarTabla bus_buscar;
    private TerminalTabla term_tabla;
    private String str_paquete;
    private String str_tipo;
    private String str_manual = "ayuda_vc.pdf";
    private String istr_titulo = "Inicio";
    private FormatoTabla fot_formato;
    private ImportarTabla imt_importar;
    private Dialogo dia_sucu_usuario;
    private SeleccionArchivo sar_upload;
    private String empresa = utilitario.getVariable("empresa");
    private AutoCompletar aut_pantalla = new AutoCompletar();
    public static StreamedContent logo;
    
    @EJB
    private ServicioSistema ser_sistema;
    
    @PostConstruct
    public void cargaSucursalesUsuario() {
        if (utilitario.getConexion() != null && utilitario.getVariable("nombre_usuario") != null) {
            seleccionarSucursal();
        }
    }
    
    /*
     * Constructor
     */
    public mbe_index() {
        
        if (utilitario.getConexion() != null && utilitario.getVariable("nombre_usuario") != null) {
            dibuja.setId("dibuja");
            dibuja.setStyleClass("ui-layout-unit-content ui-widget-content");
            dibuja.setTransient(true);
            formulario.setTransient(true);
            dibuja.setDibuja(true);
            Menu menu = new Menu();
            menu.dibujar();
            menu.setId("menu");
            formulario.getChildren().add(menu);
            formulario.getChildren().add(dibuja);
            ith_alto.setId("ith_alto");
            formulario.getChildren().add(ith_alto);
            alto.setId("alto");
            formulario.getChildren().add(alto);
            ancho.setId("ancho");
            formulario.getChildren().add(ancho);
            utilitario.crearVariable("ALTO_PANTALLA", "1000");

            mensajes.setId("mensajes");
            mensajes.setTransient(true);
            bus_buscar = new BuscarTabla();
            bus_buscar.setId("bus_buscar");
            mensajes.getChildren().add(bus_buscar);
            
            term_tabla=new TerminalTabla();            
            mensajes.getChildren().add(term_tabla);
            
            fot_formato = new FormatoTabla();
            mensajes.getChildren().add(fot_formato);
            
            sar_upload=new SeleccionArchivo();            
            mensajes.getChildren().add(sar_upload);

            imt_importar = new ImportarTabla();
            mensajes.getChildren().add(imt_importar);
            error_sql.setId("error_sql");
            mensajes.getChildren().add(error_sql);
            formulario.getChildren().add(mensajes);
            Notificacion not_notificacion = new Notificacion();
            formulario.getChildren().add(not_notificacion);
            
            Confirmar con_guarda = new Confirmar();
            con_guarda.setId("con_guarda");
            con_guarda.setWidgetVar("con_guarda");
            con_guarda.setMessage("Está seguro que desea guardar?");
            con_guarda.getBot_aceptar().setOncomplete("con_guarda.hide();");
            con_guarda.getBot_aceptar().setMetodo("guardar");
            con_guarda.getBot_cancelar().setOnclick("con_guarda.hide();");

            formulario.getChildren().add(con_guarda);

            Etiqueta eti_usuario = new Etiqueta();
            String lstr_usuario = utilitario.getVariable("nombre_usuario").toUpperCase();
            eti_usuario.setStyle("position:fixed;right:100px;top:10px;");
            eti_usuario.setValue(lstr_usuario);
            eti_usuario.setTitle("Usuario Login");
            formulario.getChildren().add(eti_usuario);
            
//            Link lin_salir = new Link();
//            lin_salir.agregarImagen("imagenes/im_salir_sistema.png", "32", "32");
//            lin_salir.setMetodoRuta("mbe_login.salir");
//            lin_salir.setTitle("Cerrar Sesión");
//            lin_salir.setStyle("position:fixed;right:2px;top:1px;");
//            formulario.getChildren().add(lin_salir);
            
//            empresa = utilitario.getVariable("empresa");
            
//            empresa = utilitario.getPropiedad("ide_empr");
            
            cargar_menu(empresa);
            
        }
        
        LOGGER.log(Level.INFO, "mbe_index cargado");
    }
    
    private void seleccionarSucursal() {
        dibuja.setStyle("width: 100%;overflow-x: hidden;overflow-y: auto;");
        dibuja.getChildren().clear();

        logo = ser_sistema.getLogoEmpresa();
        
        Alerta ale_inicio = new Alerta();
        ale_inicio.setAlertaCeleste("<strong>Bienvenido </strong> al Sistema Contable Financiero XPRIME v1.0 <span class='pull-right'> " + utilitario.getFechaLarga(utilitario.getFechaActual()) + " &nbsp;  </span>");
        dibuja.getChildren().add(ale_inicio);

        RowBootstrap row_cajas = new RowBootstrap();
        dibuja.getChildren().add(row_cajas);

        CajaBootstrap cb1 = new CajaBootstrap();
        cb1.setCajaBootstrap("USUARIO", utilitario.getVariable("nombre_usuario").toUpperCase());
        cb1.setIcono("fa fa-user", "bg-blue");
        row_cajas.getChildren().add(cb1);

        CajaBootstrap cb2 = new CajaBootstrap();
        cb2.setCajaBootstrap("PERFIL", utilitario.getVariable("nombre_perfil").toUpperCase());
        cb2.setIcono("fa fa-users", "bg-aqua");
        row_cajas.getChildren().add(cb2);

        CajaBootstrap cb3 = new CajaBootstrap();
        cb3.setCajaBootstrap("DIRECCIÓN IP", utilitario.getIp());
        cb3.setIcono("fa fa-laptop", "bg-green");
        row_cajas.getChildren().add(cb3);

        CajaBootstrap cb4 = new CajaBootstrap();
        String str_ultimo_acceso = utilitario.getFechaActual() + " </br> " + utilitario.getHoraActual();
        
        cb4.setCajaBootstrap("ÚLTIMO ACCESO", str_ultimo_acceso);
        cb4.setIcono("fa fa-calendar", "bg-yellow");
        row_cajas.getChildren().add(cb4);

        RowBootstrap row_util = new RowBootstrap();
        dibuja.getChildren().add(row_util);
        
//        ContenidoBootstrap cb_izquierda = new ContenidoBootstrap("col-md-6");
//        row_util.getChildren().add(cb_izquierda);

        PanelBootstrap pb_empresa = new PanelBootstrap();
        row_util.getChildren().add(pb_empresa);
        pb_empresa.setPanelVerde();
        pb_empresa.setTitulo(utilitario.getVariable("nombre_empresa"));
        pb_empresa.agregarComponenteContenido(new Etiqueta("<h3  style='font-weight: bold;text-align:center'>" + utilitario.getVariable("nombre_empresa") + "</h3> <p align='center'>"));
        Imagen ima_empresa = new Imagen();
        ima_empresa.setWidth("500");
        ima_empresa.setHeight("400");
        ima_empresa.setValueExpression("value", "mbe_index.logo");
        ima_empresa.setStyleClass("img-responsive");
        pb_empresa.agregarComponenteContenido(ima_empresa);
        pb_empresa.agregarComponenteContenido(new Etiqueta("</p>"));

        utilitario.addUpdate("dibuja");
    }
    
    private void cargar_menu(String ide_empresa) {
        dibuja.getChildren().clear();
        
        TablaGenerica tab_sucursal = utilitario.consultar("SELECT * FROM tbl_empresa WHERE id_empresa=" + ide_empresa);
        utilitario.crearVariable("empresa", tab_sucursal.getValor(0, "id_empresa"));
        
        Etiqueta eti_empresa = new Etiqueta();
        Etiqueta eti_representante = new Etiqueta();
        Etiqueta eti_direccion = new Etiqueta();
        Etiqueta eti_telefono = new Etiqueta();
        Imagen ima_empresa = new Imagen();
        
        eti_empresa.setValue(tab_sucursal.getValor(0, "nombre") + "<br/>");
        eti_representante.setValue(tab_sucursal.getValor(0, "representante") + "<br/>");
        eti_direccion.setValue(tab_sucursal.getValor(0, "direccion") + "<br/>");
        eti_telefono.setValue(tab_sucursal.getValor(0, "telefono") + "<br/>");
        ima_empresa.setValue(tab_sucursal.getValor(0, "logo_empr"));
        
//        pf_panel panel_empresa = new pf_panel();
//        panel_empresa.setStyle("width: 100%;font-size: 14px;text-align: center;margin-bottom:20px");
//        panel_empresa.setFooter(new Etiqueta("Message Plus Copyright 2014"));
        
        eti_empresa.setStyle("font-style: bold;border: none;text-shadow: 0px 2px 3px #ccc;background: none;text-transform: uppercase");
        
        Division pan_empresa = new Division();
        pan_empresa.setStyle("border:1px;");
        
        Grid gri_datos = new Grid();
        gri_datos.setStyle("width: 100%;font-size: 30px;text-align: center;font-weight: bold;");
        gri_datos.getChildren().add(new Espacio("100","100"));
        gri_datos.getChildren().add(eti_empresa);
        gri_datos.getChildren().add(ima_empresa);
        gri_datos.getChildren().add(eti_representante);
        gri_datos.getChildren().add(eti_direccion);
        gri_datos.getChildren().add(eti_telefono);
        
//        panel_empresa.getChildren().add(gri_datos);
        
        Grid gri_pie = new Grid();
        gri_pie.setStyle("width: 100%;font-size: 14px;text-align: center;");
        gri_pie.getChildren().add(new Etiqueta("Message Plus © Copyright 2015"));
        
        pan_empresa.dividir2(gri_datos, gri_pie, "55%", "H");
        
        pan_empresa.getDivision1().setResizable(false);
        pan_empresa.getDivision2().setResizable(false);
        
        dibuja.getChildren().add(pan_empresa);
        utilitario.addUpdate("dibuja");
    }
    
    public void cargar(ActionEvent evt) {
        
        
        dibuja.setStyle("");
        if (evt.getComponent().getRendererType() == null) { //ItemMenu
            utilitario.crearVariable("id_opcion", ((ItemMenu) evt.getComponent()).getCodigo());
            istr_titulo = ((ItemMenu) evt.getComponent()).getValue() + "";
        } else {
            utilitario.crearVariable("id_opcion", ((Link) evt.getComponent()).getCodigo());
            istr_titulo = ((Link) evt.getComponent()).getValue() + "";
        }
        if (utilitario.getVariable("id_opcion") == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una Pantalla", "");
            return;
        }
        
        utilitario.crearVariable("ALTO_PANTALLA", ith_alto.getValue() + "");  //Alto disponible
        utilitario.crearVariable("ALTO", alto.getValue() + "");  //Alto browser
        utilitario.crearVariable("ANCHO", ancho.getValue() + ""); //Ancho browser    
        utilitario.crearVariable("OPCION", istr_titulo); //Opcion
        buscarOpcion();
        
        mensajes.getChildren().clear();
        mensajes.getChildren().add(error_sql);
        mensajes.getChildren().add(bus_buscar);
        mensajes.getChildren().add(term_tabla);
        mensajes.getChildren().add(fot_formato);
        mensajes.getChildren().add(imt_importar);
        mensajes.getChildren().add(sar_upload);
        dibuja.getChildren().clear();
        utilitario.getConexion().setSqlPantalla(new ArrayList<String>());
        clase = null;
        
        try {
            try {
                Runtime basurero = Runtime.getRuntime();
                basurero.gc();
            } catch (Exception e) {
            }
            Class pantalla = Class.forName(str_paquete + "." + str_tipo);
            clase = pantalla.newInstance();
//            utilitario.buscarPermisosObjetos();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            utilitario.crearError(ex.getMessage(), "pre_index en el método cargar() ", ex);
        }
        
//        mensajes.getChildren().clear();
//        mensajes.getChildren().add(error_sql);
//        mensajes.getChildren().add(bus_buscar);
//        mensajes.getChildren().add(term_tabla);
//        mensajes.getChildren().add(fot_formato);
//        mensajes.getChildren().add(imt_importar);
//        mensajes.getChildren().add(sar_upload);
//        dibuja.getChildren().clear();
//        utilitario.getConexion().setSqlPantalla(new ArrayList<String>());
//        clase = utilitario.cargarPantalla(str_paquete, str_tipo);

        //Tacla Abajo y Arriba
        MetodoRemoto mer_abajo = new MetodoRemoto();
        mer_abajo.setName("teclaAbajo");
        mer_abajo.setMetodo("siguiente");
        dibuja.getChildren().add(mer_abajo);

        MetodoRemoto mer_arriba = new MetodoRemoto();
        mer_arriba.setName("teclaArriba");
        mer_arriba.setMetodo("atras");
        dibuja.getChildren().add(mer_arriba);

    }
    
    private void buscarOpcion() {
        TablaGenerica tab_opcion = utilitario.consultar("SELECT paquete,enlace,auditada "
                + "FROM tbl_opcion "
                + "WHERE id_opcion=" + utilitario.getVariable("id_opcion") + " AND "
                + "id_empresa = " + empresa);
        if (tab_opcion.getTotalFilas() > 0) {
            str_paquete = tab_opcion.getValor(0, "paquete");
            str_tipo = tab_opcion.getValor(0, "enlace");
            if (tab_opcion.getValor(0, "auditada") != null) {
                utilitario.crearVariable("auditada", tab_opcion.getValor(0, "auditada"));
            } else {
                utilitario.crearVariable("auditada", "false");
            }
            str_manual = "ayuda_vc.pdf";
        }
    }

    public void cerrarDialogo(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            if (com_padre.getRendererType() != null && com_padre.getRendererType().equals("org.primefaces.component.DialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((Dialogo) com_padre).cerrar();
        }
    }

    public void cerrarConfirmar(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            String str_render = com_padre.getRendererType();
            if (str_render != null && str_render.equals("org.primefaces.component.ConfirmDialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((Confirmar) com_padre).cerrar();
        }
    }
    
    public void cargar_inicio() {
        utilitario.crearVariable("ALTO_PANTALLA", ith_alto.getValue() + ""); //Alto disponible
        utilitario.crearVariable("ALTO", alto.getValue() + "");  //Alto browser
        utilitario.crearVariable("ANCHO", ancho.getValue() + ""); //Ancho browser        
        utilitario.crearVariable("id_opcion", "-1");
        istr_titulo = "Inicio";
        mensajes.getChildren().clear();
        dibuja.getChildren().clear();
        clase = utilitario.cargarPantalla("ec.com.erpxprime.sistema", "sis_admin_usuario");
        BlockUI bloc = new BlockUI();
        bloc.setBlock("bar_botones");
        bloc.setWidgetVar("bui");
        dibuja.getChildren().add(bloc);
    }
    
    public void cargar_correo() {
        utilitario.crearVariable("ALTO_PANTALLA", ith_alto.getValue() + ""); //Alto disponible
        utilitario.crearVariable("ALTO", alto.getValue() + "");  //Alto browser
        utilitario.crearVariable("ANCHO", ancho.getValue() + ""); //Ancho browser        
        utilitario.crearVariable("id_opcion", "-1");
        istr_titulo = "Soporte Mail";
        mensajes.getChildren().clear();
        dibuja.getChildren().clear();
        clase = utilitario.cargarPantalla("ec.mplus.mail", "mai_correo");
    }
    
    public void cerrarSql() {
        error_sql.setVisible(false);
        error_sql.limpiar();
    }
    
    public void ayuda() {
        //Cargar la ayuda de la pantalla     
        if (str_manual != null) {
//            utilitario.ejecutarJavaScript("window.open('" + utilitario.getURL() + "/manuales/" + str_paquete + "/" + str_manual + "','nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=800,height=600')");
            utilitario.ejecutarJavaScript("window.open('" + utilitario.getURL() + "/manuales/" + str_manual + "','nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=800,height=600')");
        }
    }
    
    public HtmlForm getFormulario() {
        return formulario;
    }

    public void setFormulario(HtmlForm formulario) {
        this.formulario = formulario;
    }

    public Object getClase() {
        return clase;
    }

    public void setClase(Object clase) {
        this.clase = clase;
    }

    public String getIstr_titulo() {
        return istr_titulo;
    }

    public void setIstr_titulo(String istr_titulo) {
        this.istr_titulo = istr_titulo;
    }

    public ImportarTabla getImt_importar() {
        return imt_importar;
    }

    public void setImt_importar(ImportarTabla imt_importar) {
        this.imt_importar = imt_importar;
    }

    public FormatoTabla getFot_formato() {
        return fot_formato;
    }

    public void setFot_formato(FormatoTabla fot_formato) {
        this.fot_formato = fot_formato;
    }

    public Dialogo getDia_sucu_usuario() {
        return dia_sucu_usuario;
    }

    public void setDia_sucu_usuario(Dialogo dia_sucu_usuario) {
        this.dia_sucu_usuario = dia_sucu_usuario;
    }

    public TerminalTabla getTerm_tabla() {
        return term_tabla;
    }

    public void setTerm_tabla(TerminalTabla term_tabla) {
        this.term_tabla = term_tabla;
    }     

    public SeleccionArchivo getSar_upload() {
        return sar_upload;
    }

    public void setSar_upload(SeleccionArchivo sar_upload) {
        this.sar_upload = sar_upload;
    }

    public HtmlInputHidden getAlto() {
        return alto;
    }

    public void setAlto(HtmlInputHidden alto) {
        this.alto = alto;
    }

    public HtmlInputHidden getAncho() {
        return ancho;
    }

    public void setAncho(HtmlInputHidden ancho) {
        this.ancho = ancho;
    }

    public Grupo getDibuja() {
        return dibuja;
    }

    public void setDibuja(Grupo dibuja) {
        this.dibuja = dibuja;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public ErrorSQL getError_sql() {
        return error_sql;
    }

    public void setError_sql(ErrorSQL error_sql) {
        this.error_sql = error_sql;
    }

    public HtmlInputHidden getIth_alto() {
        return ith_alto;
    }

    public void setIth_alto(HtmlInputHidden ith_alto) {
        this.ith_alto = ith_alto;
    }

    public Grupo getMensajes() {
        return mensajes;
    }

    public void setMensajes(Grupo mensajes) {
        this.mensajes = mensajes;
    }

    public String getStr_manual() {
        return str_manual;
    }

    public void setStr_manual(String str_manual) {
        this.str_manual = str_manual;
    }

    public String getStr_paquete() {
        return str_paquete;
    }

    public void setStr_paquete(String str_paquete) {
        this.str_paquete = str_paquete;
    }

    public String getStr_tipo() {
        return str_tipo;
    }

    public void setStr_tipo(String str_tipo) {
        this.str_tipo = str_tipo;
    }
    
    public StreamedContent getLogo() {
        return logo;
    }

    public void setLogo(StreamedContent logo) {
        this.logo = logo;
    }

    public BuscarTabla getBus_buscar() {
        return bus_buscar;
    }

    public void setBus_buscar(BuscarTabla bus_buscar) {
        this.bus_buscar = bus_buscar;
    }

    public AutoCompletar getAut_pantalla() {
        return aut_pantalla;
    }

    public void setAut_pantalla(AutoCompletar aut_pantalla) {
        this.aut_pantalla = aut_pantalla;
    }
    
}
