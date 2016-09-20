package tracker.servlet;

import tracker.model.Module;
import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * servlet per la registrazione di un nuovo device
 *
 * @author Patrizio
 */
public class SignupDevice extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws Exception
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Database.connect();
		Map data = new HashMap();
		HttpSession s = SecurityLayer.checkSession(request);

		if (s != null) {

			if (request.getMethod().equals("POST")) {
				// Recupera il seriale del device
				String serial = request.getParameter("serial");

				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("serial", serial);
				map.put("email_user", (String) s.getAttribute("username"));

				Database.insertRecord("devices", map);

				response.sendRedirect("devicelist");
			} else {
				/* RETRIVING MODULE */

				// retriving module --> andrà cambiato prendendo con il JOIN
				// solo i moduli dei dispositivi associati al player corrente
				ResultSet ss = Database.selectRecord("modules,devices","devices.serial = modules.id_device AND devices.email_user ='"+(String) s.getAttribute("username")+"'");
				List<Module> modules_2 = new ArrayList<Module>();

				while (ss.next()) {

					int id = ss.getInt("id");
					String name = ss.getString("name");
					String iframe = ss.getString("iframe");
					String serial = ss.getString("id_device");

					Module moduleTemp = new Module(id, name, iframe, serial);

					modules_2.add(moduleTemp);

				}

				data.put("lista_modules_menu", modules_2);

				/* END RETRIVING MODULE */
				data.put("userName", DataUtil.getUsername((String) s.getAttribute("username")));
				data.put("userMail", (String) s.getAttribute("username"));
				data.put("titlePage", "Aggiunta Device");
				FreeMarker.process("device-signup.html", data, response, getServletContext());
			}
		} else {
			// mando alla login
			response.sendRedirect("pages-signin");
		}
		Database.close();
	}

	/**
	 * Caricamento pagina di Home get
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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