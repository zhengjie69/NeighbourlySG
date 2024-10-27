/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import "bootstrap/dist/css/bootstrap.min.css";
import { Modal, Button, Form } from "react-bootstrap";
import neighbourlySGbackground from "../../assets/neighbourlySGbackground.jpg";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { IoMdSend } from "react-icons/io";
import { BiLike } from "react-icons/bi";
import { FaRegComment } from "react-icons/fa";
import { MdDeleteOutline } from "react-icons/md";
import { FiEdit2 } from "react-icons/fi";
import axiosInstance from '../Utils/axiosConfig'

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
  const [editCommentContent, setEditCommentContent] = useState("");
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showDeleteCommentModal, setShowDeleteCommentModal] = useState(false);
  const [postToDelete, setPostToDelete] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);
  const [editedPostContent, setEditedPostContent] = useState("");
  const [editedTags, setEditedTags] = useState([]);
  const [selectedComment, setSelectedComment] = useState(null);
  const [showEditCommentModal, setShowEditCommentModal] = useState(false);

  const userId = sessionStorage.getItem("userId");
  const roleId = sessionStorage.getItem("roles");

  useEffect(() => {
    // fetch all posts
    const fetchPosts = async () => {
      try {
        const response = await axiosInstance.get(`/PostService/`);
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
        const response = await axiosInstance.get(`/ProfileService/profile/${userId}`);

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
      const newPost = {
        content: newPostContent,
        creationDate: new Date().toISOString(), 
        profileId: userId,
        likeCount: 0,
        comments: [],
        tags: selectedTags,
        profileName: sessionStorage.getItem("name"),
      };
  
      const res = await axiosInstance.post(`/PostService/${userId}`, newPost);
      if (res.status === 200 || res.status === 201) {
        const savedPost = res.data; 
        setPosts([savedPost, ...posts]);
        resetForm(); 
        toast.success("Your status has been posted successfully!");
      }
    } catch (error) {
      console.error("Error fetching profile or creating post:", error);
      toast.error("Failed to create post, please try again later.");
    }
  };

  // Like
  const handleLikePost = async (postId) => {  
    try {
      // get list of people who liked the post 
      const response = await axiosInstance.get(`/LikeService/${postId}/likes/profiles`); 
      const likedByProfiles = response.data.map(profile => profile.id);
      const hasLiked = likedByProfiles.includes(Number(userId));
  
      if (!hasLiked) {
        const likePost = {
          id: 0,
          profileId: userId,
          postId: postId,
          likedAt: new Date().toISOString()
        };
  
        await axiosInstance.post(`/LikeService/${userId}/posts/${postId}`, likePost);
  
        const updatedPosts = posts.map((post) =>
          post.id === postId ? { ...post, likeCount: post.likeCount + 1, likedBy: [...(post.likedBy || []), Number(userId)] } : post
        );
        setPosts(updatedPosts);
      } else {
        await axiosInstance.delete(`/LikeService/${userId}/posts/${postId}`);
  
        const updatedPosts = posts.map((post) =>
          post.id === postId
            ? {
                ...post,
                likeCount: post.likeCount > 0 ? post.likeCount - 1 : 0,
                likedBy: (post.likedBy || []).filter((id) => id !== Number(userId))
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
  const handleAddComment = async (selectedPost) => {
    if (!commentContent.trim()) return; // Do nothing if the comment is empty

    try {
        const comment = {
          id: 0,
          content: commentContent,
          creationDate: new Date().toISOString(),
          postId: selectedPost.id,
          profileId: userId,
          profileName: sessionStorage.getItem("name"),
        };
        
        const response = await axiosInstance.post(`/PostService/${selectedPost.id}/comments/${userId}`, comment);

        if (response.status === 200 || response.status === 201) {
            const savedComment = response.data;
            const updatedPosts = posts.map((post) =>
                post.id === selectedPost.id ? { ...post, comments: [...(post.comments || []), comment] } : post
            );
            setPosts(updatedPosts);
            
            // Update selected post to reflect new comment
            setSelectedPost((prevPost) => ({
                ...prevPost,
                comments: [...(prevPost.comments || []), savedComment]
            }));

            setCommentContent(""); 
            toast.success("Comment added successfully!");
        }
    } catch (error) {
        console.error("Error adding comment:", error);
    }
};

  const handleShowComments = async (post) => {
    setSelectedPost(post); // Set the selected post
    setShowModal(true); // Open the modal

    try {
      const response = await axiosInstance.get(`/PostService/${post.id}/comments`);
      if (response.status === 200) {
        // Assuming your response structure is an array of comments
        setSelectedPost((prevPost) => ({ ...prevPost, comments: response.data }));
      }
    } catch (error) {
      console.error("Error fetching comments:", error);
    }
  };  

  const handleEditComment = (comment) => {
    setSelectedComment(comment);
    setEditCommentContent(comment.content); 
    setShowEditCommentModal(true);
  };  

  const confirmDeleteComment = (commentId) => {
    setSelectedComment(commentId);
    setShowDeleteCommentModal(true);
  };

  const handleDeleteComment = async () => {
    if (!selectedComment) return;
  
    try {
      const response = await axiosInstance.delete(`/PostService/${selectedPost.id}/comments/${selectedComment.id}/${userId}`);
      if (response.status === 200 || response.status === 204) {
        const updatedPosts = posts.map(post =>
          post.id === selectedPost.id
            ? { ...post, comments: post.comments.filter(comment => comment.id !== selectedComment.id) }
            : post
        );
        setPosts(updatedPosts);
        setSelectedPost(prevPost => ({
          ...prevPost,
          comments: prevPost.comments.filter(comment => comment.id !== selectedComment.id)
        }));
        toast.success("Comment deleted successfully!");
      }
    } catch (error) {
      console.error("Error deleting comment:", error);
      toast.error("Failed to delete comment, please try again later.");
    } finally {
      setShowDeleteCommentModal(false);
      setSelectedComment(null);
    }
  };

  const handleEditConfirmed = async (postId) => {
    if (!selectedComment) return; // Ensure there’s a comment selected
  
    try {
      const updatedCommentData = {
        content: editCommentContent, // Use the state for the updated content
      };
  
      // Update the comment
      const response = await axiosInstance.put(`/PostService/${postId}/comments/${selectedComment.id}/${userId}`, updatedCommentData);
  
      if (response.status === 200) {
        // Update local state to reflect the edited comment
        const updatedPosts = posts.map(post =>
          post.id === postId
            ? {
                ...post,
                comments: post.comments.map(comment =>
                  comment.id === selectedComment.id ? { ...comment, content: editCommentContent } : comment
                ),
              }
            : post
        );
  
        setPosts(updatedPosts);
        setSelectedPost((prevPost) => ({
            ...prevPost,
            comments: prevPost.comments.map((comment) =>
                comment.id === selectedComment.id
                    ? { ...comment, content: editCommentContent }
                    : comment
            )
        }));
        setShowEditCommentModal(false);
        toast.success("Comment updated successfully!");
      }
    } catch (error) {
      console.error('Error updating comment:', error);
      toast.error("Failed to update comment, please try again later.");
    }
  };
  

  // Delete post
  const handleDeletePost = async () => {
    if (postToDelete === null) return;

    try {
      const response = await axiosInstance.delete(`/PostService/${postToDelete}/${userId}`);

      if (response.status === 200 || response.status === 204) {
        const updatedPosts = posts.filter((post) => post.id !== postToDelete);
        setPosts(updatedPosts);
        toast.success("Post deleted successfully!");
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        toast.error("You can only delete your own posts.");
      } else {
        toast.error("Failed to delete post, please try again later.");
      } 
    } finally {
      setShowDeleteModal(false); 
      setPostToDelete(null);
    }
  };

  const confirmDeletePost = (postId) => {
    setPostToDelete(postId);
    setShowDeleteModal(true);
  };

  // edit post
  const handleSaveEditedPost = async (postId) => {
    try {
      const updatedPost = {
        ...selectedPost,
        content: editedPostContent,
        tags: editedTags
      };
  
      const res = await axiosInstance.put(`/PostService/${postId}/${userId}`, updatedPost);
  
      if (res.status === 200 || res.status === 204) {
        const updatedPosts = posts.map(post => 
          post.id === postId ? { ...post, content: editedPostContent, tags: editedTags } : post
        );
        setPosts(updatedPosts); 
        setShowEditModal(false);
        toast.success("Post updated successfully!");
      }
    } catch (error) {
      console.error("Error updating post:", error);
      toast.error("Failed to update post, please try again later.");
    }
  };
  
  const handleEditPost = (post) => {
    setSelectedPost(post);
    setEditedPostContent(post.content);
    setEditedTags(post.tags);
    setShowEditModal(true);
  };
  
  // reset form
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
              <div className="col-sm-11">
                <Form.Group>
                  <Select
                    closeMenuOnSelect={false}
                    components={animatedComponents}
                    isMulti
                    options={tags.map(tag => ({ value: tag, label: tag }))}
                    value={selectedTags.map(tag => ({ value: tag, label: tag }))} // Add this line
                    onChange={(selectedOptions) => setSelectedTags(selectedOptions.map(option => option.value))}
                  />
                </Form.Group>
              </div>

              <div className="d-flex justify-content-end col-sm-1">
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
                  {post.likeCount} Like {}
                  <BiLike />
                </Button>

                <Button variant="btn btn-theme" onClick={() => handleShowComments(post)}>
                    Comment {}
                    <FaRegComment />
                </Button>

                {post.profileId == userId && (
                  <>
                  <Button variant="btn btn-theme" onClick={() => handleEditPost(post)} 
                    style={{ position: "absolute", top: "10px", right: "40px" }}
                  >
                    <FiEdit2/>
                  </Button>

                  <Button variant="btn btn-theme" onClick={() => confirmDeletePost(post.id)} 
                    style={{ position: "absolute", top: "10px", right: "10px" }}
                  >
                    <MdDeleteOutline/>
                  </Button>
                  </>
                )}

                {roleId === "ROLE_ADMIN" && (
                  <Button variant="btn btn-theme" onClick={() => confirmDeletePost(post.id)} 
                  style={{ position: "absolute", top: "10px", right: "10px" }}
                  >
                    <MdDeleteOutline/>
                  </Button>
                )}

              </div>
            </div>
          </div>
        </div>
      ))}
    </div>

        {selectedPost && (
          <Modal show={showModal} onHide={() => setShowModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Leave a comment</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">{selectedPost.profileName}</h5>
                  <p>{selectedPost.content}
                  {selectedPost.tags.map((tag) => (
                    <span key={tag} className="badge bg-secondary me-1">{tag}</span>
                  ))} 
                  </p>
                </div>
              </div>

              <div className="comments-list mt-3" > 
                <strong> Comments: </strong>
                {selectedPost.comments && selectedPost.comments.map((comment, index) => (
                      <div key={index} className="comment-item" style={{ 
                        position: "relative", 
                        padding: "10px", 
                        marginBottom: "10px", 
                        border: "1px solid #ccc", 
                        borderRadius: "8px",
                      }}>
                    <strong>{comment.profileName}</strong>: {comment.content}

                  {comment.profileId == userId && (
                  <>
                  <Button variant="btn btn-theme" onClick={() => handleEditComment(comment)} 
                    style={{ 
                      position: "absolute", 
                      right: "40px", 
                      top: "50%", 
                      transform: "translateY(-50%)", 
                      display: "flex", 
                      alignItems: "center", 
                      justifyContent: "center", 
                      height: "40px", 
                      width: "40px", 
                      padding: "0"
                    }}
                  >
                    <FiEdit2/>
                  </Button>

                  <Button variant="btn btn-theme" onClick={() => confirmDeleteComment(comment)} 
                    style={{ 
                      position: "absolute", 
                      right: "10px", 
                      top: "50%", 
                      transform: "translateY(-50%)", 
                      display: "flex", 
                      alignItems: "center", 
                      justifyContent: "center", 
                      height: "40px", 
                      width: "40px", 
                      padding: "0"
                    }}
                  >
                    <MdDeleteOutline/>
                  </Button>
                  </>
                  )}

                {roleId === "ROLE_ADMIN" && (
                  <Button variant="btn btn-theme" onClick={() => confirmDeleteComment(comment)} 
                    style={{ 
                      position: "absolute", 
                      right: "10px", 
                      top: "50%", 
                      transform: "translateY(-50%)", 
                      display: "flex", 
                      alignItems: "center", 
                      justifyContent: "center", 
                      height: "40px", 
                      width: "40px", 
                      padding: "0"
                    }}
                  >
                    <MdDeleteOutline/>
                  </Button>
                )}

                </div>
                ))}
              </div>
              
              <div className="row">
                <div className="col-sm-10">
                  <Form.Group className="mt-3">
                    <Form.Control
                      type="text"
                      placeholder="Add a comment..."
                      value={commentContent}
                      onChange={(e) => setCommentContent(e.target.value)}
                    />
                  </Form.Group>
                </div>
                <div className="col-sm-2 mt-3">
                  <Button variant="btn btn-outline-secondary w-100" style={{ borderColor: '#ccc' }} onClick={() => {handleAddComment(selectedPost);}}>
                    <IoMdSend />
                  </Button>
                </div>
              </div>
            </Modal.Body>
          </Modal>
        )}

        <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Delete</Modal.Title>
          </Modal.Header>
          <Modal.Body>Are you sure you want to delete this post?</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
              Cancel
            </Button>
            <Button variant="danger" onClick={handleDeletePost}>
              Delete
            </Button>
          </Modal.Footer>
        </Modal>

        <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Edit Post</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Control
                as="textarea"
                rows={3}
                value={editedPostContent}
                onChange={(e) => setEditedPostContent(e.target.value)}
              />
            </Form.Group>

            <Form.Group className="mt-3">
              <Select
                closeMenuOnSelect={false}
                components={animatedComponents}
                isMulti
                options={tags.map(tag => ({ value: tag, label: tag }))}
                value={editedTags.map(tag => ({ value: tag, label: tag }))}
                onChange={(selectedOptions) => setEditedTags(selectedOptions.map(option => option.value))}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowEditModal(false)}>
              Cancel
            </Button>
            <Button variant="primary" onClick={() => handleSaveEditedPost(selectedPost.id)}>
              Save Changes
            </Button>
          </Modal.Footer>
        </Modal>


        {/* edit comment */}
        {showEditCommentModal && (
        <Modal show={showEditCommentModal} onHide={() => setShowEditCommentModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Edit Comment</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Control
                as="textarea"
                rows={3}
                value={editCommentContent}
                onChange={(e) => setEditCommentContent(e.target.value)}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowEditCommentModal(false)}>
              Close
            </Button>
            <Button variant="primary" onClick={() => handleEditConfirmed(selectedPost.id)}>
              Save Changes
            </Button>
          </Modal.Footer>
        </Modal>
      )}


      {/* Delete comment */}
      <Modal show={showDeleteCommentModal} onHide={() => setShowDeleteCommentModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Delete</Modal.Title>
          </Modal.Header>
          <Modal.Body>Are you sure you want to delete this comment?</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowDeleteCommentModal(false)}>
              Cancel
            </Button>
            <Button variant="danger" onClick={handleDeleteComment}>
              Delete
            </Button>
          </Modal.Footer>
        </Modal>

      </div>
      <footer className="bg-dark text-white text-center py-3 mt-auto">
        <p>NeighbourlySG &copy; 2024. All rights reserved.</p>
      </footer>
    </div>
  );
}
export default CommunityPost;