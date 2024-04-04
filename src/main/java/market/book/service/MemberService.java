package market.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.book.dto.member.MemberSaveDto;
import market.book.entity.Authority;
import market.book.entity.Member;
import market.book.entity.Profile;
import market.book.entity.common.Address;
import market.book.entity.type.RoleType;
import market.book.global.exception.DuplicateEmailException;
import market.book.repository.member.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public void create(MemberSaveDto memberSaveDto) {
        Optional<Member> optional = memberRepository.findByEmail(memberSaveDto.getEmail());

        // 회원 이메일이 중복일 경우
        if(optional.isPresent()) {
            throw new DuplicateEmailException("duplicate Email Exception");
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
}
