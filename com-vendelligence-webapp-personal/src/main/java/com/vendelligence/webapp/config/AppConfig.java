package com.vendelligence.webapp.config;

// java utility imports here
import java.util.Properties;

// spring framework imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

// vendelligence web app imports
import com.vendelligence.webapp.searchmanager.config.SearchConfig;
import com.vendelligence.webapp.searchmanager.config.VendorConfig;

@Configuration
@ComponentScan(basePackages = { "com.vendelligence.webapp" })
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AppConfig 
{
	// autowire environment variables. we will use this throughout the web app
	// http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() 
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

	// mail property values are controlled via spring profiles and application*.property sheets
    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() 
    {
    	// javamail properties should not change after application startup
        final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(env.getProperty("smtp.host"));
        mailSenderImpl.setPort(env.getProperty("smtp.port", Integer.class));
        mailSenderImpl.setProtocol(env.getProperty("smtp.protocol"));
        mailSenderImpl.setUsername(env.getProperty("smtp.username"));
        mailSenderImpl.setPassword(env.getProperty("smtp.password"));
        final Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        javaMailProps.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        mailSenderImpl.setJavaMailProperties(javaMailProps);
        return mailSenderImpl;
    }
    
    // search configuration chooses between personal and community edition api keys
    @Bean
    public SearchConfig searchConfig()
    {
    	// searchconfiguration object values should not change after application startup
    	final SearchConfig sc = new SearchConfig();
    	sc.setApiPaidKey(env.getProperty("search.paidapikey"));
    	sc.setApiFreeKey(env.getProperty("search.freeapikey"));
    	return sc;
    }
    
    // vendorconfiguration uses externalized configuration to create key-value hashmap
    @Bean VendorConfig vendorConfig()
    {
    	final VendorConfig vc = new VendorConfig();
    	return vc;
    }
    
    // resttemplate configuration - used for google custom search service interfaces
    @Bean RestTemplate restTemplate()
    {
    	RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    	return restTemplate;
    }
}