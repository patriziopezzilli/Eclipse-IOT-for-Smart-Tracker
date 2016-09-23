package tracker.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tracker.util.Database;
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
				 	HttpSession s = SecurityLayer.checkSession(request);
				 	Database.connect();
	                Map<String, Object> temp = new HashMap<String, Object>();
	        		temp.put("active", '0');
	        		Database.updateRecord("users", temp, "email ='" + (String) s.getAttribute("username") + "'");
	                Database.close();
		            SecurityLayer.disposeSession(request);
		            
		            response.sendRedirect("pages-signin");

		        } catch (IOException ex) {
		            request.setAttribute("exception", ex);
		            System.out.println("not working");
		        } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}