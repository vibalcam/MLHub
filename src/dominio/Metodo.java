package dominio;

public class Metodo {
    private String nombre;
    private int eficacia;
    private int tiempo;


    public Metodo(String nombre, int eficacia, int tiempo) {
        setNombre(nombre);
        setEficacia(eficacia);
        setTiempo(tiempo);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEficacia(int eficacia) {
        this.eficacia = eficacia;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEficacia() {
        return eficacia;
    }

    public int getTiempo() {
        return tiempo;
    }
}
