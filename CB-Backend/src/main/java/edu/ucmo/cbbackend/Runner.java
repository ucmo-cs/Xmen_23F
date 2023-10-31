package edu.ucmo.cbbackend;

import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.repository.RolesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
    RolesRepository rolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    Runner(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (rolesRepository.findByName("USER") == null) {
            Roles user = new Roles();
            user.setName("USER");
            rolesRepository.save(user);
        }

        if (rolesRepository.findByName("DEPARTMENT") == null) {
            Roles Department = new Roles();
            Department.setName("DEPARTMENT");
            rolesRepository.save(Department);
        }
        rolesRepository.findAll().forEach((role -> {
            logger.info("{}", role);
        }));


    }
}
