package fit.fitspring.controller;

import fit.fitspring.controller.dto.account.AccountForRegisterDto;
import fit.fitspring.controller.dto.account.RegisterDto;
import fit.fitspring.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "계정 API")
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /* 테스트 용 */
    @Operation(summary = "테스트", description = "테스트")
    @GetMapping("/test")
    public String test(){
        return "1";
    }

    /**
     * 회원 가입
     * @Param : AccountForRegisterDto
     * @Response : 200(성공) or 400
     * */
//    @PostMapping
//    public ResponseEntity registerUser(@RequestBody AccountForRegisterDto accountDto){
//        accountService.registerAccount(accountDto);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "회원가입(미완)", description = "회원가입(Request)")
    @PostMapping
    public ResponseEntity registerUser(@RequestBody RegisterDto registerDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인(미완)", description = "로그인(Request)")
    @PostMapping("/{email}/{password}")
    public ResponseEntity userLogin(@Parameter(description = "이메일")@PathVariable String email,
                                    @Parameter(description = "비밀번호")@PathVariable String password){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증메일전송(미완)", description = "인증메일전송(Request/Response)")
    @GetMapping("/{email}")
    public ResponseEntity findUserPassword(@Parameter(description = "이메일")@PathVariable String email){
        String certificationNumber = "6자리 인증번호";
        return ResponseEntity.ok().body(certificationNumber);
    }

    @Operation(summary = "카카오로그인(미완)", description = "카카오로그인(Request)")
    @PostMapping("/kakao")
    public ResponseEntity userKakaoLogin(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "애플로그인(미완)", description = "애플로그인(Request)")
    @PostMapping("/apple")
    public ResponseEntity userAppleLogin(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "네이버로그인(미완)", description = "네이버로그인(Request)")
    @PostMapping("/naver")
    public ResponseEntity userNaverLogin(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이용약관수락(미완)", description = "이용약관수락")
    @PostMapping("/terms")
    public ResponseEntity acceptTermsOfUse(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호변경(미완)", description = "비밀번호변경(Request)")
    @PatchMapping("/{password}")
    public ResponseEntity modifyPassword(@Parameter(description = "비밀번호")@PathVariable String password){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃(미완)", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity userLogout(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계정탈퇴(미완)", description = "계정탈퇴")
    @PatchMapping("/close")
    public ResponseEntity userCloseAccount(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계정상태수정(미완)", description = "계정상태수정(Request)")
    @PatchMapping("/state/{state}")
    public ResponseEntity modifyAccountState(@Parameter(description = "상태('able' or 'disabled'")@PathVariable String state){
        return ResponseEntity.ok().build();
    }
}
