/*
 * Copyright (c) 2013, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.util.ArrayList;
import java.util.List;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.event.FileUploadEvent;

/*
 * @author xedushx Fernando Jácome
 */
public class ImportarTabla extends Dialogo {

    private Tabla tab_col_importar = new Tabla();
    private Grid gri_cuerpo = new Grid();
    private Upload upl_importarxls = new Upload();
    private Texto tex_columna = new Texto();
    private Texto tex_inicio = new Texto();
    private Texto tex_fin = new Texto();
    private Texto tex_numero_hoja = new Texto();
    private List lis_campos_excel = new ArrayList();
    private Tabla tab_tabla;
    private Grid gri_datos = new Grid();
    private int int_inicio = -1;
    private int int_fin = -1;
    private int int_numColumnas = -1;
    private Sheet hoja = null;

    public ImportarTabla() {
        this.setId("imt_importar");
        this.setWidth("62%");
        this.setHeight("70%");
        this.setResizable(false);
        this.setHeader("Importar Registros");


        gri_cuerpo.setMensajeInfo("Configuración del Archivo xls");
        gri_datos.setStyle("width:" + (getAnchoPanel() - 20) + "px;");
        Grid gri_archivo = new Grid();
        gri_archivo.setWidth("95%");
        gri_archivo.setColumns(3);

        Grid gri_matriz = new Grid();
        gri_matriz.setStyle("width:" + (getAnchoPanel() - 40) + "px;");
        gri_matriz.setColumns(4);


        gri_matriz.getChildren().add(new Etiqueta("Fila Nombres de Campos"));
        tex_columna.setSize(5);
        tex_columna.setSoloEnteros();
        gri_matriz.getChildren().add(tex_columna);
        gri_matriz.getChildren().add(new Etiqueta("Primera Fila de Datos"));
        tex_inicio.setSize(5);
        tex_inicio.setSoloEnteros();
        gri_matriz.getChildren().add(tex_inicio);
        gri_matriz.getChildren().add(new Etiqueta("Última Fila de Datos"));
        tex_fin.setSize(5);
        tex_fin.setSoloEnteros();

        gri_matriz.getChildren().add(tex_fin);

        gri_matriz.getChildren().add(new Etiqueta("Número o Nombre de la Hoja"));
        gri_matriz.getChildren().add(tex_numero_hoja);

        gri_archivo.getChildren().add(gri_matriz);

        Grid gri_valida = new Grid();
        gri_valida.setId("gri_valida_importar");
        gri_valida.setColumns(3);

        Etiqueta eti_valida = new Etiqueta();
        eti_valida.setValueExpression("value", "mbe_index.imt_importar.upl_importarxls.nombreReal");
        eti_valida.setValueExpression("rendered", "mbe_index.imt_importar.upl_importarxls.nombreReal != null");
        gri_valida.getChildren().add(eti_valida);

        Imagen ima_valida = new Imagen();
        ima_valida.setWidth("22");
        ima_valida.setHeight("22");
        ima_valida.setValue("/imagenes/im_excel.gif");
        ima_valida.setValueExpression("rendered", "mbe_index.imt_importar.upl_importarxls.nombreReal != null");
        gri_valida.getChildren().add(ima_valida);
        gri_archivo.setFooter(gri_valida);

        upl_importarxls.setUpdate("gri_valida_importar");
        upl_importarxls.setAllowTypes("/(\\.|\\/)(xls)$/");
        upl_importarxls.setMetodoRuta("mbe_index.imt_importar.validar");
        upl_importarxls.setUploadLabel("Validar");
        upl_importarxls.setAuto(false);

        gri_matriz.setFooter(upl_importarxls);

        gri_datos.getChildren().add(gri_archivo);

        gri_cuerpo.setId("gri_cuerpo_importar");

        this.setDialogo(gri_cuerpo);

        this.getBot_aceptar().setMetodoRuta("mbe_index.imt_importar.aceptar");
    }

    public void setImportarTabla(Tabla tabla) {
        tab_col_importar.limpiar();
        this.tab_tabla = tabla;
    }

