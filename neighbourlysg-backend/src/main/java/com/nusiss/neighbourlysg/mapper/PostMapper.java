package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(source = "content", target = "content")
    @Mapping(source = "profile.id", target = "profileId") // Map profile ID
    @Mapping(source = "comments", target = "comments") // Map comments
    @Mapping(source = "tags", target = "tags") // Map tags
    PostDto toDto(Post post);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "profileId", target = "profile.id") // Map profile ID
    @Mapping(source = "comments", target = "comments") // Map comments
    @Mapping(source = "tags", target = "tags") // Map tags
    Post toEntity(PostDto postDto);
}