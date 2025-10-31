package seoil.capstone.flashbid.domain.auth.dto;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailOtpDto {
    private String email;
    private String otp;
}
