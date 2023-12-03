package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.dto.request.UserEditRequest;
import edu.ucmo.cbbackend.dto.request.UserRegisterRequest;
import edu.ucmo.cbbackend.dto.response.UserResponse;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.UserRepository;
import edu.ucmo.cbbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Operation(summary = "Get a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a user object", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Return a String of 'User does not exist'", content = @Content),

    })
    @GetMapping("/api/v1/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new Exception("User does not exist")); //TODO: consider add error handling
            return ResponseEntity.ok().body(new UserResponse(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @Operation(summary = "Create a user and return the username and id but leave out the password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a user object", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserRegisterRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Return a String of 'Username is already taken'", content = @Content),
            @ApiResponse(responseCode = "400", description = "Return a String of 'User id must be null'", content = @Content),
    })
    @PostMapping("/api/v1/user")
    public ResponseEntity<?> createUser(@RequestBody UserRegisterRequest user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (user.getId() != null)
            return ResponseEntity.badRequest().body("User id must be null"); // TODO: could add model or class that does not define id}

        try {
         User  userEnity  =   userService.toEntity(user);
        userService.save(userEnity);
            return ResponseEntity.ok().body(new UserResponse(userEnity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }

    }

    // Genreate json to create user
    // {
    //     "username": "admin",
    //     "password": "admin"
    // }

    @Operation(summary = "Delete a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return no content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Return a String of 'User does not exist'", content = @Content),
    })
    @DeleteMapping("/api/v1/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        if (userRepository.findById(id).orElse(null) == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a user object", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Return a String of 'User does not exist'", content = @Content),
    })
    @PutMapping("/api/v1/user/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody UserEditRequest user) {
        User userToUpdate = userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            return ResponseEntity.badRequest().body("User does not exist"); //Todo could verify that this works
        }

        if (user.getUsername() != null)
            userToUpdate.setUsername(user.getUsername());

        if (user.getPassword() != null)
            userToUpdate.setPassword(userService.passwordEncoder(user.getPassword()));

        if (user.getRoles() != null)
            userToUpdate.setRoles(user.getRoles());

        userService.save(userToUpdate);

        return ResponseEntity.ok().body(new UserResponse(userToUpdate));
    }

    @SecurityRequirement(name = "jwtAuth")
    @GetMapping("/api/v1/user/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User userData  =  userService.loadUserByUsername(principal.getName());
             return ResponseEntity.ok().body(new UserResponse(userData));
    }
}
