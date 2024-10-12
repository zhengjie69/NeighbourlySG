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
import { FaHeart, FaComment, FaShare } from 'react-icons/fa'; // Icons for Like, Comment, Share

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
  const [newComment, setNewComment] = useState(""); // New comment input
  const userId = sessionStorage.getItem("userId");

  useEffect(() => {
    // fetch all posts
    const fetchPosts = async () => {
      try {
        const response = await axios.get(`http://localhost:5000/api/PostService/`);
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
        const response = await axios.get(`http://localhost:5000/api/ProfileService/profile/${userId}`);
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
        creationDate: new Date().toISOString(),
        profileId: userId,
        likeCount: 0,
        comments: [],
        tags: selectedTags,
        profileName: sessionStorage.getItem('name')
      };

      const res = await axios.post(`http://localhost:5000/api/PostService/${userId}`, newPost);
      if (res.status === 200 || res.status === 201) {
        setPosts([newPost, ...posts]);
        resetForm(); // Reset the form fields after posting
        toast.success("Your status has been posted successfully!");
      }

    } catch (error) {
      console.error("Error creating post:", error);
    }
  };

  const resetForm = () => {
    setNewPostContent("");
    setSelectedTags([]);
  };

  const handleLikePost = (postId) => {
    // Increment the like count for the post
    setPosts(posts.map(post => post.id === postId ? { ...post, likeCount: post.likeCount + 1 } : post));
  };

  const handleAddComment = (postId) => {
    if (newComment.trim() === "") return;
    const updatedPosts = posts.map(post => {
      if (post.id === postId) {
        return { ...post, comments: [...post.comments, { text: newComment, profileName: sessionStorage.getItem('name') }] };
      }
      return post;
    });
    setPosts(updatedPosts);
    setNewComment("");
  };

  const handleSharePost = (postId) => {
    // Simple share function, this can be enhanced further
    toast.info(`Post ID: ${postId} shared!`);
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
              <div className="card" style={{ height: "auto", display: "flex" }}>
                <div className="card-body" >
                  <h5 className="card-title">{post.profileName}</h5>
                  <p className="card-text">{post.content}</p>
                  <p>
                    {post.tags.map((tag) => (
                      <span key={tag} className="badge bg-secondary me-1">{tag}</span>
                    ))}
                  </p>
                  <div className="d-flex justify-content-start">
                    <Button variant="link" onClick={() => handleLikePost(post.id)}>
                      <FaHeart /> {post.likeCount} Likes
                    </Button>
                    <Button variant="link" onClick={() => handleSharePost(post.id)}>
                      <FaShare /> Share
                    </Button>
                    <Button variant="link" onClick={() => setSelectedPost(post)}>
                      <FaComment /> Comment
                    </Button>
                  </div>
                  <div>
                    {post.comments.length > 0 && post.comments.map((comment, index) => (
                      <div key={index} className="mt-2">
                        <strong>{comment.profileName}</strong>: {comment.text}
                      </div>
                    ))}
                  </div>
                  {selectedPost && selectedPost.id === post.id && (
                    <div className="mt-3">
                      <Form.Group>
                        <Form.Control
                          type="text"
                          placeholder="Write a comment..."
                          value={newComment}
                          onChange={(e) => setNewComment(e.target.value)}
                        />
                      </Form.Group>
                      <Button variant="primary" className="mt-2" onClick={() => handleAddComment(post.id)}>
                        Add Comment
                      </Button>
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

        <footer className="bg-dark text-white text-center py-3 mt-auto" style={{ position: "relative", bottom: 0, width: "100%" }}>
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
        <p>
        </p>
      </footer>
      </div>
    </div>
  );
}
export default CommunityPost;
