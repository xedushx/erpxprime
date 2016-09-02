/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.persistencia.Conexion;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.primefaces.component.tree.Tree;
import org.primefaces.component.tree.UITreeNode;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author xedushx
 */
public class Arbol extends Tree {

    private String ruta = "mbe_index.clase";
    private String tabla = "";
    private String campoPrimaria = "";
    private String campoNombre = "";
    private String campoPadre = "";
    private String campoOrden = "";
    private String condicion = "";
    private String condicionEmpresa = "";
    private String sql = "";
    private Conexion conexion;
    private final Framework framework = new Framework();
    private TreeNode nodos = new DefaultTreeNode(null, null);
    private TreeNode raiz;
    private String metodo = "";
    private TreeNode nodoSeleccionado;
    private boolean tipoSeleccion = false;
    private TreeNode[] seleccionados;
    private boolean optimiza = true;

    public Arbol() {
        this.setDynamic(true);
        this.setCache(true);
        this.setAnimate(true);
        this.setStyle("width:99%;height:99.6%;overflow: hidden;display: block;");
    }

    public void setArbol(String tabla, String campoPrimaria, String campoNombre, String campoPadre) {
        if (conexion == null) {
            conexion = framework.getConexion();
        }

        this.tabla = tabla;
        this.campoPrimaria = campoPrimaria;
        this.campoNombre = campoNombre;
        this.campoPadre = campoPadre;
        if (campoOrden.isEmpty()) {
            this.campoOrden = campoNombre;
        }
        if (condicionEmpresa.isEmpty() && framework.tieneCampoEmpresa(tabla, campoPrimaria)) {
            condicionEmpresa = " AND id_empresa=" + framework.getVariable("empresa") + " ";
        }
        String titulo = framework.getVariable("OPCION");
        if (titulo == null) {
            titulo = "Root";
        }
        titulo = titulo.toUpperCase();
        Object[] root = {
            null, titulo
        };
        raiz = new DefaultTreeNode(root, nodos);
        raiz.setExpanded(true);
    }

    public void setTitulo(String titulo) {
        titulo = titulo.toUpperCase();
        Object[] root = {
            null, titulo
        };
        raiz = new DefaultTreeNode(root, nodos);
    }

    public String getCampoOrden() {
        return campoOrden;
    }

    public void setCampoOrden(String campoOrden) {
        this.campoOrden = campoOrden;
    }

    public void onCommitInsertar(String campoPrimaria, String campoNombre) {
        if (tipoSeleccion == false) {
            if (nodoSeleccionado == null) {
                nodoSeleccionado = raiz;
            }
            Object[] fila = {campoPrimaria, campoNombre};
            TreeNode hijo = new DefaultTreeNode("document", fila, nodoSeleccionado);
        }
    }

    public void onCommitModificar(String campoPrimaria, String campoNombre) {
        if (tipoSeleccion == false) {
            if (nodoSeleccionado == null) {
                nodoSeleccionado = raiz;
            }
            for (TreeNode nodo : nodoSeleccionado.getChildren()) {
                Object[] fila = (Object[]) nodo.getData();
                if (fila[0].toString().equals(campoPrimaria)) {
                    fila[1] = campoNombre;
                    break;
                }
            }
        }
    }

    public void onCommitEliminar(String campoPrimaria) {
        if (tipoSeleccion == false) {
            if (nodoSeleccionado == null) {
                nodoSeleccionado = raiz;
            }
            for (TreeNode nodo : nodoSeleccionado.getChildren()) {
                Object[] fila = (Object[]) nodo.getData();
                if (fila[0].toString().equals(campoPrimaria)) {
                    nodoSeleccionado.getChildren().remove(nodo);
                    break;
                }
            }
        }
    }

