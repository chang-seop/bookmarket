package market.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.common.file.FileStore;
import market.book.dto.item.ItemSaveDto;
import market.book.dto.member.MemberDetailsDto;
import market.book.dto.seller.SellerModifyDto;
import market.book.dto.seller.SellerMyItemDto;
import market.book.dto.seller.SellerSaveDto;
import market.book.entity.Member;
import market.book.repository.item.ItemQueryRepository;
import market.book.repository.member.MemberRepository;
import market.book.service.ItemService;
import market.book.service.SellerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    private final ItemService itemService;
    private final ItemQueryRepository itemQueryRepository;
    /**
     * 판매자 등록 폼
     */
    @GetMapping("/add")
    public String addView(@ModelAttribute SellerSaveDto sellerSaveDto) {
        return "seller/add";
    }

    /**
     * 판매자 등록
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
     * 수정 폼
     */
    @GetMapping("/modify")
    public String modifyView(@ModelAttribute SellerModifyDto sellerModifyDto) {
        return "seller/modify";
    }

    /**
     * 판매자가 등록 한 아이템 폼
     */
    @GetMapping("/items")
    public String itemView(@PageableDefault(page = 0, size = 20) Pageable pageable,
                           @AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                           Model model) {
        Member member = memberRepository.findFetchSellerById(memberDetailsDto.getMemberId())
                .orElseThrow(() -> new BusinessException("존재 하지 않는 회원 또는 판매자 등록이 안된 회원"));
        Page<SellerMyItemDto> list = itemQueryRepository.findPageSellerMyItemDtoBySellerId(member.getSeller().getId(), pageable);
        model.addAttribute("list", list);
        return "seller/item";
    }

    @GetMapping("/items/add")
    public String addView(@ModelAttribute ItemSaveDto itemSaveDto) {
        return "seller/itemAdd";
    }

    @PostMapping("/items/add")
    public String add(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                      @Valid @ModelAttribute ItemSaveDto itemSaveDto,
                      BindingResult bindingResult,
                      Errors errors) {
        if(bindingResult.hasErrors()) {
            return "seller/itemAdd";
        }

        if(!ObjectUtils.isEmpty(itemSaveDto.getMainImage())) {
            if(!fileStore.isImageFile(itemSaveDto.getMainImage())) {
                errors.reject("addFail", "메인 이미지 파일은 jpg, png, gif 만 가능합니다");
            }
        }

        if(!ObjectUtils.isEmpty(itemSaveDto.getSubImage())) {
            itemSaveDto.getSubImage().forEach((imageFile) -> {
                if(!fileStore.isImageFile(imageFile)) {
                    errors.reject("addFail", "서브 이미지 파일은 jpg, png, gif 만 가능합니다");
                }
            });
        }

        try{
            itemService.create(memberDetailsDto.getMemberId(), itemSaveDto);
        } catch(BusinessException e) {
            errors.reject("addFail", e.getMessage());
            return "seller/itemAdd";
        }

        return "redirect:/sellers/items";
    }
}
