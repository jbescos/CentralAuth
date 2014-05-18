package es.tododev.auth.server.tag;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PrintIFrame extends TagSupport {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final static String REPLACER = "crossdomain";
	private final static String TEMPLATE = "<iframe src='"+REPLACER+"' style='visibility:hidden;display:none' height='0' width='0'></iframe>";

	@Override
	public int doStartTag() throws JspException {

		final StringBuilder builder = new StringBuilder();

		ServletRequest request = pageContext.getRequest();
		for (@SuppressWarnings("unchecked") Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements();) {
			String attName = e.nextElement();
			if(isInt(attName)){
				try{
					String attValue = (String) request.getAttribute(attName);
					String iframe = TEMPLATE.replace(REPLACER, attValue);
					builder.append(iframe);
				}catch(ClassCastException ex){}
			}
		}

		try {
			pageContext.getOut().append(builder.toString());
		} catch (IOException e) {
			log.error("", e);
		}
		return SKIP_BODY;
	}
	
	private boolean isInt(String attName){
		try{
			Integer.parseInt(attName);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

}