    public void ejecutarSql() {
        limpiar();
        if (!condicion.isEmpty() && !condicion.trim().startsWith("AND")) {
            condicion = " AND " + condicion;
        }
        sql = "SELECT " + campoPrimaria + "," + campoNombre + "," + campoPadre + ",(SELECT count(" + campoPadre + ") from " + tabla + " where " + campoPadre + "=a." + campoPrimaria + ")  FROM " + tabla + " a WHERE 1=1" + condicion + " " + condicionEmpresa + " ORDER BY " + campoPadre + " desc," + campoOrden;

        List lista = conexion.consultar(sql);
        if (!lista.isEmpty()) {
            for (Object actual : lista) {
                Object[] fila = (Object[]) actual;
                //Los que son padres
                TreeNode padre = null;
                if (fila[2] == null) {
                    //Agrega Menus principales

                    int numHijos = 0;
                    try {
                        numHijos = Integer.parseInt(String.valueOf(fila[3]));
                    } catch (Exception e) {
                    }
                    //Si tiene hijos busca los hijos
                    if (numHijos > 0) {
                        padre = new DefaultTreeNode(fila, raiz);

                        if (optimiza) {//Una sola vuelta
                            formar_arbol_recursivo(padre, fila, lista);
                        } else { //Hace varios sql por la condición que tiene
                            formar_arbol_recursivo(padre, fila[0]);
                        }

                    } else {
                        padre = new DefaultTreeNode("document", fila, raiz);
                    }
                } else {
                    break;
                }
            }
        }
        seleccionados = null;
    }

    private void formar_arbol_recursivo(TreeNode padre, Object valor_padre) {
        String sql_recursivo = "SELECT " + campoPrimaria + "," + campoNombre + "," + campoPadre + ",(SELECT count(" + campoPadre + ") from " + tabla + " where " + campoPadre + "=a." + campoPrimaria + ")  FROM " + tabla + " a WHERE " + campoPadre + "=" + valor_padre + " ORDER BY " + campoPadre + " desc," + campoOrden;
        List lista = conexion.consultar(sql_recursivo);
        if (!lista.isEmpty()) {
            for (Object lista1 : lista) {
                TreeNode hijo = null;
                Object[] fila = (Object[]) lista1;
                int numHijos = 0;
                try {
                    numHijos = Integer.parseInt(String.valueOf(fila[3]));
                } catch (Exception e) {
                }
                //Si tiene hijos busca los hijos                
                if (numHijos > 0) {
                    hijo = new DefaultTreeNode(fila, padre);
                    formar_arbol_recursivo(hijo, fila[0]);
                } else {
                    hijo = new DefaultTreeNode("document", fila, padre);
                }
            }
        }
    }

    private void formar_arbol_recursivo(TreeNode padre, Object[] fila, List lista) {
        boolean encontro = false;
        //Si no tiene hijos
        for (Object actual : lista) {
            //Busca los hijos            
            Object[] filaActual = (Object[]) actual;

            //Optimiza si ya encontro y el siguiente registro es diferente, fin de la busqueda
            if (encontro) {
                if (!fila[0].equals(filaActual[2])) {
                    break;
                }
            }

            if (fila[0].equals(filaActual[2])) {
                encontro = true;
                //System.out.print("Busca : " + fila[0] + "   " + filaActual[2]);
                int numHijos = 0;
                try {
                    numHijos = Integer.parseInt(String.valueOf(filaActual[3]));
                } catch (Exception e) {
                }
                //Si tiene hijos busca los hijos
                TreeNode node = null;
                if (numHijos > 0) {
                    node = new DefaultTreeNode(filaActual, padre);
                    formar_arbol_recursivo(node, filaActual, lista);
                } else {
                    node = new DefaultTreeNode("document", filaActual, padre);

                }
            }
        }
    }

//    public void setNumeroTabla(int numeroTabla) {
//        if (conexion == null) {
//            conexion = framework.getConexion();
//        }
//        List lis_busca = conexion.consultar("SELECT TABLA_TABL,PRIMARIA_TABL,NOMBRE_TABL,PADRE_TABL SIS_TABLA WHERE NUMERO_TABL=" + numeroTabla + " AND id_opcion=" + framework.getVariable("id_opcion"));
//        if (!lis_busca.isEmpty()) {
//            Object[] obj = (Object[]) lis_busca.get(0);
//            if ((obj[0] != null && !obj[0].toString().isEmpty()) && (obj[1] != null && !obj[1].toString().isEmpty()) && (obj[2] != null && !obj[2].toString().isEmpty()) && (obj[3] != null && !obj[3].toString().isEmpty())) {
//                setArbol(obj[0].toString(), obj[1].toString(), obj[2].toString(), obj[3].toString());
//            }
//        }
//    }

