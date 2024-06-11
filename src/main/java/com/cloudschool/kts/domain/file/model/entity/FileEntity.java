package com.cloudschool.kts.domain.file.model.entity;

import com.cloudschool.kts.domain.board.model.entity.Board;
import com.cloudschool.kts.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE")
@Getter
@NoArgsConstructor
public class FileEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column()
    private Long id;

    @Column()
    private String originFileName;

    @Column()
    private String fileType;

    @Column()
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    public Board board;

    @Builder
    public FileEntity(Long id, String originFileName, String filePath, String fileType) {
        this.id = id;
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public void setMappingBoard(Board board) {
        this.board = board;
    }
}