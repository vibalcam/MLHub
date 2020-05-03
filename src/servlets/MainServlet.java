package servlets;

import dao.MLDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "MainServlet", urlPatterns = "/inicio")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        //todo mostrar pagina normal
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action == null)
            return;

        switch (action) {
            case "searchEntry":
                search(request, response);
                return;
            case "subs":
//                accederSubscripciones(request,response);
                return;
            case "cerrar":
                request.getSession().invalidate();
                response.sendRedirect(getServletContext().getContextPath());
                return;
        }
    }

    private void accederSubscripciones(HttpServletRequest request, HttpServletResponse response, MLDao dao) throws IOException, SQLException, ServletException {
        request.setAttribute("subscripciones",dao.getSubscripciones());
        request.getRequestDispatcher("/inicio/subscripcion").forward(request,response);
        //todo acceder a subscripciones, debe tener el dao
    }

    private void search(HttpServletRequest request, HttpServletResponse response) {
        request.getAttribute("searchEntry");
        //todo search entries
    }
}
