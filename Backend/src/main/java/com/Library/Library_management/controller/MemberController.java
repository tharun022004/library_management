package com.Library.Library_management.controller;

import com.Library.Library_management.dto.ApiResponse;
import com.Library.Library_management.dto.IssueResponse;
import com.Library.Library_management.dto.MemberRequest;
import com.Library.Library_management.dto.MemberResponse;
import com.Library.Library_management.service.IssueService;
import com.Library.Library_management.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Member management APIs")
public class MemberController {

    private final MemberService memberService;
    private final IssueService issueService;

    @PostMapping("/add")
    @Operation(summary = "Create a new member")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Member created successfully", response),
                HttpStatus.CREATED);
    }

    @GetMapping("/get")
    @Operation(summary = "Get all members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> response = memberService.getAllMembers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Members retrieved successfully", response));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get a member by ID")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long id) {
        MemberResponse response = memberService.getMemberById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Member retrieved successfully", response));
    }

    @GetMapping("/get/{id}/issues")
    @Operation(summary = "Get all issues by a member")
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getIssuesByMember(@PathVariable Long id) {
        List<IssueResponse> response = issueService.getIssuesByMember(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Issues retrieved successfully", response));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a member")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.updateMember(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Member updated successfully", response));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a member")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Member deleted successfully", null));
    }
}
