package com.vendelligence.webapp.config;

// java imports
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// spring framework imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//import com.vendelligence.webapp.querymanager.service.IQueryService;

/*
 * The following core resources were used as a research reference for jpa configuration:
 * http://www.baeldung.com/spring-security-registration
 * 
 * Rewrote this code as part of a design simplification process to focus on core functions
 * as a single user. No need for user registration, roles, and privileges at this point
 * in time. Other labs can set that stuff up; purpose here now is to bootstrap the
 * primary functionalithy in vendelligence of research support, filtering, and internal
 * querying of stored data, not focus on security framework distractions.
 * 
 */

@Component
public class DatabaseLoadDataBootstrap implements ApplicationListener<ContextRefreshedEvent> 
{
	// set this to true on initial deploy and set to false if doing update deploys
    private boolean alreadySetup = true;
    
//allow autowiring of the QueryService interface to simulate query data loads   
//@Autowired
//private IQueryService iQueryService;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) 
    {
        if (alreadySetup) 
        {
            return;
        }

        alreadySetup = true;
    }

}