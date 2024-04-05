package market.book.repository.item;

import market.book.common.querydsl.Querydsl5RepositorySupport;
import market.book.dto.item.ItemSearchCond;
import market.book.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static market.book.entity.QItem.*;

@Repository
public class ItemQueryRepository extends Querydsl5RepositorySupport {
    public ItemQueryRepository() {
        super(Item.class);
    }

    public Page<Item> findPageItemMainDto(ItemSearchCond itemSearchCond, Pageable pageable) {
        return applyPagination(pageable,
                (contentQuery) -> contentQuery
                        .select(item)
                        .from(item),
                (countQuery) -> countQuery
                        .select(item.count())
                        .from(item));
    }
}
