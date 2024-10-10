package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.service.SurveyResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SurveyResponseControllerTest {

    @InjectMocks
    private SurveyResponseController surveyResponseController;

    @Mock
    private SurveyResponseService surveyResponseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitSurveyResponse_Success() {
        SurveyResponseDTO inputDTO = new SurveyResponseDTO();
        SurveyResponseDTO savedDTO = new SurveyResponseDTO();

        when(surveyResponseService.saveSurveyResponse(any(SurveyResponseDTO.class))).thenReturn(savedDTO);

        ResponseEntity<SurveyResponseDTO> response = surveyResponseController.submitSurveyResponse(inputDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedDTO, response.getBody());
        verify(surveyResponseService, times(1)).saveSurveyResponse(inputDTO);
    }

    @Test
    public void testSubmitSurveyResponse_Failure() {
        SurveyResponseDTO inputDTO = new SurveyResponseDTO();

        when(surveyResponseService.saveSurveyResponse(any(SurveyResponseDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<SurveyResponseDTO> response = surveyResponseController.submitSurveyResponse(inputDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyResponseService, times(1)).saveSurveyResponse(inputDTO);
    }

    @Test
    public void testGetSurveyResponses_Success() {
        Long surveyId = 1L;
        List<SurveyResponseDTO> responseList = Collections.singletonList(new SurveyResponseDTO());

        when(surveyResponseService.getSurveyResponses(surveyId)).thenReturn(responseList);

        ResponseEntity<List<SurveyResponseDTO>> response = surveyResponseController.getSurveyResponses(surveyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseList, response.getBody());
        verify(surveyResponseService, times(1)).getSurveyResponses(surveyId);
    }

    @Test
    public void testGetSurveyResponses_Failure() {
        Long surveyId = 1L;

        when(surveyResponseService.getSurveyResponses(surveyId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<SurveyResponseDTO>> response = surveyResponseController.getSurveyResponses(surveyId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyResponseService, times(1)).getSurveyResponses(surveyId);
    }

    @Test
    public void testGetUserResponses_Success() {
        Long surveyId = 1L;
        Long userId = 2L;
        SurveyResponseDTO responseDTO = new SurveyResponseDTO();

        when(surveyResponseService.getUserResponses(surveyId, userId)).thenReturn(responseDTO);

        ResponseEntity<SurveyResponseDTO> response = surveyResponseController.getUserResponses(surveyId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(surveyResponseService, times(1)).getUserResponses(surveyId, userId);
    }

    @Test
    public void testGetUserResponses_Failure() {
        Long surveyId = 1L;
        Long userId = 2L;

        when(surveyResponseService.getUserResponses(surveyId, userId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<SurveyResponseDTO> response = surveyResponseController.getUserResponses(surveyId, userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyResponseService, times(1)).getUserResponses(surveyId, userId);
    }





}
