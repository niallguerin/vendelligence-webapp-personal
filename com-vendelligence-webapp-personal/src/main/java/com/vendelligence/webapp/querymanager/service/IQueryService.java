package com.vendelligence.webapp.querymanager.service;

// java imports
import java.util.List;

// spring framework imports
import org.springframework.data.domain.Page;

// vendelligence imports
import com.vendelligence.webapp.querymanager.model.Query;

public interface IQueryService
{
	// save a query using DTO and id
	void saveQuery(Query queryObject);
	
	// update a query using DTO and id
	void updateQuery(final Query queryObject, Long id);
	
	// delete a query using DTO and id
	void deleteQuery(Long id);
	
	// get individual query by id
	Query getQueryById(final long id);
	
	// list all queries
	List<Query> findAll();
	
	// add Paginate support
	Page<Query> findAll(Integer pagenum);
	
	// get total record count
	Long getCount();
	
}
