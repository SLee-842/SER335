/**
 * 
 */
package edu.asu.ser335.jfm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jfm.main.CommonConstants;
import org.jfm.main.Role;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kevinagary
 *
 */
public final class RolesSingleton {
	private static Hashtable<String, String[]> rolePrivilegesMapping = new Hashtable<String, String[]>();
	private static RolesSingleton theRoles = null;
	
	private RolesSingleton() {
		RolesSingleton.loadRoles();
	}
	
	public static final RolesSingleton getRoleMapping() {
		if (theRoles == null) {
			theRoles = new RolesSingleton();
		}
		return theRoles;
	}

	public final String[] getDisplayRoles() {
		ArrayList<String> displayRoles = new ArrayList<String>();
		Enumeration<String> e = rolePrivilegesMapping.keys();
		String role = null;
		while (e.hasMoreElements()) {
			role = e.nextElement();
			System.out.println("Adding rike: " + role);
			displayRoles.add(role);
		}
		return displayRoles.toArray(new String[] {});
	}
	
	public final String[] getPrivilegesForRole(String role) {
		String[] rval = rolePrivilegesMapping.get(role);
		if (rval != null) {
			rval = rval.clone();
		}
		return rval;
	}
	
	// load authorization.json and create rolePrivilegesMapping.
	private static void loadRoles() {
		List<Role> userRole;

		try {
			// using jackson data binder
			ObjectMapper mapper = new ObjectMapper();
			InputStream inputStream = new FileInputStream(new File(CommonConstants.AUTHORIZATION_FILE));
			TypeReference<List<Role>> typeReference = new TypeReference<List<Role>>() {};
			userRole = mapper.readValue(inputStream, typeReference);
			for (Role u : userRole) {
				rolePrivilegesMapping.put(u.getRole(), u.getPrivileges());
			}
			System.out.println("User roles and privileges loaded !!");
			System.out.println("rolePrivilegesMapping: " + RolesSingleton.rolePrivilegesMapping);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
