package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class PersonPrincipal implements UserDetails {
    private Person person;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.asSet(person.getRole());
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
