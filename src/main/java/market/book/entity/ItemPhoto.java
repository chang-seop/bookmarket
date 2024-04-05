package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPhoto extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "ITEM_PHOTO_ID")
    private Long id;
    private String imageUrl;
    private Boolean imageMainYn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    public ItemPhoto(String imageUrl, Boolean imageMainYn) {
        this.imageUrl = imageUrl;
        this.imageMainYn = imageMainYn;
    }

    /**
     * * 사용 주의 *
     * 연관관계 편의 메서드용 set 메서드
     */
    public void setItem(Item item) {
        this.item = item;
    }
}
