package market.book.api;

import lombok.RequiredArgsConstructor;
import market.book.common.exception.BusinessException;
import market.book.api.dto.MemberApiDto;
import market.book.dto.member.MemberDetailsDto;
import market.book.entity.Member;
import market.book.repository.member.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {
    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public ResponseEntity<Object> getMember(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        Member member = memberRepository.findById(memberDetailsDto.getMemberId())
                .orElseThrow(() -> new BusinessException("존재 하지 않은 회원"));

        MemberApiDto result = MemberApiDto.builder()
                .email(member.getEmail())
                .contact(member.getContact())
                .zoneCode(member.getAddress().getZoneCode())
                .subAddress(member.getAddress().getSubAddress())
                .detailedAddress(member.getAddress().getDetailedAddress())
                .build();

        return ResponseEntity.ok()
                .body(result);
    }
}
