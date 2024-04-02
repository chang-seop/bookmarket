package market.book.service;

import lombok.RequiredArgsConstructor;
import market.book.dto.member.MemberSaveDto;
import market.book.entity.Member;
import market.book.entity.Profile;
import market.book.entity.common.Address;
import market.book.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void create(MemberSaveDto memberSaveDto) {
        Optional<Member> optional = memberRepository.findByEmail(memberSaveDto.getEmail());

        // 회원 닉네임 중복일 경우
        if(optional.isPresent()) {
            throw new RuntimeException();
        }

        // 회원 생성
        Member member = new Member(memberSaveDto.getUsername(),
                memberSaveDto.getEmail(),
                memberSaveDto.getNickname(),
                memberSaveDto.getPassword(),
                new Address(memberSaveDto.getZoneCode(),
                        memberSaveDto.getJibunAddress(),
                        memberSaveDto.getRoadAddress(),
                        memberSaveDto.getDetailedAddress()),
                new Profile());

        // 회원 저장
        memberRepository.save(member);
    }
}
