package com.manager.social_network.friend.controller;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.friend.service.FriendRequestService;
import com.manager.social_network.friend.service.FriendService;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/friends")
@AllArgsConstructor
@Tag(name = "Friends Controller Manager")
public class FriendRequestController {
    UserService userService;
    FriendService friendService;
    FriendRequestService friendRequestService;
    private Common common;

    @Operation(summary = "Đồng ý lời mời kết bạn")
    @PostMapping("accept-friend/{user_name}")
    public ResponseEntity<Map<String, Object>> acceptFriend(
            @PathVariable(name = "user_name") @ApiParam(required = true) String userName,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!userService.userExits(userName)) {
            response.put(Message.ERROR, Message.NOT_FOUND_USER);
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }
        Long id = userService.findByUsername(userName).get().getId();
        if (!friendRequestService.checkFriendRequestByUserId(id, common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.NOT_FOUND_REQUEST);
            status = HttpStatus.NOT_FOUND;
        } else {
            friendRequestService.delRequest(id, common.getUserIdByToken(request));
            friendService.addFriend(id, common.getUserIdByToken(request));
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }

    @Operation(summary = "Từ chối lời mời kết bạn")
    @DeleteMapping("deny-friend/{user_name}")
    public ResponseEntity<Map<String, Object>> denyFriend(
            @PathVariable(name = "user_name") String userName,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!userService.userExits(userName)) {
            response.put(Message.ERROR, Message.NOT_FOUND_USER);
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }
        Long id = userService.findByUsername(userName).get().getId();
        if (!friendRequestService.checkFriendRequestByUserId(id, common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.NOT_FOUND_REQUEST);
            status = HttpStatus.NOT_FOUND;
        } else {
            friendRequestService.delRequest(id, common.getUserIdByToken(request));
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }

    @Operation(summary = "Gửi lời mời kết bạn")
    @PostMapping("add-friend/{user_name}")
    public ResponseEntity<Map<String, Object>> addFriend(
            @PathVariable(name = "user_name") String userName,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!userService.userExits(userName)) {
            response.put(Message.ERROR, Message.NOT_FOUND_USER);
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }
        Long id = userService.findByUsername(userName).get().getId();
        if (friendService.isFriend(id, common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.ALREADY_FRIEND);
            status = HttpStatus.BAD_REQUEST;
        } else if (friendRequestService.validRequest(common.getUserIdByToken(request), id)) {
            response.put(Message.ERROR, Message.FRIEND_REQUEST_DUPLICATE);
            status = HttpStatus.BAD_REQUEST;
        } else if (friendRequestService.checkFriendRequestByUserId(id, common.getUserIdByToken(request))) {
            friendService.addFriend(common.getUserIdByToken(request), id);
            friendRequestService.delRequest(common.getUserIdByToken(request), id);
            response.put(Message.STATUS, Message.ACCEPT_STATUS);
        } else {
            friendRequestService.addRequest(common.getUserIdByToken(request), id);
            response.put(Message.STATUS, Message.SUCCESS);
        }

        return new ResponseEntity<>(response, status);
    }


    @Operation(summary = "Xóa bạn bè")
    @DeleteMapping("un-friend/{user_name}")
    public ResponseEntity<Map<String, Object>> unFriend(
            @PathVariable(name = "user_name") String userName,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!userService.userExits(userName)) {
            response.put(Message.ERROR, Message.NOT_FOUND_USER);
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }

        Long id = userService.findByUsername(userName).get().getId();
        if (!friendService.isFriend(id, common.getUserIdByToken(request))) {
            response.put(Message.ERROR, Message.NOT_ALREADY_FRIEND);
            status = HttpStatus.BAD_REQUEST;
        } else {
            friendService.unFriend(common.getUserIdByToken(request), id);
            response.put(Message.STATUS, Message.SUCCESS);
        }
        return new ResponseEntity<>(response, status);
    }


    @Operation(summary = "Hủy lời mời kết bạn")
    @DeleteMapping("un-request-friend/{user_name}")
    public ResponseEntity<Map<String, Object>> unRequestFriend(
            @PathVariable(name = "user_name") String userName,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        if (!userService.userExits(userName)) {
            response.put(Message.ERROR, Message.NOT_FOUND_USER);
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }
        Long id = userService.findByUsername(userName).get().getId();
        if (!friendRequestService.checkFriendRequestByUserId(common.getUserIdByToken(request), id)) {
            response.put(Message.ERROR, Message.NOT_FOUND_REQUEST);
            status = HttpStatus.NOT_FOUND;
        } else {
            friendRequestService.delRequest(common.getUserIdByToken(request), id);
            response.put(Message.STATUS, Message.UN_REQUEST);
        }
        return new ResponseEntity<>(response, status);
    }

    @Operation(summary = "Xem danh sách lời mời kết bạn")
    @GetMapping("list-request-friend")
    public List<String> listRequestFriend(
            HttpServletRequest request
    ) {
        return friendRequestService.getListRequest(common.getUserIdByToken(request));
    }

    @Operation(summary = "Xem danh sách bạn bè")
    @GetMapping("list-friend")
    public List<String> listFriend(
            HttpServletRequest request
    ) {
        return friendService.getListFriend(common.getUserIdByToken(request));
    }

    @GetMapping("/info/{user_name}")
    @Operation(summary = "Thông tin bạn bè")
    @PermitAll
    public ResponseEntity<Object> infoFriend(
            @PathVariable(name = "user_name") String userName,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status;

        if (!userService.userExits(userName)) {
            response.put(Message.ERROR, Message.NOT_FOUND_USER);
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        }
        Long id = userService.findByUsername(userName).get().getId();
        if (!friendService.isFriend(common.getUserIdByToken(request), id)) {
            response.put(Message.ERROR, Message.NOT_ALREADY_FRIEND);
            status = HttpStatus.BAD_REQUEST;
        } else {
            return new ResponseEntity<>(userService.findByUsername(userName).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, status);
    }

}
