/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Columna;
import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.orderlist.OrderList;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class FormatoTabla extends Dialogo {
    
    private final OrderList ord_campos = new OrderList();
    private List campos = new ArrayList();
    private Tabla tab_tabla;
    private final Tabla tab_campos = new Tabla();
    private final Texto tex_nombre = new Texto();
    private final AreaTexto ate_comentario = new AreaTexto();
    private final Combo com_busca = new Combo();
    
    public FormatoTabla() {
        this.setId("fot_formato");
        this.setWidth("60%");
        this.setHeight("50%");
        this.setResizable(false);
        this.setHeader("Formato de la Tabla");
        Grid gri_detalle = new Grid();
        gri_detalle.setColumns(2);
        Etiqueta eti_columna = new Etiqueta();
        eti_columna.setValue("COLUMNAS :");
        gri_detalle.getChildren().add(eti_columna);
        com_busca.setId("com_busca_orden");
        gri_detalle.getChildren().add(com_busca);
        
        
        Etiqueta eti_nombre = new Etiqueta();
        eti_nombre.setValue("NOMBRE :");
        gri_detalle.getChildren().add(eti_nombre);
        tex_nombre.setSize(24);
        tex_nombre.setId("tex_nombre_orden");
        tex_nombre.setMetodoChangeRuta("mbe_index.fot_formato.cambiarNombre");
        gri_detalle.getChildren().add(tex_nombre);
        
        Etiqueta eti_comentario = new Etiqueta();
        eti_comentario.setValue("COMENTARIO :");
        gri_detalle.getChildren().add(eti_comentario);
        ate_comentario.setMetodoChangeRuta("mbe_index.fot_formato.cambiarComentario");
        ate_comentario.setStyle("width:91%;");
        ate_comentario.setRows(5);
        gri_detalle.getChildren().add(ate_comentario);
        
        Grid gri_cuerpo = new Grid();
        gri_cuerpo.setColumns(2);
        gri_cuerpo.setId("gri_fot_formato");
        gri_cuerpo.getChildren().add(ord_campos);
        gri_cuerpo.getChildren().add(gri_detalle);
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        ord_campos.setStyle("color:red");
        ord_campos.setId("ord_campos");
        ord_campos.setValueExpression("value", crearValueExpression("mbe_index.fot_formato.campos"));
        ord_campos.setVar("campo");
        ord_campos.setValueExpression("itemLabel", crearValueExpression("campo"));
        ord_campos.setValueExpression("itemValue", crearValueExpression("campo"));
        
        Ajax aja_change = new Ajax();
        aja_change.setUpdate("gri_fot_formato");
        aja_change.setGlobal(false);
        aja_change.setMetodoRuta("mbe_index.fot_formato.onSelect");
        com_busca.addClientBehavior("change", aja_change);
        getBot_aceptar().setMetodoRuta("mbe_index.fot_formato.aceptar");
        getBot_aceptar().setUpdate("fot_formato");
        
        gri_cuerpo.setMensajeInfo("Los cambios tendrán efecto la próxima vez que se abra la pantalla");
        this.setDialogo(gri_cuerpo);
    }
    
    public void aceptar() {        
        tab_campos.getConexion().getSqlPantalla().clear();
        getOrden();
        tab_tabla.establecerConfiguracion();
        for (Columna columna : tab_tabla.getColumnas()) {
            int numero = buscar_campo(columna.getNombre());
            if (numero == -1) {
                tab_campos.insertar();
                tab_campos.setValor("IDE_TABL", tab_tabla.ide_tabl);
                tab_campos.setValor("NOM_CAMP", columna.getNombre());
                tab_campos.setValor("NOM_VISUAL_CAMP", columna.getNombreVisual().replace("*", "").trim());
                tab_campos.setValor("ORDEN_CAMP", columna.getOrden() + "");
                tab_campos.setValor("VISIBLE_CAMP", columna.isVisible() + "");
                tab_campos.setValor("LECTURA_CAMP", columna.isLectura() + "");
                tab_campos.setValor("DEFECTO_CAMP", columna.getValorDefecto());
                tab_campos.setValor("MASCARA_CAMP", columna.getMascara());
                tab_campos.setValor("FILTRO_CAMP", columna.isFiltro() + "");
            } else {
                tab_campos.setValor(numero, "NOM_CAMP", columna.getNombre());
                tab_campos.setValor(numero, "NOM_VISUAL_CAMP", columna.getNombreVisual().replace("*", "").trim());
                tab_campos.setValor(numero, "ORDEN_CAMP", columna.getOrden() + "");
                tab_campos.setValor(numero, "VISIBLE_CAMP", columna.isVisible() + "");
                tab_campos.setValor(numero, "LECTURA_CAMP", columna.isLectura() + "");
                tab_campos.setValor(numero, "DEFECTO_CAMP", columna.getValorDefecto());
                tab_campos.setValor(numero, "MASCARA_CAMP", columna.getMascara());
                tab_campos.setValor(numero, "FILTRO_CAMP", columna.isFiltro() + "");
                tab_campos.modificar(numero);
            }
        }
        //if (tab_tabla.isTipoFormulario() && tab_tabla.getTotalFilas() == 0) {
        //  tab_tabla.limpiar();
        //   tab_tabla.insertar();
        // } else {
        tab_tabla.restablecer();
        // }
        tab_campos.guardar();
        tab_campos.getConexion().guardarPantalla();
        cerrar();
    }
    
    private int buscar_campo(String str_nombre) {

        //Busca si existe un campo en la tabla 2
        for (int i = 0; i < tab_campos.getFilas().size(); i++) {
            if (tab_campos.getValor(i, "NOM_CAMP").equalsIgnoreCase(str_nombre)) {
                return i;
            }
        }
        return -1;
    }
    
    private String getNombreColumna(String str_nombre_campo) {
        String nombre = "?";
        for (Columna columna : tab_tabla.getColumnas()) {
            if (columna.getNombreVisual().equalsIgnoreCase(str_nombre_campo)) {
                nombre = columna.getNombre();
                break;
            }
        }
        return nombre;
    }
    
    public void onSelect() {
        
        tex_nombre.setValue(tab_tabla.getColumna(com_busca.getValue() + "").getNombreVisual());
        ate_comentario.setValue(tab_tabla.getColumna(com_busca.getValue() + "").getComentario());
    }
    
    public void cambiarNombre() {
        tab_tabla.getColumna(com_busca.getValue() + "").setNombreVisual(tex_nombre.getValue() + "");
        dibujarLista();
        dibujarCombo();        
        framework.addUpdate("ord_campos,com_busca_orden,tex_nombre_orden");
    }
    
    public void cambiarComentario() {
        tab_tabla.getColumna(com_busca.getValue() + "").setComentario(ate_comentario.getValue() + "");
    }
    
    public void setFormatoTabla(Tabla tabla) {
        tab_tabla = tabla;
        if (tabla.getTabla().isEmpty()) {
            ord_campos.setDisabled(true);
        } else {
            ord_campos.setDisabled(false);
        }
        tab_campos.setTabla("SIS_CAMPO", "ide_camp", 0);
        tab_campos.setCondicion("ide_tabl =" + tab_tabla.ide_tabl);
        tab_campos.ejecutarSql();
        dibujarLista();
        dibujarCombo();
    }
    
    private void dibujarCombo() {
        com_busca.getChildren().clear();
        for (Columna columna : tab_tabla.getColumnas()) {
            if (columna.isVisible()) {
                ItemOpcion ito_campo = new ItemOpcion();
                if (com_busca.getValue() == null) {
                    com_busca.setValue(columna.getNombre());
                    tex_nombre.setValue(columna.getNombreVisual());
                    ate_comentario.setValue(columna.getComentario());
                }
                ito_campo.setItemValue(columna.getNombre());
                ito_campo.setItemLabel(columna.getNombreVisual());
                com_busca.getChildren().add(ito_campo);
            }
        }
        if (tab_tabla.getColumnas().length > 10) {
            com_busca.setFilter(true);
            com_busca.setFilterMatchMode("startsWith");
        } else {
            com_busca.setFilter(false);
            com_busca.setFilterMatchMode(null);
        }
    }
    
    private void dibujarLista() {
        campos.clear();
        for (Columna columna : tab_tabla.getColumnas()) {
            if (columna.isVisible()) {
                campos.add(columna.getNombreVisual());
            }
        }
    }
    
    public void getOrden() {
        List<String> lis = (List<String>) ord_campos.getValue();
        for (int i = 0; i < lis.size(); i++) {
            tab_tabla.getColumna(getNombreColumna(lis.get(i))).setOrden(i);
            
        }
    }
    
    private ValueExpression crearValueExpression(String valueExpression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
    }
    
    public List getCampos() {
        return campos;
    }
    
    public void setCampos(List campos) {
        this.campos = campos;
    }
}
