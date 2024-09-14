package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.mapper.SurveyMapper;
import com.nusiss.neighbourlysg.repository.SurveyRepository;
import com.nusiss.neighbourlysg.service.SurveyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {


    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;

    public SurveyServiceImpl(SurveyRepository surveyRepository, SurveyMapper surveyMapper) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
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


}
