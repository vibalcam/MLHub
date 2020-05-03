package dao;

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
        statement.setInt(5,usuario.getAccessLevel().getId());

        int result = statement.executeUpdate();
        statement.close();
        if(result == 0)
            throw new SQLException("No se ha podido insertar debido a un fallo inesperado");
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
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM productos AS p " +
                "LEFT JOIN access_level AS a ON p.accessLevel=a.id " +
                "WHERE a.id!=0 ORDER BY p.accessLevel DESC");
        ArrayList<Subscripcion> subscripcions = new ArrayList<>();
        while (resultSet.next())
            subscripcions.add(getSubscripcionFromResultSet(resultSet));
        resultSet.close();
        statement.close();
        return subscripcions;
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
            //todo añadir compra

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
            throw new IllegalArgumentException("Operacion no posible: consulte con soporte para mayor informacion");

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
        PreparedStatement statement = connection.prepareStatement("SELECT c.fecha,COUNT(c.id) AS sumCantidad " +
                "FROM compras AS c GROUP BY c.fecha ORDER BY sumCantidad DESC LIMIT ?");
        statement.setInt(1,number);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Compra> list = new ArrayList<>();
        while (resultSet.next())
            list.add(getFechaCompraFromResultSet(resultSet));

        resultSet.close();
        statement.close();
        return list;
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
                resultSet.getString("p.nombre"),//todo get ofertas
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
}
