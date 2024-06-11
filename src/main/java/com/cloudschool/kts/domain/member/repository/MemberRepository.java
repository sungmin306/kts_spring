package com.cloudschool.kts.domain.member.repository;

import com.cloudschool.kts.domain.member.model.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findByEmail(String Email);
    Optional <Member> findById(Long id);
//    void deleteById(Long id);
//
//    Boolean existsByEmail(String email);
}
