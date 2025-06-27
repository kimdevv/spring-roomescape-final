package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.TestConstant;
import roomescape.TestFixture;
import roomescape.reservation.presentation.dto.request.ThemeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ThemeGetResponse;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 테마_생성_요청을_보내고_성공_시_201과_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ThemeCreateWebRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL))
                .when()
                    .post("/reservations/themes")
                .then()
                    .statusCode(201)
                    .body("name", equalTo(TestConstant.THEME_NAME),
                            "description", equalTo(TestConstant.THEME_DESCRIPTION),
                            "thumbnail", equalTo(TestConstant.THEME_THUMBNAIL)
                    );
    }

    @Test
    void 모든_테마_조회_요청을_보내고_성공_시_200과_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ThemeCreateWebRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL))
                .when()
                    .post("/reservations/themes")
                .then()
                    .statusCode(201);

        RestAssured
                .given()
                    .cookie("token", adminToken)
                .when()
                    .get("/reservations/themes")
                .then()
                    .statusCode(200)
                    .body("", hasSize(1));
    }

    @Test
    void 테마_삭제_요청을_보내고_성공_시_204와_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        ThemeGetResponse themeGetResponse = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ThemeCreateWebRequest(TestConstant.THEME_NAME, TestConstant.THEME_DESCRIPTION, TestConstant.THEME_THUMBNAIL))
                .when()
                    .post("/reservations/themes")
                .then()
                    .statusCode(201)
                .extract()
                    .body().as(ThemeGetResponse.class);

        RestAssured
                .given()
                    .cookie("token", adminToken)
                .when()
                    .delete("/reservations/themes/" + themeGetResponse.id())
                .then()
                    .statusCode(204);
    }
}