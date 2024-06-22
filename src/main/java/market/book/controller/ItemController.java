package market.book.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.common.file.FileStore;
import market.book.dto.item.ItemMainDto;
import market.book.dto.item.ItemSaveDto;
import market.book.dto.member.MemberDetailsDto;
import market.book.entity.Item;
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
        Page<ItemMainDto> pageItemMainDto = itemQueryRepository.findPageItemMainDto(null, pageable);
        model.addAttribute("list", pageItemMainDto);
        return "item/main";
    }

    @GetMapping("/{itemId}")
    public String detailView(@PathVariable Long itemId,
                             Model model) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException("찾을 수 없는 아이템"));

        model.addAttribute("item", item);
        return "item/detail";
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
