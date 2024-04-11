package market.book.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.book.common.exception.BusinessException;
import market.book.dto.admin.AdminMemberManageDto;
import market.book.dto.admin.SellerInfoDto;
import market.book.entity.Member;
import market.book.entity.Seller;
import market.book.repository.member.MemberQueryRepository;
import market.book.repository.member.MemberRepository;
import market.book.service.MemberService;
import market.book.service.SellerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final MemberService memberService;
    private final SellerService sellerService;

    /**
     * 메인 폼
     */
    @GetMapping("/main")
    public String mainView() {
        return "admin/main";
    }

    /**
     * 판매자 등록 관리 (권한 부여)
     */
    @GetMapping("/sellers")
    public String sellerRoleView(@PageableDefault(page = 0, size = 20) Pageable pageable,
                             Model model) {
        Page<AdminMemberManageDto> list = memberQueryRepository.findPageMemberInnerSeller(pageable);

        model.addAttribute("list", list);
        return "admin/sellerRole";
    }

    /**
     * 판매자 등록 detail 조회
     */
    @GetMapping("/sellers/{memberId}")
    public String sellerRoleDetailView(@PathVariable Long memberId,
                                   Model model) {
        Member member = memberRepository.findFetchSellerById(memberId)
                .orElseThrow(() -> new BusinessException("회원이 존재하지 않습니다"));
        Seller seller = member.getSeller();

        SellerInfoDto sellerInfo = SellerInfoDto.builder()
                .memberId(member.getId())
                .email(seller.getEmail())
                .contact(seller.getContact())
                .zoneCode(seller.getAddress().getZoneCode())
                .subAddress(seller.getAddress().getSubAddress())
                .detailedAddress(seller.getAddress().getDetailedAddress())
                .build();

        model.addAttribute("sellerInfo", sellerInfo);
        return "admin/sellerRoleDetail";
    }

    /**
     * 권한 부여
     */
    @PostMapping("/sellers/{memberId}/add")
    public String addSellerRole(@PathVariable Long memberId) {
        // ROLE_SELLER 권한 추가
        memberService.addMemberRoleSeller(memberId);

        return "redirect:/admins/sellers";
    }

    /**
     * 권한 취소 (Seller 삭제)
     */
    @PostMapping("/sellers/{memberId}/cancel")
    public String cancelSellerRole(@PathVariable Long memberId) {
        // SELLER 정보 삭제
        sellerService.delete(memberId);

        return "redirect:/admins/sellers";
    }
}
