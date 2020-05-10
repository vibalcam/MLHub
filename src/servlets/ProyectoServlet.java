package servlets;

import dao.MLDao;
import dominio.AccessLevel;
import dominio.Metodo;
import dominio.Subscripcion;
import dominio.Usuario;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet(name = "ProyectoServlet", urlPatterns = "/inicio/proyecto")
public class ProyectoServlet extends HttpServlet {
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String proyecto = request.getParameter("peticion");
        String proyecto2 = request.getParameter("peticion2");

        if((proyecto == null) || (proyecto.isBlank())){
            if((proyecto2 == null) || (proyecto2.isBlank())) {
                response.sendRedirect(getServletContext().getContextPath() + "/inicio/view");
                return;
            }
            proyecto = proyecto2;
        }

        String nombre = request.getParameter("nombre");
        String eficacia = request.getParameter("eficacia");
        String tiempo = request.getParameter("tiempo");
        String codigo = request.getParameter("codigo");

        MLDao dao = null;

        try{
            dao = MLDao.getInstance();

            if(!((nombre == null) || (eficacia == null) || (tiempo == null)) && !(nombre.isBlank() || eficacia.isBlank() || tiempo.isBlank())){
                dao.addMetodo(new Metodo(nombre, Integer.parseInt(eficacia), Integer.parseInt(tiempo)), proyecto);
            }

            if((codigo != null) && (proyecto2 != null)){
                dao.updateCodigo(proyecto, codigo);
            }

            if(dao.getUsuario(proyecto) == ((Usuario) request.getSession().getAttribute("userLogged")).getId()){
                request.setAttribute("addPosible",1);
            } else{
                request.setAttribute("addPosible",0);
            }

            int id = ((Usuario) request.getSession().getAttribute("userLogged")).getId();

            request.setAttribute("metodos", dao.getAllMetodos(proyecto));
            request.setAttribute("nombreProyecto", proyecto);
            request.setAttribute("codigo",dao.getCodigo(proyecto));
            request.setAttribute("level", dao.getSubscripcion(id));
            request.getRequestDispatcher("/inicio/proyecto/view").forward(request,response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(getServletContext().getContextPath() + "/inicio");
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
