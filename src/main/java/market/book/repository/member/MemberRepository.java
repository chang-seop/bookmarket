package market.book.repository.member;

import market.book.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    @Query("select m from Member m inner join fetch m.profile p where m.id = :id")
    Optional<Member> findFetchProfileById(Long id);
    @Query("select m from Member m inner join fetch m.profile p where m.email = :email")
    Optional<Member> findFetchProfileByEmail(String email);
}
