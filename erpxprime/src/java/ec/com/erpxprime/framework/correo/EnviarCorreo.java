package ec.com.erpxprime.framework.correo;

import ec.com.erpxprime.framework.aplicacion.Framework;
import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 *
 * @author xedushx Fernando Jácome
 */
public class EnviarCorreo {

    private Properties propiedades = new Properties();
    private String correo = "edushinksanetattoo@gmail.com";
    private String clave = "7SxeHardcore2711";
    private List<MimeMessage> listaMensajes;
    private Session session = null;

    public EnviarCorreo() {
        Framework framework=new Framework();

        TablaGenerica tab_correo = framework.consultar("SELECT * FROM tbl_correo");
        
        propiedades.setProperty("mail.smtp.starttls.enable", "true");
        propiedades.setProperty("mail.smtp.auth", "false");
        propiedades.setProperty("mail.transport.protocol", "smtp");
        
        if (tab_correo.isEmpty() == false) {
            propiedades.put("mail.smtp.host", tab_correo.getValor("smtp"));
            propiedades.setProperty("mail.smtp.port", tab_correo.getValor("puerto"));
            propiedades.setProperty("mail.smtp.user", tab_correo.getValor("usuario"));
            correo = tab_correo.getValor("usuario");
            clave = tab_correo.getValor("clave");
        } else {
            //Propiedades para gmail
            propiedades.put("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.port", "587");
            propiedades.setProperty("mail.smtp.user", "edushinksanetattoo@gmail.com");
        }
        
        session = Session.getDefaultInstance(propiedades, null);

    }

    public String agregarCorreo(String destinatario, String asunto, String mensaje, List<File> adjuntos) {
        String str = "";
        if (listaMensajes == null) {
            listaMensajes = new ArrayList();
        }
        if (isEmailValido(destinatario)) {
            try {
                BodyPart texto = new MimeBodyPart();
                StringBuilder stb_mensaje = new StringBuilder("<div style='font: normal 14px Tahoma, Verdana, Arial, sans-serif;'>").append(mensaje);

                stb_mensaje.append("</div> <div style='font-size: 12px;padding-top: 100px' > <strong> Nota:</strong> El envío de este correo es automático, por favor no lo responda. Si tiene alguna inquietud al respecto contáctese con el administrador del sistema.   </div>");
                texto.setContent(stb_mensaje.toString(), "text/html");
                // Una MultiParte para agrupar texto e imagen.
                MimeMultipart multiParte = new MimeMultipart();

                multiParte.addBodyPart(texto);
                if (adjuntos != null) {
                    // Se compone el adjuntos
                    for (File archivo : adjuntos) {
                        BodyPart adjunto = new MimeBodyPart();
                        adjunto.setDataHandler(
                                new DataHandler(new FileDataSource(archivo)));
                        adjunto.setFileName(archivo.getName());
                        multiParte.addBodyPart(adjunto);
                    }
                }
                // Se compone el correo, dando to, from, subject y el
                // contenido.
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.addRecipient(
                        Message.RecipientType.TO,
                        new InternetAddress(destinatario));
                message.setSubject(asunto);
                message.setContent(multiParte);
                listaMensajes.add(message);
            } catch (Exception e) {
            }
        } else {
            str += "Correo no válido: " + destinatario;
        }
        return str;
    }

    public String enviarTodos() {
        String str = "";
        if (listaMensajes != null) {
            try {
                Transport t = session.getTransport("smtp");
                t.connect(correo, clave);
                for (Message actual : listaMensajes) {
                    if (t.isConnected() == false) {
                        t.connect(correo, clave);
                    } else {
                        t.sendMessage(actual, actual.getRecipients(Message.RecipientType.TO));
                    }
                }
                t.close();

            } catch (Exception e) {
                str += "No se puede enviar el correo: " + e.getMessage() + " por: " + e.getMessage();
            }
        }
        return str;
    }

    public String enviar(String destinatario, String asunto, String mensaje, List<File> adjuntos) {
        StringBuilder msj = new StringBuilder();
        String[] destinatarios = destinatario.split(";");
        List<String> mensajes = new ArrayList<>();
        
        for (String destinatario1 : destinatarios) {
            mensajes.add(agregarCorreo(destinatario1, asunto, mensaje, adjuntos));            
        }
        
        if (mensajes.isEmpty()) {
            msj.append(enviarTodos());
            listaMensajes.clear();
        }
        return msj.toString();
    }

    public boolean isEmailValido(String email) {
        Pattern pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        Matcher mat = pat.matcher(email);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    public Properties getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Properties propiedades) {
        this.propiedades = propiedades;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public static void main(String args[]) {
        EnviarCorreo env = new EnviarCorreo();
        List<File> ff = new ArrayList();
        System.out.println(env.enviar("edyson51@hotmail.com", "PRUEBA HTML", "<strong> TITULO CON NEGRILLA </Strong > \n  <p> Texto xxxxx </p>", null));

    }
}
