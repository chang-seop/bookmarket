package market.book.dto.member;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberModifyDto {
    private String username;
    private String nickname;
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;
    private String stateMessage;
    private MultipartFile imageFile;

    @Builder
    public MemberModifyDto(String username, String nickname, String zoneCode, String subAddress, String detailedAddress, String stateMessage, MultipartFile imageFile) {
        this.username = username;
        this.nickname = nickname;
        this.zoneCode = zoneCode;
        this.subAddress = subAddress;
        this.detailedAddress = detailedAddress;
        this.stateMessage = stateMessage;
        this.imageFile = imageFile;
    }
}
