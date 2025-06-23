package roomescape.auth.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.TestConstant;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.member.model.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider();

    @Test
    void 이메일과_권한으로_토큰을_생성할_수_있다() {
        // Given
        String email = TestConstant.MEMBER_EMAIL;
        Role role = Role.NORMAL;

        // When
        String token = jwtProvider.generateToken(email, role);

        // Then
        assertThat(token).startsWith("ey");
    }

    @Test
    void 토큰으로부터_claim을_파싱할_수_있다() {
        // Given
        String email = TestConstant.MEMBER_EMAIL;
        Role role = Role.NORMAL;
        String token = jwtProvider.generateToken(email, role);

        // When & Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(jwtProvider.parseClaim(token, "sub")).isEqualTo(email);
            softAssertions.assertThat(jwtProvider.parseClaim(token, "role")).isEqualTo(role.name());
        });
    }

    @Test
    void 유효기간이_지난_토큰으로부터는_파싱할_수_없다() {
        // Given
        String invalidToken = TestConstant.FAKE_TOKEN;

        // When & Then
        assertThatThrownBy(() -> jwtProvider.parseClaim(invalidToken, "exp"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("세션이 만료되었습니다. 다시 로그인해 주세요.");
    }

    @Test
    void 주어진_토큰이_관리자의_토큰인지_검사한다() {
        // Given
        String adminToken = jwtProvider.generateToken("admin@admin.com", Role.ADMIN);
        String normalToken = jwtProvider.generateToken("normal@normal.com", Role.NORMAL);

        // When & Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(jwtProvider.isAdmin(adminToken)).isTrue();
            softAssertions.assertThat(jwtProvider.isAdmin(normalToken)).isFalse();
        });
    }
}
