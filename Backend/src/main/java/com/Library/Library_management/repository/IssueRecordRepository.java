package com.Library.Library_management.repository;

import com.Library.Library_management.entity.Book;
import com.Library.Library_management.entity.IssueRecord;
import com.Library.Library_management.entity.Member;
import com.Library.Library_management.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {
    long countByMemberAndStatus(Member member, IssueStatus status);
    List<IssueRecord> findByStatus(IssueStatus status);
    List<IssueRecord> findByMember_MemberId(Long memberId);
    boolean existsByBookAndStatus(Book book, IssueStatus status);
}
