/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.servicios.cuentas_x_cobrar;

import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.Tabla;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ec.com.erpxprime.servicios.ServicioBase;
import ec.com.erpxprime.servicios.contabilidad.ServicioConfiguracion;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xedushx
 */
@Stateless
public class ServicioCliente extends ServicioBase {

    /**
     * Codigo Padre de todos los clientes para el campo GEN_IDE_GEPER
     */
    public final static String P_PADRE_CLIENTES = "1";

    @EJB
    private ServicioConfiguracion ser_configuracion;

    @PostConstruct
    public void init() {
        //Recupera todos los parametros que se van a ocupar en el EJB
        parametros = utilitario.getVariables("p_cxc_estado_factura_normal");
    }

    /**
     * Retorna los datos de un cliente
     *
     * @param cli_codigo Cliente
     * @return
     */
    public TablaGenerica getCliente(String cli_codigo) {
        return utilitario.consultar("select * from ven_cliente where cli_codigo=" + cli_codigo);
    }

    /**
     * Retorna la cuenta configurada del Cliente con el identificador CUENTA POR
     * COBRAR
     *
     * @param cli_codigo Cliente
     * @return
     */
    public String getCuentaCliente(String cli_codigo) {
        return ser_configuracion.getCuentaPersona("CUENTA POR COBRAR", cli_codigo);
    }

    /**
     * Retorna la sentencia SQL para actualizar la configuracion de la cuenta
     * del cliente
     *
     * @param cli_codigo Cliente
     * @param ide_cndpc Nueva Cuenta
     * @return
     */
    public String getSqlActualizarCuentaCliente(String cli_codigo, String ide_cndpc) {
        return "update con_det_conf_asie "
                + "set ide_cndpc=" + ide_cndpc + " "
                + "where cli_codigo=" + cli_codigo + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR COBRAR");
    }

    /**
     * Retorna si un cliente tiene configurada una cuenta contable
     *
     * @param cli_codigo
     * @return
     */
    public boolean isTieneCuentaConfiguradaCliente(String cli_codigo) {
        return !utilitario.consultar("Select * from con_det_conf_asie "
                + "where cli_codigo=" + cli_codigo + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR COBRAR")).isEmpty();
    }

    /**
     * Retorna la sentencia SQL para insertar la configuracion de la cuenta del
     * cliente
     *
     * @param cli_codigo
     * @param ide_cndpc
     * @return
     */
    public String getSqlInsertarCuentaCliente(String cli_codigo, String ide_cndpc) {
        return "insert into con_det_conf_asie (ide_cndca,cli_codigo,ide_cndpc,ide_cnvca)"
                + "values (" + utilitario.getConexion().getMaximo("con_det_conf_asie", "ide_cndca", 1)
                + ", " + cli_codigo + ", " + ide_cndpc + ","
                + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR COBRAR") + " )";
    }

    /**
     * Reorna la sentecnia SQL para que se utilice en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboClientes() {
        return "select cli_codigo,cli_numero_documento,cli_razon_social from ven_cliente where cli_numero_documento is not null order by cli_razon_social ";
    }

    /**
     * Asigna las configuraciones de un cliente
     *
     * @param tabla
     */
    public void configurarTablaCliente(Tabla tabla) {

        tabla.setTabla("ven_cliente", "cli_codigo", -1);
        tabla.getColumna("cli_codigo").setNombreVisual("CÓDIGO");
        tabla.getColumna("tic_codigo").setNombreVisual("TIPO DE CONTRIBUYENTE");
        tabla.getColumna("cli_razon_social").setNombreVisual("RAZÓN SOCIAL / NOMBRES");
        tabla.getColumna("tid_codigo").setNombreVisual("TIPO DE DOCUMENTO");
        tabla.getColumna("cli_numero_documento").setNombreVisual("NÚMERO DE DOCUMENTO");
        tabla.getColumna("cli_tiene_ruc").setNombreVisual("POSEE RUC");
        tabla.getColumna("lge_codigo").setNombreVisual("UBICACIÓN");
        tabla.getColumna("cli_nivel").setNombreVisual("NIVEL");
        tabla.getColumna("cli_direccion").setNombreVisual("DIRECCIÓN");
        tabla.getColumna("cli_telefono").setNombreVisual("TELÉFONO");
        
        tabla.getColumna("tid_codigo").setCombo("gen_tipo_identificacion", "tid_codigo", "tid_nombre", "");
        tabla.getColumna("tic_codigo").setCombo("ven_tipo_contribuyente", "tic_codigo", "tic_nombre", "");
        
        List tieneRUC = new ArrayList();
        Object si[] = {
            "TRUE", "SI"
        };
        Object no[] = {
            "FALSE", "NO"
        };
        tieneRUC.add(si);
        tieneRUC.add(no);
        
        tabla.getColumna("cli_tiene_ruc").setRadio(tieneRUC, "FALSE");
        tabla.getColumna("lge_codigo").setCombo("gen_lugar_geografico", "lge_codigo", "lge_nombre", "lge_nivel = 'HIJO'");
        
        tabla.getColumna("cli_nivel").setCombo(utilitario.getListaNiveles());
        tabla.getColumna("cli_nivel").setValorDefecto("HIJO");
        tabla.getColumna("cli_nivel").setPermitirNullCombo(true);
        
        tabla.getColumna("cli_numero_documento").setUnico(true);
        
        tabla.getColumna("cli_codigo_2").setValorDefecto(P_PADRE_CLIENTES); //padre de todos los clientes
        tabla.getColumna("cli_fecha_registro").setValorDefecto(utilitario.getFechaHoraActual());
        tabla.getColumna("id_usuario_registro").setValorDefecto(utilitario.getVariable("id_usuario"));
        
        tabla.getColumna("cli_codigo_2").setVisible(false);
        tabla.getColumna("cli_nivel").setVisible(false);
        tabla.getColumna("cli_fecha_registro").setVisible(false);
        tabla.getColumna("id_usuario_registro").setVisible(false);
        tabla.getColumna("cli_fecha_modificacion").setVisible(false);
        tabla.getColumna("id_usuario_modificacion").setVisible(false);
        
        tabla.setTipoFormulario(true);
        tabla.getGrid().setColumns(2);
    }
    
