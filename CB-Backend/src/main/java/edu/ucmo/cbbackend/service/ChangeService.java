package edu.ucmo.cbbackend.service;

import edu.ucmo.cbbackend.dto.request.ChangeRequestBodyDTO;
import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponseDTO;
import edu.ucmo.cbbackend.model.*;
import edu.ucmo.cbbackend.repository.ChangeRepository;
import edu.ucmo.cbbackend.repository.RolesRepository;
import edu.ucmo.cbbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ChangeService {

   private final ChangeRepository changeRepository;
    private final UserRepository userRepository;
    private final RolesRepository roleRepository;
    public ChangeService(ChangeRepository changeRepository,
                         UserRepository userRepository, RolesRepository roleRepository) {
        this.changeRepository = changeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ChangeRequest findById(Long id) {
        return changeRepository.findById(id).orElseThrow(() -> new RuntimeException("Change Request not found"));

    }

    public void deleteById(Long id) {
        changeRepository.deleteById(id);
    }

    public void save(ChangeRequest changeRequest) {
        changeRepository.save(changeRequest);
    }

 public Page<ChangeRequestHttpResponseDTO> findAllByRolesAndState(int page, int size, boolean showUsernames, Roles roles, ChangeRequestState state) {
     Page<ChangeRequest> changeRequests = changeRepository.findByRoles_NameAndState(roles.getName(), state, PageRequest.of(page, size,  Sort.by("dateUpdated").descending()));
    return changeRequests.map(changeRequest -> toDto(changeRequest, showUsernames));
}

public Page<ChangeRequestHttpResponseDTO> findAllByState(int page, int size, boolean showUsernames, ChangeRequestState state) {
        Page<ChangeRequest> changeRequests = changeRepository.findAllByState(state, PageRequest.of(page, size, Sort.by("dateUpdated").descending()));
        return changeRequests.map(changeRequest -> toDto(changeRequest, showUsernames));
    }

    public Page<ChangeRequestHttpResponseDTO> findAllByUserIdAndSortByDate(int page, int size, String username, boolean showUsernames, ChangeRequestState state) {
        User user = userRepository.findByUsername(username);
        // This is use for the USER ROLE to see all of their change requests
        Page<ChangeRequest> changeRequestHttpResponses = changeRepository.findByAuthorAndState(user, state , PageRequest.of(page, size, Sort.by("dateUpdated").descending()));
        return changeRequestHttpResponses.map(changeRequest -> toDto(changeRequest, showUsernames));
    }

    public ChangeRequest toEnity (ChangeRequestBodyDTO changeRequestBodyDTO) {
            User user = userRepository.findById(changeRequestBodyDTO.getAuthorId()).orElseThrow(() -> new RuntimeException("User not found"));
        return ChangeRequest.builder()
                .author(user)
                .changeType(toChangeType(changeRequestBodyDTO.getChangeType()))
                .applicationId(changeRequestBodyDTO.getApplicationId())
                .description(changeRequestBodyDTO.getDescription())
                .reason(changeRequestBodyDTO.getReason())
                .dateCreated(changeRequestBodyDTO.getDateCreated())
                .dateUpdated(new Date())
                .timeWindowStart(changeRequestBodyDTO.getTimeWindowStart())
                .timeWindowEnd(changeRequestBodyDTO.getTimeWindowEnd())
                .timeToRevert(changeRequestBodyDTO.getTimeToRevert())
                .state(changeRequestBodyDTO.getState())
                .Implementer(changeRequestBodyDTO.getImplementer())
                .approveOrDeny(changeRequestBodyDTO.getApproveOrDeny())
                .backoutPlan(changeRequestBodyDTO.getBackoutPlan())
                .roles( toRole(changeRequestBodyDTO.getRoles()))
                .riskLevel(toRiskLevel(changeRequestBodyDTO.getRiskLevel()))
                .build();
    }

    public ChangeRequestHttpResponseDTO toDto (ChangeRequest changeRequest, boolean showUsername){
        return new ChangeRequestHttpResponseDTO(changeRequest.getId(),
                changeRequest.getAuthor().getId(),
                changeRequest.getChangeType(),
                changeRequest.getApplicationId(),
                changeRequest.getDescription(),
                changeRequest.getReason(),
                changeRequest.getDateCreated(),
                changeRequest.getDateUpdated(),
                changeRequest.getTimeWindowStart(),
                changeRequest.getTimeWindowEnd(),
                changeRequest.getTimeToRevert(),
                changeRequest.getApproveOrDeny(),
                changeRequest.getState(),
                changeRequest.getImplementer(),
                showUsername ? Optional.of(changeRequest.getAuthor().getUsername()) : Optional.empty(),
                changeRequest.getRiskLevel(),
                changeRequest.getRoles(),
                changeRequest.getBackoutPlan()
        );

    }

    public boolean changeRequestDateValidation(ChangeRequestBodyDTO changeRequestBodyDTO){
        return  changeRequestBodyDTO.getTimeWindowStart().after(changeRequestBodyDTO.getTimeWindowEnd());
    }

    public Roles toRole(String role) {
        //! ERROR Handling is need here
        return roleRepository.findByNameIgnoreCase(role.toUpperCase());
    }

    public ChangeRequestRiskLevel toRiskLevel(String riskLevel) {
        if( riskLevel == null || riskLevel.isEmpty() || riskLevel.isBlank()){
            return null;
        }
        if(riskLevel.equalsIgnoreCase("null")){
            return null;
        }
        if (riskLevel.equalsIgnoreCase("HIGH") ){
            return ChangeRequestRiskLevel.HIGH;
        }
        else if (riskLevel.equalsIgnoreCase("MEDIUM")){
            return ChangeRequestRiskLevel.MEDIUM;
        }
        else if (riskLevel.equalsIgnoreCase("LOW")){
            return ChangeRequestRiskLevel.LOW;
        }
        else {
            return null;
        }
    }

    public ChangeRequestState toState(String state) {
        //! ERROR Handling is need here
        return ChangeRequestState.valueOf(state.toUpperCase());

    }
    public ChangeType toChangeType(String changeType) {
        //! ERROR Handling is need here
        return ChangeType.valueOf(changeType.toUpperCase());

    }

    public Roles determineChangeRequestNextRole(Roles roles) {
        if (roles.getName().equalsIgnoreCase("USER")) {
            return roleRepository.findByNameIgnoreCase("DEPARTMENT");
        } else if (roles.getName().equalsIgnoreCase("DEPARTMENT")) {
            return roleRepository.findByNameIgnoreCase("APPLICATION");
        } else {
            return roleRepository.findByNameIgnoreCase("OPERATIONS");
        }

    }


}

