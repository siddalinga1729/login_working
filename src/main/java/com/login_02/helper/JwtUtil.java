package com.login_02.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//methods for generating tokens
//validate
//isExp
//util class for JWT
@Component
public class JwtUtil {
	private static final long serialversionUID = -25500158422L;
	public static final long JWT_TOKEN_VALIDITY = 5000* 60 * 60*10;

	private String secret = "siddu";

	// retrieve expiration from jwt token
	public String getUsernameFromToken(String token) throws Exception{
		return getClaimFromToken(token, Claims::getSubject);
	}

	// rerieve expiration time from token
	public Date getExpirationDatefromToken(String token) throws Exception{
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) throws Exception{
		final Date expiration = getExpirationDatefromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());

	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
