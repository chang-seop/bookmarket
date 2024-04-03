package market.book.entity.common;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;

    @Builder
    public Address(String zoneCode, String subAddress, String detailedAddress) {
        this.zoneCode = zoneCode;
        this.subAddress = subAddress;
        this.detailedAddress = detailedAddress;
    }
}
