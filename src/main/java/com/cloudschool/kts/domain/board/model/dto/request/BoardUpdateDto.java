package com.cloudschool.kts.domain.board.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDto {

    private String title;
    private String content;

    @Builder
    public BoardUpdateDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
    }
}