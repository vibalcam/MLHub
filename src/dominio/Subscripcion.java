package dominio;

public class Subscripcion implements Comparable<Subscripcion> {
    private static final int NO_ID = -1;

    private int id = NO_ID;
    private String nombre;
    private double precio;
    private int porcentajeOferta;
    private AccessLevel accessLevel;

    public Subscripcion(int id, String nombre, double precio,int porcentajeOferta, AccessLevel accessLevel) {
        this.id = id;
        this.nombre = nombre;
        setPrecio(precio);
        setPorcentajeOferta(porcentajeOferta);
        this.accessLevel = accessLevel;
    }

    public Subscripcion(String nombre, double precio, int accessLevel) {
        this.nombre = nombre;
        this.precio = precio;
        this.accessLevel = new AccessLevel(accessLevel);
    }

    public Subscripcion(int id, String nombre, double precio, int accessLevel) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.accessLevel = new AccessLevel(accessLevel);
    }

    public Subscripcion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Subscripcion(String nombre) {
        this.nombre = nombre;
    }

    public Subscripcion(int id) {
        this.id = id;
    }

    public int getPorcentajeOferta() {
        return porcentajeOferta;
    }

    public void setPorcentajeOferta(int porcentajeOferta) {
        if(porcentajeOferta < 0 || porcentajeOferta > 100)
            throw new IllegalArgumentException("Oferta debe de estar entre 0 y 100");
        this.porcentajeOferta = porcentajeOferta;
    }

    public double getPrecioReal() {
        return precio * (1 - porcentajeOferta/100.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        if(precio < 0)
            throw new IllegalArgumentException("Precio no puede ser negativo");
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public int compareTo(Subscripcion subscripcion) {
        int result = (-1) * getAccessLevel().compareTo(subscripcion.getAccessLevel());
        if(result == 0)
            result = getNombre().compareTo(subscripcion.getNombre());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Subscripcion)
            return getId() == ((Subscripcion) obj).getId();
        else
            return false;
    }
}
