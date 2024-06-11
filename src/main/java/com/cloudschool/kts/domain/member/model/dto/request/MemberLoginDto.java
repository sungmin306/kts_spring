package com.cloudschool.kts.domain.member.model.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberLoginDto {

    private String email;
    private String password;

    @Builder
    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;

    }

}
