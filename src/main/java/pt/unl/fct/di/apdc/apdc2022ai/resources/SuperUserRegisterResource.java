package pt.unl.fct.di.apdc.apdc2022ai.resources;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

import pt.unl.fct.di.apdc.apdc2022ai.utils.Role;

@WebServlet("/registerSU")
public class SuperUserRegisterResource extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		
		
		String username = req.getParameter("username");
		String password = req.getParameter("pwd");
		String confirmation = req.getParameter("cpwd");
		String email = req.getParameter("email");
		String name = req.getParameter("name");
		String profile = req.getParameter("profile");
		String landline = req.getParameter("landline");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		String postalCode = req.getParameter("postc");
		long userRole = 4;
		String state = "ACTIVE";
		
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Entity user = datastore.get(userKey);
		
		if (user == null) {
			if (validRegistration(username, password, confirmation, email, name)) {
		
		
				Entity newUser = Entity.newBuilder(userKey)
						.set("user_pwd", DigestUtils.sha512Hex(password))
						.set("user_creation_time", Timestamp.now())
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
				
				datastore.add(newUser);
				
				PrintWriter pw = res.getWriter();
				
				String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "	<title>APDC-2022-Avaliacao-Individual</title>\r\n"
						+ "	<link href=\"style.css\" rel=\"stylesheet\">\r\n"
						+ "</head>\r\n"
						+ "	\r\n"
						+ "<body>\r\n"
						+ "	<h3>User Information</h3>\r\n"
						+ "	<form >\r\n"
						+ "	<div>\r\n"
						+ "		<label>"+ username +"</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + email + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + name + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + profile + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + landline + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + phone + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + address + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + postalCode + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + Role.from(userRole) + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<label>" + state + "</label>\r\n"
						+ "	</div>\r\n"
						+ "	<div>\r\n"
						+ "		<a href='/menu.html'>Next</a></td>\r\n"
						+ "	</div>\r\n"
						+ "	</form>\r\n"
						+ "</body>\r\n"
						+ "</html>";
				
				pw.println(html);
			}
			else
				res.sendError(Status.FORBIDDEN.getStatusCode());
		}
			else
				res.sendError(Status.FORBIDDEN.getStatusCode());
		
		
		
	}
	
	private boolean validRegistration(String username, String password, String confirmation, String email,
			String name) {
		return (username != null && password != null && email != null && name != null && password.equals(confirmation));
	}
}