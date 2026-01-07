package seoil.capstone.flashbid.domain.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterDto {
    private String email;
    private String password;
    private String username;

}
