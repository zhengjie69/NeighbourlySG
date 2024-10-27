package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.config.ErrorMessagesConstants;
import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.exception.SurveyNotFoundException;
import com.nusiss.neighbourlysg.mapper.SurveyMapper;
import com.nusiss.neighbourlysg.repository.SurveyRepository;
import com.nusiss.neighbourlysg.service.SurveyResponseService;
import com.nusiss.neighbourlysg.service.SurveyService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {


    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;
    private final SurveyResponseService surveyResponseService;

    public SurveyServiceImpl(SurveyRepository surveyRepository, SurveyMapper surveyMapper,SurveyResponseService surveyResponseService) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
        this.surveyResponseService = surveyResponseService;
    }

    @Override
    public SurveyDTO createSurvey(SurveyDTO surveyDTO) {


        Survey survey = surveyMapper.toEntity(surveyDTO);
        // Set the survey reference in each question
        for (Question question : survey.getQuestions()) {
            question.setSurvey(survey);
        }
        Survey savedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDto(savedSurvey);
    }

    @Override
    public List<SurveyDTO> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(surveyMapper::toDto).toList();
    }

    @Override
    public Optional<SurveyDTO> getSurveyById(Long id) {
        return surveyRepository.findById(id)
                .map(surveyMapper::toDto);
    }

    @Override
    public SurveyDTO updateSurvey(SurveyDTO surveyDTO) {

        Survey survey = surveyMapper.toEntity(surveyDTO);
        // Set the survey reference in each question
        for (Question question : survey.getQuestions()) {
            question.setSurvey(survey);
        }
        Survey savedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDto(savedSurvey);
    }

    @Override
    public void deleteSurveyById(Long surveyId) {

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException(ErrorMessagesConstants.SURVEY_NOT_FOUND + surveyId));

        //delete related survey responses
        surveyResponseService.deleteUserResponses(surveyId);

        surveyRepository.delete(survey); // Use delete() with the profile entity
    }

    @Override
    public void saveSurveyResponse(SurveyResponseDTO response) {
        // Logic to save the response to the database
        // You might want to create a new Response entity and save it using your repository
    }

    @Override
    public List<SurveyResponseDTO> getSurveyResponses(Long surveyId) {
        return Collections.emptyList();
    }



}
