package com.calderon.mikimexiapp.models;

public class PedidoR {


    private String idV;
    private String idC;
    private String destinatario;
    private String descripcion;
    private String precio;
    private String direccionTienda;
    private String direccionEntrega;

    public PedidoR() {

    }

    public String getIdV() {
        return idV;
    }

    public void setIdV(String idV) {
        this.idV = idV;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
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

    public String getDireccionTienda() {
        return direccionTienda;
    }

    public void setDireccionTienda(String direccionTienda) {
        this.direccionTienda = direccionTienda;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    @Override
    public String toString() {
        return "PedidoR{" +
                "idV='" + idV + '\'' +
                ", idC='" + idC + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio='" + precio + '\'' +
                ", direccionTienda='" + direccionTienda + '\'' +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                '}';
    }
}