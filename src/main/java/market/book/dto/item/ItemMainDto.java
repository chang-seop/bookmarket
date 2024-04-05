package market.book.dto.item;

import lombok.Data;
import market.book.entity.Item;

import java.util.List;

@Data
public class ItemMainDto {
    private Long id;
    private String name;
    private String author;
    private Integer price;
    private List<ItemPhotoDto> itemPhotoDtoList;

    public ItemMainDto(Item item) {
        this.name = item.getName();
        this.author = item.getAuthor();
        this.price = item.getPrice();
        this.itemPhotoDtoList = item.getItemPhotoList()
                .stream()
                .map(ItemPhotoDto::new).toList();
    }
}
