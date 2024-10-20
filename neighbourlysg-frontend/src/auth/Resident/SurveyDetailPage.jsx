// eslint-disable-next-line no-unused-vars
import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Form } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import axios from 'axios';

const SurveyDetailPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [survey, setSurvey] = useState(null);
  const [responses, setResponses] = useState({});
  const [existingResponseId, setExistingResponseId] = useState(null); // State for existing response ID
  const [isEditing, setIsEditing] = useState(true); // Allow editing by default for new users
  const [submissionSuccess, setSubmissionSuccess] = useState(false); // Track submission success
  const [hasExistingResponse, setHasExistingResponse] = useState(false); // Track if the user has an existing response

  // Get the survey passed through navigation
  useEffect(() => {
    const fetchedSurvey = location.state?.survey; // Get survey data from navigation state
    if (fetchedSurvey) {
      setSurvey(fetchedSurvey);
      fetchExistingResponses(fetchedSurvey.id); // Fetch existing responses for the survey
    } else {
      console.error('No survey data available');
      navigate('/survey-showcase'); // Navigate back if survey not found
    }
  }, [location.state, navigate]);

  // Function to fetch existing responses
  const fetchExistingResponses = async (surveyId) => {
    const userId = sessionStorage.getItem('userId'); // Get userId from sessionStorage
    try {
      const response = await axios.get(`http://neighbourlysg.ap-southeast-1.elasticbeanstalk.com/api/SurveyResponseService/getUserResponses${surveyId}/${userId}`);
      const existingResponse = response.data;

      if (existingResponse && existingResponse.responses.length > 0) {
        // If a previous response exists
        setExistingResponseId(existingResponse.id); // Set existing response ID
        setHasExistingResponse(true); // Set flag for existing response
        setIsEditing(false); // Disable editing initially since they have a response

        // Populate responses state with existing answers
        const populatedResponses = {};
        existingResponse.responses.forEach((response) => {
          populatedResponses[response.questionId] = response.answer;
        });
        setResponses(populatedResponses);
      } else {
        // No existing response, allow user to edit/submit a new response
        setIsEditing(true);
        setHasExistingResponse(false); // No existing response
      }
    } catch (error) {
      console.error('Error fetching existing responses:', error);
    }
  };

  const handleResponseChange = (questionId, value) => {
    setResponses((prevResponses) => ({
      ...prevResponses,
      [questionId]: value,
    }));
  };

  const handleSubmitResponses = async () => {
    const userId = sessionStorage.getItem('userId'); // Get userId from sessionStorage

    const responsePayload = {
      id: existingResponseId || null, // Include existing response ID or null for new response
      userId,
      surveyId: survey.id,
      responses: Object.entries(responses).map(([questionId, answer]) => ({
        questionId,
        answer,
      })),
    };

    try {
      await axios.post('http://neighbourlysg.ap-southeast-1.elasticbeanstalk.com/api/SurveyResponseService/submitSurveyResponse', responsePayload);
      console.log('Responses submitted:', responsePayload);
      setSubmissionSuccess(true); // Mark submission as successful
      setIsEditing(false); // Exit editing mode after submission
      setExistingResponseId(responsePayload.id); // Update the existing response ID
      setHasExistingResponse(false); // Reset this so the message doesn't show again
    } catch (error) {
      console.error('Error submitting responses:', error);
    }
  };

  const handleEditResponses = () => {
    setIsEditing(true); // Enable editing mode
    setSubmissionSuccess(false); // Reset submission success state
  };

  return (
    <div
      className="d-flex flex-column align-items-center vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        backdropFilter: 'blur(5px)',
        position: 'relative',
      }}
    >
      {/* Back Button positioned at the top-right */}
      <Button
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
      </Button>

      <div className="card p-5 mt-5 mb-4" style={{ width: '800px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.2)', borderRadius: '16px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
        <h3 style={{ fontWeight: '700', fontSize: '1.8rem', color: '#333' }}>{survey?.title}</h3>
        <p style={{ fontSize: '1rem', color: '#6c757d' }}>{survey?.description}</p>

        {/* Show success message after submission */}
        {submissionSuccess && (
          <div className="alert alert-success mb-4">
            Submitted responses successfully!
            <Button variant="link" onClick={handleEditResponses} style={{ marginLeft: '10px' }}>
              Edit Your Responses
            </Button>
          </div>
        )}

        {/* Display existing responses message if they exist and not editing */}
        {hasExistingResponse && !isEditing && (
          <div className="alert alert-info">
            You have submitted responses before. You can edit your answers below.
            <Button variant="link" onClick={() => setIsEditing(true)} style={{ marginLeft: '10px' }}>
              Edit Answers
            </Button>
          </div>
        )}

        {/* Render questions and answers */}
        {survey?.questions.map((question) => (
          <Form.Group key={question.id} controlId={`question-${question.id}`} className="mb-3">
            <Form.Label>{question.questionText}</Form.Label>
            {isEditing ? (
              // Editable input fields for answering
              <>
                {question.questionType === 'shortAnswer' && (
                  <Form.Control
                    type="text"
                    value={responses[question.id] || ''}
                    onChange={(e) => handleResponseChange(question.id, e.target.value)}
                    placeholder="Your answer"
                  />
                )}
                {question.questionType === 'paragraph' && (
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={responses[question.id] || ''}
                    onChange={(e) => handleResponseChange(question.id, e.target.value)}
                    placeholder="Your thoughts..."
                  />
                )}
                {question.questionType === 'multipleChoice' && (
                  <Form.Select
                    value={responses[question.id] || ''}
                    onChange={(e) => handleResponseChange(question.id, e.target.value)}
                  >
                    <option value="">Select an option</option>
                    {question.options.map((option, index) => (
                      <option key={index} value={option}>{option}</option>
                    ))}
                  </Form.Select>
                )}
                {question.questionType === 'rating' && (
                  <div style={{ display: 'flex', justifyContent: 'space-between', maxWidth: '120px', margin: 'auto' }}>
                    {[1, 2, 3, 4, 5].map((star) => (
                      <span
                        key={star}
                        style={{ fontSize: '1.5rem', cursor: 'pointer', color: star <= (responses[question.id] || 0) ? '#ffc107' : '#e4e5e9' }}
                        onClick={() => handleResponseChange(question.id, star)}
                      >
                        ★
                      </span>
                    ))}
                  </div>
                )}
              </>
            ) : (
              // Non-editable display of existing answers
              <>
                {question.questionType === 'rating' ? (
                  <div style={{ display: 'flex', justifyContent: 'space-between', maxWidth: '120px', margin: 'auto' }}>
                    {[1, 2, 3, 4, 5].map((star) => (
                      <span
                        key={star}
                        style={{ fontSize: '1.5rem', color: star <= (responses[question.id] || 0) ? '#ffc107' : '#e4e5e9' }}
                      >
                        ★
                      </span>
                    ))}
                  </div>
                ) : (
                  <Form.Control
                    type="text"
                    value={responses[question.id] || 'No response given'}
                    readOnly
                  />
                )}
              </>
            )}
          </Form.Group>
        ))}

        {isEditing && (
          <Button variant="primary" onClick={handleSubmitResponses} style={{ borderRadius: '10px', padding: '10px 20px', fontSize: '1rem' }}>
            Submit Responses
          </Button>
        )}
      </div>

      {/* Footer */}
      <footer className="bg-dark text-white text-center py-3 mt-auto" style={{ zIndex: 2, position: 'relative', width: '100%' }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default SurveyDetailPage;
