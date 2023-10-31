package edu.ucmo.cbbackend.controller;

import edu.ucmo.cbbackend.dto.request.ChangeRequestBody;
import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponse;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.service.ChangeService;
import edu.ucmo.cbbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("/api/v1/change")
public class ChangeController {

    private final UserService userService;
    private final ChangeService changeService;

    public ChangeController(ChangeService changeService, UserService userService) {
        this.changeService = changeService;
        this.userService = userService;

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
    @PostMapping("/api/v1/change")
    public ResponseEntity<?> change(@RequestBody ChangeRequestBody change) {

        try {
            User user = userService.loadUserById(change.getUserId());
            ChangeRequest convertedChangeRequest = change.toChangeRequest(user);
            changeService.save(convertedChangeRequest);
            ChangeRequestHttpResponse changeRequestHttpResponse = new ChangeRequestHttpResponse(convertedChangeRequest);
            return ResponseEntity.ok().body(change);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }


    }
}
