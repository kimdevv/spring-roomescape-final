package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestConstant {

    public static final String FAKE_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHJlZUBnbWFpbC5jb20iLCJpYXQiOjE3NTA0OTU5OTgsImV4cCI6MTc1MDQ5OTU5OH0.tkFrxuBodd_xjZSs4cTPMzvxk3FC4h4ZBBN9s2Z6AhY";

    public static final String MEMBER_EMAIL = "phree@gmail.com";
    public static final String MEMBER_PASSWORD = "phreepass1234!";
    public static final String MEMBER_NAME = "프리";
    public static final String MEMBER_NAME2 = "프리2";

    public static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(1);
    public static final LocalTime FUTURE_TIME = LocalTime.now().plusMinutes(2).withNano(0);

    public static final String THEME_NAME = "테마";
    public static final String THEME_DESCRIPTION = "테마의 설명입니다.";
    public static final String THEME_THUMBNAIL = "https://ssl.pstatic.net/melona/libs/1524/1524941/8e44e27eee8d13f9a26b_20250527144532109.jpg";

    public static final Long INVALID_ENTITY_ID = Long.MIN_VALUE;
}
