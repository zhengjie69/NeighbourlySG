package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.EventParticipantDto;
import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.EventParticipant;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.ResourceNotFoundException;
import com.nusiss.neighbourlysg.mapper.EventMapper;
import com.nusiss.neighbourlysg.observer.EventCreatedEvent;
import com.nusiss.neighbourlysg.observer.EventDeletedEvent;
import com.nusiss.neighbourlysg.observer.EventUpdatedEvent;
import com.nusiss.neighbourlysg.repository.EventParticipantRepository;
import com.nusiss.neighbourlysg.repository.EventRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.EventService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EventServiceImpl implements EventService {

    private final ProfileRepository profileRepository;
    private final EventRepository eventRepository;

    private final EventParticipantRepository eventParticipantRepository;
    private final EventMapper eventMapper;

    private final ApplicationEventPublisher eventPublisher;

    public EventServiceImpl(ProfileRepository profileRepository, EventRepository eventRepository,
                            EventParticipantRepository eventParticipantRepository, EventMapper eventMapper,
                            ApplicationEventPublisher eventPublisher) {
        this.profileRepository = profileRepository;
        this.eventRepository = eventRepository;
        this.eventParticipantRepository = eventParticipantRepository;
        this.eventMapper = eventMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto, Long profileId) {

        if(profileId == null) {
            throw new IllegalArgumentException("No Profile Id is inputted");
        }

        if(eventDto.getId() != null) {
            throw new IllegalArgumentException("No Event Id should be inputted during creation");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Invalid profile is used"));

        Event event = eventMapper.toEntity(eventDto);
        event.setProfile(profile);
        Event savedEvent = eventRepository.save(event);

        eventPublisher.publishEvent(new EventCreatedEvent(this, savedEvent.getId()));

        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional
    public List<EventDto> getAllUserEvent(Long profileId) {
        if(profileId == null) {
            throw new IllegalArgumentException("No Profile Id is inputted");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile with ID of " + profileId + " cannot be found"));

        List<Event> listOfEvent = eventRepository.findByProfile(profile);

        List<EventDto> listOfEventDto = new ArrayList<>();
        for(Event event : listOfEvent){
            long rsvpCount = eventParticipantRepository.countByEvent(event);
            EventDto eventDto = eventMapper.toDto(event);
            eventDto.setRsvpCount(rsvpCount);
            listOfEventDto.add(eventDto);
        }
        return listOfEventDto;
    }

    @Override
    @Transactional
    public List<EventDto> getAllCurrentEvent(Long profileId, String constituency, String location) {
        LocalDate currentDate = LocalDate.now();

        List<Event> listOfEvent = eventRepository.findByDateGreaterThanEqualAndNotOwnedBy(currentDate, profileId, constituency, location);

        List<EventDto> listOfEventDto = new ArrayList<>();
        for(Event event : listOfEvent){
            long rsvpCount = eventParticipantRepository.countByEvent(event);
            EventDto eventDto = eventMapper.toDto(event);
            eventDto.setRsvpCount(rsvpCount);
            listOfEventDto.add(eventDto);
        }
        return listOfEventDto;
    }

    @Override
    @Transactional
    public List<EventDto> getAllPastEvent(Long profileId, String constituency, String location) {
        LocalDate currentDate = LocalDate.now();

        List<Event> listOfEvent = eventRepository.findByDateBeforeAndNotOwnedBy(currentDate, profileId, constituency, location);

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

        eventPublisher.publishEvent(new EventDeletedEvent(this, retrievedEvent.getId()));
    }

    @Override
    @Transactional
    public EventDto updateEvent(EventDto eventDto) {
        Event existingEvent = eventRepository.findById(eventDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Event to update not found with id: " + eventDto.getId()));

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

        eventPublisher.publishEvent(new EventUpdatedEvent(this, existingEvent.getId()));

        return eventMapper.toDto(updatedEvent);
    }

    @Override
    @Transactional
    public long rsvpParticipant(EventParticipantDto eventParticipantDto){

        Event selectedEvent = eventRepository.findById(eventParticipantDto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event to rsvp not found with id: " + eventParticipantDto.getEventId()));

        Profile selectedProfile = profileRepository.findById(eventParticipantDto.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + eventParticipantDto.getProfileId()));

        boolean duplicateCheck = eventParticipantRepository.existsByEventAndProfile(selectedEvent, selectedProfile);

        if(duplicateCheck) {
           throw new IllegalArgumentException("You have registered for this event already.");
        }

        EventParticipant eventParticipant = new EventParticipant();
        eventParticipant.setEvent(selectedEvent);
        eventParticipant.setProfile(selectedProfile);

        EventParticipant savedParticipant = eventParticipantRepository.save(eventParticipant);

        if(savedParticipant.getId() != null) {
            return eventParticipantRepository.countByEvent(eventParticipant.getEvent());
        }

        return -1;
    }

    @Override
    @Transactional
    public boolean deleteRsvpAsParticipant(EventParticipantDto eventParticipantDto) {
        Event selectedEvent = eventRepository.findById(eventParticipantDto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event to delete rsvp not found with id: " + eventParticipantDto.getEventId()));

        Profile selectedProfile = profileRepository.findById(eventParticipantDto.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + eventParticipantDto.getProfileId()));

        EventParticipant eventParticipant = eventParticipantRepository.findByEventAndProfile(selectedEvent, selectedProfile)
                .orElse(null);

        if (eventParticipant != null) {
            eventParticipantRepository.delete(eventParticipant);
            return true;
        }

        return false;
    }

}
