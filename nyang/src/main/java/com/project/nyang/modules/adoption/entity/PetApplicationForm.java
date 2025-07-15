package com.project.nyang.modules.adoption.entity;

import com.project.nyang.modules.animal.entity.Animal;
import com.project.nyang.modules.shelter.entity.Shelter;
import com.project.nyang.modules.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PetApplicationForm {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long formId;

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
    @Column(name = "form_created_at", columnDefinition = "TIMESTAMP",
            updatable = false, nullable = false)
    private LocalDateTime formCreatedAt;

    @Column(name = "application_reason", columnDefinition = "TEXT", nullable = false)
    private String applicationReason;

    @Column(name = "adult_count", nullable = false)
    private int adultCount;

    @Column(name = "children_count", nullable = false)
    private int childrenCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "all_consent", nullable = false, length = 3)
    private YesNo allConsent;  // YES/NO

    @Enumerated(EnumType.STRING)
    @Column(name = "housing_type", nullable = false, length = 20)
    private HousingType housingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "has_allergy", nullable = false, length = 3)
    private YesNo hasAllergy;  // YES/NO

    @Enumerated(EnumType.STRING)
    @Column(name = "has_other_pets", nullable = false, length = 3)
    private YesNo hasOtherPets; // YES/NO

    @Enumerated(EnumType.STRING)
    @Column(name = "consent_for_check", nullable = false, length = 3)
    private YesNo consentForCheck; // YES/NO



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
        YES, NO
    }

    public enum HousingType {
        DAGA_GU_JUTAUK,
        DANDOK_JUTAUK,
        APATEU,
        ONE_ROOM,
        ETC
    }

}