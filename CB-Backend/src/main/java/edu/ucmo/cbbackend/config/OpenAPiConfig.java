package edu.ucmo.cbbackend.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "jwtAuth",
        scheme = "bearer",
        bearerFormat = "JWT")

public class OpenAPiConfig {


    @Bean
    public OpenAPI openAPi() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Your development server");

        Info info = new Info()
                .title("CB Backend")
                .version("0.0.1")
                .description("Commerce Bank Change System Backend API");

        return new OpenAPI().info(info).addServersItem(devServer);
    }


}
