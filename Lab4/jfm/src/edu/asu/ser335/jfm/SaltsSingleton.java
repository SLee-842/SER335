/**
 * 
 */
package edu.asu.ser335.jfm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.List;
import java.util.Base64.Encoder;

import org.jfm.main.CommonConstants;
import org.jfm.main.Salt;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.whitfin.siphash.SipHasher;

/**
 * @author kevinagary
 *
 */
public final class SaltsSingleton {
	private static final Hashtable<String, String> userSaltsMapping = new Hashtable<String, String>();
	private static SaltsSingleton theSalts = null;
	
	private SaltsSingleton() throws Exception {
		SaltsSingleton.loadSalts();
	}
	
	public static final SaltsSingleton getUserSalts() throws Exception {
		if (SaltsSingleton.theSalts == null) {
			theSalts = new SaltsSingleton();
		}
		return theSalts;
	}
	
	public final String getUserSalt(String user) {
		return userSaltsMapping.get(user);
	}
	
	public final String createSaltedPassword(String userName, String password) throws Exception {
		List<Salt> salts;
		
		// Generate the random salt
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		Encoder encoder = Base64.getUrlEncoder().withoutPadding();
		byte[] tempArray = encoder.encode(bytes);
		byte [] salt = Arrays.copyOfRange(tempArray, 0, 16);

		// Store the salt in userMapping
		String slt = new String(salt);
		userSaltsMapping.put(userName, slt);
		System.out.println("userSaltMapping: " + userSaltsMapping);

		// adding salt of new user to salts.json
		Salt s = new Salt();
		s.setName(userName);
		s.setSalt(slt);
		ObjectMapper map = new ObjectMapper();
		try {
			InputStream inputStream = new FileInputStream(new File(CommonConstants.SALTS_FILE));
			TypeReference<List<Salt>> typeReference = new TypeReference<List<Salt>>() {};
			salts = map.readValue(inputStream, typeReference);
			salts.add(s);
			map.writeValue(new File(CommonConstants.SALTS_FILE), salts);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw(e1);
		}

		long salted_password = SipHasher.hash(salt, password.getBytes());

		return Long.toString(salted_password);
	}
	
	// load salts.json and create userSaltMapping.
	private static void loadSalts() throws Exception {
		List<Salt> userSalts;
		try {
			// using jackson data binder
			ObjectMapper mapper = new ObjectMapper();
			InputStream inputStream = new FileInputStream(new File(CommonConstants.SALTS_FILE));
			TypeReference<List<Salt>> typeReference = new TypeReference<List<Salt>>() {
			};
			userSalts = mapper.readValue(inputStream, typeReference);
			for (Salt s : userSalts) {
				userSaltsMapping.put(s.getName(), s.getSalt());
			}
			System.out.println("Salts Loaded !!");
			System.out.println("userSaltMapping: " + userSaltsMapping);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw(e);
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw(e);
		}
	}
}
