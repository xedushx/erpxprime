/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes.graficos;

import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.Tabla;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author xedushx
 */
public class GraficoPastel extends PieChart {
    
    private PieChartModel modelo = new PieChartModel();
    
    public GraficoPastel() {
        this.setValue(modelo);
        this.setLegendPosition("e");
    }
    
    public void agregarSerie(TablaGenerica tabla, String columnaNombre, String columnaValor) {
        modelo.clear();
        for (int i = 0; i < tabla.getTotalFilas(); i++) {
            modelo.set(tabla.getValor(i, columnaNombre), ((Number) tabla.getValorObjeto(i, columnaValor)));
        }
    }
    
    public void agregarSerie(Tabla tabla, String columnaNombre, String columnaValor) {
        modelo.clear();
        for (int i = 0; i < tabla.getTotalFilas(); i++) {
            modelo.set(tabla.getValor(i, columnaNombre), ((Number) tabla.getValorObjeto(i, columnaValor)));
        }
    }
    
    public void setTitulo(String titulo) {
        this.setTitle(titulo);
    }
    
    public void limpiar() {
        modelo.clear();
    }
    
    public PieChartModel getModelo() {
        return modelo;
    }
    
    public void setModelo(PieChartModel modelo) {
        this.modelo = modelo;
    }
    
}
