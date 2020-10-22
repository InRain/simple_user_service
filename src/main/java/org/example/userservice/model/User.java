package org.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {
    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "userid", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "roleid", referencedColumnName = "id")})
    private List<Role> roles;

    @JsonIgnore
    public boolean isNameValid() {
        if (name != null) {
            return !name.isEmpty();
        } else return false;
    }

    @JsonIgnore
    public boolean isPasswordValid() {
        if (password != null) {
            return !password.isEmpty() && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{3,}$");
        } else return false;
    }

    @JsonIgnore
    public boolean isLoginValid() {
        if (login != null) {
            return !login.isEmpty();
        } else return false;
    }

    @JsonIgnore
    public boolean isRolesValid() {
        if (roles != null) {
            return roles.size() > 0;
        } else return false;
    }
}
