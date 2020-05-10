package servlets;

import dao.MLDao;
import dominio.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "EditarServlet", urlPatterns = "/inicio/modificar")
public class EditarServlet extends HttpServlet {
    private static final String KEY_ERROR = "error";
    private static final String DUPLICITY_ERROR = "Este usuario ya se encuentra en nuestra base de datos";
    private static final String MSG_UNKNOWN_ERROR = "No se han podido validar sus credenciales: inténtelo más tarde";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute(AccesoServlet.USER_LOGGED);
        if (usuario.getId() == Usuario.ADMIN_ID)
            response.sendRedirect(getServletContext().getContextPath() + "/inicio/admin");
        else
            response.sendRedirect(getServletContext().getContextPath() + "/inicio/modificar/view");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String peticion = request.getParameter("peticion");
        if (peticion == null) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }

        switch (peticion) {
            case "save":
                editarUsuario(request, response);
                return;
            case "delete":
                eliminarUsuario(request, response);
                return;
            default:
                vueltaInicio(request, response);
        }
    }

    private void vueltaInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(getServletContext().getContextPath() + "/inicio");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            int id = ((Usuario) request.getSession().getAttribute("userLogged")).getId();
            dao.deleteUser(id);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute(KEY_ERROR, MSG_UNKNOWN_ERROR);
            request.getRequestDispatcher("/").forward(request, response);
        } finally {
            if (dao != null) {
                try {
                    dao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        request.getSession().invalidate();
        response.sendRedirect(getServletContext().getContextPath());
        return;
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreUsuario = request.getParameter("usuario");
        String pwd = request.getParameter("pwd");
        if (nombreUsuario == null || pwd == null || nombreUsuario.isBlank() || pwd.isBlank()) {
            vueltaInicio(request, response);
            return;
        }

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            int id = ((Usuario) request.getSession().getAttribute("userLogged")).getId();
            String name = request.getParameter("name");
            String lastname = request.getParameter("lastname");

            Usuario usuario = new Usuario(id, name, lastname, new Usuario.Credentials(nombreUsuario, pwd), null);
            dao.editUser(usuario);
            request.getSession().setAttribute("userLogged", usuario);

            vueltaInicio(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute(KEY_ERROR, MSG_UNKNOWN_ERROR);
            request.getRequestDispatcher("/").forward(request, response);
        } finally {
            if (dao != null) {
                try {
                    dao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
