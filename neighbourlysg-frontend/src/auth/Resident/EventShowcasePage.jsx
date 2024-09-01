// ResidentEventPage.jsx
// eslint-disable-next-line no-unused-vars
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { Modal, Button, Alert } from 'react-bootstrap';
import axios from 'axios';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import SGLogo from '../../assets/SGLogo.avif';

const grcSmcOptions = [
  'All Locations', 'Aljunied GRC', 'Ang Mo Kio GRC', 'Bishan-Toa Payoh GRC', 'Chua Chu Kang GRC',
  'East Coast GRC', 'Holland-Bukit Timah GRC', 'Jalan Besar GRC', 'Jurong GRC',
  'Marine Parade GRC', 'Marsiling-Yew Tee GRC', 'Nee Soon GRC', 'Pasir Ris-Punggol GRC',
  'Sembawang GRC', 'Tampines GRC', 'Tanjong Pagar GRC', 'West Coast GRC',
  'Bukit Batok SMC', 'Bukit Panjang SMC', 'Hong Kah North SMC', 'Hougang SMC',
  'Kebun Baru SMC', 'MacPherson SMC', 'Marymount SMC', 'Mountbatten SMC',
  'Pioneer SMC', 'Potong Pasir SMC', 'Punggol West SMC', 'Radin Mas SMC',
  'Yio Chu Kang SMC', 'Yuhua SMC'
];

