package roomescape.member.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.TestConstant;
import roomescape.TestFixture;
import roomescape.member.presentation.dto.request.MemberCreateWebRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 회원가입_요청을_보내고_성공_시_201과_함께_응답한다() {
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME))
                .when()
                    .post("/members")
                .then()
                    .statusCode(201)
                    .body(
                            "email", equalTo(TestConstant.MEMBER_EMAIL),
                            "name", equalTo(TestConstant.MEMBER_NAME)
                    );
    }

    @Test
    void 중복된_이메일으로_회원가입_요청_시_400과_함께_응답한다() {
        회원가입_요청을_보내고_성공_시_201과_함께_응답한다();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME2))
                .when()
                    .post("/members")
                .then()
                    .statusCode(400);
    }

    @Test
    void 저장된_모든_멤버를_조회하는_요청을_보내고_성공_시_200과_함께_응답한다() {
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
                    .body(new MemberCreateWebRequest(TestConstant.MEMBER_EMAIL2, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME2))
                .when()
                    .post("/members")
                .then()
                    .statusCode(201);

        String adminToken = TestFixture.loginAndGetAdminToken();
        RestAssured
                .given()
                    .cookie("token", adminToken)
                .when()
                    .get("/members")
                .then()
                    .statusCode(200)
                    .body("", hasSize(2 + 2));
    }
}