package com.jskim.preuorder.controller.v1;

import com.jskim.preuorder.advice.exception.CUserNotFoundException;
import com.jskim.preuorder.entity.User;
import com.jskim.preuorder.model.response.CommonResult;
import com.jskim.preuorder.model.response.ListResult;
import com.jskim.preuorder.model.response.SingleResult;
import com.jskim.preuorder.repository.UserJpaRepository;
import com.jskim.preuorder.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "2. User", description = "PUO API") // UserController를 대표하는 최상단 타이틀 영역에 표시될 값 세팅
@RequiredArgsConstructor // class 내부의 final 객체는 Constructor Injection 수행, @Autowired도 가능
@RestController // 결과를 JSON으로 도출
@RequestMapping(value = "/v1") // api resource를 버전별로 관리, /v1 을 모든 리소스 주소에 적용
public class UserController {

    private final UserJpaRepository userJpaRepository;
    private final ResponseService responseService; // 결과를 처리하는 Service
    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token",
                    required = true, in = ParameterIn.HEADER)
    })
    @Operation(summary = "회원 리스트 조회", description = "모든 회원을 조회한다.") // 각각의 resource에 제목과 설명 표시
    @GetMapping(value = "/users") // user 테이블의 모든 정보를 읽어옴
    public ListResult<User> findAllUser() { // 데이터가 1개 이상일 수 있기에 List<User>로 선언
        return responseService.getListResult(userJpaRepository.findAll()); // JPA를 사용하면 CRUD에 대해 설정 없이 쿼리 사용 가능 (select * from user 와 같음)
        // 결과 데이터가 여러개인 경우 getListResult 활용
    }

    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token",
                    required = true, in = ParameterIn.HEADER)
    })
    @Operation(summary = "회원 단건 조회", description = "msrl로 회원을 조회한다.")
    @GetMapping(value = "/user")
    public SingleResult<User> findUserById(@Parameter(description = "언어", example = "ko") @RequestParam String lang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        return responseService.getSingleResult(userJpaRepository.findByUid(id).orElseThrow(CUserNotFoundException::new));
        // 결과 데이터가 단일건인 경우 getSingleResult를 이용하여 결과를 출력
    }

    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token",
                    required = true, in = ParameterIn.HEADER)
    })
    @Operation(summary = "회원 수정", description = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @Parameter(description = "회원번호", required = true) @RequestParam long msrl,
            @Parameter(description = "회원이름", required = true) @RequestParam String name
    ) {
        User user = User.builder()
                .msrl(msrl)
                .name(name)
                .build();

        return responseService.getSingleResult(userJpaRepository.save(user));
    }

    @Parameters({
            @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token",
                    required = true, in = ParameterIn.HEADER)
    })
    @Operation(summary = "회원 삭제", description = "msrl로 회원정보를 삭제한다.")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete (@Parameter(description = "회원 정보") @PathVariable long msrl) {
        userJpaRepository.deleteById(msrl); // deleteByID id를 받아 delete query 실행
        return responseService.getSuccessResult();
    }
}
