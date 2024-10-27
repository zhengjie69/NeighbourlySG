// eslint-disable-next-line no-unused-vars
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Modal, Button, Form, OverlayTrigger, Tooltip } from "react-bootstrap";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";
import { useNavigate } from "react-router-dom";
import axiosInstance from '../Utils/axiosConfig'

const SurveyShowcasePage = () => {
  const [surveys, setSurveys] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showResponseModal, setShowResponseModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedSurvey, setSelectedSurvey] = useState(null);
  const [surveyToDelete, setSurveyToDelete] = useState(null);
  const [responses, setResponses] = useState({});
  const [userResponses] = useState([]);
  const userRoles = sessionStorage.getItem("roles")
    ? sessionStorage.getItem("roles").split(",")
    : [];
  const isOrganiser = userRoles.includes("ROLE_ORGANISER");
  const isResident = userRoles.includes("ROLE_USER"); // Updated to check for ROLE_USER
  const isAdmin = userRoles.includes("ROLE_ADMIN"); // Check for Admin role
  const isOrganiserOrAdmin = isOrganiser || isAdmin; // Combine organizer and admin access
  const navigate = useNavigate(); // Initialize navigate
  const [viewMode, setViewMode] = useState("response"); // Default to "response by response"

  // Fetch surveys from the backend
  useEffect(() => {
    const fetchSurveys = async () => {
      try {
        const response = await axiosInstance.get(
          "/SurveyService/getAllSurveys"
        );
        setSurveys(response.data);
        console.log("Surveys:", response.data); // Log surveys to check if data is being fetched correctly
      } catch (error) {
        console.error("Error fetching surveys:", error);
      }
    };

    fetchSurveys();
  }, []);

  // Open Survey Modal
  const handleShowModal = (survey) => {
    if (isResident) {
      navigate("/survey-detail", { state: { survey } });
    } else {
      alert("Only residents can access the survey detail page.");
    }
  };

  // Close Survey Modal
  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedSurvey(null);
  };

  // View Responses Modal
  const handleViewResponses = (survey) => {
    navigate("/survey-responses", { state: { survey } });
  };

  // Update Survey Modal
  const handleUpdateSurvey = (survey) => {
    navigate("/CreateSurveyForm", { state: { surveyId: survey.id } });
  };

  // Handle Delete
  const handleDeleteSurvey = async () => {
    try {
      await axiosInstance.delete(
        `/SurveyService/survey/${surveyToDelete.id}`
      );
      setSurveys((prevSurveys) =>
        prevSurveys.filter((survey) => survey.id !== surveyToDelete.id)
      );
      setShowDeleteModal(false);
    } catch (error) {
      console.error("Error deleting survey:", error);
    }
  };

  // Handle response changes
  const handleResponseChange = (questionId, value) => {
    setResponses((prevResponses) => ({
      ...prevResponses,
      [questionId]: value,
    }));
  };

  // Handle the toggle between 'question' and 'response' views
  const handleViewToggle = (mode) => {
    setViewMode(mode);
  };

  const handleSubmit = async () => {
    if (!selectedSurvey) return;

    const userId = sessionStorage.getItem("userId"); // Get userId from sessionStorage

    const responsePayload = {
      userId: userId, // Include the userId
      surveyId: selectedSurvey.id,
      responses: Object.entries(responses).map(([questionId, answer]) => ({
        questionId,
        answer,
      })),
    };

    try {
      await axiosInstance.post(
        "/SurveyResponseService/submitSurveyResponse",
        responsePayload
      );
      console.log("Survey responses submitted:", responsePayload);
      handleCloseModal(); // Close the modal after submission
    } catch (error) {
      console.error("Error submitting survey responses:", error);
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
        position: "relative",
      }}
    >
      {/* Back Button positioned at the top-right */}
      {/* <Button
        onClick={() => navigate(-1)}
        style={{
          position: "absolute",
          top: "20px",
          right: "20px",
          zIndex: "1000",
          backgroundColor: "#fff",
          color: "#333",
          borderRadius: "50%",
          border: "none",
          width: "50px",
          height: "50px",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          boxShadow: "0px 2px 5px rgba(0,0,0,0.2)",
        }}
      >
        ←
      </Button> */}

      <div
        className="card p-5 mt-5 mb-4"
        style={{
          width: "800px",
          boxShadow: "0 12px 24px rgba(0, 0, 0, 0.2)",
          borderRadius: "16px",
          backgroundColor: "rgba(255, 255, 255, 0.9)",
        }}
      >
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h3
              style={{ fontWeight: "700", fontSize: "1.8rem", color: "#333" }}
            >
              Available Surveys
            </h3>
            <p style={{ fontSize: "1rem", color: "#6c757d" }}>
              Participate in our surveys and share your valuable feedback with
              the community.
            </p>
          </div>
          {isOrganiserOrAdmin && (
            <Button
              variant="primary"
              onClick={() => navigate("/CreateSurveyForm")}
            >
              Create Survey
            </Button>
          )}
        </div>

        {/* Display each survey with different buttons for Residents and Organizers */}
        <div className="list-group">
          {surveys.map((survey) => (
            <div
              key={survey.id}
              className="d-flex align-items-center justify-content-between mb-3"
            >
              <div className="flex-grow-1">
                <button
                  className="list-group-item list-group-item-action"
                  style={{
                    borderRadius: "10px",
                    transition: "background-color 0.3s ease",
                    cursor: "pointer",
                  }}
                  onClick={() => handleShowModal(survey)}
                >
                  <div className="d-flex justify-content-between">
                    <h5
                      className="mb-1"
                      style={{ fontWeight: "600", color: "#333" }}
                    >
                      {survey.title}
                    </h5>
                  </div>
                  <p className="mb-1" style={{ color: "#495057" }}>
                    {survey.description}
                  </p>
                </button>
              </div>

              {/* Resident Role: Show "Respond to Survey" button */}
              {isResident && (
                <Button
                  variant="success"
                  onClick={() => handleShowModal(survey)}
                  style={{ marginLeft: "15px" }} // No comments inside style
                >
                  Respond to Survey
                </Button>
              )}

              {/* Organizer Role: Show "View Responses," "Update Survey," and "Delete Survey" buttons */}
              {isOrganiserOrAdmin && (
                <div className="d-flex ms-3">
                  <OverlayTrigger
                    placement="top"
                    overlay={<Tooltip>View Responses</Tooltip>}
                  >
                    <Button
                      variant="outline-primary"
                      className="ms-2"
                      onClick={() => handleViewResponses(survey)}
                    >
                      <FaEye />
                    </Button>
                  </OverlayTrigger>

                  <OverlayTrigger
                    placement="top"
                    overlay={<Tooltip>Update Survey</Tooltip>}
                  >
                    <Button
                      variant="outline-warning"
                      className="ms-2"
                      onClick={() => handleUpdateSurvey(survey)}
                    >
                      <FaEdit />
                    </Button>
                  </OverlayTrigger>

                  <OverlayTrigger
                    placement="top"
                    overlay={<Tooltip>Delete Survey</Tooltip>}
                  >
                    <Button
                      variant="outline-danger"
                      className="ms-2"
                      onClick={() => {
                        setSurveyToDelete(survey);
                        setShowDeleteModal(true);
                      }}
                    >
                      <FaTrash />
                    </Button>
                  </OverlayTrigger>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Survey Modal */}
      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>{selectedSurvey?.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{selectedSurvey?.description}</p>
          {selectedSurvey?.questions.map((question) => (
            <Form.Group
              key={question.id}
              controlId={`question-${question.id}`}
              className="mb-3"
            >
              <Form.Label>{question.questionText}</Form.Label>
              {question.questionType === "shortAnswer" && (
                <Form.Control
                  type="text"
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                  placeholder="Your answer"
                />
              )}
              {question.questionType === "paragraph" && (
                <Form.Control
                  as="textarea"
                  rows={3}
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                  placeholder="Your thoughts..."
                />
              )}
              {question.questionType === "multipleChoice" && (
                <Form.Select
                  value={responses[question.id] || ""}
                  onChange={(e) =>
                    handleResponseChange(question.id, e.target.value)
                  }
                >
                  <option value="">Select an option</option>
                  {question.options.map((option, index) => (
                    <option key={index} value={option}>
                      {option}
                    </option>
                  ))}
                </Form.Select>
              )}
              {question.questionType === "rating" && (
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    maxWidth: "120px",
                    margin: "auto",
                  }}
                >
                  {[1, 2, 3, 4, 5].map((star) => (
                    <span
                      key={star}
                      style={{
                        fontSize: "1.5rem",
                        cursor: "pointer",
                        color:
                          star <= (responses[question.id] || 0)
                            ? "#ffc107"
                            : "#e4e5e9",
                      }}
                      onClick={() => handleResponseChange(question.id, star)}
                    >
                      ★
                    </span>
                  ))}
                </div>
              )}
            </Form.Group>
          ))}
        </Modal.Body>
        <Modal.Footer style={{ display: "flex", justifyContent: "center" }}>
          <Button
            variant="secondary"
            onClick={handleCloseModal}
            style={{
              borderRadius: "10px",
              padding: "10px 20px",
              fontSize: "1rem",
            }}
          >
            Close
          </Button>
          <Button
            variant="primary"
            onClick={handleSubmit}
            style={{
              borderRadius: "10px",
              padding: "10px 20px",
              fontSize: "1rem",
            }}
          >
            Submit
          </Button>
        </Modal.Footer>
      </Modal>

      {/* View Responses Modal */}
      <Modal
        show={showResponseModal}
        onHide={() => setShowResponseModal(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Responses for {selectedSurvey?.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {/* Toggle between response or question view */}
          <div className="d-flex justify-content-center mb-3">
            <Button
              variant={viewMode === "response" ? "primary" : "secondary"}
              onClick={() => handleViewToggle("response")}
              className="me-2"
            >
              View by Response
            </Button>
            <Button
              variant={viewMode === "question" ? "primary" : "secondary"}
              onClick={() => handleViewToggle("question")}
            >
              View by Question
            </Button>
          </div>

          {/* Conditionally render based on selected view mode */}
          {viewMode === "response" ? (
            userResponses.length > 0 ? (
              userResponses.map((response, index) => (
                <div key={index} className="mb-4">
                  <h5>
                    <strong>Response {index + 1}:</strong>
                  </h5>
                  {response.responses.map((questionResponse, idx) => (
                    <div key={idx} style={{ marginLeft: "20px" }}>
                      <p>
                        <strong>{questionResponse.questionText}:</strong>{" "}
                        {questionResponse.answer}
                      </p>
                    </div>
                  ))}
                </div>
              ))
            ) : (
              <p>No responses yet.</p>
            )
          ) : userResponses.length > 0 ? (
            userResponses[0].responses.map((questionResponse, qIndex) => (
              <div key={qIndex} className="mb-4">
                <h5>
                  <strong>{questionResponse.questionText}:</strong>
                </h5>
                {userResponses.map((response, rIndex) => {
                  const matchingResponse = response.responses.find(
                    (r) => r.questionId === questionResponse.questionId
                  );
                  return (
                    <div key={rIndex} style={{ marginLeft: "20px" }}>
                      {matchingResponse ? (
                        <p>
                          <strong>Response {rIndex + 1}:</strong>{" "}
                          {matchingResponse.answer}
                        </p>
                      ) : (
                        <p>
                          <strong>Response {rIndex + 1}:</strong> No response
                          for this question.
                        </p>
                      )}
                    </div>
                  );
                })}
              </div>
            ))
          ) : (
            <p>No responses yet.</p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => setShowResponseModal(false)}
          >
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Update Survey Modal */}
      <Modal
        show={showUpdateModal}
        onHide={() => setShowUpdateModal(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Update Survey: {selectedSurvey?.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Title</Form.Label>
              <Form.Control type="text" defaultValue={selectedSurvey?.title} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                defaultValue={selectedSurvey?.description}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowUpdateModal(false)}>
            Close
          </Button>
          <Button variant="primary">Save Changes</Button>
        </Modal.Footer>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        show={showDeleteModal}
        onHide={() => setShowDeleteModal(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Confirm Deletion</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to delete this survey?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteSurvey}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Footer */}
      <footer
        className="bg-dark text-white text-center py-3 mt-auto"
        style={{ zIndex: 2, position: "relative", width: "100%" }}
      >
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default SurveyShowcasePage;
