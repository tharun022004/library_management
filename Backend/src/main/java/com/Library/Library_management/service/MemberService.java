package com.Library.Library_management.service;

import com.Library.Library_management.dto.MemberRequest;
import com.Library.Library_management.dto.MemberResponse;
import com.Library.Library_management.entity.Member;
import com.Library.Library_management.exception.DuplicateEmailException;
import com.Library.Library_management.exception.ResourceNotFoundException;
import com.Library.Library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        member = memberRepository.save(member);
        return mapToResponse(member);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return mapToResponse(member);
    }

    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        
        if (!member.getEmail().equals(request.getEmail()) && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member = memberRepository.save(member);
        return mapToResponse(member);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    public MemberResponse mapToResponse(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
