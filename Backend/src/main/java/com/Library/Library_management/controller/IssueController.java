package com.Library.Library_management.controller;

import com.Library.Library_management.dto.ApiResponse;
import com.Library.Library_management.dto.IssueRequest;
import com.Library.Library_management.dto.IssueResponse;
import com.Library.Library_management.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@Tag(name = "Issues", description = "Book issue and return APIs")
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/issue-book")
    @Operation(summary = "Issue a book to a member")
    public ResponseEntity<ApiResponse<IssueResponse>> issueBook(@Valid @RequestBody IssueRequest request) {
        IssueResponse response = issueService.issueBook(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Book issued successfully", response), HttpStatus.CREATED);
    }

    @PutMapping("/{issueId}/return")
    @Operation(summary = "Return an issued book")
    public ResponseEntity<ApiResponse<IssueResponse>> returnBook(@PathVariable Long issueId) {
        IssueResponse response = issueService.returnBook(issueId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book returned successfully", response));
    }

    @GetMapping("issue-records")
    @Operation(summary = "Get all issue records")
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getAllIssues() {
        List<IssueResponse> response = issueService.getAllIssues();
        return ResponseEntity.ok(new ApiResponse<>(true, "Issues retrieved successfully", response));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active issue records")
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getActiveIssues() {
        List<IssueResponse> response = issueService.getActiveIssues();
        return ResponseEntity.ok(new ApiResponse<>(true, "Active issues retrieved successfully", response));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get all issue records for a specific member")
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getIssuesByMemberId(@PathVariable Long memberId) {
        List<IssueResponse> response = issueService.getIssuesByMember(memberId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Issues for member retrieved successfully", response));
    }
}
