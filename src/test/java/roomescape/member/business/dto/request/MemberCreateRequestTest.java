package roomescape.member.business.dto.request;

import org.junit.jupiter.api.Test;
import roomescape.TestConstant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberCreateRequestTest {

    @Test
    void 이메일은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest(null, TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일은 빈 값이 될 수 없습니다.");
    }

    @Test
    void 이메일은_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest("   ", TestConstant.MEMBER_PASSWORD, TestConstant.MEMBER_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일은 빈 값이 될 수 없습니다.");
    }

    @Test
    void 비밀번호는_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest(TestConstant.MEMBER_EMAIL, null, TestConstant.MEMBER_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 비밀번호는_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest(TestConstant.MEMBER_EMAIL, "   ", TestConstant.MEMBER_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 빈 값이 될 수 없습니다.");
    }

    @Test
    void 이름은_null이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값이 될 수 없습니다.");
    }

    @Test
    void 이름은_빈_값이_될_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new MemberCreateRequest(TestConstant.MEMBER_EMAIL, TestConstant.MEMBER_PASSWORD, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값이 될 수 없습니다.");
    }
}