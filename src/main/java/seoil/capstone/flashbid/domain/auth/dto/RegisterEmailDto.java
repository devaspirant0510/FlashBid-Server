package seoil.capstone.flashbid.domain.auth.dto;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmailDto{
    private String password;
    private String nickname;
    private String email;
}
