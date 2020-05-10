package servlets;

import dao.MLDao;
import dominio.AccessLevel;
import dominio.Proyecto;
import dominio.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "InicioServlet", urlPatterns = "/inicio/view")
public class InicioServlet extends HttpServlet {
    private static final String KEY_ERROR = "error";
    private static final String DUPLICITY_ERROR = "Este usuario ya se encuentra en nuestra base de datos";
    private static final String MSG_UNKNOWN_ERROR = "No se han podido validar sus credenciales: inténtelo más tarde";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        acceso(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        acceso(request, response);
    }

    protected void acceso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MLDao dao = null;
        try {
            dao = MLDao.getInstance();

            String nuevoProyecto = request.getParameter("nuevoProyecto");
            int id = ((Usuario) request.getSession().getAttribute("userLogged")).getId();

            if(nuevoProyecto != null){
                dao.addProyecto(nuevoProyecto, id);
            }

            ArrayList<Proyecto> proyectos;

            if(request.getParameter("action") != null){
                proyectos = dao.getSomeProyectos(request.getParameter("searchName"));
            } else {
                proyectos = dao.getAllProyectos();
            }
            request.setAttribute("proyectos", proyectos);

            request.getRequestDispatcher("/inicio/usuario").forward(request,response);

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

    private void vueltaInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(getServletContext().getContextPath() + "/inicio/view");
    }

}