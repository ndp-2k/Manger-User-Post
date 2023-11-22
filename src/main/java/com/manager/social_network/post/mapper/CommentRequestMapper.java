package com.manager.social_network.post.mapper;

import com.manager.social_network.post.entity.Comment;
import com.manager.social_network.common.mapper.GenericMapper;
import com.manager.social_network.post.dto.CommentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentRequestMapper extends GenericMapper<CommentRequest, Comment> {
}
