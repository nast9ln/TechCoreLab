package org.example.service.security;

import lombok.RequiredArgsConstructor;
import org.example.dto.security.AuthenticationRequest;
import org.example.dto.security.AuthenticationResponse;
import org.example.dto.security.JwtPerson;
import org.example.entity.Person;
import org.example.enums.RoleEnum;
import org.example.exception.EntityNotFoundException;
import org.example.exception.LoginDuplicateException;
import org.example.mapper.JwtPersonMapper;
import org.example.repository.PersonRepository;
import org.example.repository.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtPersonMapper jwtPersonMapper;

    public AuthenticationResponse register(JwtPerson request) {
        Person person = Person.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(
                        request.getPassword()
                ))
                .registrationDate(Instant.now())
                .role(roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(EntityNotFoundException::new))
                .build();
        try {
            person = personRepository.save(person);
        } catch (Exception e) {
            throw new LoginDuplicateException("The username is already taken: {0}", person.getLogin());
        }
        String token = jwtService.generateToken(jwtPersonMapper.toJwtPerson(person));
        return AuthenticationResponse
                .builder()
                .login(request.getLogin())
                .token(token)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );
        Person person = personRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new EntityNotFoundException("person not found with login: {0}", request.getLogin()));
        String token = jwtService.generateToken(jwtPersonMapper.toJwtPerson(person));
        return AuthenticationResponse.builder()
                .login(person.getLogin())
                .token(token)
                .build();
    }

}