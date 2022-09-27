package com.vendelligence.webapp.querymanager.dao;

// spring framework imports
import org.springframework.data.jpa.repository.JpaRepository;

// keep this for future development as it was used in prior builds for paging - do not use own paging
import org.springframework.data.repository.PagingAndSortingRepository;

// Vendelligence imports
import com.vendelligence.webapp.querymanager.model.Query;

// API Reference
// http://docs.spring.io/spring-data/jpa/docs/current/reference/html/
// http://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
// The JpaRepository is Extended which itself Extends Spring CrudRepository
// http://docs.spring.io/spring/docs/current/spring-framework-reference/html/dao.html
// Spring Guides Accessing data with JPA
// https://spring.io/guides/gs/accessing-data-jpa/

/* 
 * This interface purely allows for customization of the methods in Spring Standard.
 * Spring JPA documentation describes this design approach and the other service classes
 * and interfaces to configure if using this approach as it gives you full control.
 * In current development status, we have no need to customize this interface with
 * additional methods of our own as the standard ones are sufficient thus far. That will
 * probably change per Spring documentation guidelines.
 * 
 */
public interface QueryRepository extends JpaRepository<Query, Long> 
{  
	
}
