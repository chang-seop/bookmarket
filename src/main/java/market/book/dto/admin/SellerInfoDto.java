package market.book.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
public class SellerInfoDto {
    private Long memberId;
    private String email;
    private String contact;
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;
    @Builder
    public SellerInfoDto(Long memberId, String email, String contact, String zoneCode, String subAddress, String detailedAddress) {
        this.memberId = memberId;
        this.email = email;
        this.contact = contact;
        this.zoneCode = zoneCode;
        this.subAddress = subAddress;
        this.detailedAddress = detailedAddress;
    }
}
