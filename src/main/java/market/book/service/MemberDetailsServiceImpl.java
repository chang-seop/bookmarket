package market.book.service;

import lombok.RequiredArgsConstructor;
import market.book.dto.member.MemberDetailsDto;
import market.book.entity.Authority;
import market.book.repository.member.MemberRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 사용자 정보가 존재하지 않는 경우 예외 처리
        if(StringUtils.hasText(email)) {
            return memberRepository.findByEmail(email)
                    .map(m ->
                        new MemberDetailsDto(
                                m.getId(),
                                m.getUsername(),
                                m.getEmail(),
                                m.getPassword(),
                                m.getNickname(),
                                m.getAuthorityList()
                                        .stream()
                                        .map(Authority::getRole)
                                        .map(Enum::name).toList()))
                    .orElseThrow(() -> new AuthenticationServiceException(email));
        }

        // 이메일이 존재하지 않은 경우 예외 처리
        else {
            return memberRepository.findByEmail(email)
                    .map(m -> new MemberDetailsDto(
                            m.getId(),
                            m.getUsername(),
                            m.getEmail(),
                            m.getPassword(),
                            m.getNickname(),
                            m.getAuthorityList()
                                    .stream()
                                    .map(Authority::getRole)
                                    .map(Enum::name).toList()))
                    .orElseThrow(() -> new BadCredentialsException(email));
        }
    }
}
