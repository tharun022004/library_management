package com.Library.Library_management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookResponse {
    private Long bookId;
    private String title;
    private String author;
    private boolean available;
    private LocalDateTime createdAt;
}
