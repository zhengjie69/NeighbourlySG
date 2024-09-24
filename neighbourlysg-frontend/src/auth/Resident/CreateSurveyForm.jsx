/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button, Form, Card, InputGroup, FormControl } from "react-bootstrap";
import { Link } from "react-router-dom";
import SGLogo from "../../assets/SGLogo.avif";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";

const CreateSurveyPage = () => {
  const [surveyTitle, setSurveyTitle] = useState("");
  const [surveyDescription, setSurveyDescription] = useState("");
  const [questions, setQuestions] = useState([
    { id: 1, questionText: "", questionType: "shortAnswer", options: [] },
  ]);
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState(''); // State for success or error message
  const [isError, setIsError] = useState(false); // New state to track error vs success

  const handleQuestionTextChange = (id, text) => {
    setQuestions(
      questions.map((q) => (q.id === id ? { ...q, questionText: text } : q))
    );
  };

  const handleQuestionTypeChange = (id, type) => {
    setQuestions(
      questions.map((q) =>
        q.id === id
          ? {
              ...q,
              questionType: type,
              options: type === "multipleChoice" ? [""] : [],
            }
          : q
      )
    );
  };

  const handleOptionChange = (questionId, optionIndex, value) => {
    setQuestions(
      questions.map((q) =>
        q.id === questionId
          ? {
              ...q,
              options: q.options.map((opt, idx) =>
                idx === optionIndex ? value : opt
              ),
            }
          : q
      )
    );
  };

  const addQuestion = () => {
    setQuestions([
      ...questions,
      {
        id: questions.length + 1,
        questionText: "",
        questionType: "shortAnswer",
        options: [],
      },
    ]);
  };

  const addOption = (questionId) => {
    setQuestions(
      questions.map((q) =>
        q.id === questionId ? { ...q, options: [...q.options, ""] } : q
      )
    );
  };

  // Retrieve the JWT token from sessionStorage
  const token = sessionStorage.getItem('accessToken'); // Get the token


  const handleSubmit = async () => {
    try {
      const surveyData = {
        title: surveyTitle,
        description: surveyDescription,
        questions: questions.map((q) => ({
          questionText: q.questionText,
          questionType: q.questionType,
          options: q.options,
        })),
      };

      const response = await fetch('http://localhost:8080/api/SurveyService/createSurvey', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`, // Add the JWT token here
      },
        body: JSON.stringify(surveyData),
      });

      if (response.ok) {
        const result = await response.json();
        setMessage("Survey created successfully!");
        setIsError(false); // Clear error state
        setErrors({});
        setSurveyTitle("");
        setSurveyDescription("");
        setQuestions([
          { id: 1, questionText: "", questionType: "shortAnswer", options: [] },
        ]);

      } else {
        console.error('Registration error:', error); 
        console.error('Registration error msg:', error.response.data.errorDetails); 
        
        if (error.response && error.response.data && error.response.data.errorDetails) {
            setMessage(error.response.data.errorDetails); // Display the backend error message
        } else {
            setMessage("Failed to create survey."); // Fallback message
        }
        setIsError(true); // Set error state
      }
    } catch (error) {
      console.error('Registration error:', error); 
      
      if (error.response && error.response.data && error.response.data.errorDetails) {
          setMessage(error.response.data.errorDetails); // Display the backend error message
      } else {
          setMessage("Error submitting survey."); // Fallback message
      }
      setIsError(true); // Set error state
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

      {/* Survey Creation Form */}
      <div
        className="card p-5 mt-5 mb-4"
        style={{
          width: "800px",
          boxShadow: "0 12px 24px rgba(0, 0, 0, 0.2)",
          borderRadius: "16px",
          backgroundColor: "rgba(255, 255, 255, 0.9)",
        }}
      >
        <h3
          className="text-center mb-4"
          style={{ fontWeight: "700", fontSize: "1.8rem", color: "#333" }}
        >
          Create New Survey
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
                      onChange={(e) =>
                        handleQuestionTextChange(question.id, e.target.value)
                      }
                    />
                    <Form.Select
                      value={question.questionType}
                      onChange={(e) =>
                        handleQuestionTypeChange(question.id, e.target.value)
                      }
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
                        <InputGroup.Text>
                          {String.fromCharCode(65 + idx)}
                        </InputGroup.Text>
                        <FormControl
                          placeholder="Option"
                          value={option}
                          onChange={(e) =>
                            handleOptionChange(question.id, idx, e.target.value)
                          }
                        />
                      </InputGroup>
                    ))}
                    <Button
                      variant="link"
                      onClick={() => addOption(question.id)}
                    >
                      + Add Option
                    </Button>
                  </div>
                )}

                {question.questionType === "rating" && (
                  <div className="mb-3">
                    <Form.Label>Rating</Form.Label>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        maxWidth: "200px",
                      }}
                    >
                      {[1, 2, 3, 4, 5].map((star) => (
                        <span
                          key={star}
                          style={{ fontSize: "1.5rem", color: "#ffc107" }}
                        >
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
            Create Survey
          </Button>
        </Form>
        {message && (
                    <div className={`alert ${isError ? 'alert-danger' : 'alert-success'} mt-4`} role="alert">
                        {message}
                    </div>
        )}
      </div>

      {/* Footer */}
      {/* Footer */}
      <footer
        className="bg-dark text-white text-center py-3 mt-auto"
        style={{ position: "relative", bottom: 0, width: "100%" }}
      >
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
