package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.config.ErrorMessagesConstants;
import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.exception.SurveyNotFoundException;
import com.nusiss.neighbourlysg.mapper.SurveyMapper;
import com.nusiss.neighbourlysg.repository.SurveyRepository;
import com.nusiss.neighbourlysg.service.impl.SurveyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurveyServiceImplTest {

	@Mock
	private SurveyRepository surveyRepository;

	@Mock
	private SurveyMapper surveyMapper;

	@Mock
	private SurveyResponseService surveyResponseService;

	@InjectMocks
	private SurveyServiceImpl surveyService;

	private SurveyDTO surveyDTO;
	private Survey survey;
	private Question question;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		// Initialize test data
		surveyDTO = new SurveyDTO();
		survey = new Survey();
		question = new Question();

		// Set up relationships for questions if needed
		survey.setQuestions(Arrays.asList(question));
		question.setSurvey(survey);
	}

	@Test
	public void testCreateSurvey() {
		when(surveyMapper.toEntity(surveyDTO)).thenReturn(survey);
		when(surveyRepository.save(survey)).thenReturn(survey);
		when(surveyMapper.toDto(survey)).thenReturn(surveyDTO);

		SurveyDTO createdSurvey = surveyService.createSurvey(surveyDTO);

		assertNotNull(createdSurvey);
		verify(surveyMapper).toEntity(surveyDTO);
		verify(surveyRepository).save(survey);
		verify(surveyMapper).toDto(survey);
	}

	@Test
	public void testGetAllSurveys() {
		Survey survey1 = new Survey();
		Survey survey2 = new Survey();
		List<Survey> surveys = Arrays.asList(survey1, survey2);
		List<SurveyDTO> surveyDTOs = Arrays.asList(new SurveyDTO(), new SurveyDTO());

		when(surveyRepository.findAllByOrderByIdDesc()).thenReturn(surveys);
		when(surveyMapper.toDto(survey1)).thenReturn(surveyDTOs.get(0));
		when(surveyMapper.toDto(survey2)).thenReturn(surveyDTOs.get(1));

		List<SurveyDTO> result = surveyService.getAllSurveys();

		assertEquals(2, result.size());
		verify(surveyRepository).findAllByOrderByIdDesc();
	}

	@Test
	public void testGetSurveyById_Success() {
		Long surveyId = 1L;
		when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
		when(surveyMapper.toDto(survey)).thenReturn(surveyDTO);

		Optional<SurveyDTO> result = surveyService.getSurveyById(surveyId);

		assertTrue(result.isPresent());
		assertEquals(surveyDTO, result.get());
		verify(surveyRepository).findById(surveyId);
	}

	@Test
	public void testGetSurveyById_NotFound() {
		Long surveyId = 1L;
		when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

		Optional<SurveyDTO> result = surveyService.getSurveyById(surveyId);

		assertFalse(result.isPresent());
		verify(surveyRepository).findById(surveyId);
	}

	@Test
	public void testUpdateSurvey() {
		when(surveyMapper.toEntity(surveyDTO)).thenReturn(survey);
		when(surveyRepository.save(survey)).thenReturn(survey);
		when(surveyMapper.toDto(survey)).thenReturn(surveyDTO);

		SurveyDTO updatedSurvey = surveyService.updateSurvey(surveyDTO);

		assertNotNull(updatedSurvey);
		verify(surveyMapper).toEntity(surveyDTO);
		verify(surveyRepository).save(survey);
		verify(surveyMapper).toDto(survey);
	}

	@Test
	public void testDeleteSurveyById_WhenSurveyExists() {
		// Arrange
		Long surveyId = 1L;
		Survey survey = new Survey(); // Assuming Survey has a default constructor

		when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

		// Act
		surveyService.deleteSurveyById(surveyId);

		// Assert
		verify(surveyResponseService, times(1)).deleteUserResponses(surveyId); // Ensure responses are deleted
		verify(surveyRepository, times(1)).delete(survey); // Ensure the survey is deleted
	}

	@Test
	public void testDeleteSurveyById_WhenSurveyDoesNotExist() {
		// Arrange
		Long surveyId = 2L;

		when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());

		// Act & Assert
		SurveyNotFoundException exception = assertThrows(SurveyNotFoundException.class, () -> {
			surveyService.deleteSurveyById(surveyId);
		});

		assertEquals(ErrorMessagesConstants.SURVEY_NOT_FOUND + surveyId, exception.getMessage()); // Verify exception message
		verify(surveyResponseService, never()).deleteUserResponses(anyLong()); // Ensure responses are not deleted
		verify(surveyRepository, never()).delete(any(Survey.class)); // Ensure survey is not deleted
	}

	@Test
	public void testSaveSurveyResponse() {
		SurveyResponseDTO responseDTO = new SurveyResponseDTO();
		// Assuming saveSurveyResponse is implemented, write necessary code to test it
		// For now, we will just verify it doesn't throw an error
		assertDoesNotThrow(() -> surveyService.saveSurveyResponse(responseDTO));
	}

	@Test
	public void testGetSurveyResponses() {
		Long surveyId = 1L;
		List<SurveyResponseDTO> responses = Collections.emptyList();

		// Assuming getSurveyResponses is implemented, we just return an empty list for now
		List<SurveyResponseDTO> result = surveyService.getSurveyResponses(surveyId);

		assertEquals(responses, result);
		// Verify if the method calls to fetch responses if implemented
		// verify(surveyRepository).findResponsesBySurveyId(surveyId); // Example verification if method existed
	}
}
