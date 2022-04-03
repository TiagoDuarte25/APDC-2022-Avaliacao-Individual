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
import com.google.cloud.datastore.Value;

import pt.unl.fct.di.apdc.apdc2022ai.utils.Role;

@WebServlet("/roleuser")
public class UpdateRoleResource extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		String userRole = req.getParameter("userrole");
		String roleChange = req.getParameter("role");
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Key userRoleKey = datastore.newKeyFactory().setKind("User").newKey(userRole);
		
		Entity user = datastore.get(userKey);
		
		Entity userR = datastore.get(userRoleKey);
		
		
		if (user != null && userR != null && user.getString("user_state").equals("ACTIVE")) {
			
			long role = Long.parseLong(user.getProperties().get("user_role").get().toString());
			long roleS = Long.parseLong(userR.getProperties().get("user_role").get().toString());
			
			
			if (role > roleS) {
				if (role == 4)
					roleS = Role.valueOf(roleChange).weight;
				else if (role == 3 && Role.valueOf(roleChange).weight < 3)
					roleS = Role.valueOf(roleChange).weight;	
				else
					res.sendError(Status.FORBIDDEN.getStatusCode());
			} else {
				res.sendError(Status.FORBIDDEN.getStatusCode());
			}	
				String pwd = userR.getString("user_pwd");
				Value<?> ct = userR.getProperties().get("user_creation_time");
				String email = userR.getString("user_email");
				String name = userR.getString("user_name");
				String profile = userR.getString("user_profile");
				String landline = userR.getString("user_fix");
				String phone = userR.getString("user_phone");
				String address = userR.getString("user_address");
				String postalCode = userR.getString("user_postalCode");
				String state = userR.getString("user_state");
				
			
				Entity newUser = Entity.newBuilder(userRoleKey)
						.set("user_pwd", pwd)
						.set("user_creation_time", ct)
						.set("user_email", email)
						.set("user_name", name)
						.set("user_profile", profile)
						.set("user_fix", landline)
						.set("user_phone", phone)
						.set("user_address", address)
						.set("user_postalCode", postalCode)
						.set("user_role", roleS)
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
						+ "	<h3>Role Updated!</h3>\r\n"
						+ "	<div>\r\n"
						+ "		<a href='/usermenu.html'>User Menu</a></td>\r\n"
						+ "	</div>\r\n"
						+ "</body>\r\n"
						+ "</html>\r\n";
				
				pw.println(html);
		} else {
			res.sendError(Status.FORBIDDEN.getStatusCode());
		}
	}
}
