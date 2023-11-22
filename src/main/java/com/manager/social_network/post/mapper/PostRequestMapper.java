package com.manager.social_network.post.mapper;

import com.manager.social_network.common.mapper.GenericMapper;
import com.manager.social_network.post.dto.PostRequest;
import com.manager.social_network.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostRequestMapper extends GenericMapper<PostRequest, Post> {
}
