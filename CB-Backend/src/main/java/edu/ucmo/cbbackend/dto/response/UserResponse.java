package edu.ucmo.cbbackend.dto.response;

import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String Username;
    private Roles role;
    private List<ChangeRequest> changeRequests;

 public UserResponse(User user) {
          this.id = user.getId();
            this.Username = user.getUsername();
            this.role = user.getRoles();
            this.changeRequests = user.getChangeRequests();
 }


}
