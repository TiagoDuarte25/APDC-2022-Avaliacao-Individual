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

@WebServlet("/stateuser")
public class UpdateUserStateResource extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		String userToChangeState = req.getParameter("userState");
		String state = req.getParameter("state");
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Key userStateKey = datastore.newKeyFactory().setKind("User").newKey(userToChangeState);
		
		Entity user = datastore.get(userKey);
		
		Entity userState = datastore.get(userStateKey);
		
		
		if (user != null && userState != null && user.getString("user_state").equals("ACTIVE")) {
			
			long role = Long.parseLong(user.getProperties().get("user_role").get().toString());
			long roleS = Long.parseLong(userState.getProperties().get("user_role").get().toString());
			
			String newState = userState.getString("user_state");
			
			if (username.equals(userToChangeState)) {
				if (role == 1) {
					if (state.equals("INACTIVE"))
						newState = state;
					else
						res.sendError(Status.FORBIDDEN.getStatusCode());
				} else 
					newState = state;
			} else {
				if (role > roleS) {
					newState = state;
				} else
					res.sendError(Status.FORBIDDEN.getStatusCode());
			}	
				String pwd = userState.getString("user_pwd");
				Value<?> ct = userState.getProperties().get("user_creation_time");
				String email = userState.getString("user_email");
				String name = userState.getString("user_name");
				String profile = userState.getString("user_profile");
				String landline = userState.getString("user_fix");
				String phone = userState.getString("user_phone");
				String address = userState.getString("user_address");
				String postalCode = userState.getString("user_postalCode");
				
			
				Entity newUser = Entity.newBuilder(userStateKey)
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
						.set("user_state", newState)
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
						+ "	<h3>State Updated!</h3>\r\n"
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
