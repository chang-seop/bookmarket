package market.book.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.book.dto.admin.AdminMemberManageDto;
import market.book.repository.member.MemberQueryRepository;
import market.book.repository.member.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

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
    public String sellerView(@PageableDefault(page = 0, size = 20) Pageable pageable,
                             Model model) {
        Page<AdminMemberManageDto> list = memberQueryRepository.findPageMemberInnerSeller(pageable);

        model.addAttribute("list", list);
        return "admin/seller";
    }

    /**
     * 판매자 등록 detail 조회
     */
    @GetMapping("/sellers/{memberId}")
    public String sellerMemberView(@PathVariable Long memberId) {
        return "admin/detail";
    }
}
