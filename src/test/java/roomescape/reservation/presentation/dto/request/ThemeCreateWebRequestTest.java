package roomescape.reservation.presentation.dto.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ThemeCreateWebRequestTest {

    @Test
    void 테마의_이름은_null이_될_수_없다() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new ThemeCreateWebRequest(null, "description", "thumbnail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마의 이름은 빈 칸일 수 없습니다.");
    }

    @Test
    void 테마의_이름은_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new ThemeCreateWebRequest("   ", "description", "thumbnail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마의 이름은 빈 칸일 수 없습니다.");
    }
}