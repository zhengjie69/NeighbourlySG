package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId")  // Map post ID
    @Mapping(source = "profile.id", target = "profileId")  // Map profile ID
    @Mapping(source = "profile.name", target = "profileName") // Map profile Name
    CommentDto toDto(Comment comment);

    @Mapping(source = "postId", target = "post.id")  // Map post ID
    @Mapping(source = "profileId", target = "profile.id")  // Map profile ID
    @Mapping(source = "profileName", target = "profile.name") // Map profile Name
    Comment toEntity(CommentDto commentDto);
}
