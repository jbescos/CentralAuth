package es.tododev.auth.server.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.commons.dto.RespAuthorizationDTO;

public class AuthorizeService {

	private final EntityManager em;
	private final DigestGenerator digestGenerator;
	
	@Inject
	public AuthorizeService(EntityManager em, DigestGenerator digestGenerator){
		this.em = em;
		this.digestGenerator = digestGenerator;
	}
	
	public RespAuthorizationDTO authorize(ReqAuthorizationDTO in){
		RespAuthorizationDTO out = new RespAuthorizationDTO();
		
		return out;
	}
	
}
