package com.manager.social_network.friend.controller;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.friend.entity.Friend;
import com.manager.social_network.friend.entity.FriendRequest;
import com.manager.social_network.friend.service.FriendRequestService;
import com.manager.social_network.friend.service.FriendService;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendRequestControllerTest {

    private final MockMvc mockMvc;
    String username;
    User user;
    @Mock
    private UserService userService;
    @Mock
    private FriendService friendService;
    @Mock
    private FriendRequestService friendRequestService;
    @Mock
    private Common common;
    @InjectMocks
    private FriendRequestController friendRequestController;

    FriendRequestControllerTest() {
        username = "usernameTest";
        user = new User();
        user.setId(27L);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(friendRequestController).build();
    }

    @Test
    void testAcceptFriend() throws Exception {
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendRequestService.checkFriendRequestByUserId(user.getId(), 12L)).thenReturn(true);
        doNothing().when(friendRequestService).delRequest(user.getId(), 12L);
        doNothing().when(friendService).addFriend(user.getId(), 12L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/accept-friend/{user_name}", username))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testAcceptFriend_WhenNotFoundFriend() throws Exception {
        when(userService.userExits(username)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/accept-friend/{user_name}", username))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.NOT_FOUND_USER));

    }

    @Test
    void testAcceptFriend_WhenNotFoundRequest() throws Exception {
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendRequestService.checkFriendRequestByUserId(user.getId(), 12L)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/accept-friend/{user_name}", username))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.NOT_FOUND_REQUEST));

    }

    @Test
    void testDenyFriendRequest_FriendRequestNotFound() throws Exception {
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(any())).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendRequestService.checkFriendRequestByUserId(user.getId(), 12L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/deny-friend/{user_name}", username))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.NOT_FOUND_REQUEST));
    }

    @Test
    void testDenyFriendRequest_Success() throws Exception {

        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendRequestService.checkFriendRequestByUserId(user.getId(), 12L)).thenReturn(true);
        doNothing().when(friendRequestService).delRequest(user.getId(), 12L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/deny-friend/{user_name}", username))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testDenyFriendRequest_NotFoundFriend() throws Exception {
        String username = "usernameTest";

        when(userService.userExits(username)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/deny-friend/{user_name}", username))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.NOT_FOUND_USER));
    }

    @Test
    void testAddFriendSuccess() throws Exception {
        String username = "usernameTest";
        // Mocking the necessary services
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendService.isFriend(user.getId(), 12L)).thenReturn(false);
        when(friendRequestService.validRequest(12L, user.getId())).thenReturn(false);
        when(friendRequestService.checkFriendRequestByUserId(user.getId(), 12L)).thenReturn(false);
        doNothing().when(friendRequestService).addRequest(12L, user.getId());

        // Your test logic here using mockMvc.perform() and expectations
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/add-friend/{user_name}", username)
                        .header("Authorization", "Bearer yourAuthToken"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Message.SUCCESS));
    }

    @Test
    void testAddFriendNotFoundUser() throws Exception {
        when(userService.userExits(username)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/add-friend/{user_name}", username)
                        .header("Authorization", "Bearer yourAuthToken"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.NOT_FOUND_USER));
    }

    @Test
    void testAddFriendAlreadyFriend() throws Exception {
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendService.isFriend(user.getId(), 12L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/add-friend/{user_name}", username)
                        .header("Authorization", "Bearer yourAuthToken"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.ALREADY_FRIEND));
    }

    @Test
    void testAddFriendDuplicateRequest() throws Exception {
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendService.isFriend(user.getId(), 12L)).thenReturn(false);
        when(friendRequestService.validRequest(12L, user.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/add-friend/{user_name}", username)
                        .header("Authorization", "Bearer yourAuthToken"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Message.FRIEND_REQUEST_DUPLICATE));
    }

    @Test
    void testAddFriendReplyRequest() throws Exception {
        when(userService.userExits(username)).thenReturn(true);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(common.getUserIdByToken(any())).thenReturn(12L);
        when(friendService.isFriend(user.getId(), 12L)).thenReturn(false);
        when(friendRequestService.validRequest(12L, user.getId())).thenReturn(false);
        when(friendRequestService.checkFriendRequestByUserId(user.getId(), 12L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/add-friend/{user_name}", username)
                        .header("Authorization", "Bearer yourAuthToken"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Message.ACCEPT_STATUS));
    }


    @Test
    void testForEntity() {
        Friend friend = new Friend();
        friend.setId(1L);
        friend.setUser(12L);
        friend.setUserSecond(27L);
        friend.setFriendAt(Instant.now());

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(1L);
        friendRequest.setTo(27L);
        friendRequest.setFrom(12L);
        friendRequest.setCreateAt(Instant.now());

        friend.getId();
        friend.getUser();
        friend.getFriendAt();
        friend.getUserSecond();
        friendRequest.getFrom();

        friendRequest.getId();
        friendRequest.getTo();
        friendRequest.getFrom();
        friendRequest.getCreateAt();
    }

}