    public void dibujar() {
        Ajax aja_selec_nodo = new Ajax();
        this.getChildren().clear();
        ejecutarSql();
        nodoSeleccionado = raiz;
        nodoSeleccionado.setSelected(true);
        if (tipoSeleccion == false) {
            if (!metodo.isEmpty()) {
                aja_selec_nodo.setMetodo(metodo);
            } else {
                aja_selec_nodo.setMetodoRuta(ruta + "." + this.getId() + ".seleccionarNodo");
            }

            this.addClientBehavior("select", aja_selec_nodo);
            this.setValueExpression("value", crearValueExpression(ruta + "." + this.getId() + ".nodos"));
            this.setValueExpression("selection", crearValueExpression(ruta + "." + this.getId() + ".nodoSeleccionado"));
            this.setValueExpression("rowKey", crearValueExpression("fila[0]"));
            this.setVar("fila");
            this.setSelectionMode("single");
        } else {
            //tipo seleccion             
            this.setValueExpression("value", crearValueExpression(ruta + ".arb_seleccion.nodos"));
            this.setValueExpression("selection", crearValueExpression(ruta + ".arb_seleccion.seleccionados"));
            this.setSelectionMode("checkbox");
            this.setValueExpression("rowKey", crearValueExpression("fila[0]"));
            this.setVar("fila");
        }
        UITreeNode ui_nodos = new UITreeNode();
        ui_nodos.setExpandedIcon("ui-icon-folder-open");
        ui_nodos.setCollapsedIcon("ui-icon-folder-collapsed");

        Etiqueta lin_texto = new Etiqueta();
        lin_texto.setValueExpression("value", crearValueExpression("fila[1]"));
        ui_nodos.getChildren().add(lin_texto);
        this.getChildren().add(ui_nodos);

        UITreeNode ui_docu = new UITreeNode();
        ui_docu.setType("document");
        ui_docu.setIcon("ui-icon-document");
        Etiqueta lin_docu = new Etiqueta();
        lin_docu.setValueExpression("value", crearValueExpression("fila[1]"));
        ui_docu.getChildren().add(lin_docu);
        this.getChildren().add(ui_docu);
    }

    private boolean mover = false; //Para el rendered del menu contextual
    private TreeNode nodoMueve = null;

    public boolean isMover() {
        return mover;
    }

    /**
     * Cuando selecciona mover del menu contextual
     */
    public void seleccionarMover() {
        //Valida que no se mueva el nodo raiz
        if (nodoSeleccionado == raiz) {
            framework.agregarMensajeError("Mover Nodo", "No se puede mover el nodo Raíz");
            return;
        }
//Respalda nodo que se va a mover
        nodoMueve = nodoSeleccionado;
        mover = true;
        framework.addUpdate("menuc_" + this.getId());
        framework.agregarMensaje("Mover Nodo", "Seleccione un nodo para realizar el cambio");
    }

    /**
     * Mueve un nodo a otro nodo
     */
    public void aceptarMoverNodo() {
        if (nodoMueve != null && nodoSeleccionado != null) {
            if (nodoSeleccionado != nodoMueve) //Elimina del padre el nodo actual
            {
                try {
                    //Elimina nodo hijo del padre actual
                    nodoMueve.getParent().getChildren().remove(nodoMueve);
                    nodoSeleccionado.getChildren().add(nodoMueve);
                    //Genera sql de actualizacion y agrega a la lista de sql
                    Object[] fila = (Object[]) nodoMueve.getData();
                    String str_sql = "UPDATE " + tabla + " SET " + campoPadre + "=" + getValorSeleccionado() + " WHERE " + campoPrimaria + "=" + fila[0];
                    framework.getConexion().agregarSql(str_sql);
                    cancelarMoverNodo();
                    framework.agregarMensaje("Mover Nodo", "Se movió correctamente");
                    framework.addUpdate(this.getId());
                } catch (Exception e) {
                }
            } else {
                framework.agregarMensajeInfo("No se puede Mover", "Debe seleccionar un Nodo diferente");
            }
        }
    }

