package edu.school21.restful.models;

import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.models.roles.Role;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import javax.persistence.*;

@Getter
@Setter
@Table(name = "usr")
@Entity
@NoArgsConstructor
public class User extends RepresentationModel<User> implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Role roles;
    private String login;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User(String username, String password, Role role) {
        this(username, password, true, true, true, true, role);
    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Role role) {
        if (username != null && !"".equals(username) && password != null) {
            this.firstname = username;
            this.password = password;
            this.enabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.roles = role;
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public User(UserDto userDto) {
        this.firstname = userDto.getFirstname();
        this.lastname = userDto.getLastname();
        this.login = userDto.getLogin();
        this.password = userDto.getPassword();
        this.roles = Role.valueOf(userDto.getRoles());
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(roles);
    }

    @Override
    public String getUsername() {
        return firstname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
