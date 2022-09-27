package com.vendelligence.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Controller;

/* 
 * main vendelligence web application class.
 * it resides in the root com.vendelligence.webapp package and is spring boot specific.
 * the current configuration works for embedded and external container mode.
 * in local dev we run as .jar and test on external tomcat container as .war
 * in remote aws quality server we test as packaged .war
 * plan to support docker build as it lets us keep spring boot .jar mode, my dev setup default
 * 
 */

@EntityScan(basePackageClasses = { WebApplication.class, Jsr310JpaConverters.class } )
/* adding below exclusion per stack overflow thread for issues between Spring Boot
 * latest release and Thymleaf 3.0 integration 
 * http://stackoverflow.com/questions/33536034/spring-boot-with-thymeleaf-3-beta
 * hold off on thymeleaf3 upgrade for vendelligence until spring boot team finalize this thread
 * https://github.com/spring-projects/spring-boot/issues/4393
 * (exclude={org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class}
 */
@SpringBootApplication
public class WebApplication extends SpringBootServletInitializer
{
 @Override
 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
 {
     return application.sources(WebApplication.class);
 }
 public static void main(String[] args) 
 {
     SpringApplication.run(WebApplication.class, args);
 }
}