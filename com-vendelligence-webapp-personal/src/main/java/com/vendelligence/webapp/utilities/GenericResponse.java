package com.vendelligence.webapp.utilities;

// Keep this class as it gives us scope to re-use it for our own custom messages*.properties configuration

/* 
 * Blog Tutorial Reference:
 * http://www.baeldung.com/spring-security-registration-verification-email -> section 3 - Exception Handler
 * Kept in for now purely to avoid general code errors in the PoC. 
 * This uses property sheet values to avoid putting general message text for users directly in source code.
 *
 */
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericResponse 
{
    private String message;
    private String error;

    public GenericResponse(final String message) 
    {
        super();
        this.message = message;
    }

    public GenericResponse(final String message, final String error) 
    {
        super();
        this.message = message;
        this.error = error;
    }

    public GenericResponse(final List<FieldError> fieldErrors, final List<ObjectError> globalErrors) 
    {
        super();
        final ObjectMapper mapper = new ObjectMapper();
        try 
        {
            this.message = mapper.writeValueAsString(fieldErrors);
            this.error = mapper.writeValueAsString(globalErrors);
        } 
        catch (final JsonProcessingException e) 
        {
            this.message = "";
            this.error = "";
        }
    }

    public String getMessage() 
    {
        return message;
    }

    public void setMessage(final String message) 
    {
        this.message = message;
    }

    public String getError() 
    {
        return error;
    }

    public void setError(final String error) 
    {
        this.error = error;
    }

}
