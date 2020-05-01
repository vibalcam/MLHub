package dao;

import dominio.Usuario;

import java.sql.*;

public class MLDao {
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
                "INSERT INTO users " + "(nombre,apellidos,usuario,password,subscripcion) VALUES (?,?,?,?,?)");

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
}
