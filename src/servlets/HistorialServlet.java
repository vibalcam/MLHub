package servlets;

import dao.MLDao;
import dominio.Compra;
import dominio.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "HistorialServlet", urlPatterns = "/inicio/historial")
public class HistorialServlet extends HttpServlet {
    private static final String KEY_ERROR = "error";
    private static final String MSG_UNKNOWN_ERROR = "No se han podido validar sus credenciales: inténtelo más tarde";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            int id = ((Usuario) request.getSession().getAttribute("userLogged")).getId();
            ArrayList<Compra> compras = dao.getCompras(id);
            request.setAttribute("compras", compras);

            request.getRequestDispatcher("/inicio/historial/view").forward(request, response);

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