package com.Library.Library_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueRequest {
    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    private Long memberId;
}
