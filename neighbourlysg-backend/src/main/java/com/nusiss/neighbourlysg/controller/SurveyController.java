package com.nusiss.neighbourlysg.controller;


import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.exception.ProfileNotFoundException;
import com.nusiss.neighbourlysg.exception.SurveyNotFoundException;
import com.nusiss.neighbourlysg.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/SurveyService")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/createSurvey")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) {
        return ResponseEntity.ok(surveyService.createSurvey(surveyDTO));
    }

    @GetMapping("/getAllSurveys")
    public ResponseEntity<List<SurveyDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/getAllSurveys2")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<List<SurveyDTO>> getAllSurveys2() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/getSurvey/{id}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        return surveyService.getSurveyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateSurvey")
    public ResponseEntity<SurveyDTO> updateSurvey(@RequestBody SurveyDTO updatedSurvey) {

        try {
            SurveyDTO surveyDTO = surveyService.updateSurvey(updatedSurvey);
            return ResponseEntity.ok(surveyDTO);
        }  catch (Exception e) {
            // Handle other exceptions as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Delete Profile REST API
    @DeleteMapping("/survey/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable("id") Long id) {
        try {
            surveyService.deleteSurveyById(id);
            return ResponseEntity.ok("Profile deleted successfully!");
        } catch (SurveyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle other exceptions as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/submitSurveyResponses")
    public ResponseEntity<String> submitSurveyResponses(@RequestBody SurveyResponseDTO response) {
        surveyService.saveSurveyResponse(response);
        return ResponseEntity.ok("Responses submitted successfully!");
    }

    @GetMapping("/getSurveyResponses/{surveyId}")
    public ResponseEntity<List<SurveyResponseDTO>> getSurveyResponses(@PathVariable Long surveyId) {
        List<SurveyResponseDTO> responses = surveyService.getSurveyResponses(surveyId);
        return ResponseEntity.ok(responses);
    }

}
