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

	private static final void writeAuthFile(User u) throws IOException {
		// adding the new user to authentication.json
		ObjectMapper mapper = new ObjectMapper();
		List<User> users;
		InputStream inputStream = new FileInputStream(new File(CommonConstants.AUTHENTICATION_FILE));
		TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {};
		users = mapper.readValue(inputStream, typeReference);
		users.add(u);
		mapper.writeValue(new File(CommonConstants.AUTHENTICATION_FILE), users);
	}

	public static final boolean createPasswordMapping(String userName, String password, String role) throws Exception {
		boolean rval = true; // we assume user does not exist

		// Check if user already exists.
		if (!userName.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
			if (UsersSingleton.userRoleMapping.containsKey(userName)
					&& UsersSingleton.userRoleMapping.get(userName).equals(role)) {
				rval = false;
			} else {
				// If user does not exist, create a new salted password and add the user details
				// in userPasswordMapping, userRoleMapping, userSaltMapping
				// and all write details in authentication.json and salt.json
				String saltedPassword = null;
				try {
					saltedPassword = SaltsSingleton.getUserSalts().createSaltedPassword(userName, password, true);
				} catch (Exception exc) {
					// if we got an exception on this call then the salt was not saved, so we should abort by rethrowing exc
					throw new Exception(exc);
				}
				// now we can go ahead with our work
				User u = new User();
				u.setName(userName);
				u.setPassword(saltedPassword);
				u.setRole(role);

				try {
					writeAuthFile(u);
				} catch (Exception exc2) {
					// one problem we have is that we are not transactional; if we fail writing the auth file but succeeded
					// with the salts file above we will be out of sync
					throw new Exception("Unable to write auth file, salts file may be corrupted");
				}
				UsersSingleton.userPasswordMapping.put(userName, saltedPassword);
				UsersSingleton.userRoleMapping.put(userName, role);
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
