package servlets;

import dao.MLDao;
import dominio.AccessLevel;
import dominio.Subscripcion;
import dominio.Usuario;
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
        Usuario loggedUser = (Usuario) request.getSession().getAttribute(AccesoServlet.USER_LOGGED);
        if(loggedUser.getAccessLevel().getId() != AccessLevel.ADMIN_LEVEL) {
            response.sendError(400, "No tiene acceso de administrador: acceso denegado");
            return;
        }

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
        Subscripcion subscripcion;
        try {
            dao = MLDao.getInstance();

            switch (action) {
                case "getMaxProductos":
                    request.setAttribute(ATRIB_MAX_PRODUCTOS,dao.getSubscripcionesMasCompradas(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasProductos.jsp").forward(request,response);
                    break;

                case "getMaxFechas":
                    request.setAttribute(ATRIB_MAX_FECHA,dao.getFechaMaxCompras(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasFechas.jsp").forward(request,response);
                    break;

                case "changeProducto":
                    dao.updateProducto(new Subscripcion(
                            Integer.parseInt(request.getParameter("id")),
                            request.getParameter("nombre"),
                            Double.parseDouble(request.getParameter("precio")),
                            Integer.parseInt(request.getParameter("accessLevel"))
                    ));
                    mostrarProductos(request,response,dao);
                    break;

                case "deleteProducto":
                    dao.deleteProducto(new Subscripcion(Integer.parseInt(request.getParameter("id"))));
                    mostrarProductos(request,response,dao);
                    break;

                case "addProducto":
                    dao.addProducto(new Subscripcion(
                            request.getParameter("nombre"),
                            Double.parseDouble(request.getParameter("precio")),
                            Integer.parseInt(request.getParameter("accessLevel"))
                    ));
                    mostrarProductos(request,response,dao);
                    break;

                case "searchProductos":
                    String filtro = "%" + request.getParameter("filtro") + "%";
                    request.setAttribute(ATRIB_PRODUCTOS,dao.getSubscripciones(filtro));
                    request.getRequestDispatcher("/admin/mostrarProductos.jsp").forward(request,response);
                    break;

                case "deleteOferta":
                    subscripcion = new Subscripcion(Integer.parseInt(request.getParameter("id")));
                    subscripcion.setPorcentajeOferta(0);
                    if(dao.updateOferta(subscripcion))
                        mostrarOfertas(request,response,dao);
                    else
                        response.sendError(400,"No se pudo realizar la operación debido a una petición errónea");
                    break;

                case "addOferta":
                    subscripcion = new Subscripcion(request.getParameter("nombre"));
                    subscripcion.setPorcentajeOferta(Integer.parseInt(request.getParameter("oferta")));
                    if(dao.updateOfertaByName(subscripcion))
                        mostrarOfertas(request,response,dao);
                    else
                        response.sendError(400,"No se pudo realizar la operación debido a una petición errónea");
                    break;

                default:
                    response.sendError(400,"Petición no reconocida");
            }
        } catch (SQLException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
            response.sendError(400,"No se pudo realizar la operación debido a una petición errónea");
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

    private void mostrarOfertas(HttpServletRequest request, HttpServletResponse response, MLDao dao) throws SQLException, ServletException, IOException {
        request.setAttribute(ATRIB_PRODUCTOS,dao.getSubscripciones());
        request.getRequestDispatcher("/admin/mostrarOfertas.jsp").forward(request,response);
    }
}
