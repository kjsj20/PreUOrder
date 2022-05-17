package com.jskim.preuorder.controller.v1;

import com.jskim.preuorder.advice.exception.CEmailSigninFailedException;
import com.jskim.preuorder.config.security.JwtTokenProvider;
import com.jskim.preuorder.entity.User;
import com.jskim.preuorder.model.response.CommonResult;
import com.jskim.preuorder.model.response.SingleResult;
import com.jskim.preuorder.repository.UserJpaRepository;
import com.jskim.preuorder.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Tag(name = "1. Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserJpaRepository userJpaRepository; // jpa 쿼리 활용
    private final JwtTokenProvider jwtTokenProvider; // jwt 토큰 생성
    private final ResponseService responseService; // API 요청 결과에 대한 code, messageㅍ
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화

    @Operation(summary = "로그인", description = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@Parameter(description = "회원ID : 이메일", required = true) @RequestParam String id,
                                       @Parameter(description = "비밀번호", required = true) @RequestParam String password) {
        User user = userJpaRepository.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // matches : 평문, 암호문 패스워드 비교 후 boolean 결과 return
            throw new CEmailSigninFailedException();
        }
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @Operation(summary = "가입", description = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(@Parameter(description = "회원ID : 이메일", required = true) @RequestParam String id,
                               @Parameter(description = "비밀번호", required = true) @RequestParam String password,
                               @Parameter(description = "이름", required = true) @RequestParam String name) {

        userJpaRepository.save(User.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return responseService.getSuccessResult();
    }
}