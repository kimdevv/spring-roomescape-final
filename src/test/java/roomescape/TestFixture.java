package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.presentation.dto.request.LoginRequest;

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
}