    /**
     * Asigna las configuraciones de un contacto
     *
     * @param tabla
     */
    public void configurarTablaContactos(Tabla tabla) {

        tabla.setTabla("gen_contacto", "con_codigo", -1);
        tabla.getColumna("con_codigo").setNombreVisual("CÓDIGO");
        tabla.getColumna("con_nombre").setNombreVisual("NOMBRES");
        tabla.getColumna("con_telefono").setNombreVisual("TELÉFONOS");
        tabla.getColumna("con_celular").setNombreVisual("CELULAR");
        tabla.getColumna("con_correo").setNombreVisual("CORREO");
        tabla.getColumna("con_correo_2").setNombreVisual("CORREO ALTERNO");
        tabla.getColumna("con_direccion").setNombreVisual("DIRECCIÓN");
        tabla.getColumna("con_calle_principal").setNombreVisual("CALLA PRINCIPAL");
        tabla.getColumna("con_numero_casa").setNombreVisual("NÚMERO CASA");
        tabla.getColumna("con_calle_secundaria").setNombreVisual("CALLE SECUNDARIA");
        tabla.getColumna("tco_codigo").setNombreVisual("TIPO");
        
        tabla.getColumna("tco_codigo").setCombo("gen_tipo_contacto", "tco_codigo", "tco_nombre", "");
        
        tabla.getColumna("con_fecha_registro").setValorDefecto(utilitario.getFechaHoraActual());
        tabla.getColumna("id_usuario_registro").setValorDefecto(utilitario.getVariable("id_usuario"));
        
        tabla.getColumna("con_fecha_registro").setVisible(false);
        tabla.getColumna("id_usuario_registro").setVisible(false);
        tabla.getColumna("con_fecha_modificacion").setVisible(false);
        tabla.getColumna("id_usuario_modificacion").setVisible(false);
        
    }

