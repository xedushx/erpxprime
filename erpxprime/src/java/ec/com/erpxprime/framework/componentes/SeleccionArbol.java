/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import org.primefaces.model.TreeNode;

/**
 *
 * @author xedushx 
 */
public class SeleccionArbol extends Dialogo {

    private String ruta = "mbe_index.clase";
    private Arbol arb_seleccion = new Arbol();
    private Grid gri_cuerpo = new Grid();
    private boolean aux_arbol = false;

    public SeleccionArbol() {
        this.setWidth("35%");
        this.setHeight("60%");
        this.setResizable(false);
        this.setHeader("Seleccionar");
        gri_cuerpo.setTransient(true);
        gri_cuerpo.getChildren().add(arb_seleccion);
        this.setDialogo(gri_cuerpo);

    }

    public void setSeleccionArbol(String tabla, String campoPrimaria, String campoNombre, String campoPadre) {
        arb_seleccion.setId(this.getId() + "_arb_seleccion");
        arb_seleccion.setDynamic(false);
        arb_seleccion.setRuta(ruta + "." + this.getId());
        arb_seleccion.setTipoSeleccion(true);
        arb_seleccion.setArbol(tabla, campoPrimaria, campoNombre, campoPadre);
    }

    public void setTituloArbol(String titulo) {
        arb_seleccion.setTitulo(titulo);
    }

    @Override
    public void dibujar() {
        if (aux_arbol == false) {
            arb_seleccion.dibujar();
            aux_arbol = true;
        }
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        arb_seleccion.setStyle("width:" + (getAnchoPanel() - 15) + "px;height:" + (getAltoPanel() - 10) + "px;overflow: hidden;display: block;");
        super.dibujar();
    }

    @Override
    public Grid getGri_cuerpo() {
        return gri_cuerpo;
    }

    @Override
    public void setGri_cuerpo(Grid gri_cuerpo) {
        this.gri_cuerpo = gri_cuerpo;
    }

    public String getSeleccionados() {
        return arb_seleccion.getFilasSeleccionadas();
    }

    public TreeNode[] getNodosSeleccionados() {
        return arb_seleccion.getSeleccionados();
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Arbol getArb_seleccion() {
        return arb_seleccion;
    }

    public void setArb_seleccion(Arbol arb_seleccion) {
        this.arb_seleccion = arb_seleccion;
    }

    @Override
    public void cerrar() {
        arb_seleccion.setSeleccionados(null);
        super.cerrar();
    }
}
