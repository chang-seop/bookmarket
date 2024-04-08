package market.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.dto.member.MemberDetailsDto;
import market.book.dto.seller.SellerModifyDto;
import market.book.dto.seller.SellerSaveDto;
import market.book.entity.Member;
import market.book.entity.Seller;
import market.book.repository.member.MemberRepository;
import market.book.repository.seller.SellerRepository;
import market.book.service.SellerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final SellerRepository sellerRepository;
    private final MemberRepository memberRepository;
    /**
     * 등록 폼
     */
    @GetMapping("/add")
    public String addView(@ModelAttribute SellerSaveDto sellerSaveDto) {
        return "seller/add";
    }

    /**
     * 등록
     */
    @PostMapping("/add")
    public String add(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                      @Valid @ModelAttribute SellerSaveDto sellerSaveDto,
                      BindingResult bindingResult,
                      Errors errors) {
        if (bindingResult.hasErrors()) {
            return "seller/add";
        }

        try{
            sellerService.create(memberDetailsDto.getMemberId(), sellerSaveDto);
        } catch (BusinessException e) {
            errors.reject("globalError", e.getMessage());
            return "seller/add";
        }

        return "redirect:/sellers/main";
    }

    /**
     * 메인 폼
     * 판매자 등록을 완료한 사람은 버튼이 보이면 안된다.
     */
    @GetMapping("/main")
    public String mainView(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                           Model model) {
        Optional<Member> memberOptional = memberRepository.findInnerSellerById(memberDetailsDto.getMemberId());
        if(memberOptional.isPresent()) {
            // 판매자 등록을 완료한 사람
            model.addAttribute("isSeller", true);
        }

        return "seller/main";
    }

    /**
     * 등록 한 아이템 폼
     */
    @GetMapping("/items")
    public String itemView() {
        return "seller/item";
    }

    /**
     * 수정 폼
     */
    @GetMapping("/modify")
    public String modifyView(@ModelAttribute SellerModifyDto sellerModifyDto) {
        return "seller/modify";
    }
}
