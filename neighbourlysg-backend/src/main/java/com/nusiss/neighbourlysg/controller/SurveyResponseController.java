package com.nusiss.neighbourlysg.controller;


import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.SurveyResponse;
import com.nusiss.neighbourlysg.service.SurveyResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/SurveyResponseService")
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;

    public SurveyResponseController(SurveyResponseService surveyResponseService) {
        this.surveyResponseService = surveyResponseService;
    }

    @PostMapping("/submitSurveyResponse")
    public ResponseEntity<SurveyResponseDTO> submitSurveyResponse(@RequestBody SurveyResponseDTO surveyResponseDTO) {
        try {
            SurveyResponseDTO result = surveyResponseService.saveSurveyResponse(surveyResponseDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getSurveyResponses/{surveyId}")
    public ResponseEntity<List<SurveyResponseDTO>> getSurveyResponses(@PathVariable Long surveyId) {
        try {
            List<SurveyResponseDTO> responseDtos = surveyResponseService.getSurveyResponses(surveyId);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getUserResponses/{surveyId}/{userId}")
    public ResponseEntity<SurveyResponseDTO> getUserResponses(@PathVariable Long surveyId, @PathVariable Long userId) {
        try {
            // Call the service method to retrieve the user's responses
            SurveyResponseDTO surveyResponseDTO = surveyResponseService.getUserResponses(surveyId, userId);
            return ResponseEntity.ok(surveyResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}

