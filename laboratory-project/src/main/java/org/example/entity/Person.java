package org.example.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "person", schema = "techcore")
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
