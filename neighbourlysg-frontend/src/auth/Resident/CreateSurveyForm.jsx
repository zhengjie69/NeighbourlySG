import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button, Form, Card, InputGroup, FormControl, OverlayTrigger, Tooltip } from "react-bootstrap";
import { FaTrash } from 'react-icons/fa';
import { useLocation, useNavigate } from "react-router-dom";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";
import axiosInstance from '../Utils/axiosConfig'

const CreateSurveyPage = () => {
  const [surveyTitle, setSurveyTitle] = useState("");
  const [surveyDescription, setSurveyDescription] = useState("");
  const [questions, setQuestions] = useState([
    { id: 1, questionText: "", questionType: "shortAnswer", options: [] },
  ]);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  const [isTitleValid, setIsTitleValid] = useState(true); // Track title validity
  const [isDescriptionValid, setIsDescriptionValid] = useState(true); // Track description validity
  const [isQuestionListValid, setIsQuestionListValid] = useState(true); // New state for question list validation
  const [questionErrors, setQuestionErrors] = useState({}); // Track errors for each question
  const [isCreating, setIsCreating] = useState(true); // Track description validity

  const location = useLocation();
  const surveyId = location.state?.surveyId; // Get surveyId from state
  const navigate = useNavigate();  // Initialize navigate
  

 // Function to fetch existing survey data based on surveyId
  const fetchSurvey = async (id) => {
    try {
      const response = await axiosInstance.get(`/SurveyService/getSurvey/${id}`);

      if (response.data) {
        const surveyData = response.data;  // Directly access response.data with Axios
        setSurveyTitle(surveyData.title);
        setSurveyDescription(surveyData.description);
        
        // Map through questions while maintaining original IDs
        setQuestions(surveyData.questions.map(q => ({
          id: q.id,  // Maintain original question ID
          questionText: q.questionText,
          questionType: q.questionType,
          options: q.options || [],
          isNew: false, // Mark as not new
        })));
        setIsCreating(false);
      } else {
        console.error('Failed to fetch survey data.');
        setMessage("Failed to fetch survey data.");
        setIsError(true);
      }
    } catch (error) {
      console.error('Error fetching survey:', error);
      setMessage("An error occurred while fetching the survey.");
      setIsError(true);
    }
  };


  useEffect(() => {
    if (surveyId) {
      fetchSurvey(surveyId); // Fetch the survey data if editing
    }
  }, [surveyId]);

  const handleQuestionTextChange = (id, text) => {

    setIsQuestionListValid(true); // No valid questions
    setIsError(false);

    setQuestions(questions.map(q => (q.id === id ? { ...q, questionText: text } : q)));
  
    // Clear the error for this specific question as soon as the user starts typing
    setQuestionErrors(prevErrors => ({
      ...prevErrors,
      [id]: !text.trim() // If the text is empty or just spaces, set error to true, otherwise false
    }));
  };
  

  const handleQuestionTypeChange = (id, type) => {
    setQuestions(questions.map(q => (q.id === id ? { ...q, questionType: type, options: type === "multipleChoice" ? [""] : [] } : q)));
  };

  const handleOptionChange = (questionId, optionIndex, value) => {
    setQuestions(questions.map(q => (q.id === questionId ? { ...q, options: q.options.map((opt, idx) => (idx === optionIndex ? value : opt)) } : q)));
  };
  
  
  const removeOption = (questionId, optionIndex) => {
    setQuestions(questions.map(q => 
      q.id === questionId 
        ? { 
            ...q, 
            options: q.options.filter((_, idx) => idx !== optionIndex)
          } 
        : q
    ));
  };
  
  

  const addQuestion = () => {
    setQuestions([
      ...questions,
      { id: questions.length + 1, questionText: "", questionType: "shortAnswer", options: [], isNew: true },
    ]);
  };
  

  const addOption = (questionId) => {
    setQuestions(questions.map(q => (q.id === questionId ? { ...q, options: [...q.options, ""] } : q)));
  };
  

  const deleteQuestion = (id) => {
    setQuestions(questions.filter((q) => q.id !== id));
  };

  

  
  const handleSubmit = async () => {
    // Validation logic
    if (!surveyTitle.trim()) {
      setIsError(true);
      setIsTitleValid(false); 
      return;
    }else {
      setIsTitleValid(true); 
    }

    if (!surveyDescription.trim()) {
      setIsDescriptionValid(false);
      setIsError(true);
      return;
    }else {
      setIsDescriptionValid(true); 
    }

    // Validation logic for questions
    const questionErrorsTemp = {};
    let isAnyQuestionFilled = false; // Flag to check if at least one question has text
    questions.forEach(q => {
      if (!q.questionText.trim()) {
        questionErrorsTemp[q.id] = true;// Mark question as invalid if empty
        setIsError(true); 
      } else {
        isAnyQuestionFilled = true; // Mark that at least one question is filled
      }
    });

    // Set validation for question list
    if (!isAnyQuestionFilled) {
      setIsQuestionListValid(false); // No valid questions
      setIsError(true);
      // Check if there are any question errors
      if (Object.keys(questionErrorsTemp).length > 0) {
        setQuestionErrors(questionErrorsTemp); // Set the question errors
        return; // Stop submission if there are question errors
      }
      return;
    } else {
      setIsQuestionListValid(true); // At least one question is valid
    }

    // Check if there are any question errors
  if (Object.keys(questionErrorsTemp).length > 0) {
    setQuestionErrors(questionErrorsTemp); // Set the question errors
    return; // Stop submission if there are question errors
  }

    try {
      const surveyData = {
        id: surveyId, // Include the ID for update
        title: surveyTitle,
        description: surveyDescription,
        // Include question IDs only if updating
        questions: questions.map(q => ({
          ...(surveyId ? { id: q.id } : {}), // Include ID only if updating
          questionText: q.questionText,
          questionType: q.questionType,
          options: q.options,
        })),
      };

      let response;
      if (surveyId) {
        // If surveyId exists, it's an update, so use PUT
        response = await axiosInstance.put(`/SurveyService/updateSurvey`, surveyData);
      } else {
        // If surveyId does not exist, it's a create, so use POST
        response = await axiosInstance.post(`/SurveyService/createSurvey`, surveyData);
      }

      

      if (response.data) {
        setMessage(surveyId ? "Survey updated successfully!" : "Survey created successfully!");
        setIsError(false);
        // Reset form
        setSurveyTitle("");
        setSurveyDescription("");
        setQuestions([{ id: 1, questionText: "", questionType: "shortAnswer", options: [] }]);
      } else {
        const errorData = await response.json();
        setMessage(errorData.errorDetails || "Failed to save survey.");
        setIsError(true);
      }
    } catch (error) {
      console.error('Submission error:', error);
      setMessage("Error submitting survey.");
      setIsError(true);
    }
  };

  return (
    <div
      className="d-flex flex-column align-items-center vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundColor: "rgba(0, 0, 0, 0.5)",
        backdropFilter: "blur(5px)",
      }}
    >
      <div
        className="card p-5 mt-5 mb-4"
        style={{
          width: "800px",
          boxShadow: "0 12px 24px rgba(0, 0, 0, 0.2)",
          borderRadius: "16px",
          backgroundColor: "rgba(255, 255, 255, 0.9)",
        }}
      >
        <h3 className="text-center mb-4" style={{ fontWeight: "700", fontSize: "1.8rem", color: "#333" }}>
          {surveyId ? "Update Survey" : "Create New Survey"}
        </h3>
         {/* Back Button positioned at the top-right */}
      {/* <Button
        onClick={() => navigate(-1)}
        style={{
          position: 'absolute',
          top: '20px',
          right: '20px',
          zIndex: '1000',
          backgroundColor: '#fff',
          color: '#333',
          borderRadius: '50%',
          border: 'none',
          width: '50px',
          height: '50px',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          boxShadow: '0px 2px 5px rgba(0,0,0,0.2)',
        }}
      >
        ←
      </Button> */}

        <Form>
          <Form.Group className="mb-4">
            <Form.Label>Survey Title</Form.Label>
            <FormControl
              placeholder="Enter survey title"
              value={surveyTitle}
              onChange={(e) => {
                setSurveyTitle(e.target.value);
                setIsTitleValid(true); // Set title validity to true
              }}
              style={{ borderColor: isTitleValid ? "" : "red" }} // Conditional styling
            />
            {!isTitleValid && (
              <div style={{ color: "red", fontSize: "0.875rem" }}>
                Survey title is required.
              </div>
            )}
          </Form.Group>

          <Form.Group className="mb-4">
            <Form.Label>Survey Description</Form.Label>
            <FormControl
              as="textarea"
              rows={3}
              placeholder="Enter survey description"
              value={surveyDescription}
              onChange={(e) => {setSurveyDescription(e.target.value); setIsDescriptionValid(true);}}
              style={{ borderColor: isDescriptionValid ? "" : "red" }} // Conditional styling
            />
            {!isDescriptionValid && (
              <div style={{ color: "red", fontSize: "0.875rem" }}>
                Survey description is required.
              </div>
            )}
          </Form.Group>

          {questions.map((question, index) => (
            <Card key={question.id} className="mb-3">
              <Card.Body>
              <div className="d-flex justify-content-between align-items-center mb-3">
                <Form.Label>Question {index + 1}</Form.Label>
                {(question.isNew || isCreating) &&  (
                  <OverlayTrigger
                    placement="top"
                    overlay={<Tooltip>Delete Question</Tooltip>}
                  >
                    <Button
                      variant="outline-danger"
                      className="ms-2"
                      onClick={() => { deleteQuestion(question.id); }}
                    >
                      <FaTrash />
                    </Button>
                  </OverlayTrigger>
                )}
               </div>
                <Form.Group className="mb-3">
                  <InputGroup>
                    <FormControl
                      placeholder="Enter question"
                      value={question.questionText}
                      onChange={(e) => handleQuestionTextChange(question.id, e.target.value)}
                      style={{ borderColor: questionErrors[question.id] ? "red" : "" }} // Red border for invalid input
                    />
                    <Form.Select
                      value={question.questionType}
                      onChange={(e) => handleQuestionTypeChange(question.id, e.target.value)}
                    >
                      <option value="shortAnswer">Short Answer</option>
                      <option value="paragraph">Paragraph</option>
                      <option value="multipleChoice">Multiple Choice</option>
                      <option value="rating">Rating (1-5)</option>
                    </Form.Select>
                  </InputGroup>
                  {questionErrors[question.id] && (
                    <div style={{ color: "red", fontSize: "0.875rem" }}>
                      The question is required.
                    </div>
                  )}
                </Form.Group>

                {question.questionType === "multipleChoice" && (
                  <div className="mb-3">
                   {question.options.map((option, idx) => (
                      <InputGroup className="mb-2" key={idx}>
                        <InputGroup.Text>{String.fromCharCode(65 + idx)}</InputGroup.Text>
                        <FormControl
                          placeholder="Option"
                          value={option}
                          onChange={(e) => handleOptionChange(question.id, idx, e.target.value)}
                        />
                        {option.isNew && (
                        <OverlayTrigger
                          placement="top"
                          overlay={<Tooltip>Delete Option</Tooltip>}
                          >
                          <Button 
                            variant="outline-danger" 
                            onClick={() => removeOption(question.id, idx)}
                          >
                            <FaTrash />
                          </Button>
                          </OverlayTrigger>
                        )}
                      </InputGroup>
                    ))}
                    <Button variant="link" onClick={() => addOption(question.id)}>
                      + Add Option
                    </Button>
                  </div>
                )}

                {question.questionType === "rating" && (
                  <div className="mb-3">
                    <Form.Label>Rating</Form.Label>
                    <div style={{ display: "flex", justifyContent: "space-between", maxWidth: "200px" }}>
                      {[1, 2, 3, 4, 5].map((star) => (
                        <span key={star} style={{ fontSize: "1.5rem", color: "#ffc107" }}>
                          ★
                        </span>
                      ))}
                    </div>
                  </div>
                )}
              </Card.Body>
            </Card>
          ))}

          {!isQuestionListValid && (
            <div style={{ color: "red", fontSize: "0.875rem", marginBottom: "1rem" }}>
              At least one question is required.
            </div>
          )}

          <Button variant="secondary" className="mb-3" onClick={addQuestion}>
            + Add Question
          </Button>

          <Button variant="primary" className="w-100" onClick={handleSubmit}>
            {surveyId ? "Update Survey" : "Create Survey"}
          </Button>
        </Form>

        {message && (
          <div className={`alert ${isError ? 'alert-danger' : 'alert-success'} mt-4`} role="alert">
            {message}
          </div>
        )}
      </div>

      <footer className="bg-dark text-white text-center py-3 mt-auto" style={{ position: "relative", bottom: 0, width: "100%" }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default CreateSurveyPage;