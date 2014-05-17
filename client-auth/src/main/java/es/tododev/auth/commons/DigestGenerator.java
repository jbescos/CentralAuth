package es.tododev.auth.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class DigestGenerator {

	private final static String ALGORITHM="SHA-512";
	private final MessageDigest digester;
	
	public DigestGenerator() throws NoSuchAlgorithmException{
		digester = MessageDigest.getInstance(ALGORITHM);
	}
	
	public String generateDigest(String appId, String appPassword, String sharedDomainToken, String role){
		StringBuilder builder = new StringBuilder(appId).append(sharedDomainToken).append(role).append(appPassword);
		byte[] bytes=builder.toString().getBytes();
		digester.update(bytes);
    	bytes=digester.digest();
    	String hex=Hex.encodeHexString(bytes);
    	return hex;
	}
	
}
