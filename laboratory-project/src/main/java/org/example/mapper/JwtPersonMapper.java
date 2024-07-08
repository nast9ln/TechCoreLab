package org.example.mapper;

import org.example.constant.GlobalConst;
import org.example.dto.security.JwtPerson;
import org.example.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = GlobalConst.COMPONENT_MODEL)
public interface JwtPersonMapper {
    JwtPerson toJwtPerson(Person person);

    Person toPerson(JwtPerson jwtPerson);
}
