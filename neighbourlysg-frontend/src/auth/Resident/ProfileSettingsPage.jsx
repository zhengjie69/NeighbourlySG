import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Form, Button, Alert, Modal } from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";

const grcSmcOptions = [
  "Aljunied GRC",
  "Ang Mo Kio GRC",
  "Bishan-Toa Payoh GRC",
  "Chua Chu Kang GRC",
  "East Coast GRC",
  "Holland-Bukit Timah GRC",
  "Jalan Besar GRC",
  "Jurong GRC",
  "Marine Parade GRC",
  "Marsiling-Yew Tee GRC",
  "Nee Soon GRC",
  "Pasir Ris-Punggol GRC",
  "Sembawang GRC",
  "Tampines GRC",
  "Tanjong Pagar GRC",
  "West Coast GRC",
  "Bukit Batok SMC",
  "Bukit Panjang SMC",
  "Hong Kah North SMC",
  "Hougang SMC",
  "Kebun Baru SMC",
  "MacPherson SMC",
  "Marymount SMC",
  "Mountbatten SMC",
  "Pioneer SMC",
  "Potong Pasir SMC",
  "Punggol West SMC",
  "Radin Mas SMC",
  "Yio Chu Kang SMC",
  "Yuhua SMC",
];

function ProfileSettingsPage() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [constituency, setConstituency] = useState("");
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState("");
  const userId = sessionStorage.getItem("userId");
  const navigate = useNavigate();

  const validateForm = () => {
    const errors = {};
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

    // Name is optional, validate only if provided
    if (name && name.trim() === "") {
      errors.name = "Name cannot be empty";
    }

    // Email is optional, validate only if provided
    if (email && !emailRegex.test(email)) {
      errors.email = "Please enter a valid email address";
    }

    // Password fields are only required if oldPassword is provided
    if (oldPassword) {
      if (!newPassword) {
        errors.newPassword = "New password is required if changing password";
      } else if (!passwordRegex.test(newPassword)) {
        errors.newPassword =
          "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character";
      }

      if (newPassword !== confirmPassword) {
        errors.confirmPassword = "Passwords do not match";
      }
    }

    // Constituency is optional, validate only if provided
    if (constituency && !grcSmcOptions.includes(constituency)) {
      errors.constituency = "Please select a valid constituency";
    }

    return errors;
  };

  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      setSuccessMessage("");
      return;
    }

    setErrors({}); // Clear previous errors

    try {
      // Prepare the data for the profile update
      const updatedData = {};
      if (name) updatedData.name = name;
      if (email) updatedData.email = email;
      if (newPassword) updatedData.password = newPassword;
      if (constituency) updatedData.constituency = constituency;

      // Only update the fields that are provided
      const response = await axios.put(
        `http://neighbourlysg.ap-southeast-1.elasticbeanstalk.com/api/ProfileService/updateProfile/${userId}`,
        updatedData
      );

      setSuccessMessage("Profile updated successfully!");
      setTimeout(() => {
        navigate("/ResidentMainPage"); // Redirect to the main page after 2 seconds
      }, 2000);
    } catch (error) {
      setErrors({ api: "Failed to update profile. Please try again later." });
    }
  };

  const handleDeleteAccount = async () => {
    if (deleteConfirm === "delete account") {
      try {
        const response = await axios.delete(
          `http://neighbourlysg.ap-southeast-1.elasticbeanstalk.com/api/ProfileService/profile/${userId}`
        );
        if (response.status === 200) {
          alert("Your account has been deleted.");
          setShowModal(false);
          navigate("/ResidentLogin");
        }
      } catch (error) {
        alert("Failed to delete account. Please try again.");
      }
    } else {
      alert('You need to type "delete account" to confirm.');
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
      {/* Back Button */}
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

      <div className="container mt-5 flex-grow-1">
        <div
          className="mb-4 d-flex justify-content-center"
          style={{ width: "100%" }}
        >
          <h2
            className="text-dark bg-white bg-opacity-75 p-2 rounded text-center"
            style={{ display: "inline-block", width: "100%" }}
          >
            Profile Settings
          </h2>
        </div>

        <div className="card mb-4" style={{ width: "100%", padding: "20px" }}>
          <Form onSubmit={handleProfileUpdate}>
            <Form.Group controlId="formName" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Name</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="text"
                    placeholder="Enter your name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    isInvalid={errors.name}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.name}
                  </Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formEmail" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Email Address</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    isInvalid={errors.email}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.email}
                  </Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formOldPassword" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Old Password</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="password"
                    placeholder="Enter old password"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                    isInvalid={errors.oldPassword}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.oldPassword}
                  </Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formNewPassword" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">New Password</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="password"
                    placeholder="Enter new password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    isInvalid={errors.newPassword}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.newPassword}
                  </Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formConfirmPassword" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">
                    Confirm Password
                  </Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Control
                    type="password"
                    placeholder="Confirm new password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    isInvalid={errors.confirmPassword}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.confirmPassword}
                  </Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <Form.Group controlId="formConstituency" className="mb-3">
              <div className="row align-items-center">
                <div className="col-sm-4">
                  <Form.Label className="form-label">Constituency</Form.Label>
                </div>
                <div className="col-sm-8">
                  <Form.Select
                    value={constituency}
                    onChange={(e) => setConstituency(e.target.value)}
                    isInvalid={errors.constituency}
                  >
                    <option value="">Select your constituency</option>
                    {grcSmcOptions.map((option, index) => (
                      <option key={index} value={option}>
                        {option}
                      </option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">
                    {errors.constituency}
                  </Form.Control.Feedback>
                </div>
              </div>
            </Form.Group>

            <div className="row">
              <div className="col d-flex justify-content-start">
                <Button
                  variant="danger"
                  type="button"
                  className="mt-2"
                  onClick={() => setShowModal(true)}
                >
                  Delete Account
                </Button>
              </div>
              <div className="col d-flex justify-content-end">
                <Button variant="primary" type="submit" className="mt-2">
                  Update
                </Button>
              </div>
            </div>
          </Form>

          {successMessage && (
            <Alert variant="success" className="mt-3">
              {successMessage}
            </Alert>
          )}
        </div>
      </div>

      {/* Delete Account Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete Account</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <p style={{ color: "#d9534f" }}>
              Are you sure you want to delete your account? This action cannot
              be undone.
            </p>
            <Form.Group controlId="formDeleteAccount" className="mb-3">
              <Form.Label>
                Type 'delete account' to delete your account
              </Form.Label>
              <Form.Control
                placeholder="delete account"
                value={deleteConfirm}
                onChange={(e) => setDeleteConfirm(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteAccount}>
            Confirm Delete Account
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default ProfileSettingsPage;
