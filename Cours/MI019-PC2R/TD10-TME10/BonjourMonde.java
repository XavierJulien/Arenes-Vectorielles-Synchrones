// BonjourMonde.java

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BonjourMonde extends HttpServlet {

    public void doGet (HttpServletRequest q, HttpServletResponse r)
        throws ServletException, IOException {
      PrintWriter out = r.getWriter();

      r.setContentType("text/html");
      out.println("<html> <body>" +
                  "<h1>Bonjour Monde!</h1>" +
                  "</body> </html> \n ");
    }

    public void doPut (HttpServletRequest q, HttpServletResponse r)
        throws ServletException, IOException {
        doGet(q, r);
    }
}
