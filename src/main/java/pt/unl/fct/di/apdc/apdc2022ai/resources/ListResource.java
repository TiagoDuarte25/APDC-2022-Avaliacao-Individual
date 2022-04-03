package pt.unl.fct.di.apdc.apdc2022ai.resources;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import pt.unl.fct.di.apdc.apdc2022ai.utils.UserData;

@WebServlet("/list")
public class ListResource extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Entity user = datastore.get(userKey);
		
		long userRole = Long.parseLong(user.getProperties().get("user_role").get().toString());
		
		if(user != null && user.getString("user_state").equals("ACTIVE")) {
		
			PrintWriter pw = res.getWriter();
			
			Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind("User")
				.setFilter(
						PropertyFilter.le("user_role", userRole)
				)		
				.build();
				
			QueryResults<Entity> entities = datastore.run(query);
			
			List<UserData> users = new ArrayList<UserData>();
			
			entities.forEachRemaining(u -> {
				String userId = u.getKey().getName();
				String email = u.getString("user_email");
				String name = u.getString("user_name");
				String profile = u.getString("user_profile");
				String landline = u.getString("user_fix");
				String phone = u.getString("user_phone");
				String address = u.getString("user_address");
				String postalCode = u.getString("user_postalCode");
				
				users.add(new UserData(userId, email, name, profile, landline, phone, address, postalCode));
			});
			
			if (!users.isEmpty()) {
				Iterator<UserData> it = users.iterator();
				while(it.hasNext()) {
					UserData u = it.next();
					pw.println("User Info:");
					pw.println(" ");
					pw.println(u.username);
					pw.println(u.email);
					pw.println(u.name);
					pw.println(u.profile);
					pw.println(u.landline);
					pw.println(u.phone);
					pw.println(u.address);
					pw.println(u.postalCode);
					pw.println(" ");
				}
			}
			else
				pw.print("No Users to list!");
		} else 
			res.sendError(Status.FORBIDDEN.getStatusCode());
	}
}
