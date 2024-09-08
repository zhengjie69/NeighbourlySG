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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

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
        eventController = new EventController(eventService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    public void testCreateEvent() throws Exception {
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
        doThrow(new IllegalArgumentException("No Profile Id is inputted"))
                .when(eventService).createEvent(any(EventDto.class), eq(profileId));

        // Call the controller method
        ResponseEntity<?> response = eventController.createEvent(profileId, eventDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No Profile Id is inputted", response.getBody());
    }

    @Test
    public void testGetAllUserEvent() throws Exception {
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
        doThrow(new RuntimeException("Error retrieving events"))
                .when(eventService).getAllUserEvent(eq(profileId));

        // Call the controller method
        ResponseEntity<?> response = eventController.getAllUserEvent(profileId);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error retrieving events", response.getBody());
    }

    @Test
    public void testGetAllCurrentEvent() throws Exception {
        Long profileId = 1L;
        List<EventDto> events = List.of(new EventDto()); // populate as needed

        when(eventService.getAllCurrentEvent(profileId)).thenReturn(events);

        mockMvc.perform(get("/api/EventService/getAllCurrentEvent/{profileId}", profileId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllCurrentEvent_ExceptionThrown() {
        Long profileId = 1L;

        // Configure mock to throw an exception
        doThrow(new RuntimeException("Error retrieving events"))
                .when(eventService).getAllCurrentEvent(eq(profileId));

        // Call the controller method
        ResponseEntity<?> response = eventController.getAllCurrentEvent(profileId);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error retrieving events", response.getBody());
    }

    @Test
    public void testGetAllPastEvent() throws Exception {
        Long profileId = 1L;
        List<EventDto> events = List.of(new EventDto()); // populate as needed

        when(eventService.getAllPastEvent(profileId)).thenReturn(events);

        mockMvc.perform(get("/api/EventService/getAllPastEvent/{profileId}", profileId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPastEvent_ExceptionThrown() {
        Long profileId = 1L;

        // Configure mock to throw an exception
        doThrow(new RuntimeException("Error retrieving events"))
                .when(eventService).getAllPastEvent(eq(profileId));

        // Call the controller method
        ResponseEntity<?> response = eventController.getAllPastEvent(profileId);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error retrieving events", response.getBody());
    }

    @Test
    public void testDeleteEvent() throws Exception {
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
                .when(eventService).deleteEvent(eq(eventId));

        // Call the controller method
        ResponseEntity<String> response = eventController.deleteEvent(eventId);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error deleting event", response.getBody());
    }

    @Test
    public void testUpdateEvent() throws Exception {
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
        doThrow(new RuntimeException("Error updating event"))
                .when(eventService).updateEvent(any(EventDto.class));

        // Call the controller method
        ResponseEntity<?> response = eventController.updateEvent(eventDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error updating event", response.getBody());
    }

    @Test
    public void testRsvpParticipant() throws Exception {
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
    public void testDeleteRsvpAsParticipant() throws Exception {
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
