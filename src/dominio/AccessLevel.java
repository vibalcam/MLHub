package dominio;

public class AccessLevel implements Comparable<AccessLevel> {
    public static final int ADMIN_LEVEL = 0;
    public static final int REGISTERED_LEVEL = 99;
    public static final int PRO_LEVEL = 70;
    public static final int PRIME_LEVEL = 50;

    private int id;
    private boolean subirCodigo = true;
    private boolean accesoResultados = false;
    private boolean subirResultados = false;

    public AccessLevel(int id) {
        this.id = id;
    }

    public AccessLevel(int id, boolean subirCodigo, boolean subirResultados, boolean accesoResultados) {
        setId(id);
        this.subirCodigo = subirCodigo;
        this.accesoResultados = accesoResultados;
        this.subirResultados = subirResultados;
    }

    public static AccessLevel getInstance(int subscription) {
        if (subscription == 3) {
            return new AccessLevel(PRIME_LEVEL);
        } else if (subscription == 2) {
            return new AccessLevel(PRO_LEVEL);
        } else {
            return new AccessLevel(REGISTERED_LEVEL);
        }
    }

    public int getSubscription() {
        if (id == 50) {
            return 3;
        } else if (id == 70) {
            return 2;
        } else {
            return 1;
        }
    }

    public void setId(int id) {
        if (id < ADMIN_LEVEL || id > REGISTERED_LEVEL)
            throw new IllegalArgumentException("Nivel de acceso no valido");
        this.id = id;
    }

    public void setSubirCodigo(boolean subirCodigo) {
        this.subirCodigo = subirCodigo;
    }

    public void setAccesoResultados(boolean accesoResultados) {
        this.accesoResultados = accesoResultados;
    }

    public void setSubirResultados(boolean subirResultados) {
        this.subirResultados = subirResultados;
    }

    public int getId() {
        return id;
    }

    public boolean isSubirCodigo() {
        return subirCodigo;
    }

    public boolean isAccesoResultados() {
        return accesoResultados;
    }

    public boolean isSubirResultados() {
        return subirResultados;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccessLevel)
            return id == ((AccessLevel) obj).id;
        else
            return false;
    }

    @Override
    public int compareTo(AccessLevel accessLevel) {
        return Integer.compare(id, accessLevel.id);
    }
}
