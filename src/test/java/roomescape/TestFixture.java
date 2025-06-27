package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.reservation.presentation.dto.request.ReservationTimeCreateWebRequest;
import roomescape.reservation.presentation.dto.request.ThemeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetResponse;
import roomescape.reservation.presentation.dto.response.ThemeGetResponse;

public class TestFixture {

    public static String loginAndGetAdminToken() {
        return RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest("admin@admin.com", "admin_password"))
                .when()
                    .post("/login")
                .then()
                .extract()
                    .cookie("token");
    }

    public static String loginAndGetNormalToken() {
        return RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest("normal@normal.com", "normal_password"))
                .when()
                    .post("/login")
                .then()
                .extract()
                    .cookie("token");
    }

    public static Long createThemeAndGetId() {
        ThemeGetResponse themeGetResponse = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", loginAndGetAdminToken())
                    .body(new ThemeCreateWebRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL))
                .when()
                    .post("/reservations/themes")
                .then()
                .extract()
                    .as(ThemeGetResponse.class);
        return themeGetResponse.id();
    }

    public static Long createReservationTimeAndGetId() {
        ReservationTimeGetResponse reservationTimeGetResponse = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", loginAndGetAdminToken())
                    .body(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME))
                .when()
                    .post("/reservations/times")
                .then()
                .extract()
                    .as(ReservationTimeGetResponse.class);
        return reservationTimeGetResponse.id();
    }
}
