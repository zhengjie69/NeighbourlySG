import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button, Form, Card, InputGroup, FormControl } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";

const CreateSurveyPage = () => {
  const [surveyTitle, setSurveyTitle] = useState("");
  const [surveyDescription, setSurveyDescription] = useState("");
  const [questions, setQuestions] = useState([
    { id: 1, questionText: "", questionType: "shortAnswer", options: [] },
  ]);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);
  
  const location = useLocation();
  const surveyId = location.state?.surveyId; // Get surveyId from state

  // Function to fetch existing survey data based on surveyId
  const fetchSurvey = async (id) => {
    //const token = sessionStorage.getItem('accessToken');
    const response = await fetch(`http://localhost:8080/api/SurveyService/getSurvey/${id}`, {
      method: 'GET',
      headers: {
        //'Authorization': `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const surveyData = await response.json();
      setSurveyTitle(surveyData.title);
      setSurveyDescription(surveyData.description);
      // Map through questions while maintaining original IDs
      setQuestions(surveyData.questions.map(q => ({
        id: q.id,  // Maintain original question ID
        questionText: q.questionText,
        questionType: q.questionType,
        options: q.options || [],
      })));
    } else {
      console.error('Failed to fetch survey data.');
      setMessage("Failed to fetch survey data.");
      setIsError(true);
    }
  };

  useEffect(() => {
    if (surveyId) {
      fetchSurvey(surveyId); // Fetch the survey data if editing
    }
  }, [surveyId]);

  const handleQuestionTextChange = (id, text) => {
    setQuestions(questions.map(q => (q.id === id ? { ...q, questionText: text } : q)));
  };

  const handleQuestionTypeChange = (id, type) => {
    setQuestions(questions.map(q => (q.id === id ? { ...q, questionType: type, options: type === "multipleChoice" ? [""] : [] } : q)));
  };

  const handleOptionChange = (questionId, optionIndex, value) => {
    setQuestions(questions.map(q => (q.id === questionId ? { ...q, options: q.options.map((opt, idx) => (idx === optionIndex ? value : opt)) } : q)));
  };

  const addQuestion = () => {
    setQuestions([...questions, { id: questions.length + 1, questionText: "", questionType: "shortAnswer", options: [] }]);
  };

  const addOption = (questionId) => {
    setQuestions(questions.map(q => (q.id === questionId ? { ...q, options: [...q.options, ""] } : q)));
  };

  const handleSubmit = async () => {
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

      const response = await fetch(`http://localhost:8080/api/SurveyService/${surveyId ? 'updateSurvey' : 'createSurvey'}`, { // Check if it's an update or create
        method: surveyId ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json',
          //'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(surveyData),
      });

      if (response.ok) {
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

        <Form>
          <Form.Group className="mb-4">
            <Form.Label>Survey Title</Form.Label>
            <FormControl
              placeholder="Enter survey title"
              value={surveyTitle}
              onChange={(e) => setSurveyTitle(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="mb-4">
            <Form.Label>Survey Description</Form.Label>
            <FormControl
              as="textarea"
              rows={3}
              placeholder="Enter survey description"
              value={surveyDescription}
              onChange={(e) => setSurveyDescription(e.target.value)}
            />
          </Form.Group>

          {questions.map((question, index) => (
            <Card key={question.id} className="mb-3">
              <Card.Body>
                <Form.Group className="mb-3">
                  <Form.Label>Question {index + 1}</Form.Label>
                  <InputGroup>
                    <FormControl
                      placeholder="Enter your question here"
                      value={question.questionText}
                      onChange={(e) => handleQuestionTextChange(question.id, e.target.value)}
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
        <p>
          <Link to="/contact" className="text-white">
            Contact Support
          </Link>
        </p>
      </footer>
    </div>
  );
};

export default CreateSurveyPage;
