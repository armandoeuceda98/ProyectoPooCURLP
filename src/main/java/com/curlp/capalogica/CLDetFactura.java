
package com.curlp.capalogica;

public class CLDetFactura {
    private int codDetFactura;
    private int cantidad;
    private double precio;
    private int codProducto;
    private String nomProducto;
    private double total;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }
    private int codFactura;

    public int getCodDetFactura() {
        return codDetFactura;
    }

    public void setCodDetFactura(int codDetFactura) {
        this.codDetFactura = codDetFactura;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(int codProducto) {
        this.codProducto = codProducto;
    }

    public int getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(int codFactura) {
        this.codFactura = codFactura;
    }
}
