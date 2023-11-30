package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.dto.request.ChangeRequestBodyDTO;
import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponseDTO;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.ChangeRequestApproveOrDeny;
import edu.ucmo.cbbackend.model.ChangeRequestState;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.service.ChangeService;
import edu.ucmo.cbbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;

@RestController
@CrossOrigin
public class ChangeController {

    private final UserService userService;
    private final ChangeService changeService;
    private final RolesRepository rolesRepository;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ChangeController.class);
    public ChangeController(ChangeService changeService, UserService userService,
                            RolesRepository rolesRepository) {
        this.changeService = changeService;
        this.userService = userService;

        this.rolesRepository = rolesRepository;
    }

    @SecurityRequirement(name = "jwtAuth")
    @PatchMapping("/api/v1/change/{id}/approved")
    public ResponseEntity<?> updateChangeRequestRole(@PathVariable Long id, HttpServletRequest request){
        
        
        ChangeRequest changeRequest = changeService.findById(id);


        logger.info("Change Request Role Updated:" + changeRequest.getRoles());
        changeRequest.setRoles( changeService.determineChangeRequestNextRole( userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles() ));
        logger.info("Change Request Role Updated:" + changeRequest.getRoles());
        changeService.save(changeRequest);
        ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(changeRequest, false);
        return ResponseEntity.ok().body(changeRequestHttpResponse);



    }


    @SecurityRequirement(name = "jwtAuth")
    @PatchMapping("/api/v1/change/{id}/denied")
    public ResponseEntity<?> updateChangeRequestRoleDeny(@PathVariable Long id,   HttpServletRequest request) {
        ChangeRequest changeRequest = changeService.findById(id);
        changeRequest.setApproveOrDeny(ChangeRequestApproveOrDeny.DENIED);
        changeRequest.setState(ChangeRequestState.FROZEN);
        changeService.save(changeRequest);
        ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(changeRequest, false);
        return ResponseEntity.ok().body(changeRequestHttpResponse);

    }


    @SecurityRequirement(name = "jwtAuth")
    @PutMapping("/api/v1/change/{id}")
    public ResponseEntity<?> updateChangeById(@PathVariable Long id, @RequestBody ChangeRequestBodyDTO changeRequestBody) {
        try {
            ChangeRequest changeRequest = changeService.findById(id);
            ChangeRequest x = changeService.toEnity(changeRequestBody);
            x.setDateCreated(changeRequest.getDateCreated());

             changeRequest.setDateUpdated(new Date());
            changeService.save(changeRequest);
            ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(changeRequest, false);
            return ResponseEntity.ok().body(changeRequestHttpResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }


    @SecurityRequirement(name = "jwtAuth")
    @DeleteMapping("/api/v1/change/{id}")
    public ResponseEntity<?> deleteChangeById(@PathVariable Long id) {
        try {
            if (changeService.findById(id) == null)
                return ResponseEntity.badRequest().body("Change Request does not exist");

            changeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @SecurityRequirement(name = "jwtAuth")
    @GetMapping("/api/v1/change/{id}")
    public ResponseEntity<?> getChangeById(@PathVariable Long id) {
        try {
            ChangeRequest changeRequest = changeService.findById(id);
            if (changeRequest == null)
                return ResponseEntity.badRequest().body("Change Request does not exist");
            ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(changeRequest, false);
            return ResponseEntity.ok().body(changeRequestHttpResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @SecurityRequirement(name = "jwtAuth")
    @GetMapping("/api/v1/change")
    public ResponseEntity<?> getChange(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "5") Integer size, @RequestParam(required = false, defaultValue = "false") Boolean showAuthorUsername, @RequestParam(required = false, defaultValue = "Application") String state) {
        ChangeRequestState changeRequestState = null;
        if(state != null){
            changeRequestState = changeService.toState(state);
        }
        if (userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles().equals(rolesRepository.findByNameIgnoreCase("USER"))) {
            Page<ChangeRequestHttpResponseDTO> list = changeService.findAllByUserIdAndSortByDate(page, size, request.getUserPrincipal().getName(), showAuthorUsername, changeRequestState);
            return ResponseEntity.ok().body(list);
        }
        if( userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles().equals(rolesRepository.findByNameIgnoreCase("OPERATIONS"))){
            Page<ChangeRequestHttpResponseDTO> list = changeService.findAllByState(page, size, showAuthorUsername, changeRequestState);
            return ResponseEntity.ok().body(list);
        }


        Page<ChangeRequestHttpResponseDTO> list = changeService.findAllByRolesAndState(page, size, showAuthorUsername,  userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles(), changeRequestState);
        return ResponseEntity.ok().body(list);
    }


    @SecurityRequirement(name = "jwtAuth")
    @PostMapping("/api/v1/change")
    public ResponseEntity<?> change(@RequestBody ChangeRequestBodyDTO change, HttpServletRequest request) {
        try {
            change.setApproveOrDeny(ChangeRequestApproveOrDeny.PENDING);
            change.setState(ChangeRequestState.APPLICATION);
            change.setDateCreated(new Date());
            if (change.getAuthorId() == null) {
                change.setRoles(userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles().getName());
                change.setAuthorId(userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getId());
            }
            else {
                try {
                    User user = userService.loadUserById(change.getAuthorId());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("User does not exist");
                }
                User user = userService.loadUserById(change.getAuthorId());
                change.setRoles(user.getRoles().getName());
            }
            try {
                changeService.toChangeType(change.getChangeType());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Change Type does not exist");
            }

            ChangeRequest convertedChangeRequest = changeService.toEnity(change);
            changeService.save(convertedChangeRequest);
            ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(convertedChangeRequest, false);
            return ResponseEntity.created(URI.create("/api/v1/change/" + changeRequestHttpResponse.getId())).body(changeRequestHttpResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }

    }
}
