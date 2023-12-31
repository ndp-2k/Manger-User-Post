package com.manager.social_network.user.respository;

import com.manager.social_network.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndDeleteFlag(Long id,int deleteFlag);

    Optional<User> findByUsernameAndDeleteFlag(String userName, int i);
}
