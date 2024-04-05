package market.book.repository.item;

import market.book.entity.ItemPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemPhotoRepository extends JpaRepository<ItemPhoto, Long> {
    Optional<ItemPhoto> findByImageUrl(String imageUrl);
}
