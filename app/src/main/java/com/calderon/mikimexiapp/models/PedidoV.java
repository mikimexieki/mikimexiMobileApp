package com.calderon.mikimexiapp.models;

public class PedidoV {

    private String id;
    private String descripcion;
    private String direccion;
    private String destinatario;

    public PedidoV() {
    }

    public PedidoV(String id, String descripcion, String direccion, String destinatario) {
        this.id = id;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.destinatario = destinatario;
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

    @Override
    public String toString() {
        return "PedidoV{" +
                "descripcion='" + descripcion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", destinatario='" + destinatario + '\'' +
                '}';
    }
}
