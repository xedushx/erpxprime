package ec.com.erpxprime.framework.reportes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.persistencia.Conexion;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

//http://curiotecnology.blogspot.com/2015/03/primefaces-jasperreports-reportes.html 
/**
 *
 * @author xedushx
 */
public class GenerarReporte {

    private final Framework framework = new Framework();
    private ReporteDataSource dataSource;
    private JasperPrint jasperPrint;
    private Conexion con_conexion; //Si el reporte es de otra conexion

    public GenerarReporte() {
        this.con_conexion = framework.getConexion();
    }

    public void generarHTML(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ejecutar(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "inline; filename=" + getNombreDescarga(reporte) + ".html");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRExporter htmlExporter = new JRHtmlExporter();
            htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            htmlExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
            htmlExporter.exportReport();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (Exception ex) {
            System.out.println("Error generarHTML:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarPPTX(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ejecutar(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getNombreDescarga(reporte) + ".pptx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRPptxExporter docxExporter = new JRPptxExporter();
            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            docxExporter.exportReport();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarDOCX():" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarODT(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ejecutar(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getNombreDescarga(reporte) + ".odt");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JROdtExporter docxExporter = new JROdtExporter();
            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            docxExporter.exportReport();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarDOCX():" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarDOCX(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ejecutar(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getNombreDescarga(reporte) + ".docx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRDocxExporter docxExporter = new JRDocxExporter();
            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            docxExporter.exportReport();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarDOCX():" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarPDF(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ejecutar(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-Disposition", "attachment; filename=" + getNombreDescarga(reporte) + ".pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            pdfExporter.exportReport();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarPDF() :" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarXLSX(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ejecutar(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getNombreDescarga(reporte) + ".xlsx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRXlsxExporter docxExporter = new JRXlsxExporter();
            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            docxExporter.exportReport();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarXLSX() :" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void generarXLS(Map parametros, String reporte) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext ec = facesContext.getExternalContext();
            ejecutar(parametros, reporte);
            HttpServletResponse resp = (HttpServletResponse) ec.getResponse();
            resp.setContentType("application/vnd.ms-excel");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + getNombreDescarga(reporte) + ".xls\"");
            JExcelApiExporter exporterXLS = new JExcelApiExporter();
            exporterXLS.setParameter(
                    JRXlsExporterParameter.JASPER_PRINT,
                    jasperPrint);
            exporterXLS.setParameter(
                    JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
                    Boolean.TRUE);
            exporterXLS.setParameter(
                    JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                    Boolean.FALSE);
            exporterXLS.setParameter(
                    JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                    Boolean.TRUE);
            exporterXLS.setParameter(
                    JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
                    Boolean.TRUE);

            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
                    resp.getOutputStream());
            exporterXLS.exportReport();
            //  System.out.println(" reporte generado ");

            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarXLS() :" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public File generar(Map parametros, String reporte) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ejecutar(parametros, reporte);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            String realPath = (String) ec.getRealPath("/reportes");
            File fil_reporte = new File(realPath + "/reporte" + framework.getVariable("id_usuario") + ".pdf");
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
            exporter.exportReport();
            //  System.out.println(" Reporte Generado " + fil_reporte);  
            return fil_reporte;
        } catch (Exception ex) {
            System.out.println("error" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public File generarXLSUsuario(Map parametros, String reporte) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ejecutar(parametros, reporte);
            JExcelApiExporter exporter = new JExcelApiExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            String realPath = (String) ec.getRealPath("/reportes");
            File fil_reporte = new File(realPath + "/reporte" + framework.getVariable("id_usuario") + ".pdf");
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
            exporter.exportReport();
            //  System.out.println(" Reporte Generado " + fil_reporte);  
            return fil_reporte;
        } catch (Exception ex) {
            System.out.println("error" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private void ejecutar(Map parametros, String reporte) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            InputStream fis = ec.getResourceAsStream(reporte);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fis);
            if (parametros == null) {
                parametros = new HashMap();
            }
            try {
                parametros.put("id_empresa", Integer.parseInt(framework.getVariable("empresa")));
            } catch (Exception e) {
            }
            
            try {
                parametros.put("usuario", Integer.parseInt(framework.getVariable("id_usuario")));
            } catch (Exception e) {
            }

            parametros.put("SUBREPORT_DIR", framework.getURL());
            if (dataSource == null) {
                con_conexion.conectar(false);
                jasperPrint = JasperFillManager.fillReport(
                        jasperReport, parametros, con_conexion.getConnection());
                //Envio la conexion si no existe otra 
                if (parametros.get("REPORT_CONNECTION") == null) {
                    parametros.put("REPORT_CONNECTION", con_conexion.getConnection()); //Cambio antes getConexion()
                }
                con_conexion.desconectar(false);
            } else {
                jasperPrint = JasperFillManager.fillReport(
                        jasperReport, parametros, dataSource);
                dataSource.setIndice(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error ejecutar" + e.getMessage());
            framework.crearError("No se pudo ejecutar el reporte", "metodo ejecutar()", e);
        }
    }

    private String getNombreDescarga(String reporte) {
        return reporte.substring(reporte.lastIndexOf("/") + 1, reporte.lastIndexOf("."));
    }

    public Conexion getConexion() {
        return con_conexion;
    }

    public void setConexion(Conexion con_conexion) {
        this.con_conexion = con_conexion;
    }

    public ReporteDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReporteDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
