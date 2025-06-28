package roomescape.member.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import roomescape.member.model.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    List<Member> findAll();

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
