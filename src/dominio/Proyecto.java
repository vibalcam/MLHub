package dominio;

import java.util.ArrayList;

public class Proyecto {
    private int id;
    private String nombre;
    private ArrayList<Metodo> metodos;
    private String codigo;

    public Proyecto(String nombre, String codigo){
        setNombre(nombre);
        metodos = new ArrayList<>();
        this.codigo = codigo;
    }

    public Proyecto(String nombre) {
        this(nombre, "");
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addMetodo(Metodo metodo){
        metodos.add(metodo);
    }

    public int getId(){
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre(){
        return nombre;
    }

    public ArrayList<Metodo> getMetodos(){
        return metodos;
    }
}
