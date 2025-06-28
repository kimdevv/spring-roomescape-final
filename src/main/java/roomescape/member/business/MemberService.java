package roomescape.member.business;

import org.springframework.stereotype.Service;
import roomescape.auth.business.PasswordEncoder;
import roomescape.member.database.MemberRepository;
import roomescape.member.exception.DuplicatedMemberException;
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member createNormalMember(MemberCreateWebRequest memberCreateWebRequest) {
        String email = memberCreateWebRequest.email();
        validateDuplicatedEmail(email);
        String encryptedPassword = passwordEncoder.encode(memberCreateWebRequest.password());
        return memberRepository.save(new Member(email, encryptedPassword, memberCreateWebRequest.name(), Role.NORMAL));
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
