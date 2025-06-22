package roomescape.auth.business;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.member.database.MemberDoesNotExistException;
import roomescape.member.database.MemberRepository;
import roomescape.member.model.Member;
import roomescape.member.model.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void 로그인_성공_시_토큰을_만들어_반환한다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());
        when(jwtProvider.generateToken(anyString(), any())).thenReturn(TestConstant.FAKE_TOKEN);

        // When
        String token = authService.login(loginRequest);

        // Then
        assertThat(token).isEqualTo(TestConstant.FAKE_TOKEN);
    }

    @Test
    void 잘못된_이메일으로_요청한_경우에는_토큰을_만들지_않는다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        LoginRequest loginRequest = new LoginRequest("invalid@email.com", member.getPassword());
        when(jwtProvider.generateToken(anyString(), any())).thenReturn(TestConstant.FAKE_TOKEN);

        // When & Then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(MemberDoesNotExistException.class)
                .hasMessage("잘못된 이메일 혹은 비밀번호입니다.");
    }

    @Test
    void 잘못된_비밀번호로_요청한_경우에는_토큰을_만들지_않는다() {
        // Given
        Member member = memberRepository.save(new Member(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME, Role.NORMAL));
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), "invalidPassword");
        when(jwtProvider.generateToken(anyString(), any())).thenReturn(TestConstant.FAKE_TOKEN);

        // When & Then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(MemberDoesNotExistException.class)
                .hasMessage("잘못된 이메일 혹은 비밀번호입니다.");
    }
}