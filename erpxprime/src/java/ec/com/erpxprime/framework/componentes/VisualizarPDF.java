/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.reportes.GenerarReporte;
import ec.com.erpxprime.framework.reportes.ReporteDataSource;
import java.util.Map;
import javax.faces.component.html.HtmlOutputLink;
import org.primefaces.component.media.Media;

/**
 *
 * @author xedushx
 */
public class VisualizarPDF extends Dialogo {

    private final Grid gri_cuerpo = new Grid();
    private final Media med_archivo = new Media();
    HtmlOutputLink out_recurso = new HtmlOutputLink();

    public VisualizarPDF() {
        this.setWidth("80%");
        this.setHeight("80%");
        this.setTitle("Visualizador PDF");
        this.setResizable(false);

        med_archivo.setWidth("100%");
        med_archivo.setHeight(getAltoPanel() - 10 + "px");
        gri_cuerpo.getChildren().add(med_archivo);
        med_archivo.setStyle("");
        this.setDialogo(gri_cuerpo);
        this.getBot_aceptar().setRendered(false);
        this.getBot_cancelar().setValue("Cerrar");

        out_recurso.getChildren().add(new Etiqueta("Descargar archivo"));
        med_archivo.getChildren().add(out_recurso);
    }

    @Override
    public void dibujar() {
        med_archivo.setWidth((getAnchoPanel() - 10) + "px");
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 1) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        super.dibujar();
    }

    public void setVisualizarPDF(String pathReporte, Map parametros) {

        GenerarReporte ger = new GenerarReporte();
        ger.generar(parametros, "/reportes/" + pathReporte);
        med_archivo.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");
        out_recurso.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");

    }

    public void setVisualizarPDF(String path) {
        med_archivo.setValue(path);
        out_recurso.setValue(path);
    }

    public void setVisualizarPDFUsuario() {
        med_archivo.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");
        out_recurso.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");
    }

    public void setVisualizarPDF(ReporteDataSource data, String pathReporte, Map parametros) {

        GenerarReporte ger = new GenerarReporte();
        ger.setDataSource(data);
        ger.generar(parametros, "/reportes/" + pathReporte);
        med_archivo.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");
        out_recurso.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");

    }

    public void setVisualizarPDF(String pathReporte, Map parametros, ReporteDataSource dataSource) {

        GenerarReporte ger = new GenerarReporte();
        ger.setDataSource(dataSource);
        ger.generar(parametros, "/reportes/" + pathReporte);
        med_archivo.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");
        out_recurso.setValue("/reportes/reporte" + framework.getVariable("id_usuario") + ".pdf");
    }
}
