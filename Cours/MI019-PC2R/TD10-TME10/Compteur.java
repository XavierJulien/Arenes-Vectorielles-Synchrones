import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Compteur extends HttpServlet {

    int compteur = 0;

    public void doGet (HttpServletRequest q, HttpServletResponse r)
	throws ServletException, IOException {
      PrintWriter out = r.getWriter();

      r.setContentType("text/html");
      out.println("<!DOCTYPE HTML PUBLIC \".//W3C//DTD HTML 4.0 " +
		  "Transitional//EN\">");
      out.println("<HTML>\n" +
		  "<HEAD><TITLE>Compteur</TITLE></HEAD>\n" +
		  "<BODY>\n" +
		  "<H1>\n"+
		  "Vous êtes la " +
		  (++compteur==1 ? "première " : ""+compteur+" ième ") +
		  "personne." +
		  "</H1>\n" +
		  "</BODY>" +
		  "</HTML>\n");
    }

    public void doPut (HttpServletRequest q, HttpServletResponse r)
	throws ServletException, IOException {
	doGet(q, r);
    }
}
