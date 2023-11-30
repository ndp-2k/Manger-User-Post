package com.manager.social_network.post.mapper;

import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.entity.Post;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-29T17:16:37+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Oracle Corporation)"
)
@Component
public class PostRequestMapperImpl implements PostRequestMapper {

    @Override
    public Post dtoToEntity(PostRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Post post = new Post();

        post.setContent( dto.getContent() );
        post.setImgId( dto.getImgId() );

        return post;
    }

    @Override
    public PostRequest entityToDto(Post entity) {
        if ( entity == null ) {
            return null;
        }

        PostRequest postRequest = new PostRequest();

        postRequest.setContent( entity.getContent() );
        postRequest.setImgId( entity.getImgId() );

        return postRequest;
    }

    @Override
    public List<PostRequest> listEntityToDto(List<Post> entity) {
        if ( entity == null ) {
            return null;
        }

        List<PostRequest> list = new ArrayList<PostRequest>( entity.size() );
        for ( Post post : entity ) {
            list.add( entityToDto( post ) );
        }

        return list;
    }

    @Override
    public List<Post> listDtoToEntity(List<PostRequest> dto) {
        if ( dto == null ) {
            return null;
        }

        List<Post> list = new ArrayList<Post>( dto.size() );
        for ( PostRequest postRequest : dto ) {
            list.add( dtoToEntity( postRequest ) );
        }

        return list;
    }
}
