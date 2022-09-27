package com.vendelligence.webapp.searchmanager.config;

/*
 * Search API Configuration class. We set up the properties as they are sensitive from the application.*.properties files
 * depending on the active Spring Profile. This avoids writing key values directly into the source code in design view.
 * 
 * This class chooses between the Google Custom Search freeapikey or paidapikey. My private build uses
 * a paid apikey to allow 500K queries per year for individual scenarios. The freeapikey is for 
 * development or community edition aws demo use and maxes out at 100 queries in a 24 hour period.
 * 
 */
public class SearchConfig
{
	private String apiPaidKey;
	private String apiFreeKey;
	
	public SearchConfig()
	{
	}
	
	public String getApiPaidKey()
	{
		return apiPaidKey;
	}

	public String getApiFreeKey()
	{
		return apiFreeKey;
	}

	public void setApiPaidKey(String apiPaidKey)
	{
		this.apiPaidKey = apiPaidKey;
	}

	public void setApiFreeKey(String apiFreeKey)
	{
		this.apiFreeKey = apiFreeKey;
	}

}
