package seoil.capstone.flashbid.global.model;


import lombok.Data;

@Data
public class NaverUserInfoResponse {
    private String resultcode;
    private String message;
    private NaverUser response;

    @Data
    public static class NaverUser {
        private String id;
        private String email;
        private String name;
    }
}
