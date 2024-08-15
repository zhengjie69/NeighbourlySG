/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Modal, Button, Form } from "react-bootstrap";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";
import SGLogo from "../../assets/SGLogo.avif";

const initialPosts = [
  {
    id: 1,
    user: "John Doe",
    content: "Had a great time at the community cleanup event!",
    tags: ["#community", "#cleanup"],
    likes: 5,
    comments: [
      { id: 1, user: "Jane Smith", content: "It was an amazing event!" },
      { id: 2, user: "Bob Lee", content: "Glad I could be a part of it!" },
    ],
  },
  {
    id: 2,
    user: "Mary Jane",
    content: "Looking forward to the Fall Festival next week!",
    tags: ["#fallfestival", "#community"],
    likes: 8,
    comments: [{ id: 1, user: "Tom Cruise", content: "Can’t wait!" }],
  },
];

function CommunityPost() {
  const [posts, setPosts] = useState(initialPosts);
  const [newPostContent, setNewPostContent] = useState("");
  const [newTags, setNewTags] = useState("");
  const [newMedia, setNewMedia] = useState(null);
  const [selectedPost, setSelectedPost] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const handleCreatePost = () => {
    const newPost = {
      id: posts.length + 1,
      user: "Current User",
      content: newPostContent,
      tags: newTags.split(" "),
      media: newMedia,
      likes: 0,
      comments: [],
    };
    setPosts([newPost, ...posts]);
    setNewPostContent("");
    setNewTags("");
    setNewMedia(null);
  };

  const handleLikePost = (postId) => {
    setPosts(
      posts.map((post) =>
        post.id === postId ? { ...post, likes: post.likes + 1 } : post
      )
    );
  };

  const handleSharePost = (postId) => {
    console.log(`Post ${postId} shared!`);
  };

  const handleEditPost = () => {
    setPosts(
      posts.map((post) =>
        post.id === selectedPost.id
          ? {
              ...post,
              content: newPostContent,
              tags: newTags.split(" "),
              media: newMedia,
            }
          : post
      )
    );
    setSelectedPost(null);
    setShowModal(false);
  };

  const handleDeletePost = (postId) => {
    setPosts(posts.filter((post) => post.id !== postId));
  };

  const handleMediaChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setNewMedia(reader.result);
      };
      reader.readAsDataURL(file);
    }
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
      <nav
        className="navbar navbar-expand-lg navbar-light bg-light"
        style={{ zIndex: 2, padding: "10px 20px" }}
      >
        <div className="container-fluid">
          <a className="navbar-brand" href="/ResidentMainPage">
            <img
              src={SGLogo}
              alt="SG Logo"
              style={{ marginRight: "10px", width: "50px", height: "35px" }}
            />
            NeighbourlySG
          </a>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNavDropdown"
            aria-controls="navbarNavDropdown"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <a
                  className="nav-link active"
                  aria-current="page"
                  href="/surveys"
                >
                  Surveys
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/events">
                  Events
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/posts">
                  Community Posts
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/ProfileSettings">
                  Profile
                </a>
              </li>
              <li className="nav-item dropdown">
                <ul
                  className="dropdown-menu"
                  aria-labelledby="navbarDropdownMenuLink"
                >
                  <li>
                    <a className="dropdown-item" href="/settings">
                      Settings
                    </a>
                  </li>
                  <li>
                    <a className="dropdown-item" href="/help">
                      Help
                    </a>
                  </li>
                </ul>
              </li>
            </ul>
            <span className="navbar-text">Welcome, [User]!</span>
          </div>
        </div>
      </nav>

      <div className="container mt-5 flex-grow-1">
        <div className="mb-4" style={{ maxWidth: "600px", width: "100%", margin: "0 auto" }}>
          <h2
            className="text-dark bg-white bg-opacity-75 p-2 rounded text-center"
            style={{ display: "inline-block", width: "100%" }}
          >
            Community News Feed
          </h2>
        </div>

        <div
          className="card mb-4"
          style={{ maxWidth: "600px", width: "100%", margin: "0 auto" }}
        >
          <div className="card-body">
            <Form.Group>
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="What's on your mind?"
                value={newPostContent}
                onChange={(e) => setNewPostContent(e.target.value)}
              />
              <Form.Control
                type="text"
                className="mt-2"
                placeholder="Add tags (separated by space)"
                value={newTags}
                onChange={(e) => setNewTags(e.target.value)}
              />
              <Form.Control
                type="file"
                className="mt-2"
                accept="image/*,video/*"
                onChange={handleMediaChange}
              />
              <Form.Control
                type="text"
                className="mt-2"
                placeholder="Add YouTube or TikTok link"
                value={newMedia ? "" : ""}
                onChange={(e) => setNewMedia(e.target.value)}
              />
            </Form.Group>
            <Button
              variant="primary"
              className="mt-2"
              onClick={handleCreatePost}
            >
              Post
            </Button>
          </div>
        </div>

        {posts.map((post) => (
          <div
            key={post.id}
            className="card mb-4"
            style={{ maxWidth: "600px", width: "100%", margin: "0 auto" }}
          >
            <div className="card-body">
              <h5 className="card-title">{post.user}</h5>
              <p className="card-text">{post.content}</p>
              {post.media && (
                <div className="mb-3">
                  {post.media.startsWith("data:image") ? (
                    <img src={post.media} alt="media" className="img-fluid" />
                  ) : post.media.startsWith("data:video") ? (
                    <video controls className="img-fluid">
                      <source src={post.media} />
                    </video>
                  ) : post.media.includes("youtube.com") ||
                    post.media.includes("youtu.be") ? (
                    <iframe
                      width="100%"
                      height="315"
                      src={`https://www.youtube.com/embed/${post.media
                        .split("/")
                        .pop()}`}
                      frameBorder="0"
                      allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                      allowFullScreen
                      title="YouTube video"
                    ></iframe>
                  ) : post.media.includes("tiktok.com") ? (
                    <blockquote
                      className="tiktok-embed"
                      cite={post.media}
                      data-video-id={post.media.split("/").pop()}
                    >
                      <section> </section>
                    </blockquote>
                  ) : null}
                </div>
              )}
              <p>
                {post.tags.map((tag) => (
                  <span key={tag} className="badge bg-secondary me-1">
                    {tag}
                  </span>
                ))}
              </p>
              <Button
                variant="outline-primary"
                className="me-2"
                onClick={() => handleLikePost(post.id)}
              >
                Like ({post.likes})
              </Button>
              <Button
                variant="outline-secondary"
                className="me-2"
                onClick={() => setSelectedPost(post)}
              >
                Comment
              </Button>
              <Button
                variant="outline-success"
                className="me-2"
                onClick={() => handleSharePost(post.id)}
              >
                Share
              </Button>
              {post.user === "Current User" && (
                <>
                  <Button
                    variant="outline-warning"
                    className="me-2"
                    onClick={() => {
                      setNewPostContent(post.content);
                      setNewTags(post.tags.join(" "));
                      setSelectedPost(post);
                      setShowModal(true);
                    }}
                  >
                    Edit
                  </Button>
                  <Button
                    variant="outline-danger"
                    onClick={() => handleDeletePost(post.id)}
                  >
                    Delete
                  </Button>
                </>
              )}
            </div>

            <div className="card-footer">
              {post.comments.map((comment) => (
                <p key={comment.id}>
                  <strong>{comment.user}:</strong> {comment.content}
                </p>
              ))}
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
                  value={newTags}
                  onChange={(e) => setNewTags(e.target.value)}
                />
                <Form.Control
                  type="file"
                  className="mt-2"
                  accept="image/*,video/*"
                  onChange={handleMediaChange}
                />
                <Form.Control
                  type="text"
                  className="mt-2"
                  placeholder="Add YouTube or TikTok link"
                  value={newMedia ? "" : ""}
                  onChange={(e) => setNewMedia(e.target.value)}
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
        <p>
          <a href="/contact" className="text-white">
            Contact Support
          </a>
        </p>
      </footer>
    </div>
  );
}

export default CommunityPost;
