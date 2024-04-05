package market.book.dto.item;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemSaveDto {
    private String isbn; // ISBN
    private String name; // 책 이름
    private String introduction; // 책 소개
    private String author; // 저자
    private String contents; // 목차
    private Integer quantity; // 재고 수량
    private Integer price; // 가격
    private MultipartFile mainImage; // 메인 이미지
    private List<MultipartFile> subImage; // 서브 이미지

    @Builder
    public ItemSaveDto(String isbn, String name, String introduction, String author, String contents, Integer quantity, Integer price) {
        this.isbn = isbn;
        this.name = name;
        this.introduction = introduction;
        this.author = author;
        this.contents = contents;
        this.quantity = quantity;
        this.price = price;
    }
}
