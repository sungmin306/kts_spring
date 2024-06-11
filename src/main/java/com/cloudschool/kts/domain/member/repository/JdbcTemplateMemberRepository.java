package com.cloudschool.kts.domain.member.repository;

import com.cloudschool.kts.domain.member.model.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcTemplateMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("username", member.getUsername());
        parameters.put("image_url", member.getImageUrl());
        parameters.put("roles", member.getRoles().name());

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        member.setId(key.longValue());
        return member;
    }
    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> result = jdbcTemplate.query("select * from member where email = ?", memberRowMapper(), email);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setUsername(rs.getString("username"));
            return member;
        };
    }
//

//    @Override
//    public void deleteById(Long id) {
//        String sql = "DELETE FROM member WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }
//
//    @Override
//    public Boolean existsByEmail(String email) {
//        String sql = "SELECT COUNT(*) FROM member WHERE email = ?";
//        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
//        return count != null && count > 0;
//    }
}