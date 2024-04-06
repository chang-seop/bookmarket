package market.book.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.book.entity.Member;
import market.book.repository.member.MemberRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileImageInterceptor implements HandlerInterceptor {
    private final MemberRepository memberRepository;

    /**
     * 컨트롤러 호출 후에 호출된다. (정확히는 핸들러 어댑터 호출 후에 호출된다)
     * TODO: Redis 에 이미지 캐시를 해야지 Open-In-View 를 끌 수 있다.
     * 현재는 repository 에서 조회 후에 지연로딩이 을 통해 이미지를 가져온다.
     * 이미지 -> 로그인시에 redis 에 저장 및 수정 시에 저장 session 제한시간에 맞게
     * 멤버 닉네임 -> SecurityContextHolder 에서 가져오기
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // ModelAndView 가 존재할 경우
        if(!ObjectUtils.isEmpty(modelAndView)) {
            // GET Method 일 경우
            if(request.getMethod().equals(HttpMethod.GET.name())) {
                log.info("=== 프로필 이미지 인터셉트 호출 ===");
                if(!ObjectUtils.isEmpty(request.getUserPrincipal())) {
                    String email = request.getUserPrincipal().getName();
                    if(StringUtils.hasText(email)) {
                        // 사용자 인증이 되어었을 경우
                        Optional<Member> findMember = memberRepository.findFetchProfileByEmail(email);
                        // 사용자가 존재할 경우 모델에 프로필 이미지 등록
                        findMember.ifPresent(member -> {
                            modelAndView.addObject("imageUrl", member.getProfile().getImageUrl());
                            modelAndView.addObject("nickname", member.getNickname());
                        });
                    }
                }
                log.info("=================================");
            }
        }
    }
}
