package com.cloudschool.kts.domain.member.config;

import com.cloudschool.kts.domain.member.repository.JdbcTemplateMemberRepository;
import com.cloudschool.kts.domain.member.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class MemberConfig {

    private final DataSource dataSource;

    public MemberConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JdbcTemplateMemberRepository(jdbcTemplate());
    }

}

