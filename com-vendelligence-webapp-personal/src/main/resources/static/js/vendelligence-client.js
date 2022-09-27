/*
 * Vendelligence main javascript client. This is a simple js client originally written
 * to integrate with an elasticsearch server. It uses a few global variables, and was
 * adapted to integrate with the google custom search api. It was ported into the spring
 * boot application for vendelligence as I prototyped initially in pure javascript with
 * callbacks to google custom search api.
 * 
 * It is possible to take this js client, modify it, and bypass completely the spring boot
 * querymanager controller and associated interfaces/services. 
 * 
 * I kept it integrated as the js client lets me iterate new
 * functions quickly and integrate them with the querymanager, which for me personally,
 * is an important part of why I built vendelligence. I wanted both options in case I was
 * in scenario where spring boot local or remote server was not working and I needed
 * to slim back the functionality to the most basic requirement - search my curated
 * vendor repository using a simple http client.
 * 
 * The current version uses a mix of basic javascript and jquery. A further cleanup
 * is needed as there is functional duplication in statements repeated in some functions
 * which can be refactored into their own function based on the usage pattern.
 * 
 */

// validator message containers
var $queryDiv = $('#queryMsg');

// html5 web storage support - use session storage so user does not have to clean up local files
var sessionStorage;

// track where we are in the pagination ui (3 pages max based on own search patterns)
var currentPageIndex = 0;

// track sort-by function request
var sortByDate = false;

// get the pathname only
var urlPath = window.location.pathname;
var matchingUrlPath = "/querymanager/query-console";

// function - check dom is ready for processing
$(document).ready(function() 
{
	// check state of activity and present ui in response to last event in history (if any)
	$queryDiv.hide();
	console.log("Calling checkActivityState function.");
	checkActivityState();
	
	// handle filter by vendor changes during result display from prior vendor and adapt ui components to selection changes
	$("input[name$='vkb']").click(function() 
		{
			// reset the query string field if the vendor context changes
			$('#query').val("");
			
			// decide which ui panels to show and hide based on vendor filter selector change
			$('#pre-processing-filter').show();
			$('#response-panel div').empty();
			$('#pagination-panel').empty();
			// clear session storage and any message divs if user switches vendor filter selector
			sessionStorage.clear();
			$queryDiv.hide();
	    });
	
	// check whether we handle default filter selection or load a saved query filter
	if ( urlPath === matchingUrlPath ) 
	{
		// handle default radio button list vendor selection - fragment defaults to aws
		$('[name="queryVendorFilter"]').val($( "input:radio[name=vkb]:checked" ).val());
		
		console.log("URL Pathname is:" + urlPath);
	}
	else
	{
	//	 handle default radio button list vendor selection. this must be done on load after we read the db record
		$(function loadSavedQueryVendorFilter()
		{
			var savedQueryVendorFilter = $('[name="queryVendorFilter"]').val();
			var savedQueryString = $('[name="queryString"]').val();
	
			$('input[name=vkb]').val([savedQueryVendorFilter]);
			$('#query').val(savedQueryString);
		});
	}
	
	// handle search reset - use this for full reset of search selection options including clearing of results history
	$("#searchResetButton").click(function() 
		{
			console.log("Cleared query-console search view back to default selection.")
			// reset the query string field if the vendor context changes
			$('#query').val("");
			// reset the vendor filter to empty
			$('[name="vkb"]').prop('checked', false);
			
			// decide which ui panels to show and hide based on vendor filter selector change
			$('#pre-processing-filter').show();
			$('#response-panel div').empty();
			$('#pagination-panel').empty();
			// clear session storage and any message divs if user switches vendor filter selector
			sessionStorage.clear();
			$queryDiv.hide();
	    });
	
	// handle sort-by-date button event
	$("#sortByDateButton").click(function() 
			{
				console.log("User selected Sort-by-date function.")
				// update the global sort-by-date flag and reset it on returned results
				sortByDate = true;
				$(apiSearch());
		    });
	
	// handle sort-by-relevance button event
	$("#sortByRelevanceButton").click(function() 
			{
				console.log("User selected Sort-by-Relevance function.")
				// update the global sort-by-date flag and reset it on returned results			
				resetSortByDate();
				$(apiSearch());
		    });
	
	//========================================================================================================================================================================

	//handle filter by vendor changes dynamically using hidden input field reference for crud form query object binding
	$(function() 
	{
		$('[name="vkb"]').on('change', function() 
				{
	        		$('[name="queryVendorFilter"]').val($(this).val());
				});
	});

	//========================================================================================================================================================================

	//handle queryString clone to crud form query object binding. this is used used for the crud form that lets us create, read, update, delete queries
	$('#query').on('input',function(e)
	{
		$('[name="queryString"]').val($(this).val());
	});

	//========================================================================================================================================================================

	/*
	 * functions - handle keypress or mouseclick event for search and fire main search function.
	 */
	$(function searchReactorSearchField()
	{
		$('#query').on('keypress', function(eventReturnKey)
		{
		    if (eventReturnKey.which === 13) 
		    {
		    	// critical to avoid browser default and vendorfilter resets - refer to web developer tips on stack overflow
		    	eventReturnKey.preventDefault();
		    	
		    	// reset sortByDate global
		    	resetSortByDate();
		    	
		    	$(apiSearch());
		    }
		});
	});

//========================================================================================================================================================================

	/*
	 * functions - handle keypress or mouseclick event for search and fire main search function.
	 */
	$(function searchReactorButton()
	{
		$('#searchButton').on('keypress click', function(eventKeypressClick)
		{
		    if (eventKeypressClick.which === 13 || eventKeypressClick.type === 'click') 
		    {
		    	// critical to avoid browser default and vendorfilter resets - refer to web developer tips on stack overflow
		    	eventKeypressClick.preventDefault();
		    	
		    	// reset sortByDate global
		    	resetSortByDate();
		    	
		    	$(apiSearch());
		    }
		});
	});
	
});

