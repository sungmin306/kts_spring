package com.cloudschool.kts.domain.board.model.entity;


import com.cloudschool.kts.domain.comment.model.entity.Comment;
import com.cloudschool.kts.domain.file.model.entity.FileEntity;
import com.cloudschool.kts.domain.member.model.entity.Member;
import com.cloudschool.kts.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false, length = 26)
    private String title;

    @Lob
    private String content;

    @ColumnDefault("0")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="authorId")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public List<FileEntity> files = new ArrayList<>();


    @Builder
    public Board(Long id, String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.member = member;
    }

    //==비즈니스 로직==//

    /**
     * 조회수 증가
     */
    public void upViewCount() {
        this.viewCount++;
    }

    /**
     * 업데이트 더디 채킹
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //==연관관계 편의 메서드==//
    public void setMappingMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

}
