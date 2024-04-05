package market.book.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import market.book.common.file.FileStore;
import market.book.dto.member.*;
import market.book.entity.Member;
import market.book.common.exception.BusinessException;
import market.book.entity.Profile;
import market.book.repository.member.ProfileRepository;
import market.book.repository.member.MemberRepository;
import market.book.service.MemberService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final FileStore fileStore;
    @GetMapping("/signup-view")
    public String signupView(@ModelAttribute MemberSaveDto memberSaveDto) {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberSaveDto memberSaveDto,
                         BindingResult bindingResult,
                         Errors errors) {

        if ( bindingResult.hasErrors()) return "member/signup";

        try {
            memberService.create(memberSaveDto);
        } catch(BusinessException e) {
            errors.reject("signUpFail", e.getMessage());
            return "member/signup";
        }

        return "redirect:/members/login-view";
    }

    @GetMapping("/login-view")
    public String loginView(@RequestParam(required = false, defaultValue = "false") Boolean fail,
                            @ModelAttribute MemberLoginDto memberLoginDto,
                            Errors errors) {
        if(fail) {
            // MemberCustomLoginFailHandler 에서 redirect
            errors.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return "member/login";
    }

    @GetMapping("/mypage")
    public String myPageView(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                             Model model) {
        Member member = memberRepository.findById(memberDetailsDto.getMemberId())
                .orElseThrow(() -> new BusinessException("not found member id"));

        MemberMyPageDto memberMyPageDto = MemberMyPageDto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .zoneCode(member.getAddress().getZoneCode())
                .subAddress(member.getAddress().getSubAddress())
                .detailedAddress(member.getAddress().getDetailedAddress())
                .imageUrl(member.getProfile().getImageUrl())
                .stateMessage(member.getProfile().getStateMessage())
                .build();

        model.addAttribute("memberMyPageDto", memberMyPageDto);
        return "member/myPage";
    }

    @GetMapping("/mypage/modify")
    public String myPageUpdateView(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                   Model model) {
        Member member = memberRepository.findById(memberDetailsDto.getMemberId())
                .orElseThrow(() -> new BusinessException("not found member id"));

        MemberModifyDto memberModifyDto = MemberModifyDto.builder()
                .stateMessage(member.getProfile().getStateMessage())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .zoneCode(member.getAddress().getZoneCode())
                .subAddress(member.getAddress().getSubAddress())
                .detailedAddress(member.getAddress().getDetailedAddress())
                .build();

        model.addAttribute("memberModifyDto", memberModifyDto);
        return "member/modify";
    }

    @PostMapping("/mypage/modify")
    public String myPageUpdate(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                               @Valid @ModelAttribute MemberModifyDto memberModifyDto,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "member/modify";
        }

        if(!fileStore.isImageFile(memberModifyDto.getImageFile())) {
            bindingResult.reject("isNotImage", "이미지 파일은 jpg, png, gif 만 가능합니다");
            return "member/modify";
        }

        memberService.update(memberDetailsDto.getMemberId(), memberModifyDto);

        return "redirect:/members/mypage";
    }

    @ResponseBody
    @GetMapping("/profiles/images/{fileName}")
    public ResponseEntity<Resource> profileImage(@PathVariable String fileName,
                                                 HttpServletResponse response) throws IOException {
        Optional<Profile> findProfile = profileRepository.findByImageUrl(fileName);

        // DB 에 존재하는지 확인
        if(findProfile.isPresent()) {
            String imageName = findProfile.get().getImageUrl();
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
