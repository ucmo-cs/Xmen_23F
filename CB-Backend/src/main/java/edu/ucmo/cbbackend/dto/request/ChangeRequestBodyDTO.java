package edu.ucmo.cbbackend.dto.request;

import edu.ucmo.cbbackend.model.*;
import edu.ucmo.cbbackend.repository.UserRepository;
import edu.ucmo.cbbackend.service.ChangeService;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link edu.ucmo.cbbackend.model.ChangeRequest}
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChangeRequestBodyDTO implements Serializable {
    private Long authorId;
    private ChangeType changeType;
    private Long applicationId;
    private String description;
    private String reason;
    @FutureOrPresent
    private Date dateCreated;
    @FutureOrPresent
    private Date dateUpdated;
    @Future
    private Date timeWindowStart;
    @Future
    private Date timeWindowEnd;
    @PositiveOrZero
    private Long timeToRevert;
    private ChangeRequestApproveOrDeny approveOrDeny;
    private ChangeRequestState state;
    private String Implementer = "Not Assigned";
    private Roles roles;
    private ChangeRequestRiskLevel riskLevel;

    public ChangeRequest toChangeRequest(UserRepository userRepository) {
        User user = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("User not found"));
        return ChangeRequest.builder()
                .author(user)
                .changeType(changeType)
                .applicationId(applicationId)
                .description(description)
                .reason(reason)
                .dateCreated(dateCreated)
                .dateUpdated(dateUpdated)
                .timeWindowStart(timeWindowStart)
                .timeWindowEnd(timeWindowEnd)
                .timeToRevert(timeToRevert)
                .state(state)
                .Implementer(Implementer)
                .approveOrDeny(approveOrDeny)
                .roles(roles)
                .riskLevel(riskLevel)
                .build();
    }

}