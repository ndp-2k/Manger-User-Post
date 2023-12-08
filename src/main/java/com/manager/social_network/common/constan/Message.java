package com.manager.social_network.common.constan;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Message {

    public static final String SUCCESS = "Success";
    public static final String STATUS = "status";
    public static final String ACCEPT_STATUS = "Hai bạn đã trở thành bạn bè";
    public static final String NOT_ALLOWED = "Không có quyền";
    public static final String NOT_FOUND_POST = "Không tìm thấy bài viết";
    public static final String NOT_FOUND_COMMENT = "khong tim thay comment";
    public static final String NOT_FOUND_USER = "Không tìm thấy người dùng";
    public static final String FRIEND_REQUEST_DUPLICATE = "Lời mời kết bạn đã được gửi từ trướ́c !";
    public static final String ALREADY_FRIEND = "Hai người đã là bạn bè";
    public static final String NOT_ALREADY_FRIEND = "Hai nguoi khong phai la ban be";
    public static final String NOT_FOUND_REQUEST = "Không tìm thấy lời mời đã gửi tới người này";
    public static final String UN_REQUEST = "Hủy lời mời thành công";
    public static final String LIKE_POST = "Đã like bài viết";
    public static final String UN_LIKE_POST = "Đã un like bài viết";
    public static final String ERROR = "error";
    public static final String POST = "post";
    public static final String AVT = "avt";
    public static final Instant LAST_WEEK = Instant.now().minus(7, ChronoUnit.DAYS);
}
