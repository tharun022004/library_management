package com.Library.Library_management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberResponse {
    private Long memberId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
