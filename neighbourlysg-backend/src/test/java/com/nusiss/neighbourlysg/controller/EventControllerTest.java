package com.nusiss.neighbourlysg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.EventParticipantDto;
import com.nusiss.neighbourlysg.service.EventService;
import com.nusiss.neighbourlysg.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private EventController eventController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        eventController = new EventController(eventService, messagingTemplate);
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void testCreateEvent() throws Exception {
        EventDto eventDto = new EventDto(); // Populate with necessary data
        Long profileId = 1L;

        when(eventService.createEvent(any(EventDto.class), eq(profileId))).thenReturn(eventDto);

        mockMvc.perform(post("/api/EventService/createEvent/{profileId}", profileId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEvent_ExceptionThrown() {
        Long profileId = 1L;
        EventDto eventDto = new EventDto();

        // Configure mock to throw exception
        when(eventService.createEvent(eventDto, profileId)).thenReturn(null);

        // Call the controller method
        ResponseEntity<String> response = eventController.createEvent(profileId, eventDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAllUserEvent() throws Exception {
        Long profileId = 1L;
        List<EventDto> events = List.of(new EventDto()); // populate as needed

        when(eventService.getAllUserEvent(profileId)).thenReturn(events);

        mockMvc.perform(get("/api/EventService/getAllUserEvent/{profileId}", profileId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUserEvent_ExceptionThrown() {
        Long profileId = 1L;

        // Configure mock to throw an exception
        when(eventService.getAllUserEvent(profileId)).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<EventDto>> response = eventController.getAllUserEvent(profileId);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAllCurrentEvent() throws Exception {
        Long profileId = 1L;
        String constituency = "testConstituency";
        String location = "test location";
        List<EventDto> events = List.of(new EventDto()); // populate as needed

        when(eventService.getAllCurrentEvent(profileId, constituency, location)).thenReturn(events);

        mockMvc.perform(get("/api/EventService/getAllCurrentEvent/{profileId}/{constituency}",
                        profileId, constituency).param("location", location))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllCurrentEvent_ExceptionThrown() {
        Long profileId = 1L;
        String constituency = "testConstituency";
        String location = "test location";

        // Configure mock to throw an exception
        when(eventService.getAllCurrentEvent(profileId, constituency, location)).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<EventDto>> response = eventController.getAllCurrentEvent(profileId, constituency, location);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAllPastEvent() throws Exception {
        Long profileId = 1L;
        String constituency = "testConstituency";
        String location = "test location";
        List<EventDto> events = List.of(new EventDto()); // populate as needed

        when(eventService.getAllPastEvent(profileId, constituency, location)).thenReturn(events);

        mockMvc.perform(get("/api/EventService/getAllPastEvent/{profileId}/{constituency}",
                        profileId, constituency).param("location", location))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPastEvent_ExceptionThrown() {
        Long profileId = 1L;
        String constituency = "testConstituency";
        String location = "test location";
        // Configure mock to throw an exception
        when(eventService.getAllPastEvent(profileId, constituency, location)).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<EventDto>> response = eventController.getAllPastEvent(profileId, constituency, location);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteEvent() throws Exception {
        Long eventId = 1L;

        doNothing().when(eventService).deleteEvent(eventId);

        mockMvc.perform(delete("/api/EventService/deleteEvent/{eventId}", eventId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEvent_ExceptionThrown() {
        Long eventId = 1L;

        // Configure mock to throw an exception
        doThrow(new RuntimeException("Error deleting event"))
                .when(eventService).deleteEvent(eventId);

        // Call the controller method
        ResponseEntity<String> response = eventController.deleteEvent(eventId);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error deleting event", response.getBody());
    }

    @Test
    void testUpdateEvent() throws Exception {
        EventDto updatedEvent = new EventDto(); // populate as needed

        when(eventService.updateEvent(any(EventDto.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/api/EventService/updateEvent")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEvent_ExceptionThrown() {
        EventDto eventDto = new EventDto();

        // Configure mock to throw an exception
        when(eventService.updateEvent(eventDto)).thenReturn(null);

        // Call the controller method
        ResponseEntity<EventDto> response = eventController.updateEvent(eventDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRsvpParticipant() throws Exception {
        EventParticipantDto rsvpDto = new EventParticipantDto(); // populate as needed

        Long rsvpCount = 1L;
        when(eventService.rsvpParticipant(any(EventParticipantDto.class))).thenReturn(rsvpCount);

        mockMvc.perform(post("/api/EventService/rsvpParticipant")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(rsvpDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testRsvpParticipant_ExceptionThrown() {
        EventParticipantDto rsvpDto = new EventParticipantDto();

        // Configure mock to throw an exception
        doThrow(new RuntimeException("Error RSVPing participant"))
                .when(eventService).rsvpParticipant(any(EventParticipantDto.class));

        // Call the controller method
        ResponseEntity<?> response = eventController.rsvpParticipant(rsvpDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error RSVPing participant", response.getBody());
    }

    @Test
    void testDeleteRsvpAsParticipant() throws Exception {
        EventParticipantDto rsvpDto = new EventParticipantDto(); // populate as needed

        when(eventService.deleteRsvpAsParticipant(any(EventParticipantDto.class))).thenReturn(true);

        mockMvc.perform(post("/api/EventService/deleteRsvpAsParticipant")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(rsvpDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRsvpAsParticipant_ExceptionThrown() {
        EventParticipantDto rsvpDto = new EventParticipantDto();

        // Configure mock to throw an exception
        doThrow(new RuntimeException("Error removing RSVP"))
                .when(eventService).deleteRsvpAsParticipant(any(EventParticipantDto.class));

        // Call the controller method
        ResponseEntity<?> response = eventController.deleteRsvpAsParticipant(rsvpDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error removing RSVP", response.getBody());
    }
}
