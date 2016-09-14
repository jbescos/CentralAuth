package es.tododev.auth.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtractRoleFromPath {

	private final static Logger log = LogManager.getLogger();
	public static final String FILTER_AUTH_PATH = "auth";
	
	public String extract(String path){
		String[] words = path.split("\\/");
		int i = 0;
		for(String word : words){
			if(FILTER_AUTH_PATH.equals(word) && words.length >= i+1){
				String role = words[i+1];
				log.debug("Role {}", role);
				return role;
			}
			i++;
		}
		log.warn("Role not found");
		return null;
	}
	
}
