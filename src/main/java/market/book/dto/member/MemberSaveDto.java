package market.book.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSaveDto {
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String contact;
    private String zoneCode;
    private String subAddress;
    private String detailedAddress;
}
