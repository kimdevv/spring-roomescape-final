package roomescape.reservation.business;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestConstant;
import roomescape.reservation.business.dto.request.ThemeCreateRequest;
import roomescape.reservation.database.ReservationRepository;
import roomescape.reservation.database.ReservationTimeRepository;
import roomescape.reservation.exception.DuplicatedThemeException;
import roomescape.reservation.exception.ThemeDoesNotExistException;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

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

    @Test
    void 오늘로부터_일주일_동안_가장_많이_예약된_테마_10개를_불러올_수_있다() {
        // Given
        Theme theme1 = themeService.createTheme(new ThemeCreateRequest("테마1", "테마1 설명", "테마1 썸네일"));
        Theme theme2 = themeService.createTheme(new ThemeCreateRequest("테마2", "테마2 설명", "테마2 썸네일"));
        Theme theme3 = themeService.createTheme(new ThemeCreateRequest("테마3", "테마3 설명", "테마3 썸네일"));
        Theme theme4 = themeService.createTheme(new ThemeCreateRequest("테마4", "테마4 설명", "테마4 썸네일"));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(TestConstant.FUTURE_TIME));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now(), time, theme3));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().minusDays(1), time, theme3));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().minusDays(2), time, theme3));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().minusDays(3), time, theme3));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now(), time, theme4));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(1), time, theme4));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(2), time, theme4));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().minusDays(1), time, theme4));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().minusDays(2), time, theme4));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now(), time, theme2));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(1), time, theme1));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(2), time, theme1));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(3), time, theme1));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(4), time, theme1));
        reservationRepository.save(new Reservation(TestConstant.MEMBER_NAME, LocalDate.now().plusDays(5), time, theme1));

        // When & Then
        assertThat(themeService.findPopularThemes()).containsExactly(theme3, theme4, theme2);
    }
}