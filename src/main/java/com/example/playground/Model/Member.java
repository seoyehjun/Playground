package com.example.playground.Model;

import com.example.playground.Config.UserInfo.UserRole;
import jakarta.persistence.*;
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
    private String profileImageUrl; // 프로필 이미지 URL 필드 추가
    @CreationTimestamp
    private Timestamp createDate;

    public Member() {
    }

    @Builder
    public Member(int id, String nickname, String password, String userpw, String email, UserRole role, String provider,
                  String providerId, String loginId, String profileImageUrl, Timestamp createDate)
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
        this.profileImageUrl = profileImageUrl; // 프로필 이미지 URL 필드 초기화
        this.createDate = createDate;
    }


    //@PrePersist는 엔티티가 처음 데이터베이스에 저장되기 전에만 호출됩니다.
    // 따라서 데이터가 이미 저장된 후에 profileImageUrl이 null이 되면 @PrePersist가 다시 발동되지 않습니다.
    @PrePersist
    public void prePersist()
    {
        if (this.profileImageUrl == null || this.profileImageUrl.isEmpty())
        {
            this.profileImageUrl = "/lib/images/profile/default_profile_image.png"; // 기본 프로필 이미지 경로 설정
        }
    }

}
