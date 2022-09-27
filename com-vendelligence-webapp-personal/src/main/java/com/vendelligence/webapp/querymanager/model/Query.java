package com.vendelligence.webapp.querymanager.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/* 
 * The Query class is the primary data object for the Vendelligence Query Manager.
 * It captures the Query id, Query Vendor Filter, Query String, Query Refinement, custom Query Name, 
 * custom tags user assigns to it, and allows notes so the user can annotate their saved 
 * queries.
 * 
 * API Reference
 * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 * 
 */
@Entity
@Table(name="query")
public class Query
{
	// Explicitly configured the columnDefinition as our model drives the physical design using these annotations.
	// Hibernate will not adjust the physical database and defaults for jdbc are to convert string to varchar.
	// Web References:
	// http://stackoverflow.com/questions/1281188/text-field-using-hibernate-annotation
	// https://mariadb.com/kb/en/mariadb/alter-table/
	// https://vladmihalcea.com/2014/06/10/a-beginners-guide-to-hibernate-types/
	// http://docs.oracle.com/javaee/7/api/javax/persistence/Column.html
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="query_id")
	private Long id;
    
    @Column(name="query_vendor_filter", columnDefinition="TEXT")
    private String queryVendorFilter;
	
    @Column(name="query_string", columnDefinition="TEXT")
    private String queryString;
    
    @Column(name="query_refinement", columnDefinition="TEXT")
    private String queryRefinement;
    
    @Column(name="query_name", columnDefinition="TEXT")
    private String queryName;
	
    /*    
     *  This field is not finalized. I personally do not need it. Users do not need it. But online
     *  research shows many users like the option based on own custom tagging workflows. Gives 
     *  option of a tag cloud if a user wanted it. Keep it for now in initial build.
     *  
     */
	@Column(name="query_tags", length = 255)
	private String queryTags;
	
	@Column(name="query_notes", columnDefinition="TEXT")
	private String queryNotes;
	
	/*
	 * The following two columns handle query creation date and change date and timestamps.
	 * This is necessary for future application audit trails by humans or client programs.
	 * 
	 */
	@Column(name="creation_date_time")
	private LocalDateTime creationDateTime;

	@Column(name="change_date_time")
	private LocalDateTime changeDateTime;
	
	// constructor
	
	// basic setters and getters
	public Long getId()
	{
		return id;
	}
	
	public void setId(final Long id)
	{
		this.id = id;
	}
	
	public String getQueryVendorFilter()
	{
		return queryVendorFilter;
	}
	
	public void setQueryVendorFilter(final String qVendorFilter)
	{
		this.queryVendorFilter = qVendorFilter;
	}
	
	public String getQueryString()
	{
		return queryString;
	}
	
	public void setQueryString(final String qstring)
	{
		this.queryString = qstring;
	}
	
	public String getQueryRefinement()
	{
		return queryRefinement;
	}
	
	public void setQueryRefinement(final String qrefinement)
	{
		this.queryRefinement = qrefinement;
	}
	
	public String getQueryName()
	{
		return queryName;
	}
	
	public void setQueryName(final String qname)
	{
		this.queryName = qname;
	}
	
	public String getQueryTags()
	{
		return queryTags;
	}
	
	public void setQueryTags(final String qtags)
	{
		this.queryTags = qtags;
	}
	
	public String getQueryNotes()
	{
		return queryNotes;
	}
	
	public void setQueryNotes(final String qnotes)
	{
		this.queryNotes = qnotes;
	}
	
	public LocalDateTime getCreationDateTime()
	{
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime)
	{
		this.creationDateTime = creationDateTime;
	}

	public LocalDateTime getChangeDateTime()
	{
		return changeDateTime;
	}

	public void setChangeDateTime(LocalDateTime changeDateTime)
	{
		this.changeDateTime = changeDateTime;
	}
	
    @Override
    public String toString() 
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("QueryVendorFilter[queryVendorFilter=").append(queryVendorFilter).append("]").append("QueryString[queryString=").append(queryString).append("]").append("QueryRefinement[queryRefinement=").append(queryRefinement).append("]").append("QueryName[queryName=").append(queryName).append("]").append("[queryTags=").append(queryTags).append("]").append("[queryNotes").append(queryNotes).append("]").append("[creationDateTime").append(creationDateTime).append("]").append("[changeDateTime").append(changeDateTime).append("]");
        return builder.toString();
    }
}