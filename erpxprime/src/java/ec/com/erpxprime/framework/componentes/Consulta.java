/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Consulta extends Dialogo {

    private String ruta = "mbe_index.clase";
    private Tabla tab_consulta_dialogo = new Tabla();
    private Grid gri_cuerpo = new Grid();
    private boolean aux_tabla = false;

    public Consulta() {        
        this.setWidth("50%");
        this.setHeight("60%");
        this.setResizable(false);
        this.setHeader("");

        gri_cuerpo.setTransient(true);
        this.setDialogo(gri_cuerpo);
    }

    public void setConsulta(String tabla, String campoPrimaria, String campos) {
        gri_cuerpo.getChildren().clear();
        tab_consulta_dialogo.getChildren().clear();
        tab_consulta_dialogo.setId("tab_consulta_dialogo");

        tab_consulta_dialogo.setRuta(ruta + "." + this.getId());
        tab_consulta_dialogo.setCampoPrimaria(campoPrimaria);
        tab_consulta_dialogo.setSql("SELECT " + campos + " FROM " + tabla + " WHERE id_empresa=" + framework.getVariable("empresa"));
        tab_consulta_dialogo.setLectura(true);
        tab_consulta_dialogo.setRows(15);

    }

    public void setConsulta(String sql, String campoPrimaria) {
        gri_cuerpo.getChildren().clear();        
        tab_consulta_dialogo.getChildren().clear();
        tab_consulta_dialogo.setId("tab_consulta_dialogo");
        tab_consulta_dialogo.setCampoPrimaria(campoPrimaria);
        tab_consulta_dialogo.setRuta(ruta + "." + this.getId());
        tab_consulta_dialogo.setSql(sql);
        tab_consulta_dialogo.setLectura(true);
        tab_consulta_dialogo.setRows(15);


    }

    @Override
    public void dibujar() {
        if (aux_tabla == false) {
            tab_consulta_dialogo.dibujar();
            gri_cuerpo.getChildren().add(tab_consulta_dialogo);
            aux_tabla = true;
        }
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Tabla getTab_consulta_dialogo() {
        return tab_consulta_dialogo;
    }

    public void setTab_consulta_dialogo(Tabla tab_consulta_dialogo) {
        this.tab_consulta_dialogo = tab_consulta_dialogo;
    }
    
}
