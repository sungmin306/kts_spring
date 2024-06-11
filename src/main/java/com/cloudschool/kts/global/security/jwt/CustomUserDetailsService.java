package com.cloudschool.kts.global.security.jwt;

import com.cloudschool.kts.domain.member.repository.MemberRepository;
import com.cloudschool.kts.global.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByEmail(username).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member Email : ", username));
    }
}