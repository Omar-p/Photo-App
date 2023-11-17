package com.example.photoapp.api.users.register;

import com.example.photoapp.api.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "userId", ignore = true)})
  User userRegistrationRequestToUser(UserRegistrationRequest userRegistrationRequest);

}
