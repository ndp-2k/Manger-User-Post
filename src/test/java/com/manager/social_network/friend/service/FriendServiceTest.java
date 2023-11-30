package com.manager.social_network.friend.service;

import com.manager.social_network.friend.entity.Friend;
import com.manager.social_network.friend.repository.FriendRepository;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddFriend() {
        Long user = 1L;
        Long userSecond = 2L;

        friendService.addFriend(user, userSecond);

        verify(friendRepository, times(1)).save(any(Friend.class));
    }

    @Test
    void testIsFriend() {
        Long user = 1L;
        Long userSecond = 2L;

        when(friendRepository.findByUserAndUserSecond(user, userSecond)).thenReturn(Optional.of(new Friend()));

        assertTrue(friendService.isFriend(user, userSecond));
    }

    @Test
    void testUnFriend() {
        Long user = 1L;
        Long userSecond = 2L;

        when(friendRepository.findByUserAndUserSecond(user, userSecond)).thenReturn(Optional.of(new Friend()));

        friendService.unFriend(user, userSecond);

        verify(friendRepository, times(1)).delete(any(Friend.class));
    }

    @Test
    void testUnFriendCase2() {
        Long user = 1L;
        Long userSecond = 2L;

        when(friendRepository.findByUserAndUserSecond(user, userSecond)).thenReturn(Optional.empty());
        when(friendRepository.findByUserAndUserSecond(userSecond, user)).thenReturn(Optional.of(new Friend()));

        friendService.unFriend(user, userSecond);

        verify(friendRepository, times(1)).delete(any(Friend.class));
    }

    @Test
    void testGetListFriend() {
        Long userId = 1L;
        User user = new User();
        user.setUsername("username");
        List<Long> friendIds = new ArrayList<>();
        friendIds.add(2L);
        friendIds.add(3L);

        when(friendRepository.findFromAndToByValue(userId)).thenReturn(friendIds);

        when(userService.userExits(2L)).thenReturn(true);
        when(userService.userExits(3L)).thenReturn(false);

        when(userService.findById(2L)).thenReturn(user);

        List<String> result = friendService.getListFriend(userId);

        assertEquals(1, result.size());
        assertTrue(result.contains(user.getUsername()));
    }
}
