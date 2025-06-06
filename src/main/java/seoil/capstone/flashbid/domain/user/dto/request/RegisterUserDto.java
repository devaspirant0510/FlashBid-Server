package seoil.capstone.flashbid.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterUserDto {
    private String userName;
    private String uid;
    private Long id;
}
