package market.book.entity.common;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String zoneCode;
    private String jibunAddress;
    private String roadAddress;
    private String detailedAddress;

    @Builder
    public Address(String zoneCode, String jibunAddress, String roadAddress, String detailedAddress) {
        this.zoneCode = zoneCode;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.detailedAddress = detailedAddress;
    }
}
