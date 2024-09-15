package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(source = "comments", target = "commentDtos") // If you have a commentDto list in PostDto
    PostDto toDto(Post post);

    @Mapping(source = "commentDtos", target = "comments")
    Post toEntity(PostDto postDto);
}