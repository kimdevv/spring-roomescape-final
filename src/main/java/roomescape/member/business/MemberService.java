package roomescape.member.business;

import org.springframework.stereotype.Service;
import roomescape.member.database.MemberRepository;
import roomescape.member.exception.DuplicatedMemberException;
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createNormalMember(MemberCreateWebRequest memberCreateWebRequest) {
        String email = memberCreateWebRequest.email();
        validateDuplicatedEmail(email);
        return memberRepository.save(new Member(email, memberCreateWebRequest.password(), memberCreateWebRequest.name(), Role.NORMAL));
    }

    private void validateDuplicatedEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedMemberException("이미 가입된 이메일입니다.");
        }
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}