//========================================================================================================================================================================

/*
 * function - checkactivitystate to verify ui display actions primarily for standard search history or refinement label history.
 */
function checkActivityState()
{
	// 1. check if a query was entered and we executed a refined search
	if (sessionStorage.labelString)	
	{
		console.log("Query was entered AND we did refined search. Reloading the Results History - refined search.");
		refineSearch(sessionStorage.labelString);
	}
	
	// 2. check if a query was entered and we executed a standard search. does not cater for page selector use case
	else if (sessionStorage.queryExecuted)
	{
		console.log("Query was entered AND we did standard search. Reloading the Results History - standard search.");
		apiSearch();
	}
	
	// 3. check if empty default state
	else if (sessionStorage.queryRecord == "")
	{
		console.log("No query entered. UI default state.");
	}
}

//========================================================================================================================================================================

/*
 * function - google custom search apisearch for passing form data to the search api
 */
function apiSearch() 
{
	console.log("Called Google Custom Search - apiSearch function with default startIndex.");
	{
		// get the querystring
		var queryString = getQueryString();
	
		// initialize the sessionstorage so we can handle results history use case
		sessionStorage.setItem("queryRecord", queryString);
		sessionStorage.setItem("queryExecuted", true);
		
		var vfilter = getVendorFilter();
		console.log("Vendor Filter: " + vfilter);
		
		// update the current global page number index
		currentPageIndex = 1;
		
		// apisearch() function calls use startindex of 10 (default)
		var defaultStartIndex = 10;
		
		// validate filter was chosen, query was entered, and trigger api call if the client-side validation passes
		validateVendorAndQuery(vfilter, queryString, defaultStartIndex);
	}
}

//========================================================================================================================================================================

/*
 * function - verifies querystring
 */
function getQueryString()
{
	var qstring = "";
	if($('#query').val() === "")
	{
		qstring = sessionStorage.queryRecord;
	}
	else
	{
		qstring = $('#query').val();
	}
	return qstring;
}

// ========================================================================================================================================================================

/*
 * function - obtains vendor filter selection from user
 */
function getVendorFilter()
{
	var vtarget = "";
	var radioNodeList = document.getElementsByName("vkb");
	for (var i = 0; i < radioNodeList.length; i++)
	{
		if (radioNodeList[i].checked)
		{
			vtarget = radioNodeList[i].value;
			console.log("Vendor Array Value is: " + vtarget);
		}
	}
	
	// handle browsers that reset the filter to empty value
	if ((vtarget === "")&&(sessionStorage.filter))
	{
		vtarget = sessionStorage.filter;
	}
	
	return vtarget;
}

