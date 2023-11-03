package edu.ucmo.cbbackend;

import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.ChangeType;
import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.repository.UserRepository;
import edu.ucmo.cbbackend.service.ChangeService;
import edu.ucmo.cbbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
    private final RolesRepository rolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Value("${isDemo}")
    private boolean isDemo;
    private final UserRepository userRepository;
    private final ChangeService changeService;
    private final UserService userService;

    Runner(RolesRepository rolesRepository,
           UserRepository userRepository, ChangeService changeService, UserService userService) {
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
        this.changeService = changeService;
        this.userService = userService;
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

        if (isDemo) {
            logger.info("Demo mode is enabled");
            Faker faker = new Faker();
            Superhero userInfo = faker.superhero();
            User user = new User();
            user.setRoles(rolesRepository.findByName("USER"));
            user.setUsername(userInfo.name().replace(" ", "_"));

            user.setPassword(userService.passwordEncoder("password"));
            userRepository.save(user);
            logger.info("User was created " + user.getUsername() + " with password " + "password" + " and role " + user.getRoles().getName());

            for (int i = 0; i < 20; i++) {
                ChangeType changeType;
                // genreater random number from 0 to 2
                int random = (int) (Math.random() * 3);
                if (random == 0) {
                    changeType = ChangeType.PLANNED;
                }
                if (random == 1) {
                    changeType = ChangeType.EMERGENCY;
                } else {
                    changeType = ChangeType.UNPLANNED;
                }

                ChangeRequest changeRequest = ChangeRequest.builder()
                        .applicationId(Long.valueOf(faker.number().digits(10)))
                        .changeType(changeType)
                        .dateCreated(faker.date().birthday())
                        .description(faker.lorem().sentence(20))
                        .author(user)
                        .reason(faker.lorem().sentence(20))
                        .build();
                changeService.save(changeRequest);
            }

            User department = new User();
            userInfo = faker.superhero();
            department.setRoles(rolesRepository.findByName("DEPARTMENT"));
            department.setUsername(userInfo.name().replace(" ", "_"));
            department.setPassword(userService.passwordEncoder("password"));

            userRepository.save(department);


            for (int i = 0; i < 20; i++) {
                ChangeType changeType;
                // genreater random number from 0 to 2
                int random = (int) (Math.random() * 3);
                if (random == 0) {
                    changeType = ChangeType.PLANNED;
                }
                if (random == 1) {
                    changeType = ChangeType.EMERGENCY;
                } else {
                    changeType = ChangeType.UNPLANNED;
                }
                ChangeRequest changeRequest = ChangeRequest.builder()
                        .applicationId(Long.valueOf(faker.number().digits(10)))
                        .changeType(changeType)
                        .dateCreated(faker.date().birthday())
                        .description(faker.lorem().sentence(20))
                        .reason(faker.lorem().sentence(20))
                        .author(user)
                        .build();
                changeService.save(changeRequest);
            }
            logger.info("Department was created " + department.getUsername() + " with password " + "password" + " and role " + department.getRoles().getName());
        }

    }
}
