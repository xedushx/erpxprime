/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.servicios.sistema;

import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import javax.ejb.Stateless;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ec.com.erpxprime.sistema.aplicacion.Utilitario;

/**
 *
 * @author xedushx
 */
@Stateless

public class ServicioSistema {

    private final Utilitario utilitario = new Utilitario();

    /**
     * Retorna Datos de la Empresa
     *
     * @return
     */
    public TablaGenerica getEmpresa() {
        return getEmpresa(utilitario.getVariable("empresa"));
    }

    /**
     * Retorna Datos de una Empresa
     *
     * @param id_empresa
     * @return
     */
    public TablaGenerica getEmpresa(String id_empresa) {
        return utilitario.consultar("SELECT * from tbl_empresa where id_empresa=" + id_empresa);
    }

    /**
     * Retorna el Logo de la empresa en forma StreamedContent
     *
     * @return
     */
    public StreamedContent getLogoEmpresa() {
        StreamedContent stream = null;
        try {
            TablaGenerica tabEmpresa = getEmpresa();
            if (tabEmpresa.isEmpty() == false) {
                try (InputStream myInputStream = new ByteArrayInputStream((byte[]) tabEmpresa.getValorObjeto("LOGO_EMPR"))) {
                    myInputStream.mark(0);
                    String mimeType = URLConnection.guessContentTypeFromStream(myInputStream);
                    stream = new DefaultStreamedContent(myInputStream, mimeType);
                    myInputStream.close();
                } catch (Exception e) {
                    System.out.println("Error {}" + e);
                }
            }
        } catch (Exception e) {
            System.out.println("Error {}" + e);
        }
        return stream;
    }

    /**
     * Retorna los datos de una Sucursal
     *
     * @param ide_sucu
     * @return
     */
    public TablaGenerica getSucursal(String ide_sucu) {
        return utilitario.consultar("SELECT * from sis_sucursal where ide_sucu=" + ide_sucu);
    }

    /**
     * Retorna los datos de la Sucursal
     *
     * @return
     */
    public TablaGenerica getSucursal() {
        return getSucursal(utilitario.getVariable("IDE_SUCU"));
    }

    public TablaGenerica getUsuario() {
        return getUsuario(utilitario.getVariable("id_usuario"));
    }

    public TablaGenerica getUsuario(String id_usuario) {
        return utilitario.consultar("SELECT * from tbl_usuario where id_usuario=" + id_usuario);
    }

    public TablaGenerica getPerfil(String id_perfil) {
        return utilitario.consultar("SELECT * from tbl_perfil where id_perfil=" + id_perfil);
    }

    public TablaGenerica getPerfil() {
        return getPerfil(utilitario.getVariable("id_perfil"));
    }

    public TablaGenerica getOpcionPantalla() {
        return utilitario.consultar("SELECT PAQUETE_OPCI,TIPO_OPCI,AUDITORIA_OPCI,MANUAL_OPCI FROM SIS_OPCION WHERE IDE_OPCI=" + utilitario.getVariable("IDE_OPCI"));
    }

    public String getSqlPantallasPerfil(String id_perfil) {
        return "SELECT a.IDE_OPCI,NOM_OPCI\n"
                + "FROM SIS_OPCION a ,tbl_perfil_OPCION b\n"
                + "WHERE a.IDE_OPCI=b.IDE_OPCI \n"
                + "AND b.id_perfil=" + id_perfil + " \n"
                + "and tipo_opci is not null and paquete_opci is not null\n"
                + "ORDER BY NOM_OPCI";
    }

}
