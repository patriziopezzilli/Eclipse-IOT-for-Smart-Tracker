package tracker.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
import tracker.model.Event;
import tracker.model.Friend;
import tracker.model.Module;
import tracker.model.Path;
import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;

/**
 * servlet per la homepage
 *
 * @author Biblioteca Digitale
 */
public class Index extends HttpServlet {

    
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
    	 Database.connect();
    	 HttpSession s = SecurityLayer.checkSession(request);
         Map data = new HashMap();
         
         if (s != null) {
        	 
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
			
			//handling events
			ResultSet ev = Database.selectRecord("events","email_user='"+(String) s.getAttribute("username")+"'");
			 List<Event> events = new ArrayList<Event>();

			 while (ev.next()) {
				 
				Date timestamp = ev.getTimestamp("timestamp");
				String description= ev.getString("description");

				Event eventTemp = new Event(description,timestamp);

				events.add(eventTemp);

			}
			
			data.put("events", events);
			
			/* HANDLING LIST FOR ALL FUNCTIONALITY */
			ResultSet vv = Database.selectRecord("path", "user='"+(String) s.getAttribute("username")+"'");
			List<Path> paths = new ArrayList<Path>();
			while(vv.next()){
				int id = vv.getInt("id");
				String city= vv.getString("city");
				int km = vv.getInt("km");
				int time = vv.getInt("time"); 		//express in hours
				String user = vv.getString("user");
				Date timestamp = vv.getTimestamp("insert_date");
				
				Path pathTemp = new Path(id, city, km, time, user, timestamp);

				paths.add(pathTemp);
			}
			
			data.put("localitapercorse", paths.size());
			
			//initialize the calendar
			GregorianCalendar gc = new GregorianCalendar();
			
			//get date of this month
			Date actual = gc.getTime();
			
			//get date of one month ago
			gc.add(gc.DATE, -30);
			Date actual2 = gc.getTime();
			
			//get date of one week ago
			gc.add(gc.DATE, +30);
			gc.add(gc.DATE, -7);
			Date actual3 = gc.getTime();
			
			//get user weight
			ResultSet temp= Database.selectRecord("users", "email='"+(String) s.getAttribute("username")+"'");
			temp.next();
			int peso = temp.getInt("kg");
			
			//now remove from the list out of bound date
			int monthKm = 0;
			int monthTime = 0;
			int monthCal = 0;
			for(int i = 0; i<paths.size(); i++){
				if(paths.get(i).getTimestamp().before(actual2) && paths.get(i).getTimestamp().after(actual)){
					
				} else {
					monthKm += paths.get(i).getKm();
					monthTime += paths.get(i).getTime();
					monthCal += paths.get(i).getKm();
					monthCal *= peso;
				}
			}
			
			data.put("monthCal", monthCal);
			data.put("monthKm", monthKm);
			data.put("monthTime", monthTime);
			
			//now build weeklist
			int weekKm = 0;
			int weekTime = 0;
			for(int i = 0; i<paths.size(); i++){
				if(paths.get(i).getTimestamp().before(actual2) && paths.get(i).getTimestamp().after(actual)){
					
				} else {
					weekKm += paths.get(i).getKm();
					weekTime += paths.get(i).getTime();
				}
			}
			data.put("weekKm", weekKm);
			data.put("weekTime", weekTime);
			
			//calcolo storyboard
			
			//remember to imbrove weight registration
			
			//end calcolo storyboard
			
			
        	 data.put("userName", DataUtil.getUsername((String)s.getAttribute("username")));
        	 data.put("userMail",(String) s.getAttribute("username"));
        	 data.put("titlePage", "Panoramica");
        	 
        	 
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
			 data.put("friendNumber", friends.size());
        	 FreeMarker.process("index.html", data, response, getServletContext());
         }else {
        	 response.sendRedirect("pages-signin");
         }
         Database.close();
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