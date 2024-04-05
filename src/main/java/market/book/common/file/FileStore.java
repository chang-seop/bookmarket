package market.book.common.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    /**
     * 파일 저장 경로 반환
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 파일 삭제
     */
    public boolean removeFile(String fileUrl) {
        File removeFile = new File(getFullPath(fileUrl));
        log.info("파일삭제 : {}", fileUrl);
        return removeFile.delete();
    }

    /**
     * 이미지 파일 .jpeg, .png, .gif 검사 / 파일을 등록하지 않았을 때 통과
     */
    public Boolean isImageFile(MultipartFile multipartFiles) {
        if(!ObjectUtils.isEmpty(multipartFiles)) {
            if(!multipartFiles.isEmpty()) {
                String contentType = multipartFiles.getContentType();
                if(contentType.contains(ImageCode.JPG.getCode())) return true;
                if(contentType.contains(ImageCode.PNG.getCode())) return true;
                if(contentType.contains(ImageCode.GIF.getCode())) return true;
                return false;
            }
        }
        // 파일을 등록하지 않았을 경우
        return true;
    }

    /**
     * 멀티 파트 파일을 가지고 파일을 저장 후 UploadFile 로 만든 후 반환
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = getStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    /**
     * 서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID 를 사용해서 충돌하지 않도록 한다.
     */
    private String getStoreFileName(String originalFilename) {
        // 고객이 입력한 파일 : image.png
        // 서버에 저장하는 파일명 (UUID 를 쓰는데 확장자 명을 남겨두기)
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext; // 저장할 파일 이름 예) 51041c62-86e4-4274-801d-614a7d994edb.png 반환
    }

    /**
     * 확장자를 별도로 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다. 예를 들어서
     * 고객이 a.png 라는 이름으로 업로드하면 51041c62-86e4-4274-801d-614a7d994edb.png 와 같이 저장한다.
     */
    private String extractExt(String originalFilename) { // 예) "png" 반환
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
