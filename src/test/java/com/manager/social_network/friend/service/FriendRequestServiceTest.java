package com.manager.social_network.friend.service;

import com.manager.social_network.friend.entity.FriendRequest;
import com.manager.social_network.friend.repository.FriendRequestRepository;
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

class FriendRequestServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testValidRequest() {
        Long from = 1L;
        Long to = 2L;

        when(friendRequestRepository.findByFromAndTo(from, to)).thenReturn(Optional.of(new FriendRequest()));

        assertTrue(friendRequestService.validRequest(from, to));
    }

    @Test
    void testCheckFriendRequestByUserId() {
        Long from = 1L;
        Long to = 2L;

        when(friendRequestRepository.findByFromAndTo(from, to)).thenReturn(Optional.of(new FriendRequest()));

        assertTrue(friendRequestService.checkFriendRequestByUserId(from, to));
    }

    @Test
    void testAddRequest() {
        Long from = 1L;
        Long to = 2L;

        friendRequestService.addRequest(from, to);

        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class));
    }

    @Test
    void testDelRequest() {
        Long from = 1L;
        Long to = 2L;

        when(friendRequestRepository.findByFromAndTo(from, to)).thenReturn(Optional.of(new FriendRequest()));

        friendRequestService.delRequest(from, to);

        verify(friendRequestRepository, times(1)).delete(any(FriendRequest.class));
    }

    @Test
    void testGetListRequest() {
        Long userId = 1L;
        User user = new User();
        user.setUsername("username");

        List<Long> friendRequestIds = new ArrayList<>();
        friendRequestIds.add(2L);
        friendRequestIds.add(3L);

        when(friendRequestRepository.findByTo(userId)).thenReturn(friendRequestIds);

        when(userService.userExits(2L)).thenReturn(true);
        when(userService.userExits(3L)).thenReturn(false);

        when(userService.findById(2L)).thenReturn(user);

        List<String> result = friendRequestService.getListRequest(userId);

        assertEquals(1, result.size());
        assertTrue(result.contains(user.getUsername()));
    }
}