//========================================================================================================================================================================

/*
 * function - validates if a filter was entered and displays message to handle blank filter use case. refactored to allow validation re-use
 */
function validateVendorAndQuery(f, q, resultStartIndex)
{
	// 1. check they supplied a query even if they supplied a filter as we always have default filter
	if (q == null || q == 'undefined')
	{
		var noQueryMsg = "<strong>No query provided. Please enter a query, so we can search the vendor repositories.</strong>";
		$queryDiv.show().html(noQueryMsg);
		console.log("API call rejected as no Query defined by user.");
	}
	// only let the custom search api service calls proceed if the two preconditions are met by the client
	else
	{	
		// hide the divs
		$queryDiv.hide();
		// Set sessionStorage copy of the filter
		sessionStorage.setItem("filter", f);
		
		// call the custom search api
		apiRequestHandler(resultStartIndex);
		console.log("apiRequestHandler service call initiated.");
	}
}

// ========================================================================================================================================================================

/*
 * function - jquery ajax call to search api to determine starting index from the api result payload
 */
function apiRequestHandler(stIndex)
{
	// clean up if any refinement api calls were made previously
	var emptyString = "";
	sessionStorage.setItem("labelString", emptyString);
	
	var apiurl = "/search";
	
	// variables needed for the search api service
	var queryString = getQueryString();
	
	// get the filter
	var vfilter = getVendorFilter();
	
	// log the parameters used to make the call
	console.log("Called the search api with this URL:" + apiurl);
	
	// log the status of the sort function
	console.log("The Sort-by-Date function value is: " + sortByDate);
	
	// evaluate if default sort-by-relevance or explicit-sort-by-date is requested by user
	$.ajax({
		url : apiurl,
		type : "GET",
		data: {query: queryString, vendor: vfilter, startIndex: stIndex, sortMethod: sortByDate},
		dataType : 'json',
		async : 'true',
		success : function(response) 
		{
			console.log("Search API Generic Request Handler called by jQuery ajax request");
			apiResponseHandler(response);
		}
	});
}

//========================================================================================================================================================================

// function - handle refinement label button click events for filtering the results to more precise information objects
function refineSearch(refLabel)
{
	// set the refinementlabel in the local store
	sessionStorage.setItem("labelString", refLabel);
	
	// specify the api endpoint
	var refinedUrl = "/search-refinement";
	
	var vfilter = getVendorFilter();
	var queryString = getQueryString();
	
	// log the status of the sort function
	console.log("The Sort-by-Date function value is: " + sortByDate);
	console.log("Refined URL: " + refinedUrl);
	
	$.ajax({
			url : refinedUrl,
			type : "GET",
			data: {query: queryString, vendor: vfilter, reflabel: refLabel, sortMethod: sortByDate},
			dataType : 'json',
			async : 'true',
			success : function(refResponse) 
			{
				console.log("Refined Search API endpoint called by jQuery ajax request");
				
				// utility functions: for verifying the response payloads
				console.log("Refine Search Results Response is: " + refResponse);
				
				// let the apiresponsehandler take it directly as a json object if the call is successful.
				apiResponseHandlerSinglePage(refResponse);
			}
	});
}

//========================================================================================================================================================================

// function - handle refinement label button click events for filtering the results to more precise information objects with dynamic startindex
function refineSearchWithStartIndex(refLabel, startingIndex)
{
	// update the local store with the refinement label
	sessionStorage.setItem("labelString", refLabel);
	
	// specify the api endpoint
	var refinedUrl = "/search-refinement-startindex";
	
	// local function variables for vendor filter and the search query string
	var vfilter = getVendorFilter();
	var queryString = getQueryString();
	
	// log the status of the sort function
	console.log("The Sort-by-Date function value is: " + sortByDate);
	console.log("Refined URL: " + refinedUrl);
	
	$.ajax({
			url : refinedUrl,
			type : "GET",
			data: {query: queryString, vendor: vfilter, reflabel: refLabel, startIndex: startingIndex, sortMethod: sortByDate},
			dataType : 'json',
			async : 'true',
			success : function(refResponse) 
			{
				console.log("Refined Search API endpoint with dynamic startIndex called by jQuery ajax request with HTTP response status 200-OK");
				
				// utility functions: for testing the response payloads. move away from console loggers and replace with unit tests
				console.log("Refine Search Results Response with dynamic startIndex is: " + refResponse);
				
				// let the apiresponsehandler callback handle the refined payload response
				apiResponseHandlerSinglePage(refResponse);
			}
	});
	
}

