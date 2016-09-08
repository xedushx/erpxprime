/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Fila;
import java.util.List;

/**
 *
 * @author xedushx 
 */
public class SeleccionTabla extends Dialogo {

    private String ruta = "mbe_index.clase";
    private Tabla tab_seleccion = new Tabla();
    private final Grid gri_cuerpo = new Grid();
    private BotonesCombo boc_seleccion_inversa = new BotonesCombo();
    private final ItemMenu itm_todas = new ItemMenu();
    private final ItemMenu itm_niguna = new ItemMenu();
    private boolean aux_tabla = false;

    public SeleccionTabla() {
        this.setWidth("50%");
        this.setHeight("60%");
        this.setResizable(false);
        this.setHeader("Seleccionar");

        gri_cuerpo.setTransient(true);
        this.setDialogo(gri_cuerpo);
    }

    public void setSeleccionTabla(String tabla, String campoPrimaria, String campos) {
        tab_seleccion.inicializar();
        gri_cuerpo.getChildren().clear();
        tab_seleccion.getChildren().clear();
        tab_seleccion.setId(this.getId() + "_tab_seleccion");
        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodoRuta(ruta + "." + this.getId() + ".seleccinarInversa");
        boc_seleccion_inversa.setUpdate(tab_seleccion.getId());
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodoRuta(ruta + "." + this.getId() + ".seleccionarTodas");
        itm_todas.setUpdate(tab_seleccion.getId());
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodoRuta(ruta + "." + this.getId() + ".seleccionarNinguna");
        itm_niguna.setUpdate(tab_seleccion.getId());
        boc_seleccion_inversa.agregarBoton(itm_niguna);

        tab_seleccion.setRuta(ruta + "." + this.getId());
        tab_seleccion.setCampoPrimaria(campoPrimaria);        
        if (framework.tieneCampoEmpresa(tabla, campoPrimaria)) {
            tab_seleccion.setSql("SELECT " + campoPrimaria + "," + campos + " FROM " + tabla + " WHERE id_empresa=" + framework.getVariable("empresa"));
        } else {
            tab_seleccion.setSql("SELECT " + campoPrimaria + "," + campos + " FROM " + tabla);
        }

        tab_seleccion.setTipoSeleccion(true);
        tab_seleccion.setRows(15);
    }

    public void setRadio() {
        tab_seleccion.setSeleccionTabla("single");
        boc_seleccion_inversa.setRendered(false);
    }

    public void setCheck() {
        tab_seleccion.setSeleccionTabla("multiple");
        boc_seleccion_inversa.setRendered(true);
    }

    public void setSeleccionTabla(String sql, String campoPrimaria) {
        tab_seleccion.inicializar();
        gri_cuerpo.getChildren().clear();
        tab_seleccion.setId(this.getId() + "_tab_seleccion");
        tab_seleccion.getChildren().clear();
        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodoRuta(ruta + "." + this.getId() + ".seleccinarInversa");
        boc_seleccion_inversa.setUpdate(tab_seleccion.getId());
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodoRuta(ruta + "." + this.getId() + ".seleccionarTodas");
        itm_todas.setUpdate(tab_seleccion.getId());
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodoRuta(ruta + "." + this.getId() + ".seleccionarNinguna");
        itm_niguna.setUpdate(tab_seleccion.getId());
        boc_seleccion_inversa.agregarBoton(itm_niguna);

        tab_seleccion.setRuta(ruta + "." + this.getId());
        tab_seleccion.setCampoPrimaria(campoPrimaria);
        tab_seleccion.setSql(sql);
        tab_seleccion.setTipoSeleccion(true);
        tab_seleccion.setRows(15);
    }

    @Override
    public void dibujar() {
        if (aux_tabla == false) {
            tab_seleccion.dibujar();
            gri_cuerpo.getChildren().add(boc_seleccion_inversa);
            gri_cuerpo.getChildren().add(tab_seleccion);
            aux_tabla = true;
        }
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        limpiarSeleccionados();
        super.dibujar();
    }

    public void setSql(String sql) {
        tab_seleccion.setSql(sql);
        tab_seleccion.ejecutarSql();
    }

    public void redibujar() {
        tab_seleccion.dibujar();
        gri_cuerpo.getChildren().add(boc_seleccion_inversa);
        gri_cuerpo.getChildren().add(tab_seleccion);
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        super.dibujar();
    }

  
    public String getSeleccionados() {
        return tab_seleccion.getFilasSeleccionadas();
    }

    public void limpiarSeleccionados() {
        if (tab_seleccion != null) {
            tab_seleccion.setSeleccionados(null);
            tab_seleccion.setFiltro(null);
    //        tab_seleccion.limpiarFiltros();  
            tab_seleccion.setFilaSeleccionada(null);
        }
    }

    public List<Fila> getListaSeleccionados() {
        return tab_seleccion.getListaFilasSeleccionadas();
    }
    
    public String getValorSeleccionado() {
        if (tab_seleccion.getFilaSeleccionada() != null) {
            return tab_seleccion.getFilaSeleccionada().getRowKey();
        }
        return null;
    }

    public void seleccionarTodas() {
        tab_seleccion.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_seleccion.getTotalFilas()];
        for (int i = 0; i < tab_seleccion.getFilas().size(); i++) {
            seleccionados[i] = tab_seleccion.getFilas().get(i);
        }
        tab_seleccion.setSeleccionados(seleccionados);
    }

    public void seleccionarNinguna() {
        tab_seleccion.setSeleccionados(null);
    }

    public int getNumeroSeleccionados() {
        int int_seleccionados = 0;
        if (tab_seleccion.getSeleccionados() != null) {
            int_seleccionados = tab_seleccion.getSeleccionados().length;
        }
        return int_seleccionados;
    }

    public void seleccinarInversa() {
        if (tab_seleccion.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_seleccion.getSeleccionados().length == tab_seleccion.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_seleccion.getTotalFilas() - tab_seleccion.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_seleccion.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (Fila seleccionado : tab_seleccion.getSeleccionados()) {
                    if (seleccionado.equals(tab_seleccion.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_seleccion.getFilas().get(i);
                    cont++;
                }
            }
            tab_seleccion.setSeleccionados(seleccionados);
        }
    }

    public void setFilasSeleccionados(String seleccionados) {
        tab_seleccion.setFilasSeleccionados(seleccionados);

    }

    public BotonesCombo getBoc_seleccion_inversa() {
        return boc_seleccion_inversa;
    }

    public void setBoc_seleccion_inversa(BotonesCombo boc_seleccion_inversa) {
        this.boc_seleccion_inversa = boc_seleccion_inversa;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Tabla getTab_seleccion() {
        return tab_seleccion;
    }

    public void setTab_seleccion(Tabla tab_seleccion) {
        this.tab_seleccion = tab_seleccion;
    }
}
