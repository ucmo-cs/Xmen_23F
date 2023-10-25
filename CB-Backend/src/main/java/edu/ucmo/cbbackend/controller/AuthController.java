package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.dto.request.LoginRequest;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.service.TokenService;
import edu.ucmo.cbbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class AuthController {

    private final TokenService tokenService;
    private  final UserService userService;


    public AuthController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a JWT", content = @Content),
            @ApiResponse(responseCode = "401", description = "Return a String of 'Unauthorized'", content = @Content),
            @ApiResponse(responseCode = "500", description = "Return a String of 'Internal Server Error'", content = @Content)
    })
    @Operation(summary = "Login endpoint")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try{
            User dbUser = userService.loadUserByUsername(loginRequest.getUsername());
            if ((dbUser == null) || !userService.passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            String token = tokenService.generateToken(dbUser);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

}
