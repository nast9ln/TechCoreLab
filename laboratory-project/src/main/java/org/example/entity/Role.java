package org.example.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.RoleEnum;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "role")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public Role(Long id, RoleEnum name) {
        super(id);
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.name();
    }
}
