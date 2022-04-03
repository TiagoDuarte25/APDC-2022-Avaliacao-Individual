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

@WebServlet("/update")
public class UpdateResource extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		String userToChange = req.getParameter("userupdate");
		String email = req.getParameter("email");
		String name = req.getParameter("name");
		String profile = req.getParameter("profile");
		String landLine = req.getParameter("landline");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		String postalCode = req.getParameter("postc");
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Key userUpdateKey = datastore.newKeyFactory().setKind("User").newKey(userToChange);
		
		Entity user = datastore.get(userKey);
		
		Entity userUpdate = datastore.get(userUpdateKey);
		
		if (user != null && userUpdate != null && user.getString("user_state").equals("ACTIVE")) {
			
			long userRole = Long.parseLong(user.getProperties().get("user_role").get().toString());
			long userUpdateRole = Long.parseLong(userUpdate.getProperties().get("user_role").get().toString());
			
			if (userRole >= userUpdateRole) {
				if(userRole == 1 && username.equals(userToChange) && email == null && name == null) {
					
					String newEmail = "";
					String newName = "";
					String newProfile = "";
					String newLandline = "";
					String newPhone = "";
					String newAddress = "";
					String newPostalCode = "";
					
					newEmail = user.getString("user_email");
					
					newName = user.getString("user_name");
					
					if (profile != null)
						newProfile = profile;
					else
						newProfile = user.getString("user_profile");
					
					if (landLine != null)
						newLandline = landLine;
					else
						newLandline = user.getString("user_fix");
					
					if (phone != null)
						newPhone = phone;
					else
						newPhone = user.getString("user_phone");
					
					if (address != null)
						newAddress = address;
					else
						newAddress = user.getString("user_address");
					
					if (postalCode != null)
						newPostalCode = postalCode;
					else
						newPostalCode = user.getString("user_postalCode");
					
					String pwd = user.getString("user_pwd");
					Value<?> ct = user.getProperties().get("user_creation_time");
					String state = user.getString("user_state");
				
					Entity newUser = Entity.newBuilder(userKey)
							.set("user_pwd", pwd)
							.set("user_creation_time", ct)
							.set("user_email", newEmail)
							.set("user_name", newName)
							.set("user_profile", newProfile)
							.set("user_fix", newLandline)
							.set("user_phone", newPhone)
							.set("user_address", newAddress)
							.set("user_postalCode", newPostalCode)
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
							+ "	<h3>Updated!</h3>\r\n"
							+ "	<div>\r\n"
							+ "		<a href='/usermenu.html'>User Menu</a></td>\r\n"
							+ "	</div>\r\n"
							+ "</body>\r\n"
							+ "</html>\r\n";
					
					pw.println(html);
				}	else if(userRole != 1) {
					
						String newEmail = "";
						String newName = "";
						String newProfile = "";
						String newLandline = "";
						String newPhone = "";
						String newAddress = "";
						String newPostalCode = "";
						
						if (email != null)
							newEmail = email;
						else
							newEmail = user.getString("user_email");
						
						if (name != null)
							newName = name;
						else
							newName = user.getString("user_name");
						
						if (profile != null)
							newProfile = profile;
						else
							newProfile = user.getString("user_profile");
						
						if (landLine != null)
							newLandline = landLine;
						else
							newLandline = user.getString("user_fix");
						
						if (phone != null)
							newPhone = phone;
						else
							newPhone = user.getString("user_phone");
						
						if (address != null)
							newAddress = address;
						else
							newAddress = user.getString("user_address");
						
						if (postalCode != null)
							newPostalCode = postalCode;
						else
							newPostalCode = user.getString("user_postalCode");
						
						String pwd = user.getString("user_pwd");
						Value<?> ct = user.getProperties().get("user_creation_time");
						String state = user.getString("user_state");
					
						Entity newUser = Entity.newBuilder(userKey)
								.set("user_pwd", pwd)
								.set("user_creation_time", ct)
								.set("user_email", newEmail)
								.set("user_name", newName)
								.set("user_profile", newProfile)
								.set("user_fix", newLandline)
								.set("user_phone", newPhone)
								.set("user_address", newAddress)
								.set("user_postalCode", newPostalCode)
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
								+ "	<h3>Updated!</h3>\r\n"
								+ "	<div>\r\n"
								+ "		<a href='/usermenu.html'>User Menu</a></td>\r\n"
								+ "	</div>\r\n"
								+ "</body>\r\n"
								+ "</html>\r\n";
						
						pw.println(html);		
				} else
					res.sendError(Status.FORBIDDEN.getStatusCode());
			} else
				res.sendError(Status.FORBIDDEN.getStatusCode());
			
		} else {
			res.sendError(Status.FORBIDDEN.getStatusCode());
		}
	}
}
