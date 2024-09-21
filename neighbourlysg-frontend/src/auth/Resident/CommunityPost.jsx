/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import "bootstrap/dist/css/bootstrap.min.css";
import { Modal, Button, Form } from "react-bootstrap";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";

const animatedComponents = makeAnimated();

const tags = [
  "#Life", "#Travel", "#Food", "#Fitness", "#Art", "#Music", "#Fashion",
  "#Technology", "#Photography", "#Nature", "#Birthday", "#Wedding",
  "#Vacation", "#Concert", "#Conference", "#Festival", "#Holiday",
  "#FamilyReunion", "#Happy", "#Sad", "#Excited", "#Grateful", "#Motivated",
  "#Chill", "#Adventurous", "#Inspired", "#Relaxed", "#SelfCare",
  "#MentalHealth", "#EcoFriendly", "#Inclusivity"
];

const initialPosts = [
  {
    id: 1,
    user: "John Doe",
    content: "Had a great time at the community cleanup event!",
    tags: ["#community", "#cleanup"],
    likes: 5,
    comments: [{ id: 1, user: "Jane Smith", content: "It was an amazing event!" }]
  },
  {
    id: 2,
    user: "Mary Jane",
    content: "Looking forward to the Fall Festival next week!",
    tags: ["#fallfestival", "#community"],
    likes: 8,
    comments: [{ id: 1, user: "Tom Cruise", content: "Can’t wait!" }]
  }
];

function CommunityPost() {
  const [posts, setPosts] = useState(initialPosts);
  const [newPostContent, setNewPostContent] = useState("");
  const [selectedTags, setSelectedTags] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedPost, setSelectedPost] = useState(null);

  const handleCreatePost = () => {
    const newPost = {
      id: posts.length + 1,
      user: "Current User",
      content: newPostContent,
      tags: selectedTags,
      likes: 0,
      comments: [],
    };
    setPosts([newPost, ...posts]);
    resetForm();
  };

  const resetForm = () => {
    setNewPostContent("");
    setSelectedTags([]);
  };


  return (
    <div
      className="d-flex flex-column min-vh-100"
      style={{
        backgroundImage: `url(${neighbourlySGbackground})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
      }}
    >
      <div className="container mt-5 flex-grow-1">
        <div className="mb-4" style={{ maxWidth: "600px", margin: "0 auto" }}>
          <h2 className="text-dark bg-white bg-opacity-75 p-2 rounded text-center">
            Community News Feed
          </h2>
        </div>

        <div className="card mb-4" style={{ maxWidth: "600px", margin: "0 auto" }}>
        <div className="card-body">
          <Form.Group>
            <Form.Control
              as="textarea"
              rows={2}
              placeholder="What's on your mind?"
              value={newPostContent}
              onChange={(e) => setNewPostContent(e.target.value)}
            />
          </Form.Group>

          <div className="mb-3" />

          <Form.Group>
            <Select
              closeMenuOnSelect={false}
              components={animatedComponents}
              isMulti
              options={tags.map(tag => ({ value: tag, label: tag }))}
              onChange={(selectedOptions) => setSelectedTags(selectedOptions.map(option => option.value))}
            />
          </Form.Group>

          <Button variant="primary" className="mt-2" onClick={handleCreatePost}>
            Post
          </Button>
        </div>
      </div>


        {posts.map((post) => (
          <div key={post.id} className="card mb-4" style={{ maxWidth: "600px", margin: "0 auto" }}>
            <div className="card-body">
              <h5 className="card-title">{post.user}</h5>
              <p className="card-text">{post.content}</p>
              <p>
                {post.tags.map((tag) => (
                  <span key={tag} className="badge bg-secondary me-1">{tag}</span>
                ))}
              </p>
            </div>
          </div>
        ))}

        {/* Modal for Editing Post */}
        {selectedPost && (
          <Modal show={showModal} onHide={() => setShowModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Edit Post</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form.Group>
                <Form.Control
                  as="textarea"
                  rows={3}
                  value={newPostContent}
                  onChange={(e) => setNewPostContent(e.target.value)}
                />
                <Form.Control
                  type="text"
                  className="mt-2"
                  value={selectedTags.join(" ")}
                  readOnly
                />
              </Form.Group>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => setShowModal(false)}>
                Close
              </Button>
              <Button variant="primary" onClick={handleEditPost}>
                Save Changes
              </Button>
            </Modal.Footer>
          </Modal>
        )}
      </div>

      <footer className="bg-dark text-white text-center py-3 mt-auto">
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default CommunityPost;
