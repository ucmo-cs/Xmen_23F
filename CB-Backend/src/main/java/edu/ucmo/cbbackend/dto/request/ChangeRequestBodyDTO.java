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
import org.apache.logging.log4j.util.Strings;

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
    private String backoutPlan;
    @FutureOrPresent
    private Date dateCreated;

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





}