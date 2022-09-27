package com.vendelligence.webapp.errormanager;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/* 
 * This is a simple general Error Controller based on the Spring Boot Reference guide.
 * http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling
 * 
 * I have more specific exception handler from courses I did with Eugen's material, so will
 * modify the error handling to facilitate specific error conditions in the next build. 
 * 
 */

@Controller
public class GenericErrorController implements ErrorController
{
	private static final String ERROR_PATH = "/error";
	
	// packaged web app physical resource location
	private static final String GLOBAL_ERROR_PAGE = "/errormanager/global-error";
	
    @RequestMapping(value=ERROR_PATH)
    public String error()
    {
    	return GLOBAL_ERROR_PAGE;
    }

	@Override
	public String getErrorPath() 
	{
		return ERROR_PATH;
	}
}