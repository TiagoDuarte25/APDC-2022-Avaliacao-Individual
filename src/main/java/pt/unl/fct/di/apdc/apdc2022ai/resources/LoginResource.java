package pt.unl.fct.di.apdc.apdc2022ai.resources;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import pt.unl.fct.di.apdc.apdc2022ai.utils.AuthToken;

@WebServlet("/login")
public class LoginResource extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("pwd");
		
		AuthToken at = new AuthToken(username);
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(at.username);
		
		Entity token = datastore.get(tokenKey);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		//Key ctrsKey = datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username)).setKind("UserStats").newKey("counters");
		//Key logKey = datastore.newKeyFactory().addAncestor(PathElement.of("User", data.username)).setKind("UserLog").newKey("info");
		if (validLogin(userKey, password) && token == null) {
			
			Entity newToken = Entity.newBuilder(tokenKey)
					.set("token_id", at.tokenID)
					.set("token_creation_data", at.creationData)
					.set("token_expiration_data", at.expirationData)
					.build();
			
			datastore.add(newToken);
			
			Cookie cookie = new Cookie(at.tokenID, at.username);
			
			res.addCookie(cookie);
			
			PrintWriter pw = res.getWriter();
			
			String html = "<!DOCTYPE html>\r\n"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\r\n"
					+ "<head>\r\n"
					+ "	<title>APDC-2022-Avaliacao-Individual</title>\r\n"
					+ "	<link href=\"style.css\" rel=\"stylesheet\">\r\n"
					+ "</head>\r\n"
					+ "	\r\n"
					+ "<body>\r\n"
					+ "	<h3>Logged!</h3>\r\n"
					+ "	<div>\r\n"
					+ "		<label>" + at.username + "</label>\r\n"
					+ "	</div>\r\n"
					+ "	<div>\r\n"
					+ "		<label>" + at.tokenID + "</label>\r\n"
					+ "	</div>\r\n"
					+ "	<div>\r\n"
					+ "		<label>" + at.creationData + "</label>\r\n"
					+ "	</div>\r\n"
					+ "	<div>\r\n"
					+ "		<label>" + at.expirationData + "</label>\r\n"
					+ "	</div>\r\n"
					+ "	<div>\r\n"
					+ "		<a href='/usermenu.html'>User Menu</a></td>\r\n"
					+ "	</div>\r\n"
					+ "</body>\r\n"
					+ "</html>\r\n";
			
			pw.println(html);
			
		} else {
			if (token != null) {
				Entity newToken = Entity.newBuilder(tokenKey).set("token_id", at.tokenID)
						.set("token_creation_data", at.creationData)
						.set("token_expiration_data", at.expirationData)
						.build();
				datastore.put(newToken);
				
				Cookie cookie = new Cookie(at.tokenID, at.username);
				
				res.addCookie(cookie);
				
				PrintWriter pw = res.getWriter();
				
				String html = "<!DOCTYPE html>\r\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "	<title>APDC-2022-Avaliacao-Individual</title>\r\n"
						+ "	<link href=\"style.css\" rel=\"stylesheet\">\r\n"
						+ "</head>\r\n"
						+ "	\r\n"
						+ "<body>\r\n"
						+ "	<h3>Logged!</h3>\r\n"
						+ "	<div>\r\n"
						+ "		<a href='/usermenu.html'>User Menu</a></td>\r\n"
						+ "	</div>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n";
				
				pw.println(html);
			}
			else
				res.sendError(Status.FORBIDDEN.getStatusCode());
		}
	}
	
	private boolean validLogin(Key userKey, String password) {
		Entity user = datastore.get(userKey);
		if (user != null)
			if (user.getString("user_pwd").equals(DigestUtils.sha512Hex(password)))
				return true;
		return false;
	}

}
