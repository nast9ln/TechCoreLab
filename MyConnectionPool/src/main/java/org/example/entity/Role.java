package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.enums.RoleEnum;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    private RoleEnum name;

    public Role(Long id, RoleEnum name) {
        super(id);
        this.name = name;
    }
}
