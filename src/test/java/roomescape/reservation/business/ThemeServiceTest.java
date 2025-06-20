package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.business.dto.request.ThemeCreateRequest;
import roomescape.reservation.exception.DuplicatedThemeException;
import roomescape.reservation.exception.ThemeDoesNotExistException;
import roomescape.reservation.model.Theme;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void 저장되어_있는_모든_테마를_조회할_수_있다() {
        // Given
        Theme theme = themeService.createTheme(new ThemeCreateRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));

        // When & Then
        assertThat(themeService.findAllThemes()).containsExactlyInAnyOrder(theme);
    }

    @Test
    void 저장되어_있는_테마를_id로_삭제할_수_있다() {
        // Given
        Theme theme = themeService.createTheme(new ThemeCreateRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL));
        int originalCount = themeService.findAllThemes().size();

        // When
        themeService.deleteThemeById(theme.getId());

        // Then
        assertThat(themeService.findAllThemes().size()).isEqualTo(originalCount - 1);
    }

    @Test
    void 존재하지_않는_테마의_id로는_테마를_삭제할_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> themeService.deleteThemeById(TestConstant.INVALID_ENTITY_ID))
                .isInstanceOf(ThemeDoesNotExistException.class)
                .hasMessage("존재하지 않는 테마 id입니다.");
    }
}