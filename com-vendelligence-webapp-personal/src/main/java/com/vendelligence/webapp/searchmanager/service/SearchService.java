package com.vendelligence.webapp.searchmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vendelligence.webapp.searchmanager.config.SearchConfig;
import com.vendelligence.webapp.searchmanager.config.VendorConfig;

/*
 * Service for Remote Google Custom Search API search methods. This build uses the Google CSE API.
 * 
 * This uses ResponseEntity<String>. While this works fine for the Javascript AJAX
 * request to the API, we will want to consider a stricter data type specifically
 * for used libraries: Jackson. Using List/Map/Hashmap is more common in most of
 * the Stack Overflow forums on the topic.
 *
 */

@Service
public class SearchService implements ISearchService
{
	// autowire the search configuration so we can flip from free to paid api on
	// demand
	@Autowired
	SearchConfig sc;

	// autowire the vendor configuration so the repository of vendors can be
	// searched
	@Autowired
	VendorConfig vc;

	// autowire the rest template from spring
	@Autowired
	RestTemplate restTemplate;

	private final Logger log = LoggerFactory.getLogger(getClass());

	// Holding off on switch to StringBuilder (singlethreaded) OR StringBuffer
	// (multithreaded) optimization as JVM compiler will normally
	// switch below concatenation to StringBuilder if it deems optimization is
	// required at compile time
	// Web References:
	// https://docs.oracle.com/javase/7/docs/api/java/lang/StringBuffer.html
	// https://docs.oracle.com/javase/7/docs/api/java/lang/StringBuilder.html
	// http://stackoverflow.com/questions/1532461/stringbuilder-vs-string-concatenation-in-tostring-in-java

	// define constants for api service endpoint url construction
	private static final String API_INIT_URL = "https://www.googleapis.com/customsearch/v1?key=";
	private static final String SELECT_FIELDS = "&fields=context,searchInformation,items&cx=";
	private static final String QUERY_MARKER = "&q=";
	private static final String NUMBER_START_INDEX_MARKER = "&num=10&start=";
	private static final String WHITE_SPACE = " ";

	/*
	 * Feature - sort by date function Author: niall guerin Date: 29.11.2016
	 * 
	 * API documentation reference for google cse json api integration:
	 * https://developers.google.com/custom-search/json-api/v1/reference/cse/
	 * list
	 */
	private static final String SORT_BY_DATE = "&sort=date";

	// the following 3 methods use sort-by-relevance mode

	// default vendor repository search using generic search request for
	// paginator requests
	public String searchVendorRepositoryWithFilter(String q,
			String vendor, int startIndex, boolean sortByDate) throws Exception
	{
		String vendorFilter = vc.getVendor().get(vendor);

		// service endpoint using dynamic parameters in http request from client
		String googleCseApiUrl = "";
		
		if (!sortByDate)
		{
			googleCseApiUrl = API_INIT_URL + sc.getApiFreeKey() + SELECT_FIELDS
					+ vendorFilter + QUERY_MARKER + q
					+ NUMBER_START_INDEX_MARKER + startIndex;
		}
		else
		{
			googleCseApiUrl = API_INIT_URL + sc.getApiFreeKey() + SELECT_FIELDS
					+ vendorFilter + QUERY_MARKER + q
					+ NUMBER_START_INDEX_MARKER + startIndex + SORT_BY_DATE;
		}

		// log the default url
		log.info("Default API URL: " + googleCseApiUrl);

		// modified this url to remove the "/1" string value in the parameter
		// list as it was breaking the google cse api url
		ResponseEntity<String> response = restTemplate
				.getForEntity(googleCseApiUrl, String.class);

		log.info("Google CSE API Response: " + response.getBody());
		
		return response.getBody();
	}

	// refine the vendor repository search - only available for
	// vendor-filter-driven searches
	public String refineSearchResults(String q, String vendor,
			String rlabel, boolean sortByDate) throws Exception
	{
		String vendorFilter = vc.getVendor().get(vendor);
		// https://developers.google.com/custom-search/docs/xml_results#xml-results-for-regular-and-advanced-search-queries
		String googleCseApiUrl = "";
		
		if (!sortByDate)
		{
			googleCseApiUrl = API_INIT_URL + sc.getApiFreeKey() + SELECT_FIELDS
					+ vendorFilter + QUERY_MARKER + q + WHITE_SPACE + rlabel;
		}
		else
		{
			googleCseApiUrl = API_INIT_URL + sc.getApiFreeKey() + SELECT_FIELDS
					+ vendorFilter + QUERY_MARKER + q + WHITE_SPACE + rlabel
					+ SORT_BY_DATE;
		}
		// log the refined url
		log.info("Refined API URL: " + googleCseApiUrl);

		// modified this url to remove the "/1" string value in the parameter
		// list as it was breaking the google cse api url
		ResponseEntity<String> response = restTemplate
				.getForEntity(googleCseApiUrl, String.class);

		// log the response from the searchservice
		log.info("Google CSE API Response from Refined Search Results: "
				+ response.getBody());

		return response.getBody();
	}

	// clone of previous method to test using refinement search AND startIndex
	// for pagination option
	public String refineSearchResultsWithStartIndex(String q,
			String vendor, String rlabel, int startIndex, boolean sortByDate)
			throws Exception
	{
		String vendorFilter = vc.getVendor().get(vendor);

		// https://developers.google.com/custom-search/docs/xml_results#xml-results-for-regular-and-advanced-search-queries
		String googleCseApiUrl = "";

		if (!sortByDate)
		{
			googleCseApiUrl = API_INIT_URL + sc.getApiFreeKey() + SELECT_FIELDS
					+ vendorFilter + QUERY_MARKER + q + WHITE_SPACE + rlabel
					+ NUMBER_START_INDEX_MARKER + startIndex;
		}
		else
		{
			googleCseApiUrl = API_INIT_URL + sc.getApiFreeKey() + SELECT_FIELDS
					+ vendorFilter + QUERY_MARKER + q + WHITE_SPACE + rlabel
					+ NUMBER_START_INDEX_MARKER + startIndex + SORT_BY_DATE;
		}
		// log the refined url
		log.info("Refined API URL: " + googleCseApiUrl);

		// modified this url to remove the "/1" string value in the parameter
		// list as it was breaking the google cse api url
		ResponseEntity<String> response = restTemplate
				.getForEntity(googleCseApiUrl, String.class);

		// log the response from the searchservice
		log.info("Google CSE API Response from Refined Search Results: "
				+ response.getBody());

		return response.getBody();
	}

}