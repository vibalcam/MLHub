package dominio;

public class ProductoComprado implements Comparable<ProductoComprado> {
    private String nombre;
    private int cantidad;

    public ProductoComprado(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int compareTo(ProductoComprado productoComprado) {
        int resultado = Integer.compare(cantidad, productoComprado.cantidad);
        return resultado == 0 ? nombre.compareTo(productoComprado.nombre) : resultado;
    }
}
