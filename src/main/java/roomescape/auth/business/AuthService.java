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
}
