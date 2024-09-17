package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto toDto(Event event);

    Event toEntity(EventDto eventDto);
}
