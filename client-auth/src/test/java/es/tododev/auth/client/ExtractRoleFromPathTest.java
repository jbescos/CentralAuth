package es.tododev.auth.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExtractRoleFromPathTest {

	private final String[] PATHS = {"/sample-app/test/sample/auth/user", "/sample-app/test/sample/auth/user/other"};
	private final ExtractRoleFromPath extrater = new ExtractRoleFromPath();
	
	@Test
	public void extractRole(){
		for(String path : PATHS){
			assertEquals("user", extrater.extract(path));
		}
	}
	
}
