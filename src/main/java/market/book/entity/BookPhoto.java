package market.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import market.book.entity.common.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookPhoto extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "BOOK_PHOTO_ID")
    private Long id;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public BookPhoto(String imageUrl, Book book) {
        this.imageUrl = imageUrl;
        this.book = book;
    }
}
