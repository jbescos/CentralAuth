package es.tododev.auth.server.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.jvnet.hk2.annotations.Service;

@Service
public class HK2InterceptionService implements InterceptionService {

	private final static List<MethodInterceptor> TRANSACTIONAL_LIST = Collections.singletonList(new TransactionalInterceptor());
	
	@Override
	public Filter getDescriptorFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MethodInterceptor> getMethodInterceptors(Method method) {
		if (method.isAnnotationPresent(Transactional.class)) {
            return TRANSACTIONAL_LIST;
        }
		return null;
	}

	@Override
	public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
		// TODO Auto-generated method stub
		return null;
	}

}
