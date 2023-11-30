package com.manager.social_network.report.Service;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.friend.repository.FriendRepository;
import com.manager.social_network.post.repository.CommentRepository;
import com.manager.social_network.post.repository.LikeRepository;
import com.manager.social_network.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReportExcelFile() {
        // Mock data
        Long userId = 1L;
        String filePath = "C:/TestReport.xlsx";

        // Mock the behavior of getCountPostInWeekByUserId and other repository methods
        when(postRepository.getCountPostInWeekByUserId(anyLong(), any())).thenReturn(5L);
        when(commentRepository.getCountCommentInWeekByUserId(anyLong(), any())).thenReturn(10L);
        when(friendRepository.getCountFriendInWeekByUserId(anyLong(), any())).thenReturn(15L);
        when(likeRepository.getCountLikeInWeekByUserId(anyLong(), any())).thenReturn(20L);

        // Call the method to test
        reportService.createReportExcelFile(userId, filePath);

        // Verify that the repository methods were called with the correct arguments
        verify(postRepository, times(1)).getCountPostInWeekByUserId(userId, Message.LAST_WEEK);
        verify(commentRepository, times(1)).getCountCommentInWeekByUserId(userId, Message.LAST_WEEK);
        verify(friendRepository, times(1)).getCountFriendInWeekByUserId(userId, Message.LAST_WEEK);
        verify(likeRepository, times(1)).getCountLikeInWeekByUserId(userId, Message.LAST_WEEK);
    }

    // You can write similar tests for other methods in ReportService
}
