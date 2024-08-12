// ResidentEventPage.jsx
// eslint-disable-next-line no-unused-vars
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { Modal, Button } from 'react-bootstrap';
import neighbourlySGbackground from '../../assets/neighbourlySGbackground.jpg';
import SGLogo from '../../assets/SGLogo.avif';

const grcSmcOptions = [
  'Aljunied GRC', 'Ang Mo Kio GRC', 'Bishan-Toa Payoh GRC', 'Chua Chu Kang GRC',
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
  const [notifications, setNotifications] = useState([]);
  const [reminders, setReminders] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedArea, setSelectedArea] = useState('');
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [eventToCancel, setEventToCancel] = useState(null);

  useEffect(() => {
    const events = [
      {
        id: 1,
        title: 'Neighborhood Cleanup',
        date: 'August 20, 2024 10:00 AM',
        description: 'Join us for a community cleanup event.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Jurong GRC',
        rsvp: false,
        rsvpCount: 10,
      },
      {
        id: 2,
        title: 'Fall Festival',
        date: 'September 15, 2024 2:00 PM',
        description: 'Celebrate the season with food, games, and fun!',
        image: 'https://via.placeholder.com/600x400',
        area: 'Tampines GRC',
        rsvp: false,
        rsvpCount: 25,
      },
      {
        id: 3,
        title: 'Community Garden Workshop',
        date: 'July 15, 2024 9:00 AM',
        description: 'Learn about community gardening and sustainability.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Ang Mo Kio GRC',
        rsvp: true,
        rsvpCount: 30,
      },
      {
        id: 4,
        title: 'Health and Wellness Fair',
        date: 'October 10, 2024 11:00 AM',
        description: 'A fair dedicated to promoting health and wellness in the community.',
        image: 'https://via.placeholder.com/600x400',
        area: 'East Coast GRC',
        rsvp: false,
        rsvpCount: 15,
      },
      {
        id: 5,
        title: 'Book Club Meeting',
        date: 'November 2, 2024 3:00 PM',
        description: 'Join us for a discussion on the latest bestsellers.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Marine Parade GRC',
        rsvp: true,
        rsvpCount: 20,
      },
      {
        id: 6,
        title: 'Community Sports Day',
        date: 'December 5, 2024 9:30 AM',
        description: 'Participate in various sports activities and competitions.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Holland-Bukit Timah GRC',
        rsvp: false,
        rsvpCount: 40,
      },
      {
        id: 7,
        title: 'Music in the Park',
        date: 'January 14, 2024 5:00 PM',
        description: 'Enjoy live music performances in the park.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Jalan Besar GRC',
        rsvp: true,
        rsvpCount: 35,
      },
      {
        id: 8,
        title: 'Cultural Food Festival',
        date: 'February 25, 2024 12:00 PM',
        description: 'Taste cuisines from around the world at our cultural food festival.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Chua Chu Kang GRC',
        rsvp: false,
        rsvpCount: 50,
      },
      {
        id: 9,
        title: 'Art in the Community',
        date: 'March 15, 2024 10:00 AM',
        description: 'A showcase of local artists and their work.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Sembawang GRC',
        rsvp: true,
        rsvpCount: 60,
      },
      {
        id: 10,
        title: 'Technology Expo',
        date: 'April 5, 2024 9:00 AM',
        description: 'Explore the latest in technology and innovation.',
        image: 'https://via.placeholder.com/600x400',
        area: 'Pasir Ris-Punggol GRC',
        rsvp: false,
        rsvpCount: 70,
      },
    ];

    const currentDate = new Date();
    const upcoming = events.filter(event => new Date(event.date) >= currentDate);
    const past = events.filter(event => new Date(event.date) < currentDate);

    setUpcomingEvents(upcoming);
    setPastEvents(past);
    setFilteredEvents(upcoming);
    setNotifications(['Don\'t miss the Fall Festival on September 15, 2024!']);
  }, []);

  const handleRSVP = (eventId, eventTitle, eventDate) => {
    setUpcomingEvents(prevEvents => {
      const updatedEvents = prevEvents.map(event => {
        if (event.id === eventId && !event.rsvp) {
          return { ...event, rsvp: true, rsvpCount: event.rsvpCount + 1 };
        } else if (event.id === eventId && event.rsvp) {
          setEventToCancel(event);
          setShowCancelModal(true);
        }
        return event;
      });
      setFilteredEvents(updatedEvents);
      return updatedEvents;
    });

    if (!notifications.includes(`You have RSVP'd for the event: ${eventTitle}`)) {
      setNotifications(prevNotifications => [
        ...prevNotifications,
        `You have RSVP'd for the event: ${eventTitle}`,
      ]);
      setReminders(prevReminders => [
        ...prevReminders,
        { eventTitle, eventDate }
      ]);
    }
  };

  const handleCancelRSVP = () => {
    setUpcomingEvents(prevEvents => {
      const updatedEvents = prevEvents.map(event => {
        if (event.id === eventToCancel.id && event.rsvp) {
          return { ...event, rsvp: false, rsvpCount: event.rsvpCount - 1 };
        }
        return event;
      });
      setFilteredEvents(updatedEvents);
      return updatedEvents;
    });

    setNotifications(prevNotifications =>
      prevNotifications.filter(notification => !notification.includes(eventToCancel.title))
    );
    setReminders(prevReminders =>
      prevReminders.filter(reminder => reminder.eventTitle !== eventToCancel.title)
    );
    setShowCancelModal(false);
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
    if (area) {
      filtered = filtered.filter(event => event.area === area);
    }
    setFilteredEvents(filtered);
  };

  const handleShowModal = (event) => {
    setSelectedEvent(event);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedEvent(null);
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

        {/* Notifications */}
        <div className="alert alert-info">
          <h4>Notifications</h4>
          <ul>
            {notifications.map((notification, index) => (
              <li key={index}>{notification}</li>
            ))}
          </ul>
          <h5>Reminder</h5>
          <ul>
            {reminders.map((reminder, index) => (
              <li key={index}>{reminder.eventTitle} - {reminder.eventDate}</li>
            ))}
          </ul>
        </div>

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
              <option value="">Select Area/Location</option>
              {grcSmcOptions.map(option => (
                <option key={option} value={option}>{option}</option>
              ))}
            </select>
          </div>
        </div>

        {/* Upcoming Events */}
        <div className="mb-5" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '8px', overflowX: 'auto', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Upcoming Events</h3>
          {filteredEvents.length > 0 ? (
            <div className="d-flex">
              {filteredEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', flex: '0 0 auto', width: '300px' }} onClick={() => handleShowModal(event)}>
                  <img src={event.image} className="card-img-top" alt={event.title} />
                  <div className="card-body">
                    <h4 className="card-title">{event.title}</h4>
                    <h6 className="card-subtitle mb-2 text-muted">{event.date}</h6>
                    <p className="card-text text-truncate" style={{ maxHeight: '50px', overflow: 'hidden', textOverflow: 'ellipsis' }}>{event.description}</p>
                    <p><strong>RSVP Count:</strong> {event.rsvpCount}</p>
                    {!event.rsvp ? (
                      <button className="btn btn-primary" onClick={(e) => { e.stopPropagation(); handleRSVP(event.id, event.title, event.date); }}>
                        RSVP
                      </button>
                    ) : (
                      <button className="btn btn-success" onClick={(e) => { e.stopPropagation(); handleRSVP(event.id, event.title, event.date); }}>
                        RSVP&apos;d
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-dark">No upcoming events at the moment.</p>
          )}
        </div>

        {/* Past Events */}
        <div className="mb-5" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '20px', borderRadius: '8px', overflowX: 'auto', whiteSpace: 'nowrap' }}>
          <h3 className="text-dark">Past Events</h3>
          {pastEvents.length > 0 ? (
            <div className="d-flex">
              {pastEvents.map(event => (
                <div key={event.id} className="card h-100 me-3" style={{ backgroundColor: 'rgba(255, 255, 255, 0.8)', flex: '0 0 auto', width: '300px' }} onClick={() => handleShowModal(event)}>
                  <img src={event.image} className="card-img-top" alt={event.title} />
                  <div className="card-body">
                    <h4 className="card-title">{event.title}</h4>
                    <h6 className="card-subtitle mb-2 text-muted">{event.date}</h6>
                    <p className="card-text text-truncate" style={{ maxHeight: '50px', overflow: 'hidden', textOverflow: 'ellipsis' }}>{event.description}</p>
                    <p><strong>RSVP Count:</strong> {event.rsvpCount}</p>
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
        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>{selectedEvent.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <img src={selectedEvent.image} className="img-fluid mb-3" alt={selectedEvent.title} />
            <h5>{selectedEvent.date}</h5>
            <p>{selectedEvent.description}</p>
            <p><strong>RSVP Count:</strong> {selectedEvent.rsvpCount}</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            {!selectedEvent.rsvp ? (
              <Button variant="primary" onClick={() => handleRSVP(selectedEvent.id, selectedEvent.title, selectedEvent.date)}>
                RSVP
              </Button>
            ) : (
              <Button variant="success" onClick={() => handleRSVP(selectedEvent.id, selectedEvent.title, selectedEvent.date)}>
                RSVP&apos;d
              </Button>
            )}
          </Modal.Footer>
        </Modal>
      )}

      {/* Cancel RSVP Modal */}
      {eventToCancel && (
        <Modal show={showCancelModal} onHide={() => setShowCancelModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Cancel RSVP</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Are you sure you want to cancel your RSVP for {eventToCancel.title}?
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowCancelModal(false)}>
              No
            </Button>
            <Button variant="danger" onClick={handleCancelRSVP}>
              Yes, Cancel RSVP
            </Button>
          </Modal.Footer>
        </Modal>
      )}

      <footer className="bg-dark text-white text-center py-3 mt-auto">
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
        <p><Link to="/contact" className="text-white">Contact Support</Link></p>
      </footer>
    </div>
  );
}

export default ResidentEventPage;
