package edu.ucmo.cbbackend;

import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;

@Component
public class Runner implements CommandLineRunner     {
    RolesRepository rolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    Runner(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Roles user = new Roles();
        user.setName("USER");

        if (rolesRepository.findByName("USER") == null)
            rolesRepository.save(user);
        Roles Department = new Roles();
        Department.setName("DEPARTMENT");
        if (rolesRepository.findByName("DEPARTMENT") == null)
            rolesRepository.save(Department);

        rolesRepository.findAll().forEach((role -> {
            logger.info("{}", role);
        }) );


    }
}
