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
    private String contact;
    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PROFILE_ID")
    // 라이프 사이클이 소유자인 member 에 의존
    private Profile profile;

    // 라이프 사이클이 소유자인 member 에 의존
    // orphanRemoval = true : 부모 엔티티가 삭제되면 자식 엔티티도 함께 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authority> authorityList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private Seller seller;

    public Member(String username, String email, String nickname, String password, String contact, Address address, Profile profile) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.profile = profile;
    }

    /**
     * * 사용 주의 *
     */
    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    // 멤버가 authority 를 관리
    // 연관관계 편의 메서드
    public void addAuthority(Authority authority) {
        authorityList.add(authority);
        authority.setMember(this);
    }

    // 비즈니스 코드
    public void changeAddress(Address address) {
        this.address = address;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changeContact(String contact) {
        this.contact = contact;
    }
    public void removeSeller() {
        this.seller = null;
    }
}
