package market.book.repository.item;

import com.querydsl.core.types.Projections;
import market.book.common.querydsl.Querydsl5RepositorySupport;
import market.book.dto.item.ItemMainDto;
import market.book.dto.item.ItemSearchCond;
import market.book.dto.seller.SellerMyItemDto;
import market.book.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static market.book.entity.QItem.*;
import static market.book.entity.QItemPhoto.*;
import static market.book.entity.QSeller.seller;

@Repository
public class ItemQueryRepository extends Querydsl5RepositorySupport {
    public ItemQueryRepository() {
        super(Item.class);
    }

    /**
     * Dto 딱 조회, imageMainYn 여부가 true 일 경우에만 이미지 url 을 가져오기
     * 이미지를 등록 안한 아이템도 있기 때문에 on 절로 가져오기 where X
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
                        .on(itemPhoto.imageMainYn.eq(true))
                        .orderBy(item.createdDate.desc()), // 최신 순
                (countQuery) -> countQuery
                        .select(item.count())
                        .from(item));
    }

    /**
     * 판매 회원 아이디를 가지고 자기가 등록 한 상품 조회
     * ManyToOne 조인
     */
    public Page<SellerMyItemDto> findPageSellerMyItemDtoBySellerId(Long sellerId, Pageable pageable) {
        return applyPagination(pageable,
                (contentQuery) -> contentQuery
                        .select(Projections.constructor(SellerMyItemDto.class,
                                item.id,
                                item.name,
                                item.quantity,
                                item.price,
                                item.createdDate))
                        .from(item)
                        .where(item.seller.id.eq(sellerId)),
                (countQuery) -> countQuery
                        .select(item.count())
                        .from(item)
                        .where(item.seller.id.eq(sellerId)));
    }
}
