/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.util.ArrayList;
import java.util.List;
import javax.faces.event.AjaxBehaviorEvent;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx 
 */
public class Reporte extends Dialogo {

    private String ruta = "mbe_index.clase";
    private final Lista listaReportes = new Lista();
    private List lis_reportes = new ArrayList();
    private Conexion conexion;
    private final Grid gri_cuerpo = new Grid();
    private String nombre = "";
    private String path = "";

    public Reporte() {
        this.setId("rep_reporte");
        this.setWidth("25%");
        this.setHeight("40%");
        this.setResizable(false);
        this.setHeader("Reportes");
        gri_cuerpo.getChildren().add(listaReportes);
        this.getBot_aceptar().setMetodo("aceptarReporte");
        this.setDialogo(gri_cuerpo);
    }

    public void onSelect(AjaxBehaviorEvent evt) {
        List lis_path = framework.getConexion().consultar("SELECT nombre,path FROM tbl_reporte where id_reporte=" + listaReportes.getValue());
        if (!lis_path.isEmpty()) {
            Object[] obj_fila = (Object[]) lis_path.get(0);
            path = "/reportes/" + obj_fila[1];
            nombre = obj_fila[0] + "";
        }
    }

    public String getValue() {
        return (String) listaReportes.getValue();
    }

    @Override
    public void dibujar() {
        gri_cuerpo.setStyle("width:" + getAnchoPanel() + "px;height:" + getAltoPanel() + "px;");
        listaReportes.setStyle("width:" + (getAnchoPanel() - 10) + "px;height:" + (getAltoPanel() - 10) + "px;");
        listaReportes.getChildren().clear();
        listaReportes.setMetodoChangeRuta(ruta + "." + this.getId() + ".onSelect", "");
        conexion = framework.getConexion();
        lis_reportes = conexion.consultar("SELECT id_reporte,nombre FROM tbl_reporte WHERE id_opcion=" + framework.getVariable("id_opcion") + " AND id_reporte IN (select id_reporte from tbl_perfil_reporte where id_perfil=" + framework.getVariable("id_perfil") + ")");
        if (!lis_reportes.isEmpty()) {
            listaReportes.SetLista(lis_reportes);
            super.dibujar();
        }
        listaReportes.setValue(null);
        path = "";
        nombre = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List getReportes() {
        return lis_reportes;
    }

    public void setReportes(List lis_reportes) {
        this.lis_reportes = lis_reportes;
    }

    public String getReporteSelecionado() {
        return nombre;
    }
}
