package market.book.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberMyPageDto {
    private String email;
    private String username;
    private String nickname;
    private String contact;
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;
    private String stateMessage;
    private String imageUrl;

    @Builder
    public MemberMyPageDto(String email, String username, String nickname, String contact, String zoneCode, String subAddress, String detailedAddress, String stateMessage, String imageUrl) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.contact = contact;
        this.zoneCode = zoneCode;
        this.subAddress = subAddress;
        this.detailedAddress = detailedAddress;
        this.stateMessage = stateMessage;
        this.imageUrl = imageUrl;
    }
}
