package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.EventParticipantDto;
import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.EventParticipant;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.mapper.EventMapper;
import com.nusiss.neighbourlysg.repository.EventParticipantRepository;
import com.nusiss.neighbourlysg.repository.EventRepository;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.impl.EventServiceImpl;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class EventServiceImplTest {

    @Mock
    ProfileRepository profileRepository;

    @Mock
    EventRepository eventRepository;

    @Mock
    EventParticipantRepository eventParticipantRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EventMapper eventMapper;

    private EventService eventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventServiceImpl(profileRepository, eventRepository,
                eventParticipantRepository, eventMapper, eventPublisher);
    }

    @Test
    void testCreateEvent_Success() {

        Mockito.when(profileRepository.findById(any(Long.class))).thenReturn(Optional.of(MasterEntityTestUtil.createProfileEntity()));
        Mockito.when(eventRepository.save(Mockito.any())).thenReturn(MasterEntityTestUtil.createEventEntity());
        final EventDto dto = eventMapper.toDto(MasterEntityTestUtil.createEventEntity());
        EventDto result = eventService.createEvent(MasterDTOTestUtil.createEventDTO(), 1L);

        assertEquals(dto.getId(), result.getId());
    }

    @Test
    void testCreateEvent_NoProfileId() {
        EventDto eventDto = new EventDto();
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(eventDto, null));
    }

    @Test
    void testCreateEvent_NoEventIdProvided() {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(eventDto, 1L));
    }

    @Test
    void testCreateEvent_InvalidProfile() {
        // Setup the mock to return an empty Optional when finding the profile
        when(profileRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Directly invoke the method that may throw an exception
        assertThrows(RuntimeException.class, () -> eventService.createEvent(new EventDto(), 1L));
    }

    @Test
    void testGetAllUserEvent_Success() {
        Long profileId = 1L;
        Profile profile = MasterEntityTestUtil.createProfileEntity();
        Event event = MasterEntityTestUtil.createEventEntity();
        event.setId(1L);
        List<Event> listOfEvent = new ArrayList<>();
        listOfEvent.add(event);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(eventRepository.findByProfile(profile)).thenReturn(listOfEvent);
        when(eventParticipantRepository.countByEvent(event)).thenReturn(10L);

        List<EventDto> result = eventService.getAllUserEvent(profile.getId());

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllUserEvent_NoProfileId() {
        assertThrows(IllegalArgumentException.class, () -> eventService.getAllUserEvent(null));
    }

    @Test
    void testGetAllCurrentEvent() {
        Profile profile = MasterEntityTestUtil.createProfileEntity();
        Event event = MasterEntityTestUtil.createEventEntity();
        String constituency = "testConstituency";
        String location = "test location";
        event.setId(1L);
        List<Event> listOfEvent = new ArrayList<>();
        listOfEvent.add(event);

        when(eventRepository.findByDateGreaterThanEqualAndNotOwnedBy(LocalDate.now(), profile.getId(), constituency, location)).thenReturn(listOfEvent);
        when(eventParticipantRepository.countByEvent(event)).thenReturn(10L);

        List<EventDto> result = eventService.getAllCurrentEvent(profile.getId(), constituency, location);

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPastEvent() {
        Profile profile = MasterEntityTestUtil.createProfileEntity();
        Event event = MasterEntityTestUtil.createEventEntity();
        String constituency = "testConstituency";
        String location = "test location";
        event.setId(1L);
        List<Event> listOfEvent = new ArrayList<>();
        listOfEvent.add(event);

        when(eventRepository.findByDateBeforeAndNotOwnedBy(LocalDate.now(), profile.getId(), constituency, location)).thenReturn(listOfEvent);
        when(eventParticipantRepository.countByEvent(event)).thenReturn(10L);

        List<EventDto> result = eventService.getAllPastEvent(profile.getId(), constituency, location);

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteEvent() {
        Event event = MasterEntityTestUtil.createEventEntity();
        event.setId(1L);

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        eventService.deleteEvent(event.getId());

        assertEquals(1, event.getId());
    }

    @Test
    void testUpdateEvent() {
        EventDto targetEventDto = new EventDto();
        targetEventDto.setId(1L);
        targetEventDto.setTitle("TestTitle2");
        targetEventDto.setDescription("TestDescription2");
        targetEventDto.setDate(LocalDate.now().plusDays(1));
        targetEventDto.setStartTime("TestStartTime2");
        targetEventDto.setEndTime("TestEndTime2");
        targetEventDto.setLocation("TestLocation2");

        Event existingEvent = MasterEntityTestUtil.createEventEntity();

        Event updatedEvent = eventMapper.toEntity(targetEventDto);

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);

        EventDto result = eventService.updateEvent(targetEventDto);

        assertEquals(targetEventDto.getId(), result.getId());
    }

    @Test
    void testRsvpParticipantEvent_Success() {
        EventParticipantDto eventParticipantDto = new EventParticipantDto();
        eventParticipantDto.setEventId(1L);
        eventParticipantDto.setProfileId(1L);

        EventParticipant eventParticipant = MasterEntityTestUtil.createEventParticipantEntity();
        eventParticipant.setId(1L);

        List<EventParticipant> eventParticipantList = new ArrayList<>();
        eventParticipantList.add(eventParticipant);

        Event event = MasterEntityTestUtil.createEventEntity();
        event.setEventParticipants(eventParticipantList);
        Profile profile = MasterEntityTestUtil.createProfileEntity();
        profile.setEventParticipants(eventParticipantList);

        when(eventRepository.findById(eventParticipantDto.getEventId())).thenReturn(Optional.of(event));
        when(profileRepository.findById(eventParticipantDto.getProfileId())).thenReturn(Optional.of(profile));
        when(eventParticipantRepository.existsByEventAndProfile(event, profile)).thenReturn(false);
        when(eventParticipantRepository.save(any(EventParticipant.class))).thenReturn(eventParticipant);
        when(eventParticipantRepository.countByEvent(any(Event.class))).thenReturn(10L);

        Long result = eventService.rsvpParticipant(eventParticipantDto);

        assertNotEquals(-1, result);
    }

    @Test
    void testRsvpParticipantEvent_DuplicateFound() {
        EventParticipantDto eventParticipantDto = new EventParticipantDto();
        eventParticipantDto.setEventId(1L);
        eventParticipantDto.setProfileId(1L);

        EventParticipant eventParticipant = MasterEntityTestUtil.createEventParticipantEntity();
        eventParticipant.setId(1L);

        List<EventParticipant> eventParticipantList = new ArrayList<>();
        eventParticipantList.add(eventParticipant);

        Event event = MasterEntityTestUtil.createEventEntity();
        event.setEventParticipants(eventParticipantList);
        Profile profile = MasterEntityTestUtil.createProfileEntity();
        profile.setEventParticipants(eventParticipantList);

        when(eventRepository.findById(eventParticipantDto.getEventId())).thenReturn(Optional.of(event));
        when(profileRepository.findById(eventParticipantDto.getProfileId())).thenReturn(Optional.of(profile));
        when(eventParticipantRepository.existsByEventAndProfile(event, profile)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> eventService.rsvpParticipant(eventParticipantDto));
    }

    @Test
    void testDeleteRsvpAsParticipant()
    {
        EventParticipantDto eventParticipantDto = new EventParticipantDto();
        eventParticipantDto.setEventId(1L);
        eventParticipantDto.setProfileId(1L);

        EventParticipant eventParticipant = MasterEntityTestUtil.createEventParticipantEntity();
        eventParticipant.setId(1L);

        List<EventParticipant> eventParticipantList = new ArrayList<>();
        eventParticipantList.add(eventParticipant);

        Event event = MasterEntityTestUtil.createEventEntity();
        event.setEventParticipants(eventParticipantList);
        Profile profile = MasterEntityTestUtil.createProfileEntity();
        profile.setEventParticipants(eventParticipantList);

        when(eventRepository.findById(eventParticipantDto.getEventId())).thenReturn(Optional.of(event));
        when(profileRepository.findById(eventParticipantDto.getProfileId())).thenReturn(Optional.of(profile));
        when(eventParticipantRepository.findByEventAndProfile(event, profile)).thenReturn(Optional.of(eventParticipant));

        boolean result = eventService.deleteRsvpAsParticipant(eventParticipantDto);

        assertTrue(result);
    }

}
