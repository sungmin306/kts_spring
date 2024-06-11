package com.cloudschool.kts.domain.board.service;

import com.cloudschool.kts.domain.board.model.dto.request.BoardUpdateDto;
import com.cloudschool.kts.domain.board.model.dto.request.BoardWriteDto;
import com.cloudschool.kts.domain.board.model.dto.response.ResBoardDetailsDto;
import com.cloudschool.kts.domain.board.model.dto.response.ResBoardListDto;
import com.cloudschool.kts.domain.board.model.dto.response.ResBoardWriteDto;
import com.cloudschool.kts.domain.board.model.entity.Board;
import com.cloudschool.kts.domain.board.repository.BoardRepository;
import com.cloudschool.kts.domain.member.model.entity.Member;
import com.cloudschool.kts.domain.member.repository.MemberRepository;
import com.cloudschool.kts.global.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    public Page<ResBoardListDto> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllWithMemberAndComments(pageable);
        List<ResBoardListDto> list = boards.getContent().stream()
                .map(ResBoardListDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }



    public ResBoardWriteDto write(BoardWriteDto boardDTO, Member member) {

        Board board = BoardWriteDto.ofEntity(boardDTO);
        Member writerMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member Email", member.getEmail())
        );
        board.setMappingMember(writerMember);
        Board saveBoard = boardRepository.save(board);
        return ResBoardWriteDto.fromEntity(saveBoard, writerMember.getUsername());
    }


    public ResBoardDetailsDto detail(Long boardId) {
        Board findBoard = boardRepository.findByIdWithMemberAndCommentsAndFiles(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        // 조회수 증가
        findBoard.upViewCount();
        return ResBoardDetailsDto.fromEntity(findBoard);
    }


    public ResBoardDetailsDto update(Long boardId, BoardUpdateDto boardDTO) {
        Board updateBoard = boardRepository.findByIdWithMemberAndCommentsAndFiles(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        updateBoard.update(boardDTO.getTitle(), boardDTO.getContent());
        return ResBoardDetailsDto.fromEntity(updateBoard);
    }

    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}
