package roomescape.auth.presentation.dto.request;

public record LoginRequest(String email, String password) {

    public LoginRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈 값이 될 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 빈 값이 될 수 없습니다.");
        }
    }
}
