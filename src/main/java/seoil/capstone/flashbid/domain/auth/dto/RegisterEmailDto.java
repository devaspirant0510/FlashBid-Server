package seoil.capstone.flashbid.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


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
