package tracker.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tracker.model.Module;
import tracker.model.Path;
import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;

/**
@author Patrizio
*/
public class PathController extends HttpServlet {

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
		HttpSession s = SecurityLayer.checkSession(request);
		Map data = new HashMap();

		if (s != null) {
			
			if (request.getMethod().equals("POST")) {
				
				String id = request.getParameter("id");
				
				if(id != null){
					//delete mode
					Database.deleteRecord("path", "id='" + id + "'");
				} else {
					//add mode
					String city = request.getParameter("city");
					String km = request.getParameter("km");
					String time = request.getParameter("time");

					
					Map<String, Object> map = new HashMap<String, Object>();

					// da cambiare
					map.put("city", city);
					map.put("km", km);
					map.put("time", time);
					map.put("user", (String) s.getAttribute("username"));

					Database.insertRecord("path", map);
				}
				
			} else {
				/* RETRIVING MODULE */
	        	 
	        	 //retriving module --> andrà cambiato prendendo con il JOIN
	        	 //solo i moduli dei dispositivi associati al player corrente
	        	 ResultSet ss = Database.selectRecord("modules,devices","devices.serial = modules.id_device AND devices.email_user ='"+(String) s.getAttribute("username")+"'");
	 			 List<Module> modules_2 = new ArrayList<Module>();

	 			 while (ss.next()) {
	 				 
	 				int id = ss.getInt("id");
					String name= ss.getString("name");
					String iframe = ss.getString("iframe");
					String serial = ss.getString("id_device");

					Module moduleTemp = new Module(id, name, iframe, serial);

					modules_2.add(moduleTemp);

				}
				
				data.put("lista_modules_menu", modules_2);
	        	 
				/* END RETRIVING MODULE */
				data.put("userName", DataUtil.getUsername((String) s.getAttribute("username")));
				data.put("userMail", (String) s.getAttribute("username"));
				data.put("titlePage", "Percorsi");

				ResultSet rs = Database.selectRecord("path", "user='"+(String) s.getAttribute("username")+"'");
				List<Path> paths = new ArrayList<Path>();

				while (rs.next()) {
					
					int id = rs.getInt("id");
					String city= rs.getString("city");
					int km = rs.getInt("km");
					int time = rs.getInt("time"); 		//express in hours
					String user = rs.getString("user");

					Path pathTemp = new Path(id, city, km, time, user);

					paths.add(pathTemp);

				}
				
				data.put("lista_path", paths);

				FreeMarker.process("pathcontroller.html", data, response, getServletContext());
				
			}
			
			
		} else {
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