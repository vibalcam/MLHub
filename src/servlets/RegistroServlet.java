package servlets;

import dao.MLDao;
import dominio.AccessLevel;
import dominio.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegistroServlet", urlPatterns = "/registro")
public class RegistroServlet extends HttpServlet {
    private static final String KEY_ERROR = "error";
    private static final String DUPLICITY_ERROR = "Este usuario ya se encuentra en nuestra base de datos";
    private static final String MSG_UNKNOWN_ERROR = "No se han podido validar sus credenciales: inténtelo más tarde";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        vueltaInicio(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String peticion = request.getParameter("peticion");
        if(peticion == null) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }

        switch (peticion) {
            case "reg":
                crearUsuario(request,response);
                return;
            case "can":
                vueltaInicio(request, response);
                return;
            default:
                vueltaInicio(request, response);
        }
    }

    private void vueltaInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(getServletContext().getContextPath());
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreUsuario = request.getParameter("usuario");
        String pwd = request.getParameter("pwd");
        if(nombreUsuario == null || pwd == null || nombreUsuario.isBlank() || pwd.isBlank()) {
            vueltaInicio(request,response);
            return;
        }

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            if(dao.existsUser(nombreUsuario)){
                request.setAttribute(KEY_ERROR,DUPLICITY_ERROR);
                request.getRequestDispatcher("/").forward(request,response);
                return;
            }

            String name = request.getParameter("name");
            String lastname = request.getParameter("lastname");

            Usuario usuario = new Usuario(name, lastname, new Usuario.Credentials(nombreUsuario, pwd), null);

            dao.addUser(usuario);

            vueltaInicio(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute(KEY_ERROR,MSG_UNKNOWN_ERROR);
            request.getRequestDispatcher("/").forward(request,response);
        } finally {
            if(dao != null) {
                try {
                    dao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}