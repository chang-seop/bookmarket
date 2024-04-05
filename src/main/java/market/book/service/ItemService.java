package market.book.service;

import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.common.file.FileStore;
import market.book.common.file.UploadFile;
import market.book.dto.item.ItemModifyDto;
import market.book.dto.item.ItemSaveDto;
import market.book.entity.Item;
import market.book.entity.ItemPhoto;
import market.book.entity.Member;
import market.book.repository.item.ItemRepository;
import market.book.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    /**
     * 책 등록
     */
    @Transactional
    public void create(Long memberId, ItemSaveDto itemSaveDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다"));

        Item item = new Item(itemSaveDto.getIsbn(),
                itemSaveDto.getName(),
                itemSaveDto.getIntroduction(),
                itemSaveDto.getAuthor(),
                itemSaveDto.getContents(),
                itemSaveDto.getQuantity(),
                itemSaveDto.getPrice());

        // 메인 이미지 등록
        if(!ObjectUtils.isEmpty(itemSaveDto.getMainImage())) {
            MultipartFile mainImage = itemSaveDto.getMainImage();
            if(!mainImage.isEmpty()) {
                try {
                    UploadFile uploadFile = fileStore.storeFile(mainImage);
                    item.addItemPhoto(new ItemPhoto(uploadFile.getStoreFileName(), true));
                } catch(IOException e) {
                    throw new BusinessException("메인 이미지를 업로드할 수 없습니다");
                }
            }
        }

        // 북 서브 이미지 등록
        if(!ObjectUtils.isEmpty(itemSaveDto.getSubImage())) {
            List<MultipartFile> imageList = itemSaveDto.getSubImage();
            imageList.forEach((image) -> {
                if(!image.isEmpty()) {
                    try {
                        UploadFile uploadFile = fileStore.storeFile(image);
                        // item 등록
                        item.addItemPhoto(new ItemPhoto(uploadFile.getStoreFileName(), false));
                    } catch(IOException e) {
                        throw new BusinessException("서브 이미지를 업로드할 수 없습니다");
                    }
                }
            });
        }
        itemRepository.save(item);
    }

    // 책 수정
    @Transactional
    public void update(Long memberId, Long itemId, ItemModifyDto itemModifyDto) {

    }
    // 책 삭제
}
