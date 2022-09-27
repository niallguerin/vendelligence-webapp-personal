package com.vendelligence.webapp.searchmanager.service;

import org.springframework.http.ResponseEntity;

public interface ISearchService
{
	// advanced search using vendor filter for api call to google custom search
	String searchVendorRepositoryWithFilter(String query, String vendor, int startIndex, boolean sortByDate) throws Exception;
	
	// refine search results using user interface pre-defined pre and post processing filters
	String refineSearchResults(String query, String vendor, String rlabel, boolean sortByDate) throws Exception;
	
	// refine search results with dynamic startIndex
	String refineSearchResultsWithStartIndex(String query, String vendor, String rlabel, int startIndex, boolean sortByDate) throws Exception;
	
}
