package ru.gozhan.pronotesapi.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import ru.gozhan.pronotesapi.domain.user.Role;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
public class JwtTokenUtil { // TODO may used in unit tests

    private static final String SECRET = """
            4449a01142f439c579bcb3eee38f8a2150618c54cdd5dd220daae89dd2b0c0a4ab43674bd57ab5a6b34\
            4eab8514eb520085d5edef96a911fba3b94624c5c0fbd93e288d1f3c359550ce2544c99dae3685f0921\
            eeb3c4e78181281a6b7914a0394e5fd0011afcc3b027becb4a78fedc39029c57254d2705a09551630fb\
            3ab5f06db9f1fe7ad2c589d3eabca703bf63133bc12b3fb116e4e4bf51a2ae9dd3606f589ba990eddd8\
            efb6a4abe238c09452736b6a00d80e4bf3d5812bc3d7d680ca30a74335f092436c849c944c316eca2f0\
            02d2f67b7d3b214be68ae641a8474d1a4e57d4356f78c67713f3af6b04010330750900ee59d0216f69d\
            7ad16160ccd5b3""";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Decode and parse claims from the given JWT token.
     *
     * @param token JWT token to decode.
     * @return Claims object containing the token's payload.
     */
    public static Claims decodeToken(final String token) {
        final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static String formatTokenAsJson(final String token) {
        Claims claims = decodeToken(token);
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (Exception e) {
            throw new RuntimeException("Error formatting token as JSON", e);
        }
    }

    /**
     * Extracts the 'id' field from the decoded JWT token.
     *
     * @param token JWT token to decode.
     * @return User ID extracted from the token.
     */
    public static Long getUserId(final String token) {
        return decodeToken(token).get("id", Long.class);
    }

    /**
     * Extracts the 'exp' field from the decoded JWT token.
     *
     * @param token JWT token to decode.
     * @return User ID extracted from the token.
     */
    public static Instant getExpirationInstant(final String token) {
        Date expirationDate = decodeToken(token).getExpiration();
        return expirationDate.toInstant();
    }

    /**
     * Extracts the 'username' field from the decoded JWT token.
     *
     * @param token JWT token to decode.
     * @return Username extracted from the token.
     */
    public static String getUsername(final String token) {
        return decodeToken(token).getSubject();
    }

    /**
     * Extracts the 'roles' field from the decoded JWT token.
     *
     * @param token JWT token to decode.
     * @return Roles extracted from the token.
     */
    @SuppressWarnings("unchecked")
    public static List<Role> getRoles(final String token) {
        List<String> roles = (List<String>) decodeToken(token).get("roles", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .toList();
    }

}
