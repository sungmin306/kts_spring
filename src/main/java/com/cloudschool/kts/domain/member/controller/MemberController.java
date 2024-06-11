package com.cloudschool.kts.domain.member.controller;


import com.cloudschool.kts.domain.member.model.dto.request.MemberLoginDto;
import com.cloudschool.kts.domain.member.model.dto.request.MemberRegisterDto;
import com.cloudschool.kts.domain.member.model.dto.request.MemberUpdateDto;
import com.cloudschool.kts.domain.member.model.dto.response.MemberResponseDto;
import com.cloudschool.kts.domain.member.model.dto.response.MemberTokenDto;
import com.cloudschool.kts.domain.member.model.entity.Member;
import com.cloudschool.kts.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@RequestBody MemberRegisterDto memberRegisterDto) {
        MemberResponseDto registerMember =  memberService.register(memberRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerMember);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody MemberLoginDto memberLoginDto) {
        MemberTokenDto memberTokenDto = memberService.login(memberLoginDto);
        return ResponseEntity.status(HttpStatus.OK).header(memberTokenDto.getToken()).body(memberTokenDto);
    }

    @PostMapping("/check/password")
    public ResponseEntity<MemberResponseDto> check(@AuthenticationPrincipal Member member,
                                                   @RequestBody Map<String, String> request) {
        String password = request.get("password");
        MemberResponseDto memberResponseDto = memberService.check(member, password);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<MemberResponseDto> update(@AuthenticationPrincipal Member member,
                                                    @RequestBody MemberUpdateDto memberUpdateDto) {
        MemberResponseDto memberResponseDto = memberService.update(member, memberUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }
}
