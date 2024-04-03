package market.book;

import market.book.dto.member.MemberDetailsDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils;

import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class BookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

	// 등록자, 수정자를 처리해주는 AuditorAware 스프링 빈 등록
	// 스프링 데이터 JPA 에서는 @CreatedBy, @LastModifiedBy 로 등록된 엔티티가 등록되거나 수정될 때 마다
	// auditorProvider() 를 호출을 통해 결과물을 꺼내간다.
	@Bean
	public AuditorAware<Long> auditorProvider() {
		return () -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication.getPrincipal() instanceof MemberDetailsDto) {
				// 세큐리티 컨텍스트 홀더에 등록된 멤버 id 값
				return Optional.of(((MemberDetailsDto) authentication.getPrincipal()).getMemberId());
			}
			return Optional.empty();
		};
	}
}
