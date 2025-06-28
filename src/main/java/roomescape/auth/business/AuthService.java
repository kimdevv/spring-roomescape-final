package roomescape.auth.business;

import org.springframework.stereotype.Service;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.member.exception.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest requestBody) {
        Member member = memberRepository.findByEmail(requestBody.email())
                .orElseThrow(() -> new MemberDoesNotExistException("잘못된 이메일 혹은 비밀번호입니다."));
        validatePassword(requestBody.password(), member.getPassword());
        return jwtProvider.generateToken(member.getEmail(), member.getRole());
    }

    private void validatePassword(String rawPassword, String encryptedPassword) {
        if (!passwordEncoder.matches(rawPassword, encryptedPassword)) {
            throw new MemberDoesNotExistException("잘못된 이메일 혹은 비밀번호입니다.");
        }
    }

    public String findMemberNameByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberDoesNotExistException("존재하지 않는 멤버의 이메일입니다."));
        return member.getName();
    }
}
