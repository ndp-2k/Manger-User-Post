package com.manager.social_network.post.repository;

import com.manager.social_network.post.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndTheIdAndType(Long userId, Long postId, String post);
    @Query("select count(l) FROM Like l WHERE l.userId=:userId AND l.likeAt >= :lastWeek")
    Long getCountLikeInWeekByUserId(Long userId, Instant lastWeek);
}
