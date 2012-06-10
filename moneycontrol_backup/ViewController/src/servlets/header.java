package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

public class header extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateFormat df = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"simpleblue.css\"/>");
        out.println("</head>");
        
        out.println("<body>");
        out.println("<div id=\"container\">");
        out.println("<div id=\"header\">");
        out.println("<a href=\"#\" title=\"Money Control\"><img src=\"images/logo.gif\" alt=\"Hosting Company Name\" class=\"logo\"/></a>");
        out.println("<ul id=\"toplinks\">");
        out.println("<div id=\"div_clock\">Hora del Servidor: " + df.format(new Date()) + " </div>");
        out.println("<li>Version del sitio: " + getServletContext().getInitParameter("app.version") + "</li>");
        out.println("</ul></div></div>");
        out.println("</body></html>");
        out.close();
    }
}
