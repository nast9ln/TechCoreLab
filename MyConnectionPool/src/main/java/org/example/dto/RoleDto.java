package org.example.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.RoleEnum;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto extends BaseDto{
    private RoleEnum name;
}
