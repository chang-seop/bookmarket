package market.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor
public class Profile extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "PROFILE_ID")
    private Long id;
    private String imageUrl;
    private String stateMessage;
    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void changeStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
