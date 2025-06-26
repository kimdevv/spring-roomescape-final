package roomescape.member.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberGetWebResponse(
        @Schema(description = "멤버의 기본 키")Long id,
        @Schema(description = "멤버의 이메일") String email,
        @Schema(description = "멤버의 이름")String name) {
}
