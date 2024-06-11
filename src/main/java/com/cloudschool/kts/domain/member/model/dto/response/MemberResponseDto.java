package com.cloudschool.kts.domain.member.model.dto.response;


import com.cloudschool.kts.domain.member.model.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResponseDto {

    private String email;
    private String username;

    @Builder
    public MemberResponseDto(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public static MemberResponseDto fromEntity(Member member){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }
}
