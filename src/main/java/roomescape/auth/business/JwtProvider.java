package roomescape.auth.business;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.member.model.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String SECRET_KEY = "RESERVATION_PROGRAM_JWT_SECRET_KEY";
    private static final int TOKEN_VALIDITY_MS = 3_600_000;

    public String generateToken(String email, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY_MS);
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(validity)
                .signWith(generateSecretKey())
                .compact();
    }

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public Object parseClaim(String token, String claimName) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(generateSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get(claimName);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new UnauthorizedException("세션이 만료되었습니다. 다시 로그인해 주세요.");
        }
    }

    public boolean isAdmin(String token) {
        return parseClaim(token, "role").equals(Role.ADMIN.name());
    }
}
