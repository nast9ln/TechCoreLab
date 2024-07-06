package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Person;
import org.example.entity.Role;
import org.example.repository.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PersonDetailsServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Person not found "));
        return new User(person.getLogin(), person.getPassword(), mapRoleToAuthorities(person.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Role role) {
        return Collections.singletonList(role);
    }
}
