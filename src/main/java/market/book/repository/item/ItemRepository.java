package market.book.repository.item;

import market.book.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i" +
            " inner join i.seller s" +
            " inner join s.member m" +
            " where m.id = :memberId and i.id = :itemId")
    Optional<Item> findItemCheckMemberByMemberIdAndItemId(Long memberId, Long itemId);
}
