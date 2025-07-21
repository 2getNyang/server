package com.project.nyang.modules.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.nyang.modules.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 알림 기능을 위한 DTO입니다.
 *
 * @author : 이지은
 * @fileName : NotificationDTO
 * @since : 25. 7. 17.
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    @Schema(description = "알림 ID")
    private Long notyId;

    @Schema(description = "알림 메세지")
    private String notyContent;

    @Schema(description = "알림 클릭시 이동할 URL")
    private String notyLink;

    @Schema(description = "알림 읽었는지 체크하는 필드")
    private Boolean isRead;

    @Schema(description = "알림 타입")
    private String notyType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "알림 생성 시간")
    private LocalDateTime notyCreatedAt;


    public NotificationDTO(Notification entity) {
        this.notyId = entity.getNotyId();
        this.notyContent = entity.getNotyContent();
        this.notyLink = entity.getNotyLink();
        this.notyType = entity.getType().name();
        this.isRead = entity.getIsRead();
        this.notyCreatedAt = entity.getNotyCreatedAt();
    }

}