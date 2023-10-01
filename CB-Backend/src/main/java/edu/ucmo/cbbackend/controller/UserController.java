package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.UserRepository;
import edu.ucmo.cbbackend.service.UserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@CrossOrigin
public class UserController {

 private final UserRepository userRepository;
 private final UserDetailsService userDetailsService;

    public UserController(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/api/v1/user/{id}")
    public User getUserById(@PathVariable Long id){
        System.out.println("id = " + id);
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/v1/user")
    public ResponseEntity<?> createUser( @RequestBody User  user){
        if (userRepository.findByUsername(user.getUsername()) != null){
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        user.setPassword(userDetailsService.passwordEncoder(user.getPassword()));


        try {
            userRepository.save(user);
            user.setPassword(null);
            return ResponseEntity.ok().body(user);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }

    }

    // Genreate json to create user
    // {
    //     "username": "admin",
    //     "password": "admin"
    // }

    @DeleteMapping("/api/v1/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        if (userRepository.findById(id).orElse(null) == null){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping( "/api/v1/user/{id}")
    public User updateUserById(@PathVariable Long id, @RequestBody User user){
        User userToUpdate = userRepository.findById(id).orElse(null);
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(userDetailsService.passwordEncoder(user.getPassword()));
        return userRepository.save(userToUpdate);
    }

}
