package com.vendelligence.webapp.querymanager.service;

import java.time.LocalDateTime;
// java imports
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// vendelligence imports
import com.vendelligence.webapp.querymanager.model.Query;
import com.vendelligence.webapp.querymanager.dao.QueryRepository;

/*
 * The QueryService class provides method implementations
 * from the IQueryService interface. The Query Manager
 * uses Spring JPA so we have various CRUD operations
 * and basic query operations.
 */
@Service
@Transactional
public class QueryService implements IQueryService
{
	// autowire QueryRepository interface as this is the critical interface for supporting CRUD and JPA operations
	@Autowired
	private QueryRepository queryRepository;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	// constant field variable for the size of the number of queries (size) per page when using List with Pagination
	private static final int QUERIES_PER_PAGE = 10;
	
	@Override
	public void saveQuery(final Query queryObject)
	{
		LocalDateTime localDateTime = LocalDateTime.now();
		
        Query query = new Query();
        query.setQueryVendorFilter(queryObject.getQueryVendorFilter());
        query.setQueryString(queryObject.getQueryString());
        query.setQueryRefinement(queryObject.getQueryRefinement());
        query.setQueryName(queryObject.getQueryName());
        query.setQueryTags(queryObject.getQueryTags());
        query.setQueryNotes(queryObject.getQueryNotes());
        // creation date entry
        query.setCreationDateTime(localDateTime);
        query.setChangeDateTime(localDateTime);
        
        log.info("The Creation Date is: " + query.getCreationDateTime());
        log.info("The Change Date is: " + query.getChangeDateTime());
        queryRepository.save(query);
	}

	@Override
	public void deleteQuery(Long queryId)
	{
		final Query query = queryRepository.findOne(queryId);
		queryRepository.delete(query);
	}
	
	public void updateQuery(final Query queryObject, Long id)
	{
		LocalDateTime localDateTime = LocalDateTime.now();
		// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/SessionAttributes.html
		// Refer to Baeldung Persistence API Ebook PDF for best practices with Hibernate and JPA in Spring
		
		// find the object we want to update based on id of active object
		final Query query = queryRepository.findOne(id);
		
		// update the field values
		query.setQueryVendorFilter(queryObject.getQueryVendorFilter());
		query.setQueryString(queryObject.getQueryString());
		query.setQueryRefinement(queryObject.getQueryRefinement());
		query.setQueryName(queryObject.getQueryName());
		query.setQueryTags(queryObject.getQueryTags());
		query.setQueryNotes(queryObject.getQueryNotes());
		
		// update the change date and time field
		query.setChangeDateTime(localDateTime);
		log.info("The Change Date for the Updated Query is: " + query.getChangeDateTime());
		
		// http://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html#saveAndFlush-S-
 		queryRepository.save(query);
	}
	
	// Finder methods: fineOne, findAll queries in the repository
	
	@Override
	public Query getQueryById(final long id)
	{
		return queryRepository.findOne(id);
	}
	
	@Override
	public List<Query> findAll()
	{
		List<Query> queryList = queryRepository.findAll();
		return queryList;
	}
	
	/*
	 * http://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html (based on section
	 * 1.2 PagingAndSortingRepository
	 * 
	 */
	@Override
	public Page<Query> findAll(Integer pagenum)
	{	
		// adjust the request input to allow for start of 0 versus start of 1. Spring JPA uses 0, not 1
		PageRequest pRequest = new PageRequest(pagenum-1, QUERIES_PER_PAGE);		
		Page<Query> pagedQueryList = queryRepository.findAll(pRequest);
		return pagedQueryList;
	}
	
	// getting direct count here so we are not overriding the autowired interface method with our custom Query type
	public Long getCount()
	{
		return queryRepository.count();
	}

}
