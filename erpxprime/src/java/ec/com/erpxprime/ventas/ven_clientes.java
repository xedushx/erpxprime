/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.ventas;

import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.Arbol;
import ec.com.erpxprime.framework.componentes.AutoCompletar;
import ec.com.erpxprime.framework.componentes.Boton;
import ec.com.erpxprime.framework.componentes.Calendario;
import ec.com.erpxprime.framework.componentes.Combo;
import ec.com.erpxprime.framework.componentes.Division;
import ec.com.erpxprime.framework.componentes.Etiqueta;
import ec.com.erpxprime.framework.componentes.Grid;
import ec.com.erpxprime.framework.componentes.Grupo;
import ec.com.erpxprime.framework.componentes.MenuPanel;
import ec.com.erpxprime.framework.componentes.PanelArbol;
import ec.com.erpxprime.framework.componentes.PanelTabla;
import ec.com.erpxprime.framework.componentes.Tabla;
import ec.com.erpxprime.framework.componentes.graficos.GraficoCartesiano;
import ec.com.erpxprime.servicios.contabilidad.ServicioContabilidadGeneral;
import ec.com.erpxprime.servicios.cuentas_x_cobrar.ServicioCliente;
import ec.com.erpxprime.servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import ec.com.erpxprime.sistema.aplicacion.Pantalla;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class ven_clientes extends Pantalla {

    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);

    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_clientes = new AutoCompletar();

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    private Tabla tab_cliente; //formulario cliente
    private Tabla tab_contactos; //formulario de contactos
    private Tabla tab_transacciones_cxc; //transacciones cliente
    private Tabla tab_compra_productos;
    private Tabla tab_productos; //transacciones cliente
    private Tabla tab_facturas; //Facturas pendientes

    private Arbol arb_estructura;// Estructura Gerarquica de clientes

    /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    private AutoCompletar aut_cuentas;
    private Tabla tab_movimientos; //movimientos contables

    /*INFOMRES*/
    private Tabla tab_grafico;
    private GraficoCartesiano gca_grafico;
    private Combo com_periodo;

    public ven_clientes() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("CLIENTE :"));
        aut_clientes.setId("aut_clientes");
        aut_clientes.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_clientes.setSize(75);
        aut_clientes.setAutocompletarContenido(); // no startWith para la busqueda
        aut_clientes.setMetodoChangeRuta("mbe_index.clase.seleccionarCliente");

        bar_botones.agregarComponente(aut_clientes);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES CLIENTE", "20%");
        mep_menu.agregarItem("Información Cliente", "dibujarCliente", "ui-icon-person");
        mep_menu.agregarItem("Clasificación Clientes", "dibujarEstructura", "ui-icon-arrow-4-diag");
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Transacciones Cliente", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Productos Cliente", "dibujarProductos", "ui-icon-cart");
        mep_menu.agregarItem("Facturas Por Cobrar", "dibujarFacturas", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Gráfico de Ventas", "dibujarGrafico", "ui-icon-bookmark");
        mep_menu.agregarItem("Productos Vendidos", "dibujarProductosVendidos", "ui-icon-cart");

        agregarComponente(mep_menu);
    }

    /**
     * Selecciona un cliente en el autocompletar
     *
     * @param evt
     */
    public void seleccionarCliente(SelectEvent evt) {
        aut_clientes.onSelect(evt);
        if (aut_clientes != null) {
            switch (mep_menu.getOpcion()) {
                case 1:
                    dibujarCliente();
                    break;
                case 2:
                    dibujarTransacciones();
                    break;
                case 3:
                    dibujarProductos();
                    break;
                case 4:
                    dibujarConfiguraCuenta();
                    break;
                case 5:
                    dibujarMovimientos();
                    break;
                case 6:
                    dibujarFacturas();
                    break;
                case 7:
                    dibujarEstructura();
                    break;
                case 8:
                    dibujarGrafico();
                    break;
                case 9:
                    dibujarProductosVendidos();
                    break;
                default:
                    dibujarCliente();
            }
        } else {
            limpiar();
        }
    }

    /**
     * Dibuja el formulario de datos del Cliente, osigna opcion 1
     */
    public void dibujarCliente() {
        tab_cliente = new Tabla();
        tab_contactos = new Tabla();

        tab_cliente.setId("tab_cliente");
        ser_cliente.configurarTablaCliente(tab_cliente);
        tab_cliente.setTabla("ven_cliente", "cli_codigo", 1);
        tab_cliente.setCondicion("cli_codigo=" + aut_clientes.getValor());
        tab_cliente.setMostrarNumeroRegistros(false);
        tab_cliente.agregarRelacion(tab_contactos);
        tab_cliente.dibujar();

        tab_contactos.setId("tab_contactos");
        tab_contactos.setTabla("gen_contacto", "con_codigo", 1);
        ser_cliente.configurarTablaContactos(tab_contactos);
        tab_contactos.setCondicion("cli_codigo=" + aut_clientes.getValor());
        tab_contactos.setMostrarNumeroRegistros(false);
        tab_contactos.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_cliente);

        PanelTabla pat_panel_contactos = new PanelTabla();
        pat_panel_contactos.setPanelTabla(tab_contactos);

        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel_contactos.getMenuTabla().getItem_buscar().setRendered(false);

        Division div_division_contacto = new Division();
        div_division_contacto.dividir2(pat_panel, pat_panel_contactos, "70%", "H");

        mep_menu.dibujar(1, "DATOS DEL CLIENTE", div_division_contacto);
    }

    public void dibujarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {

            Fieldset fis_consulta = new Fieldset();
            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarTransacciones");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);

            Separator separar = new Separator();
            fis_consulta.getChildren().add(separar);

            tab_transacciones_cxc = new Tabla();
            tab_transacciones_cxc.setNumeroTabla(-1);
            tab_transacciones_cxc.setId("tab_transacciones_cxc");
            tab_transacciones_cxc.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_transacciones_cxc.getColumna("IDE_TECLB").setVisible(false);
            tab_transacciones_cxc.getColumna("FECHA_TRANS_CCDTR").setNombreVisual("FECHA");
            tab_transacciones_cxc.getColumna("IDE_CCDTR").setVisible(false);
            tab_transacciones_cxc.getColumna("NUMERO_PAGO_CCDTR").setVisible(false);
            tab_transacciones_cxc.getColumna("INGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("EGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("IDE_CNCCC").setFiltro(true);
            tab_transacciones_cxc.getColumna("IDE_CNCCC").setNombreVisual("N. COMP");
            tab_transacciones_cxc.getColumna("TRANSACCION").setFiltroContenido();
            tab_transacciones_cxc.getColumna("DOCUM_RELAC_CCDTR").setFiltroContenido();
            tab_transacciones_cxc.getColumna("DOCUM_RELAC_CCDTR").setNombreVisual("N. DOCUMENTO");
            tab_transacciones_cxc.getColumna("INGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("EGRESOS").alinearDerecha();
            tab_transacciones_cxc.getColumna("INGRESOS").setLongitud(20);
            tab_transacciones_cxc.getColumna("EGRESOS").setLongitud(20);
            tab_transacciones_cxc.getColumna("saldo").setLongitud(25);
            tab_transacciones_cxc.getColumna("saldo").alinearDerecha();
            tab_transacciones_cxc.getColumna("saldo").setEstilo("font-weight: bold;");
            tab_transacciones_cxc.setColumnaSuma("INGRESOS,EGRESOS,saldo");
            tab_transacciones_cxc.getColumna("INGRESOS").setSuma(false);
            tab_transacciones_cxc.getColumna("EGRESOS").setSuma(false);
            tab_transacciones_cxc.getColumna("saldo").setSuma(false);
            tab_transacciones_cxc.setOrdenar(false);
            tab_transacciones_cxc.setLectura(true);
            tab_transacciones_cxc.setScrollable(true);
            tab_transacciones_cxc.setRows(20);
            tab_transacciones_cxc.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_transacciones_cxc);
            gru_grupo.getChildren().add(pat_panel);

            actualizarSaldoxCobrar();
        }
        mep_menu.dibujar(2, "TRANSACCIONES DEL CLIENTE", gru_grupo);
    }

    public void dibujarProductos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {

            Fieldset fis_consulta = new Fieldset();

            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarProductos");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);
            tab_productos = new Tabla();
            tab_productos.setNumeroTabla(-1);
            tab_productos.setId("tab_productos");
            tab_productos.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_productos.getColumna("ide_ccdfa").setVisible(false);
            tab_productos.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
            tab_productos.getColumna("serie_ccdaf").setNombreVisual("N. SERIE");
            tab_productos.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_productos.getColumna("nombre_inarti").setNombreVisual("ARTICULO");
            tab_productos.getColumna("cantidad_ccdfa").setNombreVisual("CANTIDAD");
            tab_productos.getColumna("precio_ccdfa").setNombreVisual("PRECIO UNI.");
            tab_productos.getColumna("total_ccdfa").setNombreVisual("TOTAL");
            tab_productos.getColumna("precio_ccdfa").alinearDerecha();
            tab_productos.getColumna("total_ccdfa").alinearDerecha();
            tab_productos.getColumna("total_ccdfa").setEstilo("font-weight: bold");
            tab_productos.getColumna("nombre_inarti").setFiltroContenido();
            tab_productos.getColumna("secuencial_cccfa").setFiltroContenido();

            tab_productos.setLectura(true);
            tab_productos.setRows(20);
            tab_productos.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_productos);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(3, "DETALLE DE PRODUCTOS VENDIDOS AL CLIENTE", gru_grupo);
    }

    public void dibujarConfiguraCuenta() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            aut_cuentas = new AutoCompletar();
            aut_cuentas.setId("aut_cuentas");
            aut_cuentas.setAutoCompletar(ser_contabilidad.getSqlCuentasActivos());
            aut_cuentas.setSize(75);
            aut_cuentas.setDisabled(true);
            aut_cuentas.setMetodoChange("seleccionarCuentaContable");
            aut_cuentas.setValor(ser_cliente.getCuentaCliente(aut_clientes.getValor()));

            Grid gri_contenido = new Grid();
            gri_contenido.setColumns(3);
            gri_contenido.getChildren().add(new Etiqueta("<strong>CUENTA CONTABLE : </strong>"));
            gri_contenido.getChildren().add(aut_cuentas);
            Boton bot_cambia = new Boton();
            bot_cambia.setValue("Cambiar");
            bot_cambia.setIcon("ui-icon-refresh");
            bot_cambia.setMetodo("activaCambiarCuenta");
            gri_contenido.getChildren().add(bot_cambia);
            gru_grupo.getChildren().add(gri_contenido);
            gru_grupo.getChildren().add(new Etiqueta("<p style='padding-top:10px;'><strong >NOTA: </strong> La cuenta contable seleccionada se relacionará a los movimientos-transacciones que realice el Cliente, a partir que se <strong>guarde </strong> el cambio. </p>"));
        }
        mep_menu.dibujar(4, "CONFIGURACIÓN DE CUENTA CONTABLE", gru_grupo);
    }

    public void dibujarMovimientos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            TablaGenerica tab_cuenta = ser_contabilidad.getCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()));
            if (!tab_cuenta.isEmpty()) {

                Fieldset fis_consulta = new Fieldset();
                fis_consulta.getChildren().add(new Etiqueta("<p style='font-size:16px;padding-bottom:5px;'> <strong>" + tab_cuenta.getValor("codig_recur_cndpc") + "</strong> &nbsp; " + tab_cuenta.getValor("nombre_cndpc") + "</p>"));

                Grid gri_fechas = new Grid();
                gri_fechas.setColumns(5);
                gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
                cal_fecha_inicio = new Calendario();
                cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
                gri_fechas.getChildren().add(cal_fecha_inicio);
                gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
                cal_fecha_fin = new Calendario();
                cal_fecha_fin.setFechaActual();
                gri_fechas.getChildren().add(cal_fecha_fin);
                fis_consulta.getChildren().add(gri_fechas);

                Boton bot_consultar = new Boton();
                bot_consultar.setValue("Consultar");
                bot_consultar.setMetodo("actualizarMovimientos");
                bot_consultar.setIcon("ui-icon-search");

                gri_fechas.getChildren().add(bot_consultar);

                Separator separar = new Separator();
                fis_consulta.getChildren().add(separar);

                gru_grupo.getChildren().add(fis_consulta);

                tab_movimientos = new Tabla();
                tab_movimientos.setNumeroTabla(-1);
                tab_movimientos.setId("tab_movimientos");
                tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                tab_movimientos.setLectura(true);
                tab_movimientos.getColumna("ide_cnccc").setNombreVisual("N. COMP.");
                tab_movimientos.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
                tab_movimientos.getColumna("ide_cnlap").setVisible(false);
                tab_movimientos.getColumna("debe").setLongitud(20);
                tab_movimientos.getColumna("haber").setLongitud(20);
                tab_movimientos.getColumna("saldo").setLongitud(25);
                tab_movimientos.getColumna("debe").alinearDerecha();
                tab_movimientos.getColumna("haber").alinearDerecha();
                tab_movimientos.getColumna("saldo").alinearDerecha();
                tab_movimientos.getColumna("saldo").setEstilo("font-weight: bold;");
                tab_movimientos.getColumna("valor_cndcc").setVisible(false);
                tab_movimientos.getColumna("debe").setSuma(false);
                tab_movimientos.getColumna("haber").setSuma(false);
                tab_movimientos.getColumna("saldo").setSuma(false);
                tab_movimientos.setColumnaSuma("debe,haber,saldo");
                tab_movimientos.setRows(20);
                tab_movimientos.setOrdenar(false);
                tab_movimientos.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_movimientos);
                gru_grupo.getChildren().add(pat_panel);
                actualizarSaldosContable();
            } else {
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El cliente seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
    }

    public void dibujarFacturas() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            tab_facturas = new Tabla();
            tab_facturas.setNumeroTabla(-1);
            tab_facturas.setId("tab_facturas");
            tab_facturas.setSql(ser_cliente.getSqlFacturasPorCobrar(aut_clientes.getValor()));
            tab_facturas.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
            tab_facturas.getColumna("saldo_x_pagar").alinearDerecha();
            tab_facturas.setCampoPrimaria("ide_ccctr");
            tab_facturas.getColumna("ide_ccctr").setVisible(false);
            tab_facturas.getColumna("fecha").setVisible(true);
            tab_facturas.getColumna("serie_ccdaf").setNombreVisual("SERIE");
            tab_facturas.getColumna("secuencial_cccfa").setNombreVisual("N. FACTURA");
            tab_facturas.getColumna("secuencial_cccfa").setFiltroContenido();
            tab_facturas.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
            tab_facturas.getColumna("total_cccfa").setNombreVisual("TOTAL");
            tab_facturas.getColumna("total_cccfa").setEstilo("font-size: 13px;");
            tab_facturas.getColumna("total_cccfa").alinearDerecha();
            tab_facturas.setRows(20);
            tab_facturas.setLectura(true);
            tab_facturas.setColumnaSuma("saldo_x_pagar");
            tab_facturas.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_facturas);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_facturas.isEmpty()) {
                tab_facturas.setEmptyMessage("El cliente no tiene facturas por pagar");
            }
        }
        mep_menu.dibujar(6, "FACTURAS POR COBRAR AL CLIENTE", gru_grupo);
    }

    public void dibujarEstructura() {
        Grupo gru_grupo = new Grupo();
        arb_estructura = new Arbol();
        arb_estructura.setId("arb_estructura");
        arb_estructura.setArbol("ven_cliente", "cli_codigo", "cli_razon_social", "cli_codigo_2");
        arb_estructura.dibujar();
        //Selecciona el nodo
        if (aut_clientes.getValor() != null) {
            arb_estructura.seleccinarNodo(aut_clientes.getValor());
            arb_estructura.getNodoSeleccionado().setExpanded(true);
            arb_estructura.getNodoSeleccionado().getParent().setExpanded(true);
        }
        PanelArbol paa_panel = new PanelArbol();
        paa_panel.setPanelArbol(arb_estructura);
        gru_grupo.getChildren().add(paa_panel);
        mep_menu.dibujar(7, "CLASIFICACIÓN DE CLIENTES", gru_grupo);
    }

    public void agregarPadre() {
        if (mep_menu.getOpcion() == 7) {
            TablaGenerica tab_padre_cliente = new TablaGenerica();
            tab_padre_cliente.setTabla("ven_cliente", "cli_codigo");
            tab_padre_cliente.setCondicion("cli_codigo=-1");
            tab_padre_cliente.ejecutarSql();
        }
    }

    public void dibujarGrafico() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            gca_grafico = new GraficoCartesiano();
            gca_grafico.setId("gca_grafico");

            com_periodo = new Combo();
            com_periodo.setMetodo("actualizarGrafico");
            com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
            com_periodo.eliminarVacio();
            com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

            tab_grafico = new Tabla();
            tab_grafico.setId("tab_grafico");
            tab_grafico.setSql(ser_cliente.getSqlTotalVentasMensualesCliente(aut_clientes.getValor(), String.valueOf(com_periodo.getValue())));
            tab_grafico.setLectura(true);
            tab_grafico.setColumnaSuma("num_facturas,ventas12,ventas0,iva,total");
            tab_grafico.getColumna("num_facturas").alinearDerecha();
            tab_grafico.getColumna("ventas12").alinearDerecha();
            tab_grafico.getColumna("ventas0").alinearDerecha();
            tab_grafico.getColumna("iva").alinearDerecha();
            tab_grafico.getColumna("total").alinearDerecha();
            tab_grafico.dibujar();

            Grid gri_opciones = new Grid();
            gri_opciones.setColumns(2);
            gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
            gri_opciones.getChildren().add(com_periodo);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.getChildren().add(gri_opciones);
            pat_panel.setPanelTabla(tab_grafico);

            gca_grafico.setTitulo("VENTAS MENSUALES");
            gca_grafico.agregarSerie(tab_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));

            gru_grupo.getChildren().add(pat_panel);
            gru_grupo.getChildren().add(gca_grafico);
        }
        mep_menu.dibujar(8, "GRAFICO DE VENTAS", gru_grupo);
    }

    public void dibujarProductosVendidos() {
        Grupo gru_grupo = new Grupo();
        if (isClienteSeleccionado()) {
            tab_compra_productos = new Tabla();
            tab_compra_productos.setId("tab_compra_productos");
            tab_compra_productos.setSql(ser_cliente.getSqlProductosVendidos(aut_clientes.getValor()));
            tab_compra_productos.getColumna("ide_inarti").setVisible(false);
            tab_compra_productos.setRows(25);
            tab_compra_productos.getColumna("nombre_inarti").setNombreVisual("PRODUCTO");
            tab_compra_productos.getColumna("nombre_inarti").setFiltroContenido();
            tab_compra_productos.getColumna("nombre_inarti").setLongitud(200);
            tab_compra_productos.getColumna("fecha_ultima_venta").setNombreVisual("FECHA ULTIMA VENTA");
            tab_compra_productos.getColumna("fecha_ultima_venta").setLongitud(100);
            tab_compra_productos.getColumna("ultimo_precio").setNombreVisual("PRECIO ULTIMA VENTA");
            tab_compra_productos.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_compra_productos.getColumna("ultimo_precio").alinearDerecha();
            tab_compra_productos.getColumna("ultimo_precio").setLongitud(100);
            tab_compra_productos.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");
            tab_compra_productos.setLectura(true);
            tab_compra_productos.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_compra_productos);
            gru_grupo.getChildren().add(pat_panel1);

        }
        mep_menu.dibujar(9, "PRODUCTOS VENDIDOS", gru_grupo);
    }

    public void actualizarGrafico() {
        tab_grafico.setSql(ser_cliente.getSqlTotalVentasMensualesCliente(aut_clientes.getValor(), String.valueOf(com_periodo.getValue())));
        tab_grafico.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
        utilitario.addUpdate("gca_grafico");
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_movimientos.setSql(ser_contabilidad.getSqlMovimientosCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_movimientos.ejecutarSql();
        actualizarSaldosContable();
    }

    /**
     * Actualiza las transacciones de un cliente segun las fechas selecionadas
     */
    public void actualizarTransacciones() {
        tab_transacciones_cxc.setSql(ser_cliente.getSqlTransaccionesCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_transacciones_cxc.ejecutarSql();
        actualizarSaldoxCobrar();
    }

    /**
     * Actualiza los productos comprados del cliente segun las fechas
     * selecionadas
     */
    public void actualizarProductos() {
        tab_productos.setSql(ser_cliente.getSqlProductosCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_productos.ejecutarSql();
        if (tab_productos.isEmpty()) {

            tab_productos.setEmptyMessage("No existen Productos en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza los solados que se visualizan en pantalla
     */
    private void actualizarSaldosContable() {

        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(ser_cliente.getCuentaCliente(aut_clientes.getValor()), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_debe = 0;
        double dou_haber = 0;
        String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");

        for (int i = 0; i < tab_movimientos.getTotalFilas(); i++) {
            if (tab_movimientos.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_movimientos.setValor(i, "debe", utilitario.getFormatoNumero(tab_movimientos.getValor(i, "valor_cndcc")));
                dou_debe += Double.parseDouble(tab_movimientos.getValor(i, "debe"));

            } else {
                tab_movimientos.setValor(i, "haber", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_movimientos.getValor(i, "valor_cndcc")))));
                dou_haber += Double.parseDouble(tab_movimientos.getValor(i, "haber"));
            }
            dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_movimientos.getValor(i, "valor_cndcc"));
            tab_movimientos.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));

            saldo_anterior = dou_saldo_actual;
        }
        if (tab_movimientos.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_movimientos.setEmptyMessage("No existen Movimientos Contables en el rango de fechas seleccionado");
        }

        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_movimientos.setLectura(false);
            tab_movimientos.insertar();
            tab_movimientos.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_movimientos.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_movimientos.setValor("fecha_trans_cnccc", cal_fecha_inicio.getFecha());
            tab_movimientos.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_movimientos.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_movimientos.getColumna("debe").setTotal(dou_debe);
        tab_movimientos.getColumna("haber").setTotal(dou_haber);
    }

    /**
     * *
     * Activa el Autocompletar para cambiar la cuenta contable
     */
    public void activaCambiarCuenta() {
        if (isClienteSeleccionado()) {
            aut_cuentas.setDisabled(false);
            utilitario.addUpdate("aut_cuentas");
        }
    }

    /**
     * Selecciona una nueva cuenta contable, y agrega el SQL para que pueda ser
     * guardado
     *
     * @param evt
     */
    public void seleccionarCuentaContable(SelectEvent evt) {
        aut_cuentas.onSelect(evt);
        if (isClienteSeleccionado()) {
            if (aut_cuentas.getValor() != null) {
                utilitario.getConexion().getSqlPantalla().clear();
                if (ser_cliente.isTieneCuentaConfiguradaCliente(aut_clientes.getValor()) == false) {
                    //nueva
                    utilitario.getConexion().agregarSqlPantalla(ser_cliente.getSqlInsertarCuentaCliente(aut_clientes.getValor(), aut_cuentas.getValor()));
                } else {
                    //modifica
                    utilitario.getConexion().agregarSqlPantalla(ser_cliente.getSqlActualizarCuentaCliente(aut_clientes.getValor(), aut_cuentas.getValor()));
                }
            }
        }

    }

    /**
     * Validacion para que se seleccione un cliente del Autocompletar
     *
     * @return
     */
    private boolean isClienteSeleccionado() {
        if (aut_clientes.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Cliente", "Seleccione un cliente de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    /**
     * Calcula el saldo por cobrar del Cliente
     *
     * @return
     */
    private void actualizarSaldoxCobrar() {
        double saldo_anterior = ser_cliente.getSaldoInicialCliente(aut_clientes.getValor(), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_ingresos = 0;
        double dou_egresos = 0;

        for (int i = 0; i < tab_transacciones_cxc.getTotalFilas(); i++) {
            if (tab_transacciones_cxc.getValor(i, "ingresos") != null) {
                dou_ingresos += Double.parseDouble(tab_transacciones_cxc.getValor(i, "ingresos"));
                dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_transacciones_cxc.getValor(i, "ingresos"));
            } else {
                dou_egresos += Double.parseDouble(tab_transacciones_cxc.getValor(i, "egresos"));
                dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_transacciones_cxc.getValor(i, "egresos"));
            }

            tab_transacciones_cxc.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_transacciones_cxc.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_transacciones_cxc.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
        }

        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_transacciones_cxc.setLectura(false);
            tab_transacciones_cxc.insertar();
            tab_transacciones_cxc.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_transacciones_cxc.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_transacciones_cxc.setValor("FECHA_TRANS_CCDTR", cal_fecha_inicio.getFecha());
            tab_transacciones_cxc.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_transacciones_cxc.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_transacciones_cxc.getColumna("INGRESOS").setTotal(dou_ingresos);
        tab_transacciones_cxc.getColumna("EGRESOS").setTotal(dou_egresos);

    }

    /**
     * Limpia la pantalla y el autocompletar
     */
    public void limpiar() {
        aut_clientes.limpiar();
        mep_menu.limpiar();
    }

    @Override
    public void insertar() {

        if (tab_cliente.isFocus()) {
            aut_clientes.limpiar();
            //FORMULARIO CLIENTE
            dibujarCliente();
            tab_cliente.limpiar();
            tab_cliente.insertar();
        } else if (tab_contactos.isFocus()) {
            tab_contactos.insertar();
        }

    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO CLIENTE
            if (ser_cliente.validarCliente(tab_cliente)) {
                if (tab_cliente.isFilaModificada()) {
                    tab_cliente.setValor("cli_fecha_modificacion", utilitario.getFechaHoraActual());
                    tab_cliente.setValor("id_usuario_modificacion", utilitario.getVariable("id_usuario"));
                }
                
                if (tab_contactos.isFilaModificada()) {
                    tab_contactos.setValor("con_fecha_modificacion", utilitario.getFechaHoraActual());
                    tab_contactos.setValor("id_usuario_modificacion", utilitario.getVariable("id_usuario"));
                }

                tab_cliente.guardar();
                tab_contactos.guardar();
                
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_clientes.actualizar();
                    aut_clientes.setSize(75);
                    aut_clientes.setValor(tab_cliente.getValorSeleccionado());
                    utilitario.addUpdate("aut_clientes");
                }
            }

        } else if (mep_menu.getOpcion() == 4) {
            //Cambiar Cuenta Contable
            if (guardarPantalla().isEmpty()) {

                aut_cuentas.setDisabled(true);
            }
        } else if (mep_menu.getOpcion() == 7) {
            //Classificacion de Clientes
            guardarPantalla();
        }

    }

    @Override
    public void eliminar() {

        //FORMULARIO CLIENTE
        if (tab_cliente.eliminar()) {
            aut_clientes.actualizar();
            aut_clientes.setSize(75);
            utilitario.addUpdate("aut_clientes");
            limpiar();
        }

    }

    public AutoCompletar getAut_clientes() {
        return aut_clientes;
    }

    public void setAut_clientes(AutoCompletar aut_clientes) {
        this.aut_clientes = aut_clientes;
    }

    public Tabla getTab_cliente() {
        return tab_cliente;
    }

    public void setTab_cliente(Tabla tab_cliente) {
        this.tab_cliente = tab_cliente;
    }

    public Tabla getTab_transacciones_cxc() {
        return tab_transacciones_cxc;
    }

    public void setTab_transacciones_cxc(Tabla tab_transacciones_cxc) {
        this.tab_transacciones_cxc = tab_transacciones_cxc;
    }

    public Tabla getTab_productos() {
        return tab_productos;
    }

    public void setTab_productos(Tabla tab_productos) {
        this.tab_productos = tab_productos;
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public Tabla getTab_movimientos() {
        return tab_movimientos;
    }

    public void setTab_movimientos(Tabla tab_movimientos) {
        this.tab_movimientos = tab_movimientos;
    }

    public Tabla getTab_facturas() {
        return tab_facturas;
    }

    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }

    public Arbol getArb_estructura() {
        return arb_estructura;
    }

    public void setArb_estructura(Arbol arb_estructura) {
        this.arb_estructura = arb_estructura;
    }

    public Tabla getTab_grafico() {
        return tab_grafico;
    }

    public void setTab_grafico(Tabla tab_grafico) {
        this.tab_grafico = tab_grafico;
    }

    public GraficoCartesiano getGca_grafico() {
        return gca_grafico;
    }

    public void setGca_grafico(GraficoCartesiano gca_grafico) {
        this.gca_grafico = gca_grafico;
    }

    public Tabla getTab_compra_productos() {
        return tab_compra_productos;
    }

    public void setTab_compra_productos(Tabla tab_compra_productos) {
        this.tab_compra_productos = tab_compra_productos;
    }

    public Tabla getTab_contactos() {
        return tab_contactos;
    }

    public void setTab_contactos(Tabla tab_contactos) {
        this.tab_contactos = tab_contactos;
    }

}
