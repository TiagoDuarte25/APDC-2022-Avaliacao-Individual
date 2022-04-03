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

@WebServlet("/delete")
public class DeleteResource extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String username = req.getParameter("username");
		
		String password = req.getParameter("pwd");
		
		String userToRemove = req.getParameter("removeuser");
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		
		Key userRemoveKey = datastore.newKeyFactory().setKind("User").newKey(userToRemove);
		
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(userToRemove);
		
		Entity user = datastore.get(userKey);
		
		Entity userR = datastore.get(userRemoveKey);
		
		Entity token = datastore.get(tokenKey);
		
		PrintWriter pw = res.getWriter();
		
		if(user != null && userR != null && user.getString("user_state").equals("ACTIVE")) {
			long roleU = (long) user.getProperties().get("user_role").get();
			long roleR = (long) userR.getProperties().get("user_role").get();
			if(roleU > roleR) {
				if(!user.getString("user_pwd").equalsIgnoreCase(DigestUtils.sha512Hex(password)))
					pw.println("User or password wrong");
				else {
					datastore.delete(userRemoveKey);
					if (token != null)
						datastore.delete(tokenKey);
					pw.println("User deleted.");
				}
			} else if(roleU == roleR) {
				if(username == userToRemove) {
					datastore.delete(userRemoveKey);
					if (token != null)
						datastore.delete(tokenKey);
					pw.println("User deleted.");
				} else
					res.sendError(Status.FORBIDDEN.getStatusCode());
			} else 
				
				res.sendError(Status.FORBIDDEN.getStatusCode());
			
		}else
			pw.println("User or password wrong");
	}
}
