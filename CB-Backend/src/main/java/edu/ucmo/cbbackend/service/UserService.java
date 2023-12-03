package edu.ucmo.cbbackend.service;

import edu.ucmo.cbbackend.dto.request.UserRegisterRequest;
import edu.ucmo.cbbackend.dto.response.UserResponse;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserService implements UserDetailsService {

    @Autowired
    public   UserRepository userRepository;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RolesRepository rolesRepository;

    public UserDetails save( User user){
        if(userRepository.findByUsername(user.getUsername()) != null) throw new RuntimeException("User already exists");

        if(user.getRoles() == null){
            throw new RuntimeException("User must have one role");
        }

        if(rolesRepository.findByNameIgnoreCase(user.getRoles().getName().toUpperCase()) == null){
            throw new RuntimeException("Role does not exist");
        }

        user.setRoles(rolesRepository.findByNameIgnoreCase(user.getRoles().getName().toUpperCase()));
        user.setPassword(passwordEncoder(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public String passwordEncoder(String password){
        return passwordEncoder.encode(password);
    }

    @Override
    public User loadUserByUsername(String username)  {
        User user = userRepository.findByUsername(username);
        return user;
    }

    public User loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    public User toEntity(UserRegisterRequest userRegisterRequest){
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername());
        user.setPassword(passwordEncoder(userRegisterRequest.getPassword()));
        user.setRoles(rolesRepository.findByNameIgnoreCase(userRegisterRequest.getRoles().toUpperCase()));
        return user;
    }

    public UserResponse toDto(User user){
        return new UserResponse(user);
    }


}
