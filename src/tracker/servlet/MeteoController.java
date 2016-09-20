package tracker.servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import tracker.model.Device;
import tracker.model.Module;
import tracker.util.DataUtil;
import tracker.util.Database;
import tracker.util.FreeMarker;
import tracker.util.SecurityLayer;

/**
 * servlet per la pagina del dettaglio di uno specifico modulo
 *
 * @author Patrizio
 */
public class MeteoController extends HttpServlet {

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
			

			/* Start populate PAGE */
			
			//bring info about last city
			ResultSet rs = Database.selectRecord("path", "1 ORDER BY id DESC");
			rs.next(); //now we have info about the last path
			
			 // declaring object of "OpenWeatherMap" class passing the KEY
	        OpenWeatherMap owm = new OpenWeatherMap("e2d30929f5bcf3a7f3e4528d72807fff");
	        
	        System.out.println(rs.getString("city"));
	        data.put("city", rs.getString("city"));
	        // getting current weather data for the "London" city
	        CurrentWeather cwd = owm.currentWeatherByCityName(rs.getString("city"));

	        //printing city name from the retrieved data
	        System.out.println("City: " + cwd.getCityName());

	        // printing the max./min. temperature
	        System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
	                            + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");
	        
	        data.put("maxTemp", cwd.getMainInstance().getMaxTemperature());
	        data.put("minTemp", cwd.getMainInstance().getMinTemperature());
			data.put("temp",cwd.getMainInstance().getTemperature());
			data.put("pressure",cwd.getMainInstance().getPressure());
			data.put("humidity",cwd.getMainInstance().getHumidity());
	       
			/* End populate PAGE */
			data.put("titlePage", "Luoghi e Meteo");
			FreeMarker.process("meteo.html", data, response, getServletContext());
			
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