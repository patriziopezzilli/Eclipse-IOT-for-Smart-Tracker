package tracker.servlet;

import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tracker.util.FreeMarker;

/**
 * servlet per la homepage
 *
 * @author Patrizio
 */
public class Login extends HttpServlet {

    
	/**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
	 * @throws Exception 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession s = SecurityLayer.checkSession(request);
        Map data = new HashMap();
        if (s != null) {
            response.sendRedirect("index");
        } else {
            String email = request.getParameter("name");
            String pass = request.getParameter("password");

            if (isNull(email) || isNull(pass)) {
            	
                FreeMarker.process("pages-signin.html", data, response, getServletContext());
            }
            try {
				Database.connect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            int userid = DataUtil.checkUser(email, pass);
            Database.close();
            //... VALIDAZIONE IDENTITA'...
            //... IDENTITY CHECKS ...
            if (userid == 0) {
            	//caso in cui la validazione non ha successo
            } else {
                //se la validazione ha successo
                //if the identity validation succeeds
                //carichiamo lo userid dal database utenti
                //load userid from user database
                SecurityLayer.createSession(request, email, userid);
                Database.connect();
                Map<String, Object> temp = new HashMap<String, Object>();
        		temp.put("active", '1');
        		Database.updateRecord("users", temp, "email ='" + email + "'");
                Database.close();
                response.sendRedirect("index");
            }
        }
    }

    /**
     * Caricamento pagina di Home get
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Caricamento pagina di Home post
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}