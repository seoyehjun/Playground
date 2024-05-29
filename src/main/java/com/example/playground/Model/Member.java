package com.example.playground.Model;

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
    private String username;
    private String password;
    private String userpw;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN
    private String provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;

    public Member() {
    }

    @Builder
    public Member(int id, String username, String password, String userpw, String email, String role, String provider, String providerId, Timestamp createDate)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userpw = userpw;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
