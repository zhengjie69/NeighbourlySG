package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId")  // Map post ID
    @Mapping(source = "profile.id", target = "profileId")  // Map profile ID
    CommentDto toDto(Comment comment);

    @Mapping(source = "postId", target = "post.id")  // Map post ID
    @Mapping(source = "profileId", target = "profile.id")  // Map profile ID
    Comment toEntity(CommentDto commentDto);
}
