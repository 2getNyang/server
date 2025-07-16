package com.project.nyang.modules.adoption.pdf;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.adoption.dto.AdoptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Apache PdfBox 관련 로직 처리합니다.
 * @author : 이지은
 * @fileName : PdfGenerator
 * @since : 25. 7. 15.
 *
 */
@Slf4j
@Service
public class WordGeneratorService {

    private final String templatePath = "templates/adoption_template_v1.docx";
    private final String fontResourcePath = "fonts/MaruBuri-Regular.ttf";

    public File generateDocxFromDto(AdoptionDTO dto) throws Exception {
        // 1. 템플릿 로딩
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream(templatePath);
        if (templateStream == null) {
            log.error("템플릿 파일을 찾을 수 없습니다: {}", templatePath);
            throw new CustomException(ErrorCode.INVALID_TEMPLATE);
        }
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateStream);

        // 2. 폰트 매핑 (한글 깨짐 방지)
        mapFontFromResource(wordMLPackage);

        // 3. DTO 값 → 플레이스홀더 치환
        Map<DataFieldName, String> mappings = createMappings(dto);
        MailMerger.performMerge(wordMLPackage, mappings, true);

        // 4. Word 파일로 저장
        File dir = new File("../Docx");

        // 임시파일 생성 (디렉터리 지정)
        File outputDocx = File.createTempFile("adoption-", ".docx", dir);
        wordMLPackage.save(outputDocx);

        return outputDocx;
    }

    private Map<DataFieldName, String> createMappings(AdoptionDTO dto) {
        Map<DataFieldName, String> mappings = new HashMap<>();
        mappings.put(new DataFieldName("noticeNo"), dto.getNoticeNo() != null ? dto.getNoticeNo() : "");
        mappings.put(new DataFieldName("userName"), dto.getUserName() != null ? dto.getUserName() : "");
        mappings.put(new DataFieldName("userBirth"), dto.getUserBirth() != null ? dto.getUserBirth().toString() : "");
        mappings.put(new DataFieldName("userGender"), dto.getUserGender() != null ? dto.getUserGender().name() : "");
        mappings.put(new DataFieldName("userPhone"), dto.getUserPhone() != null ? dto.getUserPhone() : "");
        mappings.put(new DataFieldName("familyPhone"), dto.getFamilyPhone() != null ? dto.getFamilyPhone() : "");
        mappings.put(new DataFieldName("family"), dto.getFamily() != null ? dto.getFamily() : "");
        mappings.put(new DataFieldName("address"), dto.getAddress() != null ? dto.getAddress() : "");
        mappings.put(new DataFieldName("detailAddress"), dto.getDetailAddress() != null ? dto.getDetailAddress() : "");
        mappings.put(new DataFieldName("housingType"), dto.getHousingType() != null ? dto.getHousingType().name() : "");
        mappings.put(new DataFieldName("job"), dto.getJob() != null ? dto.getJob() : "");
        mappings.put(new DataFieldName("experience"), dto.getExperience() != null ? dto.getExperience().name() : "");
        mappings.put(new DataFieldName("hasOtherPets"), dto.getHasOtherPets() != null ? dto.getHasOtherPets().name() : "");
        mappings.put(new DataFieldName("adultCount"), String.valueOf(dto.getAdultCount()));
        mappings.put(new DataFieldName("childrenCount"), String.valueOf(dto.getChildrenCount()));
        mappings.put(new DataFieldName("allConsent"), dto.getAllConsent() != null ? dto.getAllConsent().name() : "");
        mappings.put(new DataFieldName("hasAllergy"), dto.getHasAllergy() != null ? dto.getHasAllergy().name() : "");
        mappings.put(new DataFieldName("consentForCheck"), dto.getConsentForCheck() != null ? dto.getConsentForCheck().name() : "");
        mappings.put(new DataFieldName("applicationReason"), dto.getApplicationReason() != null ? dto.getApplicationReason() : "");

        return mappings;
    }

    private void mapFontFromResource(WordprocessingMLPackage wordMLPackage) throws Exception {
        InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontResourcePath);
        File tempFontFile = File.createTempFile("MaruBuri-", ".ttf");
        try (FileOutputStream out = new FileOutputStream(tempFontFile)) {
            fontStream.transferTo(out);
        }
        Mapper fontMapper = new IdentityPlusMapper();
        PhysicalFonts.addPhysicalFonts("MaruBuri", tempFontFile.toURI());
        fontMapper.put("MaruBuri", PhysicalFonts.get("MaruBuri"));
        wordMLPackage.setFontMapper(fontMapper);
    }
}