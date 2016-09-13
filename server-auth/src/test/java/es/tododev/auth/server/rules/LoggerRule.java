package es.tododev.auth.server.rules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class LoggerRule extends TestWatcher {

	private final Logger log;
 
    public LoggerRule(Class<?> clazz) {
    	log = LogManager.getLogger(clazz);
    }
 
    @Override
    protected void failed(Throwable e, Description description) {
        log.error(description, e);
    }
 
    @Override
    protected void succeeded(Description description) {
        log.info(description);
    }
	
}
