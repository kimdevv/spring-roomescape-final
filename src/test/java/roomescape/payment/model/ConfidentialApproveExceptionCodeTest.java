package roomescape.payment.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ConfidentialApproveExceptionCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"INVALID_API_KEY", "INVALID_AUTHORIZE_AUTH", "UNAUTHORIZED_KEY"})
    void 예약_메세지를_숨겨야_하는_에러_코드라면_true를_반환한다(String code) {
        // Given
        // When
        // Then
        assertThat(ConfidentialApproveExceptionCode.isConfidential(code)).isTrue();
    }

    @Test
    void 예약_메세지를_숨기지_않아도_되는_에러_코드는_false를_반환한다() {
        // Given
        String code = "nonConfidentialCode";

        // When & Then
        assertThat(ConfidentialApproveExceptionCode.isConfidential(code)).isFalse();
    }
}