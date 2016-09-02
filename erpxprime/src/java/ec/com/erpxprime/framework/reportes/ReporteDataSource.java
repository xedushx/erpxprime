package ec.com.erpxprime.framework.reportes;
import ec.com.erpxprime.framework.aplicacion.TablaGenerica;
import ec.com.erpxprime.framework.componentes.Tabla;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author xedushx
 */
public class ReporteDataSource implements JRDataSource {

    private Tabla tabla;
    private TablaGenerica tablaGenerica;
    private int indice = -1;
    private List<DetalleReporte> lista;

    public ReporteDataSource(Tabla tabla) {
        this.tabla = tabla;
    }

    public ReporteDataSource(TablaGenerica tablaGenerica) {
        this.tablaGenerica = tablaGenerica;
    }

    public ReporteDataSource(List<DetalleReporte> lista) {
        this.lista = lista;
    }

    @Override
    public boolean next() throws JRException {
        if (tabla != null) {
            return ++indice < tabla.getTotalFilas();
        } else if (tablaGenerica != null) {
            return ++indice < tablaGenerica.getTotalFilas();
        } else {
            return ++indice < lista.size();
        }

    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object valor = null;
        if (tabla != null) {
            for (int i = 0; i < tabla.getTotalColumnas(); i++) {
                if (tabla.getColumnas()[i].getNombre().equalsIgnoreCase(jrField.getName())) {
                    valor = tabla.getValor(indice, tabla.getColumnas()[i].getNombre());
                    break;
                }
            }
        } else if (tablaGenerica != null) {
            for (int i = 0; i < tablaGenerica.getTotalColumnas(); i++) {
                if (tablaGenerica.getColumnas()[i].getNombre().equalsIgnoreCase(jrField.getName())) {
                    valor = tablaGenerica.getValor(indice, tablaGenerica.getColumnas()[i].getNombre());
                    break;
                }
            }
        } else if (lista != null) {
            for (int i = 0; i < lista.get(indice).getNombreColumna().length; i++) {
                if (lista.get(indice).getNombreColumna()[i].equalsIgnoreCase(jrField.getName())) {
                    valor = lista.get(indice).getValor()[i];
                    break;
                }
            }
        }
        return valor;
    }

    public Tabla getTabla() {
        return tabla;
    }

    public void setTabla(Tabla tabla) {
        this.tabla = tabla;
    }

    public TablaGenerica getTablaGenerica() {
        return tablaGenerica;
    }

    public void setTablaGenerica(TablaGenerica tablaGenerica) {
        this.tablaGenerica = tablaGenerica;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public List<DetalleReporte> getLista() {
        return lista;
    }

    public void setLista(List<DetalleReporte> lista) {
        this.lista = lista;
    }

}
