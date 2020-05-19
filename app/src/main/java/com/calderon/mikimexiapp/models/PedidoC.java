package com.calderon.mikimexiapp.models;

public class PedidoC {

    private String id;
    private String descripcion;
    private String precio;
    private String email;


    public PedidoC() {
    }

    public PedidoC(String id, String descripcion, String precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "PedidoC{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio='" + precio + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
