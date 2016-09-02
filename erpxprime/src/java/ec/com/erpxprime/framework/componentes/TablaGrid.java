/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import com.sun.faces.taglib.jsf_core.SetPropertyActionListenerImpl;
import ec.com.erpxprime.framework.aplicacion.Framework;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import org.primefaces.component.datagrid.DataGrid;
import ec.com.erpxprime.framework.aplicacion.Fila;

/**
 *
 * @author xedushx
 */
public class TablaGrid extends DataGrid {

    private Tabla tabla;
    private String ruta = "mbe_index.clase";
    private Panel panel = new Panel();
    private String columnas = "";
    private boolean mostrarTitulos = false;
    private boolean tipoSeleccion = false;
    private Fila seleccionada;
    private final Grid gri_detalle = new Grid();

    public TablaGrid() {
        this.setPaginatorTemplate("{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink} {RowsPerPageDropdown}");
        this.setColumns(5);
        this.setRows(20);
        this.setRowsPerPageTemplate("25,50,100");
        panel.setStyle("text-align: center;");
        this.setPaginator(true);
        this.setPaginatorAlwaysVisible(false);
        gri_detalle.setWidth("100%");
        gri_detalle.setStyle("height:100%;display: block;");
        panel.getChildren().add(gri_detalle);
        this.getChildren().add(panel);
    }

    public void setTablaGrid(Tabla tabla) {
        this.tabla = tabla;
        this.setVar("filagrid");
        this.setValueExpression("value", crearValueExpression(ruta + "." + this.getId() + ".tabla.filas"));
    }

    public void setImagen(String campo, String alto, String ancho, String tipo) {
        Imagen ima_imagen = new Imagen();
        ima_imagen.setValueExpression("value", "filagrid.campos[" + tabla.getNumeroColumna(campo) + "]");
        ima_imagen.setWidth(ancho);
        ima_imagen.setHeight(alto);
        if (tipo.equalsIgnoreCase("H")) {
            gri_detalle.setColumns(2);
            gri_detalle.getChildren().add(ima_imagen);
        } else {
            gri_detalle.setHeader(ima_imagen);
        }
    }

    public void setTituloPanel(String campo) {
        panel.setValueExpression("header", "filagrid.campos[" + tabla.getNumeroColumna(campo) + "]");
    }

    public void setMostrarColumnas(String columnas, boolean titulos) {
        this.columnas = columnas;
        this.mostrarTitulos = titulos;
    }

    public void dibujar() {
        Grid gri = new Grid();
        if (mostrarTitulos) {
            Framework framework = new Framework();
            framework.buscarNombresVisuales(tabla);
            gri.setColumns(2);
        }
        String col[] = columnas.split(",");
        for (String col1 : col) {
            if (mostrarTitulos) {
                gri.getChildren().add(new Etiqueta(tabla.getColumna(col1).getNombreVisual()));
            }
            Etiqueta eti_valor = new Etiqueta();
            eti_valor.setValueExpression("value", "filagrid.campos[" + tabla.getNumeroColumna(col1) + "]");
            gri.getChildren().add(eti_valor);
        }
        gri_detalle.getChildren().add(gri);
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    public Tabla getTabla() {
        return tabla;
    }

    public void setTabla(Tabla tabla) {
        this.tabla = tabla;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public boolean isTipoSeleccion() {
        return tipoSeleccion;
    }

    public Fila getSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(Fila seleccionada) {
        this.seleccionada = seleccionada;
    }

    public void setSeleccion(String metodo) {
        this.tipoSeleccion = true;
        ActionListener handler = new SetPropertyActionListenerImpl(crearValueExpression(ruta + "." + this.getId() + ".seleccionada"), crearValueExpression("filagrid"));
        Link link = new Link();
        link.setMetodo(metodo);
        link.agregarImagen("imagenes/im_ver.png", "", "");
        link.addActionListener(handler);
        panel.setFooter(link);
    }

    public String getValorSeleccionado() {
        if (seleccionada != null) {
            return seleccionada.getRowKey();
        }
        return null;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
