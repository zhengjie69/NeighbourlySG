package com.nusiss.neighbourlysg.dto;

public class QuestionResponseDTO {
    private Long questionId;
    private String questionText; // New field to hold the question text
    private String answer;

    // Constructor
    public QuestionResponseDTO(Long questionId, String questionText, String answer) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answer = answer;
    }

    public QuestionResponseDTO() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
