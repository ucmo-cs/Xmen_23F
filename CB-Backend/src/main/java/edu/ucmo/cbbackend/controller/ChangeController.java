package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.dto.request.ChangeRequestBodyDTO;
import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponseDTO;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.ChangeRequestApproveOrDeny;
import edu.ucmo.cbbackend.model.ChangeRequestState;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.service.ChangeService;
import edu.ucmo.cbbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
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

    public ChangeController(ChangeService changeService, UserService userService,
                            RolesRepository rolesRepository) {
        this.changeService = changeService;
        this.userService = userService;

        this.rolesRepository = rolesRepository;
    }

    @SecurityRequirement(name = "jwtAuth")
    @PatchMapping("/api/v1/change/{id}")
    public ResponseEntity<?> updateChangeRequestRole(@PathVariable Long id,  @RequestParam(defaultValue = "Approve") String ApproveOrDeny,  HttpServletRequest request){
        
        
        ChangeRequest changeRequest = changeService.findById(id);

        if (ApproveOrDeny.equalsIgnoreCase("Approve")){

        logger.info("Change Request Role Updated:" + changeRequest.getRoles());
        changeRequest.setRoles( changeService.determineChangeRequestNextRole( userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles() ));
        logger.info("Change Request Role Updated:" + changeRequest.getRoles());
        changeService.save(changeRequest);
        ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(changeRequest, false);
        return ResponseEntity.ok().body(changeRequestHttpResponse);
        }
        else if (ApproveOrDeny.equalsIgnoreCase("Deny")){
            changeRequest.setRoles( changeService.determineChangeRequestNextRole( userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles() ));
            changeRequest.setApproveOrDeny(ChangeRequestApproveOrDeny.DENY);
            changeService.toState("FROZEN");
            changeService.save(changeRequest);
            ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(changeRequest, false);
            return ResponseEntity.ok().body(changeRequestHttpResponse);
        }
        else{
            return ResponseEntity.badRequest().body("Invalid Approve or Deny");
        }


    }


    @SecurityRequirement(name = "jwtAuth")
    @PutMapping("/api/v1/change/{id}")
    public ResponseEntity<?> updateChangeById(@PathVariable Long id, @RequestBody ChangeRequestBodyDTO changeRequestBody) {
        try {
            ChangeRequest changeRequest = changeService.findById(id);
            if (changeRequest == null)
                return ResponseEntity.badRequest().body("Change Request does not exist");
            if (changeRequestBody.getApplicationId() != null)
                changeRequest.setApplicationId(changeRequestBody.getApplicationId());
            if (changeRequestBody.getChangeType() != null)
                changeRequest.setChangeType(changeRequestBody.getChangeType());
            if (changeRequestBody.getDescription() != null)
                changeRequest.setDescription(changeRequestBody.getDescription());
            if (changeRequestBody.getReason() != null)
                changeRequest.setReason(changeRequestBody.getReason());
            if (changeRequestBody.getTimeWindowStart() != null) {
                changeRequest.setTimeWindowStart(changeRequestBody.getTimeWindowStart());
                if (!changeService.changeRequestDateValidation(changeRequestBody)) {
                    return ResponseEntity.badRequest().body("Date needs to be validate date time range");
                }
            }
            if (changeRequestBody.getTimeWindowEnd() != null) {
                changeRequest.setTimeWindowEnd(changeRequestBody.getTimeWindowEnd());
                if (!changeService.changeRequestDateValidation(changeRequestBody)) {
                    return ResponseEntity.badRequest().body("Date needs to be validate date time range");
                }
            }
            if (changeRequestBody.getState() != null)
                changeRequest.setState(changeRequestBody.getState());
            if (changeRequestBody.getImplementer() != null)
                changeRequest.setImplementer(changeRequestBody.getImplementer());
            if (changeRequestBody.getApproveOrDeny() != null)
                changeRequest.setApproveOrDeny(changeRequestBody.getApproveOrDeny());
            if (changeRequestBody.getTimeToRevert() != null)
                changeRequest.setTimeToRevert(changeRequestBody.getTimeToRevert());
            if (changeRequestBody.getDateCreated() != null)
                changeRequest.setDateCreated(changeRequestBody.getDateCreated());
            if (changeRequestBody.getAuthorId() != null)
                changeRequest.setAuthor(userService.loadUserById(changeRequestBody.getAuthorId()));
            if (changeRequestBody.getRoles() != null)
                changeRequest.setRoles(changeService.toRole(changeRequestBody.getRoles()));
            if (changeRequestBody.getApproveOrDeny() != null)
                changeRequest.setApproveOrDeny(changeRequestBody.getApproveOrDeny());
            if (changeRequestBody.getState() != null)
                changeRequest.setState(changeRequestBody.getState());
            if (changeRequestBody.getRiskLevel() != null)
                changeRequest.setRiskLevel(changeService.toRiskLevel(changeRequestBody.getRiskLevel()));
            if (changeRequestBody.getBackoutPlan() != null){
                changeRequest.setBackoutPlan(changeRequestBody.getBackoutPlan());
            }

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
            change.setRoles(userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles().getName());
            change.setAuthorId(userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getId());
            ChangeRequest convertedChangeRequest = changeService.toEnity(change);
            changeService.save(convertedChangeRequest);
            ChangeRequestHttpResponseDTO changeRequestHttpResponse = changeService.toDto(convertedChangeRequest, false);
            return ResponseEntity.created(URI.create("/api/v1/change/" + changeRequestHttpResponse.getId())).body(changeRequestHttpResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }

    }
}
