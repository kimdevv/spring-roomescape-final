package roomescape.reservation.business;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.business.dto.request.ThemeCreateRequest;
import roomescape.reservation.exception.DuplicatedThemeException;
import roomescape.reservation.model.Theme;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void 테마를_생성할_수_있다() {
        // Given
        ThemeCreateRequest themeCreateRequest = new ThemeCreateRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL);

        // When
        Theme theme = themeService.createTheme(themeCreateRequest);

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(theme.getId()).isNotNull();
            softAssertions.assertThat(theme.getName()).isEqualTo(TestConstant.THEME_NAME);
            softAssertions.assertThat(theme.getDescription()).isEqualTo(TestConstant.THEME_DESCRIPTION);
            softAssertions.assertThat(theme.getThumbnail()).isEqualTo(TestConstant.THEME_THUMBNAIL);
        });
    }

    @Test
    void 중복된_이름의_테마는_생성할_수_없다() {
        // Given
        themeService.createTheme(new ThemeCreateRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));

        // When & Then
        assertThatThrownBy(() -> themeService.createTheme(new ThemeCreateRequest(TestConstant.THEME_NAME, "다른 설명", "다른 주소")))
                .isInstanceOf(DuplicatedThemeException.class)
                .hasMessage("이미 존재하는 테마의 이름입니다.");
    }
}