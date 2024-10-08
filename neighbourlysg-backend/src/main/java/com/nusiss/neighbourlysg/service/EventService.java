package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.EventParticipantDto;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto, Long profileId);

    List<EventDto> getAllUserEvent(Long profileId);

    List<EventDto> getAllCurrentEvent(Long profileId, String constituency, String location);

    List<EventDto> getAllPastEvent(Long profileId, String constituency, String location);

    void deleteEvent(Long id);

    EventDto updateEvent(EventDto eventDto);

    long rsvpParticipant(EventParticipantDto eventParticipantDto);

    boolean deleteRsvpAsParticipant(EventParticipantDto eventParticipantDto);
}
