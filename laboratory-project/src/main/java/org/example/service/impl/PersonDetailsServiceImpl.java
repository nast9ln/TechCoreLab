package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.JwtPerson;
import org.example.entity.Person;
import org.example.mapper.JwtPersonMapper;
import org.example.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;
    private final JwtPersonMapper personMapper;

    @Override
    public JwtPerson loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Person not found "));
        return buildUserDetails(person);
    }

    private JwtPerson buildUserDetails(Person person) {
        return personMapper.toJwtPerson(person);
    }
}
