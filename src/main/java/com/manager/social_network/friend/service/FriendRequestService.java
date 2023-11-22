package com.manager.social_network.friend.service;

import com.manager.social_network.friend.entity.FriendRequest;
import com.manager.social_network.friend.repository.FriendRequestRepository;
import com.manager.social_network.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FriendRequestService {
    UserService userService;
    FriendRequestRepository friendRequestRepository;

    public boolean validRequest(Long from, Long to) {
        return friendRequestRepository.findByFromAndTo(from, to).isPresent();
    }

    public boolean checkFriendRequestByUserId(Long from, Long to) {
        return friendRequestRepository.findByFromAndTo(from, to).isPresent();
    }

    public void addRequest(Long from, Long to) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFrom(from);
        friendRequest.setTo(to);
        friendRequest.setCreateAt(Instant.now());
        friendRequestRepository.save(friendRequest);
    }

    public void delRequest(Long from, Long to) {
        friendRequestRepository.delete(friendRequestRepository.findByFromAndTo(from, to).get());
    }

    public List<String> getListRequest(Long userId) {
        List<String> list = new ArrayList<>();
        for (Long id : friendRequestRepository.findByTo(userId)) {
            if (userService.userExits(id)) {
                list.add(userService.findById(id).getUsername());
            }
        }
        return list;
    }
}
