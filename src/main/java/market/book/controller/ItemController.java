package market.book.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.common.file.FileStore;
import market.book.dto.item.ItemMainDto;
import market.book.dto.item.ItemSaveDto;
import market.book.dto.member.MemberDetailsDto;
import market.book.entity.ItemPhoto;
import market.book.repository.item.ItemPhotoRepository;
import market.book.repository.item.ItemQueryRepository;
import market.book.repository.item.ItemRepository;
import market.book.service.ItemService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final FileStore fileStore;
    private final ItemQueryRepository itemQueryRepository;
    private final ItemPhotoRepository itemPhotoRepository;
    @GetMapping
    public String mainView(@PageableDefault(page = 0, size = 20) Pageable pageable,
                           Model model) {
        Page<ItemMainDto> pageItemMainDto = itemQueryRepository.findPageItemMainDto(null, pageable)
                .map(ItemMainDto::new);
        model.addAttribute("list", pageItemMainDto);
        return "item/main";
    }

    @GetMapping("/add")
    public String addView(@ModelAttribute ItemSaveDto itemSaveDto) {

        return "item/add";
    }

    @PostMapping("/add")
    public String add(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                      @Valid @ModelAttribute ItemSaveDto itemSaveDto,
                      BindingResult bindingResult,
                      Errors errors) {
        if(bindingResult.hasErrors()) {
            return "/item/add";
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
            return "/item/add";
        }

        return "redirect:/items";
    }

    @ResponseBody
    @GetMapping("/images/{imageUrl}")
    public ResponseEntity<Resource> itemImage(@PathVariable String imageUrl,
                                                 HttpServletResponse response) throws IOException {
        Optional<ItemPhoto> findItemPhoto = itemPhotoRepository.findByImageUrl(imageUrl);

        // DB 에 존재하는지 확인
        if(findItemPhoto.isPresent()) {
            String imageName = findItemPhoto.get().getImageUrl();
            try {
                UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(imageName));

                return ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(15, TimeUnit.DAYS)) // 15일 캐시 설정
                        .body(resource);

            } catch (IOException e) {
                throw new BusinessException("이미지를 찾을 수 없습니다");
            }
        }

        response.sendError(404);
        return null;
    }
}
