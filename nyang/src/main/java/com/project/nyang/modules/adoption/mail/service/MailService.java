package com.project.nyang.modules.adoption.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 메일 발송 관련 service 입니다
 *
 * @author : 이지은
 * @fileName : MailService
 * @since : 25. 7. 15.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    /**
     * 첨부파일과 함께 HTML 형식 이메일 발송
     * @param to 받는 사람 이메일
     * @param subject 이메일 제목
     * @param text 이메일 본문 (HTML 포함 가능)
     * //@param pdf 첨부파일 (PDF 등)
     */
    public void sendEmailWithPdf(String to, String subject, String text, File pdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart 메시지 (첨부파일 포함 가능)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true: HTML 형식

            if (pdf != null && pdf.exists()) {
                FileSystemResource file = new FileSystemResource(pdf);
                helper.addAttachment(pdf.getName(), file);
            }

            mailSender.send(message);

            log.info("이메일 전송 성공 - 받는 사람: {}", to);

        } catch (MessagingException e) {

            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}