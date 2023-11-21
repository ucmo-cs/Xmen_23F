package edu.ucmo.cbbackend;

import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
import edu.ucmo.cbbackend.model.*;
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
        if (rolesRepository.findByNameIgnoreCase("USER") == null) {
            Roles user = new Roles();
            user.setName("USER");
            rolesRepository.save(user);
        }

        if (rolesRepository.findByNameIgnoreCase("DEPARTMENT") == null) {
            Roles Department = new Roles();
            Department.setName("DEPARTMENT");
            rolesRepository.save(Department);
        }

        if (rolesRepository.findByNameIgnoreCase("APPLICATION") == null) {
            Roles Application = new Roles();
            Application.setName("APPLICATION");
            rolesRepository.save(Application);
        }


        if (rolesRepository.findByNameIgnoreCase("OPERATIONS") == null) {
            Roles Operations = new Roles();
            Operations.setName("OPERATIONS");
            rolesRepository.save(Operations);
        }

        rolesRepository.findAll().forEach((role -> {
            logger.info("{}", role);
        }));

        if (isDemo) {
            logger.info("Demo mode is enabled");
            Faker faker = new Faker();
            Superhero userInfo = faker.superhero();
            User user = new User();
            user.setRoles(rolesRepository.findByNameIgnoreCase("USER"));
            user.setUsername(userInfo.name().replace(" ", "_"));

            user.setPassword(userService.passwordEncoder("password"));
            userRepository.save(user);
            logger.info("User was created " + user.getUsername() + " with password " + "password" + " and role " + user.getRoles().getName());

            demoCreateChangeRequest(faker, user);

            User department = new User();
            userInfo = faker.superhero();
            department.setRoles(rolesRepository.findByNameIgnoreCase("DEPARTMENT"));
            department.setUsername(userInfo.name().replace(" ", "_"));
            department.setPassword(userService.passwordEncoder("password"));

            userRepository.save(department);
            logger.info("User was created " + department.getUsername() + " with password " + "password" + " and role " + department.getRoles().getName());


            demoCreateChangeRequest(faker, department);

            User application = new User();
            userInfo = faker.superhero();
            application.setRoles(rolesRepository.findByNameIgnoreCase("APPLICATION"));
            application.setUsername(userInfo.name().replace(" ", "_"));
            application.setPassword(userService.passwordEncoder("password"));

            userRepository.save(application);
            logger.info("User was created " + application.getUsername() + " with password " + "password" + " and role " + application.getRoles().getName());

            demoCreateChangeRequest(faker, application);

            User operations = new User();
            userInfo = faker.superhero();
            operations.setRoles(rolesRepository.findByNameIgnoreCase("OPERATIONS"));
            operations.setUsername(userInfo.name().replace(" ", "_"));
            operations.setPassword(userService.passwordEncoder("password"));
            userRepository.save(operations);
            demoCreateChangeRequest(faker, operations);
            logger.info("User was created " + operations.getUsername() + " with password " + "password" + " and role " + operations.getRoles().getName());




        }


    }

    private void demoCreateChangeRequest(Faker faker, User user) {
        for (int i = 0; i < 20; i++) {
                ChangeType changeType;
                ChangeRequestState changeRequestState;
                ChangeRequestApproveOrDeny approveOrDeny;
                ChangeRequestRiskLevel changeRequestRiskLevel;
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

                random = (int) (Math.random() * 4);
                if (random == 0) {
                    changeRequestState = ChangeRequestState.APPLICATION;
                }
               else if(random == 1 ){
                    changeRequestState = ChangeRequestState.DEPARTMENT;
                }
                else if(random == 2 ){
                    changeRequestState = ChangeRequestState.FROZEN;
                }
                else {
                    changeRequestState = ChangeRequestState.COMPLETE;
                }

                random = (int) (Math.random() * 2);

                if(random == 0 ){
                    approveOrDeny = ChangeRequestApproveOrDeny.APPROVE;
                } else if (random == 1){
                    approveOrDeny = ChangeRequestApproveOrDeny.PENDING;
                } else {
                    approveOrDeny = ChangeRequestApproveOrDeny.DENY;
                }
            random = (int) (Math.random() * 2);


            if( random == 0){
                changeRequestRiskLevel = ChangeRequestRiskLevel.LOW;
            }
            else if (random == 1){
                changeRequestRiskLevel = ChangeRequestRiskLevel.MEDIUM;
            }
            else {
                changeRequestRiskLevel = ChangeRequestRiskLevel.HIGH;
            }

                ChangeRequest changeRequest = ChangeRequest.builder()
                        .applicationId(Long.valueOf(faker.number().digits(10)))
                        .changeType(changeType)
                        .dateCreated(faker.date().birthday())
                        .description(faker.lorem().sentence(20))
                        .dateUpdated(faker.date().birthday())
                        .author(user)
                        .reason(faker.lorem().sentence(20))
                        .Implementer(faker.name().fullName())
                        .timeToRevert(Long.valueOf(faker.number().digits(10)))
                        .timeWindowStart(faker.date().future(365, java.util.concurrent.TimeUnit.DAYS))
                        .timeWindowEnd(faker.date().future(700, 366 ,java.util.concurrent.TimeUnit.DAYS))
                        .approveOrDeny(approveOrDeny)
                        .state(changeRequestState)
                        .riskLevel(changeRequestRiskLevel)
                        .backoutPlan(faker.lorem().sentence(100))
                        .roles(user.getRoles())
                        .build();
                changeService.save(changeRequest);
            }
    }
}
