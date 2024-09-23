package doafacil.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import doafacil.dtos.auth.AuthDTO;
import doafacil.dtos.auth.TokenDTO;
import doafacil.entities.User;

@Service
public class AuthService {
	@Value("${doafacil.jwt.expiration}")
	private String expiration;

	@Value("${doafacil.jwt.secret}")
	private String secret;

	@Value("${doafacil.jwt.issuer}")
	private String issuer;

	private final AuthenticationManager authManager;

	public AuthService(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	public TokenDTO authenticate(AuthDTO authForm) throws AuthenticationException {
		try {
			Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.getEmail(), authForm.getPassword()));
			String token = createToken(authentication);

			return new TokenDTO(token);
		} catch (BadCredentialsException e) {
			throw e;//TODO: tratamento de exceções //throw new ForbiddenException("Autenticação não realizada. Por favor, revise o email e a senha informados.");
		}
	}

	private Algorithm createAlgorithm() {
		return Algorithm.HMAC256(secret);
	}

	private String createToken(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		Date todayDate = new Date();
		Date expirationDate = new Date(todayDate.getTime() + Long.parseLong(expiration));
		
		return JWT.create()
				  .withIssuer(issuer)
				  .withExpiresAt(expirationDate)
				  .withSubject(
						  principal.getId()
						           .toString())
				  .sign(this.createAlgorithm());
	}

	public boolean verifyToken(String token) {
		try {
			if (token == null) 
				return false;

			JWT.require(this.createAlgorithm()).withIssuer(issuer).build().verify(token);
			return true;
		} catch (JWTVerificationException exception) {
			return false;
		}
	}

	public Long getUserId(String token) {
		String subject = JWT.require(this.createAlgorithm()).withIssuer(issuer).build().verify(token).getSubject();
		return Long.parseLong(subject);
	}
}
