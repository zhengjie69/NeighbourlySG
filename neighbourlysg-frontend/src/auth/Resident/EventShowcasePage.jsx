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
  const [userEvents, setUserEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [showUpcomingModal, setShowUpcomingModal] = useState(false);
  const [showPastModal, setShowPastModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showCreateEventModal, setShowCreateEventModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null); // For error messages
  const [successMessage, setSuccessMessage] = useState(null); // For success messages

  const profileId = 1; //Hardcode for now

  const [newEvent, setNewEvent] = useState({
    title: '',
    location: '',
    description: '',
    date: '',
    startTime: '',
    endTime: '',
  });

  const [editEvent, setEditEvent] = useState({
    id: '',
    title: '',
    location: '',
    description: '',
    date: '',
    startTime: '',
    endTime: '',
  });

  const fetchUpcomingEvents = async () => {
    try {
      // Example URL, adjust based on your actual API endpoint
      const response = await axios.get('http://localhost:8080/api/EventService/getAllCurrentEvent/' + profileId);
      const events = response.data;
      setUpcomingEvents(events);
    } catch (error) {
      console.error('Error fetching upcoming events:', error);
    }
  };

  const fetchUserEvents = async () => {
    try {
      // Example URL, adjust based on your actual API endpoint
      const response = await axios.get('http://localhost:8080/api/EventService/getAllUserEvent/' + profileId);
      const events = response.data;
      setUserEvents(events);
    } catch (error) {
      console.error('Error fetching user events:', error);
    }
  };

  useEffect(() => {

    const fetchPastEvents = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/EventService/getAllPastEvent/' + profileId);
        const events = response.data;
        setPastEvents(events);
      } catch (error) {
        console.error('Error fetching past events:', error);
      }
    };

    fetchUserEvents();
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

  const handleCreateEvent = async (e) => {
    e.preventDefault();
    try {
      // Replace with your API endpoint
      await axios.post('http://localhost:8080/api/EventService/createEvent/' + profileId, newEvent);
      setSuccessMessage('Event created successfully!');
      setErrorMessage(null);
      setShowCreateEventModal(false);

      setNewEvent({
        title: '',
        location: '',
        description: '',
        date: '',
        startTime: '',
        endTime: '',
      });

      fetchUserEvents(); // Refresh the events list

    } catch (error) {
      setErrorMessage('Error creating event. Please try again.');
      setSuccessMessage(null);
      console.error('Error creating event:', error);
    }
  };

  const handleEditEvent = async (e) => {
    e.preventDefault();
    try {
      await axios.put('http://localhost:8080/api/EventService/updateEvent', editEvent);
      setSuccessMessage('Event updated successfully!');
      setErrorMessage(null);
      setShowEditModal(false);

      fetchUserEvents();

    } catch (error) {
      setErrorMessage('Error updating event. Please try again.');
      setSuccessMessage(null);
      console.error('Error updating event:', error);
    }
  };

  const handleDeleteEvent = async (e) => {
    e.preventDefault();
    try {
      await axios.delete('http://localhost:8080/api/EventService/deleteEvent/' + editEvent.id);
      setSuccessMessage('Event deleted successfully!');
      setErrorMessage(null);
      setShowEditModal(null);

      fetchUserEvents();
    } catch (error) {
      setErrorMessage('Error deleting event. Please try again.');
      setSuccessMessage(null);
      console.error('Error deleting event:', error);
    }
  }

  const handleShowUpcomingModal = (upcomingEvent) => {
    setSelectedEvent(upcomingEvent);
    setShowUpcomingModal(true);
  };

  const handleShowPastModal = (pastEvent) => {
    setSelectedEvent(pastEvent);
    setShowPastModal(true);
  };

  const handleShowUserEventModal = (userEvents) => {
    setSelectedEvent(userEvents);
    setEditEvent({
      id: userEvents.id,
      title: userEvents.title,
      location: userEvents.location,
      description: userEvents.description,
      date: userEvents.date,
      startTime: userEvents.startTime,
      endTime: userEvents.endTime,
    });
    setShowEditModal(true);
  }

  const handleCloseModal = () => {
    setShowUpcomingModal(false);
    setShowPastModal(false);
    setShowEditModal(false);
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

        {/* Create Event Button */}
        <div className="mb-4 row">
          <div className="col-md-12 mt-3">
            <button className="btn btn-primary" onClick={() => setShowCreateEventModal(true)}>
              Create Event
            </button>
          </div>
        </div>

        {/* User's Events */}
        <div className="mb-5" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '8px', overflowX: 'auto', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Your Events</h3>
          {userEvents.length > 0 ? (
            <div className="d-flex">
              {userEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', flex: '0 0 auto', width: '300px' }} onClick={() => handleShowUserEventModal(event)}>
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

      {/* Modal for Creating new events */}
      {showCreateEventModal && (
        <Modal show={showCreateEventModal} onHide={() => setShowCreateEventModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Create New Event</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={handleCreateEvent}>
              <div className="mb-3">
                <label htmlFor="eventTitle" className="form-label">Event Title</label>
                <input
                  type="text"
                  className="form-control"
                  id="eventTitle"
                  value={newEvent.title}
                  onChange={(e) => setNewEvent({ ...newEvent, title: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventLocation" className="form-label">Location</label>
                <input
                  type="text"
                  className="form-control"
                  id="eventLocation"
                  value={newEvent.location}
                  onChange={(e) => setNewEvent({ ...newEvent, location: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventDescription" className="form-label">Description</label>
                <textarea
                  className="form-control"
                  id="eventDescription"
                  rows="3"
                  value={newEvent.description}
                  onChange={(e) => setNewEvent({ ...newEvent, description: e.target.value })}
                  required
                ></textarea>
              </div>
              <div className="mb-3">
                <label htmlFor="eventDate" className="form-label">Date</label>
                <input
                  type="date"
                  className="form-control"
                  id="eventDate"
                  value={newEvent.date}
                  onChange={(e) => setNewEvent({ ...newEvent, date: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventStartTime" className="form-label">Start Time</label>
                <input
                  type="time"
                  className="form-control"
                  id="eventStartTime"
                  value={newEvent.startTime}
                  onChange={(e) => setNewEvent({ ...newEvent, startTime: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventEndTime" className="form-label">End Time</label>
                <input
                  type="time"
                  className="form-control"
                  id="eventEndTime"
                  value={newEvent.endTime}
                  onChange={(e) => setNewEvent({ ...newEvent, endTime: e.target.value })}
                  required
                />
              </div>
              <div className="d-flex justify-content-end">
                <Button variant="secondary" onClick={() => setShowCreateEventModal(false)}>
                  Close
                </Button>
                <Button type="submit" variant="primary" className="ms-2">
                  Create Event
                </Button>
              </div>
            </form>
          </Modal.Body>
        </Modal>
      )}

      {/* Modal for Editing events */}
      {showEditModal && (
        <Modal show={showEditModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>Edit Event</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={handleEditEvent}>
              <input
                type="hidden"
                className="form-control"
                id="eventId"
                value={editEvent.id}
                readOnly
              />
              <div className="mb-3">
                <label htmlFor="eventTitle" className="form-label">Event Title</label>
                <input
                  type="text"
                  className="form-control"
                  id="eventTitle"
                  value={editEvent.title}
                  onChange={(e) => setEditEvent({ ...editEvent, title: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventLocation" className="form-label">Location</label>
                <input
                  type="text"
                  className="form-control"
                  id="eventLocation"
                  value={editEvent.location}
                  onChange={(e) => setEditEvent({ ...editEvent, location: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventDescription" className="form-label">Description</label>
                <textarea
                  className="form-control"
                  id="eventDescription"
                  rows="3"
                  value={editEvent.description}
                  onChange={(e) => setEditEvent({ ...editEvent, description: e.target.value })}
                  required
                ></textarea>
              </div>
              <div className="mb-3">
                <label htmlFor="eventDate" className="form-label">Date</label>
                <input
                  type="date"
                  className="form-control"
                  id="eventDate"
                  value={editEvent.date}
                  onChange={(e) => setEditEvent({ ...editEvent, date: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventStartTime" className="form-label">Start Time</label>
                <input
                  type="time"
                  className="form-control"
                  id="eventStartTime"
                  value={editEvent.startTime}
                  onChange={(e) => setEditEvent({ ...editEvent, startTime: e.target.value })}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="eventEndTime" className="form-label">End Time</label>
                <input
                  type="time"
                  className="form-control"
                  id="eventEndTime"
                  value={editEvent.endTime}
                  onChange={(e) => setEditEvent({ ...editEvent, endTime: e.target.value })}
                  required
                />
              </div>
              <div className="d-flex justify-content-end">
                <Button variant="secondary" onClick={handleCloseModal} className="ms-2">
                  Close
                </Button>
                <Button variant="danger" onClick={handleDeleteEvent} className="ms-2">
                  Delete
                </Button>
                <Button type="submit" variant="primary" className="ms-2">
                  Update
                </Button>
              </div>
            </form>
          </Modal.Body>
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
