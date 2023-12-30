package com.springboot.blog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";


    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Blog REST APIs")
                        .description("Spring Boot Blog REST API Documentation")
                        .version("1")
                        .contact(getContact())
                        .license(getLicense())
                );
    }
    private Contact getContact(){
        Contact contact = new Contact();
        contact.setName("Blog api");
        contact.setEmail("Info@gmail,com");
        contact.setUrl("www.blog.com");
        return contact;
    }
    private License getLicense(){
        License license =new License();
        license.setName("License of API");
        license.setUrl("API license URL");
        return license;
    }

}
