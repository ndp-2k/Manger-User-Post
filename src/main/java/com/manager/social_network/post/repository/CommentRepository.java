package com.manager.social_network.post.repository;

import com.manager.social_network.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndUserId(Long id, Long userid);

    @Query("select count(c) FROM Comment c WHERE c.userId=:userId AND c.createAt >= :lastWeek")
    Long getCountCommentInWeekByUserId(Long userId, Instant lastWeek);
}