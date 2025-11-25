package kr.java.jpa.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity // JPA에서 관리하는 엔티티임을 의미
@Table(name = "members")
public class Member {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL -> Auto Increment
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true) // NOT NULL UNIQUE
    private String username;

    @Column(nullable = false) // NOT NULL
    private String password;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    // JPA -> 기본 생성자 -> Setting
    protected Member() {}

    // ID, LocalDateTime -> 자동생성
    // DTO 마냥 직접 넣어서 만들어주는...
    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now(); // JPA <- 직접해주지 않으면 처리할 줄을 모름
        // 1. 스키마(DDL)을 직접 만들면서 CURRENT_TIMESTAMP를 지정
        // 2. Audit을 설정 -> Spring Boot 가서...
        // 3. * Entity 단위에서 코딩으로 집어넣기
        // -> JPA의 목적은 벤더 의존성(SQL 소프트웨어)을 줄이는 것
        // -> CURRENT_TIMESTAMP <= 소프트웨어마다 다름
    }

    // Getter/Setter
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
