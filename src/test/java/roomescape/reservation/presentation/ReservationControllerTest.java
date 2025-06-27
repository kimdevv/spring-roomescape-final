package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.TestConstant;
import roomescape.TestFixture;
import roomescape.payment.business.PaymentRestClientManager;
import roomescape.payment.business.dto.request.PaymentApplyRequest;
import roomescape.payment.business.dto.response.PaymentApproveResponse;
import roomescape.payment.model.ProductType;
import roomescape.reservation.model.ReservationStatus;
import roomescape.reservation.presentation.dto.request.ReservationCreateWebRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private PaymentRestClientManager paymentRestClientManager;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        when(paymentRestClientManager.apply(new PaymentApplyRequest(TestConstant.PAYMENT_AMOUNT, TestConstant.ORDER_ID, TestConstant.PAYMENT_KEY, ProductType.RESERVATION, 1L)))
                .thenReturn(new PaymentApproveResponse(TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT));
    }

    @Test
    void 예약_생성_요청을_보내고_성공_시_201과_함께_응답한다() {
        String normalToken = TestFixture.loginAndGetNormalToken();
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .cookie("token", normalToken)
                    .body(new ReservationCreateWebRequest(TestConstant.FUTURE_DATE, TestFixture.createReservationTimeAndGetId(), TestFixture.createThemeAndGetId(), ReservationStatus.RESERVED, TestConstant.PAYMENT_KEY, TestConstant.ORDER_ID, TestConstant.PAYMENT_AMOUNT, TestConstant.PAYMENT_TYPE))
                .when()
                    .post("/reservations")
                .then()
                    .statusCode(201)
                    .body(
                            "member", equalTo("일반인"),
                            "date", equalTo(TestConstant.FUTURE_DATE.toString()),
                            "time", equalTo(TestConstant.FUTURE_TIME.toString()),
                            "theme", equalTo(TestConstant.THEME_NAME),
                            "status", equalTo(ReservationStatus.RESERVED.name())
                    );
    }
}