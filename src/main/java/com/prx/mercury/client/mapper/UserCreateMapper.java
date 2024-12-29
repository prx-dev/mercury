package com.prx.mercury.client.mapper;

import com.prx.commons.pojo.Person;
import com.prx.mercury.client.to.BackboneUserCreateRequest;
import com.prx.mercury.client.to.BackboneUserCreateResponse;
import com.prx.mercury.client.to.UserCreateRequest;
import com.prx.mercury.client.to.UserCreateResponse;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(
        // Specifies that the mapper should be a Spring bean.
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {BackboneUserCreateRequest.class, UserCreateRequest.class}
)
@MapperConfig(
        // Specifies that the mapper should fail if there are any unmapped properties.
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        // Specifies that the mapper should fail if there are any unmapped properties.
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserCreateMapper {

    @Mapping(target="alias", source= "userCreateRequest.alias")
    @Mapping(target="email", source="userCreateRequest.email")
    @Mapping(target="active", ignore = true)
    @Mapping(target="password", source="userCreateRequest.password")
    @Mapping(target="person", expression="java(getPerson(userCreateRequest))")
    @Mapping(target="roleId", source="roleId")
    @Mapping(target="applicationId", source="applicationId")
    BackboneUserCreateRequest toBackbone(UserCreateRequest userCreateRequest, UUID applicationId, UUID roleId);

    @Mapping(target="id", source="id")
    @Mapping(target="alias", source="alias")
    @Mapping(target="active", source="active")
    @Mapping(target="email", source="email")
    @Mapping(target="roleId", source="roleId")
    @Mapping(target="personId", source="personId")
    @Mapping(target="applicationId", source="applicationId")
    UserCreateResponse fromBackbone(BackboneUserCreateResponse backboneUserCreateResponse);

    default Person getPerson(UserCreateRequest userCreateRequest){
        var person = new Person();
        person.setFirstName(userCreateRequest.firstname());
        person.setLastName(userCreateRequest.lastname());
        person.setGender("N");
        person.setMiddleName("");

        return person;
    }

}
