package tracker.servlet;

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

import tracker.model.Device;
import tracker.model.Friend;
import tracker.model.Message;
import tracker.model.Module;
import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;

/**
 * servlet per la pagina degli amici
 * permette la gestione totale, quindi
 * aggiunta, modifica e eliminazione degli stessi.
 *
 * @author Patrizio
 */
public class Friends extends HttpServlet {

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
				
				String mode = request.getParameter("addMode");
				String email = request.getParameter("email");
				if(mode != null){
					//we are creating new one
					Map<String, Object> map = new HashMap<String, Object>();

					// da cambiare
					map.put("friend_mail", email);
					map.put("my_mail", (String) s.getAttribute("username"));
					

					Database.insertRecord("friends", map);
					Map<String, Object> events = new HashMap<String, Object>();
					events.put("description", "Ho aggiunto agli amici "+ email);
					events.put("email_user", (String) s.getAttribute("username"));

					Database.insertRecord("events", events);
					response.sendRedirect("friends");
				}else{
					//we are deleting
					Database.deleteRecord("friends", "friend_mail='" + email + "' && my_mail='"+(String) s.getAttribute("username")+"'");
					Map<String, Object> events = new HashMap<String, Object>();
					events.put("description", "Ho eliminato l'amico "+ email);
					events.put("email_user", (String) s.getAttribute("username"));

					Database.insertRecord("events", events);
					response.sendRedirect("friends");
				}
				
			} else {
				
				
				//check if i have to delete something
				if(request.getParameter("idmess") != null){
					Database.deleteRecord("messages", "id="+Integer.parseInt(request.getParameter("idmess")));
					response.sendRedirect("friends");
				}
				// retrive messages
				
				ResultSet ms = Database.selectRecord("messages", "dest_mail='"+(String) s.getAttribute("username")+"'");
				List<Message> messages = new ArrayList<Message>();
				
				while(ms.next()){
					int id= ms.getInt("id");
					String mitt_mail = ms.getString("mitt_mail");
					String content= ms.getString("content");
					
					Message messTemp = new Message(id,mitt_mail,content);
					messages.add(messTemp);
					
				}
				
				data.put("messageList", messages);
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
				
				/* 	RETRIVING FRIENDS */
				ResultSet fr = Database.selectRecord("friends", "my_mail='"+ (String) s.getAttribute("username") + "'");
				List<Friend> friends = new ArrayList<Friend>();
				while(fr.next()){
					
					//retrive friend data
					int id = fr.getInt("id");
					String friend_mail = fr.getString("friend_mail");
					
					int totKm = 0;
					//retrive tot km
					ResultSet km = Database.selectRecord("path", "user='"+ friend_mail + "'");
					while(km.next()){
						totKm += km.getInt("km");
					}
					
					//retriving name
					ResultSet us = Database.selectRecord("users", "email='"+ friend_mail + "'");
					us.next();
					String name = us.getString("nome");

					int active = us.getInt("active");
					//now create the obj and add to the list
					Friend tempUser = new Friend(id,friend_mail,totKm,name,active);
					friends.add(tempUser);
					
				}
				data.put("friendList", friends);
				data.put("userName", DataUtil.getUsername((String) s.getAttribute("username")));
				data.put("userMail", (String) s.getAttribute("username"));
				data.put("titlePage", "Amici");

				ResultSet rs = Database.selectRecord("modules", "1");
				List<Module> modules = new ArrayList<Module>();

				while (rs.next()) {
					
					int id = rs.getInt("id");
					String name= rs.getString("name");
					String iframe = rs.getString("iframe");
					String serial = rs.getString("id_device");

					Module moduleTemp = new Module(id, name, iframe, serial);

					modules.add(moduleTemp);

				}
				
				data.put("lista_module", modules);

				FreeMarker.process("friends.html", data, response, getServletContext());
				
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

