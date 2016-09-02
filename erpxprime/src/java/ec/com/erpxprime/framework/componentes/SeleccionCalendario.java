/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.util.Date;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class SeleccionCalendario extends Dialogo {

    private boolean multiple = true;
    private String ruta = "mbe_index.clase";
    private Grid gri_cuerpo = new Grid();
    private Etiqueta eti_fecha1 = new Etiqueta();
    private Etiqueta eti_fecha2 = new Etiqueta();
    private Calendario cal_fecha1 = new Calendario();
    private Calendario cal_fecha2 = new Calendario();    
    private BotonesCombo boc_hoy = new BotonesCombo();
    private boolean boo_dibujo = false;

    public SeleccionCalendario() {
        this.setWidth("25%");
        this.setHeight("25%");
        this.setResizable(false);
        this.setDialogo(gri_cuerpo);
        //  gri_cuerpo.setWidth("100%");

        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        cal_fecha1.setTipoBoton(true);
        cal_fecha2.setTipoBoton(true);
        gri.getChildren().add(eti_fecha1);
        gri.getChildren().add(cal_fecha1);
        gri.getChildren().add(eti_fecha2);
        gri.getChildren().add(cal_fecha2);
        gri_cuerpo.getChildren().add(boc_hoy);
        gri_cuerpo.getChildren().add(gri);
        this.setDialogo(gri_cuerpo);
        this.setMultiple(true);

    }

    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public void dibujar() {
        if (boo_dibujo == false) {
            gri_cuerpo.setId("gri_cuerpo_" + this.getId());
            boc_hoy.setValue("Solo el día de Hoy");
            boc_hoy.setIcon("ui-icon-calendar");
            boc_hoy.setMetodoRuta(ruta + "." + this.getId() + ".seleccinarHoy");
            ItemMenu itm_este_mes = new ItemMenu();
            itm_este_mes.setValue("Solo este mes");
            itm_este_mes.setIcon("ui-icon-calendar");
            itm_este_mes.setMetodoRuta(ruta + "." + this.getId() + ".seleccinarMes");
            boc_hoy.agregarBoton(itm_este_mes);
            boo_dibujo = true;

        }
        gri_cuerpo.setStyle("height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        super.dibujar();
    }

    public void seleccinarHoy() {
        cal_fecha1.setValue(framework.getFecha(framework.getFechaActual()));
        cal_fecha2.setValue(framework.getFecha(framework.getFechaActual()));
        framework.addUpdate(gri_cuerpo.getId());
    }

    public void seleccinarMes() {
        String fecha_inicio = framework.getAnio(framework.getFechaActual()) + "-" + framework.getMes(framework.getFechaActual()) + "-1";
        cal_fecha1.setValue(framework.getFecha(fecha_inicio));
        cal_fecha2.setValue(framework.getFecha(framework.getUltimaFechaMes(framework.getFechaActual())));
        framework.addUpdate(gri_cuerpo.getId());
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
        cal_fecha2.setRendered(multiple);
        eti_fecha2.setRendered(multiple);
        boc_hoy.setRendered(multiple);
        if (multiple) {
            this.setHeader("Rango de Fechas");
            eti_fecha1.setValue("Fecha inicial:");
            eti_fecha2.setValue("Fecha final:");
        } else {
            eti_fecha1.setValue("Fecha :");
            this.setHeader("Seleccionar Fecha");
        }
    }

    public void setFechaActual() {
        cal_fecha1.setFechaActual();
        cal_fecha2.setFechaActual();
    }

    public void setFecha1(Date fecha) {
        cal_fecha1.setValue(fecha);
    }

    public void setFecha2(Date fecha) {
        cal_fecha2.setValue(fecha);
    }

    public void setLimiteRango(String fecha_inicial, String fecha_final) {
        cal_fecha2.setMindate(framework.getFecha(framework.getFormatoFecha(fecha_inicial)));
        cal_fecha1.setMaxdate(framework.getFecha(framework.getFormatoFecha(fecha_final)));
        cal_fecha2.setMaxdate(framework.getFecha(framework.getFormatoFecha(fecha_final)));
    }

    public void setRangoFechas(String fecha_inicial, String fecha_final) {
        cal_fecha1.setValue(framework.getFecha(framework.getFormatoFecha(fecha_inicial)));
        cal_fecha2.setValue(framework.getFecha(framework.getFormatoFecha(fecha_final)));
    }

    public void setTipoCalendarioHora() {
        cal_fecha1.setTipoCalendarioHora();
        cal_fecha2.setTipoCalendarioHora();
    }

    public boolean isFechasValidas() {
        if (isMultiple()) {
            if ((getFecha1String() != null && framework.isFechaValida(getFecha1String())) && (getFecha2String() != null && framework.isFechaValida(getFecha2String()))) {
                //comparo que fecha2 es mayor a fecha1
                if (framework.isFechaMayor(getFecha2(), getFecha1()) || getFecha1().equals(getFecha2())) {
                    return true;
                }
            }
        } else {
            if (getFecha1String() != null && framework.isFechaValida(getFecha1String())) {
                return true;
            }
        }
        return false;
    }

    public String getFecha1String() {
        return framework.getFormatoFecha(cal_fecha1.getValue());
    }

    public String getFecha2String() {
        return framework.getFormatoFecha(cal_fecha2.getValue());
    }

    public Date getFecha1() {
        return (Date) cal_fecha1.getValue();
    }

    public Date getFecha2() {
        return (Date) cal_fecha2.getValue();
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Calendario getCal_fecha1() {
        return cal_fecha1;
    }

    public void setCal_fecha1(Calendario cal_fecha1) {
        this.cal_fecha1 = cal_fecha1;
    }

    public Calendario getCal_fecha2() {
        return cal_fecha2;
    }

    public void setCal_fecha2(Calendario cal_fecha2) {
        this.cal_fecha2 = cal_fecha2;
    }
}
