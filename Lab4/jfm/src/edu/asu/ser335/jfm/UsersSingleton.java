/**
 * 
 */
package edu.asu.ser335.jfm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jfm.main.CommonConstants;
import org.jfm.main.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kevinagary
 *
 */
public final class UsersSingleton {

	private static final Hashtable<String, String> userPasswordMapping = new Hashtable<String, String>();
	private static final Hashtable<String, String> userRoleMapping = new Hashtable<String, String>();
	private static UsersSingleton theUsers = null;
	
	private UsersSingleton() {
		UsersSingleton.loadUsers();
	}
	
	public static UsersSingleton getUsers() {
		if (theUsers == null) {
			theUsers = new UsersSingleton();
		}
		return theUsers;
	}
	
	public static final Map<String, String> getUserRoleMapping() throws Exception {
		if (UsersSingleton.userRoleMapping == null) {
			loadUsers();
		}
		return Collections.unmodifiableMap(userRoleMapping);
	}

	public static final Map<String, String> getUserPasswordMapping() throws Exception {
		if (UsersSingleton.userPasswordMapping == null) {
			loadUsers();
		}
		return Collections.unmodifiableMap(userPasswordMapping);
	}

	public static final boolean createPasswordMapping(String userName, String password, String role) throws Exception {
		boolean rval = true;
		List<User> users;
		
		// Check if user already exists.
		if (!userName.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
			if (UsersSingleton.userRoleMapping.containsKey(userName)
					&& UsersSingleton.userRoleMapping.get(userName).equals(role)) {
				rval = false;
			} else {
				// If user does not exists, create a new salted password and add the user details
				// in userPasswordMapping, userRoleMapping, userSaltMapping
				// and all write details in authentication.json and salt.json

				String saltedPassword = SaltsSingleton.getUserSalts().createSaltedPassword(userName, password);

				UsersSingleton.userPasswordMapping.put(userName, saltedPassword);
				System.out.println("userPasswordMapping: " + UsersSingleton.userPasswordMapping);

				UsersSingleton.userRoleMapping.put(userName, role);
				System.out.println("userRoleMapping: " + UsersSingleton.userRoleMapping);

				User u = new User();
				u.setName(userName);
				u.setPassword(saltedPassword);
				u.setRole(role);

				// adding the new user to authentication.json
				ObjectMapper mapper = new ObjectMapper();
				try {
						InputStream inputStream = new FileInputStream(new File(CommonConstants.AUTHENTICATION_FILE));
						TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {};
					users = mapper.readValue(inputStream, typeReference);
					users.add(u);
					mapper.writeValue(new File(CommonConstants.AUTHENTICATION_FILE), users);
				} catch (IOException e1) {
					e1.printStackTrace();
					rval = false;
				}
			}
		}
		return rval;
	}
	
	// loads authentication.json and create userPasswordMapping and userRoleMapping
	private static final void loadUsers() {
		List<User> users;
		try {
			// using jackson data binder
			ObjectMapper mapper = new ObjectMapper();		
			InputStream inputStream = new FileInputStream(new File(CommonConstants.AUTHENTICATION_FILE));
			TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {
			};
			users = mapper.readValue(inputStream, typeReference);
			for (User u : users) {
				UsersSingleton.userPasswordMapping.put(u.getName(), u.getPassword());
				UsersSingleton.userRoleMapping.put(u.getName(), u.getRole());

			}
			System.out.println("Users loaded !!");
			System.out.println("userRoleMapping: " + UsersSingleton.userRoleMapping);
			System.out.println("userPasswordMapping: " + UsersSingleton.userPasswordMapping);

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
