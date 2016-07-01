/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.beans;

import ec.xprime.componentes.*;
import ec.xprime.persistencia.cla_conexion;
import ec.xprime.sistema.sis_soporte;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.component.blockui.BlockUI;
import org.primefaces.component.panel.Panel;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class mbe_login implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = Logger.getLogger(mbe_login.class.getName());
    private HtmlForm formulario = new HtmlForm();
    private pf_entrada_texto tex_usuario = new pf_entrada_texto();
    private pf_clave pas_clave = new pf_clave();
    private pf_error_sql error_sql = new pf_error_sql();
    private HtmlInputHidden alto = new HtmlInputHidden(); //Alto Browser
    private HtmlInputHidden ancho = new HtmlInputHidden();//Ancho Browser
    private List lis_detalle_modulos = new ArrayList();

    /**
     * Constructor
     */
    public mbe_login() {
        crearDetalleModulos();
        formulario.setTransient(true);
        pf_grid gri_matriz = new pf_grid();
        gri_matriz.setColumns(2);
        gri_matriz.setWidth("100%");
        Panel pan_panel = new Panel();
        pan_panel.setId("pan_panel");
        pan_panel.setStyle("width: 350px;");
        pan_panel.setHeader("Ingresar al Sistema");

        tex_usuario.setId("tex_usuario");
        tex_usuario.setRequired(true);
        tex_usuario.setRequiredMessage("Debe ingresar el usuario");
        tex_usuario.setStyle("width: 99%;");
        gri_matriz.getChildren().add(new pf_etiqueta("USUARIO : "));
        gri_matriz.getChildren().add(tex_usuario);
        gri_matriz.getChildren().add(new pf_etiqueta("CLAVE :"));
        pas_clave.setFeedback(false);
        pas_clave.setRequired(true);
        pas_clave.setRequiredMessage("Debe ingresar la clave");
        pas_clave.setStyle("width: 99%;");
        gri_matriz.getChildren().add(pas_clave);
        pf_boton bot_login = new pf_boton();
        bot_login.setId("bot_login");
        bot_login.setValue("Aceptar");
        bot_login.setIcon("ui-icon-locked");
        bot_login.setMetodoRuta("mbe_login.ingresar");
        bot_login.setOnclick("var na = $(window).height();"
                + " document.getElementById('formulario:alto').value=na; "
                + " var ancho = $(window).width();"
                + " document.getElementById('formulario:ancho').value=ancho;");
        gri_matriz.setFooter(bot_login);
        pf_foco foc_foco = new pf_foco();
        foc_foco.setFor("tex_usuario");
        formulario.getChildren().add(foc_foco);
        pf_grid gri_login = new pf_grid();
        gri_login.setColumns(2);
        pf_imagen ima_llave = new pf_imagen();
        ima_llave.setValue("imagenes/im_llave.png");
        gri_login.setWidth("100%");
        gri_login.getChildren().add(ima_llave);
        gri_login.getChildren().add(gri_matriz);
        pan_panel.getChildren().add(gri_login);
        pan_panel.setTransient(true);
        BlockUI blo_panel = new BlockUI();
        blo_panel.setBlock("pan_panel");
        blo_panel.setTrigger("bot_login");
        formulario.getChildren().add(blo_panel);
        error_sql.setId("error_sql");
        error_sql.setMetodoAceptar("mbe_login.cerrarSql");
        formulario.getChildren().add(error_sql);
        formulario.getChildren().add(pan_panel);
        alto.setId("alto");
        formulario.getChildren().add(alto);
        ancho.setId("ancho");
        formulario.getChildren().add(ancho);
        
    }

    public void ingresar() {
        cla_conexion conexion = sis_soporte.obtener_instancia_soporte().obtener_conexion();
        if (conexion == null) {
            conexion = new cla_conexion();
            String str_recursojdbc = sis_soporte.obtener_instancia_soporte().getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
            //sis_soporte.obtener_instancia_soporte().crear_variable("empresa", sis_soporte.obtener_instancia_soporte().getPropiedad("ide_empr"));
        }
        
        String lstr_usuario = tex_usuario.getValue().toString().trim();
        String lstr_clave = pas_clave.getValue().toString().trim();
        String lstr_empresa = sis_soporte.obtener_instancia_soporte().getPropiedad("ide_empr");
        
        if (lstr_usuario.isEmpty()){
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Ingrese Nombre de Usuario", "");
        }else if (lstr_clave.isEmpty()){
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Ingrese Clave", "");
        }else if (lstr_empresa.isEmpty() || lstr_empresa.equalsIgnoreCase("null")) {
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Seleccione la Empresa", "");
        } else {
            List lis_consulta = conexion.consultar("SELECT usu.id_usuario,usu.id_perfil,usu.tema,emp.id_empresa "
                    + "FROM tbl_usuario usu, tbl_empresa emp "
                    + "WHERE usu.nombre='" + lstr_usuario + "' AND "
                    + "usu.clave=MD5('" + lstr_clave + "') AND "
                    + "emp.id_empresa = usu.id_empresa AND "
                    + "usu.estado_sesion ='true'");
            if (lis_consulta.size() > 0) {
                Object fila[] = (Object[]) lis_consulta.get(0);
                int lint_usuario = (Integer) fila[0];
                int lint_perfil = (Integer) fila[1];
                String lstr_tema = (String) fila[2];
                lstr_empresa = String.valueOf((Integer) fila[3]);
                
                sis_soporte.obtener_instancia_soporte().crear_variable("id_usuario", lint_usuario + "");
                sis_soporte.obtener_instancia_soporte().crear_variable("id_perfil", lint_perfil + "");
                sis_soporte.obtener_instancia_soporte().crear_variable("tema", lstr_tema);
                sis_soporte.obtener_instancia_soporte().crear_variable("nombre_usuario", lstr_usuario + "");
                sis_soporte.obtener_instancia_soporte().crear_variable("empresa", lstr_empresa);
                
                try {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("mbe_index");
                    FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeError("El nombre del usuario o la clave son incorrectos", "");
                sis_soporte.obtener_instancia_soporte().addUpdate("pan_panel");
            }
        }
        
    }
    
    public void caducoSession() {
        try {
            if (sis_soporte.obtener_instancia_soporte().obtener_conexion() != null) {               
                sis_soporte.obtener_instancia_soporte().obtener_conexion().desconectar();
            }
            sis_soporte.obtener_instancia_soporte().cerrarSesion();
        } catch (Exception ex) {
        }
    }

    public void salir() {
        try {
            if (sis_soporte.obtener_instancia_soporte().obtener_conexion() != null) {
                sis_soporte.obtener_instancia_soporte().obtener_conexion().desconectar();
            }
            sis_soporte.obtener_instancia_soporte().cerrarSesion();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
//            sis_soporte.obtener_instancia_soporte().ejecutarJavaScript("location.href='about:blank';window.close();");
        } catch (Exception ex) {
        }
    }

    public String getTema() {
        String tema = sis_soporte.obtener_instancia_soporte().obtener_variable("tema");
        if (tema == null || tema.isEmpty() || tema.equals("null")) {
            if (sis_soporte.obtener_instancia_soporte().getPropiedad("temaInicial") != null) {
                return sis_soporte.obtener_instancia_soporte().getPropiedad("temaInicial");
            }
            return "sam";
        } else {
            return tema;
        }
    }

    private void crearDetalleModulos() {
        String obj1[] = {"Contabilidad", "imagenes/contabilidad.png", "Permite el registro y control de todas las transacciones registradas en el sistema financiero."};
        String obj2[] = {"Inventarios", "imagenes/inventario.png", "Permite el control de cada una de las transacciones que se realicen con los artículos que posea la institución."};
        String obj3[] = {"Facturación", "imagenes/facturacion.png", "Permite la gestión de la emisión de facturas realizadas de bienes o  servicios que realice la institución, proporcionando un óptimo control."};
        String obj4[] = {"Cuentas por Pagar", "imagenes/cxp.gif", "Permite conocer todos los valores por pagar que se adeudan a cada uno de los proveedores, llevando un óptimo control sobre los pagos que se deben realizar."};
        String obj5[] = {"Cuentas por Cobrar", "imagenes/cxc.gif", "Permite gestionar y controlar los valores por cobrar de cada uno de los clientes."};
        String obj6[] = {"Sistema", "imagenes/sistema.png", "Basado en un entorno Web que permite el manejo de permisos, configuraciones, además de ser escalable, parametrizable, integrado con todos los módulos que conforman el sistema"};
        String obj7[] = {"SRI", "imagenes/sri_menu.png", "Permite generar los formularios y anexos según la regularización que mantenga  el Servicio de Rentas Internas, generando archivos XML para enviar a la institución tributaria."};
        lis_detalle_modulos.add(obj1);
        lis_detalle_modulos.add(obj2);
        lis_detalle_modulos.add(obj3);
        lis_detalle_modulos.add(obj4);
        lis_detalle_modulos.add(obj5);
        lis_detalle_modulos.add(obj6);
        lis_detalle_modulos.add(obj7);
    }
    
    public void cerrarConfirmar(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            String str_tipo = com_padre.getRendererType();
            if (str_tipo != null && str_tipo.equals("org.primefaces.component.ConfirmDialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((pf_confirmar) com_padre).setVisible(false);
        }
    }
    
    public List getLis_detalle_modulos() {
        return lis_detalle_modulos;
    }

    public void setLis_detalle_modulos(List lis_detalle_modulos) {
        this.lis_detalle_modulos = lis_detalle_modulos;
    }
    
    public pf_error_sql getError_sql() {
        return error_sql;
    }

    public void cerrarSql() {
        error_sql.setVisible(false);
    }

    public void setError_sql(pf_error_sql error_sql) {
        this.error_sql = error_sql;
    }

    public HtmlForm getFormulario() {
        return formulario;
    }

    public void setFormulario(HtmlForm formulario) {
        this.formulario = formulario;
    }
    
}