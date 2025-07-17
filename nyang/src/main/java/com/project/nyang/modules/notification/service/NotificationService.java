package com.project.nyang.modules.notification.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.notification.dto.NotificationDTO;
import com.project.nyang.modules.notification.entity.Notification;
import com.project.nyang.modules.notification.repository.NotificationRepository;
import com.project.nyang.modules.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 알림 기능 서비스 입니다
 *
 * @author : 이지은
 * @fileName : NotificationService
 * @since : 25. 7. 17.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "안읽은 메세지 알림", description = "사용자가 읽지 않은 알람이 있을 경우 알림이 옵니다.")
    public void notifyUnreadChatMessage(User receiver, String content, String link) {
        Notification notification = Notification.builder()
                .type(Notification.NotificationType.CHAT_REPLY)
                .notyContent("💬 " + content)
                .notyLink(link)
                .isRead(false)
                .user(receiver)
                .build();

        notificationRepository.save(notification);

    }

    @Operation(summary = "입양신청완료 후 이메일발송 완료 알림", description = "입양신청이 완료 알림 전송 메서드 입니다.")
    public void mailSentNotification(User user) {
        // 1. 알림 엔티티 생성
        Notification notification = Notification.builder()
                .user(user)
                .type(Notification.NotificationType.FORM_SENT)
                .notyContent("입양 신청서가 보호소로 전송되었습니다.")
                .notyLink("/mypage/applications")
                .isRead(false)
                .build();

        // 2. 저장
        Notification saved = notificationRepository.save(notification);

        // 3. STOMP 구독 주소로 전송
        messagingTemplate.convertAndSend(
                "/sub/notifications/" + user.getId(),
                new NotificationDTO(saved) // DTO 형태로 보냄
        );
    }


    @Operation(summary = "안 읽은 알림 개수 조회", description = "읽지 않은 알림 개수를 조회합니다.")
    public long countUnreadNotifications(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Operation(summary = "알림 전체 조회", description = "최신순으로 알림 전체를 조회합니다.")
    public List<Notification> getNotifications(User user) {
        return notificationRepository.findByUserOrderByNotyCreatedAtDesc(user);
    }

    @Operation(summary = "특정 타입 알림 조회", description = "예: chat_reply 타입 알림만 조회")
    public List<Notification> getNotificationsByType(User user, Notification.NotificationType type) {
        return notificationRepository.findByUserAndTypeOrderByNotyCreatedAtDesc(user, type);
    }

    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 변경합니다.")
    public void markAsRead(Long notyId, User user) {
        Notification notification = notificationRepository.findByIdAndUser(notyId, user)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notification.markAsRead();
        notificationRepository.save(notification);
    }



}