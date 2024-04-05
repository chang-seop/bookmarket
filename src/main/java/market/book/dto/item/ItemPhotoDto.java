package market.book.dto.item;

import lombok.Data;
import market.book.entity.ItemPhoto;

@Data
public class ItemPhotoDto {
    private String imageUrl;
    private Boolean imageMainYn;

    public ItemPhotoDto(ItemPhoto itemPhotoDto) {
        this.imageUrl = itemPhotoDto.getImageUrl();
        this.imageMainYn = itemPhotoDto.getImageMainYn();
    }
}
