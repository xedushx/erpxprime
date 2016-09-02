/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

/**
 *
 * @author xedushx
 */
public class SeleccionArchivo extends Dialogo {

    private Upload upload = new Upload();

    public SeleccionArchivo() {
        this.setId("sar_upload");
        this.setWidth("75%");
        this.setHeight("35%");
        this.setTitle("Cargar Archivo");
        this.setResizable(false);
        this.getGri_cuerpo().setMensajeInfo("Debe seleccionar un archivo, también puede arrastrar y soltar el archivo en el recuadro de la parte inferior.");
        upload.setAuto(false);
        upload.setDragDropSupport(true);
        upload.setLabel("Elegir Archivo");
        upload.setCancelLabel("Cancelar");
        upload.setUploadLabel("Subir Archivo");
        upload.setShowButtons(true);
        upload.setStyle("overflow: auto;padding-top: 10px;height:" + (this.getAltoPanel() - 5) + "pk;width:" + (this.getAnchoPanel() - 8) + "px;");
        this.getGri_cuerpo().getChildren().add(upload);
        this.getBot_aceptar().setRendered(false);
    }

    public void setImagen() {
        upload.setSoloImagenes();
        this.getGri_cuerpo().setMensajeInfo("Debe seleccionar un archivo de tipo imagen, también puede arrastrar y soltar el archivo en el recuadro de la parte inferior.");
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    @Override
    public void cerrar() {
        super.cerrar(); //To change body of generated methods, choose Tools | Templates.
        limpiar();
    }

    public void limpiar() {
        upload.setLabel("Elegir Archivo");
        upload.setAllowTypes(null);
    }
}
