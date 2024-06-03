package org.example.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Person extends BaseEntity{
    private String name;
    private Instant registrationDate;
    private String login;
    private Role role;
}
