/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;


import java.lang.reflect.Method;
import java.util.Calendar;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.terminal.Terminal;

/**
 *
 * @author xedushx
 */
public class TerminalTabla extends Dialogo {

    private boolean boo_logeado = false;
    private Tabla tab_actual;   
    

    public TerminalTabla() {
        this.setId("term_tabla");
        this.getBot_aceptar().setValue("Limpiar");
        this.getBot_aceptar().setIcon("ui-icon-trash");
        this.getBot_aceptar().setOnclick("term.clear();term.focus();");
        this.getBot_aceptar().setUpdate("term");

        this.setTitle("Terminal");
        this.setWidth("65%");
        this.setResizable(false);
        this.setHeight("60%");

        Terminal term = new Terminal();
        term.setId("term");
        term.setWidgetVar("term");
        term.setWelcomeMessage("Terminal de uso exclusivo para los desarrolladores de la aplicación!");
        term.setPrompt("dj@root $");
        term.setCommandHandler(FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{mbe_index.term_tabla.handleCommand}", String.class, new Class[]{String.class, String[].class}));
        term.setWidth(String.valueOf(this.getAnchoPanel() - 10));
        term.setHeight(String.valueOf(this.getAltoPanel() - 20));
        this.setDialogo(term);
        
    }

    public void setTerminalTabla(Tabla tab_tabla) {
        this.tab_actual = tab_tabla;        
    }

    @Override
    public void cerrar() {
        tab_actual = null;
        super.cerrar(); //To change body of generated methods, choose Tools | Templates.
    }

    public String handleCommand(String command, String[] params) {

        if (boo_logeado == false) {
            if (command.equals("psw")) {
                if (params.length == 1) {
                    if (params[0].equals("developer")) {
                        boo_logeado = true;                                            
                        return "Acceso Correcto";
                    }
                } else {
                    return "Clave incorrecta";
                }
            } else {
                return "Ingrese la clave de usuario root";
            }
        } else {
            if (command.equalsIgnoreCase("sql")) {
                String str_sql="";
                for (int i = 0; i < params.length; i++) {
                    if(i>0){
                        str_sql+=" ";
                    }
                    str_sql += params[i];                    
                }
                
                    //Se toma la hora en el inicio del programa
                    long t1, t2, dif;
                    Calendar ahora1 = Calendar.getInstance();
                    t1 = ahora1.getTimeInMillis();
                    String str_mensaje = framework.getConexion().ejecutarSql(str_sql);
                    //Se vuelve a tomar la hora una vez que ha pulsado Intro
                    Calendar ahora2 = Calendar.getInstance();
                    t2 = ahora2.getTimeInMillis();
                    //Se calcula la diferencia de tiempo
                    dif = t2 - t1;
                    if (str_mensaje.isEmpty()) {
                        return "[SQL ] Sentencia ejecutada correctamente. \n Tiempo :" + (double) dif / 1000;
                    } else {
                        return "[Err SQL] " + str_mensaje;
                    }
                
            } else if (command.equalsIgnoreCase("cd")) {
                if (params.length == 1) {
                    UIComponent componente = framework.getComponente(params[0]);
                    if (componente != null) {
                        if (componente.getRendererType() != null && componente.getRendererType().equals("org.primefaces.component.DataTableRenderer")) {
                            tab_actual = (Tabla) componente;                                                        
                            return "Se cambio a "+params[0];
                        } else {
                            return "El componente " + params[0] + " no es de tipo Tabla";
                        }
                    } else {
                        return "No existe el componente " + params[0];
                    }
                } else {
                    return "Parámetros incorrectos del comado " + command;
                }
            } else {

                //no paramater
                Class noparams[] = {};

                //String parameter
                Class[] paramString = new Class[1];
                paramString[0] = String.class;

                //La clase a la que quiero buscarle el método
                Class c = tab_actual.getClass();
                for (Method m : c.getMethods()) {
                    if (command.equalsIgnoreCase(m.getName())) {
                        if (params.length == 0) {
                            try {
                                //call the printIt method
                                Method method = c.getDeclaredMethod(m.getName(), noparams);
                                Object r;
                                r = method.invoke(tab_actual, (Object) null);
                                if (r != null) {
                                    return r.toString();
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage() + " .../..");
                            }
                        }
                    }
                }
            }
        }
        return "Comando no válido";
    }

    @Override
    public void dibujar() {
        boo_logeado = false;
        super.dibujar(); //To change body of generated methods, choose Tools | Templates.
    }

}
