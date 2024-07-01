package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "person")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Person extends BaseEntity {
    private String name;
    private Instant registrationDate;
    private String login;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role")
    private Role role;
}
