package com.project.nyang.modules.user.service;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.logging.LogMessage;
import com.project.nyang.modules.user.dto.ChatUserInfoDTO;
import com.project.nyang.modules.user.dto.AuthInfoDTO;
import com.project.nyang.modules.user.dto.UpdateUserInfoDTO;
import com.project.nyang.modules.user.dto.UserInfoDTO;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService입니다.
 *
 * @author : 엄아영
 * @fileName : UserService
 * @since : 2025-07-13
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository  userRepository;

    @LogMessage(value = "채팅방 - 상대방 정보 조회", operation = "READ")
    //채팅방에서 상대방 정보 가져오는 메서드
    @Transactional(readOnly = true)
    public ChatUserInfoDTO getChatUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        return new ChatUserInfoDTO(
                user.getId(),
                user.getNickname()
        );
    }

    @LogMessage(value = "로그인한 유저 권한 획득", operation = "READ")
    @Transactional(readOnly = true)
    public AuthInfoDTO getAuthInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));

        return AuthInfoDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .loginType(user.getLoginType())
                .build();
    }

    @LogMessage(value = "내 정보 조회", operation = "READ")
    @Transactional(readOnly = true)
    public UserInfoDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));

        return new UserInfoDTO(user.getNickname(), user.getEmail(), user.getLoginType());
    }

    @LogMessage(value = "내 정보 수정", operation = "UPDATE")
    @Transactional
    public UserInfoDTO updateUserInfo(Long userId, UpdateUserInfoDTO updateUserInfoDTO) {
        //아이디 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        // 닉네임이 비어있지 않으면 중복 검사 후 업데이트
        if (updateUserInfoDTO.getNickname() != null && !updateUserInfoDTO.getNickname().isBlank()) {
            userRepository.findByNickname(updateUserInfoDTO.getNickname())
                    .filter(u -> !u.getId().equals(userId)) // 본인은 제외
                    .ifPresent(u -> { throw new CustomException(ErrorCode.DUPLICATE_NICKNAME); });

            user.updateNickname(updateUserInfoDTO.getNickname());
        }

        // 이메일이 비어있지 않으면 중복 검사 후 업데이트
        if (updateUserInfoDTO.getEmail() != null && !updateUserInfoDTO.getEmail().isBlank()) {
            userRepository.findByEmail(updateUserInfoDTO.getEmail())
                    .filter(u -> !u.getId().equals(userId)) // 본인은 제외
                    .ifPresent(u -> { throw new CustomException(ErrorCode.DUPLICATE_EMAIL); });

            user.updateEmail(updateUserInfoDTO.getEmail());
        }else {
            // 이메일이 null이거나 빈 문자열이면 null 업데이트 (혹은 유지하고 싶으면 이 블록 제외)
            user.updateEmail(null);
        }

        return new UserInfoDTO(user.getNickname(), user.getEmail(), user.getLoginType());
    }
}