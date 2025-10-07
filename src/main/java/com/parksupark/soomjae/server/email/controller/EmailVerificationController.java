package com.parksupark.soomjae.server.email.controller;

import com.parksupark.soomjae.server.email.dto.SendCodeRequest;
import com.parksupark.soomjae.server.email.dto.VerifyCodeRequest;
import com.parksupark.soomjae.server.email.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;


    /**
     * 이메일 인증코드 발송
     *
     * <p><b>플로우:</b>
     * <ol>
     * <li>영문자와 숫자가 조합된 6자리 랜덤 코드 생성</li>
     * <li>EmailVerification 엔티티를 생성하여 DB에 저장 (만료시간: 5분)</li>
     * <li>HTML 템플릿을 사용하여 이메일 작성</li>
     * <li>Gmail SMTP를 통해 이메일 발송</li>
     * </ol>
     *
     * <p><b>참고사항:</b>
     * <ul>
     * <li>동일한 이메일로 재요청 시 기존 코드는 새 코드로 덮어씌워집니다</li>
     * <li>생성된 EmailVerification 의 유효기간은 5분입니다</li>
     * </ul>
     *
     * @param request 이메일 주소가 포함된 요청 객체
     * @return 발송 완료 응답 (HTTP 200)
     * @throws jakarta.validation.ConstraintViolationException
     * 유효하지 않은 이메일 형식일 경우
     * @throws com.parksupark.soomjae.server.email.exception.EmailVerificationFailedException
     * 이메일 발송 실패 시
     * @see com.parksupark.soomjae.server.email.service.DefaultEmailVerificationService
     * #sendVerificationCode(String)
     */
    @PostMapping("/verification")
    public DeferredResult<ResponseEntity<?>> sendVerificationCode(
        @RequestBody @Valid SendCodeRequest request) {
        return emailVerificationService.sendVerificationCode(request.getEmail());
    }


    /**
     * 이메일 인증코드 검증
     *
     * <p><b>플로우:</b>
     * <ol>
     * <li>이메일로 EmailVerification 엔티티 조회</li>
     * <li>코드 만료 시간 검증 (만료 시 삭제)</li>
     * <li>입력된 코드와 저장된 코드 비교 검증</li>
     * <li>검증 성공 시 isVerified를 true로 설정</li>
     * <li>회원가입 프로세스에서 이메일 인증 완료 상태로 사용 가능</li>
     * </ol>
     *
     * <p><b>검증 규칙:</b>
     * <ul>
     * <li>코드는 대소문자 구분 없이 검증됩니다 (자동으로 대문자 변환)</li>
     * <li>코드 앞뒤 공백은 자동으로 제거됩니다</li>
     * </ul>
     *
     * @param request 이메일 주소와 인증코드가 포함된 요청 객체
     * @return 인증 성공 메시지 (HTTP 200)
     * @throws jakarta.validation.ConstraintViolationException 요청 파라미터 유효성 검증 실패 시
     * @throws com.parksupark.soomjae.server.common.exception.ResourceNotFoundException
     * 이메일과 코드를 사용한 EmailVerification 조회결과가 없을 경우
     * @see com.parksupark.soomjae.server.email.service.DefaultEmailVerificationService
     * #verifyCode(String, String)
     */
    @PutMapping("/verification")
    public ResponseEntity<String> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        String email = request.getEmail();
        String code = request.getCode();

        emailVerificationService.verifyCode(email, code);
        return ResponseEntity.ok("인증 성공");
    }
}