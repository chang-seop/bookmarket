package market.book.dto.admin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminMemberManageDto {
    private Long memberId;
    private String username;
    private String email;
    private String nickname;
    private String contact;
    private LocalDateTime createdSellerDate;

    @Builder
    public AdminMemberManageDto(Long memberId, String username, String email, String nickname, String contact, LocalDateTime createdSellerDate) {
        this.memberId = memberId;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.contact = contact;
        this.createdSellerDate = createdSellerDate;
    }
}
