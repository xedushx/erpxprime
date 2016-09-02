/*
 * Copyright (c) 2013, xedushx Fernando Jácome. All rights reserved.
 */
package ec.com.erpxprime.framework.componentes.graficos;

import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.ItemMenu;
import ec.com.erpxprime.framework.componentes.Tabla;
import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.component.chart.line.LineChart;
import org.primefaces.component.contextmenu.ContextMenu;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.separator.Separator;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author xedushx Fernando Jácome
 */
public class GraficoCartesiano extends Panel {

    private CartesianChartModel modelo = new CartesianChartModel();

    private LineChart graficoLinea = new LineChart();
    private BarChart graficoBarra = new BarChart();

    private ContextMenu menuGrafico = new ContextMenu();

    public GraficoCartesiano() {

        graficoLinea.setLegendPosition("e");
        graficoLinea.setValue(modelo);
        graficoLinea.setAnimate(true);
        graficoLinea.setRendered(true);

        graficoBarra.setLegendPosition("e");
        graficoBarra.setValue(modelo);
        graficoBarra.setAnimate(true);
        graficoBarra.setRendered(false);

        this.getChildren().add(graficoBarra);
        this.getChildren().add(graficoLinea);

    }

    public void setTitulo(String titulo) {
        if (titulo != null && titulo.isEmpty() == false) {
            graficoLinea.setTitle(titulo);
            graficoBarra.setTitle(titulo);
        }
    }

    private void agregarMenuContextual() {
        if (modelo.getSeries().isEmpty() == true) {
            menuGrafico.setId("menug_" + this.getId());
            menuGrafico.setFor(this.getId());
            menuGrafico.getChildren().clear();
            ItemMenu itm_linea = new ItemMenu();
            itm_linea.setValue("Grafico Lineal");
            itm_linea.setIcon("ui-icon-shuffle");
            itm_linea.setMetodoRuta("mbe_index.clase." + this.getId() + ".dibujarLineas");
            menuGrafico.getChildren().add(itm_linea);

            ItemMenu itm_barra = new ItemMenu();
            itm_barra.setValue("Grafico de Barras");
            itm_barra.setIcon("ui-icon-bookmark");
            itm_barra.setMetodoRuta("mbe_index.clase." + this.getId() + ".dibujarBarras");
            menuGrafico.getChildren().add(itm_barra);
            Separator separar = new Separator();

//            ItemMenu itm_exporta = new ItemMenu();
//            itm_exporta.setValue("Exportar");
//            itm_exporta.setIcon("ui-icon-print");
//            menuGrafico.getChildren().add(itm_exporta);
            menuGrafico.getChildren().add(separar);

            ItemMenu itm_opciones = new ItemMenu();
            itm_opciones.setValue("Configuraciones");
            itm_opciones.setIcon("ui-icon-wrench");
            menuGrafico.getChildren().add(itm_opciones);
            this.getChildren().add(menuGrafico);
        }

    }

    public void dibujarLineas() {
        graficoLinea.setRendered(true);
        graficoBarra.setRendered(false);
        Framework framework = new Framework();
        framework.addUpdate(this.getId());
    }

    public void dibujarBarras() {
        graficoLinea.setRendered(false);
        graficoBarra.setRendered(true);
        Framework framework = new Framework();
        framework.addUpdate(this.getId());
    }

    /**
     * Agrega una serie al grafico
     *
     * @param tabla
     * @param columnaX nombre columna eje X
     * @param columnaY nombre columna eje Y
     * @param label Etiqueta
     */
    public void agregarSerie(TablaGenerica tabla, String columnaX, String columnaY, String label) {
        agregarMenuContextual();
//grafico una sola linea
        ChartSeries serie = new ChartSeries();
        serie.setLabel(label);

        for (int i = 0; i < tabla.getTotalFilas(); i++) {
            serie.set(tabla.getValor(i, columnaX), ((Number) tabla.getValorObjeto(i, columnaY)));
        }
        modelo.addSeries(serie);
    }

    /**
     * Agrega una serie al grafico
     *
     * @param tabla
     * @param columnaX nombre columna eje X
     * @param columnaY nombre columna eje Y
     * @param label Etiqueta
     */
    public void agregarSerie(Tabla tabla, String columnaX, String columnaY, String label) {
        agregarMenuContextual();
        //grafico una sola linea
        ChartSeries serie = new ChartSeries();
        serie.setLabel(label);
        eliminarSerie(label); //elimina para que no existan series con el mismo label
        for (int i = 0; i < tabla.getTotalFilas(); i++) {
            serie.set(tabla.getValor(i, columnaX), ((Number) tabla.getValorObjeto(i, columnaY)));
        }
        modelo.addSeries(serie);
    }

    public void limpiar() {
        modelo.getSeries().clear();

    }

    public void eliminarSerie(String label) {
        for (int i = 0; i < modelo.getSeries().size(); i++) {
            if (modelo.getSeries().get(i).getLabel().equals(label)) {
                modelo.getSeries().remove(i);
                break;
            }
        }
    }

    public CartesianChartModel getModelo() {
        return modelo;
    }

    public void setModelo(CartesianChartModel modelo) {
        this.modelo = modelo;
    }

    public LineChart getGraficoLinea() {
        return graficoLinea;
    }

    public void setGraficoLinea(LineChart graficoLinea) {
        this.graficoLinea = graficoLinea;
    }

    public BarChart getGraficoBarra() {
        return graficoBarra;
    }

    public void setGraficoBarra(BarChart graficoBarra) {
        this.graficoBarra = graficoBarra;
    }

    public ContextMenu getMenuGrafico() {
        return menuGrafico;
    }

    public void setMenuGrafico(ContextMenu menuGrafico) {
        this.menuGrafico = menuGrafico;
    }

}
