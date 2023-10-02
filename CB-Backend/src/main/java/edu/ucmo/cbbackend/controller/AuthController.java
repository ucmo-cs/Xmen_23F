package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.model.UserDetails;
import edu.ucmo.cbbackend.service.TokenService;
import edu.ucmo.cbbackend.service.UserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class AuthController {

    private final TokenService tokenService;
    private  final UserDetailsService userDetailsService;


    public AuthController(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a JWT", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "401", description = "Return a String of 'Incorrect Credentials'", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "500", description = "Return a String of 'Internal Server Error'", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @Operation(summary = "Login endpoint")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{
        UserDetails dbUser = (UserDetails) userDetailsService.loadUserByUsername(user.getUsername());
        if (dbUser == null) {
            return ResponseEntity.badRequest().body("Incorrect Credentials");
        }
        if (!userDetailsService.passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect Credentials");
        }
            String token = tokenService.generateToken(dbUser);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            return ResponseEntity.ok(token);
    }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

}
