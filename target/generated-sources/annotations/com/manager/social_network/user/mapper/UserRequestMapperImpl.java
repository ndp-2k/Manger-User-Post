package com.manager.social_network.user.mapper;

import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-08T09:38:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Oracle Corporation)"
)
@Component
public class UserRequestMapperImpl implements UserRequestMapper {

    @Override
    public User dtoToEntity(UserRequest dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setFullName( dto.getFullName() );
        user.setBirthday( dto.getBirthday() );
        user.setJob( dto.getJob() );
        user.setLiving( dto.getLiving() );

        return user;
    }

    @Override
    public UserRequest entityToDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserRequest userRequest = new UserRequest();

        userRequest.setFullName( entity.getFullName() );
        userRequest.setBirthday( entity.getBirthday() );
        userRequest.setJob( entity.getJob() );
        userRequest.setLiving( entity.getLiving() );

        return userRequest;
    }

    @Override
    public List<UserRequest> listEntityToDto(List<User> entity) {
        if ( entity == null ) {
            return null;
        }

        List<UserRequest> list = new ArrayList<UserRequest>( entity.size() );
        for ( User user : entity ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    @Override
    public List<User> listDtoToEntity(List<UserRequest> dto) {
        if ( dto == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dto.size() );
        for ( UserRequest userRequest : dto ) {
            list.add( dtoToEntity( userRequest ) );
        }

        return list;
    }
}
