package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.Comment;
import com.nusiss.neighbourlysg.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
