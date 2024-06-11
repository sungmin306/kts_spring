package com.cloudschool.kts.domain.member.service;

import com.cloudschool.kts.domain.member.model.dto.request.MemberLoginDto;
import com.cloudschool.kts.domain.member.model.dto.request.MemberRegisterDto;
import com.cloudschool.kts.domain.member.model.dto.request.MemberUpdateDto;
import com.cloudschool.kts.domain.member.model.dto.response.MemberResponseDto;
import com.cloudschool.kts.domain.member.model.entity.Member;
import com.cloudschool.kts.domain.member.model.dto.response.MemberTokenDto;
import com.cloudschool.kts.domain.member.repository.MemberRepository;
import com.cloudschool.kts.domain.member.exception.MemberException;
import com.cloudschool.kts.global.exception.ResourceNotFoundException;
import com.cloudschool.kts.global.security.jwt.CustomUserDetailsService;
import com.cloudschool.kts.global.security.jwt.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public HttpStatus checkIdDuplicate(String email) {
        isExistUserEmail(email);
        return HttpStatus.OK;
    }

    public MemberResponseDto register(MemberRegisterDto memberRegisterDto) {
        isExistUserEmail(memberRegisterDto.getEmail());
        checkPassword(memberRegisterDto.getPassword(), memberRegisterDto.getPasswordCheck());
        String encodePassword = encoder.encode(memberRegisterDto.getPassword());
        memberRegisterDto.setPassword(encodePassword);
        Member newMember = memberRepository.save(MemberRegisterDto.ofEntity(memberRegisterDto));
        return MemberResponseDto.fromEntity(newMember);
    }

    public MemberTokenDto login(MemberLoginDto memberLoginDto) {
        authenticate(memberLoginDto.getEmail(), memberLoginDto.getPassword());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberLoginDto.getEmail());
        checkEncodePassword(memberLoginDto.getPassword(), userDetails.getPassword());
        String token = jwtTokenUtil.generateToken(userDetails);
        return MemberTokenDto.fromEntity(userDetails, token);
    }

    public MemberResponseDto check(Member member, String password) {
        Member checkMember = (Member) customUserDetailsService.loadUserByUsername(member.getEmail());
        checkEncodePassword(password, checkMember.getPassword());
        return MemberResponseDto.fromEntity(checkMember);
    }

    public MemberResponseDto update(Member member, MemberUpdateDto memberUpdateDto) {
        checkPassword(memberUpdateDto.getPassword(), memberUpdateDto.getPasswordCheck());
        String encodepassword = encoder.encode(memberUpdateDto.getPassword());
        Member updateMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member Email", member.getEmail()));
        updateMember.update(encodepassword, memberUpdateDto.getUsername());
        return MemberResponseDto.fromEntity(updateMember);

    }

    //==서비스 예외처리(기능) 로직==// -> 서비스 로직에 둬야할지 따로 예외처리 클래스를 만들어야 할지 고민. 일단 여기다 두는게 나을꺼같기도..?

    /**
     * 이메일 존재 확인
     */
    private void isExistUserEmail(String email) {
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new MemberException("This email already exists", HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * 비밀번호 일치 여부 확인
     */
    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new MemberException("The password and checkpassword do not match", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 사용자 인증 여부 확인
     */
    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new MemberException("This is an unauthorized user ID", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            throw new MemberException("The passwords do not match", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 비밀번호와 Db에 저장된 비밀번호 동일 여부 확인
     */

    private void checkEncodePassword(String password, String encodePassword) {
        if (!encoder.matches(password, encodePassword)) {
            throw new MemberException("The password does not match", HttpStatus.BAD_REQUEST);
        }
    }


}



