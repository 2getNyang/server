package com.project.nyang.modules.user.entity;

import com.project.nyang.global.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * User Entity입니다.
 * @fileName        : User
 * @author          : 엄아영
 * @since           : 2025-07-07
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "USERS")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login_type", length = 20, nullable = false)
    private String loginType;

    @Column(name = "login_id", length = 128, nullable = false)
    private String loginId;

    @Column(name = "nickname", length = 30, nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", length = 100)
    private String email;


    //mapped 관련하여서는 다른 엔티티들 보고 작성

}
