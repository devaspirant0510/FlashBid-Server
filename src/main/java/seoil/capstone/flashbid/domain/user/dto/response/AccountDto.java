package seoil.capstone.flashbid.domain.user.dto.response;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;
    private Integer point;
}
