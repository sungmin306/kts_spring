package com.cloudschool.kts.domain.member.model.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateDto {

    private String password;
    private String passwordCheck;
    private String username;

    @Builder
    public MemberUpdateDto(String password, String passwordCheck, String username) {
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
    }
}
