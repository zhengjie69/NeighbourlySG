package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/EventService")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService)
    {
        this.eventService = eventService;
    }

    @PostMapping("/createEvent/{profileId}")
    public ResponseEntity<?> createEvent(
            @PathVariable("profileId") Long profileId,
            @RequestBody EventDto eventDto) {
        try {
            EventDto event = eventService.createEvent(eventDto, profileId);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            // Return a response with a 400 Bad Request status and error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAllUserEvent/{profileId}")
    public ResponseEntity<?> getAllUserEvent(
            @PathVariable("profileId") Long profileId) {
        try {
            List<EventDto> event = eventService.getAllUserEvent(profileId);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            // Return a response with a 400 Bad Request status and error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok("Profile deleted successfully!");
        } catch (Exception e) {
            // Return a response with a 400 Bad Request status and error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateEvent")
    public ResponseEntity<?> updateEvent(@RequestBody EventDto updatedEvent) {
        try {
            EventDto eventDto = eventService.updateEvent(updatedEvent);
            return ResponseEntity.ok(eventDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
