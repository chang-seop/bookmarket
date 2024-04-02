package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.Address;
import market.book.entity.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String password;
    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @OneToMany(mappedBy = "member")
    private List<Authority> authorityList = new ArrayList<>();

    public Member(String username, String email, String nickname, String password, Address address, Profile profile) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.address = address;
        this.profile = profile;
    }

    // 연관관계 편의 메서드
    public void addAuthority(Authority authority) {
        this.authorityList.add(authority);
        authority.changeMember(this);
    }
}
