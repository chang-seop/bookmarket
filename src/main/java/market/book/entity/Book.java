package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "BOOK_ID")
    private Long id;
    private String isbn; // ISBN
    private String name; // 책 이름
    private String introduction; // 책 소개
    private String author; // 저자
    private String contents; // 목차
    private Integer quantity; // 재고 수량
    private Integer price; // 가격

    public Book(String isbn, String name, String introduction, String author, String contents, Integer quantity, Integer price) {
        this.isbn = isbn;
        this.name = name;
        this.introduction = introduction;
        this.author = author;
        this.contents = contents;
        this.quantity = quantity;
        this.price = price;
    }
}
