package dominio;

import java.util.regex.Pattern;

public class Usuario {
    public static final int ADMIN_ID = 0;

    private int id;
    private String nombre;
    private String apellidos;
    private final Credentials credentials;
    private AccessLevel accessLevel;

    public Usuario(int id, String nombre, String apellidos, Credentials credentials, AccessLevel accessLevel) {
        this.id = id;
        setNombre(nombre);
        setApellidos(apellidos);
        this.credentials = credentials;
        this.accessLevel = accessLevel;
    }

    public Usuario(int id, String nombre, String apellidos, Credentials credentials) {
        this(id,nombre, apellidos,credentials,new AccessLevel(AccessLevel.REGISTERED_LEVEL));
    }

    public Usuario(int id) {
        this(id,null,null,null);
    }

    public Usuario(String nombre, String apellidos, Credentials credentials, AccessLevel accessLevel) {
        this(0,nombre,apellidos,credentials, accessLevel);
    }

    public Usuario(Credentials credentials, AccessLevel accessLevel){
        this(0, null, null, credentials, accessLevel);
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
        if (nombre == null)
            throw new IllegalArgumentException("Nombre no puede ser nulo");
        this.nombre = nombre.strip();
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos == null ? null : apellidos.strip();
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getNombreCompleto() {
        return apellidos == null ? nombre : (nombre + " " + apellidos);
    }

    public static class Credentials {
        private static final Pattern PATTERN_NOT_ALLOWED = Pattern.compile("[^A-Za-z-_:0-9]");
        public static final int MAX_LENGTH = 15;

        private String nombreUsuario;
        private String password;

        public Credentials(String nombreUsuario, String password) {
            setNombreUsuario(nombreUsuario);
            setPassword(password);
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public void setNombreUsuario(String nombreUsuario) {
            if(nombreUsuario == null)
                throw new IllegalArgumentException("Usuario no puede ser nulo");

            nombreUsuario = nombreUsuario.strip();
            if(nombreUsuario.length() > MAX_LENGTH)
                throw new IllegalArgumentException("Longitud de nombre de usuario no puede exceder " + MAX_LENGTH);
            else if(PATTERN_NOT_ALLOWED.matcher(nombreUsuario).find())
                throw new IllegalArgumentException("Usuario contiene caracteres invalidos");

            this.nombreUsuario = nombreUsuario;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            if(password == null)
                throw new IllegalArgumentException("Password no puede ser nulo");
            else if(password.length() > MAX_LENGTH)
                throw new IllegalArgumentException("Longitud de Password no puede exceder " + MAX_LENGTH);
            else if(PATTERN_NOT_ALLOWED.matcher(password).find())
                throw new IllegalArgumentException("Password contiene caracteres invalidos");

            this.password = password;
        }
    }
}
