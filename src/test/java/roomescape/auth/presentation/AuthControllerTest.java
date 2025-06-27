package roomescape.auth.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.TestConstant;
import roomescape.TestFixture;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 로그인_요청을_보내고_성공_시_200과_함께_응답한다() {
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME))
                .when()
                    .post("/members")
                .then()
                    .statusCode(201);

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD))
                .when()
                    .post("/login")
                .then()
                    .statusCode(200)
                    .header(HttpHeaders.SET_COOKIE, startsWith("token=ey"));
    }

    @Test
    void 가입되어있지_않은_아이디나_비밀번호로_로그인_요청_시_400과_함께_응답한다() {
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest("invalid@email.com", "invalid_password"))
                .when()
                    .post("/login")
                .then()
                    .statusCode(400);
    }

    @Test
    void 로그인된_상태인지_검사하는_요청을_보내고_로그인이_되어있다면_200과_함께_응답한다() {
        String token = TestFixture.loginAndGetNormalToken();
        RestAssured
                .given()
                .cookie("token", token)
                .when()
                .get("/login/check")
                .then()
                .statusCode(200)
                .body("name", equalTo("일반인"));
    }

    @Test
    void 로그인된_상태인지_검사하는_요청을_보내고_로그인이_되어있지_않다면_401과_함께_응답한다() {
        RestAssured
                .given()
                .when()
                .get("/login/check")
                .then()
                .statusCode(401);
    }
}