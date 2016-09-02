/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.aplicacion;

/**
 *
 * @author xedushx
 */
public class Parametro {

    private String modulo;
    private String nombre;
    private String detalle;
    private String valor;
    private String tabla;
    private String campoCodigo;
    private String campoNombre;

    public Parametro(String modulo, String nombre, String detalle, String valor, String tabla, String campoCodigo, String campoNombre) {
        this.modulo = modulo;
        this.nombre = nombre;
        this.detalle = detalle;
        this.valor = valor;
        this.tabla = tabla;
        this.campoCodigo = campoCodigo;
        this.campoNombre = campoNombre;
    }

    public Parametro(String modulo, String nombre, String detalle, String valor) {
        this.modulo = modulo;
        this.nombre = nombre;
        this.detalle = detalle;
        this.valor = valor;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getCampoCodigo() {
        return campoCodigo;
    }

    public void setCampoCodigo(String campoCodigo) {
        this.campoCodigo = campoCodigo;
    }

    public String getCampoNombre() {
        return campoNombre;
    }

    public void setCampoNombre(String campoNombre) {
        this.campoNombre = campoNombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
