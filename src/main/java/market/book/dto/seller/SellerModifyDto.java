package market.book.dto.seller;

import lombok.Builder;
import lombok.Data;

@Data
public class SellerModifyDto {
    private String email;
    private String contact;
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;

    @Builder
    public SellerModifyDto(String email, String contact, String zoneCode, String subAddress, String detailedAddress) {
        this.email = email;
        this.contact = contact;
        this.zoneCode = zoneCode;
        this.subAddress = subAddress;
        this.detailedAddress = detailedAddress;
    }
}
