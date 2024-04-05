package market.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.book.common.file.FileStore;
import market.book.common.file.UploadFile;
import market.book.dto.member.MemberModifyDto;
import market.book.dto.member.MemberSaveDto;
import market.book.entity.Authority;
import market.book.entity.Member;
import market.book.entity.Profile;
import market.book.entity.common.Address;
import market.book.entity.type.RoleType;
import market.book.common.exception.BusinessException;
import market.book.repository.member.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileStore fileStore;
    @Transactional
    public void create(MemberSaveDto memberSaveDto) {
        Optional<Member> optional = memberRepository.findByEmail(memberSaveDto.getEmail());

        // 회원 이메일이 중복일 경우
        if(optional.isPresent()) {
            throw new BusinessException("사용할 수 없는 이메일입니다");
        }

        // 회원 생성
        Member member = new Member(memberSaveDto.getUsername(),
                memberSaveDto.getEmail(),
                memberSaveDto.getNickname(),
                passwordEncoder.encode(memberSaveDto.getPassword()),
                new Address(memberSaveDto.getZoneCode(),
                        memberSaveDto.getSubAddress(),
                        memberSaveDto.getDetailedAddress()),
                new Profile());
        member.addAuthority(new Authority(RoleType.ROLE_USER)); // 유저 권한 생성

        // 회원 저장, CASCADE 로 인하여 Authority, Profile 저장
        memberRepository.save(member);
    }

    @Transactional
    public void update(Long memberId, MemberModifyDto memberModifyDto) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다"));

        // 변경 감지
        member.changeUsername(memberModifyDto.getUsername()); // 이름
        member.changeNickname(memberModifyDto.getNickname()); // 닉네임
        member.changeAddress(new Address(memberModifyDto.getZoneCode(), // 우편번호
                        memberModifyDto.getSubAddress(), // 주소
                        memberModifyDto.getDetailedAddress())); // 세부주소

        Profile profile = member.getProfile();
        profile.changeStateMessage(memberModifyDto.getStateMessage()); // 프로필 상태 메세지

        if(!ObjectUtils.isEmpty(memberModifyDto.getImageFile())) {
            MultipartFile imageMultipartFile = memberModifyDto.getImageFile();
            // 파일을 등록했을 경우
            if(!imageMultipartFile.isEmpty()) {
                try {
                    String storedImageFileUrl = profile.getImageUrl();

                    // 파일 등록
                    UploadFile uploadFile = fileStore.storeFile(imageMultipartFile);
                    profile.changeImageUrl(uploadFile.getStoreFileName());

                    // 기존 파일 삭제
                    if(StringUtils.hasText(storedImageFileUrl)) {
                        // 기존 파일이 등록되어 있으면 삭제
                        if(!fileStore.removeFile(storedImageFileUrl)) {
                            throw new BusinessException("파일 삭제 실패");
                        }
                    }
                } catch (IOException e) {
                    throw new BusinessException("파일을 찾을 수 없습니다");
                }
            }
        }
    }
}
