package servlets;

import dao.MLDao;
import dominio.AccessLevel;
import dominio.Subscripcion;
import dominio.Usuario;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if (loggedUser.getAccessLevel().getId() != AccessLevel.ADMIN_LEVEL) {
            response.sendError(400, "No tiene acceso de administrador: acceso denegado");
            return;
        }

        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            request.setAttribute(AdminServlet.ATRIB_MAX_PRODUCTOS, dao.getSubscripcionesMasCompradas(10));
            request.setAttribute(AdminServlet.ATRIB_MAX_FECHA, dao.getFechaMaxCompras(10));
            request.setAttribute(AdminServlet.ATRIB_PRODUCTOS, dao.getSubscripciones());
            request.getRequestDispatcher("/inicio/admin/view").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            return;

        MLDao dao = null;
        Subscripcion subscripcion;
        String nombre, id, precio, accessLevel, oferta;
        try {
            dao = MLDao.getInstance();

            switch (action) {
                case "getMaxProductos":
                    request.setAttribute(ATRIB_MAX_PRODUCTOS, dao.getSubscripcionesMasCompradas(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasProductos.jsp").forward(request, response);
                    break;

                case "getMaxFechas":
                    request.setAttribute(ATRIB_MAX_FECHA, dao.getFechaMaxCompras(10,
                            Util.parseDate(request.getParameter("fechaInicio")),
                            Util.parseDate(request.getParameter("fechaFin"))));
                    request.getRequestDispatcher("/admin/maxComprasFechas.jsp").forward(request, response);
                    break;

                case "changeProducto":
                    nombre = request.getParameter("nombre");
                    id = request.getParameter("id");
                    precio = request.getParameter("precio");
                    accessLevel = request.getParameter("accessLevel");
                    if (nombre == null || id == null || precio == null || accessLevel == null) {
                        sendParamError(response);
                        return;
                    }

                    dao.updateProducto(new Subscripcion(
                            Integer.parseInt(id), nombre, Double.parseDouble(precio), Integer.parseInt(accessLevel)));
                    mostrarProductos(request, response, dao);
                    break;

                case "deleteProducto":
                    id = request.getParameter("id");
                    if (id == null) {
                        sendParamError(response);
                        return;
                    }

                    dao.deleteProducto(new Subscripcion(Integer.parseInt(id)));
                    mostrarProductos(request, response, dao);
                    break;

                case "addProducto":
                    nombre = request.getParameter("nombre");
                    if (nombre == null || nombre.isBlank()) {
                        sendParamError(response);
                        return;
                    }

                    dao.addProducto(new Subscripcion(
                            nombre,
                            Double.parseDouble(request.getParameter("precio")),
                            Integer.parseInt(request.getParameter("accessLevel"))
                    ));
                    mostrarProductos(request, response, dao);
                    break;

                case "searchProductos":
                    nombre = request.getParameter("filtro");
                    if (nombre == null) {
                        sendParamError(response);
                        return;
                    }

                    String filtro = "%" + nombre + "%";
                    request.setAttribute(ATRIB_PRODUCTOS, dao.getSubscripciones(filtro));
                    request.getRequestDispatcher("/admin/mostrarProductos.jsp").forward(request, response);
                    break;

                case "deleteOferta":
                    id = request.getParameter("id");
                    if (id == null) {
                        sendParamError(response);
                        return;
                    }

                    subscripcion = new Subscripcion(Integer.parseInt(id));
                    subscripcion.setPorcentajeOferta(0);
                    if (dao.updateOferta(subscripcion))
                        mostrarOfertas(request, response, dao);
                    else
                        response.sendError(400, "No se pudo realizar la operación debido a una petición errónea");
                    break;

                case "addOferta":
                    nombre = request.getParameter("nombre");
                    oferta = request.getParameter("oferta");
                    if (nombre == null || nombre.isBlank() || oferta == null) {
                        sendParamError(response);
                        return;
                    }

                    subscripcion = new Subscripcion(nombre);
                    subscripcion.setPorcentajeOferta(Integer.parseInt(oferta));
                    if (dao.updateOfertaByName(subscripcion))
                        mostrarOfertas(request, response, dao);
                    else
                        response.sendError(400, "No se pudo realizar la operación debido a una petición errónea");
                    break;

                default:
                    response.sendError(400, "Petición no reconocida");
            }
        } catch (SQLException | ClassNotFoundException | ParseException | IllegalArgumentException e) {
            e.printStackTrace();
            response.sendError(400, "No se pudo realizar la operación debido a una petición errónea");
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

    private void mostrarProductos(HttpServletRequest request, HttpServletResponse response, MLDao dao) throws SQLException, ServletException, IOException {
        request.setAttribute(ATRIB_PRODUCTOS, dao.getSubscripciones());
        request.getRequestDispatcher("/admin/mostrarProductos.jsp").forward(request, response);
    }

    private void mostrarOfertas(HttpServletRequest request, HttpServletResponse response, MLDao dao) throws SQLException, ServletException, IOException {
        request.setAttribute(ATRIB_PRODUCTOS, dao.getSubscripciones());
        request.getRequestDispatcher("/admin/mostrarOfertas.jsp").forward(request, response);
    }

    private void sendParamError(HttpServletResponse response) throws IOException {
        response.sendError(400, "No se pudo realizar la operación debido a una petición errónea");
    }
}
