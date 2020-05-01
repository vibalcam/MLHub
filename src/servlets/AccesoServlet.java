package servlets;

import dao.MLDao;
import dominio.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AccesoServlet", urlPatterns = "/acceso")
public class AccesoServlet extends HttpServlet {
    public static final String USER_LOGGED = "userLogged";
    private static final String KEY_ERROR = "error";
    private static final String MSG_ILLEGAL_ACCESS = "Illegal access: insert your username and password";
    private static final String MSG_CREDENTIALS_ERROR = "Usuario y/o contraseña incorrectos";
    private static final String MSG_UNKNOWN_ERROR = "No se han podido validar sus credenciales: inténtelo más tarde";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        accesoIlegal(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String peticion = request.getParameter("peticion");
        if(peticion == null) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }

        switch (peticion) {
            case "acc":
                acceder(request,response);
                return;
            case "reg":
                response.sendRedirect(getServletContext().getContextPath() + "/register");
                return;
            default:
                accesoIlegal(request, response);
        }
    }

    private void accesoIlegal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(KEY_ERROR,MSG_ILLEGAL_ACCESS);
        request.getRequestDispatcher("/").forward(request,response);
    }

    private void acceder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String pwd = request.getParameter("pwd");
        if(usuario == null || pwd == null || usuario.isBlank() || pwd.isBlank()) {
            accesoIlegal(request,response);
            return;
        }

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();
            if(dao.checkUser(new Usuario.Credentials(usuario,pwd))) {
                if(Boolean.parseBoolean(request.getParameter("remember"))) {
                    Cookie cookie = new Cookie("remember",usuario);
                    cookie.setMaxAge(Integer.MAX_VALUE);
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
                HttpSession session = request.getSession();
                session.setAttribute(USER_LOGGED,usuario);
                response.sendRedirect(getServletContext().getContextPath() + "/inicio");
            } else {
                request.setAttribute(KEY_ERROR,MSG_CREDENTIALS_ERROR);
                request.getRequestDispatcher("/").forward(request,response);
            }
            dao.close();
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
