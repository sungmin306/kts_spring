package com.cloudschool.kts.domain.comment.model.dto.request;

import com.cloudschool.kts.domain.comment.model.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private String content;

    @Builder
    public CommentDto(String content) {
        this.content = content;
    }

    public static Comment ofEntity(CommentDto dto) {
        return Comment.builder()
                .content(dto.getContent())
                .build();
    }
}
