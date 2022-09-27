package com.vendelligence.webapp.querymanager.controller;

/*
 * The QueryController handles basic CRUD operations for the querymanager package.
 * It lets the user CREATE, RETRIEVE, UPDATE, and DELETE query objects. It lets
 * them LIST queries and there is an optional but as of yet de-activated PAGE
 * LIST option two using spring pagination support based on guide samples.
 * 
 * Update: October 17, 2016
 * I have done direct end-to-end search and CRUD operations over multiple vendors and queries.
 * No issue at time of this update. Add to test suite requirements.
 * 
 */

// java utility imports
import java.util.ArrayList;

// logger imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// spring framework imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// vendelligence imports
import com.vendelligence.webapp.querymanager.model.Query;
import com.vendelligence.webapp.querymanager.service.IQueryService;

// API References
// http://projects.spring.io/spring-data-jpa/
// http://docs.spring.io/spring-data/jpa/docs/1.9.1.RELEASE/reference/html/
// http://docs.spring.io/spring-data/data-commons/docs/current/api/org/springframework/data/domain/PageRequest.html
// http://docs.spring.io/spring-data/commons/docs/current/reference/html/
// http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html

/*
 * The QueryController handles all HTTP request/responses to the
 * QueryManager package components. The QueryManager is a simple add on function
 * to enable storage, annotation, retrieval, and upates to queries.
 * 
 * Update: October 17, 2016
 * 1) A separate CRUD-only build is in development at time of writing to allow
 * Projects and Topics to reflect personal workflow requirement and that of
 * intial users in testing as they need to store by project if doing multiple
 * internal projects or external client facing projects.
 * 
 */

@Controller
public class QueryController
{
	// http://www.slf4j.org/api/org/slf4j/LoggerFactory.html
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private IQueryService queryService;

	public QueryController()
	{
		super();
	}

	// view the query console
	@RequestMapping(value = "/querymanager/query-console", method = RequestMethod.GET)
	public String viewQueryConsole(Model model)
	{
		// bind the query data model to the html form
		Query query = new Query();
		model.addAttribute("query", query);

		return "querymanager/query-console";
	}

	// handle user save action for query and custom notes
	@RequestMapping(value = "/querymanager/query-console-submit", method = RequestMethod.POST)
	public String saveQuery(@ModelAttribute Query query)
	{
		queryService.saveQuery(query);
		return "querymanager/query-saved";
	}

	// list all queries stored by the user
	@RequestMapping(value = "/querymanager/query-console-list", method = RequestMethod.GET)
	public String listAllQueries(Model model)
	{
		model.addAttribute("querylist", queryService.findAll());
		return "querymanager/query-console-list";
	}

	// list and use spring pagination support - do not use own pagination
	// function
	@RequestMapping(value = "/querymanager/query-console-list-paginated", method = RequestMethod.GET)
	public String listAllQueriesPaginate(
			@RequestParam(value = "page") Integer pagenum, Model model)
	{
		Page<Query> p = queryService.findAll(pagenum);
		int totalPages = p.getTotalPages();

		// handle 0 stored queries
		if (totalPages == 0)
		{
			return "redirect:/querymanager/query-console-list";
		} 
		else
		{
			int cpage = p.getNumber() + 1;
			ArrayList<Integer> pageRange = new ArrayList<Integer>(totalPages);
			for (int i = 0; i < totalPages; i++)
			{
				pageRange.add(i);
				System.out.println("Value of index: " + i);
			}

			boolean nextPage = p.hasNext();
			boolean prevPage = p.hasPrevious();

			model.addAttribute("pagedquerylist", queryService.findAll(pagenum));
			model.addAttribute("querylistsize", queryService.getCount());

			model.addAttribute("cpage", cpage);
			model.addAttribute("pagerange", pageRange);
			model.addAttribute("totalpages", totalPages);
			model.addAttribute("nextpage", nextPage);
			model.addAttribute("prevpage", prevPage);
		}
		return "querymanager/query-console-list-paginated";
	}

	// update the selected query id in edit mode
	@RequestMapping(value = "/querymanager/query-console/{id}/update", method = RequestMethod.POST)
	public String updateQueryById(@PathVariable("id") Long id,
			@ModelAttribute Query query)
	{
		queryService.updateQuery(query, id);
		LOGGER.debug("The Query object ID is: " + id);
		return "querymanager/query-updated";
	}

	// delete query by id
	@RequestMapping(value = "/querymanager/query-console/{id}/delete", method = RequestMethod.POST)
	public String deleteQuery(@PathVariable("id") Long id)
	{
		// Delete the query
		queryService.deleteQuery(id);
		return "querymanager/query-deleted";
	}

	// query read-update navigation based on selected id in query list
	@RequestMapping(value = "/querymanager/query-console-list/{id}/update", method = RequestMethod.GET)
	public String updateReadQueryById(@PathVariable("id") Long id, Model model)
	{
		Query query = queryService.getQueryById(id);
		model.addAttribute("query", query);
		return "querymanager/query-console-read-update";
	}

	// delete query based on query list selection id
	@RequestMapping(value = "/querymanager/query-console-list/{id}/delete", method = RequestMethod.POST)
	public String deleteQueryById(@PathVariable("id") Long id)
	{
		queryService.deleteQuery(id);
		return "querymanager/query-deleted";
	}

}