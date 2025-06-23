package roomescape.auth.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.TestConstant;
import roomescape.member.model.Role;

import static org.assertj.core.api.Assertions.assertThat;

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
}