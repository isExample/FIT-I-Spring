package fit.fitspring.controller.dto.firebase;

import lombok.Getter;

@Getter
public class FcmTokenDto {
    private String email;
    private Long id;
    private String token;
}