//========================================================================================================================================================================

// function - callback for search api response payload
function apiResponseHandler(response)
{
	console.log("Reached apiResponseHandler");
	
	// set the search box to always have the same string - ui enhancement only so user and myself are not confused with blank form on some browsers
	$('#query').val(sessionStorage.queryRecord);
	
	// clear the pre-processing filter
	$('#pre-processing-filter').hide();
	
	// clear any no query supplied messages
	$queryDiv.hide();
	
	// clear the contents from the response-panel main div container
	$('#response-panel div').empty();
	
	// handle the result count and time to execute
	var resultsCount = response.searchInformation.totalResults;
	// var resultsTime = response.searchInformation.formattedSearchTime;
	
	// show the results count to the ui
	// document.getElementById("results-metadata").innerHTML += "<p>About " + resultsCount + " results in " + resultsTime + " seconds." + "<hr>";
	
	// show the refinements to the ui - they will always be vendor-relevant
	// use the anchor property per api documentation to display to human ui
	for (var k = 0; k < response.context.facets.length; k++)
	{
		var facetItem = response.context.facets[k];
		var rlabel = facetItem[0].anchor;
		var filter = facetItem[0].label_with_op;
		console.log("Facets Element: " + facetItem[0].anchor);
		document.getElementById("results-content").innerHTML += "<button class='btn btn-primary' id='" + filter + "' onclick='refineSearch(this.id)'>" + rlabel + "</button>";
	}

	// show the results (10 items maximum) and validate if resultscount = 0, > 0, and > 10 so we handle various search api response use cases.
	for (var i = 0; i < response.items.length; i++) 
	{
		console.log("Response Items Length: " + response.items.length);
		var item = response.items[i];
		var titleLink = getLink(item.title, item.link);
	
		// add resource hyperlinks to the results
		// WORKING CODE: document.getElementById("results-content").innerHTML += "<br />" + titleLink + "<h6>" + item.link + "</h6>" + item.snippet + "<br />";
		/*
		 * Change Request: Add UI clipper for the url into the main text area of the Query panel to save copy and paste action.
		 * Status: Used .text and .append. Both are inconsistent. The first is more stable; the second works fine IF I do not edit
		 * in the textarea between click events on the button.
		 * Author: Niall Guerin
		 * Date: 22-Dec-2016
		 * 
		 */
		document.getElementById("results-content").innerHTML += '<br />' + '<button class="btn btn-default btn-sm" id="copytitle" onclick="clipTitle(' + i + ')"><span class="glyphicon glyphicon-plus"></span> Clip title</button>' + '<br />' + "<p id=title-" + i + ">"  + titleLink + "</p>" + "<h6 " + "id=titlelink-" + i + ">" + item.link + "</h6>" + '<button class="btn btn-default btn-sm" id="copytitlelink" onclick="clipTitleLink(' + i + ')"><span class="glyphicon glyphicon-plus"></span> Clip weblink</button>' + '<br />' + item.snippet + "<br />";
	}

	// if the resultscount is > 10 and we have not already paginated the panel, then paginate the results one time, so we do not keep redoing it per new request.
	if (resultsCount > 10)
	{
		// empty the div panel as user could run completely new search so keeping pagination panel static is not accurate view of live results
		// older build behavior cached the panel pagination layout but this caused issues if user runs random new search in the main search ui
		$("#pagination-panel").empty();
		paginateResults(resultsCount);
	}
	
	// determine when to display the previous and next buttons in the pagination panel.
	previousNextButtonDisplay(currentPageIndex);
}

//========================================================================================================================================================================

