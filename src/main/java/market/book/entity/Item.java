package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    private String isbn; // ISBN
    private String name; // 책 이름
    private String introduction; // 책 소개
    private String author; // 저자
    private String contents; // 목차
    private Integer quantity; // 재고 수량
    private Integer price; // 가격

    // Item 엔티티가 관리
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPhoto> itemPhotoList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private Seller seller;

    public Item(String isbn, String name, String introduction, String author, String contents, Integer quantity, Integer price, Seller seller) {
        this.isbn = isbn;
        this.name = name;
        this.introduction = introduction;
        this.author = author;
        this.contents = contents;
        this.quantity = quantity;
        this.price = price;
        this.seller = seller;
    }

    public void addItemPhoto(ItemPhoto itemPhoto) {
        this.itemPhotoList.add(itemPhoto);
        itemPhoto.setItem(this);
    }

    public void changeIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void changeAuthor(String author) {
        this.author = author;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    public void changeQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void changePrice(Integer price) {
        this.price = price;
    }
}
