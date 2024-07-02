package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.entity.Person;
import org.example.entity.PersonPrincipal;
import org.example.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username).orElseThrow(()-> new UsernameNotFoundException(""));
        return new PersonPrincipal(person);
    }
}
