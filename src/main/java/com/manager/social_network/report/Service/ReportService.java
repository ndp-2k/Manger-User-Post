package com.manager.social_network.report.Service;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.friend.repository.FriendRepository;
import com.manager.social_network.post.repository.CommentRepository;
import com.manager.social_network.post.repository.LikeRepository;
import com.manager.social_network.post.repository.PostRepository;
import com.manager.social_network.report.entity.ReportEntity;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
    public static final String EXCEL_FILE_PATH = "C:/Report.xlsx";
    PostRepository postRepository;
    FriendRepository friendRepository;
    LikeRepository likeRepository;
    CommentRepository commentRepository;


    public void createReportExcelFile(Long userId, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {

            List<String> data = Arrays.asList("Tổng bài viết", "Tổng comment", "Tổng new Friend", "Tổng like");
            Sheet sheet = workbook.createSheet("Thống kê tuần qua");
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < data.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(data.get(i));

            }
            ReportEntity report = getStatistical(userId);
            Long[] result = {
                    report.getSumPost(),
                    report.getSumComment(),
                    report.getSumNewFriend(),
                    report.getSumLike()
            };

            Row dataRow = sheet.createRow(1);
            for (int cellIndex = 0; cellIndex < result.length; cellIndex++) {
                Cell cell = dataRow.createCell(cellIndex);
                cell.setCellValue(result[cellIndex]);
            }
            sheet.setDefaultColumnWidth(13);
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
        }
    }

    public ReportEntity getStatistical(Long userId) {
        ReportEntity report = new ReportEntity();
        report.setSumPost(postRepository.getCountPostInWeekByUserId(userId, Message.LAST_WEEK));
        report.setSumComment(commentRepository.getCountCommentInWeekByUserId(userId, Message.LAST_WEEK));
        report.setSumNewFriend(friendRepository.getCountFriendInWeekByUserId(userId, Message.LAST_WEEK));
        report.setSumLike(likeRepository.getCountLikeInWeekByUserId(userId, Message.LAST_WEEK));
        return report;
    }
}