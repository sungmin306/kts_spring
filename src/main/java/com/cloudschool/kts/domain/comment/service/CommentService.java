package com.cloudschool.kts.domain.comment.service;

import com.cloudschool.kts.domain.board.model.entity.Board;
import com.cloudschool.kts.domain.board.repository.BoardRepository;
import com.cloudschool.kts.domain.comment.model.dto.request.CommentDto;
import com.cloudschool.kts.domain.comment.model.dto.response.ResCommentDto;
import com.cloudschool.kts.domain.comment.model.entity.Comment;
import com.cloudschool.kts.domain.comment.repository.CommentRepository;
import com.cloudschool.kts.domain.member.model.entity.Member;
import com.cloudschool.kts.domain.member.repository.MemberRepository;
import com.cloudschool.kts.global.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<ResCommentDto> getAllComments(Pageable pageable, Long boardId) {
        Page<Comment> comments = commentRepository.findAllWithMemberAndBoard(pageable, boardId);
        List<ResCommentDto> commentList = comments.getContent().stream()
                .map(ResCommentDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(commentList, pageable, comments.getTotalElements());
    }

    public ResCommentDto write(Long boardId, Member member, CommentDto writeDto) {
        // board 정보 검색
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board id", String.valueOf(boardId))
        );
        // member(댓글 작성자) 정보 검색
        Member commentWriter = memberRepository.findById(member.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member id", String.valueOf(member.getId()))
        );
        // Entity 변환, 연관관계 매핑
        Comment comment = CommentDto.ofEntity(writeDto);
        comment.setBoard(board);
        comment.setMember(commentWriter);

        Comment saveComment = commentRepository.save(comment);
        return ResCommentDto.fromEntity(saveComment);
    }

    public ResCommentDto update(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdWithMemberAndBoard(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Comment Id", String.valueOf(commentId))
        );
        comment.update(commentDto.getContent());
        return ResCommentDto.fromEntity(comment);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}