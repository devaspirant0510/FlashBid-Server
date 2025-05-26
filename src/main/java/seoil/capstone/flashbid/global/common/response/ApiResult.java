package seoil.capstone.flashbid.global.common.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult <T>{
    private ApiHeader apiHeader;
    private String path;
    private String method;
    private LocalDateTime timestamp;
    private T data;
    private ApiError error;
    private String message;
}
