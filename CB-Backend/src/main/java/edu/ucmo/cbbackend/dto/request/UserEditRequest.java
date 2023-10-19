package edu.ucmo.cbbackend.dto.request;

import edu.ucmo.cbbackend.model.Roles;
import edu.ucmo.cbbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditRequest {
    private String username;
    private String password;
    private Roles roles;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public UserEditRequest(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
    }


}
