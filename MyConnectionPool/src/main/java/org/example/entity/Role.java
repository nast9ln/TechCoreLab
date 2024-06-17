package org.example.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.RoleEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "role", schema = "techcore")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public Role(Long id, RoleEnum name) {
        super(id);
        this.name = name;
    }
}
