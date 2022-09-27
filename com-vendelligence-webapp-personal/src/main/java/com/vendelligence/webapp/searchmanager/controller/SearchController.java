package com.vendelligence.webapp.searchmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// test imports
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;

// Add Search API support
import com.vendelligence.webapp.searchmanager.service.ISearchService;

/*
 * Simple Controller intended only to verify flow from UI submit query to cross-system boundary Google Search API.
 * 
 */

@RestController
public class SearchController
{
	@Autowired
	private ISearchService searchService; 
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	// http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/converter/json/MappingJacksonValue.html
	// Updated build using RestController, ResponseEntity<String> return type based on validated JSON response from RestTemplate
	// Sending this ResponseEntity data type value back to the Javascript client.
	// http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
	
	// main search request handler used for paginated http requests to the google custom search API
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String searchVendorRepository(@RequestParam(value="query") String query, @RequestParam(value="vendor") String vendor, @RequestParam(value="startIndex") String stIndex, @RequestParam(value="sortMethod") boolean sortByDate)
	{
		int startIndex = Integer.parseInt(stIndex);
		
		String requestResponse = null;
		try
		{
			requestResponse = searchService.searchVendorRepositoryWithFilter(query, vendor, startIndex, sortByDate);
		} 
		catch (Exception e)
		{
			log.info("Error calling search-advanced api:" + e);
		}

		// log the results in case of incorrect vendor to results mapping e.g. search for aws and get docker results
		log.info("Results for search-advanced: " + requestResponse);
		
		return requestResponse;
	}
	
	// refine the search results using the refinement labels - keep same terminology as google custom search api (refine/refinement)
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value="/search-refinement", method=RequestMethod.GET)
	public String refineSearchResults(@RequestParam(value="query") String query, @RequestParam(value="vendor") String vendor, @RequestParam(value="reflabel") String reflabel, @RequestParam(value="sortMethod") boolean sortByDate)
	{
		String refinedResults = null;
		try
		{
			refinedResults = searchService.refineSearchResults(query, vendor, reflabel, sortByDate);
		} 
		catch (Exception e)
		{
			log.info("Error calling search-refinement API:" + e);
		}

		// log the results in case of incorrect vendor to results mapping e.g. search for aws and get docker results
		log.info("Results for search-refine: " + refinedResults);
		
		return refinedResults;
	}
	
	// refine the search results using the refinement labels and a startindex
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value="/search-refinement-startindex", method=RequestMethod.GET)
	public String refineSearchResultsStartIndex(@RequestParam(value="query") String query, @RequestParam(value="vendor") String vendor, @RequestParam(value="reflabel") String reflabel, @RequestParam(value="startIndex") String stIndex, @RequestParam(value="sortMethod") boolean sortByDate)
	{
		int startIndex = Integer.parseInt(stIndex);
		
		String refinedResults = null;
		try
		{
			refinedResults = searchService.refineSearchResultsWithStartIndex(query, vendor, reflabel, startIndex, sortByDate);
		} 
		catch (Exception e)
		{
			log.info("Error calling search-refinement-startindex API with dynamic start index:" + e);
		}

		// log the results in case of incorrect vendor to results mapping e.g. search for aws and get docker results
		log.info("Results for search-refine with dynamic start index: " + refinedResults);
		
		return refinedResults;
	}
	
}
