package com.vendelligence.webapp.searchmanager.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Vendor configuration class using properties driven by the active spring-profile and application-xxx.properties.
 * This was implemented to allow easier maintenance of single code branch instead of different branches for public
 * and private repository and to remove vendor properties from Java source files.
 * 
 * This solution using externalized configuration is modelled on stack overflow answers across
 * a few threads from pivotal spring boot developer - andy wilkinson.
 * http://stackoverflow.com/questions/24917194/spring-boot-inject-map-from-application-yml
 * http://stackoverflow.com/questions/26275736/how-to-pass-a-mapstring-string-with-application-properties
 * 
 */

@ConfigurationProperties("")
public class VendorConfig
{
	private final Map<String, String> vendor = new HashMap<>();

	public Map<String, String> getVendor() 
	{
		return vendor;
	}
}
