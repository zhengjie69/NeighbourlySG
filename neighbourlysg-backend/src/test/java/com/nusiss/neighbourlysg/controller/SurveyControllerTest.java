package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.exception.SurveyNotFoundException;
import com.nusiss.neighbourlysg.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurveyControllerTest {

    @Mock
    private SurveyService surveyService;

    @InjectMocks
    private SurveyController surveyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO();
        when(surveyService.createSurvey(any(SurveyDTO.class))).thenReturn(surveyDTO);

        ResponseEntity<SurveyDTO> response = surveyController.createSurvey(surveyDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surveyDTO, response.getBody());
        verify(surveyService, times(1)).createSurvey(surveyDTO);
    }

    @Test
    public void testGetAllSurveys() {
        List<SurveyDTO> surveys = new ArrayList<>();
        when(surveyService.getAllSurveys()).thenReturn(surveys);

        ResponseEntity<List<SurveyDTO>> response = surveyController.getAllSurveys();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surveys, response.getBody());
        verify(surveyService, times(1)).getAllSurveys();
    }

    @Test
    public void testGetSurveyById_Success() {
        Long surveyId = 1L;
        SurveyDTO surveyDTO = new SurveyDTO();
        when(surveyService.getSurveyById(surveyId)).thenReturn(Optional.of(surveyDTO));

        ResponseEntity<SurveyDTO> response = surveyController.getSurveyById(surveyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surveyDTO, response.getBody());
        verify(surveyService, times(1)).getSurveyById(surveyId);
    }

    @Test
    public void testGetSurveyById_NotFound() {
        Long surveyId = 1L;
        when(surveyService.getSurveyById(surveyId)).thenReturn(Optional.empty());

        ResponseEntity<SurveyDTO> response = surveyController.getSurveyById(surveyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(surveyService, times(1)).getSurveyById(surveyId);
    }

    @Test
    public void testUpdateSurvey_Success() {
        SurveyDTO updatedSurvey = new SurveyDTO();
        when(surveyService.updateSurvey(any(SurveyDTO.class))).thenReturn(updatedSurvey);

        ResponseEntity<SurveyDTO> response = surveyController.updateSurvey(updatedSurvey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSurvey, response.getBody());
        verify(surveyService, times(1)).updateSurvey(updatedSurvey);
    }

    @Test
    public void testDeleteSurvey_Success() {
        Long surveyId = 1L;

        ResponseEntity<String> response = surveyController.deleteProfile(surveyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Survey deleted successfully!", response.getBody());
        verify(surveyService, times(1)).deleteSurveyById(surveyId);
    }

    @Test
    public void testDeleteSurvey_NotFound() {
        Long surveyId = 1L;
        doThrow(new SurveyNotFoundException("")).when(surveyService).deleteSurveyById(surveyId);

        ResponseEntity<String> response = surveyController.deleteProfile(surveyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(surveyService, times(1)).deleteSurveyById(surveyId);
    }

    @Test
    public void testUpdateSurvey_InternalServerError() {
        SurveyDTO updatedSurvey = new SurveyDTO();

        // Simulate an unexpected exception being thrown
        when(surveyService.updateSurvey(any(SurveyDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<SurveyDTO> response = surveyController.updateSurvey(updatedSurvey);

        // Assert that the response status is INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that the service method was called
        verify(surveyService, times(1)).updateSurvey(updatedSurvey);
    }

    @Test
    public void testDeleteSurvey_InternalServerError() {
        Long surveyId = 1L;

        // Simulate an unexpected exception being thrown
        doThrow(new RuntimeException("Unexpected error")).when(surveyService).deleteSurveyById(surveyId);

        ResponseEntity<String> response = surveyController.deleteProfile(surveyId);

        // Assert that the response status is INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that the service method was called
        verify(surveyService, times(1)).deleteSurveyById(surveyId);
    }

    @Test
    void testCreateSurvey_ExceptionHandling() {
        SurveyDTO surveyDTO = new SurveyDTO();
        when(surveyService.createSurvey(surveyDTO)).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<SurveyDTO> response = surveyController.createSurvey(surveyDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyService, times(1)).createSurvey(surveyDTO);
    }

    @Test
    void testGetAllSurveys_ExceptionHandling() {
        when(surveyService.getAllSurveys()).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<List<SurveyDTO>> response = surveyController.getAllSurveys();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyService, times(1)).getAllSurveys();
    }

    @Test
    void testGetSurveyById_ExceptionHandling() {
        Long surveyId = 1L;
        when(surveyService.getSurveyById(surveyId)).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<SurveyDTO> response = surveyController.getSurveyById(surveyId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyService, times(1)).getSurveyById(surveyId);
    }

    @Test
    void testUpdateSurvey_ExceptionHandling() {
        SurveyDTO updatedSurvey = new SurveyDTO();
        when(surveyService.updateSurvey(updatedSurvey)).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<SurveyDTO> response = surveyController.updateSurvey(updatedSurvey);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyService, times(1)).updateSurvey(updatedSurvey);
    }

    @Test
    void testDeleteSurvey_ExceptionHandling() {
        Long surveyId = 1L;
        doThrow(new RuntimeException("Internal error")).when(surveyService).deleteSurveyById(surveyId);

        ResponseEntity<String> response = surveyController.deleteProfile(surveyId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(surveyService, times(1)).deleteSurveyById(surveyId);
    }
}
