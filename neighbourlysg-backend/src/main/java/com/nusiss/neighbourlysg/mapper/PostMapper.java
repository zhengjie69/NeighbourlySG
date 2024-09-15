package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.PostDto;
import com.nusiss.neighbourlysg.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(source = "comments", target = "comments")
    PostDto toDto(Post post);

    @Mapping(source = "comments", target = "comments")
    Post toEntity(PostDto postDto);

}