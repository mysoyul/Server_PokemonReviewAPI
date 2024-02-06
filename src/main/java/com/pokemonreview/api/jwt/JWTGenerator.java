package com.pokemonreview.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JWTGenerator {
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
	private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;


	public String generateToken(Authentication authentication) {
		String username = authentication.getName();

		Date exprireDate = Date.from(Instant.now().plusSeconds(SecurityConstants.JWT_EXPIRATION));
		
		String token = Jwts.builder() //JwtBuilder
				.subject(username)
				.issuedAt(new Date())
				.expiration(exprireDate)
				.signWith(KEY, ALGORITHM)
				.compact();

		System.out.println("New token :"  + token);
		return token;
	}
	public String getUsernameFromJWT(String token){
		Claims claims = Jwts.parser()
				.verifyWith(KEY)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(KEY)
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException(
					"JWT was expired or incorrect",
					ex.fillInStackTrace());
		}
	}
}