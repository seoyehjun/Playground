package com.example.playground.Model;

import com.example.playground.Config.UserInfo.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Member
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String nickname;
    private String password;
    private String userpw;
    private String email;
    private UserRole role; //ROLE_USER, ROLE_ADMIN
    private String provider;
    private String providerId;
    private String loginId;
    @CreationTimestamp
    private Timestamp createDate;

    public Member() {
    }

    @Builder
    public Member(int id, String nickname, String password, String userpw, String email, UserRole role, String provider,
                  String providerId, String loginId, Timestamp createDate)
    {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.userpw = userpw;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.loginId = loginId;
        this.createDate = createDate;
    }
}
