package dominio;

public class Subscripcion {
    private int id;
    private String nombre;
    private double precio;
    private int porcentajeOferta;
    private AccessLevel accessLevel;

    public Subscripcion(int id, String nombre, double precio, AccessLevel accessLevel) {
        this.id = id;
        this.nombre = nombre;
        setPrecio(precio);
        this.accessLevel = accessLevel;
    }

    public Subscripcion(int id, String nombre, int precio, int accessLevel) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.accessLevel = new AccessLevel(accessLevel);
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
}
