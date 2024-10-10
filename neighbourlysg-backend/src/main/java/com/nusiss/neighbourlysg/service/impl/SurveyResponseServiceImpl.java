package com.nusiss.neighbourlysg.service.impl;

import com.nusiss.neighbourlysg.dto.QuestionResponseDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.QuestionResponse;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.entity.SurveyResponse;
import com.nusiss.neighbourlysg.mapper.SurveyResponseMapper;
import com.nusiss.neighbourlysg.repository.SurveyRepository;
import com.nusiss.neighbourlysg.repository.SurveyResponseRepository;
import com.nusiss.neighbourlysg.service.SurveyResponseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyResponseServiceImpl implements SurveyResponseService {


    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyResponseMapper surveyResponseMapper;
    private final SurveyRepository surveyRepository;

    public SurveyResponseServiceImpl(SurveyResponseRepository surveyResponseRepository, SurveyResponseMapper surveyResponseMapper,
                                     SurveyRepository surveyRepository) {
        this.surveyResponseRepository = surveyResponseRepository;
        this.surveyResponseMapper = surveyResponseMapper;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public SurveyResponseDTO saveSurveyResponse(SurveyResponseDTO surveyResponseDto) {
        Survey survey = surveyRepository.findById(surveyResponseDto.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        SurveyResponse surveyResponse = surveyResponseMapper.toEntity(surveyResponseDto);
        surveyResponse.setSurvey(survey); // Set the survey
        surveyResponse.setUserId(surveyResponseDto.getUserId()); // Set the userId if needed

        // Retrieve existing question responses for the current survey response
        List<QuestionResponse> existingQuestionResponses = surveyResponse.getQuestionResponses();

        // Map to store question responses by question ID for easy access
        Map<Long, QuestionResponse> existingResponsesMap = existingQuestionResponses.stream()
                .collect(Collectors.toMap(qr -> qr.getQuestion().getId(), qr -> qr));

        // Loop through the new question responses
        for (QuestionResponseDTO questionResponseDto : surveyResponseDto.getResponses()) {
            Long questionId = questionResponseDto.getQuestionId();

            if (existingResponsesMap.containsKey(questionId)) {
                // Update existing question response
                QuestionResponse existingResponse = existingResponsesMap.get(questionId);
                existingResponse.setAnswer(questionResponseDto.getAnswer());
            } else {
                // Create new question response if it doesn't exist
                QuestionResponse questionResponse = new QuestionResponse();
                Question question = new Question();
                question.setId(questionId);
                questionResponse.setQuestion(question);
                questionResponse.setAnswer(questionResponseDto.getAnswer());

                // Add the question response to the survey response
                surveyResponse.addQuestionResponse(questionResponse); // This ensures the bidirectional link
            }
        }

        // Save the survey response, which will also cascade and save the updated question responses
        surveyResponseRepository.save(surveyResponse);

        SurveyResponseDTO dto = surveyResponseMapper.toDto(surveyResponse);
        List<QuestionResponseDTO> questionResponses = surveyResponse.getQuestionResponses().stream()
                    .map(qr -> new QuestionResponseDTO(qr.getQuestion().getId(), qr.getQuestion().getQuestionText(), qr.getAnswer()))  // Now includes questionText
                    .collect(Collectors.toList());
        dto.setResponses(questionResponses);
        return dto;

    }


    @Override
    public List<SurveyResponseDTO> getSurveyResponses(Long surveyId) {
        List<SurveyResponse> surveyResponses = surveyResponseRepository.findBySurveyId(surveyId);

        List<SurveyResponseDTO> responseDtos = surveyResponses.stream()
                .map(surveyResponse -> {
                    SurveyResponseDTO dto = new SurveyResponseDTO();
                    dto.setSurveyId(surveyResponse.getSurvey().getId());

                    // Map question responses including questionText
                    List<QuestionResponseDTO> questionResponses = surveyResponse.getQuestionResponses().stream()
                            .map(qr -> new QuestionResponseDTO(qr.getQuestion().getId(), qr.getQuestion().getQuestionText(), qr.getAnswer()))  // Now includes questionText
                            .collect(Collectors.toList());

                    dto.setResponses(questionResponses);
                    return dto;
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    @Override
    public SurveyResponseDTO getUserResponses(Long surveyId, Long userId) {
        Optional<SurveyResponse> existingResponse = surveyResponseRepository.findBySurveyIdAndUserId(surveyId, userId);
        SurveyResponseDTO dto = new SurveyResponseDTO();
       if(existingResponse.isPresent()) {
           SurveyResponse surveyResponse = existingResponse.get();
           dto.setId(surveyResponse.getId());
           dto.setSurveyId(surveyResponse.getSurvey().getId());
           dto.setUserId(surveyResponse.getUserId());

           // Map question responses including questionText
           List<QuestionResponseDTO> questionResponses = surveyResponse.getQuestionResponses().stream()
                   .map(qr -> new QuestionResponseDTO(qr.getQuestion().getId(), qr.getQuestion().getQuestionText(), qr.getAnswer()))  // Now includes questionText
                   .collect(Collectors.toList());

           dto.setResponses(questionResponses);

       }

       return dto;
    }




}
