package com.cloudschool.kts.domain.member.model.dto.request;

import com.cloudschool.kts.domain.member.model.dto.response.MemberResponseDto;
import com.cloudschool.kts.domain.member.model.entity.Member;
import com.cloudschool.kts.domain.member.model.entity.Role;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
public class MemberRegisterDto {

    private String email;
    private String password;
    private String passwordCheck;
    private String username;
    private MultipartFile imageFile;

    @Builder
    public MemberRegisterDto(String email, String password, String passwordCheck, String username, MultipartFile imageFile) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.imageFile = imageFile;
    }

    public static Member ofEntity(MemberRegisterDto dto) {
        return Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .roles(Role.USER)
                .build();
    }

}
