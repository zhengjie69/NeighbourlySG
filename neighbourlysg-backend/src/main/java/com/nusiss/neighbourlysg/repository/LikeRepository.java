package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByProfileIdAndPostId(Long profileId, Long postId);
    List<Like> findAllByPostId(Long postId);
    int countByPostId(Long postId);
}
