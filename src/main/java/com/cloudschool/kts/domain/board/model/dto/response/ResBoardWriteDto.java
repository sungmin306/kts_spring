package com.cloudschool.kts.domain.board.model.dto.response;

import com.cloudschool.kts.domain.board.model.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResBoardWriteDto {

    private Long boardId;
    private String title;
    private String content;
    private String writerName;
    private String createdDate;

    @Builder
    public ResBoardWriteDto(Long boardId, String title, String content, String writerName, String createdDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.createdDate = createdDate;
    }

    public static ResBoardWriteDto fromEntity(Board board, String writerName) {
        return ResBoardWriteDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .writerName(writerName)
                .createdDate(board.getCreatedDate())
                .build();
    }
}