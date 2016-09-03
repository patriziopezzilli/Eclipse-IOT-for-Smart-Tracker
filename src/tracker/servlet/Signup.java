package tracker.servlet;

import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;
import java.io.IOException;
import java.sql.ResultSet;
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
/**
 * servlet per la homepage
 *
 * @author Biblioteca Digitale
 */
public class Signup extends HttpServlet {

    
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
        Map data = new HashMap();
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            response.sendRedirect("index");
        } else if (request.getMethod().equals("POST")) {
            //Recupera il nome dell'utente
            String nome = request.getParameter("nome");
            //Recupera l'email dell'utente
            String email = request.getParameter("email");
            //Recupera la password dell'utente
            String password = request.getParameter("password");

            Database.connect();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("email", email);
            map.put("password", DataUtil.crypt(password));
            map.put("nome", nome);

            Database.insertRecord("users", map);
            ResultSet rs = Database.selectRecord("users", "email='" + email + "'");
            int k = 0;
            while (rs.next()) {
                k = rs.getInt("id");
            }
            SecurityLayer.createSession(request, email, k);

            Database.close();

            response.sendRedirect("index");
        } else {
            FreeMarker.process("pages-signup.html", data, response, getServletContext());
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