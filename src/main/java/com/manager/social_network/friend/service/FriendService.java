package com.manager.social_network.friend.service;

import com.manager.social_network.friend.entity.Friend;
import com.manager.social_network.friend.repository.FriendRepository;
import com.manager.social_network.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FriendService {
    FriendRepository friendRepository;
    UserService userService;

    public void addFriend(Long user, Long userSecond) {
        Friend friend = new Friend();
        friend.setUser(user);
        friend.setUserSecond(userSecond);
        friend.setFriendAt(Instant.now());
        friendRepository.save(friend);
    }

    public boolean friendExits(Long user, Long userSecond) {
        return friendRepository.findByUserAndUserSecond(user, userSecond).isPresent() || friendRepository.findByUserAndUserSecond(userSecond, user).isPresent();
    }

    public void unFriend(Long user, Long userSecond) {
        if (friendRepository.findByUserAndUserSecond(user, userSecond).isPresent()) {
            friendRepository.delete(friendRepository.findByUserAndUserSecond(user, userSecond).get());
        } else {
            friendRepository.delete(friendRepository.findByUserAndUserSecond(userSecond, user).get());
        }
    }

    public List<String> getListFriend(Long userId) {
        List<String> list = new ArrayList<>();
        for (Long id : friendRepository.findFromAndToByValue(userId)) {
            if (userService.userExits(id)) {
                list.add(userService.findById(id).getUsername());
            }
        }
        return list;
    }
}
