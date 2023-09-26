package edu.ucmo.cbbackend.service;

import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


public class UserDetailsService  implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    public   UserRepository userRepository;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        return new edu.ucmo.cbbackend.model.UserDetails(user);
    }

    public String passwordEncoder(String password){
        return passwordEncoder.encode(password);
    }

}
