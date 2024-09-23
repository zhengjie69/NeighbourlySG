package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SurveyControllerTest {

    @InjectMocks
    private SurveyController surveyController;

    @Mock
    private SurveyService surveyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO();
        when(surveyService.createSurvey(any(SurveyDTO.class))).thenReturn(surveyDTO);

        ResponseEntity<SurveyDTO> response = surveyController.createSurvey(surveyDTO);

        verify(surveyService).createSurvey(surveyDTO);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() == surveyDTO;
    }

    @Test
    void testGetAllSurveys() {
        List<SurveyDTO> surveys = new ArrayList<>();
        when(surveyService.getAllSurveys()).thenReturn(surveys);

        ResponseEntity<List<SurveyDTO>> response = surveyController.getAllSurveys();

        verify(surveyService).getAllSurveys();
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() == surveys;
    }

    @Test
    void testGetAllSurveys2() {
        List<SurveyDTO> surveys = new ArrayList<>();
        when(surveyService.getAllSurveys()).thenReturn(surveys);

        ResponseEntity<List<SurveyDTO>> response = surveyController.getAllSurveys2();

        verify(surveyService).getAllSurveys();
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() == surveys;
    }

    @Test
    void testGetSurveyByIdFound() {
        Long surveyId = 1L;
        SurveyDTO surveyDTO = new SurveyDTO();
        when(surveyService.getSurveyById(surveyId)).thenReturn(Optional.of(surveyDTO));

        ResponseEntity<SurveyDTO> response = surveyController.getSurveyById(surveyId);

        verify(surveyService).getSurveyById(surveyId);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() == surveyDTO;
    }

    @Test
    void testGetSurveyByIdNotFound() {
        Long surveyId = 1L;
        when(surveyService.getSurveyById(surveyId)).thenReturn(Optional.empty());

        ResponseEntity<SurveyDTO> response = surveyController.getSurveyById(surveyId);

        verify(surveyService).getSurveyById(surveyId);
        assert response.getStatusCode().is4xxClientError();
    }
}
