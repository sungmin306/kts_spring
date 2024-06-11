package com.cloudschool.kts.domain.member.model.entity;

import com.cloudschool.kts.domain.board.model.entity.Board;
import com.cloudschool.kts.domain.comment.model.entity.Comment;
import com.cloudschool.kts.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity implements UserDetails {

    @Transient // 필드 매핑 안되게 하는 어노테이션
    private final Path uploadDir = Paths.get("uploads"); // 상대경로 넣어야 하는 부분

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; // 아이디

    @Column(nullable = false, length=100)
    @Size(min = 8)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role roles;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Comment> comments = new ArrayList<>();

    //==생성 로직==//
    @Builder
    public Member(String email, String password, String username, Role roles, MultipartFile imageFile) {
        String imageUrl = storeImage(imageFile);
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
        this.imageUrl = imageUrl;
    }

    //==비즈니스 로직==//
    /**
     * 프로필 이미지 저장
     */
    private String storeImage(MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = imageFile.getOriginalFilename();
                Path filePath = uploadDir.resolve(filename);
                Files.write(filePath, imageFile.getBytes());
                return filePath.toString();
            } catch (IOException e) {
                throw new RuntimeException("Could not store the file: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 비밀번호, 닉네임 변경
     */
    public void update(String password, String username) {
        this.password = password;
        this.username = username;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.roles.name()));
        return authorities;
    }
}
