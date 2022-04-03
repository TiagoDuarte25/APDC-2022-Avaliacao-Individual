package pt.unl.fct.di.apdc.apdc2022ai.resources;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

@WebServlet("/logout")
public class LogOutResource extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		/*
		HttpCookie cookie = null;
		
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		URL url = new URL("https://apdc2022ai.appspot.com/usermenu.html");

		URLConnection connection = url.openConnection();
		connection.getContent();

		List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
		for (HttpCookie c : cookies) {
			if(!c.hasExpired())
				cookie = c;
		} 
		username = cookie.getValue();
		
		*/
		
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(username);
		//Key ctrsKey = datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username)).setKind("UserStats").newKey("counters");
		//Key logKey = datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username)).setKind("UserLog").newKey("info");
		if (validLogin(tokenKey)) {
			
			datastore.delete(tokenKey);
			
			//cookie.setMaxAge(0);
			
			PrintWriter pw = res.getWriter();
			
			String html = "<!DOCTYPE html>\r\n"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\r\n"
					+ "<head>\r\n"
					+ "	<title>APDC-2022-Avaliacao-Individual</title>\r\n"
					+ "	<link href=\"style.css\" rel=\"stylesheet\">\r\n"
					+ "</head>\r\n"
					+ "	\r\n"
					+ "<body>\r\n"
					+ "	<h3>Logged Out!</h3>\r\n"
					+ "	<div>\r\n"
					+ "		<a href='/menu.html'>User Menu</a></td>\r\n"
					+ "	</div>\r\n"
					+ "</body>\r\n"
					+ "</html>\r\n";
			
			pw.println(html);
		}
		else
			res.sendError(Status.FORBIDDEN.getStatusCode());
	}
	
	private boolean validLogin(Key tokenKey) {
		Entity user = datastore.get(tokenKey);
		if (user != null)
				return true;
		return false;
	}
}
