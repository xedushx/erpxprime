/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.reportes.GenerarReporte;
import ec.com.erpxprime.framework.reportes.ReporteDataSource;
import java.util.HashMap;
import java.util.Map;
import ec.com.erpxprime.persistencia.Conexion;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class SeleccionFormatoReporte extends Dialogo {

    private String ruta = "mbe_index.clase";
    private Link enl_pdf = new Link();
    private Link enl_word = new Link();
    private Link enl_excel = new Link();
    private Link enl_powerpoint = new Link();
    private Map parametros = new HashMap();
    private String pathReporte = "";
    private ReporteDataSource dataSource;

    private Conexion conexion; //Si la conexion ews diferente a la principal

    public SeleccionFormatoReporte() {
        this.setId("sfr_formato");
        this.setWidth("24%");
        this.setHeight("33%");
        this.setResizable(false);
        this.setHeader("Formato Reporte");
        Grid gri_cuerpo = new Grid();
        gri_cuerpo.setColumns(2);
        Imagen ima_pdf = new Imagen();
        ima_pdf.setValue("/imagenes/im_pdf.gif");
        enl_pdf.setValue("Adobe Reader(.PDF)");
        enl_pdf.setAjax(false);
        gri_cuerpo.getChildren().add(ima_pdf);
        gri_cuerpo.getChildren().add(enl_pdf);
        Imagen ima_excel = new Imagen();
        ima_excel.setValue("/imagenes/im_excel.gif");
        enl_excel.setValue("Microsoft Excel (.XLSX)");
        enl_excel.setAjax(false);
        gri_cuerpo.getChildren().add(ima_excel);
        gri_cuerpo.getChildren().add(enl_excel);
        Imagen ima_word = new Imagen();
        ima_word.setValue("/imagenes/im_word.gif");
        enl_word.setValue("Microsoft Word (.DOCX)");
        enl_word.setAjax(false);
        gri_cuerpo.getChildren().add(ima_word);
        gri_cuerpo.getChildren().add(enl_word);

        Imagen ima_html = new Imagen();
        ima_html.setValue("/imagenes/im_powerpoint.gif");
        enl_powerpoint.setValue("PowerPoint (.PPTX))");
        enl_powerpoint.setAjax(false);
        gri_cuerpo.getChildren().add(ima_html);
        gri_cuerpo.getChildren().add(enl_powerpoint);

        this.getBot_aceptar().setRendered(false);
        this.setDialogo(gri_cuerpo);
    }

    public void setSeleccionFormatoReporte(Map parametros, String pathReporte) {
        if (pathReporte == null) {
            pathReporte = "";
        }
        if (parametros == null) {
            parametros = new HashMap();
        }
        this.parametros = parametros;
        this.pathReporte = pathReporte;
    }

    public void setSeleccionFormatoReporte(Map parametros, String pathReporte, ReporteDataSource dataSource) {
        if (pathReporte == null) {
            pathReporte = "";
        }
        if (parametros == null) {
            parametros = new HashMap();
        }
        this.dataSource = dataSource;
        this.parametros = parametros;
        this.pathReporte = pathReporte;
    }

    @Override
    public void dibujar() {
        enl_excel.setMetodoRuta(ruta + "." + this.getId() + ".generarXLSX");
        enl_word.setMetodoRuta(ruta + "." + this.getId() + ".generarDOCX");
        enl_pdf.setMetodoRuta(ruta + "." + this.getId() + ".generarPDF");
        enl_powerpoint.setMetodoRuta(ruta + "." + this.getId() + ".generarPPTX");

        super.dibujar();
    }

    public void generarPDF() {
        if (!pathReporte.isEmpty()) {
            GenerarReporte generar = new GenerarReporte();
            if (conexion != null) {
                generar.setConexion(conexion);
            }
            generar.setDataSource(dataSource);
            generar.generarPDF(parametros, pathReporte);
        } else {
            System.out.println("! Falta el path del reporte ¡");
        }
    }

    public void generarDOCX() {
        if (!pathReporte.isEmpty()) {
            GenerarReporte generar = new GenerarReporte();
            generar.setDataSource(dataSource);
            generar.generarDOCX(parametros, pathReporte);
        } else {
            System.out.println("! Falta el path del reporte ¡");
        }
    }

    public void generarPPTX() {
        if (!pathReporte.isEmpty()) {
            GenerarReporte generar = new GenerarReporte();
            if (conexion != null) {
                generar.setConexion(conexion);
            }
            generar.setDataSource(dataSource);
            generar.generarPPTX(parametros, pathReporte);
        } else {
            System.out.println("! Falta el path del reporte ¡");
        }
    }

    public void generarXLSX() {
        if (!pathReporte.isEmpty()) {
            GenerarReporte generar = new GenerarReporte();
            if (conexion != null) {
                generar.setConexion(conexion);
            }
            generar.setDataSource(dataSource);
            generar.generarXLS(parametros, pathReporte);
        } else {
            System.out.println("! Falta el path del reporte ¡");
        }
        dataSource = null;
    }

    public Map getParametros() {
        return parametros;
    }

    public String getPathReporte() {
        return pathReporte;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public ReporteDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReporteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Conexion getConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

}
