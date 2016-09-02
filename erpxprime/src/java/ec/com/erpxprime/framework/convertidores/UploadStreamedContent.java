/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.erpxprime.framework.convertidores;

import java.io.InputStream;
import java.io.Serializable;
import org.primefaces.model.StreamedContent;

/**
 * Default implementation of a StreamedContent
 */
public class UploadStreamedContent implements StreamedContent, Serializable {

    private InputStream stream;

    private String contentType;

    private String name;

    private byte[] bytes;

    public UploadStreamedContent() {
    }

    public UploadStreamedContent(InputStream stream) {
        this.stream = stream;
    }

    public UploadStreamedContent(InputStream stream, String contentType) {
        this.contentType = contentType;
        this.stream = stream;
    }

    public UploadStreamedContent(InputStream stream, String contentType, String name) {
        this.contentType = contentType;
        this.stream = stream;
        this.name = name;
    }

    public UploadStreamedContent(InputStream stream, String contentType, byte[] bytes) {
        this.contentType = contentType;
        this.stream = stream;        
        this.bytes=bytes;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

}
