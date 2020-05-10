package dominio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Compra {
    public static final int NO_ID = -1;

    private int id;
    private Calendar fecha;
    private String nombre;
    private int cantidad;
    private int cliente;

    public Compra(int id, Calendar fecha, String nombre, int cantidad, int cliente) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        setCantidad(cantidad);
        this.cliente = cliente;
    }

    /**
     * Usado para guardar datos colectivos (suma diaria de compras...)
     * @param fecha fecha colectiva de la compra, si es que existe
     * @param nombre nombre del producto comprado, si es que existe
     * @param cantidad cantidad comprada
     */
    public Compra(Calendar fecha, String nombre, int cantidad) {
        this.id = NO_ID;
        this.fecha = fecha;
        this.nombre = nombre;
        setCantidad(cantidad);
        this.cliente = NO_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public String getFechaString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-mm ");
        return dateFormat.format(fecha.getTime());
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0)
            throw new IllegalArgumentException("Cantidad no puede ser negativa");
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }
}
