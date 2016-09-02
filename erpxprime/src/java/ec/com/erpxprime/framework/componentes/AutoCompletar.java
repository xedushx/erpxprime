/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.framework.convertidores.ConvertidorAutoCompletar;
import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.column.Column;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class AutoCompletar extends AutoComplete {

    private List lista;
    private String ruta = "mbe_index.clase";
    private Conexion conexion;
    private int imagenAutocompletar = 0;
    private String sql = "";

    private boolean autocompletarContenido = false;

    public AutoCompletar() {
        this.setForceSelection(true);
        this.setGlobal(false);
        this.setMaxResults(10);
    }

    public void setAutoCompletar(List lista) {
        this.lista = lista;
        formarAutocompletar();
    }

    public void setMetodoChange(String metodo) {

        Ajax aja_change = new Ajax();
        aja_change.setMetodo(metodo);
        aja_change.setGlobal(false);
        this.addClientBehavior("itemSelect", aja_change);
    }

    public void setMetodoChangeRuta(String metodo) {
        Ajax aja_change = new Ajax();
        aja_change.setMetodoRuta(metodo);
        this.addClientBehavior("itemSelect", aja_change);
    }

    public void limpiar() {
        this.setValue(null);
        Framework framework = new Framework();
        framework.addUpdate(this.getId());

    }

    public boolean isAutocompletarContenido() {
        return autocompletarContenido;
    }

    public void setAutocompletarContenido() {
        this.autocompletarContenido = true;
    }

    public void actualizar() {
        lista = new ArrayList();
        this.getChildren().clear();
        lista = conexion.consultar(sql);
        formarAutocompletar();
    }

    public void setAutoCompletar(String sql) {
//        this.setGlobal(true);
        this.sql = sql;
        Framework framework = new Framework();
        if (conexion == null) {
            conexion = framework.getConexion();
        }
        lista = conexion.consultar(sql);
        formarAutocompletar();
    }

    public void onSelect(SelectEvent evt) {
        this.setValue(evt.getObject());
    }

    public void onUnselect(UnselectEvent evt) {
        this.setValue(evt.getObject());
    }

    private void formarAutocompletar() {
        this.getChildren().clear();
        int int_columnas = 0;
        if (lista != null && !lista.isEmpty()) {
            Object[] fila = (Object[]) lista.get(0);
            int_columnas = fila.length;
        } else {
            int_columnas = 0;
        }
        this.setMetodoCompletarRuta(ruta + "." + this.getId() + ".autocompletar");
        this.setVar("auto");
        this.setConverter(new ConvertidorAutoCompletar());
        if (int_columnas > 2) {
            this.setValueExpression("itemValue", crearValueExpression("auto"));
            String str = "";
            int size = 27;
            for (int j = 1; j < int_columnas; j++) {

                Column col = new Column();
                if (getImagenAutocompletar() != j) {
                    if (j > 1) {
                        str += "  ";
                    }
                    str += "#{" + "auto[" + j + "]}";
                    Etiqueta eti = new Etiqueta();
                    eti.setValueExpression("value", "auto[" + j + "]");
                    col.getChildren().add(eti);
                    this.getChildren().add(col);
                } else {
                    Imagen eti = new Imagen();
                    eti.setValueExpression("value", "auto[" + j + "]");
                    eti.setWidth("96");
                    eti.setHeight("96");
                    this.setMaxResults(5);
                    col.getChildren().add(eti);
                    this.getChildren().add(col);
                }
                size += 10;
            }
            this.setSize(size);
            this.setValueExpression("itemLabel", crearValueExpression2(str));
        } else {
            this.setSize(45);
            this.setValueExpression("itemValue", crearValueExpression("auto"));
            this.setValueExpression("itemLabel", crearValueExpression("auto[1]"));
        }
    }

    public void modificar(AjaxBehaviorEvent evt) {
        this.setValue(null);
    }

    public List autocompletar(String query) {
        List suggestions = new ArrayList();
        //suggestions.add(null);
        for (Object lista1 : lista) {
            Object[] f = (Object[]) lista1;
            for (int j = 1; j < f.length; j++) {
                if (f[j] != null) {
                    String fl = f[j] + "";
                    if (autocompletarContenido) {
                        if (fl.toUpperCase().contains(query.trim().toUpperCase())) {
                            suggestions.add(f);
                            break;
                        }
                    } else {
                        if (fl.toUpperCase().startsWith(query.trim().toUpperCase())) {
                            suggestions.add(f);
                            break;
                        }
                    }

                }
            }
            if (suggestions.size() > 10) {
                break;
            }
        }
        return suggestions;
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    public void setMetodoCompletarRuta(String metodo) {
        MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + metodo + "}", List.class, new Class[]{String.class});
        this.setCompleteMethod(methodExpression);
    }

    public void setMetodoCompletar(String metodo) {
        MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{mbe_index.clase." + metodo + "}", List.class, new Class[]{String.class});
        this.setCompleteMethod(methodExpression);
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    private ValueExpression crearValueExpression2(String valueExpression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), valueExpression, Object.class);
    }

    public String getValorArreglo(int columna) {
        String valor = null;
        try {
            valor = ((Object[]) getValue())[columna] + "";
        } catch (Exception e) {
        }
        return valor;
    }

    public String getValor() {
        String valor = null;
        try {
            valor = ((Object[]) getValue())[0] + "";
        } catch (Exception e) {
        }
        return valor;
    }

    public String getNombre() {
        String valor = null;
        try {
            valor = ((Object[]) getValue())[1] + "";
        } catch (Exception e) {
        }
        return valor;
    }

    public int getImagenAutocompletar() {
        return imagenAutocompletar;
    }

    public void setImagenAutocompletar(int imagenAutocompletar) {
        this.imagenAutocompletar = imagenAutocompletar;
    }

    public void setValor(String valor) {
        this.setValue(getObjetoAutocompletar(valor));
    }

    private Object getObjetoAutocompletar(String valor) {
        for (Object lista1 : lista) {
            Object[] f = (Object[]) lista1;
            if (f[0].toString().equals(valor)) {
                return f;
            }
        }
        return null;
    }

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setPlaceHolder(String placeholder) {
        this.getPassThroughAttributes().put("placeholder", placeholder);
    }

}
