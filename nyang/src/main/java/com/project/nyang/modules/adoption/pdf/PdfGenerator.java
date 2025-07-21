package com.project.nyang.modules.adoption.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.adoption.dto.AdoptionDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
@AllArgsConstructor
public class PdfGenerator {

    private final TemplateEngine templateEngine;

    @Operation(summary = "PDF 변환", description = "HTML 템플릿을 pdf로 변환합니다")
    public File htmlToPdf(AdoptionDTO adoptionDTO) {
        try {
            // 1. Thymeleaf context 준비
            Context context = buildContextFromDto(adoptionDTO);

            // 2. HTML 렌더링
            String html = templateEngine.process("adoption_template_v1", context); // .html 없이 템플릿 이름만

            // 3. 저장 경로 (OS 독립적)
            Path baseDir = Paths.get(System.getProperty("user.dir"), "Pdf");
            File directory = baseDir.toFile();
            if (!directory.exists()) directory.mkdirs();

            // 3. UUID 기반 파일명 생성
            String filename = "adoption_form_" + UUID.randomUUID() + ".pdf";
            Path filePath = baseDir.resolve(filename);
            File pdfFile = filePath.toFile();

            // 4. 리소스 폴더 내의 한글 폰트 읽기
            ClassPathResource fontResource = new ClassPathResource("fonts/MaruBuri-Regular.ttf");
            File tempFontFile = File.createTempFile("maruburi-", ".ttf");
            try (InputStream is = fontResource.getInputStream();
                 FileOutputStream fos = new FileOutputStream(tempFontFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            // 5. PDF 변환
            try (OutputStream os = new FileOutputStream(pdfFile)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(html, null);
                builder.useFont(tempFontFile, "MaruBuri");
                builder.toStream(os);
                builder.run();
            }

            log.info("✅ PDF 생성 완료: {}", pdfFile.getAbsolutePath());
            return pdfFile;

        } catch (Exception e) {
            log.error("❌ PDF 생성 실패", e);
            throw new CustomException(ErrorCode.PDF_GENERATION_FAILED);
        }
    }
    @Operation(summary = "DTO값 바인딩", description = "DTO로 받아온 값을 thymeleaf로 HTML 템플릿에 바인딩")
    private Context buildContextFromDto(AdoptionDTO dto) {
        Context context = new Context();
        context.setVariable("noticeNo", dto.getNoticeNo());
        context.setVariable("userName", dto.getUserName());
        context.setVariable("userBirth", dto.getUserBirth());
        context.setVariable("userGender", dto.getUserGender());
        context.setVariable("userPhone", dto.getUserPhone());
        context.setVariable("familyPhone", dto.getFamilyPhone());
        context.setVariable("family", dto.getFamily());
        context.setVariable("address", dto.getAddress());
        context.setVariable("detailAddress", dto.getDetailAddress());
        context.setVariable("housingType", dto.getHousingType());
        context.setVariable("job", dto.getJob());
        context.setVariable("experience", dto.getExperience());
        context.setVariable("hasOtherPets", dto.getHasOtherPets());
        context.setVariable("adultCount", dto.getAdultCount());
        context.setVariable("childrenCount", dto.getChildrenCount());
        context.setVariable("allConsent", dto.getAllConsent());
        context.setVariable("hasAllergy", dto.getHasAllergy());
        context.setVariable("consentForCheck", dto.getConsentForCheck());
        context.setVariable("applicationReason", dto.getApplicationReason());
        return context;
    }

}