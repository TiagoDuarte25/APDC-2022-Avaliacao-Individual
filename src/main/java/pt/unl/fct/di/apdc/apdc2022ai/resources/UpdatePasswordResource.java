package pt.unl.fct.di.apdc.apdc2022ai.resources;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Value;

@WebServlet("/updatepassword")
public class UpdatePasswordResource extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("pwd");
		String newPassword = req.getParameter("newpwd");
		String confirmation = req.getParameter("confirmation");
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Entity user = datastore.get(userKey);
		
		if (user != null && user.getString("user_state").equals("ACTIVE")) {
			
			
			if (validLogin(userKey, password) && newPassword.equals(confirmation)) {
				
				Value<?> ct = user.getProperties().get("user_creation_time");
				String email = user.getString("user_email");
				String name = user.getString("user_name");
				String profile = user.getString("user_profile");
				String landline = user.getString("user_fix");
				String phone = user.getString("user_phone");
				String address = user.getString("user_address");
				String postalCode = user.getString("user_postalCode");
				Value<?> userRole = user.getProperties().get("user_role");
				String state = user.getString("user_state");
				
			
				Entity newUser = Entity.newBuilder(userKey)
						.set("user_pwd", DigestUtils.sha512Hex(newPassword))
						.set("user_creation_time", ct)
						.set("user_email", email)
						.set("user_name", name)
						.set("user_profile", profile)
						.set("user_fix", landline)
						.set("user_phone", phone)
						.set("user_address", address)
						.set("user_postalCode", postalCode)
						.set("user_role", userRole)
						.set("user_state", state)
						.build();
				
				datastore.put(newUser);;
				
				PrintWriter pw = res.getWriter();
				
				String html = "<!DOCTYPE html>\r\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "	<title>APDC-2022-Avaliacao-Individual</title>\r\n"
						+ "	<link href=\"style.css\" rel=\"stylesheet\">\r\n"
						+ "</head>\r\n"
						+ "	\r\n"
						+ "<body>\r\n"
						+ "	<h3>Password Updated!</h3>\r\n"
						+ "	<div>\r\n"
						+ "		<a href='/usermenu.html'>User Menu</a></td>\r\n"
						+ "	</div>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n";
				
				pw.println(html);
			}
			else 
				res.sendError(Status.FORBIDDEN.getStatusCode());
		} else {
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
