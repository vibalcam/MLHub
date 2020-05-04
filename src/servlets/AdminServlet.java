package servlets;

import dao.MLDao;
import dominio.Subscripcion;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet(name = "AdminServlet", urlPatterns = "/inicio/admin")
public class AdminServlet extends HttpServlet {
    public static final String ATRIB_MAX_PRODUCTOS = "masComprados";
    public static final String ATRIB_MAX_FECHA = "fechaMasCompra";
    public static final String ATRIB_PRODUCTOS = "productos";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo comprobar if admin permission

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            request.setAttribute(AdminServlet.ATRIB_MAX_PRODUCTOS,dao.getSubscripcionesMasCompradas(10));
            request.setAttribute(AdminServlet.ATRIB_MAX_FECHA,dao.getFechaMaxCompras(10));
            request.setAttribute(AdminServlet.ATRIB_PRODUCTOS,dao.getSubscripciones());
            request.getRequestDispatcher("/inicio/admin/view").forward(request,response);

            dao.close();
        } catch (SQLException | ClassNotFoundException e) {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action == null)
            return;

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            switch (action) {
                case "getMaxProductos":
                    request.setAttribute(ATRIB_MAX_PRODUCTOS,dao.getSubscripcionesMasCompradas(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasProductos.jsp").forward(request,response);
                    return;

                case "getMaxFechas":
                    request.setAttribute(ATRIB_MAX_FECHA,dao.getFechaMaxCompras(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasFechas.jsp").forward(request,response);
                    return;

                case "changeProducto":
                    try {
                        dao.updateProducto(new Subscripcion(
                                Integer.parseInt(request.getParameter("id")),
                                request.getParameter("nombre"),
                                Double.parseDouble(request.getParameter("precio")),
                                Integer.parseInt(request.getParameter("accessLevel"))
                        ));
                    } finally {
                        mostrarProductos(request,response,dao);
                    }
                    return;

                case "deleteProducto":
                    try {
                        dao.deleteProducto(new Subscripcion(Integer.parseInt(request.getParameter("id"))));
                    } finally {
                        mostrarProductos(request,response,dao);
                    }
                    return;

                case "addProducto":
                    try {
                        dao.addProducto(new Subscripcion(
                                request.getParameter("nombre"),
                                Double.parseDouble(request.getParameter("precio")),
                                Integer.parseInt(request.getParameter("accessLevel"))
                        ));
                    } finally {
                        mostrarProductos(request,response,dao);
                    }
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

    private void mostrarProductos(HttpServletRequest request, HttpServletResponse response, MLDao dao) throws SQLException, ServletException, IOException {
        request.setAttribute(ATRIB_PRODUCTOS,dao.getSubscripciones());
        request.getRequestDispatcher("/admin/mostrarProductos.jsp").forward(request,response);
    }
}
