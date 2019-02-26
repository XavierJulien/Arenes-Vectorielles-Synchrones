import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class JeVousConnais extends HttpServlet {

    int compteur = 0;

    public void doGet (HttpServletRequest q, HttpServletResponse r)
	throws ServletException, IOException {
      PrintWriter out = r.getWriter();
      String t1, t2, t3;
      Cookie[] cs = q.getCookies();
      Cookie c;
      String cn = "JeVousConnais";

      r.setContentType("text/html");
      out.println("<!DOCTYPE HTML PUBLIC \".//W3C//DTD HTML 4.0 " +
		  "Transitional//EN\">");

      if (cs != null) {
      t1 = null;
      for (int i = 0; i < cs.length; i++)
	  if (cn.equals(cs[i].getName())) {
	      t1 = cs[i].getValue();
	      break;
	  }
      } else t1 = "1";
      try {
	int i = Integer.parseInt(t1);
	t3 = "" + (i + 1);
	t2 = "Vous êtes déjà venu " + i + " fois.";	      
      } catch (NumberFormatException e) {
	  t3 = "1";
	  t2 = "Il est probable que vous êtes déjà venu.";
      }
      out.println("<HTML>\n" +
		  "<HEAD><TITLE>Compteur</TITLE></HEAD>\n" +
		  "<BODY>\n" +
		  "<H1>\n"+
		  t2 +
		  "</H1>\n" +
		  "</BODY>" +
		  "</HTML>\n");
      c = new Cookie(cn, t3);
      c.setMaxAge(60*60*24*365);
      r.addCookie(c);
    }

    public void doPut (HttpServletRequest q, HttpServletResponse r)
	throws ServletException, IOException {
	doGet(q, r);
    }
}
