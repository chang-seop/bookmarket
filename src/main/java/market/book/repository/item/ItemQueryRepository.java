package market.book.repository.item;

import com.querydsl.core.types.Projections;
import market.book.common.querydsl.Querydsl5RepositorySupport;
import market.book.dto.item.ItemMainDto;
import market.book.dto.item.ItemSearchCond;
import market.book.entity.Item;
import market.book.entity.QItemPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static market.book.entity.QItem.*;
import static market.book.entity.QItemPhoto.*;

@Repository
public class ItemQueryRepository extends Querydsl5RepositorySupport {
    public ItemQueryRepository() {
        super(Item.class);
    }

    /**
     * Dto 딱 조회, imageMainYn 여부가 true 일 경우에만 이미지 url 을 가져오기
     * 비즈니스 적으로 main 인 이미지는 딱 하나 이므로 on 절로 가져오기
     * 페이징 이므로 left join
     */
    public Page<ItemMainDto> findPageItemMainDto(ItemSearchCond itemSearchCond, Pageable pageable) {
        return applyPagination(pageable,
                (contentQuery) -> contentQuery
                        .select(Projections.constructor(ItemMainDto.class,
                                item.id,
                                item.name,
                                item.author,
                                item.price,
                                itemPhoto.imageUrl))
                        .from(item)
                        .leftJoin(item.itemPhotoList, itemPhoto)
                        .on(itemPhoto.imageMainYn.eq(true)),
                (countQuery) -> countQuery
                        .select(item.count())
                        .from(item));
    }
}
