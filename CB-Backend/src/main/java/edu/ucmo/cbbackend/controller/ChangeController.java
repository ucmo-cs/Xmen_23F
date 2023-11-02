package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.dto.request.ChangeRequestBody;
import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponse;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.service.ChangeService;
import edu.ucmo.cbbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController("/api/v1/change")
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
    @PutMapping("/api/v1/change/{id}")
    public ResponseEntity<?> updateChangeById(@PathVariable Long id, @RequestBody ChangeRequestBody changeRequestBody) {
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


            changeService.save(changeRequest);
            return ResponseEntity.ok().body(changeRequest);
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
            return ResponseEntity.ok().body(changeRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @SecurityRequirement(name = "jwtAuth")
    @GetMapping("/api/v1/change/")
    public ResponseEntity<?> getChange(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "5") Integer size) {

        if (userService.userRepository.findByUsername(request.getUserPrincipal().getName()).getRoles().equals(rolesRepository.findByName("USER"))) {
            Page<ChangeRequestHttpResponse> list = changeService.findAllByUserIdAndSortByDate(page, size, request.getUserPrincipal().getName());
            return ResponseEntity.ok().body(list);
        }
        Page<ChangeRequestHttpResponse> list = changeService.findAllSortByDate(page, size);
        return ResponseEntity.ok().body(list);
    }

    
    @SecurityRequirement(name = "jwtAuth")
    @PostMapping("/api/v1/change")
    public ResponseEntity<?> change(@RequestBody ChangeRequestBody change) {

        try {
            User user = userService.loadUserById(change.getUserId());
            ChangeRequest convertedChangeRequest = change.toChangeRequest(user);
            changeService.save(convertedChangeRequest);
            ChangeRequestHttpResponse changeRequestHttpResponse = new ChangeRequestHttpResponse(convertedChangeRequest);
            return ResponseEntity.created(URI.create("/api/v1/change/" + changeRequestHttpResponse.getId())).body(changeRequestHttpResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }


    }
}
