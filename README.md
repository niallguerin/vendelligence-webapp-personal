# Vendelligence
Vendelligence personal information retrieval, filtering, and note-taking web application.

## Description
Vendelligence was an information retrieval and filtering tool for software vendor products and open source projects. It integrated with the Google Custom Search API, now the Programmable Search API.

It was developed to support my own technical information needs when working on IT projects in consulting and researching topics on the fly as a developer. I tended to return back to the same trusted sources frequently and used common patterns for querying and note-taking.

It provided curated vendor and open source project resources, information retrieval using the Google CSE API, an internal web clipper UI for snipping the results in the view templates, and basic list and CRUD operations for stored queries and query metadata like your notes.

## Why I developed it
I wanted to create an adaptive information support system to help me in my systems integration consulting and later software developer roles. In my work with clients in the past and in my own development activity and self-learning, I encountered a mixture of vendor products and open source solutions, and often needed to ramp up on new technologies quickly or mentor team colleagues fast. I used text files and bookmarks, but they were isolated information resources and software product or open source solution data could become dated or deprecated depending on the vendor release strategy.

I also didn't want to have to rely on ad-hoc questions to colleagues; I wanted the information in a system that could be quickly indexed and added to as needed, and that everyone could use and share manually or with other machine clients.

The refinement filters were based on my own common query patterns when researching topics in vendor products or open source projects and common indexing methods used by vendors for their publicly accessible software documentation and help libraries.

## PROJECT STATUS LOG
The Vendelligence web application was completed and finished back in December 2016.

The project was reviewed and revisited in my MSc in Computer Science - Data Analytics in 2019 as I found I really needed this type of information retrieval and knowledge recommender tool more than ever and had been using the first version for personal projects and in college, but gaps were apparent to me; lack of a native IR module - it depended on the Google CSE paid-for API license and lack of automated best answer prediction support from querying third-party data.

My thesis project targeted the following items in relation to a list of Research Questions:
- removal of the dependency from cost perspective on the Google CSE API license which requires paying a third-party provider and build a standalone information retrieval module using an open source solution or completely custom solution. Gensim was ultimately chosen for the prototype
- applying machine learning to the recommender system task - i.e. best answer prediction - using methods from NLP and literature review of state-of-the-art at the time. Newer large language models have been released over the past three years, so I am assessing their capability in new standalone ML prototypes

The thesis project was written in Python and used scikit-learn, Keras with TensorFlow, and MySQL for staging the dataset. It will be uploaded separately and linked here for reference.

## ARCHITECTURE
The tool uses Javascript, jQuery, Bootstrap, Thymeleaf, Spring Boot, Spring Framework, and MariaDB.

This version was tested with Spring Boot 1.3.8, Java 1.8, and MariaDB 10.1.14 and with Apache Tomcat. It was deployed locally and on Amazon Web Services Elastic Beanstalk instances in the past.

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
