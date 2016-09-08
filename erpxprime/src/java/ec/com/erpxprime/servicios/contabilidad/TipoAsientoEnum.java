/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.servicios.contabilidad;

/**
 *
 * @author xedushx
 */
public enum TipoAsientoEnum {

    FACTURAS_CXC("1"),
    PAGO_FACTURAS_CXC("2"),
    DOCUMENTOS_CXP("3"),
    PAGO_DOCUMENTOS_CXP("4"),
    LIBRO_BANCOS("5");

    private final String codigo;

    private TipoAsientoEnum(final String codigo) {
        this.codigo = codigo;
    }

    /**
     * obtiene el codigo de la numeracion
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }
}
