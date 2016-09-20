package tracker.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;
/**
// * @author Patrizio
 *
 */
public class Logout extends HttpServlet {
	
	  Map<String,Object> data= new HashMap<String,Object>();
			  
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
                                throws ServletException, IOException {
			
			 try {
		            SecurityLayer.disposeSession(request);
		            response.sendRedirect("pages-signin");

		        } catch (IOException ex) {
		            request.setAttribute("exception", ex);
		            System.out.println("not working");
		        }
	}
}