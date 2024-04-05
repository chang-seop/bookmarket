package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.BaseTimeEntity;
import market.book.entity.type.RoleType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Authority(RoleType role) {
        this.role = role;
    }

    /**
     * * 사용 주의 *
     * 연관관계 편의 메서드용 set 메서드
     */
    public void setMember(Member member) {
        this.member = member;
    }
}
