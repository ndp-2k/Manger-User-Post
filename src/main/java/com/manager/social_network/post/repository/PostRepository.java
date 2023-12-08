package com.manager.social_network.post.repository;

import com.manager.social_network.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN Friend f ON (p.userId = f.user OR p.userId = f.userSecond OR p.userId = :userId) " +
            "JOIN User u ON (p.userId = u.id AND u.deleteFlag = 0) " +
            "WHERE (p.userId = :userId OR f.user = :userId OR f.userSecond = :userId) " +
            "AND p.createAt >= :oneWeekAgo " +
            "AND p.deleteFlag = 0")
    Page<Post> getNewFeed(
            Long userId,
            Instant oneWeekAgo,
            Pageable pageable
    );

    @Query("select count(p) FROM Post p WHERE p.userId=:userId AND p.createAt >= :oneWeekAgo AND p.deleteFlag=0  ORDER BY p.createAt ASC")
    Long getCountPostInWeekByUserId(
            @Param("userId") Long userId,
            @Param("oneWeekAgo") Instant oneWeekAgo
    );

}