package servlets;

import dao.MLDao;
import dominio.Usuario;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

@WebServlet(name = "AdminServlet", urlPatterns = "/inicio/admin/procesar")
public class AdminServlet extends HttpServlet {
    public static final String ATRIB_MAS_COMPRADOS = "masComprados";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action == null)
            return;

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            switch (action) {
                case "getMaxProductos":
                    request.setAttribute(ATRIB_MAS_COMPRADOS,dao.getSubscripcionesMasCompradas(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasProductos.jsp").forward(request,response);
                    return;
            }

            dao.close();
        } catch (SQLException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
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
