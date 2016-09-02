/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.aplicacion.Framework;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class Upload extends FileUpload {

    private String carpeta;
    private String ruta = "mbe_index.clase";
    private List<File> archivos;
    private File archivo;
    private String nombreReal;
    private StreamedContent stream;

    public Upload() {
        this.setLabel("Buscar Archivo");
        this.setCancelLabel("Cancelar");
        this.setAuto(true);
        this.setMode("advanced");
        this.setStyle("height: 60px;");
        this.setInvalidFileMessage("El archivo seleccionado no es válido");
        this.setInvalidSizeMessage("El archivo seleccionado supera le tamaño máximo");
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        setWidgetVar(id);
    }

    /**
     * Cuando sube el archivo a una carpeta
     *
     * @param carpeta
     */
    public void setUpload(String carpeta) {
        this.carpeta = carpeta;
        MethodExpression methodExpression;
        methodExpression
                = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + ruta + "." + this.getId() + ".subirArchivo}", null, new Class[]{
                    FileUploadEvent.class
                });
        this.setFileUploadListener(methodExpression);
    }

    public void setUpload() {
        MethodExpression methodExpression;
        methodExpression
                = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + ruta + "." + this.getId() + ".subirArchivo}", null, new Class[]{
                    FileUploadEvent.class
                });
        this.setFileUploadListener(methodExpression);
    }

    public void setMetodo(String metodo) {
        MethodExpression methodExpression;
        methodExpression
                = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + ruta + "." + metodo + "}", null, new Class[]{
                    FileUploadEvent.class
                });
        this.setFileUploadListener(methodExpression);
    }

    public void setMetodoRuta(String metodo) {
        MethodExpression methodExpression;
        methodExpression
                = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
                createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + metodo + "}", null, new Class[]{
                    FileUploadEvent.class
                });
        this.setFileUploadListener(methodExpression);
    }

    public void setSoloImagenes() {
        this.setAllowTypes("/(\\.|\\/)(gif|jpe?g|png|bmp)$/");
        this.setLabel("Elegir Imágen");
    }

    public void subirArchivo(FileUploadEvent event) {
        if (carpeta != null) {

            try {
                Framework framework = new Framework();
                String str_nombre = framework.getVariable("id_usuario") + framework.getFechaActual().replace("-", "") + framework.getHoraActual().replace(":", "") + event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."), event.getFile().getFileName().length());
                String str_path = framework.getPropiedad("rutaUpload") + "/" + carpeta;
                File path = new File(str_path);
                path.mkdirs();//Creo el Directorio            
                archivo = new File(str_path + "/" + str_nombre);
                ///Para el .war 
                ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
                str_path = extContext.getRealPath("/upload/" + carpeta);
                path = new File(str_path);
                path.mkdirs();//Creo el Directorio
                File result1 = new File(str_path + "/" + str_nombre);
                int BUFFER_SIZE = 6124;
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(archivo);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bulk;
                    InputStream inputStream = event.getFile().getInputstream();
                    while (true) {
                        bulk = inputStream.read(buffer);
                        if (bulk < 0) {
                            break;
                        }
                        fileOutputStream.write(buffer, 0, bulk);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    if (isMultiple()) {
                        if (archivos == null) {
                            archivos = new ArrayList();
                        } else {
                            archivos.add(archivo);
                        }
                    }
                    nombreReal = event.getFile().getFileName();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(result1);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bulk;
                    InputStream inputStream = event.getFile().getInputstream();
                    while (true) {
                        bulk = inputStream.read(buffer);
                        if (bulk < 0) {
                            break;
                        }
                        fileOutputStream.write(buffer, 0, bulk);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            try {
                //Funciona como stream
                UploadedFile file = event.getFile();
                stream = new DefaultStreamedContent(file.getInputstream(), file.getContentType());
                nombreReal = file.getFileName();
                System.out.println(" nombre " + nombreReal + "   " + stream);
            } catch (IOException ex) {
                System.out.println("ERROR:" + ex.getMessage());
            }

        }

    }

    public void limpiar() {
        archivos = null;
        archivo = null;
        nombreReal = null;
    }

    public void setValueExpression(String nombre, String expresion) {
        this.setValueExpression(nombre, crearValueExpression(expresion));
    }

    private ValueExpression crearValueExpression(String expresion) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), "#{" + expresion + "}", Object.class);
    }

    @Override
    public void setUpdate(String _update) {
        Framework framework = new Framework();
        super.setUpdate(framework.formarUpdate(_update));
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public List<File> getArchivos() {
        return archivos;
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public String getNombreReal() {
        return nombreReal;
    }

    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

}
