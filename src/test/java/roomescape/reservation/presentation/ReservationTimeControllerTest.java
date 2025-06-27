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
import roomescape.reservation.presentation.dto.request.ReservationTimeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetResponse;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 예약시간_생성_요청을_보내고_성공_시_201과_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME))
                .when()
                    .post("/reservations/times")
                .then()
                    .statusCode(201)
                    .body("startAt", equalTo(TestConstant.FUTURE_TIME.toString()));
    }

    @Test
    void 관리자_멤버가_아닌_경우_예약시간_생성_요청을_보내면_403과_함께_응답한다() {
        String normalToken = TestFixture.loginAndGetNormalToken();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", normalToken)
                    .body(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME))
                .when()
                    .post("/reservations/times")
                .then()
                    .statusCode(403);
    }

    @Test
    void 모든_예약시간_조회_요청을_보내고_성공_시_200과_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME))
                .when()
                    .post("/reservations/times")
                .then()
                    .statusCode(201);

        RestAssured
                .given()
                    .cookie("token", adminToken)
                .when()
                    .get("/reservations/times")
                .then()
                    .statusCode(200)
                    .body("", hasSize(1));
    }

    @Test
    void 관리자_멤버가_아닌_경우_모든_예약시간_조회_요청을_보내면_403과_함께_응답한다() {
        String normalToken = TestFixture.loginAndGetNormalToken();
        RestAssured
                .given()
                    .cookie("token", normalToken)
                .when()
                    .get("/reservations/times")
                .then()
                    .statusCode(403);
    }

    @Test
    void 예약시간_삭제_요청을_보내고_성공_시_204와_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        ReservationTimeGetResponse reservationTimeGetResponse = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME))
                .when()
                    .post("/reservations/times")
                .then()
                    .statusCode(201)
                .extract()
                    .as(ReservationTimeGetResponse.class);

        RestAssured
                .given()
                    .cookie("token", adminToken)
                .when()
                    .delete("/reservations/times/" + reservationTimeGetResponse.id())
                .then()
                    .statusCode(204);
    }

    @Test
    void 관리자_멤버가_아닌_경우_예약시간_삭제_요청을_보내면_403과_함께_응답한다() {
        String adminToken = TestFixture.loginAndGetAdminToken();
        ReservationTimeGetResponse reservationTimeGetResponse = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", adminToken)
                    .body(new ReservationTimeCreateWebRequest(TestConstant.FUTURE_TIME))
                .when()
                    .post("/reservations/times")
                .then()
                    .statusCode(201)
                .extract()
                    .as(ReservationTimeGetResponse.class);

        String normalToken = TestFixture.loginAndGetNormalToken();
        RestAssured
                .given()
                    .cookie("token", normalToken)
                .when()
                    .delete("/reservations/times/" + reservationTimeGetResponse.id())
                .then()
                    .statusCode(403);
    }
}