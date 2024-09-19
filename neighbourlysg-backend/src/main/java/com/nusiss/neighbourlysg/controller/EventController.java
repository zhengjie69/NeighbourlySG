package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.EventParticipantDto;
import com.nusiss.neighbourlysg.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<String> createEvent(
            @PathVariable("profileId") Long profileId,
            @RequestBody EventDto eventDto) {
            EventDto event = eventService.createEvent(eventDto, profileId);
            if(event != null)
                return ResponseEntity.ok("Successful");
            else
                return ResponseEntity.badRequest().body("Failed");
    }

    @GetMapping("/getAllUserEvent/{profileId}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<List<EventDto>> getAllUserEvent(
            @PathVariable("profileId") Long profileId) {
            List<EventDto> event = eventService.getAllUserEvent(profileId);
            if(!event.isEmpty())
                return ResponseEntity.ok(event);
            // Return a response with a 400 Bad Request status and error message
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/getAllCurrentEvent/{profileId}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<List<EventDto>> getAllCurrentEvent(
            @PathVariable("profileId") Long profileId) {
            List<EventDto> event = eventService.getAllCurrentEvent(profileId);
            if(!event.isEmpty())
                return ResponseEntity.ok(event);
            else
            // Return a response with a 400 Bad Request status and error message
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/getAllPastEvent/{profileId}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<List<EventDto>> getAllPastEvent(
            @PathVariable("profileId") Long profileId) {
            List<EventDto> event = eventService.getAllPastEvent(profileId);
            if(!event.isEmpty())
                return ResponseEntity.ok(event);
            else
                // Return a response with a 400 Bad Request status and error message
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto updatedEvent) {
            EventDto eventDto = eventService.updateEvent(updatedEvent);
            if(eventDto != null)
                return ResponseEntity.ok(eventDto);
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/rsvpParticipant")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<String> rsvpParticipant(@RequestBody EventParticipantDto rsvpPersonnel) {
        try {
            long rsvpCount = eventService.rsvpParticipant(rsvpPersonnel);
            if(rsvpCount > 0) {
                return ResponseEntity.ok("RSVP is completed");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error RSVPing the event, please try again later");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/deleteRsvpAsParticipant")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRsvpAsParticipant(@RequestBody EventParticipantDto rsvpToBeRemoved) {
        try {
            boolean deleteStatus = eventService.deleteRsvpAsParticipant(rsvpToBeRemoved);
            if(deleteStatus) {
                return ResponseEntity.ok("RSVP is removed");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error removing the RSVP for the event, are you enrolled to the event?");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
