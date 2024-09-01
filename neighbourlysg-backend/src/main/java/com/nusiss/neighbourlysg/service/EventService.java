package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.EventDto;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto, Long profileId);

    List<EventDto> getAllUserEvent(Long profileId);

    void deleteEvent(Long id);

    EventDto updateEvent(EventDto eventDto);
}
