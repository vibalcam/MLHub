package dao;

import com.sun.source.tree.MemberReferenceTree;
import dominio.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MLDao {
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static MLDao INSTANCE = null;

    private Connection connection;

    public static MLDao getInstance() throws ClassNotFoundException, SQLException {
        if(INSTANCE == null)
            INSTANCE = new MLDao();
        INSTANCE.connect();

        return INSTANCE;
    }

    private void connect() throws ClassNotFoundException, SQLException {
        if(connection == null || connection.isClosed() || !connection.isValid(0)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/pat_1" +
                    "?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC", "pat", "pat");
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void addUser(Usuario usuario) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users (nombre,apellidos,usuario,password,subscripcion) VALUES (?,?,?,?,?)");

        statement.setString(1,usuario.getNombre());
        statement.setString(2,usuario.getApellidos());
        statement.setString(3,usuario.getCredentials().getNombreUsuario());
        statement.setString(4,usuario.getCredentials().getPassword());
        statement.setInt(5,1);

        int result = statement.executeUpdate();
        statement.close();
        if(result == 0){
            throw new SQLException("No se ha podido insertar debido a un fallo inesperado");
        }

    }

    public void deleteUser(int id) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "DELETE FROM users WHERE ID=?"
        );

        stat.setInt(1,id);
        stat.execute();
        stat.close();
    }

    public void editUser(Usuario usuario) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "UPDATE users SET nombre = ?, apellidos = ?, usuario = ?, password = ? WHERE id = ?"
        );

        stat.setString(1,usuario.getNombre());
        stat.setString(2,usuario.getApellidos());
        stat.setString(3,usuario.getCredentials().getNombreUsuario());
        stat.setString(4,usuario.getCredentials().getPassword());
        stat.setInt(5,usuario.getId());

        stat.execute();
        stat.close();
    }

    public boolean checkUser(Usuario.Credentials credentials) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(usuario) AS 'count' FROM users WHERE usuario=? AND password=? GROUP BY usuario");
        statement.setString(1,credentials.getNombreUsuario());
        statement.setString(2,credentials.getPassword());

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.first()) {
            resultSet.first();
            int resultado = resultSet.getInt("count");
            resultSet.close();
            statement.close();

            if (resultado > 1) //should never happen
                throw new SQLException("Resultado inesperado: más de una coincidencia");
            else
                return resultado == 1;
        } else
            return false;
    }

    public boolean existsUser(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(usuario) AS 'count' FROM users WHERE usuario=? GROUP BY usuario");
        statement.setString(1,username);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.first()) {
            resultSet.first();
            int resultado = resultSet.getInt("count");
            resultSet.close();
            statement.close();

            if (resultado > 1) //should never happen
                throw new SQLException("Resultado inesperado: más de una coincidencia");
            else
                return resultado == 1;
        } else
            return false;
    }

    public Usuario checkAndGetUserInfo(Usuario.Credentials credentials) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT u.id,u.nombre,u.apellidos,a.id,a.subirCodigo," +
                "a.subirResultados,a.accesoResultados " +
                "FROM users AS u LEFT JOIN productos AS p ON u.subscripcion=p.id " +
                "LEFT JOIN access_level AS a ON p.accessLevel=a.id " +
                "WHERE u.usuario=? AND u.password=?");
        statement.setString(1,credentials.getNombreUsuario());
        statement.setString(2,credentials.getPassword());

        ResultSet resultSet = statement.executeQuery();
        Usuario usuario = resultSet.first() ? getUserInfoFromResultSet(resultSet) : null;
        resultSet.close();
        statement.close();
        return usuario;
    }

    public List<Subscripcion> getSubscripciones() throws SQLException {
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM productos AS p " +
//                "LEFT JOIN access_level AS a ON p.accessLevel=a.id " +
//                "WHERE a.id!=0 ORDER BY p.accessLevel DESC");
//        ArrayList<Subscripcion> subscripcions = new ArrayList<>();
//        while (resultSet.next())
//            subscripcions.add(getSubscripcionFromResultSet(resultSet));
//        resultSet.close();
//        statement.close();
//        return subscripcions;
        return getSubscripciones("%");
    }

    public List<Subscripcion> getSubscripciones(String filtro) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM productos AS p " +
                "LEFT JOIN access_level AS a ON p.accessLevel=a.id " +
                "WHERE a.id!=0 AND p.nombre LIKE ? ORDER BY p.accessLevel DESC");
        statement.setString(1,filtro);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Subscripcion> subscripcions = new ArrayList<>();
        while (resultSet.next())
            subscripcions.add(getSubscripcionFromResultSet(resultSet));
        resultSet.close();
        statement.close();
        return subscripcions;
    }

    public int getIdSubscripcion(int id) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "SELECT subscripcion FROM users WHERE id = ?"
        );

        stat.setInt(1,id);
        ResultSet result = stat.executeQuery();
        result.next();
        int subscripcion = result.getInt("subscripcion");
        result.close();
        stat.close();
        return subscripcion;

    }

    public int getSubscripcion(int id) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "SELECT subscripcion FROM users WHERE id = ?"
        );

        stat.setInt(1,id);

        ResultSet result = stat.executeQuery();
        result.next();
        int subscripcion = result.getInt("subscripcion");
        result.close();
        stat.close();
        return subscripcion;
    }

    public boolean subscribeNewUser(Usuario usuario, Subscripcion subscripcion) throws SQLException {
        // Se modifica solo si no tiene la subscripcion ya y es el usuario
        try {
            // Prepare transaction
            connection.setAutoCommit(false);

            PreparedStatement stat = connection.prepareStatement(
                    "SELECT id FROM users WHERE usuario = ?"
            );
            stat.setString(1,usuario.getCredentials().getNombreUsuario());
            ResultSet resultSet = stat.executeQuery();
            resultSet.next();
            int id = resultSet.getInt("id");
            resultSet.close();

            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO compras (fecha,producto,cliente) " +
                    "VALUES (?,?,?)");
            addStatement.setDate(1, new Date(new java.util.Date().getTime()),CALENDAR);
            addStatement.setInt(2, subscripcion.getId());
            addStatement.setInt(3, id);

            int result = addStatement.executeUpdate();
            addStatement.close();

            if (result == 1) {
                connection.commit();
                return true;
            } else
                throw new SQLException("Unknown error when subscribing");

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean subscribeUser(Usuario usuario, Subscripcion subscripcion) throws SQLException {
        // Se modifica solo si no tiene la subscripcion ya y es el usuario
        try {
            // Prepare transaction
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("UPDATE users SET subscripcion=? " +
                    "WHERE id!=" + Usuario.ADMIN_ID + " AND id=? AND subscripcion!=?");
            statement.setInt(1, subscripcion.getId());
            statement.setInt(2, usuario.getId());
            statement.setInt(3, subscripcion.getId());

            int result = statement.executeUpdate();
            statement.close();

            if (result == 1) {
                PreparedStatement addStatement = connection.prepareStatement("INSERT INTO compras (fecha,producto,cliente) " +
                        "VALUES (?,?,?)");
                addStatement.setDate(1, new Date(new java.util.Date().getTime()),CALENDAR);
                addStatement.setInt(2, subscripcion.getId());
                addStatement.setInt(3, usuario.getId());

                result = addStatement.executeUpdate();
                addStatement.close();

                if (result == 1) {
                    connection.commit();
                    return true;
                } else
                    throw new SQLException("Unknown error when subscribing");
            } else
                return false;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Usuario getUserInfoById(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT u.id,u.nombre,u.apellidos,a.id,a.subirCodigo," +
                "a.subirResultados,a.accesoResultados " +
                "FROM users AS u LEFT JOIN productos AS p ON u.subscripcion=p.id " +
                "LEFT JOIN access_level AS a ON p.accessLevel=a.id " +
                "WHERE u.id=?");
        statement.setInt(1,id);

        ResultSet resultSet = statement.executeQuery();
        Usuario usuario = resultSet.first() ? getUserInfoFromResultSet(resultSet) : null;
        resultSet.close();
        statement.close();
        return usuario;
    }

    public void updateUserInfo(Usuario usuario) throws SQLException {
        if(usuario.getId() == Usuario.ADMIN_ID || usuario.getAccessLevel().getId() == AccessLevel.ADMIN_LEVEL)
            throw new IllegalArgumentException("No se puede modificar el usuario admin ni dar acceso de admin");

        PreparedStatement modifyStatement = connection.prepareStatement(
                "UPDATE users SET nombre=?,apellidos=?,subscripcion=? WHERE id=?");
        modifyStatement.setString(1,usuario.getNombre());
        modifyStatement.setString(2,usuario.getApellidos());
        modifyStatement.setInt(3,usuario.getAccessLevel().getId());
        modifyStatement.setInt(4,usuario.getId());

        if(modifyStatement.executeUpdate() > 1)
            throw new SQLException("Resultado inesperado: más de una coincidencia");

        modifyStatement.close();
        modifyStatement.close();
    }

    public List<Compra> getSubscripcionesMasCompradas(int number) throws SQLException {
//        PreparedStatement statement = connection.prepareStatement("SELECT p.nombre, COUNT(c.id) AS sumCantidad " +
//                "FROM compras AS c LEFT JOIN productos AS p ON p.id=c.producto GROUP BY p.nombre ORDER BY sumCantidad DESC LIMIT ?");
//        statement.setInt(1,number);
//
//        ResultSet resultSet = statement.executeQuery();
//        ArrayList<Compra> list = new ArrayList<>();
//        while (resultSet.next())
//            list.add(getProductoCompradoFromResultSet(resultSet));
//
//        resultSet.close();
//        statement.close();
//        return list;
        return getSubscripcionesMasCompradas(number,null,null);
    }

    public List<Compra> getSubscripcionesMasCompradas(int number, Calendar inicio, Calendar fin) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT p.nombre, COUNT(c.id) AS sumCantidad " +
                "FROM compras AS c LEFT JOIN productos AS p ON p.id=c.producto WHERE c.fecha BETWEEN ? AND ? " +
                "GROUP BY p.nombre ORDER BY sumCantidad DESC LIMIT ?");
        statement.setDate(1,inicio == null ? new Date(1) : new Date(inicio.getTimeInMillis()),CALENDAR);
        statement.setDate(2,fin == null ? new Date(Calendar.getInstance().getTimeInMillis()) : new Date(fin.getTimeInMillis()), CALENDAR);
        statement.setInt(3,number);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Compra> list = new ArrayList<>();
        while (resultSet.next())
            list.add(getProductoCompradoFromResultSet(resultSet));

        resultSet.close();
        statement.close();
        return list;
    }

    public List<Compra> getFechaMaxCompras(int number) throws SQLException {
//        PreparedStatement statement = connection.prepareStatement("SELECT c.fecha,COUNT(c.id) AS sumCantidad " +
//                "FROM compras AS c GROUP BY c.fecha ORDER BY sumCantidad DESC LIMIT ?");
//        statement.setInt(1,number);
//
//        ResultSet resultSet = statement.executeQuery();
//        ArrayList<Compra> list = new ArrayList<>();
//        while (resultSet.next())
//            list.add(getFechaCompraFromResultSet(resultSet));
//
//        resultSet.close();
//        statement.close();
//        return list;
        return getFechaMaxCompras(number,null,null);
    }

    public List<Compra> getFechaMaxCompras(int number, Calendar inicio, Calendar fin) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT c.fecha,COUNT(c.id) AS sumCantidad " +
                "FROM compras AS c WHERE c.fecha BETWEEN ? AND ? GROUP BY c.fecha ORDER BY sumCantidad DESC LIMIT ?");
        statement.setDate(1,inicio == null ? new Date(1) : new Date(inicio.getTimeInMillis()),CALENDAR);
        statement.setDate(2,fin == null ? new Date(Calendar.getInstance().getTimeInMillis()) : new Date(fin.getTimeInMillis()), CALENDAR);
        statement.setInt(3,number);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Compra> list = new ArrayList<>();
        while (resultSet.next())
            list.add(getFechaCompraFromResultSet(resultSet));

        resultSet.close();
        statement.close();
        return list;
    }

    public ArrayList<Compra> getCompras(int cliente) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "SELECT * FROM compras WHERE cliente = ?"
        );
        stat.setInt(1,cliente);

        ResultSet result = stat.executeQuery();
        ArrayList<Compra> compras = new ArrayList<>();
        while(result.next()){
            String nombreProducto;
            int producto = result.getInt("producto");
            if(producto==3){
                nombreProducto = "Prime";
            } else if(producto==2){
                nombreProducto = "Pro";
            } else{
                nombreProducto = "Gratuito";
            }
            compras.add(new Compra(calendarFromSQLToDate(result.getDate("fecha")),nombreProducto,1));
        }

        result.close();
        stat.close();
        return compras;

    }

    // Cambios a productos
    public void updateProducto(Subscripcion subscripcion) throws SQLException {
        if(subscripcion.getAccessLevel().getId() == AccessLevel.ADMIN_LEVEL)
            throw new IllegalArgumentException("No se puede asignar acceso de nivel de admin");
        else if(subscripcion.getNombre() == null || subscripcion.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre no puede estar en blanco");

        PreparedStatement modifyStatement = connection.prepareStatement(
                "UPDATE productos SET nombre=?,precio=?,accessLevel=? WHERE id=?");
        modifyStatement.setString(1,subscripcion.getNombre());
        modifyStatement.setDouble(2,subscripcion.getPrecio());
        modifyStatement.setInt(3,subscripcion.getAccessLevel().getId());
        modifyStatement.setInt(4,subscripcion.getId());

        if(modifyStatement.executeUpdate() > 1)
            throw new SQLException("Resultado inesperado: más de una coincidencia");

        modifyStatement.close();
    }

    public void deleteProducto(Subscripcion subscripcion) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM productos WHERE id=?");
        statement.setInt(1,subscripcion.getId());

        if(statement.executeUpdate() > 1)
            throw new SQLException("Resultado inesperado: más de una coincidencia");
        statement.close();
    }

    public void addProducto(Subscripcion subscripcion) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO productos (nombre,precio,accessLevel) VALUES (?,?,?)");

        statement.setString(1,subscripcion.getNombre());
        statement.setDouble(2,subscripcion.getPrecio());
        statement.setInt(3,subscripcion.getAccessLevel().getId());

        int result = statement.executeUpdate();
        statement.close();
        if(result == 0)
            throw new SQLException("No se ha podido insertar debido a un fallo inesperado");
    }

    public boolean updateOferta(Subscripcion subscripcion) throws SQLException {
        PreparedStatement modifyStatement = connection.prepareStatement(
                "UPDATE productos SET porcentajeOferta=? WHERE id=?");
        modifyStatement.setInt(1,subscripcion.getPorcentajeOferta());
        modifyStatement.setInt(2,subscripcion.getId());

        int result = modifyStatement.executeUpdate();
        modifyStatement.close();

        if(result > 1)
            throw new SQLException("Resultado inesperado: más de una coincidencia");
        else
            return result==1;
    }

    public boolean updateOfertaByName(Subscripcion subscripcion) throws SQLException {
        PreparedStatement modifyStatement = connection.prepareStatement(
                "UPDATE productos SET porcentajeOferta=? WHERE nombre=?");
        modifyStatement.setInt(1,subscripcion.getPorcentajeOferta());
        modifyStatement.setString(2,subscripcion.getNombre());

        int result = modifyStatement.executeUpdate();
        modifyStatement.close();

        if(result > 1)
        throw new SQLException("Resultado inesperado: más de una coincidencia");
        else
            return result==1;
    }

    // Helper methods
    private Calendar calendarFromSQLToDate(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        return calendar;
    }

    private Compra getFechaCompraFromResultSet(ResultSet resultSet) throws SQLException {
        return new Compra(calendarFromSQLToDate(resultSet.getDate("c.fecha")),null,resultSet.getInt("sumCantidad"));
    }

    private Compra getProductoCompradoFromResultSet(ResultSet resultSet) throws SQLException {
        return new Compra(null,resultSet.getString("p.nombre"), resultSet.getInt("sumCantidad"));
    }

    private Subscripcion getSubscripcionFromResultSet(ResultSet resultSet) throws SQLException {
        return new Subscripcion(
                resultSet.getInt("p.id"),
                resultSet.getString("p.nombre"),
                resultSet.getDouble("p.precio"),
                resultSet.getInt("p.porcentajeOferta"),
                getAccessLevelFromResultSet(resultSet));
    }

    private Usuario getUserInfoFromResultSet(ResultSet resultSet) throws SQLException {
        return new Usuario(resultSet.getInt("u.id"),
                resultSet.getString("u.nombre"),
                resultSet.getString("u.apellidos"),
                null,
                getAccessLevelFromResultSet(resultSet));
    }

    private AccessLevel getAccessLevelFromResultSet(ResultSet resultSet) throws SQLException {
        return new AccessLevel(resultSet.getInt("a.id"),resultSet.getBoolean("a.subirCodigo"),
                resultSet.getBoolean("a.subirResultados"),resultSet.getBoolean("a.accesoResultados"));
    }

    public void addProyecto(String nombre, int usuario) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO proyectos (nombre, usuario, codigo) VALUES (?,?,?)"
        );

        statement.setString(1, nombre);
        statement.setInt(2,usuario);
        statement.setString(3,"");

        int result = statement.executeUpdate();
        statement.close();
        if(result==0)
            throw new SQLException("No se ha podido insertar debido a un fallo inesperado");
    }

    public boolean updateCodigo(String nombre, String codigo) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "UPDATE proyectos SET codigo=? WHERE nombre=?");
        stat.setString(1,codigo);
        stat.setString(2,nombre);

        int result = stat.executeUpdate();
        stat.close();

        if(result > 1)
            throw new SQLException("Resultado inesperado: más de una coincidencia");
        else
            return result==1;
    }

    public String getCodigo(String nombre) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "SELECT codigo FROM proyectos WHERE nombre = ?"
        );
        stat.setString(1,nombre);

        ResultSet result = stat.executeQuery();
        result.next();
        String codigo = result.getString("codigo");
        result.close();
        stat.close();
        return codigo;
    }

    public ArrayList<Proyecto> getAllProyectos() throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                    "SELECT nombre,codigo FROM proyectos"
        );

        ResultSet result = stat.executeQuery();
        ArrayList<Proyecto> proyectos = new ArrayList<>();

        while(result.next()){
            proyectos.add(new Proyecto(result.getString("nombre"), result.getString("codigo")));
        }

        return proyectos;

    }

    public ArrayList<Proyecto> getSomeProyectos(String filtro) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "SELECT nombre FROM proyectos WHERE nombre LIKE ?"
        );

        stat.setString(1,"%"+filtro+"%");

        ResultSet result = stat.executeQuery();
        ArrayList<Proyecto> proyectos = new ArrayList<>();

        while(result.next()){
            proyectos.add(new Proyecto(result.getString("nombre")));
        }

        return proyectos;
    }

    public int getUsuario(String nombre) throws SQLException {
        PreparedStatement stat = connection.prepareStatement(
                "SELECT usuario FROM proyectos WHERE nombre = ?"
        );

        stat.setString(1,nombre);
        ResultSet result = stat.executeQuery();
        result.next();

        return result.getInt("usuario");
    }

    public ArrayList<Metodo> getAllMetodos(String proyecto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM metodos WHERE proyecto = ?");

        statement.setString(1,proyecto);

        ResultSet result = statement.executeQuery();
        ArrayList<Metodo> metodos = new ArrayList<>();

        while(result.next()){
            metodos.add(new Metodo(result.getString("nombre"), result.getInt("eficacia"), result.getInt("tiempo")));
        }
        result.close();
        statement.close();
        return metodos;
    }

    public void addMetodo(Metodo metodo, String proyecto) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO metodos (nombre,eficacia,tiempo,proyecto) VALUES (?,?,?,?)");

        statement.setString(1,metodo.getNombre());
        statement.setInt(2,metodo.getEficacia());
        statement.setInt(3,metodo.getTiempo());
        statement.setString(4,proyecto);


        int result = statement.executeUpdate();
        statement.close();
        if(result == 0)
            throw new SQLException("No se ha podido insertar debido a un fallo inesperado");
    }


}
