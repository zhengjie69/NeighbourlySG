package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.ResourceNotFoundException;
import com.nusiss.neighbourlysg.mapper.EventMapper;
import com.nusiss.neighbourlysg.repository.EventRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EventServiceImpl implements EventService {

    private final ProfileRepository profileRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(ProfileRepository profileRepository, EventRepository eventRepository, EventMapper eventMapper) {
        this.profileRepository = profileRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto, Long ProfileId) {

        if(ProfileId == null) {
            throw new IllegalArgumentException("No Profile Id is inputted");
        }

        if(eventDto.getId() > 0) {
            throw new IllegalArgumentException("No Event Id should be inputted during creation");
        }

        Profile profile = profileRepository.findById(ProfileId)
                .orElseThrow(() -> new RuntimeException("Invalid profile is used"));

        Event event = eventMapper.toEntity(eventDto);
        event.setProfile(profile);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional
    public List<EventDto> getAllUserEvent(Long profileId) {
        if(profileId == null) {
            throw new IllegalArgumentException("No Profile Id is inputted");
        }

        Profile profile = new Profile();
        profile.setId(profileId);
        List<Event> listOfEvent = eventRepository.findByProfile(profile);

        List<EventDto> listOfEventDto = new ArrayList<>();
        for(Event event : listOfEvent){
            EventDto eventDto = eventMapper.toDto(event);
            listOfEventDto.add(eventDto);
        }
        return listOfEventDto;
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {

        Event retrievedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event with ID " + eventId + " cannot be found"));

        eventRepository.delete(retrievedEvent);
    }

    @Override
    @Transactional
    public EventDto updateEvent(EventDto eventDto) {
        Event existingEvent = eventRepository.findById(eventDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventDto.getId()));

        if (eventDto.getTitle() != null && !Objects.equals(eventDto.getTitle(), existingEvent.getTitle())) {
            existingEvent.setTitle(eventDto.getTitle());
        }

        if (eventDto.getDescription() != null && !Objects.equals(eventDto.getDescription(), existingEvent.getDescription())) {
            existingEvent.setDescription(eventDto.getDescription());
        }

        if (eventDto.getDate() != null && !Objects.equals(eventDto.getDate(), existingEvent.getDate())) {
            existingEvent.setDate(eventDto.getDate());
        }

        if (eventDto.getStartTime() != null && !Objects.equals(eventDto.getStartTime(), existingEvent.getStartTime())) {
            existingEvent.setStartTime(eventDto.getStartTime());
        }

        if (eventDto.getEndTime() != null && !Objects.equals(eventDto.getEndTime(), existingEvent.getEndTime())) {
            existingEvent.setEndTime(eventDto.getEndTime());
        }

        if (eventDto.getLocation() != null && !Objects.equals(eventDto.getLocation(), existingEvent.getLocation())) {
            existingEvent.setLocation(eventDto.getLocation());
        }

        Event updatedEvent = eventRepository.save(existingEvent);

        return eventMapper.toDto(updatedEvent);
    }

}
