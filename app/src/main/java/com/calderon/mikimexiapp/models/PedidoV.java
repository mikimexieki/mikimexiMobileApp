package com.calderon.mikimexiapp.models;

public class PedidoV {

    private String id;
    private String descripcion;
    private String direccion;
    private String destinatario;
    private boolean enviando;
    private String precio;
    private String rfc;

    public PedidoV() {
    }

    public PedidoV(String id, String descripcion, String direccion, String destinatario, boolean enviando, String precio, String rfc) {
        this.id = id;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.destinatario = destinatario;
        this.enviando = enviando;
        this.precio = precio;
        this.rfc = rfc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public boolean isEnviando() {
        return enviando;
    }

    public void setEnviando(boolean enviando) {
        this.enviando = enviando;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "PedidoV{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", enviando=" + enviando +
                ", precio='" + precio + '\'' +
                ", rfc='" + rfc + '\'' +
                '}';
    }
}
