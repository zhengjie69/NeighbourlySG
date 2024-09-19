package com.nusiss.neighbourlysg.controller;


import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.service.SurveyService;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) {
        return ResponseEntity.ok(surveyService.createSurvey(surveyDTO));
    }

    @GetMapping("/getAllSurveys")
    public ResponseEntity<List<SurveyDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/getAllSurveys2")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<SurveyDTO>> getAllSurveys2() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/survey/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANISER') or hasRole('ADMIN')")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        return surveyService.getSurveyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

