package roomescape.member.business;

import org.springframework.stereotype.Service;
import roomescape.member.business.dto.request.MemberCreateRequest;
import roomescape.member.database.MemberRepository;
import roomescape.member.exception.DuplicatedMemberException;
import roomescape.member.model.Member;
import roomescape.member.model.Role;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createNormalMember(MemberCreateRequest memberCreateRequest) {
        String email = memberCreateRequest.email();
        validateDuplicatedEmail(email);
        return memberRepository.save(new Member(email, memberCreateRequest.password(), memberCreateRequest.name(), Role.NORMAL));
    }

    private void validateDuplicatedEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedMemberException("이미 가입된 이메일입니다.");
        }
    }
}
