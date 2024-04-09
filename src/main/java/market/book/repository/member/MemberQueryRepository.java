package market.book.repository.member;

import com.querydsl.core.types.Projections;
import market.book.common.querydsl.Querydsl5RepositorySupport;
import market.book.dto.admin.AdminMemberManageDto;
import market.book.entity.Member;
import market.book.entity.type.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static market.book.entity.QAuthority.*;
import static market.book.entity.QMember.*;
import static market.book.entity.QSeller.*;

@Repository
public class MemberQueryRepository extends Querydsl5RepositorySupport {
    public MemberQueryRepository() {
        super(Member.class);
    }

    /**
     * 판매자 등록을 완료한 멤버 조회
     * 판매자(Seller) 등록을 꼭 해야 함으로 INNER
     * Authority(권한)에 'ROLE_USER', 'ROLE_SELLER' 중에 'ROLE_SELLER' 권한이 없는 사람
     * + 페이징 처리
     */
    public Page<AdminMemberManageDto> findPageMemberInnerSeller(Pageable pageable) {
        return applyPagination(pageable, (contentQuery) -> contentQuery
                        .select(Projections.constructor(AdminMemberManageDto.class,
                                member.id,
                                member.username,
                                member.email,
                                member.nickname,
                                member.contact,
                                seller.createdDate))
                        .from(member)
                        .innerJoin(member.seller, seller)
                        .innerJoin(member.authorityList, authority)
                        .where(authority.role.in(RoleType.ROLE_USER, RoleType.ROLE_SELLER))
                        .groupBy(member)
                        .having(authority.count().lt(2)) // 2 미만일 경우 (ROLE_SELLER 가 없을 경우)
                        .orderBy(seller.createdDate.desc()),
                (countQuery) -> countQuery
                        .select(member.count())
                        .from(member)
                        .innerJoin(member.seller, seller)
                        .innerJoin(member.authorityList, authority)
                        .where(authority.role.in(RoleType.ROLE_USER, RoleType.ROLE_SELLER))
                        .groupBy(member)
                        .having(authority.count().lt(2)));
    }
}