    /**
     * Retorna una sentencia SQL que contiene los detalles de Productos X
     * SUCURSAL que a comprado un Cliente en un rango de fechas
     *
     * @param cli_codigo
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlProductosCliente(String cli_codigo, String fechaInicio, String fechaFin) {
        return "SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa ,iart.nombre_inarti ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa \n"
                + "from cxc_deta_factura cdf \n"
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa \n"
                + "left join inv_articulo iart on iart.ide_inarti=cdf.ide_inarti \n"
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "where cf.cli_codigo=" + cli_codigo + " "
                + "and cdf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "and cf.fecha_emisi_cccfa  BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "ORDER BY cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa";
    }

    /**
     * Retorna las Transacciones del Cliente X SUCURSAL
     *
     * @param cli_codigo
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlTransaccionesCliente(String cli_codigo, String fechaInicio, String fechaFin) {
        return "SELECT FECHA_TRANS_CCDTR, IDE_CNCCC, nombre_ccttr as TRANSACCION,DOCUM_RELAC_CCDTR, case when signo_ccttr = 1 THEN VALOR_CCDTR  end as INGRESOS,case when signo_ccttr = -1 THEN VALOR_CCDTR end as EGRESOS, '' SALDO,OBSERVACION_CCDTR as OBSERVACION, NOM_USUA as USUARIO, FECHA_VENCI_CCDTR,IDE_CCDTR,IDE_TECLB,NUMERO_PAGO_CCDTR "
                + "FROM cxc_detall_transa a "
                + "INNER JOIN  cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR "
                + "INNER JOIN  sis_usuario c on a.IDE_USUA =c.IDE_USUA "
                + "INNER JOIN cxc_cabece_transa d on a.ide_ccctr=d.ide_ccctr "
                + "WHERE cli_codigo=" + cli_codigo + " "
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "AND FECHA_TRANS_CCDTR BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "ORDER BY FECHA_TRANS_CCDTR,IDE_CCDTR";
    }

    /**
     * Retorna el saldo inicial de un Cliente X SUCURSAL a una fecha determinada
     *
     * @param cli_codigo
     * @param fecha
     * @return
     */
    public double getSaldoInicialCliente(String cli_codigo, String fecha) {
        double saldo = 0;
        String sql = "select cli_codigo,sum(valor_ccdtr* signo_ccttr) as valor from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "where cli_codigo=" + cli_codigo + " "
                + "and fecha_trans_ccdtr <'" + fecha + "' "
                + "and dt.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "group by cli_codigo";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor(0, "valor") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor(0, "valor"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }

    /**
     * Retorna sentencia SQL para obtener las facturas por cobrar de un cliente
     * X SUCURSAL
     *
     * @param cli_codigo
     * @return
     */
    public String getSqlFacturasPorCobrar(String cli_codigo) {
        return "select dt.ide_ccctr,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end as FECHA,"
                + "serie_ccdaf,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cccfa) is NULL then ct.observacion_ccctr else cf.observacion_cccfa end as OBSERVACION "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "where ct.cli_codigo=" + cli_codigo + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,serie_ccdaf,cf.secuencial_cccfa, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";
    }

    public String getSqlCuentasPorCobrar(String cli_codigo) {

        String str_sql_cxc = "select dt.ide_ccctr,"
                + "dt.ide_cccfa,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cccfa) is NULL then ct.observacion_ccctr else cf.observacion_cccfa end "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "where ct.cli_codigo=" + cli_codigo + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,cf.secuencial_cccfa, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";

        return str_sql_cxc;
    }

    /**
     * Ventas Mensuales en un año de un cliente
     *
     * @param cli_codigo
     * @param anio
     * @return
     */
    public String getSqlTotalVentasMensualesCliente(String cli_codigo, String anio) {
        return "select nombre_gemes,"
                + "(select count(ide_cccfa) as num_facturas from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and cli_codigo=" + cli_codigo + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_grabada_cccfa) as ventas12 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and cli_codigo=" + cli_codigo + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_tarifa0_cccfa+base_no_objeto_iva_cccfa) as ventas0 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and cli_codigo=" + cli_codigo + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(valor_iva_cccfa) as iva from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and cli_codigo=" + cli_codigo + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(total_cccfa) as total from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and cli_codigo=" + cli_codigo + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + ") "
                + "from gen_mes "
                + "order by ide_gemes";
    }

    /**
     * Reorna los productos que compra el cliente, con ultima fecha de compra,
     * ultimo precio de venta,
     *
     * @param cli_codigo
     * @return
     */
    public String getSqlProductosVendidos(String cli_codigo) {

        return "select distinct a.ide_inarti,nombre_inarti,max(b.fecha_emisi_cccfa) as fecha_ultima_venta,\n"
                + "(select precio_ccdfa from cxc_deta_factura  inner join cxc_cabece_factura  on cxc_deta_factura.ide_cccfa=cxc_cabece_factura.ide_cccfa where ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " and cli_codigo=" + cli_codigo + " and ide_inarti=a.ide_inarti order by fecha_emisi_cccfa desc limit 1) as ultimo_precio\n"
                + "from cxc_deta_factura a \n"
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa\n"
                + "inner join inv_articulo c on a.ide_inarti=c.ide_inarti\n"
                + "where ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "and cli_codigo=" + cli_codigo + " "
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "group by a.ide_inarti,nombre_inarti \n"
                + "order by nombre_inarti";
    }

    /**
     * Validaciones para crear o modificar un Cliente
     *
     * @return
     */
    public boolean validarCliente(Tabla tabla) {
        if (tabla.getValor("tid_codigo") != null && tabla.getValor("tid_codigo").equals(utilitario.getVariable("p_gen_tipo_iden_cedula"))) {
            if (utilitario.validarCedula(tabla.getValor("cli_numero_documento"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar un número de cédula válido");
                return false;
            }
        }
        if (tabla.getValor("tid_codigo") != null && tabla.getValor("tid_codigo").equals(utilitario.getVariable("p_gen_tipo_iden_ruc"))) {
            if (utilitario.validarRUC(tabla.getValor("cli_numero_documento"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar un número de ruc válido");
                return false;
            }
        }
        if (tabla.getValor("tic_codigo") == null || tabla.getValor("tic_codigo").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el tipo de contribuyente");
            return false;
        }

        if (tabla.getValor("cli_direccion") == null || tabla.getValor("cli_direccion").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar la dirección");
            return false;
        }

        if (tabla.getValor("cli_telefono") == null || tabla.getValor("cli_telefono").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar un número de teléfono");
            return false;
        }
        //      }
        return true;
    }

}
