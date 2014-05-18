package es.tododev.auth.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigestGenerator {

	private final static String ALGORITHM="SHA-512";
	private final MessageDigest digester;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public DigestGenerator() throws NoSuchAlgorithmException{
		digester = MessageDigest.getInstance(ALGORITHM);
	}
	
	public String generateDigest(String appId, String appPassword, String sharedDomainToken, String role, String random){
		StringBuilder builder = new StringBuilder(appId).append(sharedDomainToken).append(role).append(appPassword).append(random);
		log.debug("Creating sign with: {} {} {} {} {}", appId, sharedDomainToken, role, appPassword, random);
    	return digest(builder.toString());
	}
	
	public String digest(String rawWord){
		byte[] bytes = rawWord.getBytes();
		digester.update(bytes);
    	bytes=digester.digest();
    	String hex=Hex.encodeHexString(bytes);
    	return hex;
	}
	
}
