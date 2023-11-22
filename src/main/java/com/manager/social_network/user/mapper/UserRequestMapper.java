package com.manager.social_network.user.mapper;

import com.manager.social_network.common.mapper.GenericMapper;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper extends GenericMapper<UserRequest, User> {
}
