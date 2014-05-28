package es.tododev.auth.server.provider;

import java.util.UUID;

import javax.inject.Singleton;

@Singleton
public class UUIDgenerator {

	public String create(){
		return UUID.randomUUID().toString();
	}
	
}
