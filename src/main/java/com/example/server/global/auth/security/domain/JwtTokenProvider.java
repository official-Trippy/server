package com.example.server.global.auth.security.domain;

import com.example.server.domain.member.domain.Member;
import com.example.server.domain.member.repository.MemberRepository;
import com.example.server.global.apiPayload.code.status.ErrorStatus;
import com.example.server.global.apiPayload.exception.handler.ErrorHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("${jwt.access.expiration_hour}")
	private long ACCESS_TOKEN_EXPIRE_HOUR;
	@Value("${jwt.refresh.expiration_date}")
	private long REFRESH_TOKEN_EXPIRE_DATE;
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.secret_email}")
	private String secretEmail;

	private static final String MEMBER_ID_CLAIM = "memberId";
	private static final String REFRESH_TOKEN_CLAIM = "RefreshToken";
	private static final String ACCESS_TOKEN_CLAIM = "AccessToken";
	private static final String EMAIL_AUTH_TOKEN_CLAIM = "EmailAuthToken";
	private static final String REVOKE_TOKEN_CLAIM = "isRevoke";
	private static final String EMAIL_CLAIM = "email";
	private final MemberRepository memberRepository;


	private Key key;

	private Key emailKey;

	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64URL.decode(secret);
		byte[] keyBytes_email = Decoders.BASE64URL.decode(secretEmail);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.emailKey = Keys.hmacShaKeyFor(keyBytes_email);
	}

	public String createEmailAuthToken(String email) {
		Date now = new Date();
		Date authTokenExpiration = new Date(now.getTime() + 30 * 60 * 1000); // 30분

		return Jwts.builder()
				.setSubject(EMAIL_AUTH_TOKEN_CLAIM)
				.claim(EMAIL_CLAIM, email)
				.claim(REVOKE_TOKEN_CLAIM, false)
				.setIssuedAt(now)
				.setExpiration(authTokenExpiration)
				.signWith(emailKey, SignatureAlgorithm.HS256)
				.compact();
	}

	// Jwt 토큰을 복호화하여 이메일 정보가 유효한지 검증하는 메서드
	public boolean isValidEmailAuthToken(String accessToken, String email) {
		// Jwt 토큰 복호화
//		accessToken = "Bearer " + accessToken;
		Claims claims = parseEmailClaims(accessToken);

		if (!claims.getSubject().equals(EMAIL_AUTH_TOKEN_CLAIM) || claims.get(EMAIL_CLAIM) == null) {
			return false;
		}

		if (!validateEmailToken(accessToken, emailKey)) {
			return false;
		}

		String memberId = (String) claims.get(EMAIL_CLAIM);
		return memberId.equals(email);
	}

	public JwtToken createToken(Authentication authentication) {
		String accessToken = createAccessToken(authentication);
		String refreshToken = createRefreshToken(authentication);

		return JwtToken.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	public String createRefreshToken(Authentication authentication) {
		Date now = new Date();
		Date refreshTokenExpiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_DATE * 24 * 60 * 60 * 1000);
//		Date refreshTokenExpiration = new Date(now.getTime() +  60 * 1000); // For test 1분

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(REFRESH_TOKEN_CLAIM)
				.setIssuedAt(now)
				.claim(MEMBER_ID_CLAIM, user.getMemberId())
				.claim(REVOKE_TOKEN_CLAIM, false)
				.setExpiration(refreshTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	public String createRefreshToken(String memberId) {
		Date now = new Date();
		Date refreshTokenExpiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_DATE * 24 * 60 * 60 * 1000);
//		Date refreshTokenExpiration = new Date(now.getTime() +  60 * 1000); // For test 1분

		return Jwts.builder()
				.setSubject(REFRESH_TOKEN_CLAIM)
				.setIssuedAt(now)
				.claim(MEMBER_ID_CLAIM, memberId)
				.claim(REVOKE_TOKEN_CLAIM, false)
				.setExpiration(refreshTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	public String createAccessToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		Date now = new Date();
		Date accessTokenExpiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_HOUR * 60 * 60 * 1000);
//		Date accessTokenExpiration = new Date(now.getTime() +  60 * 1000); // For test 1분

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(ACCESS_TOKEN_CLAIM)
				.claim("auth", authorities)
				.claim(MEMBER_ID_CLAIM, user.getMemberId())
				.setIssuedAt(now)
				.setExpiration(accessTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	public String reIssueAccessToken(String memberId) {
		Date now = new Date();
		Date accessTokenExpiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_HOUR * 60 * 60 * 1000);
//		Date accessTokenExpiration = new Date(now.getTime() +  60 * 1000); // For test 1분

		Member member = memberRepository.findByMemberId(memberId)
				.orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

		String authorities = AuthorityUtils.createAuthorityList(member.getRole().toString())
				.stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return Jwts.builder()
				.setSubject(ACCESS_TOKEN_CLAIM)
				.claim("auth", authorities)
				.claim(MEMBER_ID_CLAIM, memberId)
				.setIssuedAt(now)
				.setExpiration(accessTokenExpiration)
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	// Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public Authentication getAuthentication(String accessToken) {
		// Jwt 토큰 복호화
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}
		if (claims.get("memberId") == null) {
			throw new RuntimeException("유저 정보가 없는 토큰입니다.");
		}

		// 클레임에서 권한 정보 가져오기
		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// UserDetails 객체를 만들어서 Authentication return
		// UserDetails: interface, User: UserDetails를 구현한 class
		String memberId = (String) claims.get(MEMBER_ID_CLAIM);
		Member member = memberRepository.findByMemberId(memberId).orElse(null);;
		UserDetails principal = CustomUserDetails.create(member);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	// 토큰 정보를 검증하는 메서드
	public boolean validateToken(String token) {
		try {
			Claims claims = parseClaims(token);

			Jws<Claims> claimsJws = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token);

//			if (isRevokedToken(token)) {
//				log.info("Revoked JWT Token : " + "member id = " + claims.get("memberId"));
//				return false;
//			}

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token : " +  e.getMessage());
			return false;
		} catch (SignatureException exception) {
			log.error("JWT signature validation fails");
			return false;
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token: " +  e.getMessage());
			return false;
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token: " +  e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.: " +  e.getMessage());
			return false;
		} catch (Exception exception) {
			log.error("JWT validation fails: " +  exception.getMessage());
			return false;
		}
	}

	public boolean validateEmailToken(String token, Key keyType) {
		try {
			Claims claims = parseEmailClaims(token);

			Jws<Claims> claimsJws = Jwts.parserBuilder()
					.setSigningKey(keyType)
					.build()
					.parseClaimsJws(token);

//			if (isRevokedToken(token)) {
//				log.info("Revoked JWT Token : " + "member id = " + claims.get("memberId"));
//				return false;
//			}

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token : " +  e.getMessage());
			return false;
		} catch (SignatureException exception) {
			log.error("JWT signature validation fails");
			return false;
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token: " +  e.getMessage());
			return false;
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token: " +  e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.: " +  e.getMessage());
			return false;
		} catch (Exception exception) {
			log.error("JWT validation fails: " +  exception.getMessage());
			return false;
		}
	}

	// 토큰 만료
	public void revokeToken(String accessToken) {
		Claims claims = parseClaims(accessToken);
		claims.put(REVOKE_TOKEN_CLAIM, true);
	}

	// 폐기된 토큰인지 검사
	public boolean isRevokedToken(String accessToken) {
		Claims claims = parseClaims(accessToken);
		return claims.containsKey(REVOKE_TOKEN_CLAIM) && (boolean) claims.get(REVOKE_TOKEN_CLAIM);
	}

	public long getAccessTokenExpirationTime() {
		return ACCESS_TOKEN_EXPIRE_HOUR * 24 * 60 * 60 * 1000;
	}


	// accessToken
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(accessToken)
					.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	// emailToken
	private Claims parseEmailClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(emailKey)
					.build()
					.parseClaimsJws(accessToken)
					.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

}