    public void validar(FileUploadEvent event) {
        
        tab_col_importar.limpiar();
        try {
            Workbook archivoExcel = Workbook.getWorkbook(event.getFile().getInputstream());
            //Abro la hoja indicada
            int int_numero_hoja = -1;
            try {
                int_numero_hoja = Integer.parseInt(tex_numero_hoja.getValue() + "");
            } catch (Exception e) {
                int_numero_hoja = -1;
            }

            if (int_numero_hoja == -1) {
                String[] hojas = archivoExcel.getSheetNames();
                for (int i = 0; hojas.length < 10; i++) {
                    if (hojas[i].equalsIgnoreCase(tex_numero_hoja.getValue() + "")) {
                        hoja = archivoExcel.getSheet(i);
                    }
                }
            } else {
                hoja = archivoExcel.getSheet(int_numero_hoja);
            }

            if (hoja == null) {
                framework.agregarMensajeError("La Hoja: " + tex_numero_hoja.getValue() + " no existe en el Archivo seleccionado", "");
                return;
            }

            int_numColumnas = hoja.getColumns();

            try {
                int_inicio = Integer.parseInt(tex_inicio.getValue() + "");
                int_inicio = int_inicio - 1;
            } catch (Exception e) {
                int_inicio = -1;
            }

            try {
                int_fin = Integer.parseInt(tex_fin.getValue() + "");
            } catch (Exception e) {
                int_fin = -1;
            }

            if (tex_fin.getValue() == null || tex_fin.getValue().toString().isEmpty()) {
                //Si no hay valor en fin le pongo a el total de filas de la Hoja
                int_fin = hoja.getRows();
            }

            if ((int_inicio == -1 || int_fin == -1) || (int_inicio > int_fin)) {
                framework.agregarMensajeError("Los valores ingresados en Primera o Última fila de datos no son válidos", "");
                return;
            }

            int int_fila_columna = -1;

            try {
                int_fila_columna = Integer.parseInt(tex_columna.getValue() + "");
                int_fila_columna = int_fila_columna - 1;
            } catch (Exception e) {
                int_fila_columna = -1;
            }

            if (int_fila_columna == -1) {
                framework.agregarMensajeError("El valor de Fila Nombres de Campos no es válido", "");
                return;
            }


            //Agregar las columnas de nombre de campos    

            lis_campos_excel.clear();
            for (int i = 0; i < int_numColumnas; i++) {
                try {
                    Object[] obj = new Object[2];
                    obj[0] = i;
                    obj[1] = hoja.getCell(i, int_fila_columna).getContents();
                    lis_campos_excel.add(obj);
                } catch (Exception e) {
                    System.out.println("Fallo al leer columnas del archivo xls :" + e.getMessage());
                }
            }
            gri_cuerpo.getChildren().clear();

            gri_cuerpo.getChildren().add(gri_datos);

            tab_col_importar = new Tabla();
            tab_col_importar.setId("tab_col_importar");
            tab_col_importar.setStyle("width:100%;");
            tab_col_importar.setRuta("mbe_index.imt_importar");
            tab_col_importar.setTabla("SIS_CAMPO", "IDE_CAMP", -1);
            tab_col_importar.setCondicion("IDE_CAMP=-1");
            //OCULTO TODOS LOS CAMPOS         
            for (int i = 0; i < tab_col_importar.getTotalColumnas(); i++) {
                tab_col_importar.getColumnas()[i].setVisible(false);
            }
            tab_col_importar.getColumna("nom_camp").setVisible(true);
            tab_col_importar.getColumna("nom_camp").setNombreVisual("CAMPO");
            tab_col_importar.getColumna("nom_camp").setLectura(true);
            tab_col_importar.getColumna("nom_visual_camp").setVisible(true);
            tab_col_importar.getColumna("nom_visual_camp").setNombreVisual("CAMPO DE ARCHIVO");

            tab_col_importar.getColumna("nom_visual_camp").setCombo(lis_campos_excel);
            tab_col_importar.dibujar();

            gri_cuerpo.getChildren().add(tab_col_importar);

            // inserto 
            for (int i = 0; i < tab_tabla.getTotalColumnas(); i++) {
                // if (tab_tabla.getColumnas()[i].isVisible()) {
                if (tab_tabla.isGenerarPrimaria() && (tab_tabla.getColumnas()[i].getNombre().equalsIgnoreCase(tab_tabla.getCampoPrimaria()))) {
                    continue; //para no permitir igualar los campos primarios que se generan automáticamente
                }
                tab_col_importar.insertar();
                tab_col_importar.setValor("NOM_CAMP", tab_tabla.getColumnas()[i].getNombreVisual());
                tab_col_importar.setValor("defecto_camp", tab_tabla.getColumnas()[i].getNombre());

                //Selecciona el combo si coinside con un valor de la tabla
                for (int j = 0; j < int_numColumnas; j++) {
                    try {
                        String str_col = hoja.getCell(j, int_fila_columna).getContents();
                        if (str_col.equalsIgnoreCase(tab_tabla.getColumnas()[i].getNombreVisual()) || str_col.equalsIgnoreCase(tab_tabla.getColumnas()[i].getNombre())) {
                            tab_col_importar.setValor("nom_visual_camp", j + "");
                        }
                    } catch (Exception e) {
                        framework.agregarMensajeError("Fallo al leer columnas del archivo xls :" + e.getMessage(), "");
                    }
                }
                //    }
            }
            upl_importarxls.setNombreReal(event.getFile().getFileName());
            framework.addUpdate("gri_cuerpo_importar");
            //   archivoExcel.close();
        } catch (Exception e) {
            framework.agregarMensajeError("No se puede validar el archivo", e.getMessage());
        }
    }

