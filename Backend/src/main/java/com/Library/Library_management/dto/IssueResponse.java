package com.Library.Library_management.dto;

import com.Library.Library_management.enums.IssueStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IssueResponse {
    private Long issueId;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private IssueStatus status;
    private MemberResponse member;
    private BookResponse book;
}
