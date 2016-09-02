/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.componentes;

import ec.com.erpxprime.framework.convertidores.UploadStreamedContent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.HashMap;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

public class FileDownloadActionListener implements ActionListener, StateHolder {

    private ValueExpression value;
    private ValueExpression contentDisposition;
    private String path = "";

    public FileDownloadActionListener() {
    }

    public FileDownloadActionListener(String path) {
        this.path = path;
    }

    public FileDownloadActionListener(ValueExpression value) {
        this.value = value;
    }

    @Override
    public void processAction(ActionEvent actionEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        if (value == null) {
            return;
        }

        try {
            path = value.getValue(elContext) + "";
            InputStream stream = null;            
            if (path.isEmpty() == false) //path = value.getValue(elContext) + "";
            {
                try {
                    stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path);
                } catch (Exception e) {
                }
            }
            StreamedContent content = null;
            if (stream != null) {
                content = new DefaultStreamedContent(stream);
            } else {
                path = "";
                try {
                    content = (DefaultStreamedContent) (value.getValue(elContext));
                } catch (Exception e) {
                    try {
                        content = (UploadStreamedContent) (value.getValue(elContext));
                    } catch (Exception e1) {
                    }
                }
            }

            if (content == null) {
                return;
            }

            String mimeType = URLConnection.guessContentTypeFromStream(content.getStream());
            ExternalContext externalContext = facesContext.getExternalContext();
            String contentDispositionValue = contentDisposition != null ? (String) contentDisposition.getValue(elContext) : "attachment";

            externalContext.setResponseContentType(mimeType);
            if (path.isEmpty() && mimeType != null) {
                path = "/archivo." + mimeType.substring(mimeType.lastIndexOf("/") + 1);
            }

            externalContext.setResponseHeader("Content-Disposition", contentDispositionValue + ";filename=\"" + path.substring(path.lastIndexOf("/") + 1) + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();

            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            path = "";
            content.getStream().reset();
            facesContext.responseComplete();
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void restoreState(FacesContext facesContext, Object state) {
        Object values[] = (Object[]) state;

        value = (ValueExpression) values[0];
        contentDisposition = (ValueExpression) values[1];
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        Object values[] = new Object[2];

        values[0] = value;
        values[1] = contentDisposition;

        return ((Object[]) values);
    }

    @Override
    public void setTransient(boolean value) {
    }
}