// function - single page for refined search results:
// note to self: google custom search control-panel tests and own local client tests show odd behaviour after page 1 of refined search
function apiResponseHandlerSinglePage(response)
{
	console.log("Reached apiResponseHandlerSinglePage.");
	
	// clear the pre-processing filter
	$('#pre-processing-filter').hide();
	
	// clear any no query supplied messages
	$queryDiv.hide();
	
	// clear the contents from the response-panel main div container
	$('#response-panel div').empty();
	
	// clear any prior pagination-panel display as we do not paginated refined result sets
	$("#pagination-panel").empty();
	
	// handle the result count and time to execute
	var resultsCount = response.searchInformation.totalResults;
	// I do not care about the search time but if it becomes an issue disable comments below
	// var resultsTime = response.searchInformation.formattedSearchTime;
	
	// show the results count to the ui
	// document.getElementById("results-metadata").innerHTML += "<p>About " + resultsCount + " results in " + resultsTime + " seconds." + "<hr>";
	
	// show the refinements to the ui - they will always be vendor-relevant
	// use the anchor property per api documentation to display to human ui
	for (var k = 0; k < response.context.facets.length; k++)
	{
		var facetItem = response.context.facets[k];
		var rlabel = facetItem[0].anchor;
		var filter = facetItem[0].label_with_op;
		console.log("Facets Element: " + facetItem[0].anchor);
		document.getElementById("results-content").innerHTML += "<button class='btn btn-primary' id='" + filter + "' onclick='refineSearch(this.id)'>" + rlabel + "</button>";
	}

	// show the results (10 items maximum) and validate if resultscount = 0, > 0, and > 10 so we handle various search api response use cases.
	for (var i = 0; i < response.items.length; i++) 
	{
		console.log("Response Items Length: " + response.items.length);
		var item = response.items[i];
		var titleLink = getLink(item.title, item.link);
	
		// add resource hyperlinks to the results
		document.getElementById("results-content").innerHTML += '<br />' + '<button class="btn btn-default btn-sm" id="copytitle" onclick="clipTitle(' + i + ')"><span class="glyphicon glyphicon-plus"></span> Clip title</button>' + '<br />' + "<p id=title-" + i + ">"  + titleLink + "</p>" + "<h6 " + "id=titlelink-" + i + ">" + item.link + "</h6>" + '<button class="btn btn-default btn-sm" id="copytitlelink" onclick="clipTitleLink(' + i + ')"><span class="glyphicon glyphicon-plus"></span> Clip weblink</button>' + '<br />' + item.snippet + "<br />";
	}
	// no pagination function call for refined search results
}

//========================================================================================================================================================================

/*
 * The following functions are UI web clipper functions to quickly copy and paste text from the results titles or links directly into
 * my notes panel. It saves me doing ctrl+c and ctrl+v and the clunky select action otherwise needed on the result items. This is
 * not pretty, but it gets the job done. We use .val instead of .append in the textarea; otherwise there are DOM update issues if
 * I go into textarea, edit, and then try to re-append. The current version comfortably handles repeat actions whether it is first
 * time or repeat view visits.
 */

//function - ui clipper - title
function clipTitle(titleNum) 
{
	var marker = "#";
	var titleId = "title-" + titleNum.toString();
	var target = mergeMarkerId(marker, titleId);
	
	var inputStream = $(target).text();
	var outputStream = clipAppender(inputStream);
	
	$("#querynotesarea").val(outputStream);
	
	titleId = "";
	target = "";
}

// function - ui clipper - weblink
function clipTitleLink(titleNum) 
{
	var marker = "#";
	var titleLinkId = "titlelink-" + titleNum.toString();
	var target = mergeMarkerId(marker, titleLinkId);
	
	var inputStream = $(target).text();
	var outputStream = clipAppender(inputStream);
	
	$("#querynotesarea").val(outputStream);
	
	titleLinkId = "";
	target = "";
}

// function clip appender
function clipAppender(inputText)
{
	// this function takes inputs, appends them, and returns them so val() is used instead of append() to allow edit actions by user
	// using append() shows textarea updates but they are not being stored or saved. .val() works without issues on save and edit
	var nlcr = "\n\n";
	var outputText = "";
	var currentText = $("#querynotesarea").val();
	
	// concatenate to what we already have in the textarea so we do not lose data on update
	var outputText = outputText.concat(currentText, inputText, nlcr);
	console.log("Output text is:" + outputText);
	return outputText;
}

