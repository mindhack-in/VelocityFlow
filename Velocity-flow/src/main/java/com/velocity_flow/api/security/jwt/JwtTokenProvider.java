package com.velocity_flow.api.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration-in-ms}")
	private int jwtExpirationInMs;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();

		// Retrieve permissions to embed in token (optional depending on stateless
		// needs,
		// but helps frontend. For backend security, usually better to load from
		// DB/Cache to allow instant revocation).
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		expiryDate.setMonth(now.getMonth() + 1);
		

		return Jwts.builder().setSubject(username).claim("roles", authorities).setIssuedAt(new Date())
				.setExpiration(expiryDate).signWith(key, SignatureAlgorithm.HS512).compact();
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
			return true;
		} catch (SecurityException | MalformedJwtException ex) {
			// Invalid JWT signature
		} catch (ExpiredJwtException ex) {
			// Expired JWT token
		} catch (UnsupportedJwtException ex) {
			// Unsupported JWT token
		} catch (IllegalArgumentException ex) {
			// JWT claims string is empty
		}
		return false;
	}
}
