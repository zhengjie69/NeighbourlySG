// eslint-disable-next-line no-unused-vars
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link, useNavigate } from 'react-router-dom';
import { Modal, Button, Form } from 'react-bootstrap';
import SGLogo from '../../assets/SGLogo.avif';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';

const SurveyShowcasePage = () => {
  const surveys = [
    { id: 1, title: 'Community Garden Feedback', description: 'Share your thoughts on our new community garden initiative.' },
    { id: 2, title: 'Safety in the Neighborhood', description: 'Provide input on how we can improve safety in our area.' },
    { id: 3, title: 'Public Transportation Satisfaction', description: 'Rate your satisfaction with the public transportation system.' },
  ];

  const [showModal, setShowModal] = useState(false);
  const [selectedSurvey, setSelectedSurvey] = useState(null);
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState('');
  
  const navigate = useNavigate(); // Import navigate function

  const handleShowModal = (survey) => {
    setSelectedSurvey(survey);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setRating(0);
    setComment('');
  };

  const handleSubmit = () => {
    console.log(`Survey: ${selectedSurvey.title}, Rating: ${rating}, Comment: ${comment}`);
    handleCloseModal();
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
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h3 style={{ fontWeight: '700', fontSize: '1.8rem', color: '#333' }}>Available Surveys</h3>
            <p style={{ fontSize: '1rem', color: '#6c757d' }}>
              Participate in our surveys and share your valuable feedback with the community.
            </p>
          </div>
          <Button variant="primary" onClick={() => navigate('/CreateSurveyForm')}>
            Create Survey
          </Button>
        </div>
        <div className="list-group">
          {surveys.map((survey) => (
            <button 
              key={survey.id} 
              className="list-group-item list-group-item-action flex-column align-items-start mb-3" 
              style={{ borderRadius: '10px', transition: 'background-color 0.3s ease', cursor: 'pointer' }}
              onClick={() => handleShowModal(survey)}
            >
              <div className="d-flex w-100 justify-content-between">
                <h5 className="mb-1" style={{ fontWeight: '600', color: '#333' }}>{survey.title}</h5>
              </div>
              <p className="mb-1" style={{ color: '#495057' }}>{survey.description}</p>
            </button>
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
          <Form>
            <Form.Group controlId="rating" className="mb-3">
              <Form.Label>Rate this Survey</Form.Label>
              <div style={{ display: 'flex', justifyContent: 'space-between', maxWidth: '120px', margin: 'auto' }}>
                {[1, 2, 3, 4, 5].map(star => (
                  <span 
                    key={star} 
                    style={{ fontSize: '1.5rem', cursor: 'pointer', color: star <= rating ? '#ffc107' : '#e4e5e9' }}
                    onClick={() => setRating(star)}
                  >
                    ★
                  </span>
                ))}
              </div>
            </Form.Group>
            <Form.Group controlId="comment" className="mb-3">
              <Form.Label>Leave a Comment</Form.Label>
              <Form.Control 
                as="textarea" 
                rows={4} 
                value={comment} 
                onChange={(e) => setComment(e.target.value)} 
                placeholder="Share your thoughts about this survey..."
                style={{
                  borderRadius: '10px',
                  borderColor: '#ced4da',
                  padding: '10px',
                  fontSize: '1rem'
                }}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer style={{ display: 'flex', justifyContent: 'center' }}>
          <Button variant="secondary" onClick={handleCloseModal} style={{ borderRadius: '10px', padding: '10px 20px', fontSize: '1rem' }}>
            Close
          </Button>
          <Button variant="primary" onClick={handleSubmit} style={{ borderRadius: '10px', padding: '10px 20px', fontSize: '1rem' }}>
            Submit
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Footer */}
      <footer className="bg-dark text-white text-center py-3 mt-auto" style={{ zIndex: 2, position: 'relative', width: '100%' }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
        <p><Link to="/contact" className="text-white">Contact Support</Link></p>
      </footer>
    </div>
  );
};

export default SurveyShowcasePage;
