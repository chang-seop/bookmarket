package market.book.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberApiDto {
    private String email;
    private String contact;
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;

    @Builder
    public MemberApiDto(String email, String contact, String zoneCode, String subAddress, String detailedAddress) {
        this.email = email;
        this.contact = contact;
        this.zoneCode = zoneCode;
        this.subAddress = subAddress;
        this.detailedAddress = detailedAddress;
    }
}
