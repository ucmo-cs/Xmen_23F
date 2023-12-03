package edu.ucmo.cbbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    private Long id;
    private String Username;

    private String password;

    private String roles;
    @JsonIgnore
    private List<ChangeRequest> changeRequests;




}
