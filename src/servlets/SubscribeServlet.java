package servlets;

import dao.MLDao;
import dominio.Subscripcion;
import dominio.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "SubscribeServlet", urlPatterns = "/inicio/subscripcion/procesar")
public class SubscribeServlet extends HttpServlet {
    private static final String KEY_ERROR = "error";
    private static final String MSG_ILLEGAL_ACCESS = "Error desconocido: consulte con soporte";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subsParameter = request.getParameter("subsId");
        if(subsParameter == null) {
            request.setAttribute(KEY_ERROR,MSG_ILLEGAL_ACCESS);
            request.getRequestDispatcher("/inicio/subscripcion").forward(request,response);
            return;
        }

        MLDao dao = null;
        try {
            int subsId = Integer.parseInt(subsParameter);
            dao = MLDao.getInstance();
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute(AccesoServlet.USER_LOGGED);
            if(dao.subscribeUser(usuario, new Subscripcion(subsId))) {
                session.setAttribute(AccesoServlet.USER_LOGGED,dao.getUserInfoById(usuario.getId()));
                response.sendRedirect(getServletContext().getContextPath() + "/inicio");
            } else {
                request.setAttribute(KEY_ERROR,"Ya tiene dicha subscripción activa");
                request.setAttribute("subscripciones",dao.getSubscripciones());
                request.getRequestDispatcher("/inicio/subscripcion").forward(request,response);
            }
        } catch (NumberFormatException|SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute(KEY_ERROR,MSG_ILLEGAL_ACCESS);
            request.getRequestDispatcher("/inicio/subscripcion").forward(request,response);
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
