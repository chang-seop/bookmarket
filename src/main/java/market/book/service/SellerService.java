package market.book.service;

import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.dto.seller.SellerModifyDto;
import market.book.dto.seller.SellerSaveDto;
import market.book.entity.Member;
import market.book.entity.Seller;
import market.book.entity.common.Address;
import market.book.repository.seller.SellerRepository;
import market.book.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public void create(Long memberId, SellerSaveDto sellerSaveDto) {
        // 존재 하지 않은 회원
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("존재 하지 않은 회원"));

        // Seller 를 생성했을 경우
        if(!ObjectUtils.isEmpty(member.getSeller())) {
            throw new BusinessException("이미 판매자로 신청하였습니다.");
        }

        // 생성
        Seller seller = new Seller(sellerSaveDto.getEmail(),
                sellerSaveDto.getContact(),
                new Address(sellerSaveDto.getZoneCode(),
                        sellerSaveDto.getSubAddress(),
                        sellerSaveDto.getDetailedAddress()));

        // 등록
        sellerRepository.save(seller);

        // 변경 감지로 연관관계 설정
        member.setSeller(seller);
    }

    @Transactional
    public void modify(Long memberId, SellerModifyDto sellerModifyDto) {
        // 존재 하지 않은 회원
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("존재 하지 않은 회원"));

        // Seller 가 아닌 경우
        if(ObjectUtils.isEmpty(member.getSeller())) {
            throw new BusinessException("판매자가 아닙니다.");
        }

        Seller seller = member.getSeller(); // 조회 (영속성 컨텍스트에 등록)

        // 변경 감지
        seller.changeEmail(sellerModifyDto.getEmail());
        seller.changeContact(sellerModifyDto.getContact());
        seller.changeAddress(new Address(
                sellerModifyDto.getZoneCode(),
                sellerModifyDto.getSubAddress(),
                sellerModifyDto.getDetailedAddress()));
    }
}
