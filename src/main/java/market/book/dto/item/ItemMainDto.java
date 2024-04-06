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
    private String imageUrl;

    public ItemMainDto(Long id, String name, String author, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
