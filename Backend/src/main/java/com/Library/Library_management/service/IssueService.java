package com.Library.Library_management.service;

import com.Library.Library_management.dto.IssueRequest;
import com.Library.Library_management.dto.IssueResponse;
import com.Library.Library_management.entity.Book;
import com.Library.Library_management.entity.IssueRecord;
import com.Library.Library_management.entity.Member;
import com.Library.Library_management.enums.IssueStatus;
import com.Library.Library_management.exception.BookNotAvailableException;
import com.Library.Library_management.exception.InvalidReturnException;
import com.Library.Library_management.exception.MaxBookLimitExceededException;
import com.Library.Library_management.exception.ResourceNotFoundException;
import com.Library.Library_management.repository.BookRepository;
import com.Library.Library_management.repository.IssueRecordRepository;
import com.Library.Library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRecordRepository issueRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BookService bookService;

    @Transactional
    public IssueResponse issueBook(IssueRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is currently not available");
        }

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getMemberId()));

        long activeIssuesCount = issueRecordRepository.countByMemberAndStatus(member, IssueStatus.ISSUED);
        if (activeIssuesCount >= 3) {
            throw new MaxBookLimitExceededException("Member has reached the maximum limit of 3 issued books");
        }

        // Additional concurrency check - optimistic locking could be better, but double check works as simple prevention
        if (issueRecordRepository.existsByBookAndStatus(book, IssueStatus.ISSUED)) {
             throw new BookNotAvailableException("Book is currently not available");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        LocalDateTime now = LocalDateTime.now();
        IssueRecord issueRecord = IssueRecord.builder()
                .book(book)
                .member(member)
                .issueDate(now)
                .dueDate(now.plusDays(14)) // Configurable due date, assuming 14 days
                .status(IssueStatus.ISSUED)
                .build();

        issueRecord = issueRecordRepository.save(issueRecord);
        return mapToResponse(issueRecord);
    }

    @Transactional
    public IssueResponse returnBook(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found with id: " + issueId));

        if (issueRecord.getStatus() == IssueStatus.RETURNED) {
            throw new InvalidReturnException("Book is already returned for this issue record");
        }

        issueRecord.setStatus(IssueStatus.RETURNED);
        issueRecord.setReturnDate(LocalDateTime.now());
        issueRecordRepository.save(issueRecord);

        Book book = issueRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return mapToResponse(issueRecord);
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> getAllIssues() {
        return issueRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> getActiveIssues() {
        return issueRecordRepository.findByStatus(IssueStatus.ISSUED).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> getIssuesByMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found with id: " + memberId);
        }
        return issueRecordRepository.findByMember_MemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public IssueResponse mapToResponse(IssueRecord issueRecord) {
        return IssueResponse.builder()
                .issueId(issueRecord.getIssueId())
                .issueDate(issueRecord.getIssueDate())
                .dueDate(issueRecord.getDueDate())
                .returnDate(issueRecord.getReturnDate())
                .status(issueRecord.getStatus())
                .member(memberService.mapToResponse(issueRecord.getMember()))
                .book(bookService.mapToResponse(issueRecord.getBook()))
                .build();
    }
}
