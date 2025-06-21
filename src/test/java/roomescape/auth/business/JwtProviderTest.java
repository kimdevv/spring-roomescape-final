package roomescape.auth.business;

import org.junit.jupiter.api.Test;
import roomescape.TestConstant;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider();

    @Test
    void 이메일으로_토큰을_생성할_수_있다() {
        // Given
        String email = TestConstant.MEMBER_EMAIL;

        // When
        String token = jwtProvider.generateToken(email);

        // Then
        assertThat(token).startsWith("ey");
    }

}