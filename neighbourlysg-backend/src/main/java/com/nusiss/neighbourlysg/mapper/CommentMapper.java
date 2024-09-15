package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.CommentDto;
import com.nusiss.neighbourlysg.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);
}
