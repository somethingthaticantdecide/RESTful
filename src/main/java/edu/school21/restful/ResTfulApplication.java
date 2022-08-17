package edu.school21.restful;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "ResTful school 21 project Docs",
        version = "1.0",
        description = "This is the official documentation of the ResTful API developed by jmontagu",
        license = @License(name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"),
        contact = @Contact(email = "ghoshalshankhadeep@hotmail.com",
            name = "Shankhadeep Ghoshal"))
)
public class ResTfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResTfulApplication.class, args);
    }
}
