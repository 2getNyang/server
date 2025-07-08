package com.project.nyang.modules.adoption.entity;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * PetApplicationForm
 * @fileName        : PetApplicationForm
 * @author          : 이지은
 * @since           : 2025-07-07
 *
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PET_APPLICATION_FORM")
@EntityListeners(AuditingEntityListener.class)
public class PetApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_birth", nullable = false)
    private LocalDate userBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private Gender userGender;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "family_phone")
    private String familyPhone;

    @Column(name = "family")
    private String family;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "job", nullable = false)
    private String job;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience", nullable = false)
    private YesNo experience;

    @CreatedDate
    @Column(name = "noty_created_at", columnDefinition = "TIMESTAMP",
            updatable = false, nullable = false)
    private LocalDateTime notyCreatedAt;

    @Column(name = "application_reason", columnDefinition = "TEXT", nullable = false)
    private String applicationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desertion_no", referencedColumnName = "desertion_no")
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_reg_number", referencedColumnName = "care_reg_number")
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum Gender {
        F, M
    }

    public enum YesNo {
        Y, N
    }

    //전에 builder 패턴 사용하기로 했어서 임시 추가
    @Builder
    public PetApplicationForm(
        String userName,
        LocalDate userBirth,
        Gender userGender,
        String userPhone,
        String familyPhone,
        String family,
        String address,
        String detailAddress,
        String job,
        YesNo experience,
        LocalDateTime submissionTime,
        String applicationReason,
        Animal animal,
        Shelter shelter,
        User user
    ) {
        this.userName = userName;
        this.userBirth = userBirth;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.familyPhone = familyPhone;
        this.family = family;
        this.address = address;
        this.detailAddress = detailAddress;
        this.job = job;
        this.experience = experience;
        this.applicationReason = applicationReason;
        this.animal = animal;
        this.shelter = shelter;
        this.user = user;
    }
}