package dominio;

import java.util.ArrayList;

public class Proyecto {
    private int id;
    private String nombre;
    private ArrayList<Metodo> metodos;
    private String codigo;
    private int idUsuario;

    public Proyecto(int id, String nombre, String codigo, int idUsuario, ArrayList<Metodo> metodos) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.idUsuario = idUsuario;
        this.metodos = metodos;
    }

    public Proyecto(String nombre, String codigo) {
        setNombre(nombre);
        metodos = new ArrayList<>();
        this.codigo = codigo;
    }

    public Proyecto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.metodos = new ArrayList<>();
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


    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Metodo> getMetodos() {
        return metodos;
    }

    public void setMetodos(ArrayList<Metodo> metodos) {
        this.metodos = metodos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
