// eslint-disable-next-line no-unused-vars
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg'; // Ensure the correct path to your background image
import axiosInstance from '../Utils/axiosConfig'

const SurveyResponsesPage = () => {
  const [userResponses, setUserResponses] = useState([]);
  const [viewMode, setViewMode] = useState('response'); // Default to "response by response"
  const navigate = useNavigate();
  const location = useLocation();

  // Survey data passed from the previous page
  const { survey } = location.state || {};

  useEffect(() => {
    if (survey) {
      // Fetch user responses for the survey from the backend
      axiosInstance.get(`/SurveyResponseService/getSurveyResponses/${survey.id}`)
        .then(response => {
          setUserResponses(response.data); // Set the responses in the state
        })
        .catch(error => {
          console.error('Error fetching responses:', error);
        });
    }
  }, [survey]);

  // Handle the toggle between 'question' and 'response' views
  const handleViewToggle = (mode) => {
    setViewMode(mode);
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
      }}
    >
      <div className="card p-5 mt-5 mb-4" style={{ width: '800px', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.2)', borderRadius: '16px', backgroundColor: 'rgba(255, 255, 255, 0.9)' }}>
        <h3 style={{ fontWeight: '700', fontSize: '1.8rem', color: '#333' }}>Responses for {survey?.title}</h3>
        <p style={{ fontSize: '1rem', color: '#6c757d' }}>{survey?.description}</p>

        {/* Toggle between response or question view */}
        <div className="d-flex justify-content-center mb-3">
          <Button
            variant={viewMode === 'response' ? 'primary' : 'secondary'}
            onClick={() => handleViewToggle('response')}
            className="me-2"
          >
            View by Response
          </Button>
          <Button
            variant={viewMode === 'question' ? 'primary' : 'secondary'}
            onClick={() => handleViewToggle('question')}
          >
            View by Question
          </Button>
        </div>

        {/* Conditionally render based on selected view mode */}
        {viewMode === 'response' ? (
          // "Response by response" view
          userResponses.length > 0 ? (
            <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
              {userResponses.map((response, index) => (
                <div
                  key={index}
                  className="list-group-item list-group-item-action flex-grow-1"
                  style={{
                    borderRadius: '10px',
                    border: '1px solid #ddd',
                    padding: '15px',
                    marginBottom: '20px',
                    backgroundColor: '#fff',
                    transition: 'background-color 0.3s ease',
                    cursor: 'pointer',
                    boxShadow: '0 12px 24px rgba(0, 0, 0, 0.1)',
                  }}
                >
                  <h5><strong>Response {index + 1}:</strong></h5>
                  {response.responses.map((questionResponse, idx) => (
                    <div key={idx} style={{ marginLeft: '20px' }}>
                      <strong>{questionResponse.questionText}:</strong> 
                      {parseInt(questionResponse.answer, 10) >= 1 && parseInt(questionResponse.answer, 10) <= 5 ? (
                        <div className="mb-3">
                          <div style={{ display: "flex", justifyContent: "space-between", maxWidth: "120px" }}>
                            {[1, 2, 3, 4, 5].map((star) => (
                              <span key={star} style={{ fontSize: "1.5rem", color: star <= questionResponse.answer ? "#ffc107" : "#e4e5e9" }}>
                                ★
                              </span>
                            ))}
                          </div>
                        </div>
                      ) : (
                        <p>{questionResponse.answer}</p>
                      )}
                    </div>
                  ))}
                </div>
              ))}
            </div>
          ) : (
            <p>No responses yet.</p>
          )
        ) : (
          // "Question by question" view
          userResponses.length > 0 ? (
            userResponses[0].responses.map((questionResponse, qIndex) => {
              const responseCount = userResponses.reduce((count, response) => {
                return response.responses.some(r => r.questionId === questionResponse.questionId) ? count + 1 : count;
              }, 0);

              return (
                <div
                  key={qIndex}
                  className="list-group-item list-group-item-action flex-grow-1"
                  style={{
                    borderRadius: '10px',
                    border: '1px solid #ddd',
                    padding: '15px',
                    marginBottom: '20px',
                    backgroundColor: '#fff',
                    transition: 'background-color 0.3s ease',
                    cursor: 'pointer',
                    boxShadow: '0 12px 24px rgba(0, 0, 0, 0.1)',
                  }}
                >
                  <h5 style={{ fontWeight: '600', color: '#333' }}>{questionResponse.questionText}</h5>
                  <p style={{ color: '#6c757d', marginBottom: '10px' }}>
                    {responseCount} {responseCount === 1 ? 'person' : 'people'} answered this question.
                  </p>
                  <div style={{ maxHeight: '150px', overflowY: 'auto', marginLeft: '20px' }}>
                    {userResponses.map((response, rIndex) => {
                      const matchingResponse = response.responses.find(
                        (r) => r.questionId === questionResponse.questionId
                      );
                      return (
                        <div key={rIndex} style={{ marginBottom: '5px', color: '#495057' }}>
                          {matchingResponse ? (
                            <>
                              <strong>Response {rIndex + 1}:</strong>
                              {parseInt(matchingResponse.answer, 10) >= 1 && parseInt(matchingResponse.answer, 10) <= 5 ? (
                                <div className="mb-3">
                                  <div style={{ display: "flex", justifyContent: "space-between", maxWidth: "120px" }}>
                                    {[1, 2, 3, 4, 5].map((star) => (
                                      <span key={star} style={{ fontSize: "1.5rem", color: star <= matchingResponse.answer ? "#ffc107" : "#e4e5e9" }}>
                                        ★
                                      </span>
                                    ))}
                                  </div>
                                </div>
                              ) : (
                                <p>{matchingResponse.answer}</p>
                              )}
                            </>
                          ) : (
                            <p><strong>Response {rIndex + 1}:</strong> No response for this question.</p>
                          )}
                        </div>
                      );
                    })}
                  </div>
                </div>
              );
            })
          ) : (
            <p>No responses yet.</p>
          )
        )}

        {/* Back button */}
        <div className="mt-4 text-center">
          <Button
            onClick={() => navigate(-1)}
            style={{ borderRadius: '10px', padding: '10px 20px', fontSize: '1rem' }}
          >
            Go Back
          </Button>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-dark text-white text-center py-3 mt-auto" style={{ zIndex: 2, position: 'relative', width: '100%' }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default SurveyResponsesPage;