// function merge marker and id
function mergeMarkerId(marker, id)
{
	var mergedMarkerTitle = "";
	mergedMarkerTitle += mergedMarkerTitle.concat(marker, id);
	
	return mergedMarkerTitle;
}

//========================================================================================================================================================================


//utility function - get the querystring and the queryvendor
function getQueryMetadata()
{
	// placholder for packaging repeat statements into subroutine as still duplicates in main search functions
}

//========================================================================================================================================================================

// utility function - determine which pagination buttons to display for previous and next
function previousNextButtonDisplay(cpi)
{
	if (cpi == 1)
	{
		$("#prev-btn").hide();
	}
	else
	{
		$("#prev-btn").show();
	}
	
	if (cpi == 3)
	{
		$("#next-btn").hide();
	}
	else
	{
		$("#next-btn").show();
	}	
}

//========================================================================================================================================================================

// utility function - linkify a title using the hyperlink and title passed in as a parameters to the function
function getLink(title, link) 
{
	var remoteLink = title.link(link);
	return remoteLink;
}	

//========================================================================================================================================================================

// function - paginate results: show 5 pages of 10 resultlist items at a time per screen. index start at 1 not 0 so we can reuse variable into ui
function paginateResults(resultCount)
{
	var numberOfPages = 3;
	
	/* the following code lets us update the ui pagination-panel dynamically.
	 * we only use the inner code in the if condition, if resultscount < 30. 
	 * no pagination for refined search use cases.
	 */
	var remainder;
	
	if(resultCount < 30)
	{
		numberOfPages = resultCount / 10;
		remainder = resultCount % 10;
		if (remainder != 0)
		{
			// use math.floor to reduce the decimal value downward (math.floor()) when dealing with decimal point value for modulus != 0 use cases.
			numberOfPages++;
			Math.floor(numberOfPages);
			console.log("Lower number of pages for pagination is: " + numberOfPages);
		}
	}
	document.getElementById("pagination-panel").innerHTML += "<button class='btn btn-primary' id='prev-btn' onclick='previousButton()'>Previous</button>";
	for (var j = 1; j <= numberOfPages; j++)
	{
		var thisPage = j;
		document.getElementById("pagination-panel").innerHTML += "<button class='btn btn-primary' id='page-btn-" + thisPage + "' onclick='getClickedPageNumber(this.id)'>" + thisPage + "</button>";
	}
	document.getElementById("pagination-panel").innerHTML += "<button class='btn btn-primary' id='next-btn' onclick='nextButton()'>Next</button>";
}

//========================================================================================================================================================================

// function - determine clicked page number to control the startindex
function getClickedPageNumber(buttonid)
{
	var startIndex = 0;
	switch (buttonid)
	{
		case "page-btn-1":
		startIndex=10;
		currentPageIndex = 1;
		apiRequestHandler(startIndex);
		break;
		
		case "page-btn-2":
		startIndex=20;
		currentPageIndex = 2;
		apiRequestHandler(startIndex);
		break;
		
		case "page-btn-3":
		startIndex=30;
		currentPageIndex = 3;
		apiRequestHandler(startIndex);
		break;
	}
}

//========================================================================================================================================================================

// function - click previousbutton in pagination ui
function previousButton()
{
	var previousIndex = (currentPageIndex - 1).toString();
	var buttonidmapper = "page-btn-" + previousIndex;
	console.log("Previous Button identifier: " + buttonidmapper);
	getClickedPageNumber(buttonidmapper);
}

// function - click nextbutton in pagination ui
function nextButton()
{
	var nextIndex = (currentPageIndex + 1).toString();
	var buttonidmapper = "page-btn-" + nextIndex;
	console.log("Previous Button identifier: " + buttonidmapper);
	getClickedPageNumber(buttonidmapper);
}

//========================================================================================================================================================================

// function reset sortByDate global variable to default value
function resetSortByDate()
{
	sortByDate = false;
}
//end vendelligence - client javascript library
//author: niall guerin, october 2016