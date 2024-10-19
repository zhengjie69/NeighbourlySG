// ResidentEventPage.jsx
// eslint-disable-next-line no-unused-vars
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { Modal, Button, Alert } from 'react-bootstrap';
import axios from 'axios';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import { Stomp } from '@stomp/stompjs'; // Import Stomp
import SockJS from 'sockjs-client';


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
  const [showNotification, setNotification] = useState(false);
  const [showCreateEventModal, setShowCreateEventModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [upcomingEventSearchLocation, setUpcomingEventSearchLocation] = useState('');
  const [pastEventSearchLocation, setPastEventSearchLocation] = useState('');
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [notificationMessage, setNotificationMessage] = useState('');
  const userRoles = sessionStorage.getItem("roles") || "";
  const isOrganiser = userRoles.includes("ROLE_ORGANISER");
  const profileId = sessionStorage.getItem('userId');
  const constituency = sessionStorage.getItem('constituency');

  const [newEvent, setNewEvent] = useState({
    title: '', location: '', description: '', date: '', startTime: '', endTime: ''
  });

  const [editEvent, setEditEvent] = useState({
    id: '', title: '', location: '', description: '', date: '', startTime: '', endTime: ''
  });

  const fetchUpcomingEvents = async () => {
    try {
      const response = await axios.get(
        `http://localhost:5000/api/EventService/getAllCurrentEvent/${profileId}/${constituency}`,
        { params: { location: upcomingEventSearchLocation } }
      );
      setUpcomingEvents(response.data);
    } catch (error) {
      console.error('Error fetching upcoming events:', error);
    }
  };

  const fetchUserEvents = async () => {
    try {
      const response = await axios.get(`http://localhost:5000/api/EventService/getAllUserEvent/${profileId}`);
      setUserEvents(response.data);
    } catch (error) {
      console.error('Error fetching user events:', error);
    }
  };

  const fetchPastEvents = async () => {
    try {
      const response = await axios.get(
        `http://localhost:5000/api/EventService/getAllPastEvent/${profileId}/${constituency}`,
        { params: { location: pastEventSearchLocation } }
      );
      setPastEvents(response.data);
    } catch (error) {
      console.error('Error fetching past events:', error);
    }
  };

  useEffect(() => {
    fetchUserEvents();
    fetchUpcomingEvents();
    fetchPastEvents();
    const socket = new SockJS('http://localhost:5000/ws'); // Ensure this URL is correct
    const client = Stomp.over(socket);

    client.connect({}, (frame) => {
      console.log('Connected: ' + frame);

      // Subscribe to a topic
      client.subscribe('/topic/events', (message) => {
        if (message.body) {
          setMessages((prevMessages) => [...prevMessages, message.body]);
          setNotificationMessage(message.body);
          setNotification(true);

          setTimeout(() => {
            setNotification(false);
          }, 3000);

        }
      });
    }, (error) => {
      console.error('Connection error:', error); // Handle connection errors here
    });

    // Clean up the connection on unmount
    return () => {
      if (client) {
        client.disconnect();
        console.log("Disconnected from STOMP broker");
      }
    };
  }, []);

  const rsvpAsParticipant = async (profileId, eventId) => {
    try {
      const response = await axios.post('http://localhost:5000/api/EventService/rsvpParticipant', {
        profileId, eventId
      });
      if (response.data === "RSVP is completed") {
        setSuccessMessage('Successfully RSVP\'d to the event!');
        setErrorMessage(null);
        fetchUpcomingEvents();
      }
    } catch (error) {
      setErrorMessage('Error adding RSVP for this event.');
      console.error('Error rsvping for this event:', error);
    }
    setShowUpcomingModal(false);
  };

  const deleteRsvpAsParticipant = async (profileId, eventId) => {
    try {
      const response = await axios.post('http://localhost:5000/api/EventService/deleteRsvpAsParticipant', {
        profileId, eventId
      });
      if (response.data === "RSVP is removed") {
        setSuccessMessage('Successfully removed RSVP to the event!');
        fetchUpcomingEvents();
      }
    } catch (error) {
      setErrorMessage('Error removing RSVP for this event.');
      console.error('Error removing RSVP for this event:', error);
    }
    setShowUpcomingModal(false);
  };

  const handleCreateEvent = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`http://localhost:5000/api/EventService/createEvent/${profileId}`, newEvent);
      setSuccessMessage('Event created successfully!');
      setNewEvent({ title: '', location: '', description: '', date: '', startTime: '', endTime: '' });
      fetchUserEvents();
    } catch (error) {
      setErrorMessage('Error creating event.');
      console.error('Error creating event:', error);
    }
    setShowCreateEventModal(false);
  };

  const handleEditEvent = async (e) => {
    e.preventDefault();
    try {
      await axios.put('http://localhost:5000/api/EventService/updateEvent', editEvent);
      setSuccessMessage('Event updated successfully!');
      fetchUserEvents();
    } catch (error) {
      setErrorMessage('Error updating event.');
      console.error('Error updating event:', error);
    }
    setShowEditModal(false);
  };

  const handleDeleteEvent = async () => {
    try {
      await axios.delete(`http://localhost:5000/api/EventService/deleteEvent/${editEvent.id}`);
      setSuccessMessage('Event deleted successfully!');
      fetchUserEvents();
    } catch (error) {
      setErrorMessage('Error deleting event.');
      console.error('Error deleting event:', error);
    }
    setShowEditModal(false);
  };

  const handleSearchUpcomingEventSubmit = (e) => {
    e.preventDefault();
    fetchUpcomingEvents();
  };

  const handleSearchPastEventSubmit = (e) => {
    e.preventDefault();
    fetchPastEvents();
  };

  const handleShowUpcomingModal = (upcomingEvent) => {
    setSelectedEvent(upcomingEvent);
    setShowUpcomingModal(true);
  };

  const handleShowUserEventModal = (userEvents) => {
    setSelectedEvent(userEvents);
    setEditEvent({
      id: userEvents.id, title: userEvents.title, location: userEvents.location,
      description: userEvents.description, date: userEvents.date,
      startTime: userEvents.startTime, endTime: userEvents.endTime
    });
    setShowEditModal(true);
  };

  const handleCloseModal = () => {
    setShowUpcomingModal(false);
    setShowPastModal(false);
    setShowEditModal(false);
    setSelectedEvent(null);
  };

  const closeMessage = (type) => {
    if (type === 'success') setSuccessMessage(null);
    else if (type === 'error') setErrorMessage(null);
  };

  return (
    <div
      className="d-flex flex-column min-vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: 'cover', backgroundPosition: 'center', backgroundRepeat: 'no-repeat'
      }}
    >
      <div className="container mt-5 flex-grow-1">
        <h2 className="mb-4 text-white">Community Events</h2>

        {/* Conditionally render Create Event Button only for organizer accounts */}
        {isOrganiser && (
          <div className="mb-4">
            <button className="btn btn-primary" onClick={() => setShowCreateEventModal(true)}>
              Create Event
            </button>
          </div>
        )}

        {/* User's Events */}
        <div className="mb-5 p-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', borderRadius: '8px', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Your Events</h3>
          {userEvents.length > 0 ? (
            <div className="d-flex">
              {userEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ flex: '0 0 auto', width: '300px' }} onClick={() => handleShowUserEventModal(event)}>
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
        <div className="mb-5 p-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', borderRadius: '8px', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Upcoming Events</h3>
          <form className="d-flex mb-3" onSubmit={handleSearchUpcomingEventSubmit}>
            <input
              type="text"
              className="form-control me-2"
              placeholder="Search for Upcoming Events Location"
              onChange={(e) => setUpcomingEventSearchLocation(e.target.value)}
            />
            <Button type="submit" variant="primary">Search</Button>
          </form>
          {upcomingEvents.length > 0 ? (
            <div className="d-flex">
              {upcomingEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ flex: '0 0 auto', width: '300px' }} onClick={() => handleShowUpcomingModal(event)}>
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
        <div className="mb-5 p-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', borderRadius: '8px', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Past Events</h3>
          <form className="d-flex mb-3" onSubmit={handleSearchPastEventSubmit}>
            <input
              type="text"
              className="form-control me-2"
              placeholder="Search for Past Events Location"
              onChange={(e) => setPastEventSearchLocation(e.target.value)}
            />
            <Button type="submit" variant="primary">Search</Button>
          </form>
          {pastEvents.length > 0 ? (
            <div className="d-flex">
              {pastEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ flex: '0 0 auto', width: '300px' }} onClick={() => setSelectedEvent(event)}>
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

      {/* Modal for Event Details */}
      {selectedEvent && (
        <Modal show={showUpcomingModal || showPastModal} onHide={handleCloseModal}>
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
                {selectedEvent.rsvpCount && (
                  <tr>
                    <th>RSVP Count</th>
                    <td>{selectedEvent.rsvpCount}</td>
                  </tr>
                )}
              </tbody>
            </table>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            {showUpcomingModal && (
              <>
                <Button variant="danger" onClick={() => deleteRsvpAsParticipant(1, selectedEvent.id)}>
                  Cancel
                </Button>
                <Button variant="primary" onClick={() => rsvpAsParticipant(1, selectedEvent.id)}>
                  RSVP
                </Button>
              </>
            )}
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

      {/* Alert popup for WebSocket messages */}
      {showNotification && (
        <Alert variant="info" className="fixed-top" style={{ margin: '20px', zIndex: '9999' }} onClose={() => setShowPopup(false)} dismissible>
          {notificationMessage}
        </Alert>
      )}

      {/* Display messages list */}
      <ul>
        {messages.map((message, index) => (
          <li key={index}>{message}</li>
        ))}
      </ul>

      <footer className="bg-dark text-white text-center py-3 mt-auto">
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default ResidentEventPage;
