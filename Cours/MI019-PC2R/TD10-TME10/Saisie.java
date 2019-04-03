// Saisie.java

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Saisie extends HttpServlet {

  public void doGet (HttpServletRequest q, HttpServletResponse r)
  throws ServletException, IOException {
    PrintWriter out = r.getWriter();

    r.setContentType("text/html");
    out.println("<!DOCTYPE HTML PUBLIC \".//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
    out.println("<HTML>\n" +
                "<HEAD><TITLE>Saisie</TITLE></HEAD>\n" +
                "<BODY>\n" +
		      "La saisie donne " + q.getQueryString() + "\n" +
		      "</BODY>" +
		      "</HTML>\n");
  }

  public void doPost (HttpServletRequest q, HttpServletResponse r)
  throws ServletException, IOException {
    doGet(q, r);
  }
}

