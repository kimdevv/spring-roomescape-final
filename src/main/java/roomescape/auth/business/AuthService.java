package roomescape.auth.business;

import org.springframework.stereotype.Service;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.member.database.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequest requestBody) {
        Member member = memberRepository.findByEmailAndPassword(requestBody.email(), requestBody.password())
                .orElseThrow(() -> new MemberDoesNotExistException("잘못된 이메일 혹은 비밀번호입니다."));
        return jwtProvider.generateToken(member.getEmail(), member.getRole());
    }

    public String findMemberNameByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberDoesNotExistException("존재하지 않는 멤버의 이메일입니다."));
        return member.getName();
    }
}
