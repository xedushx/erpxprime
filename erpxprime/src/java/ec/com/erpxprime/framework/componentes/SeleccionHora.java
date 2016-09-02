/*
 * Copyright (c) 2012, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class SeleccionHora extends Dialogo {

    private boolean multiple = true;
    private Grid gri_cuerpo = new Grid();
    private Etiqueta eti_hora1 = new Etiqueta();
    private Etiqueta eti_hora2 = new Etiqueta();
    private Hora hor_hora1 = new Hora();
    private Hora hor_hora2 = new Hora();

    public SeleccionHora() {
        this.setWidth("23%");
        this.setHeight("25%");
        this.setResizable(false);
        this.setDialogo(gri_cuerpo);
        //  gri_cuerpo.setWidth("100%");

        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        hor_hora1.setTipoBoton(true);
        hor_hora2.setTipoBoton(true);
        gri.getChildren().add(eti_hora1);
        gri.getChildren().add(hor_hora1);
        gri.getChildren().add(eti_hora2);
        gri.getChildren().add(hor_hora2);
        gri_cuerpo.getChildren().add(gri);
        this.setDialogo(gri_cuerpo);
        this.setMultiple(true);
    }

    public boolean isMultiple() {
        return multiple;
    }

    public boolean isHorasValidas() {
        if (isMultiple()) {
            if ((getHora1String() != null && framework.isHoraValida(getHora1String())) && (getHora2String() != null && framework.isHoraValida(getHora2String()))) {
                //comparo que fecha2 es mayor a fecha1
                if (framework.isFechaMayor(getHora2(), getHora1()) || getHora1().equals(getHora2())) {
                    return true;
                }
            }
        } else {
            if (getHora1String() != null && framework.isHoraValida(getHora1String())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dibujar() {
        gri_cuerpo.setStyle("height:" + getAltoPanel() + "px;overflow: auto;display: block;");
        super.dibujar();
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
        hor_hora2.setRendered(multiple);
        eti_hora2.setRendered(multiple);
        if (multiple) {
            this.setHeader("Rango de Horas");
            eti_hora1.setValue("Hora inicial:");
            eti_hora2.setValue("Hora final:");
        } else {
            eti_hora1.setValue("Hora :");
            this.setHeader("Seleccionar Hora");
        }
    }

    public void setFechaActual() {
        hor_hora1.setHoraActual();
        hor_hora2.setHoraActual();
    }

    public void setHora1(Date hora) {
        hor_hora1.setValue(hora);
    }

    public void setHora2(Date hora) {
        hor_hora2.setValue(hora);
    }

    public String getHora1String() {
        return framework.getFormatoHora(hor_hora1.getValue());
    }

    public String getHora2String() {
        return framework.getFormatoHora(hor_hora2.getValue());
    }

    public Date getHora1() {
        return (Date) hor_hora1.getValue();
    }

    public Date getHora2() {
        return (Date) hor_hora2.getValue();
    }

    public Time getTime1() {     
        return new Time(((Date) hor_hora1.getValue()).getTime());
    }

    public Time getTime2() {
        return new Time(((Date) hor_hora2.getValue()).getTime());
    }

}
