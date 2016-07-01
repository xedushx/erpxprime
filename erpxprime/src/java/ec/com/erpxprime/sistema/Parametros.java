/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.sistema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parametros implements Serializable {

    public List<Parametro> getParametrosSistema() {
        List<Parametro> lis_parametros = new ArrayList();
        /*
         * CONTABILIDAD MODULO =0
         */
        lis_parametros.add(new Parametro("0", "p_con_lugar_debe", "Indica que una cuenta se aplica en el DEBE", "1", "con_lugar_aplicac", "ide_cnlap", "nombre_cnlap"));
        lis_parametros.add(new Parametro("0", "p_con_lugar_haber", "Indica que una cuenta se aplica en el HABER", "0", "con_lugar_aplicac", "ide_cnlap", "nombre_cnlap"));
        lis_parametros.add(new Parametro("0", "p_con_impuesto_iva30", "Indica el impuesto iva 30% ", "1", "con_cabece_impues", "ide_cncim", "nombre_cncim"));
        lis_parametros.add(new Parametro("0", "p_con_impuesto_iva70", "Indica el impuesto iva 70% ", "13", "con_cabece_impues", "ide_cncim", "nombre_cncim"));
        lis_parametros.add(new Parametro("0", "p_con_impuesto_iva100", "Indica el impuesto iva 100% ", "14", "con_cabece_impues", "ide_cncim", "nombre_cncim"));
        lis_parametros.add(new Parametro("0", "p_con_porcentaje_imp_iva", "Indica el porcentaje (12%) del iva ", "1", "con_porcen_impues", "ide_cnpim", "porcentaje_cnpim"));
        lis_parametros.add(new Parametro("0", "p_con_impuesto_renta", "Indica el impuesto a la renta ", "1", "con_impuesto", "ide_cnimp", "nombre_cnimp"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_documento_factura", "Indica el tipo de documento (Factura) ", "3", "con_tipo_document", "ide_cntdo", "nombre_cntdo"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_documento_reembolso", "Indica el tipo de documento (Reembolsos) ", "6", "con_tipo_document", "ide_cntdo", "nombre_cntdo"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_documento_liquidacion_compra", "Indica el tipo de documento (Factura) ", "4", "con_tipo_document", "ide_cntdo", "nombre_cntdo"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_documento_nota_venta", "Indica el tipo de documento (Factura) ", "5", "con_tipo_document", "ide_cntdo", "nombre_cntdo"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_comprobante_diario", "Indica el tipo de Comprobante de contabilidad (Diario) ", "0", "con_tipo_comproba", "ide_cntcm", "nombre_cntcm"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_comprobante_egreso", "Indica el tipo de Comprobante de contabilidad (Egreso) ", "3", "con_tipo_comproba", "ide_cntcm", "nombre_cntcm"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_comprobante_ingreso", "Indica el tipo de Comprobante de contabilidad (Ingreso) ", "2", "con_tipo_comproba", "ide_cntcm", "nombre_cntcm"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_comprobante_factura", "Indica el tipo de Comprobante de contabilidad (Factura) ", "4", "con_tipo_comproba", "ide_cntcm", "nombre_cntcm"));
        lis_parametros.add(new Parametro("0", "p_con_estado_comprobante_normal", "Indica el estado de Comprobante (Normal) ", "0", "con_estado_compro", "ide_cneco", "nombre_cneco"));
        lis_parametros.add(new Parametro("0", "p_con_estado_comprobante_anulado", "Indica el estado de Comprobante (Normal) ", "1", "con_estado_compro", "ide_cneco", "nombre_cneco"));
        lis_parametros.add(new Parametro("0", "p_con_estado_comprobante_rete_normal", "Indica el estado de Comprobante de retencion (Normal) ", "0", "con_estado_retenc", "ide_cnere", "nombre_cnere"));
        lis_parametros.add(new Parametro("0", "p_con_estado_comprobante_rete_anulado", "Indica el estado de Comprobante de retencion (Anulado) ", "1", "con_estado_retenc", "ide_cnere", "nombre_cnere"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_comprobante_ingreso", "Indica el tipo de Comprobante de contabilidad (Ingreso) ", "2", "con_tipo_comproba", "ide_cntcm", "nombre_cntcm"));
        lis_parametros.add(new Parametro("0", "p_con_estado_comp_inicial", "Indica el estado del comprobante Inicial", "2", "con_estado_compro", "ide_cneco", "nombre_cneco"));
        lis_parametros.add(new Parametro("0", "p_con_estado_comp_final", "Indica el estado del comprobante Final", "3", "con_estado_compro", "ide_cneco", "nombre_cneco"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_cuenta_activo", "Indica el estado del comprobante Final", "0", "con_tipo_cuenta", "ide_cntcu", "nombre_cntcu"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_cuenta_pasivo", "Indica el estado del comprobante Final", "4", "con_tipo_cuenta", "ide_cntcu", "nombre_cntcu"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_cuenta_patrimonio", "Indica el estado del comprobante Final", "5", "con_tipo_cuenta", "ide_cntcu", "nombre_cntcu"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_cuenta_ingresos", "Indica el estado del comprobante Final", "3", "con_tipo_cuenta", "ide_cntcu", "nombre_cntcu"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_cuenta_gastos", "Indica el estado del comprobante Final", "2", "con_tipo_cuenta", "ide_cntcu", "nombre_cntcu"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_cuenta_costos", "Indica el estado del comprobante Final", "1", "con_tipo_cuenta", "ide_cntcu", "nombre_cntcu"));
        lis_parametros.add(new Parametro("0", "p_con_tipo_contribuyente_rise", "Indica el tipo de contribuyente", "5", "con_tipo_contribu", "ide_cntco", "nombre_cntco"));
        lis_parametros.add(new Parametro("0", "p_con_beneficiario_empresa", "Indica el beneficiario Sociedad Salesiana en el Ecuador", "1294", "gen_persona", "ide_geper", "nom_geper"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_tarjeta_credito", "Indica la forma de pago con tarjeta de credito ", "4", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_donacion", "Indica la forma de pago es donacion ", "5", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_efec", "Indica la forma de pago (Efectivo) ", "0", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_cheque", "Indica la forma de pago (Cheque) ", "1", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_deposito", "Indica la forma de pago (Deposito) ", "7", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_transferencia", "Indica la forma de pago (Tranferencia) ", "6", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_anticipo", "Indica la forma de pago (Anticipo) ", "8", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_transferencia_casas", "Indica la forma de pago (Transferencia Cuentas Casas) ", "9", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_for_pag_reembolso_caja", "Indica la forma de pago se lo realiza con reembolso de caja (Reembolso de Caja) ", "10", "con_deta_forma_pago", "ide_cndfp", "nombre_cndfp"));
        lis_parametros.add(new Parametro("0", "p_con_inve_gasto_activo", "Indica la configuracion para activos ( Inventario Gasto Activo) ", "4", "con_cab_conf_asie", "ide_cncca", "nombre_cncca"));

        // parametros para el cierre de periodo
        lis_parametros.add(new Parametro("0", "p_con_superavit_ejercicio_anterior", "Indica la configuracion para la cuenta de Superavit del Ejercicio Anterior ", "1496", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_deficit_ejercicio_anterior", "Indica la configuracion para la cuenta de Deficit del Ejercicio Anterior ", "1497", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_patrimonio", "Indica la configuracion para la cuenta a la que se vamos a sumar el (superavit)  o restar el (Deficit) del Ejercicio Anterior ", "1487", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_superavit_ejercicio_presente", "Indica la configuracion para la cuenta de Superavit del Ejercicio Presente ", "1500", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_deficit_ejercicio_presente", "Indica la configuracion para la cuenta de Deficit del Ejercicio Presente ", "4481", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));

        /*
         * INVENTARIO MODULO =1
         */
        lis_parametros.add(new Parametro("1", "p_inv_servicio", "Indica el servicio ", "2", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_activo_fijo", "Indica que un articulo pertenecen al grupo de activos fijos ", "53", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_bien", "Indica que un articulo pertenecen al grupo de bienes ", "46", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_servicio", "Indica que un articulo pertenecen al grupo de servicios ", "2", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_honorarios", "Indica que un articulo pertenecen al grupo de honorarios profesionales ", "47", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_compra", "Indica el tipo de transaccion compra de inventario ", "19", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_venta", "Indica el tipo de transaccion venta de inventario ", "29", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_estado_normal", "Indica el estado normal de comprobante de inventario ", "1", "inv_est_prev_inve", "ide_inepi", "nombre_inepi"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_reversa_mas", "Indica que el tipo de transaccion inversa(cuando existe una anulacion)  ", "12", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_reversa_menos", "Indica que el tipo de transaccion inversa(cuando existe una anulacion)  ", "13", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_consumo", "Indica que el tipo de transaccion de consumo ", "27", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));


        /*
         * CUENTAS POR PAGAR MODULO =2
         */
        lis_parametros.add(new Parametro("2", "p_cxp_estado_factura_normal", "Indica el estado de la factura (Normal) ", "0", "cxp_estado_factur", "ide_cpefa", "nombre_cpefa"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_factura", "Indica que el tipo de transaccion es (Factura) ", "0", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago", "Indica que el tipo de transaccion (Pago) ", "3", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago_con_anticipo", "Indica que el tipo de transaccion es de un pago con un anticipo registrado (Pago con Anticipo) ", "17", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_reversa_mas", "Indica que el tipo de transaccion inversa(cuando existe una anulacion) ", "9", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_reversa_menos", "Indica que el tipo de transaccion inversa(cuando existe una anulacion) ", "10", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_anticipo", "Indica que el tipo de transaccion (Pago) ", "7", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_inversion", "Indica que el tipo de transaccion (Inversion) ", "15", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_inversion_interes", "Indica que el tipo de transaccion de inversion de intereses (Inversion de los interes) ", "18", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago_inversion", "Indica que el tipo de transaccion pago de inversion (pago Inversion Terminada) ", "16", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));        
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago_interes_inversion", "Indica que el tipo de transaccion pago de intereses de la inversion (Pago de intereses) ", "19", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));        
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_renova_interes_reinver", "Indica que el tipo de transaccion renovacion de intereses de inversion (Interes de Nueva Renovacion de Inversion) ", "20", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        
        /*
         * CUENTAS POR COBRAR MODULO =3
         */
        lis_parametros.add(new Parametro("3", "p_cxc_estado_factura_normal", "Indica el estado de la factura (Normal) ", "0", "cxc_estado_factura", "ide_ccefa", "nombre_ccefa"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_factura", "Indica que el tipo de transaccion es (Factura) ", "4", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_pago", "Indica que el tipo de transaccion es (Pago de cliente) ", "0", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_anticipo", "Indica que el tipo de transaccion es (Anticipo de cliente) ", "5", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_reversar_mas", "Indica que el tipo de transaccion es para reversar (+) una transaccion ", "15", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_reversar_menos", "Indica que el tipo de transaccion es para reversar (-)una transaccion ", "16", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_estado_factura_anulada", "Indica el estado de la factura (Anulada) ", "1", "cxc_estado_factura", "ide_ccefa", "nombre_ccefa"));

        /*
         * ACTIVOS FIJOS MODULO =4
         */
        lis_parametros.add(new Parametro("4", "p_act_tipo_activo_fijo", "Indica el tipo de activo fijo ", "0", "inv_tipo_producto", "ide_intpr", "nombre_intpr"));
        lis_parametros.add(new Parametro("4", "p_act_estado_dado_de_baja", "Indica el estado dado de baja de un activo ", "4", "act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf"));

        /*
         * COMPRAS MODULO =5
         *
         */
        lis_parametros.add(new Parametro("5", "p_gen_tipo_identificacion_ruc", "Indica el tipo de identificacion que sea RUC ", "1", "gen_tipo_identifi", "ide_getid", "nombre_getid"));
        lis_parametros.add(new Parametro("5", "p_gen_tipo_identificacion_cedula", "Indica el tipo de identificacion que sea CEDULA ", "0", "gen_tipo_identifi", "ide_getid", "nombre_getid"));
        lis_parametros.add(new Parametro("5", "p_com_estado_proveedor", "Indica el estado del Proveedor", "0", "com_estado_provee", "ide_coepr", "nombre_coepr"));

        /*
         * NOMINA =6
         */
        lis_parametros.add(new Parametro("6", "p_reh_dias_trabajados", "Indica el numero de dias trabajados ", "9", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_acumula_fondos_reserva", "Indica el rubro acumula fondos de reserva ", "25", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_rubro_valor_recibir", "Indica el rubro valor a recibir ", "37", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_estado_activo_empleado", "Indica el estado activo de un  empleado ", "0", "reh_estado_emplea", "ide_rheem", "nombre_rheem"));
        lis_parametros.add(new Parametro("6", "p_reh_si_acumula_fondos", "Indica que un empleado si acumula fondos de reserva", "1", "reh_fondos_reserva", "ide_rhfre", "nombre_rhfre"));
        lis_parametros.add(new Parametro("6", "p_reh_valor_acumula_fondo", "Indica el rubro acumula fondos de reserva ", "26", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_no_acumula_fondos", "Indica que un empleado no acumula fondos de reserva", "0", "reh_fondos_reserva", "ide_rhfre", "nombre_rhfre"));
        lis_parametros.add(new Parametro("6", "p_reh_rubro_valor_recibir", "Indica el rubro valor a recibir", "37", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_estado_pre_nomina", "Indica el estado pre nomina del rol ", "0", "reh_estado_rol", "ide_rhero", "nombre_rhero"));
        lis_parametros.add(new Parametro("6", "p_reh_estado_cerrada", "Indica el estado cerrado del rol ", "2", "reh_estado_rol", "ide_rhero", "nombre_rhero"));

        
        /*
         * TESORERIA =7
         */

        lis_parametros.add(new Parametro("7", "p_tes_num_dias_filtro", "Indica el numero de dias para realizar el filtro ", "90"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_cheque", "Indica la transaccion se lo realiza con cheque ", "2", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_caja", "Indica que el Banco es de tipo Caja ", "3", "tes_banco", "ide_teban", "nombre_teban"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_retiro_caja", "Indica la transaccion se lo realiza con caja ", "1", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_deposito_caja", "Indica la transaccion se lo realiza con caja ", "8", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_deposito", "Indica la transaccion se lo realiza con bancos (deposito) ", "0", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_transferencia_menos", "Indica la transaccion se lo realiza con bancos (Transferencia MENOS) ", "9", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_transferencia_mas", "Indica la transaccion se lo realiza con bancos (Transferencia MAS) ", "10", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_reversa_mas", "Indica la transaccion de reversa con signo + ", "11", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_reversa_menos", "Indica la transaccion de reversa sin signo - ", "12", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_estado_lib_banco_normal", "Indica el estado normal de la transaccion de libro bancos del modulo Tesoreria ", "0", "tes_estado_libro_banco", "ide_teelb", "nombre_teelb"));
        lis_parametros.add(new Parametro("7", "p_tes_estado_lib_banco_anulado", "Indica el estado anulado de la transaccion de libro bancos del modulo Tesoreria ", "1", "tes_estado_libro_banco", "ide_teelb", "nombre_teelb"));
        lis_parametros.add(new Parametro("7", "p_tes_nota_debito", "Indica el tipo de la transaccion nota de debito de libro bancos del modulo Tesoreria ", "3", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_nota_credito", "Indica el tipo de la transaccion nota de credito de libro bancos del modulo Tesoreria ", "4", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tip_tran_reversar_mas", "Indica que se realiza una transaccion inversa (+) ", "10", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tip_tran_reversar_menos", "Indica que se realiza una transaccion inversa (-) ", "11", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tipo_cuenta_banco_virtual", "Indica que el tipo de cuenta bancaria virtual (cajas) ", "6", "tes_tip_cuen_banc", "ide_tetcb", "nombre_tetcb"));
        lis_parametros.add(new Parametro("7", "p_tes_banco_casas", "Indica que el Banco es de tipo casas para pagar por trandsferencia de cuentas contables ", "5", "tes_banco", "ide_teban", "nombre_teban"));
        
        /*
         * VENTAS =8
         */



        /*
         * PRESTAMOS E INVERSIONES =9 *
         */
        lis_parametros.add(new Parametro("9", "p_iyp_activo", "Indica que se carga solo las cuentas de nivel de Activo", "0", "con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc"));
        lis_parametros.add(new Parametro("9", "p_iyp_pasivo", "Indica que se carga solo las cuentas de nivel de Pasivo  ", "4", "con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_normal", "Indica el estado normal del prestamo  ", "0", "iyp_estado_prestamos", "ide_ipepr", "nombre_ipepr"));
        lis_parametros.add(new Parametro("9", "p_iyp_tipo_prestamo", "Indica que se carga el tipo de prestamo cuota fija  ", "0", "iyp_tipo_prestamo", "ide_iptpr", "nombre_iptpr"));
        lis_parametros.add(new Parametro("9", "p_iyp_serie_factura", "Indica la serie a utilizar para facturar el ineters de un pago de prestamo ", "3", "cxc_datos_fac", "ide_ccdaf", "serie_ccdaf"));
        lis_parametros.add(new Parametro("9", "p_iyp_cuenta_interes", "Indica la cuenta que afecta a los interes de los prestamos de las casas", "1982", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_activo_inversion", "Indica el estado activo de la inversión", "0", "iyp_estado_inversion", "ide_ipein", "nombre_ipein"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_cancelado", "Indica el estado cancelado de la inversion  ", "1", "iyp_estado_inversion", "ide_ipein", "nombre_ipein"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_anulada", "Indica el estado anulada de la inversion  ", "2", "iyp_estado_inversion", "ide_ipein", "nombre_ipein"));
        lis_parametros.add(new Parametro("9", "p_iyp_cuenta_interes_pagado", "Indica la cuenta que afecta a los interes de los prestamos de de ingreso", "4100", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));


        /*
         * SRI =10 *
         */
        lis_parametros.add(new Parametro("10", "p_sri_tip_sus_tri02", "Indica el tipo de sustento tributario de alterno 02 (COSTO O GASTO PARA DECLARACIÓN DE IMP. A LA RENTA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )) ", "1", "sri_tipo_sustento_tributario", "ide_srtst", "nombre_srtst"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_local_12%_401", "Ventas locales [excluye activos fijos] gravadas tarifa 12%", "401"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_activos_12%_402", "Ventas de activos fijos gravadas tarifa 12%", "402"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_local_no_dertri_0%_403", "Ventas locales [excluye activos fijos] gravadas tarifa 0% que no dan derecho a crédito tributario", "403"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_activos_0%_no_dertri_404", "Ventas de activos fijos gtavadas tarifa 0% que no dan derecho a crédito tributario", "404"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_local_0%_dertri_405", "Ventas locales(excluye activos fijos) gravadasde activos fijos gtavadas tarifa 0% que dan derecho a crédito tributario", "405"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_activos_0%_dertri_406", "Ventas de activos fijos gravadas tarifa 0% que dan derecho a crédito tributario", "406"));
        lis_parametros.add(new Parametro("10", "p_sri_exportacion_bienes_407", "Exportaciones de bienes", "407"));
        lis_parametros.add(new Parametro("10", "p_sri_exportacion_servicios_408", "Exportaciones de servicios", "408"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_pagos_12%_dertri_501", "Adquisiciones y pagos [excluye activos fijos] gravados tarifa 12% (con derecho a crédito tributario)", "501"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_local_12%_dertri_502", "Adquisiciones locales de activos fijos gravados tarifa 12% (con derecho a crédito tributario)", "502"));
        lis_parametros.add(new Parametro("10", "p_sri_otra_adqui_pago_12%_no_dertri_503", "Otras adquisiones y pagos gravados tarifa 12% (sin derecho a crédito tributario)", "503"));
        lis_parametros.add(new Parametro("10", "p_sri_import_bienes_12%_504", "Importaciones de bienes [excluye activos fijos] gravados tarifa 12%", "504"));
        lis_parametros.add(new Parametro("10", "p_sri_import_act_fijos_12%_505", "Importaciones de activos fijos  gravados tarifa 12%", "505"));
        lis_parametros.add(new Parametro("10", "p_sri_import_bienes_0%_506", "Importaciones de bienes [incluye activos fijos] gravados tarifa 0%", "506"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_realizadas_rise_518", "Adquisiciones realizadas a contribuyente rise", "518"));
        lis_parametros.add(new Parametro("10", "p_sri_trans_no_obj_iva_431", "Transferencias no objeto o exentas de IVA", "431"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_no_obj_iva_531", "Adquisiciones no objeto de IVA", "531"));

        lis_parametros.add(new Parametro("10", "p_sri_base_renta", "Indica el rubro de la base imponible del impuesto a la renta para formulario 103", "53", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("10", "p_sri_impuesto_renta", "Indica el rubro del valor de impuesto a la renta para formulario 103", "31", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));

        /*
         * PRESUPUESTOS =11 *
         */
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_obra", "Indica el tipo de aporte Obra", "2", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_paricipante", "Indica el tipo de aporte Participante", "1", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_inspectoria", "Indica el tipo de aporte Inspectoria", "0", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_rmayor", "Indica el tipo de aporte Rector Mayor", "3", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_presu_gasto", "Indica el tipo de presupuesto de Gasto", "1", "pre_tipo_presupuesto", "ide_prtpr", "nombre_prtpr"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_presu_ingreso", "Indica el tipo de presupuesto de Ingreso", "0", "pre_tipo_presupuesto", "ide_prtpr", "nombre_prtpr"));
        lis_parametros.add(new Parametro("11", "p_pre_estado_en_tramite", "Indica el tipo de estado en tramite del presupuesto", "1", "pre_estado_plan_presu", "ide_prepp", "nombre_prepp"));
        lis_parametros.add(new Parametro("11", "p_pre_num_modificacion_presu", "Numero de Modificaciones permitidas para el presupuesto", "1"));

        /*
         * MODULO GENERAL 12
         */
        lis_parametros.add(new Parametro("12", "p_gen_tipo_iden_ruc", "tipo de identificacion RUC", "1", "gen_tipo_identifi", "ide_getid", "nombre_getid"));
        lis_parametros.add(new Parametro("12", "p_gen_tipo_iden_cedula", "tipo de identificacion CEDULA", "0", "gen_tipo_identifi", "ide_getid", "nombre_getid"));

        lis_parametros.add(new Parametro("12", "p_gen_beneficiario_roles", "Identifica al beneficiario en el asiento de roles de pago ", "1438", "gen_persona", "ide_geper", "nom_geper"));
        
        return lis_parametros;
    }
}
