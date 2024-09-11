package com.nusiss.neighbourlysg.service;


import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.mapper.SurveyMapper;
import com.nusiss.neighbourlysg.repository.SurveyRepository;
import com.nusiss.neighbourlysg.service.impl.SurveyServiceImpl;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.MasterEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class SurveyServiceImplTest {

	@Mock
	private SurveyRepository surveyRepository;

	@Autowired
	private SurveyMapper surveyMapper;

	private SurveyService surveyService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		surveyService = new SurveyServiceImpl(surveyRepository, surveyMapper);
	}

	@Test
	void createSurvey_shouldReturnSurveyDTO() {
		// Arrange
		SurveyDTO surveyDTO = MasterDTOTestUtil.createSurveyDTO();
		Survey savedSurvey = MasterEntityTestUtil.createSurvey();

		when(surveyRepository.save(any())).thenReturn(savedSurvey);
		// Act
		SurveyDTO result = surveyService.createSurvey(surveyDTO);

		// Assert
		assertEquals(result.getId(),surveyDTO.getId());
	}

	@Test
	void getAllSurveys_shouldReturnListOfSurveyDTOs() {
		// Arrange
		List<Survey> surveys = List.of(MasterEntityTestUtil.createSurvey());

		when(surveyRepository.findAll()).thenReturn(surveys);

		// Act
		List<SurveyDTO> result = surveyService.getAllSurveys();

		// Assert
		assertEquals(1, result.size());
	}

	@Test
	void getSurveyById_shouldReturnSurveyDTO() {
		// Arrange
		Survey survey = MasterEntityTestUtil.createSurvey();

		when(surveyRepository.findById(any())).thenReturn(Optional.of(survey));


		// Act
		Optional<SurveyDTO> result = surveyService.getSurveyById(1L);

		// Assert
		assertTrue(result.isPresent());
	}

	@Test
	void getSurveyById_shouldReturnEmptyOptional() {
		// Arrange
		when(surveyRepository.findById(any())).thenReturn(Optional.empty());

		// Act
		Optional<SurveyDTO> result = surveyService.getSurveyById(1L);

		// Assert
		assertFalse(result.isPresent());
	}
}
