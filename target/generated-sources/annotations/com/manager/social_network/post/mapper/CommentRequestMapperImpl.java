package com.manager.social_network.post.mapper;

import com.manager.social_network.post.dto.CommentRequest;
import com.manager.social_network.post.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-22T15:49:30+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Oracle Corporation)"
)
@Component
public class CommentRequestMapperImpl implements CommentRequestMapper {

    @Override
    public Comment dtoToEntity(CommentRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setContent( dto.getContent() );
        comment.setImgId( dto.getImgId() );

        return comment;
    }

    @Override
    public CommentRequest entityToDto(Comment entity) {
        if ( entity == null ) {
            return null;
        }

        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setContent( entity.getContent() );
        commentRequest.setImgId( entity.getImgId() );

        return commentRequest;
    }

    @Override
    public List<CommentRequest> listEntityToDto(List<Comment> entity) {
        if ( entity == null ) {
            return null;
        }

        List<CommentRequest> list = new ArrayList<CommentRequest>( entity.size() );
        for ( Comment comment : entity ) {
            list.add( entityToDto( comment ) );
        }

        return list;
    }

    @Override
    public List<Comment> listDtoToEntity(List<CommentRequest> dto) {
        if ( dto == null ) {
            return null;
        }

        List<Comment> list = new ArrayList<Comment>( dto.size() );
        for ( CommentRequest commentRequest : dto ) {
            list.add( dtoToEntity( commentRequest ) );
        }

        return list;
    }
}
