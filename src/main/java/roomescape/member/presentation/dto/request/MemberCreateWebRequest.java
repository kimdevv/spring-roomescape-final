package roomescape.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberCreateWebRequest(
        @Schema(description = "멤버의 이메일") String email,
        @Schema(description = "멤버의 비밀번호") String password,
        @Schema(description = "멤버의 이름") String name) {

    public MemberCreateWebRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈 값이 될 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 빈 값이 될 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈 값이 될 수 없습니다.");
        }
    }
}
