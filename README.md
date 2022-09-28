# Vendelligence
Vendelligence personal information retrieval, filtering, and note-taking web application.

## Description
Vendelligence was an information retrieval and filtering tool for software vendor products and open source projects.

It was developed to support my own information needs when working on IT projects in consulting and researching topics on the fly as a developer. This version does not contain a user management engine, but a private historic repository does contain a UME for user registration and multi-user support, but that project has been archived.

I wanted an information system to better support my daily research, information storage, and retrieval tasks and a system that could support other IT consultants, technical leads and software developers. That way, a user could always see the latest information around best practices, errors and solutions, new features, deprecated functionality, and security vulnerabilities and other types of important technical information for a project. The information had to come from trusted official sources and reliable domain experts whether they were bloggers, authors, or vendor experts in a given vendor support forum for question answering.

It provided curated vendor and open source project resources, information retrieval using the Google CSE API, an internal web clipper UI for snipping the results in the view templates, and basic list and CRUD operations for stored queries and query metadata like your notes.

The search result refinement filters were based on my own common query patterns when researching topics in vendor products or open source projects and common indexing methods used by vendors for their publicly accessible software documentation - API version links, Product function documentation, Forums.

## PROJECT STATUS LOG
The Vendelligence web application was completed and finished back in December 2016 and has been used intermittently for personal use since that time. Project development is fully closed since 2018 when minor fixes were implemented for a jQuery front-end search bug and the web clipper UI tool.

The vendelligence project was reviewed and revisited in my MSc in Computer Science - Data Analytics thesis project in 2019. The thesis project is on GitHub as a separate repository. It involved a rewrite of the system in Python based on gap analysis of original tool and research questions based on literature review of information retrieval systems, question answering systems, recommender systems, and machine learning algorithm performance over technical forum Q&A datasets.

## ARCHITECTURE
The tool uses Javascript, jQuery, Bootstrap, Thymeleaf, Spring Boot, Spring Framework, and MariaDB.

This version was tested with Spring Boot 1.3.8, Java 1.8, and MariaDB 10.1.14 and with Apache Tomcat. 

It can be run locally on your laptop with a local MariaDB or MySQL database instance.

This version was also deployed on Amazon Web Services Elastic Beanstalk back in 2016 for user requirements analysis and feedback.

### MAVEN BUILD
```
mvn clean install spring-boot:run
```

### DATABASE SETUP 
```
mysql -u root -p 
> CREATE USER 'vend-admin'@'localhost' IDENTIFIED BY 'password';
> GRANT ALL PRIVILEGES ON *.* TO 'vend-admin'@'localhost';
> FLUSH PRIVILEGES;
```

Try and use a password manager for even local developer MariaDB or MySQL DB instances. The workbench will likely block simple passwords in latest versions if you are using MySQL workbench. You can still run above SQL directly on the DB, if you wish.

### GOOGLE DEVELOPER APIKEY AND CSE SETUP
Go to https://code.google.com/apis/console and select API Access
In Create Credentials console, generate an apikey. Copy the string into the application-dev.properties files.

Once you have your own google apikey, you can still hit my search engine ids, but it will max out at 100 queries in a 24 hour period.

This is a freeapi Google Programmable Search Engine (formerly CSE) build, so it is recommended to generate your own Google Account and API developer key.

### MAIL SETTINGS
You need to update the application-dev.properties with your own email service provider settings if you wish to use it as a tool for an internal team. I have tested with Amazon SES and Google Gmail. Use whatever mail server account works for you.

### REFERENCE LINKS for SPRING PROJECTS
- [Spring Boot Project Reference](https://projects.spring.io/spring-boot/)
- [Spring Boot Guide](https://spring.io/guides/gs/spring-boot/)
- [Spring Boot 1.3.8 Reference Guide](http://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/htmlsingle/)
- [Spring Boot CLI and AngularJS Guide](https://spring.io/guides/gs/spring-boot-cli-and-js/)
- [Spring Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [Spring Data JPA Project](http://projects.spring.io/spring-data-jpa/)
- [Baeldung Hibernate and Spring Tutorials](http://www.baeldung.com/hibernate-4-spring)
- [jQuery password strength meter for Twitter Bootstrap](https://github.com/ablanco/jquery.pwstrength.bootstrap) 
- [Spring Security Login and Registration Project](https://github.com/Baeldung/spring-security-registration)
- [Baeldung Spring Login and Registration Series Tutorial](http://www.baeldung.com/registration-with-spring-mvc-and-spring-security)
- [Spring Security Guide Project Sample](https://spring.io/guides/gs/securing-web/)
- [Spring Security Reference Guide](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/)
- [Testing, Spring JPA topics](https://www.petrikainulainen.net/tutorials/)

### REFERENCE LINKS for GOOGLE CUSTOM SEARCH API
- [Google Custom Search Overview](https://developers.google.com/custom-search/docs/tutorial/introduction)
- [Google CSE JSON API](https://developers.google.com/custom-search/json-api/v1/overview)
- [Google CSE Libraries and Samples](https://developers.google.com/custom-search/json-api/v1/libraries)
