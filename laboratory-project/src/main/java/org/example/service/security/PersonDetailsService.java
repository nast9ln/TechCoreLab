package org.example.service.security;

import lombok.RequiredArgsConstructor;
import org.example.dto.security.JwtPerson;
import org.example.entity.Person;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.JwtPersonMapper;
import org.example.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;
    private final JwtPersonMapper personMapper;

    @Override
    public JwtPerson loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username).orElseThrow(() -> new EntityNotFoundException("Person not found " +
                "with login " + username));
        return buildUserDetails(person);
    }

    private JwtPerson buildUserDetails(Person person) {
        return personMapper.toJwtPerson(person);
    }
}
