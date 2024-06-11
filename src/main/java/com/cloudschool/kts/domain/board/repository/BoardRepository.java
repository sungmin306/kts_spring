package com.cloudschool.kts.domain.board.repository;

import com.cloudschool.kts.domain.board.model.entity.Board;
import com.cloudschool.kts.domain.member.model.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.boardId = :boardID")
    Optional<Board> findByIdWithMemberAndCommentsAndFiles(Long boardID);


    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member")
    Page<Board> findAllWithMemberAndComments(Pageable pageable);

}
