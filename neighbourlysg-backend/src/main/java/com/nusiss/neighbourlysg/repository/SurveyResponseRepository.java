package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

    List<SurveyResponse> findBySurveyId(Long id);

    Optional<SurveyResponse> findBySurveyIdAndUserId(Long surveyId, Long userId);
}

