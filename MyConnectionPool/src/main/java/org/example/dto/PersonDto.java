package org.example.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends BaseDto{
    private String name;
    private Instant registrationDate;
    private String login;
    private RoleDto role;
}
