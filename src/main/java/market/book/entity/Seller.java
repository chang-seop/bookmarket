package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.Address;
import market.book.entity.common.BaseTimeEntity;
import org.springframework.security.core.parameters.P;

// 일반 판매자일 경우 가족 관계 증명서 및 등본 필요
// 기업일 경우 사업자 등록 번호 필요
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "SELLER_ID")
    private Long id;
    private String email;
    private String contact; // 전화 번호
    @Embedded
    private Address address; // 회사 주소

    public Seller(String email, String contact, Address address) {
        this.email = email;
        this.contact = contact;
        this.address = address;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeContact(String contact) {
        this.contact = contact;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }
}
