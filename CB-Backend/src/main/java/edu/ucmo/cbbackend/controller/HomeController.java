package edu.ucmo.cbbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin

public class HomeController {

    @Operation(summary = "Just a test endpoint to make sure the user is authenticated")
    @ApiResponse(responseCode = "200", description = "Return a String of 'Hello World!'", content = @io.swagger.v3.oas.annotations.media.Content)
    @SecurityRequirement(name = "jwtAuth")
    @GetMapping("/api/v1/home")
    public String home() {
        return "Hello World!";
    }


}
