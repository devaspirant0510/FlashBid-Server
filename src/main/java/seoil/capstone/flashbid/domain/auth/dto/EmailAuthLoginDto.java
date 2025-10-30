package seoil.capstone.flashbid.domain.auth.dto;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthLoginDto {
    private String email;
    private String password;
}
