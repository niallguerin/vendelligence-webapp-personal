package com.vendelligence.webapp.config;

// java utility imports
import java.util.Locale;

// spring framework imports
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.context.MessageSource;

@Configuration
// http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-auto-configuration
public class MvcConfig extends WebMvcConfigurerAdapter 
{

    public MvcConfig() 
    {
        super();
    }

/* 
 * The AddViewController allows automatic http get request mapping without needing a controller. 
 * Do not add entries here if you already have entries in controllers. 
 * Do not forget to include entries here for http(s) redirects to views that have no explicit controller.
 * 
*/    
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) 
    {
        super.addViewControllers(registry);
        
        // set viewcontroller for static pages
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/privacy").setViewName("privacy");
          
    }
    
    // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/InterceptorRegistry.html
    @Override
    public void addInterceptors(final InterceptorRegistry registry) 
    {
    	// http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/i18n/LocaleChangeInterceptor.html
    	// handles locale on every request
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    // Bean configuration for localization, user interface labels, error validation messages, username and password validation
    @Bean
    // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/LocaleResolver.html
    public LocaleResolver localeResolver() 
    {
    	// http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/i18n/CookieLocaleResolver.html
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return cookieLocaleResolver;
    }

    @Bean
    // this bean handles message labels and properties via messages*.properties
    // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/messagesource.html
    public MessageSource messageSource() 
    {
    	// http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }
    
}