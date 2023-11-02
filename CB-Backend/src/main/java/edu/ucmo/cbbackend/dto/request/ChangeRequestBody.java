package edu.ucmo.cbbackend.dto.request;

import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.ChangeType;
import edu.ucmo.cbbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeRequestBody {
    private Long UserId;
    private ChangeType changeType;
    private String description;
    private String reason;
    private Long applicationId;


    public ChangeRequest toChangeRequest(User user) {
        return ChangeRequest.builder()
                .author(user)
                .changeType(changeType)
                .description(description)
                .reason(reason)
                .applicationId(applicationId)
                .dateCreated(new Date())
                .build();
    }
}


