package com.autotov.app;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class SwaggerConfig {

    private String swaggerVersion="2.2.6";

    @Value("${swagger.url:default}")
    private String swaggerUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        OpenAPI openAPI = new OpenAPI();
        HashMap<String,SecurityScheme> hashmap = new HashMap();

        //hashmap.put("Token", new SecurityScheme().name("Token").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER));
        //hashmap.put("Basic", new SecurityScheme().name("Basic").type(SecurityScheme.Type.HTTP).scheme("basic").in(SecurityScheme.In.HEADER));

        openAPI.info(new Info().title("AutoTov").version(swaggerVersion)
                .description("My Auto.")
                .contact(new Contact().name("AutoTov - Documentation").url("https://abc/aa.html"))
                .license(new License().name("AutoTov").url("http://aaa.com")))
                .components(new Components().securitySchemes(hashmap));

        if (!(swaggerUrl.equals("default"))){
             openAPI.addServersItem(new Server().url(swaggerUrl));
        }

        return openAPI;
    }
}
