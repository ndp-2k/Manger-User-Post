package com.manager.social_network.post.repository;

import com.manager.social_network.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN Friend f ON (p.userId = f.user OR p.userId = f.userSecond) " +
            "JOIN User u ON (p.userId = u.id AND u.deleteFlag = 0) " +
            "WHERE p.createAt >= :oneWeekAgo " +
            "AND p.deleteFlag = 0 " +
            "AND p.userId = :userId")
    List<Post> getNewFeed(
            @Param("userId") Long userId,
            @Param("oneWeekAgo") Instant oneWeekAgo
    );

    @Query("select count(p) FROM Post p WHERE p.userId=:userId AND p.createAt >= :oneWeekAgo AND p.deleteFlag=0")
    Long getCountPostInWeekByUserId(
            @Param("userId") Long userId,
            @Param("oneWeekAgo") Instant oneWeekAgo
    );

    @Query("SELECT " +
            "(SELECT COUNT(id) FROM Post WHERE userId = :userId AND createAt >= :lastWeek AND deleteFlag = 0) AS postCount, " +
            "(SELECT COUNT(id) FROM Comment WHERE userId = :userId AND createAt >= :lastWeek) AS commentCount, " +
            "(SELECT COUNT(id) FROM Friend WHERE (user = :userId OR userSecond = :userId) AND friendAt >= :lastWeek) AS friendCount, " +
            "(SELECT COUNT(id) FROM Like WHERE userId = :userId AND likeAt >= :lastWeek) AS likeCount ")
    Long[] getReport(Long userId, Instant lastWeek);
}