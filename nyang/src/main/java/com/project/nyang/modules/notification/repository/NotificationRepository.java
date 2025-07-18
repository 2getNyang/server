package com.project.nyang.modules.notification.repository;

import com.project.nyang.modules.notification.entity.Notification;
import com.project.nyang.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 알림기능을 위한 Repository 입니다
 *
 * @author : 이지은
 * @fileName : NotificationRepository
 * @since : 25. 7. 17.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByNotyCreatedAtDesc(User user);

    List<Notification> findByUserAndTypeOrderByNotyCreatedAtDesc(User user, Notification.NotificationType type);

    Optional<Notification> findByNotyIdAndUser(Long notyId, User user); // 잘못된 파라미터 및 리턴 타입 수정

    long countByUserAndIsReadFalse(User user);
}
