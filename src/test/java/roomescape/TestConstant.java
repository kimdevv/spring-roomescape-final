package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestConstant {

    public static final String FAKE_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHJlZUBnbWFpbC5jb20iLCJpYXQiOjE3NTA0OTU5OTgsImV4cCI6MTc1MDQ5OTU5OH0.tkFrxuBodd_xjZSs4cTPMzvxk3FC4h4ZBBN9s2Z6AhY";

    public static final String MEMBER_EMAIL = "phree@gmail.com";
    public static final String MEMBER_EMAIL2 = "phree2@gmail.com";
    public static final String MEMBER_PASSWORD = "phreepass1234!";
    public static final String MEMBER_NAME = "프리";
    public static final String MEMBER_NAME2 = "프리2";

    public static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(1);
    public static final LocalTime FUTURE_TIME = LocalTime.now().plusMinutes(2).withNano(0);

    public static final String PAYMENT_KEY = "tgen_20250528175227f6y46";
    public static final String PAYMENT_KEY2 = "tgen_20250528175227f6y47";
    public static final String PAYMENT_KEY3 = "tgen_20250528175227f6y48";
    public static final String PAYMENT_KEY4 = "tgen_20250528175227f6y49";
    public static final String PAYMENT_KEY5 = "tgen_20250528175227f6y4a";
    public static final String PAYMENT_KEY6 = "tgen_20250528175227f6y4b";
    public static final String PAYMENT_KEY7 = "tgen_20250528175227f6y4c";
    public static final String PAYMENT_KEY8 = "tgen_20250528175227f6y4d";
    public static final String ORDER_ID = "MC44NjE2MTQzMjcyMzM2";
    public static final String ORDER_ID2 = "MC44NjE2MTQzMjcyMzM3";
    public static final String ORDER_ID3 = "MC44NjE2MTQzMjcyMzM4";
    public static final String ORDER_ID4 = "MC44NjE2MTQzMjcyMzM5";
    public static final String ORDER_ID5 = "MC44NjE2MTQzMjcyMzM6";
    public static final String ORDER_ID6 = "MC44NjE2MTQzMjcyMzM7";
    public static final String ORDER_ID7 = "MC44NjE2MTQzMjcyMzM8";
    public static final String ORDER_ID8 = "MC44NjE2MTQzMjcyMzM9";
    public static final Long PAYMENT_AMOUNT = 1000L;
    public static final String PAYMENT_TYPE = "NORMAL";

    public static final String THEME_NAME = "테마";
    public static final String THEME_DESCRIPTION = "테마의 설명입니다.";
    public static final String THEME_THUMBNAIL = "https://ssl.pstatic.net/melona/libs/1524/1524941/8e44e27eee8d13f9a26b_20250527144532109.jpg";

    public static final Long INVALID_ENTITY_ID = Long.MIN_VALUE;
}
