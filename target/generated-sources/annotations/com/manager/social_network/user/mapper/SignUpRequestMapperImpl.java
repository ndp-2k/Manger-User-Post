package com.manager.social_network.user.mapper;

import com.manager.social_network.account.dto.SignUpRequest;
import com.manager.social_network.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-22T08:55:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Oracle Corporation)"
)
@Component
public class SignUpRequestMapperImpl implements SignUpRequestMapper {

    @Override
    public User dtoToEntity(SignUpRequest dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( dto.getUsername() );
        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );

        return user;
    }

    @Override
    public SignUpRequest entityToDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        SignUpRequest signUpRequest = new SignUpRequest();

        signUpRequest.setUsername( entity.getUsername() );
        signUpRequest.setEmail( entity.getEmail() );
        signUpRequest.setPassword( entity.getPassword() );

        return signUpRequest;
    }

    @Override
    public List<SignUpRequest> listEntityToDto(List<User> entity) {
        if ( entity == null ) {
            return null;
        }

        List<SignUpRequest> list = new ArrayList<SignUpRequest>( entity.size() );
        for ( User user : entity ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    @Override
    public List<User> listDtoToEntity(List<SignUpRequest> dto) {
        if ( dto == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dto.size() );
        for ( SignUpRequest signUpRequest : dto ) {
            list.add( dtoToEntity( signUpRequest ) );
        }

        return list;
    }
}
