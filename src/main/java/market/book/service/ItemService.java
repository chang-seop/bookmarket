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
import market.book.repository.item.ItemPhotoRepository;
import market.book.repository.item.ItemRepository;
import market.book.repository.member.MemberRepository;
import market.book.repository.seller.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;
    private final FileStore fileStore;
    private final ItemPhotoRepository itemPhotoRepository;
    /**
     * 책 등록
     */
    @Transactional
    public void create(Long memberId, ItemSaveDto itemSaveDto) {
        // 판매자 등록 확인
        Member member = memberRepository.findFetchSellerById(memberId) // 페치 조인으로 가져오기
                .orElseThrow(() -> new BusinessException("존재 하지 않은 회원 또는 판매자 등록을하지 않은 회원"));

        Item item = new Item(itemSaveDto.getIsbn(),
                itemSaveDto.getName(),
                itemSaveDto.getIntroduction(),
                itemSaveDto.getAuthor(),
                itemSaveDto.getContents(),
                itemSaveDto.getQuantity(),
                itemSaveDto.getPrice(),
                member.getSeller()); // 판매자 등록

        addMainImage(itemSaveDto.getMainImage(), item);
        addSubImageList(itemSaveDto.getSubImage(), item);

        itemRepository.save(item);
    }

    /**
     * 서브 이미지 등록
     */
    private void addSubImageList(List<MultipartFile> subImageList, Item item) {
        if(!ObjectUtils.isEmpty(subImageList)) {
            subImageList.forEach((image) -> {
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
    }

    /**
     * 메인 이미지 등록
     */
    private void addMainImage(MultipartFile mainImage, Item item) {
        if(!ObjectUtils.isEmpty(mainImage)) {
            if(!mainImage.isEmpty()) {
                try {
                    UploadFile uploadFile = fileStore.storeFile(mainImage);
                    item.addItemPhoto(new ItemPhoto(uploadFile.getStoreFileName(), true));
                } catch(IOException e) {
                    throw new BusinessException("메인 이미지를 업로드할 수 없습니다");
                }
            }
        }
    }

    // 책 수정
    @Transactional
    public void modify(Long memberId, Long itemId, ItemModifyDto itemModifyDto) {
        // 판매자 등록 및 자신의 상품인지 확인
        Item item = itemRepository.findItemCheckMemberByMemberIdAndItemId(memberId, itemId)
                .orElseThrow(() -> new BusinessException("판매자의 아이템이 아닙니다"));

        // 변경 감지
        item.changeIsbn(itemModifyDto.getIsbn());
        item.changeName(itemModifyDto.getName());
        item.changeIntroduction(itemModifyDto.getIntroduction());
        item.changeAuthor(itemModifyDto.getAuthor());
        item.changeContents(itemModifyDto.getContents());
        item.changeQuantity(itemModifyDto.getQuantity());
        item.changePrice(itemModifyDto.getPrice());

        List<ItemPhoto> itemPhotoList = item.getItemPhotoList(); // batch size 로 일대다 1번 조회
        ArrayList<String> deleteFileList = new ArrayList<>(); // 삭제 파일
        List<ItemPhoto> deletePhotoList = new ArrayList<>();

        // 메인 이미지 삭제
        if(ObjectUtils.isEmpty(itemModifyDto.getMainImageUrl())) {
            // NULL 값 일 경우 메인 이미지 삭제
            itemPhotoList.forEach((itemPhoto) -> {
                if(itemPhoto.getImageMainYn()) {
                    deleteFileList.add(itemPhoto.getImageUrl()); // 파일 삭제 리스트에 등록
                    deletePhotoList.add(itemPhoto); // DB 삭제 리스트에 등록
                }
            });
        }

        // 서브 이미지 삭제
        if(!ObjectUtils.isEmpty(itemModifyDto.getSubImageUrls())) {
            // null 이 아닐 경우
            itemPhotoList.forEach(itemPhoto -> {
                // 메인 이미지가 아닌 서브 이미지 만
                if(!itemPhoto.getImageMainYn()) {
                    // subImageUrls 리스트 안에 일치하는 값이 없을 경우
                    if(!itemModifyDto.getSubImageUrls().contains(itemPhoto.getImageUrl())) {
                        deleteFileList.add(itemPhoto.getImageUrl()); // 파일 삭제 리스트에 등록
                        deletePhotoList.add(itemPhoto); // DB 삭제 리스트에 등록
                    }
                }
            });
        } else {
            // null 일 경우 기존 서브 이미지 전부 삭제
            itemPhotoList.forEach(itemPhoto -> {
                if(!itemPhoto.getImageMainYn()) {
                    deleteFileList.add(itemPhoto.getImageUrl());
                    deletePhotoList.add(itemPhoto); // DB 삭제 리스트에 등록
                }
            });
        }

        // DB 삭제
        // orphanRemoval 옵션이 있기 때문에 고아가 된 ItemPhoto 요소들은 삭제 된다. (단점 : delete 쿼리가 N 번 나간다)
        // TODO: 배치 삭제 이용 (장점 : delete 쿼리가 한번 나간다)
        itemPhotoList.removeAll(deletePhotoList); // 전체 삭제

        addMainImage(itemModifyDto.getMainImage(), item); // 메인 이미지 등록
        addSubImageList(itemModifyDto.getSubImage(), item); // 서브 이미지 등록

        // 파일 삭제
        deleteFileList.forEach((fileUrl) -> {
            if(!fileStore.removeFile(fileUrl)) {
                throw new BusinessException("파일을 삭제할 수 없습니다");
            }
        });
    }

    // 책 삭제

    // 책 + 책 이미지 DTO 조회
    public ItemModifyDto findItemModifyDto(Long memberId, Long itemId) {
        Item item = itemRepository.findItemCheckMemberByMemberIdAndItemId(memberId, itemId)
                .orElseThrow(() -> new BusinessException("판매자의 상품이 아닙니다"));

        List<String> subImageUrls = new ArrayList<>();
        String mainImageUrl = null;

        for (ItemPhoto itemPhoto : item.getItemPhotoList()) {
            if(itemPhoto.getImageMainYn()) {
                // 메인 이미지
                mainImageUrl = itemPhoto.getImageUrl();
            } else {
                // 서브 이미지
                subImageUrls.add(itemPhoto.getImageUrl());
            }
        }

        return ItemModifyDto.builder()
                .isbn(item.getIsbn())
                .name(item.getName())
                .introduction(item.getIntroduction())
                .author(item.getAuthor())
                .contents(item.getContents())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .mainImageUrl(mainImageUrl)
                .subImageUrls(subImageUrls)
                .build();
    }
}
