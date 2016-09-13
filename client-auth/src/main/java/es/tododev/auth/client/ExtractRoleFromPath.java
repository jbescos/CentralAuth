package es.tododev.auth.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtractRoleFromPath {

	private final static Logger log = LogManager.getLogger();
	public static final String FILTER_AUTH_PATH = "auth";
	
	public String extract(String path){
		String[] words = path.split("\\/");
		log.info("Number of words {}", words.length);
		int i = 0;
		for(String word : words){
			log.info("Checking {}", word);
			if(FILTER_AUTH_PATH.equals(word) && words.length >= i+1){
				return words[i+1];
			}
			i++;
		}
		log.warn("Role not found");
		return null;
	}
	
}
