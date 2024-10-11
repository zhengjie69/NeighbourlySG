package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.QuestionResponseDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.QuestionResponse;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.entity.SurveyResponse;
import com.nusiss.neighbourlysg.mapper.SurveyResponseMapper;
import com.nusiss.neighbourlysg.repository.SurveyRepository;
import com.nusiss.neighbourlysg.repository.SurveyResponseRepository;
import com.nusiss.neighbourlysg.service.impl.SurveyResponseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SurveyResponseServiceImplTest {

    @InjectMocks
    private SurveyResponseServiceImpl surveyResponseService;

    @Mock
    private SurveyResponseRepository surveyResponseRepository;

    @Mock
    private SurveyResponseMapper surveyResponseMapper;

    @Mock
    private SurveyRepository surveyRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveSurveyResponse_Success() {
        SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setSurveyId(1L);
        surveyResponseDTO.setUserId(1L);
        QuestionResponseDTO questionResponseDTO = new QuestionResponseDTO();
        questionResponseDTO.setQuestionId(1L);
        questionResponseDTO.setAnswer("Sample Answer");
        surveyResponseDTO.setResponses(Collections.singletonList(questionResponseDTO));

        Survey survey = new Survey();
        survey.setId(1L);

        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setSurvey(survey);
        surveyResponse.setUserId(1L);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(surveyResponseMapper.toEntity(surveyResponseDTO)).thenReturn(surveyResponse);
        when(surveyResponseRepository.save(any(SurveyResponse.class))).thenReturn(surveyResponse);
        when(surveyResponseMapper.toDto(surveyResponse)).thenReturn(surveyResponseDTO);

        SurveyResponseDTO result = surveyResponseService.saveSurveyResponse(surveyResponseDTO);

        assertNotNull(result);
        assertEquals(surveyResponseDTO.getUserId(), result.getUserId());
        verify(surveyRepository, times(1)).findById(1L);
        verify(surveyResponseMapper, times(1)).toEntity(surveyResponseDTO);
        verify(surveyResponseRepository, times(1)).save(surveyResponse);
        verify(surveyResponseMapper, times(1)).toDto(surveyResponse);
    }

    @Test
    public void testSaveSurveyResponse_SurveyNotFound() {
        SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setSurveyId(1L);
        surveyResponseDTO.setUserId(1L);

        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            surveyResponseService.saveSurveyResponse(surveyResponseDTO);
        });

        assertEquals("Survey not found", exception.getMessage());
        verify(surveyRepository, times(1)).findById(1L);
        verify(surveyResponseMapper, never()).toEntity(any());
        verify(surveyResponseRepository, never()).save(any());
    }

    @Test
    public void testGetSurveyResponses_Success() {
        Long surveyId = 1L;
        SurveyResponse surveyResponse = new SurveyResponse();
        Survey survey = new Survey();
        survey.setId(surveyId);
        surveyResponse.setSurvey(survey);

        QuestionResponse questionResponse = new QuestionResponse();
        Question question = new Question();
        question.setId(1L);
        question.setQuestionText("Sample Question");
        questionResponse.setQuestion(question);
        questionResponse.setAnswer("Sample Answer");
        surveyResponse.addQuestionResponse(questionResponse);

        when(surveyResponseRepository.findBySurveyId(surveyId)).thenReturn(Collections.singletonList(surveyResponse));
        when(surveyResponseMapper.toDto(surveyResponse)).thenReturn(new SurveyResponseDTO());

        List<SurveyResponseDTO> result = surveyResponseService.getSurveyResponses(surveyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(surveyResponseRepository, times(1)).findBySurveyId(surveyId);
    }

    @Test
    public void testGetUserResponses_Success() {
        Long surveyId = 1L;
        Long userId = 1L;
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setId(1L);
        surveyResponse.setUserId(userId);

        Survey survey = new Survey();
        survey.setId(surveyId);
        surveyResponse.setSurvey(survey);

        when(surveyResponseRepository.findBySurveyIdAndUserId(surveyId, userId)).thenReturn(Optional.of(surveyResponse));
        when(surveyResponseMapper.toDto(surveyResponse)).thenReturn(new SurveyResponseDTO());

        SurveyResponseDTO result = surveyResponseService.getUserResponses(surveyId, userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(surveyResponseRepository, times(1)).findBySurveyIdAndUserId(surveyId, userId);
    }

    @Test
    public void testGetUserResponses_NotFound() {
        Long surveyId = 1L;
        Long userId = 1L;

        when(surveyResponseRepository.findBySurveyIdAndUserId(surveyId, userId)).thenReturn(Optional.empty());

        SurveyResponseDTO result = surveyResponseService.getUserResponses(surveyId, userId);

        assertNotNull(result);
        assertNull(result.getUserId());
        verify(surveyResponseRepository, times(1)).findBySurveyIdAndUserId(surveyId, userId);
    }
}
