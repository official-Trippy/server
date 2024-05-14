package com.example.server.domain.member.dto;

import lombok.Builder;
import lombok.Data;

public class MemberRequestDto {

    @Builder
    @Data
    public static class CreateMemberRequestDto {
        private String memberId;
        private String password;
        private String email;
    }

    @Builder
    @Data
    public static class CommonCreateMemberRequestDto {
        private String nickName;
        private String blogName;
        private String blogIntroduce;
    }

    @Builder
    @Data
    public static class ChangePasswordRequestDto {
        private String email;
        private String newPassword;
    }
}
