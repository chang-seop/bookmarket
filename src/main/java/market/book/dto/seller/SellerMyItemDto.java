package market.book.dto.seller;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SellerMyItemDto {
    private Long id;
    private String name;
    private Integer quantity;
    private Integer price;
    private LocalDateTime createdDate;

    public SellerMyItemDto(Long id, String name, Integer quantity, Integer price, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.createdDate = createdDate;
    }
}
