package edu.ucmo.cbbackend.dto.request;

import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.model.User;

import java.util.List;

public class UserRegisterRequest {

    private Long id;
    private String Username;

    private String password;

    private Roles role;
    private List<ChangeRequest> changeRequests;

    public UserRegisterRequest(User user) {
        this.id = user.getId();
        this.Username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRoles();
        this.changeRequests = user.getChangeRequests();
    }


}
