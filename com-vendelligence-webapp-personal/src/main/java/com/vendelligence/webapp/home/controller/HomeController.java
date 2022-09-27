package com.vendelligence.webapp.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * The Home View Controller handles HTTP requests to "/" URL mapping.
 * It is based on GitHub Spring Boot secure web sample application.
 * 
 */

@Controller
public class HomeController
{
	@RequestMapping("/")
	public String getHome() 
	{
		return "redirect:/querymanager/query-console";
	}
}