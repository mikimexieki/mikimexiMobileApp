package com.calderon.mikimexiapp.models;

public class PedidoC {

    public String descripcion;
    public String precio;

    public PedidoC() {
    }

    public PedidoC(String descripcion, String precio) {
        this.descripcion = descripcion;
        this.precio = precio;
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

    @Override
    public String toString() {
        return "PedidoC{" +
                "descripcion='" + descripcion + '\'' +
                ", precio='" + precio + '\'' +
                '}';
    }
}
