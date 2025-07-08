package com.project.nyang.modules.image.entity;

import com.project.nyang.modules.board.entity.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 *
 * Image 엔티티 클래스입니다.
 * @fileName        : Image
 * @author          : 선순주
 * @since           : 2025-07-07
 *
 */
@Entity
@Table(name = "S3_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "origin_file_name")
    private String originFileName;

    @Column(name="s3_url", nullable = false, updatable = false)
    private String s3Url;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "thumbnail_is", length = 1)
    private String thumbnailIs;

    @Builder
    public Image(Board board, String originFileName,
                    String s3Url, String fileSize,
                    String thumbnailIs) {
        this.board = board;
        this.originFileName = originFileName;
        this.s3Url = s3Url;
        this.fileSize = fileSize;
        this.thumbnailIs = thumbnailIs;
    }


}
