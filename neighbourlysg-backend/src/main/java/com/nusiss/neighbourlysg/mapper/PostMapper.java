package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(source = "content", target = "content")
    @Mapping(source = "profile.id", target = "profileId") // Map profile ID
    @Mapping(source = "profile.name", target = "profileName")
    @Mapping(source = "comments", target = "comments") // Map comments
    @Mapping(source = "tags", target = "tags") // Map tags
    @Mapping(source = "likesCount", target = "likeCount") // Map likesCount from entity to likeCount in DTO
    PostDto toDto(Post post);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "profileId", target = "profile.id") // Map profile ID
    @Mapping(source = "comments", target = "comments") // Map comments
    @Mapping(source = "tags", target = "tags") // Map tags
    @Mapping(source = "likeCount", target = "likesCount") // Map likeCount from DTO to likesCount in entity
    Post toEntity(PostDto postDto);
}