    public void cancelarMoverNodo() {
        nodoMueve = null;
        mover = false;
        framework.addUpdate("menuc_" + this.getId());
    }

    public void actualizar() {
        ejecutarSql();
        framework.addUpdate(this.getId());
    }

    public void seleccionarNodo(NodeSelectEvent evt) {
        nodoSeleccionado = evt.getTreeNode();
//        setSelection(nodoSeleccionado);
    }

    public void limpiar() {
        raiz.getChildren().clear();
        raiz.setExpanded(true);
        nodoSeleccionado = raiz;
//        setSelection(nodoSeleccionado);
//        setRowKey(null);
    }

    public void seleccinarNodo(String valor) {
        if (nodoSeleccionado != null) {
            nodoSeleccionado.setExpanded(false);
            nodoSeleccionado.setSelected(false);
        }

        for (TreeNode nodo : raiz.getChildren()) {
            Object[] fila = (Object[]) nodo.getData();
            if (fila[0].toString().equals(valor)) {
                nodo.getParent().setSelected(true);
                setNodoSeleccionado(nodo.getParent());
                return;
            } else {
                buscarNodo(nodo, valor);
            }
        }
        TreeNode tre_aux = nodoSeleccionado.getParent();
        while (tre_aux != null) {
            tre_aux.setExpanded(true);
            tre_aux = tre_aux.getParent();
        }
    }

    private void buscarNodo(TreeNode padre, String valor) {
        for (TreeNode nodo : padre.getChildren()) {
            Object[] fila = (Object[]) nodo.getData();
            if (fila[0].toString().equals(valor)) {
                nodo.getParent().setSelected(true);
                setNodoSeleccionado(nodo.getParent());
                return;
            } else {
                buscarNodo(nodo, valor);
            }
        }
    }

    public String getCampoNombre() {
        return campoNombre;
    }

    public String getCampoPadre() {
        return campoPadre;
    }

    public String getTabla() {
        return tabla;
    }

    public TreeNode getNodos() {
        return nodos;
    }

    public void setNodos(TreeNode nodos) {
        this.nodos = nodos;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
        this.optimiza = false;  //por defecto hace varios sql para formar el arbol
    }

    public void onSelect(String metodo) {
        this.metodo = metodo;
    }

    public String getValorSeleccionado() {

        if (((Object[]) nodoSeleccionado.getData())[0] == null) {
            return null;
        } else {
            return ((Object[]) nodoSeleccionado.getData())[0] + "";
        }
    }

    public TreeNode getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(TreeNode nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

    private ValueExpression crearValueExpression(String valueExpression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getCampoPrimaria() {
        return campoPrimaria;
    }

    public boolean isTipoSeleccion() {
        return tipoSeleccion;
    }

    public void setTipoSeleccion(boolean tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }

    public TreeNode[] getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(TreeNode[] seleccionados) {
        this.seleccionados = seleccionados;
    }

    public String getFilasSeleccionadas() {
        String str_seleccionadas = "";
        if (seleccionados != null) {
            for (TreeNode seleccionado : seleccionados) {
                if (!str_seleccionadas.isEmpty()) {
                    str_seleccionadas += ",";
                }
                str_seleccionadas += ((Object[]) seleccionado.getData())[0] + "";
            }
        }
        return str_seleccionadas;
    }

    public String getSql() {
        return sql;
    }

    /**
     * Optimiza a un solo sql para formar el Arbol true= 1 solo sql
     *
     * @return
     */
    public boolean isOptimiza() {
        return optimiza;
    }

    public void setOptimiza(boolean optimiza) {
        this.optimiza = optimiza;
    }

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

}
