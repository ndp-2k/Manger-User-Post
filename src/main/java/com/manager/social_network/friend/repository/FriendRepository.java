package com.manager.social_network.friend.repository;

import com.manager.social_network.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserAndUserSecond(Long user, Long userSecond);

    @Query("SELECT fr.user FROM Friend fr WHERE fr.userSecond = :value " +
            "UNION " +
            "SELECT fr.userSecond FROM Friend fr WHERE fr.user = :value")
    List<Long> findFromAndToByValue(Long value);
}