function ResidentEventPage() {
  const [upcomingEvents, setUpcomingEvents] = useState([]);
  const [pastEvents, setPastEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedArea, setSelectedArea] = useState('All Locations');
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [showUpcomingModal, setShowUpcomingModal] = useState(false);
  const [showPastModal, setShowPastModal] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null); // For error messages
  const [successMessage, setSuccessMessage] = useState(null); // For success messages

  const fetchUpcomingEvents = async () => {
    try {
      // Example URL, adjust based on your actual API endpoint
      const response = await axios.get('http://localhost:8080/api/EventService/getAllCurrentEvent');
      const events = response.data;
      setUpcomingEvents(events);
    } catch (error) {
      console.error('Error fetching upcoming events:', error);
    }
  };

  useEffect(() => {

    const fetchPastEvents = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/EventService/getAllPastEvent');
        const events = response.data;
        setPastEvents(events);
      } catch (error) {
        console.error('Error fetching past events:', error);
      }
    };

    fetchUpcomingEvents();
    fetchPastEvents();

  }, []);

  const rsvpAsParticipant = async (profileId, eventId) => {
    try {
      const response = await axios.post('http://localhost:8080/api/EventService/rsvpParticipant', {
        profileId: profileId,
        eventId: eventId,
      });
      const rsvpResponse = response.data;

      handleCloseModal();

      if (rsvpResponse == "RSVP is completed") {
        setSuccessMessage('Successfully RSVP\'d to the event!');
        setErrorMessage(null);
      }

      fetchUpcomingEvents();

    } catch (error) {

      handleCloseModal();

      setErrorMessage('Error adding RSVP for this event. Are you already enrolled to the event?');
      setSuccessMessage(null);

      console.error('Error rsvping for this event:', error);
    }
  };

  const deleteRsvpAsParticipant = async (profileId, eventId) => {
    try {
      const response = await axios.post('http://localhost:8080/api/EventService/deleteRsvpAsParticipant', {
        profileId: profileId,
        eventId: eventId,
      });
      const rsvpResponse = response.data;

      handleCloseModal();
      setShowCancelModal(false);

      if (rsvpResponse == "RSVP is removed") {
        setSuccessMessage('Successfully remove RSVP\'d to the event!');
        setErrorMessage(null);
      }

      fetchUpcomingEvents();

    } catch (error) {

      handleCloseModal();
      setShowCancelModal(false);

      setErrorMessage('Error removing RSVP for this event. Are you enrolled to the event?');
      setSuccessMessage(null);

      console.error('Error removing RSVP for this event:', error);
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
    filterEvents(e.target.value, selectedArea);
  };

  const handleAreaChange = (e) => {
    setSelectedArea(e.target.value);
    filterEvents(searchTerm, e.target.value);
  };

  const filterEvents = (searchTerm, area) => {
    let filtered = upcomingEvents;
    if (searchTerm) {
      filtered = filtered.filter(event =>
        event.title.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    if (area && area !== 'All Locations') {
      filtered = filtered.filter(event => event.area === area);
    }
    setFilteredEvents(filtered);
  };

  const handleShowUpcomingModal = (upcomingEvent) => {
    setSelectedEvent(upcomingEvent);
    setShowUpcomingModal(true);
  };

  const handleShowPastModal = (pastEvent) => {
    setSelectedEvent(pastEvent);
    setShowPastModal(true);
  };

  const handleCloseModal = () => {
    setShowUpcomingModal(false);
    setShowPastModal(false);
    setSelectedEvent(null);
  };

  const closeMessage = (type) => {
    if (type === 'success') {
      setSuccessMessage(null);
    } else if (type === 'error') {
      setErrorMessage(null);
    }
  };

  return (
    <div
      className="d-flex flex-column min-vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
      }}
    >
      <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{ zIndex: 2, padding: '10px 20px' }}>
        <div className="container-fluid">
          <Link className="navbar-brand" to="/ResidentMainPage">
            <img src={SGLogo} alt="SG Logo" style={{ marginRight: '10px', width: '50px', height: '35px' }} />
            NeighbourlySG
          </Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <Link className="nav-link active" aria-current="page" to="/surveys">Surveys</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/events">Events</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/posts">Community Posts</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/ProfileSettings">Profile</Link>
              </li>
              <li className="nav-item dropdown">
                <ul className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                  <li><Link className="dropdown-item" to="/settings">Settings</Link></li>
                  <li><Link className="dropdown-item" to="/help">Help</Link></li>
                </ul>
              </li>
            </ul>
            <span className="navbar-text">
              Welcome, [User]!
            </span>
          </div>
        </div>
      </nav>

      <div className="container mt-5 flex-grow-1">
        <h2 className="mb-4 text-white">Community Events</h2>

        {/* Search and Filter */}
        <div className="mb-4 row">
          <div className="col-md-6">
            <input
              type="text"
              className="form-control mb-3"
              placeholder="Search events..."
              value={searchTerm}
              onChange={handleSearch}
            />
          </div>
          <div className="col-md-6">
            <select className="form-control" value={selectedArea} onChange={handleAreaChange}>
              {grcSmcOptions.map(option => (
                <option key={option} value={option}>{option}</option>
              ))}
            </select>
          </div>
        </div>

        {/* Upcoming Events */}
        <div className="mb-5" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '8px', overflowX: 'auto', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Upcoming Events</h3>
          {upcomingEvents.length > 0 ? (
            <div className="d-flex">
              {upcomingEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', flex: '0 0 auto', width: '300px' }} onClick={() => handleShowUpcomingModal(event)}>
                  <div className="card-body">
                    <h4 className="card-title">{event.title}</h4>
                    <h6 className="card-subtitle mb-2 text-muted">{event.date}</h6>
                    <p className="card-text">{event.location}</p>
                    <p><strong>RSVP Count:</strong> {event.rsvpCount}</p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-dark">No upcoming events to display.</p>
          )}
        </div>

        {/* Past Events */}
        <div className="mb-5" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '8px', overflowX: 'auto', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Past Events</h3>
          {pastEvents.length > 0 ? (
            <div className="d-flex">
              {pastEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', flex: '0 0 auto', width: '300px' }} onClick={() => handleShowPastModal(event)}>
                  <div className="card-body">
                    <h4 className="card-title">{event.title}</h4>
                    <h6 className="card-subtitle mb-2 text-muted">{event.date}</h6>
                    <p className="card-text">{event.location}</p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-dark">No past events to display.</p>
          )}
        </div>
      </div>

      {/* Modal for Upcoming Event Details */}
      {selectedEvent && (
        <Modal show={showUpcomingModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>{selectedEvent.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <table className="table">
              <tbody>
                <tr>
                  <th>Event Location</th>
                  <td>{selectedEvent.location}</td>
                </tr>
                <tr>
                  <th>Description</th>
                  <td>{selectedEvent.description}</td>
                </tr>
                <tr>
                  <th>Date</th>
                  <td>{selectedEvent.date}</td>
                </tr>
                <tr>
                  <th>Start Time</th>
                  <td>{selectedEvent.startTime}</td>
                </tr>
                <tr>
                  <th>End Time</th>
                  <td>{selectedEvent.endTime}</td>
                </tr>
                <tr>
                  <th>RSVP Count</th>
                  <td>{selectedEvent.rsvpCount}</td>
                </tr>
              </tbody>
            </table>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            <Button variant="danger" onClick={() => deleteRsvpAsParticipant(1, selectedEvent.id)}>
              Cancel
            </Button>
            <Button variant="primary" onClick={() => rsvpAsParticipant(1, selectedEvent.id)}>
              RSVP
            </Button>
          </Modal.Footer>
        </Modal>
      )}

      {/* Modal for Past Event Details */}
      {selectedEvent && (
        <Modal show={showPastModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>{selectedEvent.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <table className="table">
              <tbody>
                <tr>
                  <th>Event Location</th>
                  <td>{selectedEvent.location}</td>
                </tr>
                <tr>
                  <th>Description</th>
                  <td>{selectedEvent.description}</td>
                </tr>
                <tr>
                  <th>Date</th>
                  <td>{selectedEvent.date}</td>
                </tr>
                <tr>
                  <th>Start Time</th>
                  <td>{selectedEvent.startTime}</td>
                </tr>
                <tr>
                  <th>End Time</th>
                  <td>{selectedEvent.endTime}</td>
                </tr>
              </tbody>
            </table>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      )}

      {/* Display Success or Error Messages */}
      {successMessage && (
        <Alert variant="success" className="fixed-bottom mb-0" onClose={() => closeMessage('success')} dismissible>
          {successMessage}
        </Alert>
      )}
      {errorMessage && (
        <Alert variant="danger" className="fixed-bottom mb-0" onClose={() => closeMessage('error')} dismissible>
          {errorMessage}
        </Alert>
      )}

      <footer className="bg-dark text-white text-center py-3 mt-auto">
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
        <p><Link to="/contact" className="text-white">Contact Support</Link></p>
      </footer>
    </div>
  );
}

export default ResidentEventPage;
