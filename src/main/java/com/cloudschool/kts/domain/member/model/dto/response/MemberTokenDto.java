package com.cloudschool.kts.domain.member.model.dto.response;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
public class MemberTokenDto {
    private String email;
    private String token;

    @Builder
    public MemberTokenDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static MemberTokenDto fromEntity(UserDetails member, String token) {
        return MemberTokenDto.builder()
                .email(member.getUsername())
                .token(token)
                .build();
    }
}
