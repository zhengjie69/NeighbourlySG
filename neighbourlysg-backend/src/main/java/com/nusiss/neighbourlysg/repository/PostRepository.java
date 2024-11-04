package com.nusiss.neighbourlysg.repository;


import com.nusiss.neighbourlysg.entity.Post;
import com.nusiss.neighbourlysg.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByProfile(Profile profile);
}
