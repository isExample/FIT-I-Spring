
package fit.fitspring.controller.mdoel.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
//@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(jwt, userIdx)를 받는 생성자를 생성-
/**
 * Res.java: From Server To Client
 * 회원가입의 결과(Respone)를 보여주는 데이터의 형태
 */

public class PostAccountRes {
    private String success;

    public PostAccountRes(String name) {
        this.success = name + " 님, 환영합니다.";
    }
}
