/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import "bootstrap/dist/css/bootstrap.min.css";
import { Modal, Button, Form } from "react-bootstrap";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";
import axios from "axios";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const animatedComponents = makeAnimated();

const tags = [
  "#Life", "#Travel", "#Food", "#Fitness", "#Art", "#Music", "#Fashion",
  "#Technology", "#Photography", "#Nature", "#Birthday", "#Wedding",
  "#Vacation", "#Concert", "#Conference", "#Festival", "#Holiday",
  "#FamilyReunion", "#Happy", "#Sad", "#Excited", "#Grateful", "#Motivated",
  "#Chill", "#Adventurous", "#Inspired", "#Relaxed", "#SelfCare",
  "#MentalHealth", "#EcoFriendly", "#Inclusivity"
];

function CommunityPost() {
  const [posts, setPosts] = useState([]);
  const [newPostContent, setNewPostContent] = useState("");
  const [selectedTags, setSelectedTags] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedPost, setSelectedPost] = useState(null);
  const [commentContent, setCommentContent] = useState("");
  const userId = sessionStorage.getItem("userId");

  useEffect(() => {
    // fetch all posts
    const fetchPosts = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/PostService/`);
        if (response.status === 200) {
          setPosts(response.data);
        }
      } catch (error) {
        console.error("Error fetching posts:", error);
      }
    };

    const fetchProfile = async () => {
      try {
        // Fetch the user's profile details to get name
        const response = await axios.get(`http://localhost:8080/api/ProfileService/profile/${userId}`);
  
        if (response.status === 200) {
          const name = response.data.name; 
          sessionStorage.setItem("name", name);
        }
      } catch (error) {
        console.error("Error fetching profile:", error);
      }
    };

    fetchPosts(); 
    fetchProfile(); 
  }, []);   

  const handleCreatePost = async () => {
    try {
        // Create a new post
        const newPost = {
          content: newPostContent,
          creationDate: new Date().toISOString(), // Use the current date
          profileId: userId,
          likeCount: 0,
          comments: [],
          tags: selectedTags,
          profileName: sessionStorage.getItem('name')
        };

        const res = await axios.post(`http://localhost:8080/api/PostService/${userId}`, newPost);
        if (res.status === 200 || res.status === 201) {
          setPosts([newPost, ...posts]);
          resetForm(); // Reset the form fields after posting
          toast.success("Your status have been posted successfully!");
        }
      
    } catch (error) {
      console.error("Error fetching profile or creating post:", error);
    }
  };

  // Like
  const handleLikePost = async (postId) => {  
    try {
      const postToUpdate = posts.find((post) => post.id === postId);
  
      const hasLiked = postToUpdate.likedBy?.includes(userId);
  
      if (!hasLiked) {
        const likePost = {
          id: 0,
          profileId: userId,
          postId: postId,
          likedAt: new Date().toISOString()
        };
  
        await axios.post(`http://localhost:8080/api/LikeService/${userId}/posts/${postId}`, likePost);
  
        const updatedPosts = posts.map((post) =>
          post.id === postId ? { ...post, likeCount: post.likeCount + 1, likedBy: [...(post.likedBy || []), userId] } : post
        );
        setPosts(updatedPosts);
      } else {
        await axios.delete(`http://localhost:8080/api/LikeService/${userId}/posts/${postId}`);
  
        const updatedPosts = posts.map((post) =>
          post.id === postId
            ? {
                ...post,
                likeCount: post.likeCount > 0 ? post.likeCount - 1 : 0,
                likedBy: post.likedBy.filter((id) => id !== userId)
              }
            : post
        );
        setPosts(updatedPosts);
      }
    } catch (error) {
      console.error("Error toggling like/unlike: ", error);
    }
  };
  

  // Comment
  const handleAddComment = async (postId) => {
    if (!commentContent.trim()) return; // Do nothing if the comment is empty

    try {
      const comment = {
        postId: postId,
        profileId: userId,
        content: commentContent,
        creationDate: new Date().toISOString(),
      };

      // POST comment to the server (replace with your actual endpoint)
      const response = await axios.post(`http://localhost:8080/api/CommentService/${postId}`, comment);

      if (response.status === 200 || response.status === 201) {
        // Update posts state with new comment
        const updatedPosts = posts.map((post) =>
          post.id === postId ? { ...post, comments: [...(post.comments || []), comment] } : post
        );
        setPosts(updatedPosts);
        setCommentContent(""); // Reset comment input
        toast.success("Comment added successfully!");
      }
    } catch (error) {
      console.error("Error adding comment:", error);
    }
  };

  const handleShowComments = (post) => {
    setSelectedPost(post); // Set the selected post
    setShowModal(true); // Open the modal
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

    <ToastContainer />

    <div className="container mt-5 flex-grow-1">
    <div className="mb-4" style={{ width: "100%" }}>
      <h2
        className="text-dark bg-white bg-opacity-75 p-2 rounded text-center"
        style={{ display: "inline-block", width: "100%" }}
      >
        Community News Feed
      </h2>
    </div>

    <div className="card mb-4" style={{ maxWidth: "100%", margin: "0 auto" }}>
      <div className="card-body">
        <Form.Group>
          <Form.Control
            as="textarea"
            rows={2}
            placeholder={`What's on your mind, ${sessionStorage.getItem('name')}?`}
            value={newPostContent}
            onChange={(e) => setNewPostContent(e.target.value)}
          />
        </Form.Group>

        <div className="mt-2" />

        <div className="row">
          <div className="col-sm-10">
          <Form.Group>
            <Select
              closeMenuOnSelect={false}
              components={animatedComponents}
              isMulti
              options={tags.map(tag => ({ value: tag, label: tag }))}
              onChange={(selectedOptions) => setSelectedTags(selectedOptions.map(option => option.value))}
            />
          </Form.Group>
          </div>

          <div className="col d-flex justify-content-end col-sm-2">
          <Button 
            variant="primary" 
            onClick={handleCreatePost} 
            disabled={!newPostContent.trim()}
          >
            Post
          </Button>
          </div>
        </div>
      </div>
    </div>

      <div className="row">
      {posts.map((post) => (
        <div key={post.id} className="col-md-6 mb-4">
          <div className="card" style={{height: "auto", display: "flex"}}>
            <div className="card-body" >
              <h5 className="card-title">{post.profileName}</h5>
              <p className="card-text">{post.content}</p>
              <p>
                {post.tags.map((tag) => (
                  <span key={tag} className="badge bg-secondary me-1">{tag}</span>
                ))}
              </p>

              <div class="card-footer d-flex justify-content-between text-center">
                <Button variant="btn btn-theme" onClick={() => handleLikePost(post.id)}>
                  {post.likeCount} Like
                </Button>

                <Button variant="btn btn-theme" onClick={() => handleShowComments(post)}>
                    Comment
                </Button>
              </div>

            </div>
          </div>
        </div>
      ))}
    </div>

        {/* Modal for Editing Post */}
        {/* {selectedPost && (
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
        )} */}

        {selectedPost && (
          <Modal show={showModal} onHide={() => setShowModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Comments for {selectedPost.profileName}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <div className="comments-list">
                {selectedPost.comments && selectedPost.comments.map((comment, index) => (
                  <div key={index} className="comment-item">
                    <strong>{sessionStorage.getItem("name")}</strong>: {comment.content}
                  </div>
                ))}
              </div>
              <Form.Group className="mt-3">
                <Form.Control
                  type="text"
                  placeholder="Add a comment..."
                  value={commentContent}
                  onChange={(e) => setCommentContent(e.target.value)}
                />
                <Button variant="primary" onClick={() => { handleAddComment(selectedPost.id); setShowModal(false); }}>
                  Submit
                </Button>
              </Form.Group>
            </Modal.Body>
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
