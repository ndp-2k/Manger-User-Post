package com.manager.social_network.friend.repository;

import com.manager.social_network.friend.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findByFromAndTo(Long from, Long to);

    @Query("SELECT friend.from from FriendRequest friend WHERE friend.to= :userId")
    List<Long> findByTo(Long userId);
}