    public void aceptar() {
       
        if (!tab_col_importar.isEmpty()) {
            //hago los insert 
            Tabla tab_inserta = new Tabla();
            tab_inserta.setConexion(tab_tabla.getConexion());
            try {
                tab_inserta.setTabla(tab_tabla.getTabla(), tab_tabla.getCampoPrimaria(), -1);
                tab_inserta.setCondicion(tab_tabla.getCampoPrimaria() + "=-1");
                tab_inserta.setColumnas(tab_tabla.getColumnas());
                tab_inserta.ejecutarSql();
                for (int fila = int_inicio; fila < int_fin; fila++) {
                    tab_inserta.insertar();
                    for (int col = 0; col < tab_col_importar.getTotalFilas(); col++) {
                        if (tab_col_importar.getValor(col, "nom_visual_camp") != null) {
                            try {
                                String str_valor = hoja.getCell(Integer.parseInt(tab_col_importar.getValor(col, "nom_visual_camp")), fila).getContents();
                                if (hoja.getCell(Integer.parseInt(tab_col_importar.getValor(col, "nom_visual_camp")), fila).getType().equals(CellType.NUMBER)) {
                                    //remplazo las comas por puntos
                                    if (str_valor != null) {
                                        str_valor = str_valor.replace(",", ".");
                                    }
                                }
                                tab_inserta.setValor(tab_col_importar.getValor(col, "defecto_camp"), str_valor);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            tab_inserta.getConexion().getSqlPantalla().clear();
            if (tab_inserta.guardar()) {
                String str_mensaje = tab_inserta.getConexion().guardarPantalla();
                if (str_mensaje.isEmpty()) {
                    framework.agregarMensaje("Se importaron " + tab_inserta.getInsertadas().size() + " registros", "");
                    this.cerrar();
                    tab_tabla.actualizar();
                    tab_tabla.addUpdateArbol();
                } else {
                    framework.agregarMensajeError("No se pudo importar", "Se produjo un error sql al tratar de importar un registro");
                }
            }
        } else {
            framework.agregarMensajeInfo("No se puede Importar", "Debe validar el archivo para asignar los campos");
        }
    }

    @Override
    public void dibujar() {
        upl_importarxls.limpiar();
        tex_columna.setValue("1");
        tex_inicio.setValue("2");
        tex_numero_hoja.setValue("0");
        gri_cuerpo.getChildren().clear();
        gri_cuerpo.getChildren().add(gri_datos);
        gri_cuerpo.setStyle("width:" + (getAnchoPanel() - 10) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        super.dibujar();
    }

    public Tabla getTab_col_importar() {
        return tab_col_importar;
    }

    public void setTab_col_importar(Tabla tab_col_importar) {
        this.tab_col_importar = tab_col_importar;
    }

    public Upload getUpl_importarxls() {
        return upl_importarxls;
    }

    public void setUpl_importarxls(Upload upl_importarxls) {
        this.upl_importarxls = upl_importarxls;
    }
}
