package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class footer extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nav = request.getHeader("User-Agent");
        boolean soporta = nav.contains("Firefox") || nav.contains("Chrome");
        String foot = soporta ? "Su navegador funciona adecuadamente con la p&aacute;gina" :
            "Su navegador no es compatible con la p&aacute;gina. " +
            "Puede que algunas funciones no funcionen correctamente. " +
            "El sitio s&oacute;lo soporta los navegadores Mozilla Firefox y Google Chrome.";
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"simpleblue.css\"/>");

        out.println("<body><div id=\"footer\">");
        out.println("<p>Proyecto Grupo 11 del ramo CC5604, <a href=\"http://www.dcc.uchile.cl/\">DCC</a>"+
                    ", <a href=\"http://www.uchile.cl/\">Universidad de Chile</a><br>");
        out.println(foot + "</p>");
        out.println("</div></body></html>");
        out.close();
    }
}
