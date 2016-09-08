/*
 * Copyright (c) 2012, xedushx . All rights reserved.
 */
package ec.com.erpxprime.framework.componentes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author xedushx 
 */
public class Encriptar {

    private static final char[] CONSTS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    //algoritmos
    public static String MD5 = "MD5";

    public String getEncriptar(String palabra) {
        try {
            MessageDigest msgd = MessageDigest.getInstance(MD5);
            byte[] bytes = msgd.digest(palabra.getBytes());
            StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int bajo = (int) (bytes[i] & 0x0f);
                int alto = (int) ((bytes[i] & 0xf0) >> 4);
                strbCadenaMD5.append(CONSTS_HEX[alto]);
                strbCadenaMD5.append(CONSTS_HEX[bajo]);
            }
            return strbCadenaMD5.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public String getGenerarClave() {
        String clave = getLentra() + getNumero() + getLentra() + getNumero() + getLentra();
        return clave;
    }

    private String getLentra() {
        int int_numero = (int) (Math.random() * 26);
        int_numero += 65;
        return (char) int_numero + "";
    }

    private int getNumero() {
        int int_numero = (int) (Math.random() * 10) + 1;
        return int_numero;
    }

}
