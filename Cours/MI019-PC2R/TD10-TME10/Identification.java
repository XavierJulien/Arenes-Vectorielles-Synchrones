// Identification.java

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Identification extends HttpServlet {

    public void doGet (HttpServletRequest q, HttpServletResponse r)
	throws ServletException, IOException {
      PrintWriter out = r.getWriter();

      r.setContentType("text/html");
      out.println("<!DOCTYPE HTML PUBLIC \".//W3C//DTD HTML 4.0 " +
		  "Transitional//EN\">");
      out.println("<HTML>\n" +
		  "<HEAD><TITLE>Identification</TITLE></HEAD>\n" +
		  "<BODY>\n" +
		  "<H1 ALIGN=CENTER>\n" +
		  "Identification de l'utilisateur\n" +
		  "</H1>\n" +
		  "<TABLE BORDER=1 ALIGN=CENTER>\n" +
		  "<TR><TD>" + "REMOTE ADRESSE" +
		  "<TD>" + q.getRemoteAddr() +
		  "<TR><TD>" + "REMOTE HOST" +
		  "<TD>" + q.getRemoteHost() +
		  "<TR><TD>" + "REMOTE USER" +
		  "<TD>" +
		      ((q.getRemoteUser()==null)?"Anonyme":q.getRemoteUser()) +
		  "</TABLE>" +
		  "</BODY>" +
		  "</HTML>\n");
    }

    public void doPut (HttpServletRequest q, HttpServletResponse r)
	throws ServletException, IOException {
	doGet(q, r);
    }
}
