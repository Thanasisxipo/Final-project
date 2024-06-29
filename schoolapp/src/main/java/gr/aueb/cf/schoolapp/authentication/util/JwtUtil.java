package gr.aueb.cf.schoolapp.authentication.util;

import gr.aueb.cf.schoolapp.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for working with JSON Web Tokens (JWTs).
 * Provides methods for generating, extracting information from,
 * and validating JWTs.
 */
@Service
public class JwtUtil {

    private final SecretKey secretKey = getSigningKey();

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the given JWT token using a claims resolver function.
     *
     * @param token the JWT token
     * @param claimsResolver a function to resolve the desired claim
     * @param <T> the type of the claim
     * @return the claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the role from the given JWT token.
     *
     * @param token the JWT token
     * @return the role
     */
    public Role extractRole(String token) {
        Claims claims = extractAllClaims(token);
        String roleString = claims.get("role", String.class);
        return Role.valueOf(roleString); // Convert String to Role enum
    }

    /**
     * Generates a JWT token for the given username and role.
     *
     * @param username the username
     * @param role the role
     * @return the generated JWT token
     */
    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token the JWT token
     * @param userDetails the user details
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates a signing key for HS256 algorithm.
     *
     * @return the secret key
     */
    private SecretKey getSigningKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
